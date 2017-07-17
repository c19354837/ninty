package com.ninty.cmd;

import com.ninty.cmd.base.Index8Cmd;
import com.ninty.cmd.base.NoOperandCmd;
import com.ninty.runtime.NiFrame;

/**
 * Created by ninty on 2017/7/16.
 */
public class CmdLoads {

    static void iLoad(NiFrame frame, int index){
        int val = frame.getLocalVars().getInt(index);
        frame.getOperandStack().pushInt(val);
    }

    static class ILOAD extends Index8Cmd{
        @Override
        public void exec(NiFrame frame) {
            iLoad(frame, index);
        }
    }

    static class ILOAD_0 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            iLoad(frame, 0);
        }
    }

    static class ILOAD_1 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            iLoad(frame, 1);
        }
    }

    static class ILOAD_2 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            iLoad(frame, 2);
        }
    }

    static class ILOAD_3 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            iLoad(frame, 3);
        }
    }
}
