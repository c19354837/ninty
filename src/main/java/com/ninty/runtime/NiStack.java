package com.ninty.runtime;

import java.util.EmptyStackException;

/**
 * Created by ninty on 2017/7/12.
 */
public class NiStack {
    int maxSize;
    int size;
    NiFrame top;

    NiStack(int maxSize) {
        this.maxSize = maxSize;
    }

    public void push(NiFrame frame) {
        if (size >= maxSize) {
            throw new StackOverflowError("max stack size:" + maxSize);
        }
        if (top != null) {
            frame.prevFrame = top;
        }
        top = frame;
        size++;
    }

    public NiFrame pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        NiFrame frame = top;
        top = top.prevFrame;
        frame.prevFrame = null;
        size--;
        return frame;
    }

    public NiFrame top() {
        if (top == null) {
            throw new EmptyStackException();
        }
        return top;
    }
}
