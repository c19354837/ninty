package com.ninty.runtime.heap;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/10/21.
 */
public class CodeBytes {

    private ByteBuffer codes;
    private ThreadLocal<Integer> position = new ThreadLocal<>();

    public CodeBytes(ByteBuffer codes){
        this.codes = codes;
    }

    public CodeBytes(byte[] codes){
        this.codes = ByteBuffer.wrap(codes);
    }

    public static CodeBytes wrap(byte[] codes){
        return new CodeBytes(codes);
    }

    public static CodeBytes allocate(int capacity){
        return new CodeBytes(ByteBuffer.allocate(0));
    }

    public void position(int newPosition){
        position.set(newPosition);
    }

    public int position(){
        return position.get();
    }

    public byte get() {
        int pos = position.get();
        byte data = codes.get(pos);
        position.set(pos+1);
        return data;
    }

    public char getChar() {
        int pos = position.get();
        char data = codes.getChar(pos);
        position.set(pos+2);
        return data;
    }

    public short getShort() {
        int pos = position.get();
        short data = codes.getShort(pos);
        position.set(pos+2);
        return data;
    }

    public int getInt() {
        int pos = position.get();
        int data = codes.getInt(pos);
        position.set(pos+4);
        return data;
    }

    public long getLong() {
        int pos = position.get();
        long data = codes.getLong(pos);
        position.set(pos+8);
        return data;
    }

    public float getFloat() {
        int pos = position.get();
        float data = codes.getFloat(pos);
        position.set(pos+4);
        return data;
    }

    public double getDouble() {
        int pos = position.get();
        double data = codes.getDouble(pos);
        position.set(pos+8);
        return data;
    }
}
