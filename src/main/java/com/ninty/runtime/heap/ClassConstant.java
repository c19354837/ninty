package com.ninty.runtime.heap;

/**
 * Created by ninty on 2017/7/25.
 */
public class ClassConstant {
    public static final int ACC_PUBLIC = 0X0001;        //class     field       method
    public static final int ACC_PRIVATE = 0X002;        //          field       method
    public static final int ACC_PROTECTED = 0X0004;     //          field       method
    public static final int ACC_STATIC = 0X0008;        //          field       method
    public static final int ACC_FINAL = 0X0010;         //class     field       method
    public static final int ACC_SUPER = 0X0020;         //class
    public static final int ACC_SYNCHRONIZED = 0X0020;  //                      method
    public static final int ACC_VOLATILE = 0X0040;      //          field
    public static final int ACC_BRIDGE = 0X0040;        //                      method
    public static final int ACC_TRANSIENT = 0X0080;     //          field
    public static final int ACC_VARARGS = 0X0080;       //                      method
    public static final int ACC_NATIVE = 0X0100;        //                      method
    public static final int ACC_INTERFACE = 0X0200;     //class
    public static final int ACC_ABSTRACT = 0X0400;      //class                 method
    public static final int ACC_STRICT = 0X0800;        //                      method
    public static final int ACC_SYNTHETIC = 0X1000;     //class     field       method
    public static final int ACC_ANNOTATION = 0X2000;    //class
    public static final int ACC_ENUM = 0X4000;          //class     field
}
