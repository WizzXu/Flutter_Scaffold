#include <jni.h>
#include <string>
#include<android/log.h>
#include <pthread.h>
#include "d_channel.h"
#include "jni_object_ref.h"

#define TAG "myDemo-jni" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // 定义LOGF类型

#define DART_API extern "C" __attribute__((visibility("default"))) __attribute__((used))

static JavaVM *gJvm = nullptr;
static jmethodID gFindClassMethod;
static JavaGlobalRef<jobject> *gClassLoader = nullptr;
static pthread_key_t detachKey = 0;
static jclass gNativeClass;

static char nativeClassname[] = "com/github/hchannel/HChannelJava";

jclass _findClass(JNIEnv *env, const char *name);

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_github_hchannel_HchannelPlugin_getBytesFromJNI(JNIEnv *env, jobject thiz,
                                                        jbyteArray byte_array) {

    env->GetJavaVM(&gJvm);

    jint byte_array_size = env->GetArrayLength(byte_array);
    jbyte *p_byte_array = (*env).GetByteArrayElements(byte_array, nullptr);

    int i;
    for (i = 0; i < byte_array_size; i++)
        LOGE("[%d]", *(p_byte_array + i));

    env->ReleaseByteArrayElements(byte_array, p_byte_array, NULL);

    jbyteArray ret = env->NewByteArray(byte_array_size);
    env->SetByteArrayRegion(ret, 0, byte_array_size, p_byte_array);

    //获取class对象
    jclass jDChannel = env->FindClass("com/github/hchannel/HChannelJava");
    //2 调用java对象中的方法

    //获取class对象中的print方法
    jmethodID callNative = env->GetStaticMethodID(jDChannel, "callNative", "([B)[B");
    //调用java对象中的print方法
    auto ret_2 = (jbyteArray)(env->CallStaticObjectMethod(jDChannel, callNative, ret));
    return ret_2;
}


void _detachThreadDestructor(void *arg) {
    LOGI("detach from current thread");
    gJvm->DetachCurrentThread();
    detachKey = 0;
}


/// each thread attach once
JNIEnv *_getEnv() {
    if (gJvm == nullptr) {
        return nullptr;
    }

    if (detachKey == 0) {
        pthread_key_create(&detachKey, _detachThreadDestructor);
    }
    JNIEnv *env;
    jint ret = gJvm->GetEnv((void **) &env, JNI_VERSION_1_6);
    int attachRet;

    switch (ret) {
        case JNI_OK:
            return env;
        case JNI_EDETACHED:
            attachRet = gJvm->AttachCurrentThread(&env, nullptr);
            LOGI("attach to current thread: %d", attachRet);

            return env;
        default:
            LOGE("fail to get env");
            return nullptr;
    }
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOGI("JNI_OnLoad");
    gJvm = vm;
    JNIEnv *env = _getEnv();

    /// cache classLoader
    auto plugin = _findClass(env, nativeClassname);
    jclass pluginClass = env->GetObjectClass(plugin);
    auto classLoaderClass = env->FindClass("java/lang/ClassLoader");
    auto getClassLoaderMethod = env->GetMethodID(pluginClass, "getClassLoader",
                                                 "()Ljava/lang/ClassLoader;");
    auto classLoader = env->CallObjectMethod(plugin, getClassLoaderMethod);
    gClassLoader = new JavaGlobalRef<jobject>(env->NewGlobalRef(classLoader), env);
    gFindClassMethod = env->GetMethodID(classLoaderClass, "findClass",
                                        "(Ljava/lang/String;)Ljava/lang/Class;");

    env->DeleteLocalRef(plugin);
    env->DeleteLocalRef(pluginClass);
    env->DeleteLocalRef(classLoaderClass);
    env->DeleteLocalRef(classLoader);
    return JNI_VERSION_1_6;
}

jclass _findClass(JNIEnv *env, const char *name) {
    jclass nativeClass = env->FindClass(name);
    jthrowable exception = env->ExceptionOccurred();
    /// class loader not found class
    if (exception) {
        env->ExceptionClear();
        if (gNativeClass != nullptr){
            return gNativeClass;
        }

        LOGI("FindClass Exception:%s", name);
        jstring nameStringUTF = env->NewStringUTF(name);
        jclass classLoaderFindClass = static_cast<jclass>(env->CallObjectMethod(
                gClassLoader->Object(),
                gFindClassMethod,
                nameStringUTF));
        if (classLoaderFindClass != nullptr) {
            //LOGI("ClassLoader FindClass Success:%s", name);
            gNativeClass = classLoaderFindClass;
        } else {
            //LOGE("ClassLoader FindClass Fail!");
        }
        env->DeleteLocalRef(nameStringUTF);
        return classLoaderFindClass;
    }
    return nativeClass;
}


DART_API ByteArray *callNative(char *data, int length) {
    JNIEnv *env = _getEnv();
    jbyteArray argumentBytes = nullptr;
    if (data != nullptr && length > 0){
        argumentBytes = env->NewByteArray(length);
        env->SetByteArrayRegion(argumentBytes, 0, length, (jbyte *) data);
    }
    //获取class对象
    jclass jHChannel = _findClass(env, nativeClassname);
    char methodName[] = "callNative";
    char methodSig[] = "([B)[B";

    //2 调用java对象中的方法
    jmethodID callNative = env->GetStaticMethodID(jHChannel, methodName, methodSig);
    jbyteArray ret_java = (jbyteArray)(env->CallStaticObjectMethod(jHChannel, callNative,
                                                             argumentBytes));
    ByteArray *result = nullptr;
    if (ret_java != nullptr){
        int ret_java_length = env->GetArrayLength(ret_java);
        //LOGD("ret_java_length:[%d]", ret_java_length);
        result = (ByteArray *) malloc(sizeof(ByteArray));

        result->length = ret_java_length;
        result->data = (char *) malloc(result->length * sizeof(char));
        jbyte *byte = env->GetByteArrayElements(ret_java, nullptr);
        memcpy(result->data, byte, ret_java_length);

        env->ReleaseByteArrayElements(ret_java, byte, 0);
    }
    // 释放
    env->DeleteLocalRef(ret_java);
    env->DeleteLocalRef(argumentBytes);
    //free(methodName);
    //free(methodSig);
    return result;
}

DART_API void freeByteArray(ByteArray *byteArray) {
    if (byteArray != nullptr) {
        free(byteArray->data);
        free(byteArray);
    }
}
