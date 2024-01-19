package com.dpjh.nativelib;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Native JNI 练习
 */
public class NativeLib {

    private final static String tag = "Natvie-test-java";
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final ExecutorService executor = Executors.newCachedThreadPool();

    // Used to load the 'nativelib' library on application startup.
    static {
        System.loadLibrary("nativelib");
    }

    /**
     * A native method that is implemented by the 'nativelib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


    // test
    public native String getStringForNative(String str);

   // test
    public native void invokeJni();

    public native String encryptVerify(String plainText);

    //test
    public native boolean isBeingDebug();

    // test
    public native void showNativeToast(String content);

    public NativeLib() {
        executor.execute(() -> {
            while (true) {
                if (isBeingDebug()) {
                    handler.post(() ->
                            //Toast.makeText(getAppContext(), "警告-设备正在被调试", Toast.LENGTH_SHORT).show()
                            showNativeToast("警告-设备正在被调试")
                    );
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void test() {
        Log.e(tag, "native方法 执行之前的 int_1 " + int_1);
        Log.e(tag, "native方法 执行之前的 int_2 " + int_2);
        String string = getStringForNative("java---java----层");
        Log.e(tag, "native 返回的字符串  " + string);
        Log.e(tag, "native方法 执行之后的 int_1 " + int_1);
        Log.e(tag, "native方法 执行之后的 int_2 " + int_2);

        Log.e(tag, encryptVerify("asdssdf")+ "  jni 生成  ");
        Log.e(tag, md5("asdssdf")+ "   java 生成 ");

    }

    private int int_1 = 9955842;

    private int int_2 = 324;

    public String str2 = "java_public_str_2";


    public void setInt2(int num) {
        this.int_2 = num;
        Log.e(tag, "native invoke ->" + num);

        new Toast(getAppContext()).setGravity(Gravity.CENTER,0,0);
    }

    private Context context;

    private Context getAppContext() {
        if (context == null) {
            try {
                Class<?> aClass = Class.forName("com.dpdp.testapplication.BaseApp");
                Field field = aClass.getField("globalContext");
                context = (Context) field.get(null);
                Log.e(tag, "反射获取 AppContext ->" + context.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(tag, "反射获取 AppContext Exception ->" + e.toString());
            }
        }
        return context;
    }


    public static String md5(String plainText) {
        if (null == plainText) {
            plainText = "";
        }
        String MD5Str = "";
        try {
            // JDK 6 支持以下6种消息摘要算法，不区分大小写  验证
            // md5,sha(sha-1),md2,sha-256,sha-384,sha-512
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuilder builder = new StringBuilder(32);

            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    builder.append("0");
                builder.append(Integer.toHexString(i));
            }
            MD5Str = builder.toString();
            // LogUtil.println("result: " + buf.toString());// 32位的加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return MD5Str;
    }
}