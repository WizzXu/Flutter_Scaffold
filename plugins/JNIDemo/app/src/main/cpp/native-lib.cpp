#include <jni.h>
#include <string>

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_example_jnidemo_MainActivity_bytesFromJNI(JNIEnv *env, jobject thiz) {
    jbyte a[] = {1,2,3,4,5,6};
    jbyteArray ret = env->NewByteArray(6);
    env->SetByteArrayRegion (ret, 0, 6, a);
    return ret;
}