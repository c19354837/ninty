package com.ninty.cmd;

import com.ninty.cmd.base.NoOperandCmd;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.OperandStack;
import com.ninty.runtime.Slot;

/**
 * Created by ninty on 2017/7/16.
 */
public class CmdStack {

    static class POP extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().popSlot();
        }
    }

    static class POP2 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            frame.getOperandStack().popSlot();
            frame.getOperandStack().popSlot();
        }
    }

    static class DUP extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            Slot slot = stack.popSlot();
            stack.pushSlot(slot);
            stack.pushSlot(slot);
        }
    }


    static class DUP_X1 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            Slot slot1 = stack.popSlot();
            Slot slot2 = stack.popSlot();
            stack.pushSlot(slot1);
            stack.pushSlot(slot2);
            stack.pushSlot(slot1);
        }
    }


    static class DUP_X2 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            Slot slot1 = stack.popSlot();
            Slot slot2 = stack.popSlot();
            Slot slot3 = stack.popSlot();
            stack.pushSlot(slot1);
            stack.pushSlot(slot3);
            stack.pushSlot(slot2);
            stack.pushSlot(slot1);
        }
    }

    static class DUP2 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            Slot slot1 = stack.popSlot();
            Slot slot2 = stack.popSlot();
            stack.pushSlot(slot2);
            stack.pushSlot(slot1);
            stack.pushSlot(slot2);
            stack.pushSlot(slot1);
        }
    }

    static class DUP2_X1 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            Slot slot1 = stack.popSlot();
            Slot slot2 = stack.popSlot();
            Slot slot3 = stack.popSlot();
            stack.pushSlot(slot2);
            stack.pushSlot(slot1);
            stack.pushSlot(slot3);
            stack.pushSlot(slot2);
            stack.pushSlot(slot1);
        }
    }

    static class DUP2_X2 extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            Slot slot1 = stack.popSlot();
            Slot slot2 = stack.popSlot();
            Slot slot3 = stack.popSlot();
            Slot slot4 = stack.popSlot();
            stack.pushSlot(slot2);
            stack.pushSlot(slot1);
            stack.pushSlot(slot4);
            stack.pushSlot(slot3);
            stack.pushSlot(slot2);
            stack.pushSlot(slot1);
        }
    }

    static class SWAP extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            Slot slot1 = stack.popSlot();
            Slot slot2 = stack.popSlot();
            stack.pushSlot(slot2);
            stack.pushSlot(slot1);
        }
    }

}
