package com.ninty.cmd;

import com.ninty.cmd.base.BranchCmd;
import com.ninty.cmd.base.ICmdBase;
import com.ninty.cmd.base.Index8Cmd;
import com.ninty.runtime.NiFrame;

import java.nio.ByteBuffer;

import static com.ninty.utils.VMUtils.toUInt;

/**
 * Created by ninty on 2017/7/16.
 */
public class CmdExtended {

    public static class WIDE implements ICmdBase {
        private ICmdBase cmd;

        private void initIndex8Cmd(Index8Cmd cmd, ByteBuffer bb) {
            cmd.index = bb.getChar();
            this.cmd = cmd;
        }

        @Override
        public void init(ByteBuffer bb) {
            int code = toUInt(bb.get());
            switch (code) {
                case 0x15:
                    initIndex8Cmd(new CmdLoads.ILOAD(), bb);
                    break;
                case 0x16:
                    initIndex8Cmd(new CmdLoads.LLOAD(), bb);
                    break;
                case 0x17:
                    initIndex8Cmd(new CmdLoads.FLOAD(), bb);
                    break;
                case 0x18:
                    initIndex8Cmd(new CmdLoads.DLOAD(), bb);
                    break;
                case 0x19:
                    initIndex8Cmd(new CmdStores.ISTORE(), bb);
                    break;
                case 0x36:
                    initIndex8Cmd(new CmdStores.LSTORE(), bb);
                    break;
                case 0x37:
                    initIndex8Cmd(new CmdStores.FSTORE(), bb);
                    break;
                case 0x38:
                    initIndex8Cmd(new CmdStores.DSTORE(), bb);
                    break;
                case 0x39:
                    initIndex8Cmd(new CmdStores.ASTORE(), bb);
                    break;
                case 0x3a:
                    initIndex8Cmd(new CmdLoads.ILOAD(), bb);
                    break;
                case 0x84:
                    CmdMath.IINC iinc = new CmdMath.IINC();
                    iinc.index = bb.getChar();
                    iinc.inc = bb.getShort();
                    cmd = iinc;
                    break;
                case 0xa9:
                default:
                    throw new UnsupportedOperationException("unsupport opcode:" + code);
            }
        }

        @Override
        public void exec(NiFrame frame) {
            cmd.exec(frame);
        }
    }

    public static class IFNULL extends BranchCmd {
        @Override
        public void exec(NiFrame frame) {
            Object ref = frame.getOperandStack().popRef();
            if (ref == null) {
                branch();
            }
        }
    }

    public static class IFNONNULL extends BranchCmd {
        @Override
        public void exec(NiFrame frame) {
            Object ref = frame.getOperandStack().popRef();
            if (ref != null) {
                branch();
            }
        }
    }
}