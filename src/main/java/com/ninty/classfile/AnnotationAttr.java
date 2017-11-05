package com.ninty.classfile;

import com.ninty.classfile.constantpool.ConstantInfo;
import com.ninty.classfile.constantpool.ConstantPoolInfos;

import java.nio.ByteBuffer;
import java.util.UnknownFormatConversionException;

public class AnnotationAttr {

    public static ElementValue generateEV(ConstantPoolInfos cps, ByteBuffer bb) {
        byte tag = bb.get();
        switch (tag) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'Z':
            case 's':
                return new ConstEV(cps, bb);
            case 'e':
                return new EnumEV(cps, bb);
            case 'c':
                return new ClassEV(cps, bb);
            case '@':
                return new AnnoEV(cps, bb);
            case '[':
                return new ArrayEV(cps, bb);
            default:
                throw new UnknownFormatConversionException("invalid annation.tag: " + (char) tag);
        }
    }

    public static class RuntimeVisibleAnnotations extends AttributeInfo {
        public AnnotationInfo[] annotationInfos;

        RuntimeVisibleAnnotations(ConstantPoolInfos cps, ByteBuffer bb) {
            skipAttributeLen(bb);
            int num = bb.getChar();
            annotationInfos = new AnnotationInfo[num];
            for (int i = 0; i < num; i++) {
                annotationInfos[i] = new AnnotationInfo(cps, bb);
            }
        }
    }

    public static class RuntimeInvisibleAnnotations extends RuntimeVisibleAnnotations {
        RuntimeInvisibleAnnotations(ConstantPoolInfos cps, ByteBuffer bb) {
            super(cps, bb);
        }
    }

    public static class AnnotationInfo {
        public String type;
        public ElementValuePairs[] evps;

        AnnotationInfo(ConstantPoolInfos cps, ByteBuffer bb) {
            int index = bb.getChar();
            type = cps.getUtf8(index);
            int num = bb.getChar();
            evps = new ElementValuePairs[num];
            for (int i = 0; i < num; i++) {
                evps[i] = new ElementValuePairs(cps, bb);
            }
        }
    }

    public static class ElementValuePairs {
        public String name;
        public ElementValue ev;

        ElementValuePairs(ConstantPoolInfos cps, ByteBuffer bb) {
            name = cps.getUtf8(bb);
            ev = generateEV(cps, bb);
        }
    }

    public interface ElementValue {
    }

    public static class ConstEV implements ElementValue {
        public ConstantInfo constantInfo;

        ConstEV(ConstantPoolInfos cps, ByteBuffer bb) {
            char constIndex = bb.getChar();
            constantInfo = cps.get(constIndex);
        }
    }

    public static class EnumEV implements ElementValue {
        public String typeName;
        public String constName;


        EnumEV(ConstantPoolInfos cps, ByteBuffer bb) {
            typeName = cps.getUtf8(bb);
            constName = cps.getUtf8(bb);
        }
    }

    public static class ClassEV implements ElementValue {
        public String classInfo;

        ClassEV(ConstantPoolInfos cps, ByteBuffer bb) {
            classInfo = cps.getUtf8(bb);
        }
    }

    public static class AnnoEV implements ElementValue {
        public AnnotationInfo anno;

        AnnoEV(ConstantPoolInfos cps, ByteBuffer bb) {
            anno = new AnnotationInfo(cps, bb);
        }
    }

    public static class ArrayEV implements ElementValue {
        public ElementValue[] evs;

        ArrayEV(ConstantPoolInfos cps, ByteBuffer bb) {
            int num = bb.getChar();
            for (int i = 0; i < num; i++) {
                evs[i] = generateEV(cps, bb);
            }
        }
    }
}
