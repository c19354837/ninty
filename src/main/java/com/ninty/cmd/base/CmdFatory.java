package com.ninty.cmd.base;

import static com.ninty.cmd.CmdArray.*;
import static com.ninty.cmd.CmdComparisons.*;
import static com.ninty.cmd.CmdConstants.*;
import static com.ninty.cmd.CmdControl.*;
import static com.ninty.cmd.CmdConversions.*;
import static com.ninty.cmd.CmdExtended.*;
import static com.ninty.cmd.CmdLoads.*;
import static com.ninty.cmd.CmdMath.*;
import static com.ninty.cmd.CmdReferences.*;
import static com.ninty.cmd.CmdStack.*;
import static com.ninty.cmd.CmdStores.*;
import static com.ninty.utils.VMUtils.toUInt;

/**
 * Created by ninty on 2017/7/22.
 */
public class CmdFatory {

    private static ICmdBase NOP = new NOP();
    private static ICmdBase ACONST_NULL = new ACONST_NULL();
    private static ICmdBase ICONST_M1 = new ICONST_M1();
    private static ICmdBase ICONST_0 = new ICONST_0();
    private static ICmdBase ICONST_1 = new ICONST_1();
    private static ICmdBase ICONST_2 = new ICONST_2();
    private static ICmdBase ICONST_3 = new ICONST_3();
    private static ICmdBase ICONST_4 = new ICONST_4();
    private static ICmdBase ICONST_5 = new ICONST_5();
    private static ICmdBase LCONST_0 = new LCONST_0();
    private static ICmdBase LCONST_1 = new LCONST_1();
    private static ICmdBase FCONST_0 = new FCONST_0();
    private static ICmdBase FCONST_1 = new FCONST_1();
    private static ICmdBase FCONST_2 = new FCONST_2();
    private static ICmdBase DCONST_0 = new DCONST_0();
    private static ICmdBase DCONST_1 = new DCONST_1();
    private static ICmdBase ILOAD_0 = new ILOAD_0();
    private static ICmdBase ILOAD_1 = new ILOAD_1();
    private static ICmdBase ILOAD_2 = new ILOAD_2();
    private static ICmdBase ILOAD_3 = new ILOAD_3();
    private static ICmdBase LLOAD_0 = new LLOAD_0();
    private static ICmdBase LLOAD_1 = new LLOAD_1();
    private static ICmdBase LLOAD_2 = new LLOAD_2();
    private static ICmdBase LLOAD_3 = new LLOAD_3();
    private static ICmdBase FLOAD_0 = new FLOAD_0();
    private static ICmdBase FLOAD_1 = new FLOAD_1();
    private static ICmdBase FLOAD_2 = new FLOAD_2();
    private static ICmdBase FLOAD_3 = new FLOAD_3();
    private static ICmdBase DLOAD_0 = new DLOAD_0();
    private static ICmdBase DLOAD_1 = new DLOAD_1();
    private static ICmdBase DLOAD_2 = new DLOAD_2();
    private static ICmdBase DLOAD_3 = new DLOAD_3();
    private static ICmdBase ALOAD_0 = new ALOAD_0();
    private static ICmdBase ALOAD_1 = new ALOAD_1();
    private static ICmdBase ALOAD_2 = new ALOAD_2();
    private static ICmdBase ALOAD_3 = new ALOAD_3();
    private static ICmdBase AALOAD = new AALOAD();
    private static ICmdBase CALOAD = new CALOAD();
    private static ICmdBase IALOAD = new IALOAD();
    private static ICmdBase LALOAD = new LALOAD();
    private static ICmdBase FALOAD = new FALOAD();
    private static ICmdBase DALOAD = new DALOAD();
    private static ICmdBase SALOAD = new SALOAD();
    private static ICmdBase ISTORE_0 = new ISTORE_0();
    private static ICmdBase ISTORE_1 = new ISTORE_1();
    private static ICmdBase ISTORE_2 = new ISTORE_2();
    private static ICmdBase ISTORE_3 = new ISTORE_3();
    private static ICmdBase LSTORE_0 = new LSTORE_0();
    private static ICmdBase LSTORE_1 = new LSTORE_1();
    private static ICmdBase LSTORE_2 = new LSTORE_2();
    private static ICmdBase LSTORE_3 = new LSTORE_3();
    private static ICmdBase FSTORE_0 = new FSTORE_0();
    private static ICmdBase FSTORE_1 = new FSTORE_1();
    private static ICmdBase FSTORE_2 = new FSTORE_2();
    private static ICmdBase FSTORE_3 = new FSTORE_3();
    private static ICmdBase DSTORE_0 = new DSTORE_0();
    private static ICmdBase DSTORE_1 = new DSTORE_1();
    private static ICmdBase DSTORE_2 = new DSTORE_2();
    private static ICmdBase DSTORE_3 = new DSTORE_3();
    private static ICmdBase ASTORE_0 = new ASTORE_0();
    private static ICmdBase ASTORE_1 = new ASTORE_1();
    private static ICmdBase ASTORE_2 = new ASTORE_2();
    private static ICmdBase ASTORE_3 = new ASTORE_3();
    private static ICmdBase AASTORE = new AASTORE();
    private static ICmdBase IASTORE = new IASTORE();
    private static ICmdBase LASTORE = new LASTORE();
    private static ICmdBase FASTORE = new FASTORE();
    private static ICmdBase DASTORE = new DASTORE();
    private static ICmdBase CASTORE = new CASTORE();
    private static ICmdBase BASTORE = new BASTORE();
    private static ICmdBase SASTORE = new SASTORE();
    private static ICmdBase ARRAY_LENGTH = new ARRAY_LENGTH();
    private static ICmdBase ATHROW = new ATHROW();
    private static ICmdBase BALOAD = new BALOAD();
    private static ICmdBase POP = new POP();
    private static ICmdBase POP2 = new POP2();
    private static ICmdBase DUP = new DUP();
    private static ICmdBase DUP_X1 = new DUP_X1();
    private static ICmdBase DUP_X2 = new DUP_X2();
    private static ICmdBase DUP2 = new DUP2();
    private static ICmdBase DUP2_X1 = new DUP2_X1();
    private static ICmdBase DUP2_X2 = new DUP2_X2();
    private static ICmdBase SWAP = new SWAP();
    private static ICmdBase IADD = new IADD();
    private static ICmdBase LADD = new LADD();
    private static ICmdBase FADD = new FADD();
    private static ICmdBase DADD = new DADD();
    private static ICmdBase ISUB = new ISUB();
    private static ICmdBase LSUB = new LSUB();
    private static ICmdBase FSUB = new FSUB();
    private static ICmdBase DSUB = new DSUB();
    private static ICmdBase IMUL = new IMUL();
    private static ICmdBase LMUL = new LMUL();
    private static ICmdBase FMUL = new FMUL();
    private static ICmdBase DMUL = new DMUL();
    private static ICmdBase IDIV = new IDIV();
    private static ICmdBase LDIV = new LDIV();
    private static ICmdBase FDIV = new FDIV();
    private static ICmdBase DDIV = new DDIV();
    private static ICmdBase IREM = new IREM();
    private static ICmdBase LREM = new LREM();
    private static ICmdBase FREM = new FREM();
    private static ICmdBase DREM = new DREM();
    private static ICmdBase INEG = new INEG();
    private static ICmdBase LNEG = new LNEG();
    private static ICmdBase FNEG = new FNEG();
    private static ICmdBase DNEG = new DNEG();
    private static ICmdBase ISHL = new ISHL();
    private static ICmdBase ISHR = new ISHR();
    private static ICmdBase LSHL = new LSHL();
    private static ICmdBase LSHR = new LSHR();
    private static ICmdBase IUSHR = new IUSHR();
    private static ICmdBase LUSHR = new LUSHR();
    private static ICmdBase IAND = new IAND();
    private static ICmdBase LAND = new LAND();
    private static ICmdBase IOR = new IOR();
    private static ICmdBase LOR = new LOR();
    private static ICmdBase IXOR = new IXOR();
    private static ICmdBase LXOR = new LXOR();
    private static ICmdBase I2L = new I2L();
    private static ICmdBase I2F = new I2F();
    private static ICmdBase I2D = new I2D();
    private static ICmdBase L2I = new L2I();
    private static ICmdBase L2F = new L2F();
    private static ICmdBase L2D = new L2D();
    private static ICmdBase F2I = new F2I();
    private static ICmdBase F2L = new F2L();
    private static ICmdBase F2D = new F2D();
    private static ICmdBase D2I = new D2I();
    private static ICmdBase D2L = new D2L();
    private static ICmdBase D2F = new D2F();
    private static ICmdBase I2B = new I2B();
    private static ICmdBase I2C = new I2C();
    private static ICmdBase I2S = new I2S();
    private static ICmdBase LCMP = new LCMP();
    private static ICmdBase FCMPL = new FCMPL();
    private static ICmdBase FCMPG = new FCMPG();
    private static ICmdBase DCMPL = new DCMPL();
    private static ICmdBase DCMPG = new DCMPG();
    private static ICmdBase RETURN = new RETURN();
    private static ICmdBase IRETURN = new IRETURN();
    private static ICmdBase LRETURN = new LRETURN();
    private static ICmdBase FRETURN = new FRETURN();
    private static ICmdBase DRETURN = new DRETURN();
    private static ICmdBase ARETURN = new ARETURN();
    private static ICmdBase INVOKE_NATIVE = new INVOKE_NATIVE();

