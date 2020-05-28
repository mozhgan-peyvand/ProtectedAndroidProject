#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_protectedprojectwithcandc_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_protectedprojectwithcandc_MainActivity_getValueJNI(
        JNIEnv *env,
        jobject,
        jstring value
) {

    std::string tempValue = "mozhgan";

    char key[3] = {'K', 'C', 'Q'}; //Any chars will work, in an array of any size

    std:: string output = tempValue;

    for (int i = 0; i < tempValue.size(); i++)
        output[i] = tempValue[i] ^ key[i % (sizeof(key) / sizeof(char))];

    return env->NewStringUTF(output.c_str());
}

