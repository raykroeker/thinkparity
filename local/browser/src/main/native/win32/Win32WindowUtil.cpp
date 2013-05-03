#include <windows.h>
#include <assert.h>
#include <jawt.h>
#include <jawt_md.h>
#include "Win32WindowUtil.h"

/*
 * Class:     com_thinkparity_ophelia_browser_util_window_win32_Win32WindowUtil
 * Method:    CreateRectRgn
 * Signature: (IIII)J
 */
JNIEXPORT jint JNICALL
Java_com_thinkparity_ophelia_browser_util_window_win32_Win32WindowUtil_CreateRectRgn(JNIEnv* env, jobject refThis, jint nLeftRect, jint nTopRect, jint nRightRect, jint nBottomRect)
{
    return (int) CreateRectRgn(nLeftRect, nTopRect, nRightRect, nBottomRect);
}

/*
 * Class:     com_thinkparity_ophelia_browser_util_window_win32_Win32WindowUtil
 * Method:    CreateRoundRectRgn
 * Signature: (IIIIII)J
 */
JNIEXPORT jint JNICALL
Java_com_thinkparity_ophelia_browser_util_window_win32_Win32WindowUtil_CreateRoundRectRgn(JNIEnv* env, jobject refThis, jint nLeftRect, jint nTopRect, jint nRightRect, jint nBottomRect, jint nWidthEllipse, jint nHeightEllipse)
{
    return (int) CreateRoundRectRgn(nLeftRect, nTopRect, nRightRect, nBottomRect, nWidthEllipse, nHeightEllipse);
}

/*
 * Class:     com_thinkparity_ophelia_browser_util_window_win32_Win32WindowUtil
 * Method:    GetWindowHandle
 * Signature: (Ljava/awt/Window;)J
 */
JNIEXPORT jint JNICALL
Java_com_thinkparity_ophelia_browser_util_window_win32_Win32WindowUtil_GetWindowHandle(JNIEnv* env, jobject refThis, jobject window)
{
	/* NOTE big fat reminder, in c you need to pre-declare vars at the
	 * beginning of the code block */
	jboolean result;
	jint lock;
    JAWT awt;
    JAWT_DrawingSurface* drawingSurface;
	JAWT_DrawingSurfaceInfo* info;
	JAWT_Win32DrawingSurfaceInfo* win32Info;
	HWND windowHandle;

	// grab awt
    awt.version = JAWT_VERSION_1_3;
	result = JAWT_GetAWT(env, &awt);
    assert(result != JNI_FALSE);

    // grab and lock the drawing surface
    drawingSurface = awt.GetDrawingSurface(env, window);
    assert(drawingSurface != NULL);
    lock = drawingSurface->Lock(drawingSurface);
    assert((lock & JAWT_LOCK_ERROR) == 0);

    // grab info
    info = drawingSurface->GetDrawingSurfaceInfo(drawingSurface);
    win32Info = (JAWT_Win32DrawingSurfaceInfo*) info->platformInfo;

    // grab handle
    windowHandle = win32Info->hwnd;

    // release info
    drawingSurface->FreeDrawingSurfaceInfo(info);
    // unlock drawing surface
    drawingSurface->Unlock(drawingSurface);
    // release drawing surface
    awt.FreeDrawingSurface(drawingSurface);

    return (int) windowHandle;
}

/*
 * Class:     com_thinkparity_ophelia_browser_util_window_win32_Win32WindowUtil
 * Method:    SetWindowRgn
 * Signature: (JJZ)I
 */
JNIEXPORT jint JNICALL
Java_com_thinkparity_ophelia_browser_util_window_win32_Win32WindowUtil_SetWindowRgn(JNIEnv* env, jobject refThis, jint hWnd, jint hRgn, jboolean bRedraw)
{
    return (int) SetWindowRgn((HWND) hWnd, (HRGN) hRgn, bRedraw);
}
