package com.ninty.cmd;

import com.ninty.cmd.base.ICmdBase;
import com.ninty.cmd.base.Index16Cmd;
import com.ninty.cmd.base.Index8Cmd;
import com.ninty.cmd.base.NoOperandCmd;
import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.nativee.lang.NaThrowable;
import com.ninty.runtime.LocalVars;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.NiThread;
import com.ninty.runtime.OperandStack;
import com.ninty.runtime.heap.*;
import com.ninty.runtime.heap.constantpool.*;
import com.sun.jdi.NativeMethodException;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/27.
 */
public class CmdReferences {

    public static void invokeMethod(NiFrame frame, NiMethod method) {
        NiThread thread = frame.getThread();
        NiFrame newFrame = new NiFrame(method);
        thread.pushFrame(newFrame);
        int argsCount = method.getArgsCount();
        OperandStack stack = frame.getOperandStack();
        LocalVars slots = newFrame.getLocalVars();
        if (argsCount > 0) {
            for (int i = argsCount - 1; i >= 0; i--) {
                slots.setSlot(i, stack.popSlot());
            }
        }
        System.out.println("invoke method: " + method);
    }

    private static void print(NiFrame frame, String desc) {
        OperandStack stack = frame.getOperandStack();
        switch (desc) {
            case "(Z)V":
                System.out.println(stack.popInt() != 0);
                break;
            case "(C)V":
                System.out.println((char) stack.popInt());
                break;
            case "(B)V":
                System.out.println((byte) stack.popInt());
                break;
            case "(S)V":
                System.out.println((short) stack.popInt());
                break;
            case "(I)V":
                System.out.println(stack.popInt());
                break;
            case "(J)V":
                System.out.println(stack.popLong());
                break;
            case "(F)V":
                System.out.println(stack.popFloat());
                break;
            case "(D)V":
                System.out.println(stack.popDouble());
                break;
            case "(Ljava/lang/String;)V":
                System.out.println(NiString.getString(stack.popRef()));
                break;
            default:
                NiObject ref = stack.getRefFromTop();
                if (NiString.isString(ref)) {
                    System.out.println(NiString.getString(ref));
                    return;
                }
                frame.restorePostion();
                NiMethod method = ref.getClz().getToStringMethod();
                invokeMethod(frame, method);
        }
    }

    private static NiConstantPool getCP(NiFrame frame) {
        return frame.getMethod().getClz().getCps();
    }

    private static void ldc(NiFrame frame, int index) {
        OperandStack stack = frame.getOperandStack();
        NiClassLoader loader = frame.getMethod().getClz().getLoader();
        NiConstantPool cp = getCP(frame);
        NiConstant constant = cp.get(index);
        if (constant instanceof NiConstant.NiInteger) {
            stack.pushInt(((NiConstant.NiInteger) constant).value);
        } else if (constant instanceof NiConstant.NiFloat) {
            stack.pushFloat(((NiConstant.NiFloat) constant).value);
        } else if (constant instanceof NiConstant.NiStr) {
            stack.pushRef(NiString.newString(loader, ((NiConstant.NiStr) constant).value));
        } else if (constant instanceof ClassRef) {
            ClassRef ref = ((ClassRef) constant);
            ref.resolve();
            NiObject jClass = ref.getClz().getjClass();
            stack.pushRef(jClass);
        } else {
            throw new UnsupportedOperationException("ldc unknow");
        }
    }

    public static class NEW extends Index16Cmd {
        @Override
        public void exec(NiFrame frame) {
            NiConstantPool cps = getCP(frame);
            ClassRef classRef = (ClassRef) cps.get(index);
            classRef.resolve();
            NiClass clz = classRef.getClz();
            if (clz.isInterface() || clz.isAbstract()) {
                throw new InstantiationError(clz.toString());
            }
            if (!clz.isClinit()) {
                frame.restorePostion();
                clz.clinit(frame.getThread());
                return;
            }
            NiObject ref = clz.newObject();
            frame.getOperandStack().pushRef(ref);
        }
    }

