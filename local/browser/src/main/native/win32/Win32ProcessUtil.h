#include <jni.h>

#ifndef _Included_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil
#define _Included_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil
#ifdef __cplusplus
extern "C" {
#endif
/* Inaccessible static: loaded */
/* Inaccessible static: LOGGER */

JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_createModuleSnapshot(JNIEnv*, jobject, jint);
JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_createProcessSnapshot(JNIEnv*, jobject);
JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_deleteModuleSnapshot(JNIEnv*, jobject);
JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_deleteProcessSnapshot(JNIEnv*, jobject);
JNIEXPORT jint JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_getCurrentProcessId(JNIEnv*, jobject);
JNIEXPORT jstring JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_getModuleSnapshotName(JNIEnv*, jobject);
JNIEXPORT jstring JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_getModuleSnapshotExePath(JNIEnv*, jobject);
JNIEXPORT jint JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_getModuleSnapshotProcessId(JNIEnv*, jobject);
JNIEXPORT jint JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_getProcessSnapshotId(JNIEnv*, jobject);
JNIEXPORT jint JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_getProcessSnapshotParentId(JNIEnv*, jobject);
JNIEXPORT jstring JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_getProcessSnapshotExeFile(JNIEnv*, jobject);
JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_nextSnapshotModule(JNIEnv*, jobject);
JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_nextSnapshotProcess(JNIEnv*, jobject);
JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_terminate(JNIEnv*, jobject, jint);

#ifdef __cplusplus
}
#endif

/**
 * Create a com bstr reference from a java string reference.
 *
 * @param env A <code>JNIEnv*</code> jni environment.
 * @param string A <code>jstring</code>.
 */
CComBSTR getCComBSTR(JNIEnv*, jstring);

#endif
