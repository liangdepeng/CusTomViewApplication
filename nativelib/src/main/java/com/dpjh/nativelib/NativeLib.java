package com.dpjh.nativelib;

import android.util.Log;

public class NativeLib {

    private final static String tag = "Natvie-test";

    // Used to load the 'nativelib' library on application startup.
    static {
        System.loadLibrary("nativelib");
    }

    /**
     * A native method that is implemented by the 'nativelib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


    public native String getStringForNative(String str);


    public native void invokeJni();

    public NativeLib() {

    }

    public void test(){
        Log.e(tag,"native方法 执行之前的 int_1 "+int_1);
        Log.e(tag,"native方法 执行之前的 int_2 "+int_2);
        String string = getStringForNative("java---java----层");
        Log.e(tag,"native 返回的字符串  "+string);
        Log.e(tag,"native方法 执行之后的 int_1 "+int_1);
        Log.e(tag,"native方法 执行之后的 int_2 "+int_2);


    }

    private int int_1 = 9955842;

    private int int_2 = 324;

    public String str2 = "java_public_str_2";


    public void setInt2(int num) {
        this.int_2 = num;
        Log.e(tag,"native invoke ->"+num);
    }
}