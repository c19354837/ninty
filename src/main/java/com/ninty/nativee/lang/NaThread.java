/*
 * MIT License
 *
 * Copyright (c) 2017 ninty
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ninty.nativee.lang;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.LocalVars;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.heap.NiObject;

/**
 * Created by ninty on 2017/9/25.
 */
public class NaThread {

    private final static String className = "java/lang/Thread";

    public static void init() {
        NaMethodManager.register(className, "currentThread", "()Ljava/lang/thread;", new currentThread());
    }

    public static class currentThread implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            Thread currentThread = Thread.currentThread();
            LocalVars localVars = frame.getLocalVars();
            NiObject src = localVars.getRef(0);
            int srcPos = localVars.getInt(1);
            NiObject desc = localVars.getRef(2);
            int descPos = localVars.getInt(3);
            int length = localVars.getInt(4);
            if (!src.getClz().isArray() || !desc.getClz().isArray()) {
                throw new ArrayStoreException();
            }
            System.arraycopy(src.getArrayDatas(), srcPos, desc.getArrayDatas(), descPos, length);
        }
    }
}
