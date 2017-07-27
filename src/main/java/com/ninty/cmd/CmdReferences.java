package com.ninty.cmd;

import com.ninty.cmd.base.Index16Cmd;
import com.ninty.cmd.base.Index8Cmd;
import com.ninty.runtime.LocalVars;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.OperandStack;
import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiField;
import com.ninty.runtime.heap.NiMethod;
import com.ninty.runtime.heap.NiObject;
import com.ninty.runtime.heap.constantpool.ClassRef;
import com.ninty.runtime.heap.constantpool.FieldRef;
import com.ninty.runtime.heap.constantpool.NiConstant;
import com.ninty.runtime.heap.constantpool.NiConstantPool;

/**
 * Created by ninty on 2017/7/27.
 */
public class CmdReferences {

    private static NiConstantPool getCP(NiFrame frame){
        return frame.getMethod().getClz().getCps();
    }
    private static void ldc(NiFrame frame, int index){
        OperandStack stack = frame.getOperandStack();
        NiConstantPool cp = getCP(frame);
        NiConstant constant = cp.get(index);
        if(constant instanceof NiConstant.NiInteger){
            stack.pushInt(((NiConstant.NiInteger) constant).value);
        }else if(constant instanceof NiConstant.NiFloat){
            stack.pushFloat(((NiConstant.NiFloat) constant).value);
        }else if(constant instanceof NiConstant.NiString){
            // TODO
            throw new UnsupportedOperationException("ldc string");
        }else if(constant instanceof ClassRef){
            // TODO
            throw new UnsupportedOperationException("ldc classRef");
        }
    }

    public static class NEW extends Index16Cmd {
        @Override
        public void exec(NiFrame frame) {
            NiConstantPool cps = getCP(frame);
            ClassRef classRef = (ClassRef) cps.get(index);
            classRef.resolve();
            NiClass clz = classRef.getClz();
            if(clz.isInterface() || clz.isAbstract()){
                throw new InstantiationError(clz.toString());
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
            NiField field = fieldRef.getField();

            if(!field.isStatic()){
                throw  new IncompatibleClassChangeError(field.toString());
            }
            if (field.isFinal() &&
                    (curClz != clz || !method.getDesc().equals("<clinit>"))){
                throw new IllegalAccessError("access final field failed");
            }

            String desc = field.getDesc();
            int slotId = field.getSlotId();
            LocalVars slots = clz.getStaticSlots();
            OperandStack stack = frame.getOperandStack();
            switch (desc.charAt(0)){
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
            NiField field = fieldRef.getField();

            if(!field.isStatic()){
                throw  new IncompatibleClassChangeError(field.toString());
            }

            String desc = field.getDesc();
            int slotId = field.getSlotId();
            LocalVars slots = clz.getStaticSlots();
            OperandStack stack = frame.getOperandStack();
            switch (desc.charAt(0)){
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

            if(!field.isStatic()){
                throw  new IncompatibleClassChangeError(field.toString());
            }
            if (field.isFinal() &&
                    (curClz != clz || !method.getDesc().equals("<linit>"))){
                throw new IllegalAccessError("access final field failed");
            }

            String desc = field.getDesc();
            int slotId = field.getSlotId();
            OperandStack stack = frame.getOperandStack();
            switch (desc.charAt(0)){
                case 'Z':
                case 'B':
                case 'C':
                case 'S':
                case 'I':
                    int ival = stack.popInt();
                    NiObject iref = stack.popRef();
                    LocalVars islots = iref.getSlots();
                    islots.setInt(slotId, ival);
                    break;
                case 'F':
                    float fval = stack.popFloat();
                    NiObject fref = stack.popRef();
                    LocalVars fslots = fref.getSlots();
                    fslots.setFloat(slotId, fval);
                    break;
                case 'J':
                    long lval = stack.popLong();
                    NiObject lref = stack.popRef();
                    LocalVars lslots = lref.getSlots();
                    lslots.setLong(slotId, lval);
                    break;
                case 'D':
                    double dval = stack.popDouble();
                    NiObject dref = stack.popRef();
                    LocalVars dslots = dref.getSlots();
                    dslots.setDouble(slotId, dval);
                    break;
                case 'L':
                case '[':
                    NiObject rval = stack.popRef();
                    NiObject rref = stack.popRef();
                    LocalVars rslots = rref.getSlots();
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

            if(!field.isStatic()){
                throw  new IncompatibleClassChangeError(field.toString());
            }

            OperandStack stack = frame.getOperandStack();
            NiObject ref = stack.popRef();
            if(ref == null){
                throw new NullPointerException("can get ref");
            }

            String desc = field.getDesc();
            int slotId = field.getSlotId();
            LocalVars slots =ref.getSlots();
            switch (desc.charAt(0)){
                case 'Z':
                case 'B':
                case 'C':
                case 'S':
                case 'I':
                    stack.pushInt(slots.getInt(index));
                    break;
                case 'F':
                    stack.pushFloat(slots.getFloat(index));
                    break;
                case 'J':
                    stack.pushLong(slots.getLong(index));
                    break;
                case 'D':
                    stack.pushDouble(slots.getDouble(index));
                    break;
                case 'L':
                case '[':
                    stack.pushRef(slots.getRef(index));
                    break;
            }
        }
    }

    public static class INSTANCE_OF extends Index16Cmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            NiObject ref = stack.popRef();
            if(ref == null){
                stack.pushInt(0);
                return;
            }
            NiConstantPool cps = getCP(frame);
            ClassRef classRef = (ClassRef) cps.get(index);
            classRef.resolve();
            NiClass clz = classRef.getClz();
            if(ref.isInstanceOf(clz)){
                stack.pushInt(1);
            }else{
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
            if(ref == null){
                return;
            }
            NiConstantPool cps = getCP(frame);
            ClassRef classRef = (ClassRef) cps.get(index);
            classRef.resolve();
            NiClass clz = classRef.getClz();
            if(!ref.isInstanceOf(clz)){
                throw new ClassCastException(ref.getClz().getClassName() + " can not be cast to "+ clz.getClassName());
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
            if(constant instanceof NiConstant.NiLong){
                stack.pushLong(((NiConstant.NiLong) constant).value);
            }else if(constant instanceof NiConstant.NiDouble){
                stack.pushDouble(((NiConstant.NiDouble) constant).value);
            }else{
                throw new UnsupportedOperationException("ldc classRef");
            }
        }
    }
}
