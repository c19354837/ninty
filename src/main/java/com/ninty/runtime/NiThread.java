package com.ninty.runtime;

/**
 * Created by ninty on 2017/7/12.
 */
public class NiThread {
    private int level;
    private NiStack stack;

    public NiThread(int maxStackSize) {
        stack = new NiStack(maxStackSize);
    }

    public NiFrame popFrame() {
        level--;
        NiFrame frame = stack.pop();
        NiFrame top = topFrame();
        if (top != null) {
            top.restorePostion();
        }
        return frame;
    }

    public void pushFrame(NiFrame frame) {
        level++;
        NiFrame top = topFrame();
        if (top != null) {
            top.savePosition();
        }
        frame.reset();
        frame.setThread(this);
        stack.push(frame);
    }

    public NiFrame topFrame() {
        return stack.top();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public int getLevel() {
        return level;
    }
}
