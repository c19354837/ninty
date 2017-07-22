package com.ninty.runtime;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/12.
 */
public class NiFrame {
    NiFrame prevFrame;

    LocalVars localVars;
    OperandStack operandStack;

    ByteBuffer bb;

    public NiFrame(int localVarsSize, int operandStackSize, byte[] codes) {
        localVars = new LocalVars(localVarsSize);
        operandStack = new OperandStack(operandStackSize);
        bb = ByteBuffer.wrap(codes);
    }

    public NiFrame getPrevFrame() {
        return prevFrame;
    }

    public LocalVars getLocalVars() {
        return localVars;
    }

    public OperandStack getOperandStack() {
        return operandStack;
    }

    public ByteBuffer getCode() {
        return bb;
    }

    @Override
    public String toString() {
        return "NiFrame{" +
                "localVars=" + localVars +
                ", operandStack=" + operandStack +
                '}';
    }
}
