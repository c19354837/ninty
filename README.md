# ninty

看完[《自己动手写Java虚拟机》](https://book.douban.com/subject/26802084/)，原文用 go 实现的，寻思用 Java ~~抄~~写一遍，体会下。

# build

```shell
$ mvn package
$ java -jar ninty.jar       // print help
```

# 总结

俗话说：抄而不思则罔，思而不书则是白思了。有必要总结下。

## 语言

使用 Java 实现，一来最为熟悉，二也是觉得元实现会不会好玩些。

任何选择都是有好也有坏的，如 Java 没有无符号整数（uint32），只能用 long 替代（好像就这一点略显遗憾吧...

## IDE 与工具

既然选择了 Java，那 IDE 选择 idea 也算合理。

因为需要查看 class 文件的字节码，使用了作者写的 [classpy](https://github.com/zxh0/classpy)，非常好用，还顺手帮改了个 bug -_-!

开发与测试使用 `JDK 1.8.0_40`

## 不同之处

虽说是抄了一遍，但也不是无脑抄，比如最后一章我抄了一半就放弃了；也有地方是因为语言不同而没法抄，如解析启动参数部分用了[commons-cli](http://commons.apache.org/proper/commons-cli/)；按着书本写完还进行了一些扩展，实现多线程部分的内容。

### System.out.println()

关于 `System.out.println()` 这个方法，之前章节是用了 hack 的方式，使用的名称匹配的方式实现。最后一章通过实现 native 方法去掉了这个 hack。但我还是沿用了之前的方案来处理这个问题，主要还是发现我要实现的 native 方法远不止书本上的那几个，所以就放弃治疗。

使用名称匹配的方式只能实现基本类型和字符串的打印，我扩展了下，实现对 Object 的打印。具体实现方法上，则是检测到参数为 Object 时，在当前调用栈中压入该 Object 的 `toString()` 方法，并且回退 `position` 位置到执行打印之前，这样当 `toString()` 方法执行完后，Object 已经被替换为 String，此时判断下类型即可完成打印。代码在 [com.ninty.cmd.CmdReferences#print](https://github.com/c19354837/ninty/blob/master/src/main/java/com/ninty/cmd/CmdReferences.java#L58-L66)

### 多线程

从代码来看，线程的启动由 `thread.strat()` 开始，最终调用 `thread.start0()` 的本地方法，根据注释可以知道，该方法将创建线程，并且运行 `thread.run()` 。

所以实现起来则是在 `thread.start0()` 中启动线程，并且将其 `run()` 方法入栈即可。代码在 [com.ninty.nativee.lang.NaThread.start0](https://github.com/c19354837/ninty/blob/master/src/main/java/com/ninty/nativee/lang/NaThread.java#L66-L79)

实际情况则麻烦些，`new Thread()` 将调用 `Thread.currentThread()` 获取当前线程，接着调用 `thread.getThreadGroup()` 。 这两个是关键，这部分由虚拟机提供。在实现上，则是在运行目标代码前，先创建 thread 以及其 threadGroup，并记录下来作为主线程。代码在 [com.ninty.startup.BootStartup#prepare](https://github.com/c19354837/ninty/blob/master/src/main/java/com/ninty/startup/BootStartup.java#L58-L63)

> 小插曲：关于字节码计数我没按书中单独定义变量，直接使用的 `ByteBuffer` 内置的 `position` 代替，这种偷懒行为在实现多线程时差点挂了。
> 
> 多线程情况下各线程应该有独立指令计数器，这样才能保证代码正常执行，但我是用了内置的 `position`，这直接导致同一方法被多线程执行时崩溃。
> 
> 而此时整个程序已经大量使用了 `ByteBuffer`，我首先想到的就是通过继承 `HeapByteBuffer` 实现一个支持多线程的 `ByteBuffer`，不过很快就发现不行（包访问域不让继承）；而直接继承 `ByteBuffer` 需要实现的方法实在是多。最后采用了折中的方法，采用适配器方式对我使用过的方法进行了一遍实现，然后统一替换掉，这样对原代码改动最小，多线程计数部分使用 `ThreadLocal` 处理的。代码在 [com.ninty.runtime.heap.CodeBytes](https://github.com/c19354837/ninty/blob/master/src/main/java/com/ninty/runtime/heap/CodeBytes.java)

### synchronized

分两种情况：

```Java
// 第一种，作为方法修饰符
synchronized void foo(){
    // codes
}

// 第二种，同步方法片段
void bar(){
    synchronized(this){
        // codes
    }
}
```

两种方式在字节码层面是不同的，第一种是在方法修饰符字段进行标识，第二种则是生成 `monitor_enter(0xc2)` 和 `monitor_exit(0xc3)` 两个指令。不过处理方法是一致的，区别只是竞争对象的确定，第一种对于静态方法，则是其 Class，非静态方法则是 this，第二种对象则是指令指定的。

本来想管理竞争对象，后来发现，直接使用 Java 自带的 `ReentrantLock` 把对象锁住就好了，自然就实现了这个效果，算是偷了个懒。代码在 [[第一种]com.ninty.runtime.NiFrame#lockCheck](https://github.com/c19354837/ninty/blob/master/src/main/java/com/ninty/runtime/NiFrame.java#L96-L100)，第二种搜索 monitor 指令即可

### Object.wait() & Object.notify()

wait 和 notify 方法为本地方法，此处直接调用 Java 的即可（又可以偷个懒），需要注意的是 wait 之前需要释放资源，并且被唤起后要竞争资源，竞争成功才算成功唤醒。代码在 [com.ninty.nativee.lang.NaObject](https://github.com/c19354837/ninty/blob/master/src/main/java/com/ninty/nativee/lang/NaObject.java)

### invoke_dynamic (0xba)

先说结论：没有实现。

但还是要一说，毕竟代码都提交了，正所谓：来都来了。

在测完多线程代码后，突然发现可以用 lambda 改写，本以为只是个语法糖，结果掉进深坑。

关于这个指令网上有很多介绍，不过我天资愚笨，始终不得其要领，根据参数和方法，最后肯定了与 `java/lang/invoke.MethodType` 和 `java/lang/invoke/MethodHandle` 有说不清的关系，根据源码一看参数都齐全，索性就自己构造一下，想着应该很容易，结果走在实现各种 native 方法的路上一去不复返，至今没能走出来（不过这过程中倒是修改了不少之前隐藏的 bug，调试起来真是磨人）。代码在 [com.ninty.cmd.CmdReferences.INVOKE_DYNAMIC](https://github.com/c19354837/ninty/blob/master/src/main/java/com/ninty/cmd/CmdReferences.java#L546)

## 最后

看一遍和写一遍还是很不一样的，推荐写一写





