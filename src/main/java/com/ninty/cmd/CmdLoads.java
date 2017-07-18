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

    static void lLoad(NiFrame frame, int index){
        long val = frame.getLocalVars().getLong(index);
        frame.getOperandStack().pushLong(val);
    }

    static void fLoad(NiFrame frame, int index){
        float val = frame.getLocalVars().getFloat(index);
        frame.getOperandStack().pushFloat(val);
    }

    static void dLoad(NiFrame frame, int index){
        double val = frame.getLocalVars().getDouble(index);
        frame.getOperandStack().pushDouble(val);
    }

    static void aLoad(NiFrame frame, int index){
        Object ref = frame.getLocalVars().getRef(index);
        frame.getOperandStack().pushRef(ref);
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

    static class LLOAD extends Index8Cmd{
        @Override
        public void exec(NiFrame frame) {
            lLoad(frame, index);
        }
    }

    static class LLOAD_0 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            lLoad(frame, 0);
        }
    }

    static class LLOAD_1 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            lLoad(frame, 1);
        }
    }

    static class LLOAD_2 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            lLoad(frame, 2);
        }
    }

    static class LLOAD_3 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            lLoad(frame, 3);
        }
    }

    static class FLOAD extends Index8Cmd{
        @Override
        public void exec(NiFrame frame) {
            fLoad(frame, index);
        }
    }

    static class FLOAD_0 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            fLoad(frame, 0);
        }
    }

    static class FLOAD_1 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            fLoad(frame, 1);
        }
    }

    static class FLOAD_2 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            fLoad(frame, 2);
        }
    }

    static class FLOAD_3 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            fLoad(frame, 3);
        }
    }

    static class DLOAD extends Index8Cmd{
        @Override
        public void exec(NiFrame frame) {
            dLoad(frame, index);
        }
    }

    static class DLOAD_0 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            dLoad(frame, 0);
        }
    }

    static class DLOAD_1 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            dLoad(frame, 1);
        }
    }

    static class DLOAD_2 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            dLoad(frame, 2);
        }
    }

    static class DLOAD_3 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            dLoad(frame, 3);
        }
    }

    static class ALOAD extends Index8Cmd{
        @Override
        public void exec(NiFrame frame) {
            aLoad(frame, index);
        }
    }

    static class ALOAD_0 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            aLoad(frame, 0);
        }
    }

    static class ALOAD_1 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            aLoad(frame, 1);
        }
    }

    static class ALOAD_2 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            aLoad(frame, 2);
        }
    }

    static class ALOAD_3 extends NoOperandCmd{
        @Override
        public void exec(NiFrame frame) {
            aLoad(frame, 3);
        }
    }
}
