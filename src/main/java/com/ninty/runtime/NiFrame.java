package com.ninty.runtime;

import com.ninty.runtime.heap.NiMethod;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/12.
 */
public class NiFrame {
    public NiFrame prevFrame;

    private NiThread thread;
    private NiMethod method;

    private LocalVars localVars;
    private OperandStack operandStack;

    private ByteBuffer bb;
    private int position;

    public NiFrame(NiMethod method) {
        this.method = method;
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

    public void setThread(NiThread thread) {
        this.thread = thread;
    }

    public NiThread getThread() {
        return thread;
    }

    public void reset() {
        bb.position(0);
    }

    public void savePosition() {
        position = bb.position();
    }

    public void restorePostion() {
        bb.position(position);
    }

    @Override
    public String toString() {
        return "NiFrame{" +
                "method=" + method +
                ",\nlocalVars=" + localVars +
                ", operandStack=" + operandStack +
                '}';
    }
}
