#include <jni.h>
#include <string>
#include <android/log.h>

//JNI方法参数
// JNI方法前两个参数分别是JNIEnv和jclass，
// 其中JNIEnv是上下文环境，而jclass是类的实例对象。其他参数为带j开头，比如jint、jstring

extern "C" JNIEXPORT jstring JNICALL
Java_com_dpjh_nativelib_NativeLib_stringFromJNI(JNIEnv* env,jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_dpjh_nativelib_NativeLib_getStringForNative(JNIEnv *env, jobject thiz, jstring str) {

    // 1、获取类的实例对象
    // 通过类的实例获取
   jclass clazz = env->GetObjectClass(thiz);
   // 通过类加载器查找指定的类
   jclass clazz2 = env->FindClass("com/dpjh/nativelib/NativeLib");

   // 反射调用Java变量
   jfieldID rf= env->GetFieldID(clazz,"int_1","I");
   env ->SetIntField(thiz,rf,100);

   // 反射调用Java方法
   jmethodID methodId = env->GetMethodID(clazz2,"setInt2", "(I)V");
   env->CallVoidMethod(thiz,methodId,188);


   // 字符串操作    可以调用GetStringUTFChars()，使用完毕不要忘记释放资源，否则导致内存泄漏
   const char *strs = env->GetStringUTFChars(str,JNI_FALSE);
   int len = env->GetStringUTFLength(str);
  // printf("from_java_str=%s,len=%d",strs,len);

  //打印日志到 Logcat
    __android_log_print(ANDROID_LOG_ERROR, "Natvie-test", "native log     ---  from_java_str=%s,len=%d", strs,len);


   // 释放资源
   env->ReleaseStringUTFChars(str,strs);;

    std::string hello = "Hello C++";
    const char *teststr = "test_c++";
    // 返回字符串给Java层，使用NewStringUTF()
    return env->NewStringUTF(hello.c_str());
   // return env->NewStringUTF(teststr);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_dpjh_nativelib_NativeLib_invokeJni(JNIEnv *env, jobject thiz) {

}


JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *javaVm,void *reserved){


    return JNI_VERSION_1_4;
}