    public static class PUTSTATIC extends Index16Cmd {
        @Override
        public void exec(NiFrame frame) {
            NiMethod method = frame.getMethod();
            NiClass curClz = method.getClz();
            NiConstantPool cps = curClz.getCps();
            FieldRef fieldRef = (FieldRef) cps.get(index);
            fieldRef.resolve();

            NiClass clz = fieldRef.getClz();
            if (!clz.isClinit()) {
                frame.restorePostion();
                clz.clinit(frame.getThread());
                return;
            }

            NiField field = fieldRef.getField();
            if (!field.isStatic()) {
                throw new IncompatibleClassChangeError(field.toString());
            }
            if (field.isFinal() &&
                    (curClz != clz || !method.getName().equals("<clinit>"))) {
                throw new IllegalAccessError("access final field failed");
            }

            String desc = field.getDesc();
            int slotId = field.getSlotId();
            LocalVars slots = clz.getStaticSlots();
            OperandStack stack = frame.getOperandStack();
            switch (desc.charAt(0)) {
                case 'Z':
                case 'B':
                case 'C':
                case 'S':
                case 'I':
                    slots.setInt(slotId, stack.popInt());
                    break;
                case 'F':
                    slots.setFloat(slotId, stack.popFloat());
                    break;
                case 'J':
                    slots.setLong(slotId, stack.popLong());
                    break;
                case 'D':
                    slots.setDouble(slotId, stack.popLong());
                    break;
                case 'L':
                case '[':
                    slots.setRef(slotId, stack.popRef());
                    break;
            }
        }
    }

    public static class GETSTATIC extends Index16Cmd {
        @Override
        public void exec(NiFrame frame) {
            NiConstantPool cps = getCP(frame);
            FieldRef fieldRef = (FieldRef) cps.get(index);
            fieldRef.resolve();

            NiClass clz = fieldRef.getClz();
            if (!clz.isClinit()) {
                frame.restorePostion();
                clz.clinit(frame.getThread());
                return;
            }

            NiField field = fieldRef.getField();
            if (!field.isStatic()) {
                throw new IncompatibleClassChangeError(field.toString());
            }

            String desc = field.getDesc();
            int slotId = field.getSlotId();
            LocalVars slots = clz.getStaticSlots();
            OperandStack stack = frame.getOperandStack();
            switch (desc.charAt(0)) {
                case 'Z':
                case 'B':
                case 'C':
                case 'S':
                case 'I':
                    stack.pushInt(slots.getInt(slotId));
                    break;
                case 'F':
                    stack.pushFloat(slots.getFloat(slotId));
                    break;
                case 'J':
                    stack.pushLong(slots.getLong(slotId));
                    break;
                case 'D':
                    stack.pushDouble(slots.getDouble(slotId));
                    break;
                case 'L':
                case '[':
                    stack.pushRef(slots.getRef(slotId));
                    break;
            }
        }
    }

    public static class PUTFIELD extends Index16Cmd {
        @Override
        public void exec(NiFrame frame) {
            NiMethod method = frame.getMethod();
            NiClass curClz = method.getClz();
            NiConstantPool cps = curClz.getCps();
            FieldRef fieldRef = (FieldRef) cps.get(index);
            fieldRef.resolve();
            NiClass clz = fieldRef.getClz();
            NiField field = fieldRef.getField();

            if (field.isStatic()) {
                throw new IncompatibleClassChangeError(field.toString());
            }
            if (field.isFinal() &&
                    (curClz != clz || !method.getName().equals("<init>"))) {
                throw new IllegalAccessError("access final field failed");
            }

            String desc = field.getDesc();
            int slotId = field.getSlotId();
            OperandStack stack = frame.getOperandStack();
            switch (desc.charAt(0)) {
                case 'Z':
                case 'B':
                case 'C':
                case 'S':
                case 'I':
                    int ival = stack.popInt();
                    NiObject iref = stack.popRef();
                    LocalVars islots = iref.getFields();
                    islots.setInt(slotId, ival);
                    break;
                case 'F':
                    float fval = stack.popFloat();
                    NiObject fref = stack.popRef();
                    LocalVars fslots = fref.getFields();
                    fslots.setFloat(slotId, fval);
                    break;
                case 'J':
                    long lval = stack.popLong();
                    NiObject lref = stack.popRef();
                    LocalVars lslots = lref.getFields();
                    lslots.setLong(slotId, lval);
                    break;
                case 'D':
                    double dval = stack.popDouble();
                    NiObject dref = stack.popRef();
                    LocalVars dslots = dref.getFields();
                    dslots.setDouble(slotId, dval);
                    break;
                case 'L':
                case '[':
                    NiObject rval = stack.popRef();
                    NiObject rref = stack.popRef();
                    LocalVars rslots = rref.getFields();
                    rslots.setRef(slotId, rval);
                    break;
            }
        }
    }

