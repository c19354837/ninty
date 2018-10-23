package com.ninty.nativee.lang.invoke;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.LocalVars;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.NiThread;
import com.ninty.runtime.Slot;
import com.ninty.runtime.heap.*;

public class NaMethodHandleNatives {
    private final static String className = "java/lang/invoke/MethodHandleNatives";

    public static void init() {
        NaMethodManager.register(className, "getConstant", "(I)I", new
                getConstant());
        NaMethodManager.register(className, "resolve", "(Ljava/lang/invoke/MemberName;Ljava/lang/Class;)Ljava/lang/invoke/MemberName;", new
                resolve());
        NaMethodManager.register(className, "init", "(Ljava/lang/invoke/MemberName;Ljava/lang/Object;)V", new
                init());
    }

    public static class getConstant implements INativeMethod {

        @Override
        public void invoke(NiFrame frame) {
            int which = frame.getLocalVars().getInt(0);
            frame.getOperandStack().pushInt(which == 4 ? 1 : 0);
        }
    }

    public static class resolve implements INativeMethod {

        @Override
        public void invoke(NiFrame frame) {
            NiObject memberName = frame.getLocalVars().getRef(0);
            NiObject caller = frame.getLocalVars().getRef(1);
            if (caller == null) {
                caller = memberName.getFieldRef("clazz", "Ljava/lang/Class;");
            }
            // TODO: getMethod with name and desc
//            NiObject type = memberName.getFieldRef("type", "Ljava/lang/Object;");
//            NiObject rtypes = type.getFieldRef("rtype", "Ljava/lang/Class;");
//            NiObject ptypes = type.getFieldRef("ptypes", "[Ljava/lang/Class;");
            NiClass callerClz = caller.getClzByExtra();
            int flags = memberName.getFieldInt("flags");
            String name = NiString.getString(memberName.getFieldRef("name", "Ljava/lang/String;"));
            memberName.setFieldInt("flags", flags | callerClz.getMethodByName(name).getAccessFlags());
            frame.getOperandStack().pushRef(memberName);
        }
    }

    public static class init implements INativeMethod {

        @Override
        public void invoke(NiFrame frame) {
            LocalVars localVars = frame.getLocalVars();
            NiObject self = localVars.getRef(0);
            NiObject ref = localVars.getRef(1);
            if (ref.getClz().getClassName().equals("java/lang/reflect/Method")) {
                NiObject clazz = ref.getFieldRef("clazz", "Ljava/lang/Class;");
                self.setFieldRef("clazz", "Ljava/lang/Class;", clazz);

                NiObject name = ref.getFieldRef("name", "Ljava/lang/String;");
                self.setFieldRef("name", "Ljava/lang/String;", name);

                NiObject rtype = ref.getFieldRef("returnType", "Ljava/lang/Class;");
                NiObject ptypes = ref.getFieldRef("parameterTypes", "[Ljava/lang/Class;");
                NiObject type = getMethodType(frame, rtype, ptypes);
                self.setFieldRef("type", "Ljava/lang/Object;", type);
                self.setFieldInt("flags", 100728840);
                return;
            }
            throw new UnsupportedOperationException("need to implement : " + ref.getClz().getClassName());
        }

        private NiObject getMethodType(NiFrame frame, NiObject rClz, NiObject pClzs) {
            NiClassLoader loader = frame.getMethod().getClz().getLoader();
            NiClass clzMethodType = loader.loadClass("java/lang/invoke/MethodType");
            NiMethod methodTypeMethod = clzMethodType.getMethod("methodType",
                    "(Ljava/lang/Class;[Ljava/lang/Class;)Ljava/lang/invoke/MethodType;");
            Slot mt = NiThread.execMethodAtCurrent(frame, methodTypeMethod,
                    new Slot(rClz),
                    new Slot(pClzs));
            return mt.getRef();
        }
    }
}
