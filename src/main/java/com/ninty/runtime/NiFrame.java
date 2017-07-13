package com.ninty.runtime;

/**
 * Created by ninty on 2017/7/12.
 */
public class NiFrame {
    NiFrame prevFrame;

    LocalVars localVars;
    OperandStack operandStack;

    public NiFrame(int localVarsSize, int operandStackSize){
        localVars = new LocalVars(localVarsSize);
        operandStack = new OperandStack(operandStackSize);
    }
}
