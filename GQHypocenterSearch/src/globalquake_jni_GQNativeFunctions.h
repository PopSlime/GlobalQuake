/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class globalquake_jni_GQNativeFunctions */

#ifndef _Included_globalquake_jni_GQNativeFunctions
#define _Included_globalquake_jni_GQNativeFunctions
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     globalquake_jni_GQNativeFunctions
 * Method:    copyPTravelTable
 * Signature: ([[FF)Z
 */
JNIEXPORT jboolean JNICALL Java_globalquake_jni_GQNativeFunctions_copyPTravelTable(JNIEnv *, jclass, jobjectArray, jfloat);

/*
 * Class:     globalquake_jni_GQNativeFunctions
 * Method:    isTravelTableReady
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_globalquake_jni_GQNativeFunctions_isTravelTableReady(JNIEnv *, jclass);

/*
 * Class:     globalquake_jni_GQNativeFunctions
 * Method:    initCUDA
 * Signature: ([F)Z
 */
JNIEXPORT jboolean JNICALL Java_globalquake_jni_GQNativeFunctions_initCUDA(JNIEnv *, jclass, jfloatArray);

/*
 * Class:     globalquake_jni_GQNativeFunctions
 * Method:    getAllocationSize
 * Signature: (IIF)J
 */
JNIEXPORT jlong JNICALL Java_globalquake_jni_GQNativeFunctions_getAllocationSize(JNIEnv *, jclass, jint, jint, jfloat);

/*
 * Class:     globalquake_jni_GQNativeFunctions
 * Method:    findHypocenter
 * Signature: ([FFFJIF)[F
 */
JNIEXPORT jfloatArray JNICALL Java_globalquake_jni_GQNativeFunctions_findHypocenter(JNIEnv *, jclass, jfloatArray, jfloat, jfloat, jlong, jint, jfloat, jfloat);

#ifdef __cplusplus
}
#endif
#endif
