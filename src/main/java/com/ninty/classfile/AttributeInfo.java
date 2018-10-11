package com.ninty.classfile;

import com.ninty.classfile.constantpool.ConstantInfo;
import com.ninty.classfile.constantpool.ConstantPoolInfos;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/9.
 */
public class AttributeInfo {

    static AttributeInfo generate(ConstantPoolInfos cps, ByteBuffer bb) {
        String name = cps.getUtf8(bb);
        switch (name) {
            case "Code":
                return new AttrCode(cps, bb);
            case "Deprecated":
                return new AttrDeprecated(bb);
            case "Synthetic":
                return new AttrSynthetic(bb);
            case "SourceFile":
                return new AttrSourceFile(cps, bb);
            case "ConstantValue":
                return new AttrConstantValue(bb);
            case "Exceptions":
                return new AttrExceptions(cps, bb);
            case "LineNumberTable":
                return new AttrLineNumberTable(bb);
            case "LocalVariableTable":
                return new AttrLocalVariableTable(cps, bb);
            case "BootstrapMethods":
                return new AttrBootstrapMethods(cps, bb);
            case "Signature":
                return new Signature(cps, bb);
            case "RuntimeVisibleAnnotations":
                return new AnnotationAttr.RuntimeVisibleAnnotations(cps, bb);
            case "RuntimeInvisibleAnnotations":
                return new AnnotationAttr.RuntimeInvisibleAnnotations(cps, bb);
            default:
                return new UnkonwAttr(name, bb);
        }
    }

    void skip(ByteBuffer bb, int len) {
        bb.position(bb.position() + len);
    }

    void skipAttributeLen(ByteBuffer bb) {
        skip(bb, 4);
    }

    static class UnkonwAttr extends AttributeInfo {
        private String name;

        UnkonwAttr(String name, ByteBuffer bb) {
            this.name = name;
            long len = bb.getInt();
            skip(bb, (int) len);
        }
    }

    static class AttrDeprecated extends AttributeInfo {
        AttrDeprecated(ByteBuffer bb) {
            bb.getInt(); // must be 0
        }
    }

    static class AttrSynthetic extends AttributeInfo {
        AttrSynthetic(ByteBuffer bb) {
            bb.getInt(); // must be 0
        }
    }

    public static class AttrSourceFile extends AttributeInfo {
        public String sourceFile;

        AttrSourceFile(ConstantPoolInfos cps, ByteBuffer bb) {
            skipAttributeLen(bb);
            sourceFile = cps.getUtf8(bb);
        }
    }

    public static class AttrConstantValue extends AttributeInfo {
        private int constantValueIndex;

        AttrConstantValue(ByteBuffer bb) {
            skipAttributeLen(bb);
            constantValueIndex = bb.getChar();
        }

        public int getConstantValueIndex() {
            return constantValueIndex;
        }
    }

    static class AttrExceptions extends AttributeInfo {
        String[] exceptionNames;

        AttrExceptions(ConstantPoolInfos cps, ByteBuffer bb) {
            skipAttributeLen(bb);
            int count = bb.getChar();
            exceptionNames = new String[count];
            for (int i = 0; i < count; i++) {
                exceptionNames[i] = cps.getClassName(bb.getChar());
            }
        }
    }

    public static class AttrLineNumberTable extends AttributeInfo {
        public LineNumberTable[] lineNumberTables;

        AttrLineNumberTable(ByteBuffer bb) {
            skipAttributeLen(bb);
            int count = bb.getChar();
            lineNumberTables = new LineNumberTable[count];
            for (int i = 0; i < count; i++) {
                lineNumberTables[i] = new LineNumberTable(bb);
            }
        }
    }

    static class AttrLocalVariableTable extends AttributeInfo {
        LocalVariable[] localVariables;

        AttrLocalVariableTable(ConstantPoolInfos cps, ByteBuffer bb) {
            skipAttributeLen(bb);
            int count = bb.getChar();
            localVariables = new LocalVariable[count];
            for (int i = 0; i < count; i++) {
                localVariables[i] = new LocalVariable(cps, bb);
            }
        }
    }

    public static class AttrCode extends AttributeInfo {
        public int maxStack;
        public int maxLocals;
        public byte[] codes;
        public ExceptionTable[] exceptionTables;
        private AttributeInfo[] attributeInfos;

        AttrCode(ConstantPoolInfos cps, ByteBuffer bb) {
            skipAttributeLen(bb);
            maxStack = bb.getChar();
            maxLocals = bb.getChar();
            codes = new byte[bb.getInt()];
            bb.get(codes);

            int exceptionCount = bb.getChar();
            exceptionTables = new ExceptionTable[exceptionCount];
            for (int i = 0; i < exceptionCount; i++) {
                exceptionTables[i] = new ExceptionTable(bb);
            }

            int attrCount = bb.getChar();
            attributeInfos = new AttributeInfo[attrCount];
            for (int i = 0; i < attrCount; i++) {
                attributeInfos[i] = AttributeInfo.generate(cps, bb);
            }
        }


        public AttrLineNumberTable getAttrLineNumberTable() {
            for (AttributeInfo attr : attributeInfos) {
                if (attr instanceof AttrLineNumberTable) {
                    return (AttrLineNumberTable) attr;
                }
            }
            return null;
        }
    }

    public static class LineNumberTable {
        public int startPC;
        public int lineNumber;

        LineNumberTable(ByteBuffer bb) {
            startPC = bb.getChar();
            lineNumber = bb.getChar();
        }
    }

    private static class LocalVariable {
        int startPC;
        int length;
        String name;
        String desc;
        int index;

        LocalVariable(ConstantPoolInfos cps, ByteBuffer bb) {
            startPC = bb.getChar();
            length = bb.getChar();
            name = cps.getUtf8(bb);
            desc = cps.getUtf8(bb);
            index = bb.getChar();
        }
    }

    public static class ExceptionTable {
        public int startPc;
        public int endPc;
        public int handlerPc;
        public int catchType;

        ExceptionTable(ByteBuffer bb) {
            startPc = bb.getChar();
            endPc = bb.getChar();
            handlerPc = bb.getChar();
            catchType = bb.getChar();
        }
    }

    public static class AttrBootstrapMethods extends AttributeInfo {
        public BootstrapMethodInfo[] bootstarpMethods;

        AttrBootstrapMethods(ConstantPoolInfos cps, ByteBuffer bb) {
            skipAttributeLen(bb);
            int num = bb.getChar();
            bootstarpMethods = new BootstrapMethodInfo[num];
            for (int i = 0; i < num; i++) {
                bootstarpMethods[i] = new BootstrapMethodInfo(cps, bb);
            }
        }
    }

    public static class BootstrapMethodInfo {
        public int bmhIndex;
        public ConstantInfo[] arguments;

        BootstrapMethodInfo(ConstantPoolInfos cps, ByteBuffer bb) {
            bmhIndex = bb.getChar();
            int argNum = bb.getChar();
            arguments = new ConstantInfo[argNum];
            for (int i = 0; i < argNum; i++) {
                int cpIndex = bb.getChar();
                arguments[i] = cps.get(cpIndex);
            }
        }
    }

    public static class Signature extends AttributeInfo {
        public String signature;

        Signature(ConstantPoolInfos cps, ByteBuffer bb) {
            skipAttributeLen(bb);
            int index = bb.getChar();
            signature = cps.getUtf8(index);
        }
    }
}
