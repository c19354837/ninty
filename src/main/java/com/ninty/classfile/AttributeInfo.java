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
                return new AttrLineNumberTable(cps, bb);
            case "LocalVariableTable":
                return new AttrLocalVariableTable(cps, bb);
            default:
                return new UnkonwAttr(bb);
        }
    }

    static class UnkonwAttr extends AttributeInfo{
        UnkonwAttr(ByteBuffer bb){
            long len = bb.getInt();
            bb.position((int) (bb.position() + len));// skip to
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
            bb.getInt(); // must be 2
            int sourceFileIndex = bb.getChar();
            sourceFile = cps.getUtf8(sourceFileIndex);
        }
    }

    static class AttrConstantValue extends AttributeInfo{
        String constantValue;

        AttrConstantValue(ConstantPoolInfos cps, ByteBuffer bb){
            bb.getInt(); // must be 2
            int constantValueIndex = bb.getChar();
            constantValue = cps.getUtf8(constantValueIndex);
        }
    }

    static class AttrExceptions extends AttributeInfo{
        String[] exceptionNames;

        AttrExceptions(ConstantPoolInfos cps, ByteBuffer bb){
            bb.getInt();
            int count = bb.getChar();
            exceptionNames = new String[count];
            for (int i = 0; i < count; i++) {
                exceptionNames[i] = cps.getClassName(bb.getChar());
            }
        }
    }

    static class AttrLineNumberTable extends AttributeInfo{
        LineNumberTable[] lineNumberTables;

        AttrLineNumberTable(ConstantPoolInfos cps, ByteBuffer bb){
            bb.getInt();
            int count = bb.getChar();
            lineNumberTables = new LineNumberTable[count];
            for (int i = 0; i < count; i++) {
                LineNumberTable lineNumber = new LineNumberTable();
                lineNumber.startPC = bb.getChar();
                lineNumber.lineNumber = bb.getChar();
                lineNumberTables[i] = lineNumber;
            }
        }
    }

    static class AttrLocalVariableTable extends AttributeInfo{
        LocalVariable[] localVariables;

        AttrLocalVariableTable(ConstantPoolInfos cps, ByteBuffer bb){
            bb.getInt();
            int count = bb.getChar();
            localVariables = new LocalVariable[count];
            for (int i = 0; i < count; i++) {
                LocalVariable localVariable = new LocalVariable();
                localVariable.startPC = bb.getChar();
                localVariable.length = bb.getChar();
                localVariable.name = cps.getUtf8(bb.getChar());
                localVariable.desc = cps.getUtf8(bb.getChar());
                localVariable.index = bb.getChar();
                localVariables[i] = localVariable;
            }
        }
    }

    static class AttrCode extends AttributeInfo{
        AttrCode(ConstantPoolInfos cps, ByteBuffer bb){

        }
    }

    private static class LineNumberTable{
        int startPC;
        int lineNumber;
    }

    private static class LocalVariable{
        int startPC;
        int length;
        String name;
        String desc;
        int index;
    }

}
