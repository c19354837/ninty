package com.ninty.cmd;

import com.ninty.cmd.base.ICmdBase;
import com.ninty.cmd.base.NoOperandCmd;
import com.ninty.runtime.NiFrame;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/16.
 */
public class CmdConstants {

    public static class NOP extends NoOperandCmd {
    }

    public static class ACONST_NULL extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushRef(null);
        }
    }

    public static class DCONST_0 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushDouble(0.0);
        }
    }

    public static class DCONST_1 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushDouble(1.0);
        }
    }

    public static class FCONST_0 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushFloat(0.0f);
        }
    }

    public static class FCONST_1 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushFloat(1.0f);
        }
    }

    public static class FCONST_2 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushFloat(2.0f);
        }
    }

    public static class ICONST_M1 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushInt(-1);
        }
    }

    public static class ICONST_0 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushInt(0);
        }
    }

    public static class ICONST_1 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushInt(1);
        }
    }

    public static class ICONST_2 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushInt(2);
        }
    }

    public static class ICONST_3 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushInt(3);
        }
    }

    public static class ICONST_4 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushInt(4);
        }
    }

    public static class ICONST_5 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushInt(5);
        }
    }

    public static class LCONST_0 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushInt(0);
        }
    }

    public static class LCONST_1 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushInt(1);
        }
    }

    public static class BIPUSH implements ICmdBase {
        int val;

        @Override
        public void init(ByteBuffer bb) {
            val = bb.get();
        }

        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushInt(val);
        }
    }

    public static class SIPUSH implements ICmdBase {
        int val;

        @Override
        public void init(ByteBuffer bb) {
            val = bb.getShort();
        }

        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushInt(val);
        }
    }
}