    public static class GETFIELD extends Index16Cmd {
        @Override
        public void exec(NiFrame frame) {
            NiConstantPool cps = getCP(frame);
            FieldRef fieldRef = (FieldRef) cps.get(index);
            fieldRef.resolve();
            NiField field = fieldRef.getField();

            if (field.isStatic()) {
                throw new IncompatibleClassChangeError(field.toString());
            }

            OperandStack stack = frame.getOperandStack();
            NiObject ref = stack.popRef();
            if (ref == null) {
                throw new NullPointerException("can get ref");
            }

            String desc = field.getDesc();
            int slotId = field.getSlotId();
            LocalVars slots = ref.getFields();
            switch (desc.charAt(0)) {
                case 'Z':
                case 'B':
                case 'C':
                case 'S':
                case 'I':
                    stack.pushInt(slots.getInt(slotId));
                    break;
                case 'F':
                    stack.pushFloat(slots.getFloat(slotId));
                    break;
                case 'J':
                    stack.pushLong(slots.getLong(slotId));
                    break;
                case 'D':
                    stack.pushDouble(slots.getDouble(slotId));
                    break;
                case 'L':
                case '[':
                    stack.pushRef(slots.getRef(slotId));
                    break;
            }
        }
    }

    public static class INSTANCE_OF extends Index16Cmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            NiObject ref = stack.popRef();
            if (ref == null) {
                stack.pushInt(0);
                return;
            }
            NiConstantPool cps = getCP(frame);
            ClassRef classRef = (ClassRef) cps.get(index);
            classRef.resolve();
            NiClass clz = classRef.getClz();
            if (ref.isInstanceOf(clz)) {
                stack.pushInt(1);
            } else {
                stack.pushInt(0);
            }
        }
    }

    public static class CHECK_CAST extends Index16Cmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            NiObject ref = stack.popRef();
            stack.pushRef(ref);
            if (ref == null) {
                return;
            }
            NiConstantPool cps = getCP(frame);
            ClassRef classRef = (ClassRef) cps.get(index);
            classRef.resolve();
            NiClass clz = classRef.getClz();
            if (!ref.isInstanceOf(clz)) {
                throw new ClassCastException(ref.getClz().getClassName() + " can not be cast to " + clz.getClassName());
            }
        }
    }

    public static class LDC extends Index8Cmd {
        @Override
        public void exec(NiFrame frame) {
            ldc(frame, index);
        }
    }

    public static class LDC_W extends Index16Cmd {
        @Override
        public void exec(NiFrame frame) {
            ldc(frame, index);
        }
    }

    public static class LDC_2W extends Index16Cmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            NiConstantPool cp = getCP(frame);
            NiConstant constant = cp.get(index);
            if (constant instanceof NiConstant.NiLong) {
                stack.pushLong(((NiConstant.NiLong) constant).value);
            } else if (constant instanceof NiConstant.NiDouble) {
                stack.pushDouble(((NiConstant.NiDouble) constant).value);
            } else {
                throw new UnsupportedOperationException("ldc classRef");
            }
        }
    }

    public static class INVOKE_STATIC extends Index16Cmd {
        @Override
        public void exec(NiFrame frame) {
            NiConstantPool cps = frame.getMethod().getClz().getCps();
            MethodRef methodRef = (MethodRef) cps.get(index);
            methodRef.resolve();

            NiClass clz = methodRef.getClz();
            if (!clz.isClinit()) {
                frame.restorePostion();
                clz.clinit(frame.getThread());
                return;
            }

            NiMethod method = methodRef.getMethod();
            if (!method.isStatic()) {
                throw new IncompatibleClassChangeError(method + " is not static");
            }
            invokeMethod(frame, method);
        }
    }

    public static class INVOKE_SPECIAL extends Index16Cmd {
        @Override
        public void exec(NiFrame frame) {
            NiConstantPool cps = frame.getMethod().getClz().getCps();
            MethodRef methodRef = (MethodRef) cps.get(index);
            methodRef.resolve();

            NiMethod method = methodRef.getMethod();
            NiClass clz = methodRef.getClz();
            NiClass c = frame.getMethod().getClz();
            if (method.getName().equals("<init>") && method.getClz() != clz) {
                throw new NoSuchMethodError("should call <init> with same class, except:" + c + ", while:" + clz);
            }
            if (method.isStatic()) {
                throw new IncompatibleClassChangeError(method + " is static");
            }
            NiObject ref = frame.getOperandStack().getRefFromTop(method.getArgsCount());
            if (ref == null) {
                throw new NullPointerException("this cannot be null");
            }

            if (method.isProtected()
                    && c.isSubClass(clz)
                    && !c.isSamePackge(clz)
                    && ref.getClz() != c
                    && !c.isSubClass(ref.getClz())) {
                throw new IllegalAccessError("only self or subclass can access the protected method");
            }

            NiMethod finalMethod = method;
            if (method.isSuper()
                    && c.isSubClass(clz)
                    && !method.getName().equals("<init>")) {
                finalMethod = MethodRef.lookUpMethods(c.getSuperClass(), methodRef.getName(), methodRef.getDesc());
            }

            if (finalMethod == null || finalMethod.isAbstract()) {
                throw new AbstractMethodError();
            }

            invokeMethod(frame, finalMethod);
        }
    }

    public static class INVOKE_VIRTUAL extends Index16Cmd {
        @Override
        public void exec(NiFrame frame) {
            NiConstantPool cps = frame.getMethod().getClz().getCps();
            MethodRef methodRef = (MethodRef) cps.get(index);

            methodRef.resolve();
            NiMethod method = methodRef.getMethod();
            NiClass clz = methodRef.getClz();
            NiClass c = frame.getMethod().getClz();

            if (method.isStatic()) {
                throw new IncompatibleClassChangeError(method + " is static");
            }
            NiObject ref = frame.getOperandStack().getRefFromTop(method.getArgsCount());
            if (ref == null) {
                //hack
                if (methodRef.getName().equals("println")) {
                    print(frame, methodRef.getDesc());
                    return;
                }
                //hack end
                throw new NullPointerException("this cannot be null");
            }

            if (method.isProtected()
                    && c.isSubClass(clz)
                    && !c.isSamePackge(clz)
                    && ref.getClz() != c
                    && !c.isSubClass(ref.getClz())) {
                throw new IllegalAccessError("only self or subclass can access the protected method");
            }

            NiMethod finalMethod = method;
            if (method.isSuper()
                    && c.isSubClass(clz)
                    && !method.getName().equals("<init>")) {
                finalMethod = MethodRef.lookUpMethods(clz, methodRef.getName(), methodRef.getDesc());
            }

            if (finalMethod == null || finalMethod.isAbstract()) {
                throw new AbstractMethodError();
            }

            invokeMethod(frame, finalMethod);
        }
    }

    // TODO vtable
    public static class INVOKE_INTERFACE implements ICmdBase {
        private int index;

        @Override
        public void init(ByteBuffer bb) {
            index = bb.getChar();
            bb.get(); //args count
            bb.get(); //must be 0
        }

        @Override
        public void exec(NiFrame frame) {
            NiConstantPool cps = getCP(frame);
            InterfaceMethodRef methodRef = (InterfaceMethodRef) cps.get(index);
            methodRef.resolve();
            NiMethod method = methodRef.getMethod();
            if (method.isStatic() || method.isPrivate()) {
                throw new IncompatibleClassChangeError();
            }
            NiObject self = frame.getOperandStack().getRefFromTop(method.getArgsCount());
            if (self == null) {
                throw new NullPointerException("this cannot be null");
            }
            if (!self.getClz().isImplements(method.getClz())) {
                throw new IncompatibleClassChangeError();
            }
            NiMethod finalMethod = MethodRef.lookUpMethods(frame.getMethod().getClz(), methodRef.getName(), methodRef.getDesc());
            if (finalMethod == null || finalMethod.isAbstract()) {
                throw new AbstractMethodError();
            }
            if (!method.isPublic()) {
                throw new IllegalAccessError(" interface method should be public: " + method);
            }
            invokeMethod(frame, finalMethod);
        }
    }

    public static class INVOKE_NATIVE extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            NiMethod method = frame.getMethod();
            INativeMethod nativeMethod = NaMethodManager.findNativeMethod(method);
            if (nativeMethod == null) {
                throw new NativeMethodException("cannot found native method: " + method);
            }
            nativeMethod.invoke(frame);
        }
    }

    public static class ATHROW extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            NiObject exception = frame.getOperandStack().popRef();
            if (exception == null) {
                throw new NullPointerException("exception is null");
            }

            NiThread thread = frame.getThread();
            while (!thread.isEmpty()) {
                NiFrame topFrame = thread.topFrame();
                int nextPC = topFrame.getMethod().findExceptionHandler(exception.getClz(), frame.getPosition() + 1);
                if (nextPC > 0) {
                    OperandStack stack = topFrame.getOperandStack();
                    stack.clear();
                    stack.pushRef(exception);
                    frame.setPosition(nextPC);
                    return;
                }
                thread.popFrame();
            }
            if(thread.isEmpty()){
                NaThrowable.print(exception);
            }
        }
    }
}

