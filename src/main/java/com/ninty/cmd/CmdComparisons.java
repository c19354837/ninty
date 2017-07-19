package com.ninty.cmd;

import com.ninty.cmd.base.BranchCmd;
import com.ninty.cmd.base.NoOperandCmd;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.OperandStack;

/**
 * Created by ninty on 2017/7/16.
 */
public class CmdComparisons {

    static void fcmp(NiFrame frame, int ifNaN) {
        OperandStack stack = frame.getOperandStack();
        float num1 = stack.popFloat();
        float num2 = stack.popFloat();
        if (Float.isNaN(num1) || Float.isNaN(num2)) {
            stack.pushInt(ifNaN);
        } else {
            stack.pushInt(num1 > num2 ? 1 : (num1 == num2 ? 0 : -1));
        }
    }

    static void dcmp(NiFrame frame, int ifNaN) {
        OperandStack stack = frame.getOperandStack();
        double num1 = stack.popDouble();
        double num2 = stack.popDouble();
        if (Double.isNaN(num1) || Double.isNaN(num2)) {
            stack.pushInt(ifNaN);
        } else {
            stack.pushInt(num1 > num2 ? 1 : (num1 == num2 ? 0 : -1));
        }
    }

    static class LCMP extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            long num1 = stack.popLong();
            long num2 = stack.popLong();
            stack.pushInt(num1 > num2 ? 1 : (num1 == num2 ? 0 : -1));
        }
    }

    static class FCMPL extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            fcmp(frame, -1);
        }
    }

    static class FCMPG extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            fcmp(frame, 1);
        }
    }

    static class DCMPL extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            fcmp(frame, -1);
        }
    }

    static class DCMPG extends NoOperandCmd {
        @Override
        public void exec(NiFrame frame) {
            fcmp(frame, 1);
        }
    }

    static class IFEQ extends BranchCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int val = stack.popInt();
            // TODO branch
        }
    }
}
