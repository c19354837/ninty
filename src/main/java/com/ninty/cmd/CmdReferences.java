package com.ninty.cmd;

import com.ninty.classfile.AttributeInfo;
import com.ninty.classfile.constantpool.ConstantInfo;
import com.ninty.cmd.base.ICmdBase;
import com.ninty.cmd.base.Index16Cmd;
import com.ninty.cmd.base.Index8Cmd;
import com.ninty.cmd.base.NoOperandCmd;
import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.nativee.lang.NaThrowable;
import com.ninty.runtime.*;
import com.ninty.runtime.heap.*;
import com.ninty.runtime.heap.constantpool.*;
import com.ninty.utils.VMUtils;
import com.sun.jdi.NativeMethodException;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/27.
 */
public class CmdReferences {

    public static void invokeMethod(NiFrame frame, NiMethod method) {
        frame.getThread().invokeMethod(method);
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
            if (field.isFinal() && (curClz != clz || !method.getName().equals("<clinit>"))) {
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
            if (field.isFinal() && (curClz != clz || !method.getName().equals("<init>"))) {
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

            if (method.getName().equals("<init>") && method.getClz() != clz) {
                throw new NoSuchMethodError("should call <init> with same class, except:" + method.getClz() + ", while:" + clz);
            }
            if (method.isStatic()) {
                throw new IncompatibleClassChangeError(method + " is static");
            }
            NiObject ref = frame.getOperandStack().getRefFromTop(method.getArgsCount());
            if (ref == null) {
                throw new NullPointerException("this cannot be null");
            }
            NiClass c = ref.getClz();

            if (method.isProtected() && c.isSubClass(clz) && !c.isSamePackge(clz) && ref.getClz() != c && !c
                    .isSubClass(ref.getClz())) {
                throw new IllegalAccessError("only self or subclass can access the protected method");
            }

            NiMethod finalMethod = method;
            if (method.isSuper() && c.isSubClass(clz) && !method.getName().equals("<init>")) {
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
            NiClass c = ref.getClz();

            if (method.isProtected() && c.isSubClass(clz) && !c.isSamePackge(clz) && ref.getClz() != c && !c
                    .isSubClass(ref.getClz())) {
                throw new IllegalAccessError("only self or subclass can access the protected method");
            }

            NiMethod finalMethod = MethodRef.lookUpMethods(c, methodRef.getName(), methodRef.getDesc());
            ;

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
            NiMethod finalMethod = MethodRef.lookUpMethods(self.getClz(), methodRef.getName(), methodRef.getDesc());
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

    public static class INVOKE_DYNAMIC extends Index16Cmd {
        private final static String CLZ_METHOD_HANDLE = "java/lang/invoke/MethodHandle";
        private final static String CLZ_METHOD_TYPE = "java/lang/invoke.MethodType";
        private final static String CLZ_LOOK_UP = "java/lang/invoke/MethodHandles$Lookup";

        @Override
        public void exec(NiFrame frame) {
            NiConstantPool cps = getCP(frame);
            NiConstant.NiInvokeDynamic dynamicInfo = (NiConstant.NiInvokeDynamic) cps.get(index);
            AttributeInfo.BootstrapMethodInfo bootstrapMethodInfo = frame.getMethod().getClz().getBootstrapMethodInfo(dynamicInfo.bmaIndex);
            NiConstant.NiMethodHandleInfo cp = (NiConstant.NiMethodHandleInfo) cps.get(bootstrapMethodInfo.bmhIndex);
            NiConstant handle = cps.get(cp.mhIndex);

            if (handle instanceof MethodRef) {
                MethodRef ref = (MethodRef) handle;
                ref.resolve();
                NiClassLoader loader = frame.getMethod().getClz().getLoader();
                NiMethod method = ref.getMethod();
                NiObject caller = getLookUp(frame);
                NiObject invokedName = NiString.newString(loader, dynamicInfo.name);
                NiObject invokedType = getMethodType(frame, dynamicInfo.desc);
                NiObject samMethodType = getMethodType(frame, ((ConstantInfo.CPMethodType) bootstrapMethodInfo.arguments[0]).desc());
                NiObject instantiatedMethodType = getMethodType(frame, ((ConstantInfo.CPMethodType) bootstrapMethodInfo.arguments[2]).desc());
                ConstantInfo.CPMethodHandleInfo argument = (ConstantInfo.CPMethodHandleInfo) bootstrapMethodInfo.arguments[1];
                MethodRef m = (MethodRef) cps.get(argument.getReference());
                m.resolve();
                NiObject implMethod = getMethodHandle(frame, caller, m.getMethod());

                invoke(frame, method, caller, invokedName, invokedType, samMethodType, implMethod, instantiatedMethodType);
            }
        }

        private NiObject getLookUp(NiFrame frame) {
            NiClassLoader loader = frame.getMethod().getClz().getLoader();
            NiClass clzLookUp = loader.loadClass(CLZ_LOOK_UP);
            NiObject objLookUp = clzLookUp.newObject();
            objLookUp.setFieldRef("lookupClass", "Ljava/lang/Class;", frame.getMethod().getClz().getjClass());
            objLookUp.setFieldInt("allowedModes", clzLookUp.getStaticInt("ALL_MODES"));
            return objLookUp;
        }

        private NiObject getMethodType(NiFrame frame, String desc) {
            NiClassLoader loader = frame.getMethod().getClz().getLoader();
            int index = desc.indexOf(')');
            String paramDesc = desc.substring(1, index);
            String[] params = VMUtils.toParams(paramDesc);
            NiObject[] objP = new NiObject[params.length];
            for (int i = 0; i < params.length; i++) {
                objP[i] = loader.loadClass(params[i]).getjClass();
            }

            NiClass clzA = loader.loadClass("[Ljava/lang/Class");
            NiObject pClzs = new NiObject(clzA, objP);

            String ret = desc.substring(index + 1);
            NiObject rClz = loader.loadClass(VMUtils.toClassname(ret)).getjClass();

            NiClassLoader methodType = frame.getMethod().getClz().getLoader();
            NiClass clzMethodType = loader.loadClass(CLZ_METHOD_TYPE);
            NiObject objMethodType = clzMethodType.newObject();
            objMethodType.setFieldRef("rtype", "Ljava/lang/Class;", rClz);
            objMethodType.setFieldRef("ptypes", "[Ljava/lang/Class;", pClzs);
            return objMethodType;
        }

        private NiObject getMethodHandle(NiFrame frame, NiObject lookUp, NiMethod method) {
            NiClassLoader loader = frame.getMethod().getClz().getLoader();
            NiMethod findStatic = lookUp.getClz().getMethod("findStatic",
                    "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/MethodHandle;");
            Slot ret = NiThread.execMethodDirectly(findStatic,
                    new Slot(lookUp),
                    new Slot(method.getClz().getjClass()),
                    new Slot(NiString.newString(loader, method.getName())),
                    new Slot(getMethodType(frame, method.getDesc())));
            return ret.getRef();
        }

        private void invoke(NiFrame frame, NiMethod method, NiObject... params) {
            NiFrame newFrame = new NiFrame(method);
            frame.getThread().pushFrame(newFrame);
            LocalVars slots = newFrame.getLocalVars();
            for (int i = 0; i < params.length; i++) {
                slots.setRef(i, params[i]);
            }
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
                NiMethod method = topFrame.getMethod();
                if (method == null) {
                    NaThrowable.print(exception);
                    return;
                }
                int nextPC = method.findExceptionHandler(exception.getClz(), topFrame.getPosition() - 1);
                if (nextPC > 0) {
                    OperandStack stack = topFrame.getOperandStack();
                    stack.clear();
                    stack.pushRef(exception);
                    topFrame.setPosition(nextPC);
                    return;
                }
                thread.popFrame();
            }
            if (thread.isEmpty()) {
                NaThrowable.print(exception);
            }
        }
    }
}

