package com.ninty.cmd.base;

import static com.ninty.cmd.CmdComparisons.*;
import static com.ninty.cmd.CmdConstants.*;
import static com.ninty.cmd.CmdControl.*;
import static com.ninty.cmd.CmdConversions.*;
import static com.ninty.cmd.CmdExtended.*;
import static com.ninty.cmd.CmdLoads.*;
import static com.ninty.cmd.CmdMath.*;
import static com.ninty.cmd.CmdStack.*;
import static com.ninty.cmd.CmdStores.*;
import static com.ninty.utils.VMUtils.toUInt;

/**
 * Created by ninty on 2017/7/22.
 */
public class CmdFatory {

    private CmdFatory() {
    }

    public static ICmdBase getCmd(byte opCode) {
        int code = toUInt(opCode);
        switch (code) {
            case 0x00:
                return new NOP();
            case 0x01:
                return new ACONST_NULL();
            case 0x02:
                return new ICONST_M1();
            case 0x03:
                return new ICONST_0();
            case 0x04:
                return new ICONST_1();
            case 0x05:
                return new ICONST_2();
            case 0x06:
                return new ICONST_3();
            case 0x07:
                return new ICONST_4();
            case 0x08:
                return new ICONST_5();
            case 0x09:
                return new LCONST_0();
            case 0x0a:
                return new LCONST_1();
            case 0x0b:
                return new FCONST_0();
            case 0x0c:
                return new FCONST_1();
            case 0x0e:
                return new FCONST_2();
            case 0x0f:
                return new DCONST_1();
            case 0x10:
                return new BIPUSH();
            case 0x11:
                return new SIPUSH();
            case 0x12:
                unsupport(code);//ldc
            case 0x13:
                unsupport(code);//ldc_w
            case 0x14:
                unsupport(code);//ldc2_w
            case 0x15:
                return new ILOAD();
            case 0x16:
                return new LLOAD();
            case 0x17:
                return new FLOAD();
            case 0x18:
                return new DLOAD();
            case 0x19:
                return new ALOAD();
            case 0x1a:
                return new ILOAD_0();
            case 0x1b:
                return new ILOAD_1();
            case 0x1c:
                return new ILOAD_2();
            case 0x1d:
                return new ILOAD_3();
            case 0x1e:
                return new LLOAD_0();
            case 0x1f:
                return new LLOAD_1();
            case 0x20:
                return new LLOAD_2();
            case 0x21:
                return new LLOAD_3();
            case 0x22:
                return new FLOAD_0();
            case 0x23:
                return new FLOAD_1();
            case 0x24:
                return new FLOAD_2();
            case 0x25:
                return new FLOAD_3();
            case 0x26:
                return new DLOAD_0();
            case 0x27:
                return new DLOAD_1();
            case 0x28:
                return new DLOAD_2();
            case 0x29:
                return new DLOAD_3();
            case 0x2a:
                return new ALOAD_0();
            case 0x2b:
                return new ALOAD_1();
            case 0x2c:
                return new ALOAD_2();
            case 0x2d:
                return new ALOAD_3();
            case 0x2e:
                unsupport(code); //iaload
            case 0x2f:
                unsupport(code); // laload
            case 0x30:
                unsupport(code); // faload
            case 0x31:
                unsupport(code); // daload
            case 0x32:
                unsupport(code); // aaload
            case 0x33:
                unsupport(code); // baload
            case 0x34:
                unsupport(code); // caload
            case 0x35:
                unsupport(code); // saload
            case 0x36:
                return new ISTORE();
            case 0x37:
                return new LSTORE();
            case 0x38:
                return new FSTORE();
            case 0x39:
                return new DSTORE();
            case 0x3a:
                return new ASTORE();
            case 0x3b:
                return new ISTORE_0();
            case 0x3c:
                return new ISTORE_1();
            case 0x3d:
                return new ISTORE_2();
            case 0x3e:
                return new ISTORE_3();
            case 0x3f:
                return new LSTORE_0();
            case 0x40:
                return new LSTORE_1();
            case 0x41:
                return new LSTORE_2();
            case 0x42:
                return new LSTORE_3();
            case 0x43:
                return new FSTORE_0();
            case 0x44:
                return new FSTORE_1();
            case 0x45:
                return new FSTORE_2();
            case 0x46:
                return new FSTORE_3();
            case 0x47:
                return new DSTORE_0();
            case 0x48:
                return new DSTORE_1();
            case 0x49:
                return new DSTORE_2();
            case 0x4a:
                return new DSTORE_3();
            case 0x4b:
                return new ASTORE_0();
            case 0x4c:
                return new ASTORE_1();
            case 0x4d:
                return new ASTORE_2();
            case 0x4e:
                return new ASTORE_3();
            case 0x4f:
                unsupport(code); //iastore
            case 0x50:
                unsupport(code); //lastore
            case 0x51:
                unsupport(code); //fastore
            case 0x52:
                unsupport(code); //dastore
            case 0x53:
                unsupport(code); //aastore
            case 0x54:
                unsupport(code); //bastore
            case 0x55:
                unsupport(code); //castore
            case 0x56:
                unsupport(code); //sastore
            case 0x57:
                return new POP();
            case 0x58:
                return new POP2();
            case 0x59:
                return new DUP();
            case 0x5a:
                return new DUP_X1();
            case 0x5b:
                return new DUP_X2();
            case 0x5c:
                return new DUP2();
            case 0x5d:
                return new DUP2_X1();
            case 0x5e:
                return new DUP2_X2();
            case 0x5f:
                return new SWAP();
            case 0x60:
                return new IADD();
            case 0x61:
                return new LADD();
            case 0x62:
                return new FADD();
            case 0x63:
                return new DADD();
            case 0x64:
                return new ISUB();
            case 0x65:
                return new LSUB();
            case 0x66:
                return new FSUB();
            case 0x67:
                return new DSUB();
            case 0x68:
                return new IMUL();
            case 0x69:
                return new LMUL();
            case 0x6a:
                return new FMUL();
            case 0x6b:
                return new DMUL();
            case 0x6c:
                return new IDIV();
            case 0x6d:
                return new LDIV();
            case 0x6e:
                return new FDIV();
            case 0x6f:
                return new DDIV();
            case 0x70:
                return new IREM();
            case 0x71:
                return new LREM();
            case 0x72:
                return new FREM();
            case 0x73:
                return new DREM();
            case 0x74:
                return new INEG();
            case 0x75:
                return new LNEG();
            case 0x76:
                return new FNEG();
            case 0x77:
                return new DNEG();
            case 0x78:
                return new ISHL();
            case 0x79:
                return new LSHL();
            case 0x7a:
                return new ISHR();
            case 0x7b:
                return new LSHR();
            case 0x7c:
                return new IUSHR();
            case 0x7d:
                return new LUSHR();
            case 0x7e:
                return new IAND();
            case 0x7f:
                return new LAND();
            case 0x80:
                return new IOR();
            case 0x81:
                return new LOR();
            case 0x82:
                return new IXOR();
            case 0x83:
                return new LXOR();
            case 0x84:
                return new IINC();
            case 0x85:
                return new I2L();
            case 0x86:
                return new I2F();
            case 0x87:
                return new I2D();
            case 0x88:
                return new L2I();
            case 0x89:
                return new L2F();
            case 0x8a:
                return new L2D();
            case 0x8b:
                return new F2I();
            case 0x8c:
                return new F2L();
            case 0x8d:
                return new F2D();
            case 0x8e:
                return new D2I();
            case 0x8f:
                return new D2L();
            case 0x90:
                return new D2F();
            case 0x91:
                return new I2B();
            case 0x92:
                return new I2C();
            case 0x93:
                return new I2S();
            case 0x94:
                return new LCMP();
            case 0x95:
                return new FCMPL();
            case 0x96:
                return new FCMPG();
            case 0x97:
                return new DCMPL();
            case 0x98:
                return new DCMPG();
            case 0x99:
                return new IFEQ();
            case 0x9a:
                return new IFNE();
            case 0x9b:
                return new IFLT();
            case 0x9c:
                return new IFGE();
            case 0x9d:
                return new IFGT();
            case 0x9e:
                return new IFLE();
            case 0x9f:
                return new IF_ICMPEQ();
            case 0xa0:
                return new IF_ICMPNE();
            case 0xa1:
                return new IF_ICMPLT();
            case 0xa2:
                return new IF_ICMPGE();
            case 0xa3:
                return new IF_ICMPGT();
            case 0xa4:
                return new IF_ICMPLE();
            case 0xa5:
                return new IF_ICMPEQ();
            case 0xa6:
                return new IF_ICMPNE();
            case 0xa7:
                return new GOTO();
            case 0xa8:
                unsupport(code);//jsr
            case 0xa9:
                unsupport(code);//ret
            case 0xaa:
                return new TABLESWITCH();
            case 0xab:
                return new LOOKUPSWITCH();
            case 0xac:
                unsupport(code);//ireturn
            case 0xad:
                unsupport(code);//lreturn
            case 0xae:
                unsupport(code);//freturn
            case 0xaf:
                unsupport(code);//dreturn
            case 0xb0:
                unsupport(code);//areturn
            case 0xb1:
                unsupport(code);//return
            case 0xb2:
                unsupport(code);//getstatic
            case 0xb3:
                unsupport(code);//putstatic
            case 0xb4:
                unsupport(code);//getfield
            case 0xb5:
                unsupport(code);//putfield
            case 0xb6:
                unsupport(code);//invokevirtual
            case 0xb7:
                unsupport(code);//invokespecial
            case 0xb8:
                unsupport(code);//invokestatic
            case 0xb9:
                unsupport(code);//invokeinterface
            case 0xba:
                unsupport(code);//invokedynamic
            case 0xbb:
                unsupport(code);//new
            case 0xbc:
                unsupport(code);//newarray
            case 0xbd:
                unsupport(code);//arraylength
            case 0xbe:
                unsupport(code);//athrow
            case 0xbf:
                unsupport(code);//checkcast
            case 0xc0:
                unsupport(code);//instanceof
            case 0xc1:
                unsupport(code);//monitorenter
            case 0xc2:
                unsupport(code);//monitorexit
            case 0xc3:
                unsupport(code);//monitorexit
            case 0xc4:
                return new WIDE();
            case 0xc5:
                unsupport(code);//multianewarray
            case 0xc6:
                return new IFNULL();
            case 0xc7:
                return new IFNONNULL();
            case 0xc8:
                return new GOTO_W();
            case 0xc9:
                unsupport(code);//jsr_w
        }
        throw new RuntimeException("Unsupport byte code: " + code);
    }

    private static void unsupport(int code) {
        throw new RuntimeException("Unsupport byte code: " + Integer.toHexString(code));
    }
}
