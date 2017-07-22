package com.ninty.cmd;

import com.ninty.cmd.base.NoOperandCmd;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.OperandStack;

/**
 * Created by ninty on 2017/7/16.
 */
public class CmdConversions {

    public static class I2L extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int val = stack.popInt();
            stack.pushLong(val);
        }
    }

    public static class I2F extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int val = stack.popInt();
            stack.pushFloat(val);
        }
    }

    public static class I2D extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int val = stack.popInt();
            stack.pushDouble(val);
        }
    }

    public static class L2I extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            long val = stack.popLong();
            stack.pushInt((int) val);
        }
    }

    public static class L2F extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            long val = stack.popLong();
            stack.pushFloat(val);
        }
    }

    public static class L2D extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            long val = stack.popLong();
            stack.pushDouble(val);
        }
    }

    public static class F2I extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            float val = stack.popFloat();
            stack.pushInt((int) val);
        }
    }

    public static class F2L extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            float val = stack.popFloat();
            stack.pushLong((long) val);
        }
    }

    public static class F2D extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            float val = stack.popFloat();
            stack.pushDouble(val);
        }
    }

    public static class D2I extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            double val = stack.popDouble();
            stack.pushInt((int) val);
        }
    }

    public static class D2L extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            double val = stack.popDouble();
            stack.pushLong((long) val);
        }
    }

    public static class D2F extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            double val = stack.popDouble();
            stack.pushFloat((float) val);
        }
    }

    public static class I2B extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int val = stack.popInt();
            stack.pushInt((byte) val);
        }
    }

    public static class I2C extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int val = stack.popInt();
            stack.pushInt((char) val);
        }
    }

    public static class I2S extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int val = stack.popInt();
            stack.pushInt((short) val);
        }
    }

}
