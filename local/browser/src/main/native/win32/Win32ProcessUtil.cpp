#include <atlbase.h>
#include <tlhelp32.h>
#include "Win32ProcessUtil.h"

/* a global process snapshot handle */
HANDLE hProcessSnapshot;

/* a global module snapshot handle */
HANDLE hModuleSnapshot;

/* a global snapshot process */
PROCESSENTRY32 lppe;

/* a global module snapshot */
MODULEENTRY32 lpme;

JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_createModuleSnapshot(JNIEnv* env, jobject refThis, jint proccessId) {
	/* ensure only a single snapshot is referenced */
	if (hModuleSnapshot != NULL) {
		if (Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_deleteModuleSnapshot(env, refThis) == JNI_FALSE) {
			return JNI_FALSE;
		}
	}
	/* create a snapshot of the module list and store the reference */
	hModuleSnapshot = CreateToolhelp32Snapshot(TH32CS_SNAPMODULE, proccessId);
	lpme.dwSize = sizeof(MODULEENTRY32);
	if (hModuleSnapshot == INVALID_HANDLE_VALUE) {
		hModuleSnapshot = NULL;
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_createProcessSnapshot(JNIEnv* env, jobject refThis) {
	/* ensure only a single snapshot is referenced */
	if (hProcessSnapshot != NULL) {
		if (Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_deleteProcessSnapshot(env, refThis) == JNI_FALSE) {
			return JNI_FALSE;
		}
	}
	/* create a snapshot of the process list and store the reference */
	hProcessSnapshot = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);
	lppe.dwSize = sizeof(PROCESSENTRY32);
	if (hProcessSnapshot == INVALID_HANDLE_VALUE) {
		hProcessSnapshot = NULL;
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_deleteModuleSnapshot(JNIEnv* env, jobject refThis) {
	if (CloseHandle(hModuleSnapshot)) {
		hModuleSnapshot = NULL;
		return JNI_TRUE;
	} else {
		hModuleSnapshot = NULL;
		return JNI_FALSE;
	}
}

JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_deleteProcessSnapshot(JNIEnv* env, jobject refThis) {
	if (CloseHandle(hProcessSnapshot)) {
		hProcessSnapshot = NULL;
		return JNI_TRUE;
	} else {
		hProcessSnapshot = NULL;
		return JNI_FALSE;
	}
}

JNIEXPORT jint JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_getCurrentProcessId(JNIEnv* env, jobject refThis) {
	return GetCurrentProcessId();
}

JNIEXPORT jstring JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_getModuleSnapshotName(JNIEnv* env, jobject refThis) {
	return env->NewString((jchar*) lpme.szModule, (jsize) wcslen(lpme.szModule));
}

JNIEXPORT jint JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_getModuleSnapshotProcessId(JNIEnv* env, jobject refThis) {
	return lpme.th32ProcessID;
}

JNIEXPORT jstring JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_getModuleSnapshotExePath(JNIEnv* env, jobject refThis) {
	return env->NewString((jchar*) lpme.szExePath, (jsize) wcslen(lpme.szExePath));
}

JNIEXPORT jint JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_getProcessSnapshotParentId(JNIEnv* env, jobject refThis) {
	return lppe.th32ParentProcessID;
}

JNIEXPORT jint JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_getProcessSnapshotId(JNIEnv* env, jobject refThis) {
	return lppe.th32ProcessID;
}

JNIEXPORT jstring JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_getProcessSnapshotExeFile(JNIEnv* env, jobject refThis) {
	return env->NewString((jchar*) lppe.szExeFile, (jsize) wcslen(lppe.szExeFile));
}

JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_nextSnapshotModule(JNIEnv* env, jobject refThis) {
	if (NULL == hModuleSnapshot) {
		return JNI_FALSE;
	} else {
		if (Module32Next(hModuleSnapshot, &lpme)) {
			return JNI_TRUE;
		} else {
			return JNI_FALSE;
		}
	}
}

JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_nextSnapshotProcess(JNIEnv* env, jobject refThis) {
	if (NULL == hProcessSnapshot) {
		return JNI_FALSE;
	} else {
		if (Process32Next(hProcessSnapshot, &lppe)) {
			return JNI_TRUE;
		} else {
			return JNI_FALSE;
		}
	}
}

JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_support_util_process_win32_Win32ProcessUtil_terminate(JNIEnv* env, jobject refThis, jint processId) {
	/* open a process handle for termination */
    HANDLE hProcess = OpenProcess(PROCESS_TERMINATE, TRUE, processId);
    if (NULL == hProcess) {
        return JNI_FALSE;
    }
    /* terminate */
    HRESULT result = TerminateProcess(hProcess, 1);
    if (FAILED(result)) {
        return JNI_FALSE;
	} else {
		/* close the process handle */
		CloseHandle(hProcess);
		return JNI_TRUE;
	}
}
