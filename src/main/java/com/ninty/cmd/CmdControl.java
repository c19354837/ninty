package com.ninty.cmd;

import com.ninty.cmd.base.BranchCmd;
import com.ninty.cmd.base.DataCmd;
import com.ninty.runtime.NiFrame;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/16.
 */
public class CmdControl {

    private static void skipPadding(ByteBuffer bb) {
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
        public void init(ByteBuffer bb) {
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
        public void init(ByteBuffer bb) {
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
        public void init(ByteBuffer bb) {
            super.init(bb);
            skipPadding(bb);
            defaultOffset = bb.getInt();
            npairs = bb.getInt();
            matchOffsets = new int[npairs];
            for (int i = 0; i < npairs; i++) {
                matchOffsets[i] = bb.getInt();
            }
        }

        @Override
        public void exec(NiFrame frame) {
            int index = frame.getOperandStack().popInt();
            for (int i = 0; i < npairs * 2; i++) {
                if (matchOffsets[i] == index) {
                    jumpTo(matchOffsets[i] + 1);
                    return;
                }
            }
            jumpTo(defaultOffset);
        }
    }
}
