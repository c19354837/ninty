package com.ninty.runtime.heap;

/**
 * Created by ninty on 2017/7/25.
 */
public class ClassConstant {
    public static final int ACC_PUBLIC = 0X0001;        //class     field       method
    public static final int ACC_PRIVATE = 0X002;        //          field       method
    public static final int ACC_PROTECTED = 0X0004;     //          field       method
    public static final int ACC_STATIC = 0X0008;        //          field       method
    public static final int ACC_FINAL = 0X0001;         //class     field       method
    public static final int ACC_SUPER = 0X0001;         //class
    public static final int ACC_SYNCHRONIZED = 0X0001;  //                      method
    public static final int ACC_VOLATILE = 0X0001;      //          field
    public static final int ACC_BRIDGE = 0X0001;        //                      method
    public static final int ACC_TRANSIENT = 0X0001;     //          field
    public static final int ACC_VARARGS = 0X0001;       //                      method
    public static final int ACC_NATIVE = 0X0001;        //                      method
    public static final int ACC_INTERFACE = 0X0001;     //class
    public static final int ACC_ABSTRACT = 0X0001;      //class                 method
    public static final int ACC_STRICT = 0X0001;        //                      method
    public static final int ACC_SYNTHETIC = 0X0001;     //class     field       method
    public static final int ACC_ANNOTATION = 0X0001;    //class
    public static final int ACC_ENUM = 0X0001;          //class     field
}
