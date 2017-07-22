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

    public NiStack getStack() {
        return stack;
    }
}
