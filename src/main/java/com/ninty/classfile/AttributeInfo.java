package com.ninty.classfile;

import com.ninty.classfile.constantpool.ConstantPoolInfos;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/9.
 */
public class AttributeInfo {

    public static AttributeInfo generate(ConstantPoolInfos cps, ByteBuffer bb){
        int nameIndex = bb.getChar();
        String name = cps.getUtf8(nameIndex);
        switch (name){
            case "Code":
                return new AttrCode(cps, bb);
            case "Deprecated":
                return new AttrDeprecated(bb);
            case "Synthetic":
                return new AttrSynthetic(bb);
            case "SourceFile":
                return new AttrSourceFile(cps, bb);
            case "ConstantValue":
                return new AttrConstantValue(cps, bb);
            case "Exceptions":
                return new AttrExceptions(cps, bb);
            case "LineNumberTable":
                return new AttrLineNumberTable(bb);
            case "LocalVariableTable":
                return new AttrLocalVariableTable(cps, bb);
            default:
                return new UnkonwAttr(bb);
        }
    }

    void skip(ByteBuffer bb, int len){
        bb.position(bb.position() + len);
    }

    void skipAttributeLen(ByteBuffer bb){
        skip(bb, 4);
    }

    static class UnkonwAttr extends AttributeInfo{
        UnkonwAttr(ByteBuffer bb){
            long len = bb.getInt();
            skip(bb, (int) len);
        }
    }

    static class AttrDeprecated extends AttributeInfo{
        AttrDeprecated(ByteBuffer bb){
            bb.getInt(); // must be 0
        }
    }

    static class AttrSynthetic extends AttributeInfo{
        AttrSynthetic(ByteBuffer bb){
            bb.getInt(); // must be 0
        }
    }

    static class AttrSourceFile extends AttributeInfo{
        String sourceFile;

        AttrSourceFile(ConstantPoolInfos cps, ByteBuffer bb){
            skipAttributeLen(bb);
            int sourceFileIndex = bb.getChar();
            sourceFile = cps.getUtf8(sourceFileIndex);
        }
    }

    static class AttrConstantValue extends AttributeInfo{
        String constantValue;

        AttrConstantValue(ConstantPoolInfos cps, ByteBuffer bb){
            skipAttributeLen(bb);
            int constantValueIndex = bb.getChar();
            constantValue = cps.getUtf8(constantValueIndex);
        }
    }

    static class AttrExceptions extends AttributeInfo{
        String[] exceptionNames;

        AttrExceptions(ConstantPoolInfos cps, ByteBuffer bb){
            skipAttributeLen(bb);
            int count = bb.getChar();
            exceptionNames = new String[count];
            for (int i = 0; i < count; i++) {
                exceptionNames[i] = cps.getClassName(bb.getChar());
            }
        }
    }

    static class AttrLineNumberTable extends AttributeInfo{
        LineNumberTable[] lineNumberTables;

        AttrLineNumberTable(ByteBuffer bb){
            skipAttributeLen(bb);
            int count = bb.getChar();
            lineNumberTables = new LineNumberTable[count];
            for (int i = 0; i < count; i++) {
                lineNumberTables[i] = new LineNumberTable(bb);
            }
        }
    }

    static class AttrLocalVariableTable extends AttributeInfo{
        LocalVariable[] localVariables;

        AttrLocalVariableTable(ConstantPoolInfos cps, ByteBuffer bb){
            skipAttributeLen(bb);
            int count = bb.getChar();
            localVariables = new LocalVariable[count];
            for (int i = 0; i < count; i++) {
                localVariables[i] = new LocalVariable(cps, bb);
            }
        }
    }

    static class AttrCode extends AttributeInfo{
        int maxStack;
        int maxLocals;
        byte[] codes;
        ExceptionTable[] exceptionTables;
        AttributeInfo[] attributeInfos;

        AttrCode(ConstantPoolInfos cps, ByteBuffer bb){
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
    }

    private static class LineNumberTable{
        int startPC;
        int lineNumber;

        LineNumberTable(ByteBuffer bb){
            startPC = bb.getChar();
            lineNumber = bb.getChar();
        }
    }

    private static class LocalVariable{
        int startPC;
        int length;
        String name;
        String desc;
        int index;

        LocalVariable(ConstantPoolInfos cps, ByteBuffer bb){
            startPC = bb.getChar();
            length = bb.getChar();
            name = cps.getUtf8(bb.getChar());
            desc = cps.getUtf8(bb.getChar());
            index = bb.getChar();
        }
    }

    private static class ExceptionTable{
        int startPc;
        int endPc;
        int handlerPc;
        int catchType;

        ExceptionTable(ByteBuffer bb){
            startPc = bb.getChar();
            endPc = bb.getChar();
            handlerPc = bb.getChar();
            catchType = bb.getChar();
        }
    }

}