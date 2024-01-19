#include <jni.h>
#include <string>
#include <android/log.h>

#define TAG "Natvie-test-jni" // 这个是自定义的LOG的标识

//定义日志宏 , 其中的 __VA_ARGS__ 表示可变参数
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // 定义LOGF类型

//JNI方法参数
// JNI方法前两个参数分别是JNIEnv和jclass，
// 其中JNIEnv是上下文环境，而jclass是类的实例对象。其他参数为带j开头，比如jint、jstring


//  含义说明
// GetMethodID  GetFieldID 中sig参数是对函数的签名，也可以说标识，具体的格式为
//
//（函数参数）返回值      类型符号对照表：
//
//  Java类型             符号
//  Boolean              Z
//  Byte                 B
//  Char                 C
//  Short                S
//  Integer              I
//  Long                 J
//  Float                F
//  Double               D
//  Void                 V
// Object对象      L开头，包名/类名，”;”结尾,$标识嵌套类(举个例子 String -> Ljava/lang/String;)
//   数组              [内部类型  (举个例子 String[] -> [Ljava/lang/String;)
//———————————————— Clang-Tidy: Function 'showJavaTips' has a definition with different parameter names

void showJavaTips(JNIEnv *env, jstring content);

extern "C" JNIEXPORT jstring JNICALL
Java_com_dpjh_nativelib_NativeLib_stringFromJNI(JNIEnv *env, jobject /* this */) {
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
    jfieldID rf = env->GetFieldID(clazz, "int_1", "I");
    env->SetIntField(thiz, rf, 100);

    // 反射调用Java方法
    jmethodID methodId = env->GetMethodID(clazz2, "setInt2", "(I)V");
    env->CallVoidMethod(thiz, methodId, 188);


    // 字符串操作    可以调用GetStringUTFChars()，使用完毕不要忘记释放资源，否则导致内存泄漏
    const char *strs = env->GetStringUTFChars(str, JNI_FALSE);
    int len = env->GetStringUTFLength(str);
    // printf("from_java_str=%s,len=%d",strs,len);

    //打印日志到 Logcat
    __android_log_print(ANDROID_LOG_ERROR, TAG,
                        "native log     ---  from_java_str=%s,len=%d", strs, len);


    // 释放资源
    env->ReleaseStringUTFChars(str, strs);

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


JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *javaVm, void *reserved) {


    return JNI_VERSION_1_4;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_dpjh_nativelib_NativeLib_isBeingDebug(JNIEnv *env, jobject thiz) {
    // native 调用 java 方法
    // 获取 Debug class对象
    jclass debugClass = env->FindClass("android/os/Debug");
    // 获取 isDebuggerConnected 方法
    jmethodID methodId = env->GetStaticMethodID(debugClass, "isDebuggerConnected", "()Z");
    // 调用该方法
    jboolean isDebug = env->CallStaticBooleanMethod(debugClass, methodId);

    const char *isDebugStr = isDebug == 0 ? "false" : "true";

    // __android_log_print(ANDROID_LOG_ERROR, "Natvie-test","native log     ---  isDebuggerConnected=%s", isDebugStr);
    LOGE("native log     ---  isDebuggerConnected=%s", isDebugStr);

    return isDebug;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_dpjh_nativelib_NativeLib_showNativeToast(JNIEnv *env, jobject thiz, jstring content) {

    showJavaTips(env, content);

//   jclass toastClass = env->FindClass("android/widget/Toast");
//
//   jmethodID methodId =env->GetStaticMethodID(toastClass,"makeText", "(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;");
//
//   jclass baseAppClass =  env->FindClass("com/dpdp/testapplication/BaseApp");
//   jfieldID ctxFieldID = env->GetStaticFieldID(baseAppClass,"globalContext","Landroid/content/Context;");
//   jobject ctxObj = env->GetStaticObjectField(baseAppClass,ctxFieldID);
//
//   jobject toastObj = env->CallStaticObjectMethod(toastClass,methodId,ctxObj,content,0);
//
//   jmethodID showMethodID = env->GetMethodID(toastClass,"show", "()V");
//   env->CallVoidMethod(toastObj,showMethodID);
}

void showJavaTips(JNIEnv *env, jstring content) {

    // 练习jni访问java的写法，这里调用java Toast展示

    try {
        jclass toastClass = env->FindClass("android/widget/Toast");

        jmethodID methodId = env->GetStaticMethodID(toastClass, "makeText","(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;");

        jclass baseAppClass = env->FindClass("com/dpdp/testapplication/BaseApp");
        jfieldID ctxFieldID = env->GetStaticFieldID(baseAppClass, "globalContext","Landroid/content/Context;");
        jobject ctxObj = env->GetStaticObjectField(baseAppClass, ctxFieldID);

        jobject toastObj = env->CallStaticObjectMethod(toastClass, methodId, ctxObj, content, 0);

        jmethodID jgravityid = env->GetMethodID(toastClass, "setGravity", "(III)V");
        jclass gravityClass = env->FindClass("android/view/Gravity");
        jfieldID centerId = env->GetStaticFieldID(gravityClass, "CENTER", "I");
        jint centerNum = env->GetStaticIntField(gravityClass, centerId);
        env->CallVoidMethod(toastObj, jgravityid, centerNum, 0, 0);

        jmethodID showMethodID = env->GetMethodID(toastClass, "show", "()V");
        env->CallVoidMethod(toastObj, showMethodID);
    } catch (...) {

    }
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_dpjh_nativelib_NativeLib_encryptVerify(JNIEnv *env, jobject thiz, jstring plain_text) {

    // 练习一下jni访问java的写法，这里用jni翻译一个java里面的md5加密方法

    try {

        jstring mmm = env->NewStringUTF("MD5");
        jclass digestClass = env->FindClass("java/security/MessageDigest");
        jmethodID instanceMethodId = env->GetStaticMethodID(digestClass, "getInstance","(Ljava/lang/String;)Ljava/security/MessageDigest;");
        jobject mdInstance = env->CallStaticObjectMethod(digestClass, instanceMethodId, mmm);

        jclass stringClass = env->FindClass("java/lang/String");
        jmethodID getbytesId = env->GetMethodID(stringClass, "getBytes", "()[B");
        jobject bytes = env->CallObjectMethod(plain_text, getbytesId);

        jmethodID updatemdId = env->GetMethodID(digestClass, "update", "([B)V");
        env->CallVoidMethod(mdInstance, updatemdId, bytes);

        jmethodID digestMdId = env->GetMethodID(digestClass, "digest", "()[B");
        auto bytes22 = (jbyteArray) env->CallObjectMethod(mdInstance, digestMdId);
        // jbyte *  指针指向数组第一个
        jbyte *byby = env->GetByteArrayElements(bytes22, NULL);
        jint b22size = env->GetArrayLength(bytes22);

        jclass stringbuilder_clazz = env->FindClass("java/lang/StringBuilder");
        jmethodID init_mdid = env->GetMethodID(stringbuilder_clazz, "<init>", "(I)V");
        jobject builder_obj = env->NewObject(stringbuilder_clazz, init_mdid, 32);
        jmethodID append_mdid = env->GetMethodID(stringbuilder_clazz, "append","(Ljava/lang/String;)Ljava/lang/StringBuilder;");
        jmethodID tos_mdid = env->GetMethodID(stringbuilder_clazz, "toString","()Ljava/lang/String;");

        jclass int_clazz = env->FindClass("java/lang/Integer");
        jmethodID tohex_mdid = env->GetStaticMethodID(int_clazz, "toHexString","(I)Ljava/lang/String;");

        for (int i = 0; i < b22size; ++i) {
            jbyte *kk = byby + i;
            jint nn;
            LOGE("bian li -> %d  %d", i, *kk);
            if (*kk < 0) {
                nn = *kk + 256;
                env->CallObjectMethod(builder_obj, append_mdid,
                                      env->CallStaticObjectMethod(int_clazz, tohex_mdid, nn));
                LOGE("bian li zeng jia -> %d  %d", i, nn);
            } else if (*kk < 16) {
                env->CallObjectMethod(builder_obj, append_mdid, env->NewStringUTF("0"));
                env->CallObjectMethod(builder_obj, append_mdid,
                                      env->CallStaticObjectMethod(int_clazz, tohex_mdid, *kk));
            } else {
                env->CallObjectMethod(builder_obj, append_mdid,
                                      env->CallStaticObjectMethod(int_clazz, tohex_mdid, *kk));
            }
        }
        auto md5sss = (jstring) env->CallObjectMethod(builder_obj, tos_mdid);

        return md5sss;
    } catch (...) {

    }
    return env->NewStringUTF("");
}



extern "C"
JNIEXPORT void JNICALL
Java_kim_hsl_jni_MainActivity_jniArrayTest(JNIEnv *env, jobject instance, jintArray intArray_,
                                           jobjectArray stringArray) {

    // I . 基本类型数组操作


    // 1 . jboolean 类型

    /*
        jboolean 类型的值可以设置成 true 或 false , 也可以不设置
        如果将值传递给 GetIntArrayElements 方法 , 需要将 isCopy 的地址放在第二个参数位置
        当做参数的格式 : env->GetIntArrayElements(intArray_, &isCopy);
        可取值 JNI_FALSE 0 和 JNI_TRUE 1 两个值
     */
    jboolean isCopy = JNI_TRUE;


    //2 . GetIntArrayElements 方法参数解析

    /*
        GetIntArrayElements 方法参数解析
            方法作用 : 将 Java 的 int 数组 , 转为 jint 数组 , 返回一个指针指向 jint 数组首元素地址

        函数原型 : jint* GetIntArrayElements(jintArray array, jboolean* isCopy)

        第一个参数 : jintArray array 是参数中的 jintArray 类型变量

            jintArray 类型说明 :
                class _jobject {};                      C ++ 中定义了 _jobject 类
                class _jarray : public _jobject {};     定义 _jarray 类 继承 _jobject 类
                                                        public 继承 : 父类成员在子类中访问级别不变
                class _jintArray : public _jarray {};   定义 _jintArray 类 继承 _jarray 类
                typedef _jintArray*     jintArray;      将 _jintArray* 类型 声明成 jintArray 类型

        第二个参数 : jboolean* isCopy

            该参数用于指定将 jintArray 类型的变量 , 转为 jint * 指针类型的变量 , 新的指针变量的生成方式

            将 该参数设置成指向 JNI_TRUE 的指针 : 将 int 数组数据拷贝到一个新的内存空间中 , 并将该内存空间首地址返回
            将 该参数设置成指向 JNI_FALSE 的指针 : 直接使用 java 中的 int 数组地址 , 返回 java 中的 int 数组的首地址
            将 该参数设置成 NULL ( 推荐 ) : 表示不关心如何实现 , 让系统自动选择指针生成方式 , 一般情况下都不关心该生成方式


        注意如果是 其它类型的数组

            如果是布尔类型的数组 , 使用 GetBooleanArrayElements 方法
            如果是浮点型的数组 , 使用 GetFloatArrayElements 方法
            如果是字符型的数组 , 使用 GetCharArrayElements 方法
            ...

     */

    jint *intArray = env->GetIntArrayElements(intArray_, NULL);

    //3 . 操作 jint * 指针变量 , 循环获取数组中每个元素的值

    /*
       获取数组长度
         函数原型 : jsize GetArrayLength(jarray array)

         返回值类型 jsize :
            jsize 类型 : 由下面可知 jsize 只是 int 类型的别名
                typedef jint            jsize;
                typedef int32_t         jint;
                typedef __int32_t       int32_t;
                typedef int             __int32_t;

     */
    jsize len = env->GetArrayLength(intArray_);

    //4 . 循环打印 int 数组中的元素

    /*
        使用指针进行访问
        intArray 是数组首元素地址
        intArray + 1 是第 1 个元素的首地址
        intArray + k 是第 k 个元素的首地址

        使用 *(intArray + k) 可以获取第 k 个元素的值
     */
    for (int i = 0; i < len; i++) {

        //获取第 i 个元素的首地址 , 使用 *num 可以获取第 i 个元素的值
        int *num = intArray + i;

        /*
            __android_log_print 打印 Android 日志函数
                函数原型 : int __android_log_print(int prio, const char* tag, const char* fmt, ...)

                int prio 参数 : 日志的等级 , 定义在 jni.h 的 android_LogPriority 枚举中
                                ANDROID_LOG_VERBOSE
                                ANDROID_LOG_DEBUG
                                ANDROID_LOG_INFO
                                ANDROID_LOG_WARN
                                ANDROID_LOG_ERROR

                const char* tag 参数 : 日志打印的 TAG 标签 , 这是一个 C/C++ char* 类型字符串

                const char* fmt, ... 参数 : 可变参数
         */
        __android_log_print(ANDROID_LOG_INFO, "JNI_TAG", "%d . %d", i, *num);

        //修改数组中的值
        *num = 8888;

    }

    //5 . 释放 jint * 类型的指针变量

    /*

        函数原型 : void ReleaseIntArrayElements(jintArray array, jint* elems, jint mode)

        第一参数 jintArray array : 是 Java 层传入的 int 数组 参数 , 即 Native 层的调用函数的参数
        第二参数 jint* elems : 通过 GetIntArrayElements 方法将 jintArray 变量转成的 jint* 变量

        第三参数 jint mode : 设置处理模式 , 有三种处理模式

            模式 0 :                  刷新 Java 数组 , 释放 C/C++ 数组
            模式 1 ( JNI_COMMIT ) :   刷新 Java 数组 , 不释放 C/C ++ 数组
            模式 2 ( JNI_ABORT ) :   不刷新 Java 数组 , 释放 C/C++ 数组

        下面是 jni.h 中的定义的模式 :
        #define JNI_COMMIT      1            copy content, do not free buffer
        #define JNI_ABORT       2            free buffer w/o copying back

        如果设置 0 和 1 , 那么 如果修改了 int 数组的值 , 那么最终 Java 层的值会被修改
        如果设置 2 , 那么 如果修改了 int 数组的值 , 那么最终 Java 层的值不会被修改

     */
    env->ReleaseIntArrayElements(intArray_, intArray, 0);

}