package com.ninty.cmd;

import com.ninty.cmd.base.ICmdBase;
import com.ninty.cmd.base.NoOperandCmd;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.OperandStack;

import java.nio.ByteBuffer;

import static com.ninty.utils.VMUtils.toUInt;

/**
 * Created by ninty on 2017/7/16.
 */
public class CmdMath {

    static class IADD extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int num1 = stack.popInt();
            int num2 = stack.popInt();
            stack.pushInt(num1 + num2);
        }
    }

    static class LADD extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            long num1 = stack.popLong();
            long num2 = stack.popLong();
            stack.pushLong(num1 + num2);
        }
    }

    static class FADD extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            float num1 = stack.popFloat();
            float num2 = stack.popFloat();
            stack.pushFloat(num1 + num2);
        }
    }

    static class DADD extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            double num1 = stack.popDouble();
            double num2 = stack.popDouble();
            stack.pushDouble(num1 + num2);
        }
    }

    static class ISUB extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int num1 = stack.popInt();
            int num2 = stack.popInt();
            stack.pushInt(num1 - num2);
        }
    }

    static class LSUB extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            long num1 = stack.popLong();
            long num2 = stack.popLong();
            stack.pushLong(num1 - num2);
        }
    }

    static class FSUB extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            float num1 = stack.popFloat();
            float num2 = stack.popFloat();
            stack.pushFloat(num1 - num2);
        }
    }

    static class DSUB extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            double num1 = stack.popDouble();
            double num2 = stack.popDouble();
            stack.pushDouble(num1 - num2);
        }
    }

    static class IMUL extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int num1 = stack.popInt();
            int num2 = stack.popInt();
            stack.pushInt(num1 * num2);
        }
    }

    static class LMUL extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            long num1 = stack.popLong();
            long num2 = stack.popLong();
            stack.pushLong(num1 * num2);
        }
    }

    static class FMUL extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            float num1 = stack.popFloat();
            float num2 = stack.popFloat();
            stack.pushFloat(num1 * num2);
        }
    }

    static class DMUL extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            double num1 = stack.popDouble();
            double num2 = stack.popDouble();
            stack.pushDouble(num1 * num2);
        }
    }

    static class IDIV extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int num1 = stack.popInt();
            int num2 = stack.popInt();
            stack.pushInt(num1 / num2);
        }
    }

    static class LDIV extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            long num1 = stack.popLong();
            long num2 = stack.popLong();
            stack.pushLong(num1 / num2);
        }
    }

    static class FDIV extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            float num1 = stack.popFloat();
            float num2 = stack.popFloat();
            stack.pushFloat(num1 / num2);
        }
    }

    static class DDIV extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            double num1 = stack.popDouble();
            double num2 = stack.popDouble();
            stack.pushDouble(num1 / num2);
        }
    }

    static class IREM extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int num1 = stack.popInt();
            int num2 = stack.popInt();
            stack.pushInt(num1 % num2);
        }
    }

    static class LREM extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            long num1 = stack.popLong();
            long num2 = stack.popLong();
            stack.pushLong(num1 % num2);
        }
    }

    static class FREM extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            float num1 = stack.popFloat();
            float num2 = stack.popFloat();
            stack.pushFloat(num1 % num2);
        }
    }

    static class DREM extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            double num1 = stack.popDouble();
            double num2 = stack.popDouble();
            stack.pushDouble(num1 % num2);
        }
    }

    static class INEG extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int num = stack.popInt();
            stack.pushInt(-num);
        }
    }

    static class LNEG extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int num = stack.popInt();
            stack.pushInt(-num);
        }
    }

    static class FNEG extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int num = stack.popInt();
            stack.pushInt(-num);
        }
    }

    static class DNEG extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int num = stack.popInt();
            stack.pushInt(-num);
        }
    }

    static class ISHL extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int shl = stack.popInt();
            int num = stack.popInt();
            stack.pushInt(num << shl);
        }
    }

    static class LSHL extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int shl = stack.popInt();
            long num = stack.popLong();
            stack.pushLong(num << shl);
        }
    }

    static class ISHR extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int shl = stack.popInt();
            int num = stack.popInt();
            stack.pushInt(num >> shl);
        }
    }

    static class LSHR extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int shl = stack.popInt();
            long num = stack.popLong();
            stack.pushLong(num >> shl);
        }
    }

    static class IUSHR extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int shl = stack.popInt();
            int num = stack.popInt();
            stack.pushInt(num >>> shl);
        }
    }

    static class LUSHR extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int shl = stack.popInt();
            long num = stack.popLong();
            stack.pushLong(num >>> shl);
        }
    }

    static class IAND extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int num1 = stack.popInt();
            int num2 = stack.popInt();
            stack.pushLong(num1 & num2);
        }
    }

    static class LAND extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            long num1 = stack.popLong();
            long num2 = stack.popLong();
            stack.pushLong(num1 & num2);
        }
    }

    static class IOR extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int num1 = stack.popInt();
            int num2 = stack.popInt();
            stack.pushLong(num1 | num2);
        }
    }

    static class LOR extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            long num1 = stack.popLong();
            long num2 = stack.popLong();
            stack.pushLong(num1 | num2);
        }
    }

    static class IXOR extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int num1 = stack.popInt();
            int num2 = stack.popInt();
            stack.pushLong(num1 ^ num2);
        }
    }

    static class LXOR extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            long num1 = stack.popLong();
            long num2 = stack.popLong();
            stack.pushLong(num1 ^ num2);
        }
    }

    static class IINC implements ICmdBase {
        int index;
        int inc;

        @Override
        public void init(ByteBuffer bb) {
            index = toUInt(bb.get());
            inc = bb.get();
        }

        @Override
        public void exec(NiFrame frame) {
            int val = frame.getLocalVars().getInt(index);
            frame.getOperandStack().pushInt(val + inc);
        }
    }
}
