package com.ninty.cmd;

import com.ninty.cmd.base.ICmdBase;
import com.ninty.cmd.base.Index16Cmd;
import com.ninty.cmd.base.NoOperandCmd;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.OperandStack;
import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiClassLoader;
import com.ninty.runtime.heap.NiObject;
import com.ninty.runtime.heap.constantpool.ClassRef;
import com.ninty.runtime.heap.constantpool.NiConstantPool;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static com.ninty.utils.VMUtils.toUInt;

/**
 * Created by ninty on 2017/7/31.
 */
public class CmdArray {

    private static int getLen(OperandStack stack) {
        int len = stack.popInt();
        if (len < 0) {
            throw new NegativeArraySizeException(len + "");
        }
        return len;
    }

    private static void aload(NiFrame frame) {
        OperandStack stack = frame.getOperandStack();
        int index = stack.popInt();
        NiObject array = stack.popRef();
        nullChech(array);
        Class clz = array.getClass();
        if (clz == byte[].class) {
            stack.pushInt(array.abyte()[index]);
        } else if (clz == short[].class) {
            stack.pushInt(array.ashort()[index]);
        } else if (clz == char[].class) {
            stack.pushInt(array.achar()[index]);
        } else if (clz == int[].class) {
            stack.pushInt(array.aint()[index]);
        } else if (clz == long[].class) {
            stack.pushLong(array.along()[index]);
        } else if (clz == float[].class) {
            stack.pushFloat(array.afloat()[index]);
        } else if (clz == double[].class) {
            stack.pushDouble(array.adouble()[index]);
        } else if (clz == NiObject[].class) {
            stack.pushRef(array.aobject()[index]);
        } else {
            throw new RuntimeException("what happened");
        }
    }

    private static void nullChech(NiObject array) {
        if (array == null) {
            throw new NullPointerException("get null array ref");
        }
    }

    public static class NEW_ARRAY implements ICmdBase {
        private int type;

        @Override
        public void init(ByteBuffer bb) {
            type = bb.get();
        }

        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int len = getLen(stack);
            NiClassLoader loader = frame.getMethod().getClz().getLoader();
            NiClass clz;
            switch (type) {
                case 4:
                    clz = loader.loadClass("[Z");
                    break;
                case 5:
                    clz = loader.loadClass("[C");
                    break;
                case 6:
                    clz = loader.loadClass("[F");
                    break;
                case 7:
                    clz = loader.loadClass("[D");
                    break;
                case 8:
                    clz = loader.loadClass("[B");
                    break;
                case 9:
                    clz = loader.loadClass("[S");
                    break;
                case 10:
                    clz = loader.loadClass("[I");
                    break;
                case 11:
                    clz = loader.loadClass("[J");
                    break;
                default:
                    throw new IllegalAccessError("except for array type:" + type);
            }
            NiObject array = clz.newArray(len);
            stack.pushRef(array);
        }
    }

    public static class ANEW_ARRAY extends Index16Cmd {
        @Override
        public void exec(NiFrame frame) {
            NiConstantPool cps = frame.getMethod().getClz().getCps();
            ClassRef classRef = (ClassRef) cps.get(index);
            classRef.resolve();
            OperandStack stack = frame.getOperandStack();
            int len = getLen(stack);
            classRef.getClz().toArrayClass();
            NiObject array = classRef.getClz().newArray(len);
            stack.pushRef(array);
        }
    }

    public static class ARRAY_LENGTH extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            NiObject array = stack.popRef();
            nullChech(array);
            stack.pushInt(array.arrayLength());
        }
    }

    public static class MULTI_ANEW_ARRAY implements ICmdBase {
        private int index;
        private int dimensions;

        @Override
        public void init(ByteBuffer bb) {
            index = bb.getChar();
            dimensions = toUInt(bb.get());
        }

        @Override
        public void exec(NiFrame frame) {
            NiConstantPool cps = frame.getMethod().getClz().getCps();
            ClassRef classRef = (ClassRef) cps.get(index);
            classRef.resolve();
            OperandStack stack = frame.getOperandStack();
            int[] counts = getCounts(stack);
            NiObject ref =newMultiDimensionalArray(counts, classRef.getClz());
            stack.pushRef(ref);
        }

        private int[] getCounts(OperandStack stack) {
            int[] counts = new int[dimensions];
            for (int i = dimensions - 1; i >= 0; i--) {
                counts[i] = stack.popInt();
                if (counts[i] < 0) {
                    throw new NegativeArraySizeException(String.valueOf(counts[i]));
                }
            }
            return counts;
        }

        private NiObject newMultiDimensionalArray(int[] counts, NiClass clz) {
            int count = counts[0];
            NiObject array = clz.newArray(count);
            if (counts.length > 1) {
                NiObject[] refs = array.aobject();
                for (int i = 0; i < count; i++) {
                    refs[i] = newMultiDimensionalArray(Arrays.copyOfRange(counts, 1, counts.length), clz.componentClass());
                }
            }
            return array;
        }
    }

    public static class AALOAD extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            aload(frame);
        }
    }

    public static class BALOAD extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            aload(frame);
        }
    }

    public static class CALOAD extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            aload(frame);
        }
    }

    public static class SALOAD extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            aload(frame);
        }
    }

    public static class IALOAD extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            aload(frame);
        }
    }

    public static class LALOAD extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            aload(frame);
        }
    }

    public static class FALOAD extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            aload(frame);
        }
    }

    public static class DALOAD extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            aload(frame);
        }
    }

    public static class AASTORE extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            NiObject val = stack.popRef();
            int index = stack.popInt();
            NiObject array = stack.popRef();
            nullChech(array);
            array.aobject()[index] = val;
        }
    }

    public static class BASTORE extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            byte val = (byte) stack.popInt();
            int index = stack.popInt();
            NiObject array = stack.popRef();
            nullChech(array);
            array.abyte()[index] = val;
        }
    }

    public static class CASTORE extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            char val = (char) stack.popInt();
            int index = stack.popInt();
            NiObject array = stack.popRef();
            nullChech(array);
            array.achar()[index] = val;
        }
    }

    public static class SASTORE extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            short val = (short) stack.popInt();
            int index = stack.popInt();
            NiObject array = stack.popRef();
            nullChech(array);
            array.ashort()[index] = val;
        }
    }

    public static class IASTORE extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int val = stack.popInt();
            int index = stack.popInt();
            NiObject array = stack.popRef();
            nullChech(array);
            array.aint()[index] = val;
        }
    }

    public static class LASTORE extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            long val = stack.popLong();
            int index = stack.popInt();
            NiObject array = stack.popRef();
            nullChech(array);
            array.along()[index] = val;
        }
    }

    public static class FASTORE extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            float val = stack.popFloat();
            int index = stack.popInt();
            NiObject array = stack.popRef();
            nullChech(array);
            array.afloat()[index] = val;
        }
    }

    public static class DASTORE extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            double val = stack.popDouble();
            int index = stack.popInt();
            NiObject array = stack.popRef();
            nullChech(array);
            array.adouble()[index] = val;
        }
    }
}
