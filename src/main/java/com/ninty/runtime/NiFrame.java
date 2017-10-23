package com.ninty.runtime;

import com.ninty.runtime.heap.CodeBytes;
import com.ninty.runtime.heap.NiMethod;

/**
 * Created by ninty on 2017/7/12.
 */
public class NiFrame {
    public final static NiFrame RETURN_FRAME = new NiFrame();

    NiFrame prevFrame;

    private NiThread thread;
    private NiMethod method;

    private LocalVars localVars;
    private OperandStack operandStack;

    private CodeBytes bb;
    private ThreadLocal<Integer> position = new ThreadLocal<>();


    public NiFrame(NiMethod method) {
        this.method = method;
        localVars = new LocalVars(method.getMaxLocals());
        operandStack = new OperandStack(method.getMaxStack());
        bb = method.getCodes();
    }

    NiFrame() {
        localVars = new LocalVars(1);
        operandStack = new OperandStack(1);
        bb = CodeBytes.allocate(0);
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

    public void setOperandStack(OperandStack operandStack) {
        this.operandStack = operandStack;
    }

    public CodeBytes getCode() {
        return bb;
    }

    public byte getOpCode() {
        savePosition();
        return bb.get();
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
        position.set(bb.position());
    }

    public void restorePostion() {
        bb.position(position.get());
    }

    public int getPosition() {
        return position.get();
    }

    public void setPosition(int position) {
        this.position.set(position);
        bb.position(position);
    }

    @Override
    public String toString() {
        return "NiFrame{" + "method=" + method + ",\nlocalVars=" + localVars + ", operandStack=" + operandStack + '}';
    }
}
