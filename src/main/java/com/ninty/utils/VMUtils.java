package com.ninty.utils;

/**
 * Created by ninty on 2017/7/8.
 */
public class VMUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.trim().equals("");
    }

    public static int toUInt(byte val) {
        return val & 0xff;
    }
}
