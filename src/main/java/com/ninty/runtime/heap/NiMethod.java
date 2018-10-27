package com.ninty.runtime.heap;

import com.ninty.classfile.AttributeInfo;
import com.ninty.classfile.MemberInfo;
import com.ninty.runtime.heap.constantpool.ClassRef;
import com.ninty.runtime.heap.constantpool.NiConstantPool;
import com.ninty.utils.VMUtils;

/**
 * Created by ninty on 2017/7/23.
 */
public class NiMethod extends ClassMember {
    private static final ExceptionTable[] EMPTY_EX = new ExceptionTable[0];

    private int maxLocals;
    private int maxStack;
    private int argsCount;
    private ExceptionTable[] exceptionTables = EMPTY_EX;
    private AttributeInfo.AttrLineNumberTable lineNumberTable;

    private CodeBytes codes;

    NiMethod(NiClass clz, MemberInfo memberInfo) {
        copyMemberInfo(memberInfo);
        this.clz = clz;
        AttributeInfo.AttrCode attrCode = memberInfo.getAttrCode();
        if (attrCode != null) {
            maxLocals = attrCode.maxLocals;
            maxStack = attrCode.maxStack;
            codes = CodeBytes.wrap(attrCode.codes);

            copyExceptionTable(attrCode.exceptionTables);

            lineNumberTable = attrCode.getAttrLineNumberTable();
        }


        argsCount = calcArgsCount();
        if (isNative()) {
            injectNativeCode(memberInfo.getDesc());
        }
    }

    private void copyExceptionTable(AttributeInfo.ExceptionTable[] exs) {
        exceptionTables = new ExceptionTable[exs.length];
        for (int i = 0; i < exs.length; i++) {
            exceptionTables[i] = new ExceptionTable(exs[i], getClz().getCps());
        }
    }

    private void injectNativeCode(String desc) {
        maxLocals = argsCount;
        maxStack = 4;
        char returnType = desc.charAt(desc.lastIndexOf(')') + 1);
        switch (returnType) {
            case 'V':
                codes = CodeBytes.wrap(new byte[]{(byte) 0xfe, (byte) 0xb1});
                break;
            case 'D':
                codes = CodeBytes.wrap(new byte[]{(byte) 0xfe, (byte) 0xaf});
                break;
            case 'F':
                codes = CodeBytes.wrap(new byte[]{(byte) 0xfe, (byte) 0xae});
                break;
            case 'J':
                codes = CodeBytes.wrap(new byte[]{(byte) 0xfe, (byte) 0xad});
                break;
            case 'L':
            case '[':
                codes = CodeBytes.wrap(new byte[]{(byte) 0xfe, (byte) 0xb0});
                break;
            default:
                codes = CodeBytes.wrap(new byte[]{(byte) 0xfe, (byte) 0xac}); //ireturn
                break;
        }
    }

    private int calcArgsCount() {
        int start = desc.indexOf('(');
        int end = desc.indexOf(')');
        if (start == -1 || end <= start) {
            throw new IllegalArgumentException("bad description for method" + this);
        }
        String args = desc.substring(start + 1, end);
        int argsCount = 0;
        for (int i = 0; i < args.length(); i++) {
            char arg = args.charAt(i);
            if (arg == '[') {
                continue;
            }
            argsCount++;
            if (arg == 'L') {
                i = args.indexOf(';', i);
            } else if (arg == 'J' || arg == 'D') {
                argsCount++;
            }
        }
        if (!isStatic()) {
            argsCount++;
        }
        return argsCount;
    }

    public int findExceptionHandler(NiClass exceptionClz, int pc) {
        for (ExceptionTable exceptionTable : exceptionTables) {
            if (exceptionTable.startPc <= pc && pc < exceptionTable.endPc) {
                if (exceptionTable.catchType == null) { // catch all exception
                    return exceptionTable.handlerPc;
                }

                exceptionTable.catchType.resolve();
                NiClass catchClz = exceptionTable.catchType.getClz();
                if (catchClz == exceptionClz || catchClz.isSubClass(exceptionClz)) {
                    return exceptionTable.handlerPc;
                }
            }
        }
        return 0;// cannot handle this exception
    }

    public int getLineNumber(int pc) {
        if (isNative()) {
            return -2;
        }
        if (lineNumberTable == null) {
            return -1;
        }
        AttributeInfo.LineNumberTable[] lineNumberTables = lineNumberTable.lineNumberTables;
        for (int i = 0; i < lineNumberTables.length; i++) {
            AttributeInfo.LineNumberTable lineNumber = lineNumberTables[i];
            if (pc > lineNumber.startPC) {
                return lineNumber.lineNumber;
            }
        }
        return -1;
    }

    public boolean isAbstract() {
        return (accessFlags & ClassConstant.ACC_ABSTRACT) != 0;
    }

    public int getMaxLocals() {
        return maxLocals;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public int getArgsCount() {
        return argsCount;
    }

    public CodeBytes getCodes() {
        return codes;
    }

    @Override
    public String toString() {
        return "NiMethod{" + "clz=" + clz + ", name='" + name + '\'' + ", desc='" + desc + '\'' + '}';
    }

    public NiObject getParamsType() {
        String desc = getDesc();
        int index = desc.indexOf(')');
        desc = desc.substring(1, index);
        String[] params = VMUtils.toParams(desc);
        NiClassLoader loader = clz.getLoader();
        NiObject[] types = new NiObject[params.length];
        for (int i = 0; i < params.length; i++) {
            types[i] = loader.loadClass(params[i]).getjClass();
        }
        NiClass clzArr = loader.loadClass("[Ljava/lang/Class;");
        return new NiObject(clzArr, types);
    }

    public NiObject getReturnType() {
        String desc = getDesc();
        int index = desc.indexOf(')');
        String returnType = desc.substring(index + 1);
        String returnClassName = VMUtils.toClassname(returnType);
        NiClass returnClz = clz.getLoader().loadClass(returnClassName);
        return returnClz.getjClass();
    }

    // TODO: implement
    public NiObject getExceptionsType() {
        NiClass clzArr = clz.getLoader().loadClass("[Ljava/lang/Class;");
        return clzArr.newArray(0);
    }

    public void setCodes(CodeBytes codes) {
        this.codes = codes;
    }

    private static class ExceptionTable {
        int startPc;
        int endPc;
        int handlerPc;
        ClassRef catchType;

        ExceptionTable(AttributeInfo.ExceptionTable exceptionTable, NiConstantPool cps) {
            startPc = exceptionTable.startPc;
            endPc = exceptionTable.endPc;
            handlerPc = exceptionTable.handlerPc;
            if (exceptionTable.catchType != 0) {
                catchType = (ClassRef) cps.get(exceptionTable.catchType);
            }
        }
    }
}
