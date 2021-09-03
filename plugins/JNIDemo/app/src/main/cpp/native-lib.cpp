#include <jni.h>
#include <string>
#include<android/log.h>

#define TAG "myDemo-jni" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // 定义LOGF类型


extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_jnidemo_MainActivity_getBytesFromJNI(JNIEnv *env, jobject thiz,
                                                   jbyteArray byte_array) {
    jint byte_array_size = env->GetArrayLength(byte_array);
    jbyte *p_byte_array = (*env).GetByteArrayElements(byte_array, nullptr);

    int i;
    for (i = 0; i < byte_array_size; i++)
        LOGI("[%d]", *(p_byte_array + i));

    env->ReleaseByteArrayElements(byte_array, p_byte_array, NULL);

    jbyteArray ret = env->NewByteArray(byte_array_size);
    env->SetByteArrayRegion (ret, 0, byte_array_size, p_byte_array);

    //获取class对象
    jclass jDChannel = env->FindClass("com/example/jnidemo/DChannelJava");
    //2 调用java对象中的方法

    //获取class对象中的print方法
    jmethodID getBytes = env->GetStaticMethodID(jDChannel, "callNative", "([B)[B");
    //调用java对象中的print方法
    auto ret_2 = (jbyteArray)(env->CallStaticObjectMethod(jDChannel , getBytes, ret));
    return ret_2;
}