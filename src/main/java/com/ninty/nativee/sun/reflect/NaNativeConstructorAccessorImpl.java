package com.ninty.nativee.sun.reflect;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.LocalVars;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.NiThread;
import com.ninty.runtime.Slot;
import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiMethod;
import com.ninty.runtime.heap.NiObject;
import com.ninty.utils.VMUtils;

/**
 * Created by ninty on 2018/10/20
 */
public class NaNativeConstructorAccessorImpl {
    private final static String className = "sun/reflect/NativeConstructorAccessorImpl";

    public static void init() {
        NaMethodManager.register(className, "newInstance0", "(Ljava/lang/reflect/Constructor;[Ljava/lang/Object;)Ljava/lang/Object;",
                new newInstance0());
    }

    public static class newInstance0 implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            LocalVars localVars = frame.getLocalVars();
            NiObject constructorObj = localVars.getRef(0);
            NiObject args = localVars.getRef(1);
            NiClass clz = constructorObj.getFieldRef("clazz", "Ljava/lang/Class;").getClzByExtra();
            NiObject params = constructorObj.getFieldRef("parameterTypes", "[Ljava/lang/Class;");
            String desc = VMUtils.toDesc(params, "V");
            NiMethod initMethod = clz.getInitMethod(desc);

            NiObject instance = clz.newObject();

            Slot[] methodArgs = new Slot[(args == null ? 0 : args.aobject().length) + 1];
            methodArgs[0] = new Slot(instance);
            for (int i = 1; i < methodArgs.length; i++) {
                methodArgs[i + 1] = new Slot(args.aobject()[i]);
            }
            NiThread.execMethodDirectly(initMethod, methodArgs);
            frame.getOperandStack().pushRef(instance);
        }
    }

}
