package com.ninty.runtime;

import com.ninty.runtime.heap.NiMethod;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/12.
 */
public class NiFrame {
    public NiFrame prevFrame;

    private NiMethod method;

    private LocalVars localVars;
    private OperandStack operandStack;

    private ByteBuffer bb;

    public NiFrame(NiMethod method) {
        localVars = new LocalVars(method.getMaxLocals());
        operandStack = new OperandStack(method.getMaxStack());
        bb = method.getCodes();
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

    public NiMethod getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "NiFrame{" +
                "localVars=" + localVars +
                ", operandStack=" + operandStack +
                '}';
    }
}
