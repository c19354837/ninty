package com.ninty.runtime;

/**
 * Created by ninty on 2017/7/12.
 */
public class NiThread {
    private int pc;
    private NiStack stack;

    public NiThread(int maxStackSize) {
        stack = new NiStack(maxStackSize);
    }


    public NiFrame popFrame() {
        return stack.pop();
    }

    public void pushFrame(NiFrame frame) {
        stack.push(frame);
    }

    public NiFrame topFrame() {
        return stack.top();
    }

    public NiStack getStack() {
        return stack;
    }
}
