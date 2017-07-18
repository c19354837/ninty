package com.ninty.cmd;

import com.ninty.cmd.base.Index8Cmd;
import com.ninty.cmd.base.NoOperandCmd;
import com.ninty.runtime.NiFrame;

/**
 * Created by ninty on 2017/7/16.
 */
public class CmdStores {
    static void iStore(NiFrame frame, int index) {
        int val = frame.getOperandStack().popInt();
        frame.getLocalVars().setInt(index, val);
    }

    static void lStore(NiFrame frame, int index) {
        long val = frame.getOperandStack().popLong();
        frame.getLocalVars().setLong(index, val);
    }

    static void fStore(NiFrame frame, int index) {
        float val = frame.getOperandStack().popFloat();
        frame.getLocalVars().setFloat(index, val);
    }

    static void dStore(NiFrame frame, int index) {
        double val = frame.getOperandStack().popDouble();
        frame.getLocalVars().setDouble(index, val);
    }

    static void aStore(NiFrame frame, int index) {
        Object ref = frame.getOperandStack().popRef();
        frame.getLocalVars().setRef(index, ref);
    }

    static class ISTORE extends Index8Cmd {
        @Override
        public void exec(NiFrame frame) {
            iStore(frame, index);
        }
    }

    static class ISTORE_0 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            iStore(frame, 0);
        }
    }

    static class ISTORE_1 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            iStore(frame, 1);
        }
    }

    static class ISTORE_2 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            iStore(frame, 2);
        }
    }

    static class ISTORE_3 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            iStore(frame, 3);
        }
    }

    static class LSTORE extends Index8Cmd {
        @Override
        public void exec(NiFrame frame) {
            lStore(frame, index);
        }
    }

    static class LSTORE_0 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            lStore(frame, 0);
        }
    }

    static class LSTORE_1 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            lStore(frame, 1);
        }
    }

    static class LSTORE_2 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            lStore(frame, 2);
        }
    }

    static class LSTORE_3 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            lStore(frame, 3);
        }
    }

    static class FSTORE extends Index8Cmd {
        @Override
        public void exec(NiFrame frame) {
            fStore(frame, index);
        }
    }

    static class FSTORE_0 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            fStore(frame, 0);
        }
    }

    static class FSTORE_1 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            fStore(frame, 1);
        }
    }

    static class FSTORE_2 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            fStore(frame, 2);
        }
    }

    static class FSTORE_3 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            fStore(frame, 3);
        }
    }

    static class DSTORE extends Index8Cmd {
        @Override
        public void exec(NiFrame frame) {
            dStore(frame, index);
        }
    }

    static class DSTORE_0 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            dStore(frame, 0);
        }
    }

    static class DSTORE_1 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            dStore(frame, 1);
        }
    }

    static class DSTORE_2 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            dStore(frame, 2);
        }
    }

    static class DSTORE_3 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            dStore(frame, 3);
        }
    }

    static class ASTORE extends Index8Cmd {
        @Override
        public void exec(NiFrame frame) {
            aStore(frame, index);
        }
    }

    static class ASTORE_0 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            aStore(frame, 0);
        }
    }

    static class ASTORE_1 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            aStore(frame, 1);
        }
    }

    static class ASTORE_2 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            aStore(frame, 2);
        }
    }

    static class ASTORE_3 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            aStore(frame, 3);
        }
    }
}
