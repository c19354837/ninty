package com.ninty.cmd;

import com.ninty.cmd.base.BranchCmd;
import com.ninty.cmd.base.NoOperandCmd;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.OperandStack;

/**
 * Created by ninty on 2017/7/16.
 */
public class CmdComparisons {

    static int icmp(NiFrame frame) {
        OperandStack stack = frame.getOperandStack();
        int num1 = stack.popInt();
        int num2 = stack.popInt();
        return num1 > num2 ? 1 : (num1 == num2 ? 0 : -1);
    }

    static boolean acmp(NiFrame frame) {
        OperandStack stack = frame.getOperandStack();
        Object ref1 = stack.popRef();
        Object ref2 = stack.popRef();
        return ref1 == ref2;
    }

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
            if (val == 0) {
                branch();
            }
        }
    }

    static class IFNE extends BranchCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int val = stack.popInt();
            if (val != 0) {
                branch();
            }
        }
    }

    static class IFLT extends BranchCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int val = stack.popInt();
            if (val < 0) {
                branch();
            }
        }
    }

    static class IFGE extends BranchCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int val = stack.popInt();
            if (val >= 0) {
                branch();
            }
        }
    }

    static class IFGT extends BranchCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int val = stack.popInt();
            if (val > 0) {
                branch();
            }
        }
    }

    static class IFLE extends BranchCmd {
        @Override
        public void exec(NiFrame frame) {
            OperandStack stack = frame.getOperandStack();
            int val = stack.popInt();
            if (val <= 0) {
                branch();
            }
        }
    }

    static class IF_ICMPEQ extends BranchCmd {
        @Override
        public void exec(NiFrame frame) {
            if (icmp(frame) == 0) {
                branch();
            }
        }
    }

    static class IF_ICMPNE extends BranchCmd {
        @Override
        public void exec(NiFrame frame) {
            if (icmp(frame) != 0) {
                branch();
            }
        }
    }

    static class IF_ICMPLT extends BranchCmd {
        @Override
        public void exec(NiFrame frame) {
            if (icmp(frame) < 0) {
                branch();
            }
        }
    }

    static class IF_ICMPGE extends BranchCmd {
        @Override
        public void exec(NiFrame frame) {
            if (icmp(frame) >= 0) {
                branch();
            }
        }
    }

    static class IF_ICMPGT extends BranchCmd {
        @Override
        public void exec(NiFrame frame) {
            if (icmp(frame) > 0) {
                branch();
            }
        }
    }

    static class IF_ICMPLE extends BranchCmd {
        @Override
        public void exec(NiFrame frame) {
            if (icmp(frame) <= 0) {
                branch();
            }
        }
    }

    static class IF_ACMPEQ extends BranchCmd {
        @Override
        public void exec(NiFrame frame) {
            if (acmp(frame)) {
                branch();
            }
        }
    }

    static class IF_ACMPNE extends BranchCmd {
        @Override
        public void exec(NiFrame frame) {
            if (!acmp(frame)) {
                branch();
            }
        }
    }
}
