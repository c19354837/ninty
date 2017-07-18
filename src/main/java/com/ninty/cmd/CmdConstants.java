package com.ninty.cmd;

import com.ninty.cmd.base.ICmdBase;
import com.ninty.cmd.base.NoOperandCmd;
import com.ninty.runtime.NiFrame;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/16.
 */
public class CmdConstants {

    static class NOP extends NoOperandCmd{
    }

    static class ACONST_NULL extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushRef(null);
        }
    }

    static class DCONST_0 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushDouble(0.0);
        }
    }

    static class DCONST_1 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushDouble(1.0);
        }
    }

    static class FCONST_0 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushFloat(0.0f);
        }
    }

    static class FCONST_1 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushFloat(1.0f);
        }
    }

    static class FCONST_2 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushFloat(2.0f);
        }
    }

    static class ICONST_M1 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushInt(-1);
        }
    }

    static class ICONST_0 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushInt(0);
        }
    }

    static class ICONST_1 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushInt(1);
        }
    }

    static class ICONST_2 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushInt(2);
        }
    }

    static class ICONST_3 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushInt(3);
        }
    }

    static class ICONST_4 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushInt(4);
        }
    }

    static class ICONST_5 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushInt(5);
        }
    }

    static class LCONST_0 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushInt(0);
        }
    }

    static class LCONST_1 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().pushInt(1);
        }
    }

    static class BIPUSH implements ICmdBase{
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

    static class SIPUSH implements ICmdBase{
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
