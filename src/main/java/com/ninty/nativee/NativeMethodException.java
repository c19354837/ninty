package com.ninty.nativee;

/**
 * Created by ninty on 2017/10/29.
 */
public class NativeMethodException extends RuntimeException {
    public NativeMethodException() {
    }

    public NativeMethodException(String message) {
        super(message);
    }

    public NativeMethodException(String message, Throwable cause) {
        super(message, cause);
    }
}