    private CmdFatory() {
    }

    public static ICmdBase getCmd(byte opCode) {
        int code = toUInt(opCode);
        switch (code) {
            case 0x00:
                return NOP;
            case 0x01:
                return ACONST_NULL;
            case 0x02:
                return ICONST_M1;
            case 0x03:
                return ICONST_0;
            case 0x04:
                return ICONST_1;
            case 0x05:
                return ICONST_2;
            case 0x06:
                return ICONST_3;
            case 0x07:
                return ICONST_4;
            case 0x08:
                return ICONST_5;
            case 0x09:
                return LCONST_0;
            case 0x0a:
                return LCONST_1;
            case 0x0b:
                return FCONST_0;
            case 0x0c:
                return FCONST_1;
            case 0x0d:
                return FCONST_2;
            case 0x0e:
                return DCONST_0;
            case 0x0f:
                return DCONST_1;
            case 0x10:
                return new BIPUSH();
            case 0x11:
                return new SIPUSH();
            case 0x12:
                return new LDC();
            case 0x13:
                return new LDC_W();
            case 0x14:
                return new LDC_2W();
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
                return ILOAD_0;
            case 0x1b:
                return ILOAD_1;
            case 0x1c:
                return ILOAD_2;
            case 0x1d:
                return ILOAD_3;
            case 0x1e:
                return LLOAD_0;
            case 0x1f:
                return LLOAD_1;
            case 0x20:
                return LLOAD_2;
            case 0x21:
                return LLOAD_3;
            case 0x22:
                return FLOAD_0;
            case 0x23:
                return FLOAD_1;
            case 0x24:
                return FLOAD_2;
            case 0x25:
                return FLOAD_3;
            case 0x26:
                return DLOAD_0;
            case 0x27:
                return DLOAD_1;
            case 0x28:
                return DLOAD_2;
            case 0x29:
                return DLOAD_3;
            case 0x2a:
                return ALOAD_0;
            case 0x2b:
                return ALOAD_1;
            case 0x2c:
                return ALOAD_2;
            case 0x2d:
                return ALOAD_3;
            case 0x2e:
                return IALOAD;
            case 0x2f:
                return LALOAD;
            case 0x30:
                return FALOAD;
            case 0x31:
                return DALOAD;
            case 0x32:
                return AALOAD;
            case 0x33:
                return BALOAD;
            case 0x34:
                return CALOAD;
            case 0x35:
                return SALOAD;
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
                return ISTORE_0;
            case 0x3c:
                return ISTORE_1;
            case 0x3d:
                return ISTORE_2;
            case 0x3e:
                return ISTORE_3;
            case 0x3f:
                return LSTORE_0;
            case 0x40:
                return LSTORE_1;
            case 0x41:
                return LSTORE_2;
            case 0x42:
                return LSTORE_3;
            case 0x43:
                return FSTORE_0;
            case 0x44:
                return FSTORE_1;
            case 0x45:
                return FSTORE_2;
            case 0x46:
                return FSTORE_3;
            case 0x47:
                return DSTORE_0;
            case 0x48:
                return DSTORE_1;
            case 0x49:
                return DSTORE_2;
            case 0x4a:
                return DSTORE_3;
            case 0x4b:
                return ASTORE_0;
            case 0x4c:
                return ASTORE_1;
            case 0x4d:
                return ASTORE_2;
            case 0x4e:
                return ASTORE_3;
            case 0x4f:
                return IASTORE;
            case 0x50:
                return LASTORE;
            case 0x51:
                return FASTORE;
            case 0x52:
                return DASTORE;
            case 0x53:
                return AASTORE;
            case 0x54:
                return BASTORE;
            case 0x55:
                return CASTORE;
            case 0x56:
                return SASTORE;
            case 0x57:
                return POP;
            case 0x58:
                return POP2;
            case 0x59:
                return DUP;
            case 0x5a:
                return DUP_X1;
            case 0x5b:
                return DUP_X2;
            case 0x5c:
                return DUP2;
            case 0x5d:
                return DUP2_X1;
            case 0x5e:
                return DUP2_X2;
            case 0x5f:
                return SWAP;
            case 0x60:
                return IADD;
            case 0x61:
                return LADD;
            case 0x62:
                return FADD;
            case 0x63:
                return DADD;
            case 0x64:
                return ISUB;
            case 0x65:
                return LSUB;
            case 0x66:
                return FSUB;
            case 0x67:
                return DSUB;
            case 0x68:
                return IMUL;
            case 0x69:
                return LMUL;
            case 0x6a:
                return FMUL;
            case 0x6b:
                return DMUL;
            case 0x6c:
                return IDIV;
            case 0x6d:
                return LDIV;
            case 0x6e:
                return FDIV;
            case 0x6f:
                return DDIV;
            case 0x70:
                return IREM;
            case 0x71:
                return LREM;
            case 0x72:
                return FREM;
            case 0x73:
                return DREM;
            case 0x74:
                return INEG;
            case 0x75:
                return LNEG;
            case 0x76:
                return FNEG;
            case 0x77:
                return DNEG;
            case 0x78:
                return ISHL;
            case 0x79:
                return LSHL;
            case 0x7a:
                return ISHR;
            case 0x7b:
                return LSHR;
            case 0x7c:
                return IUSHR;
            case 0x7d:
                return LUSHR;
            case 0x7e:
                return IAND;
            case 0x7f:
                return LAND;
            case 0x80:
                return IOR;
            case 0x81:
                return LOR;
            case 0x82:
                return IXOR;
            case 0x83:
                return LXOR;
            case 0x84:
                return new IINC();
            case 0x85:
                return I2L;
            case 0x86:
                return I2F;
            case 0x87:
                return I2D;
            case 0x88:
                return L2I;
            case 0x89:
                return L2F;
            case 0x8a:
                return L2D;
            case 0x8b:
                return F2I;
            case 0x8c:
                return F2L;
            case 0x8d:
                return F2D;
            case 0x8e:
                return D2I;
            case 0x8f:
                return D2L;
            case 0x90:
                return D2F;
            case 0x91:
                return I2B;
            case 0x92:
                return I2C;
            case 0x93:
                return I2S;
            case 0x94:
                return LCMP;
            case 0x95:
                return FCMPL;
            case 0x96:
                return FCMPG;
            case 0x97:
                return DCMPL;
            case 0x98:
                return DCMPG;
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
                return IRETURN;
            case 0xad:
                return LRETURN;
            case 0xae:
                return FRETURN;
            case 0xaf:
                return DRETURN;
            case 0xb0:
                return ARETURN;
            case 0xb1:
                return RETURN;
            case 0xb2:
                return new GETSTATIC();
            case 0xb3:
                return new PUTSTATIC();
            case 0xb4:
                return new GETFIELD();
            case 0xb5:
                return new PUTFIELD();
            case 0xb6:
                return new INVOKE_VIRTUAL();
            case 0xb7:
                return new INVOKE_SPECIAL();
            case 0xb8:
                return new INVOKE_STATIC();
            case 0xb9:
                return new INVOKE_INTERFACE();
            case 0xba:
                unsupport(code);//invokedynamic
            case 0xbb:
                return new NEW();
            case 0xbc:
                return new NEW_ARRAY();
            case 0xbd:
                return new ANEW_ARRAY();
            case 0xbe:
                return ARRAY_LENGTH;
            case 0xbf:
                return ATHROW;
            case 0xc0:
                return new CHECK_CAST();
            case 0xc1:
                return new INSTANCE_OF();
            case 0xc2:
                unsupport(code);//monitorenter
            case 0xc3:
                unsupport(code);//monitorexit
            case 0xc4:
                return new WIDE();
            case 0xc5:
                return new MULTI_ANEW_ARRAY();
            case 0xc6:
                return new IFNULL();
            case 0xc7:
                return new IFNONNULL();
            case 0xc8:
                return new GOTO_W();
            case 0xc9:
                unsupport(code);//jsr_w
                break;
            case 0xfe:
                return INVOKE_NATIVE;
            default:
                unsupport(code);
        }
        return null;
    }

    private static void unsupport(int code) {
        throw new RuntimeException("Unsupport byte code: " + Integer.toHexString(code));
    }
}
