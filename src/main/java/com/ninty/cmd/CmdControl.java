package com.ninty.cmd;

import com.ninty.cmd.base.BranchCmd;
import com.ninty.cmd.base.DataCmd;
import com.ninty.cmd.base.NoOperandCmd;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.NiThread;
import com.ninty.runtime.heap.CodeBytes;

/**
 * Created by ninty on 2017/7/16.
 */
public class CmdControl {

    private static void returnCmd(NiFrame frame, char type) {
        NiThread thread = frame.getThread();
        thread.popFrame();
        NiFrame topFrame = thread.topFrame();
        switch (type) {
            case 'i':
                topFrame.getOperandStack().pushInt(frame.getOperandStack().popInt());
                break;
            case 'l':
                topFrame.getOperandStack().pushLong(frame.getOperandStack().popLong());
                break;
            case 'f':
                topFrame.getOperandStack().pushFloat(frame.getOperandStack().popFloat());
                break;
            case 'd':
                topFrame.getOperandStack().pushDouble(frame.getOperandStack().popDouble());
                break;
            case 'a':
                topFrame.getOperandStack().pushRef(frame.getOperandStack().popRef());
                break;
        }
    }

    private static void skipPadding(CodeBytes bb) {
        while (bb.position() % 4 != 0) {
            bb.position(bb.position() + 1);
        }
    }

    public static class GOTO extends BranchCmd {
        @Override
        public void exec(NiFrame frame) {
            branch();
        }
    }

    public static class GOTO_W extends DataCmd {
        private int offset;

        @Override
        public void init(CodeBytes bb) {
            super.init(bb);
            offset = bb.getInt();
        }

        @Override
        public void exec(NiFrame frame) {
            jumpTo(offset);
        }
    }

    public static class TABLESWITCH extends DataCmd {
        private int defaultOffset;
        private int low;
        private int high;
        private int[] jumpOffsets;

        @Override
        public void init(CodeBytes bb) {
            super.init(bb);
            skipPadding(bb);
            defaultOffset = bb.getInt();
            low = bb.getInt();
            high = bb.getInt();
            int count = high - low + 1;
            jumpOffsets = new int[count];
            for (int i = 0; i < count; i++) {
                jumpOffsets[i] = bb.getInt();
            }
        }

        @Override
        public void exec(NiFrame frame) {
            int index = frame.getOperandStack().popInt();
            if (index >= low && index <= high) {
                jumpTo(jumpOffsets[index - low]);
            } else {
                jumpTo(defaultOffset);
            }
        }
    }

    public static class LOOKUPSWITCH extends DataCmd {
        private int defaultOffset;
        private int npairs;
        private int[] matchOffsets;

        @Override
        public void init(CodeBytes bb) {
            super.init(bb);
            skipPadding(bb);
            defaultOffset = bb.getInt();
            npairs = bb.getInt();
            matchOffsets = new int[npairs*2];
            for (int i = 0; i < npairs*2; i++) {
                matchOffsets[i] = bb.getInt();
            }
        }

        @Override
        public void exec(NiFrame frame) {
            int index = frame.getOperandStack().popInt();
            for (int i = 0; i < npairs * 2; i += 2) {
                if (matchOffsets[i] == index) {
                    jumpTo(matchOffsets[i + 1]);
                    return;
                }
            }
            jumpTo(defaultOffset);
        }
    }

    public static class RETURN extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            frame.getThread().popFrame();
        }
    }

    public static class ARETURN extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            returnCmd(frame, 'a');
        }
    }

    public static class IRETURN extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            returnCmd(frame, 'i');
        }
    }

    public static class LRETURN extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            returnCmd(frame, 'l');
        }
    }

    public static class FRETURN extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            returnCmd(frame, 'f');
        }
    }

    public static class DRETURN extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            returnCmd(frame, 'd');
        }
    }
}
