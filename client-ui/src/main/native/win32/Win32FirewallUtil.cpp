#include <atlbase.h>
#include "Win32FirewallUtil.h"

JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_browser_util_firewall_win32_Win32FirewallUtil_addExecutableNative(JNIEnv* env, jobject refThis, jstring executableName, jstring executablePath) {
    return addExecutable(env, executableName, executablePath);
}

JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_browser_util_firewall_win32_Win32FirewallUtil_isEnabledNative(JNIEnv* env, jobject refThis) {
    return isEnabled();
}

JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_browser_util_firewall_win32_Win32FirewallUtil_isExceptionAllowedNative(JNIEnv* env, jobject refThis) {
    return isExceptionAllowed();
}

JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_browser_util_firewall_win32_Win32FirewallUtil_isExecutableEnabledNative(JNIEnv* env, jobject refThis, jstring path) {
    return isExecutableEnabled(env, path);
}

JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_browser_util_firewall_win32_Win32FirewallUtil_removeExecutableNative(JNIEnv* env, jobject refThis, jstring path) {
    return removeExecutable(env, path);
}

jboolean addExecutable(JNIEnv* env, jstring name, jstring path) {
    Win32Firewall win32Firewall;
    if (win32Firewall.addExecutable(getCComBSTR(env, name), getCComBSTR(env, path))) {
        return JNI_TRUE;
    } else {
        return JNI_FALSE;
    }
}

jboolean isEnabled() {
    Win32Firewall win32Firewall;
    if (win32Firewall.isEnabled()) {
        return JNI_TRUE;
    } else {
        return JNI_FALSE;
    }
}

jboolean isExceptionAllowed() {
    Win32Firewall win32Firewall;
    if (win32Firewall.isExceptionAllowed()) {
        return JNI_TRUE;
    } else {
        return JNI_FALSE;
    }
}

jboolean isExecutableEnabled(JNIEnv* env, jstring path) {
    Win32Firewall win32Firewall;
    if (win32Firewall.isExecutableEnabled(getCComBSTR(env, path))) {
        return JNI_TRUE;
    } else {
        return JNI_FALSE;
    }
}

jboolean removeExecutable(JNIEnv* env, jstring path) {
    Win32Firewall win32Firewall;
    if (win32Firewall.removeExecutable(getCComBSTR(env, path))) {
        return JNI_TRUE;
    } else {
        return JNI_FALSE;
    }
}

CComBSTR getCComBSTR(JNIEnv* env, jstring string) {
    const int length = env->GetStringLength(string);
    const jchar* characters = env->GetStringChars(string, JNI_FALSE);
    /* NOTE - i have no idea how this cast will behave in a non ASCII
     * environment */
    BSTR temp = SysAllocStringLen((OLECHAR*) characters, length);
    env->ReleaseStringChars(string, characters);
    CComBSTR bstr(temp);
    return bstr;
}

/**
 * Add an executable to the firewall exception list.
 *
 * @param name A <code>CComBSTR</code> exception entry name.
 * @param path A <code>CComBSTR</code> exception entry path.
 * @return True if the excutable was added, false othwerwise.
 */
bool Win32Firewall::addExecutable(CComBSTR name, CComBSTR path) {
    // initialize the firewall interface
    if (!initialize()) {
        return false;
    }

    // create an authorized application
    HRESULT result = CoCreateInstance(__uuidof(NetFwAuthorizedApplication),
        NULL, CLSCTX_INPROC_SERVER, __uuidof(INetFwAuthorizedApplication),
        (void **) &firewallAuthorizedApplication);
    if (FAILED(result)) {
        return false;
    }

    // set application name
    result = firewallAuthorizedApplication->put_Name(name);
    if (FAILED(result)) {
        return false;
    }

    // set application scope
    NET_FW_SCOPE scope = NET_FW_SCOPE_ALL;
    result = firewallAuthorizedApplication->put_Scope(scope);
    if (FAILED(result)) {
        return false;
    }

    // enable application
    VARIANT_BOOL isEnabled = VARIANT_TRUE;
    result =  firewallAuthorizedApplication->put_Enabled(isEnabled);
    if (FAILED(result)) {
        return false;
    }

    // set the application process image name
    result = firewallAuthorizedApplication->put_ProcessImageFileName(path);
    if (FAILED(result)) {
        return false;
    }
 
    // add the application
    result = firewallAuthorizedApplications->Add(firewallAuthorizedApplication);
    if (FAILED(result)) {
        return false;
    }

    return true;
}

/**
 * Initialize the firewall interface.
 *
 */
bool Win32Firewall::initialize() {
    // initialize COM
    HRESULT result = CoInitialize(NULL);
    if (FAILED(result)) {
        return false;
    }

    // create firewall manager
    result = CoCreateInstance(__uuidof(NetFwMgr), NULL, CLSCTX_INPROC_SERVER,
        __uuidof(INetFwMgr), (void **) & firewallManager);
    if (FAILED(result) || !firewallManager) {
        return false;
    }

    // get local firewall policy
    result = firewallManager->get_LocalPolicy(&firewallPolicy);
    if (FAILED(result) || !firewallPolicy) {
        return false;
    }

    // get current firewall profile
    result = firewallPolicy->get_CurrentProfile(&firewallProfile);
    if (FAILED(result) || !firewallProfile) {
        return false;
    }

    // get authorized applications
    result = firewallProfile->get_AuthorizedApplications(&firewallAuthorizedApplications);
    if (FAILED(result) || !firewallAuthorizedApplications) {
        return false;
    }

    return true;
}

/**
 * Determine whether or not the firewall is enabled.
 *
 * @return True if the firewall is enabled.
 */
bool Win32Firewall::isEnabled() {
    // initialize the firewall interface
    if (!initialize()) {
        return false;
    }
    // get enabled
    VARIANT_BOOL isEnabled;
    HRESULT result = firewallProfile->get_FirewallEnabled(&isEnabled);
    if (FAILED(result)) {
        return false;
    }

    if (isEnabled == VARIANT_TRUE) {
        return true;
    } else {
        return false;
    }
}

/**
 * Determine whether or not an exception can be added to the
 * firewall.
 *
 * @return True if an exception can be added.
 */
bool Win32Firewall::isExceptionAllowed() {
    // initialize the firewall interface
    if (!initialize()) {
        return false;
    }

    // get allowed
    VARIANT_BOOL isAllowed;
    HRESULT result = firewallProfile->get_ExceptionsNotAllowed(&isAllowed);
    if (FAILED(result)) {
        return false;
    }

    if (isAllowed == VARIANT_TRUE) {
        return false;
    }

    return true;
}

/**
 * Determine whether or not the executable path within the firewall has been
 * enabled.  This means that the executable must be listed, it must be
 * listed and the scope must be all.
 *
 * @param executablePath
 *      An executable path <code>jstring</code>.
 */
bool Win32Firewall::isExecutableEnabled(CComBSTR path) {
    // initialize the firewall interface
    if (!initialize()) {
        return false;
    }
    // determine whether or not the application is listed
    HRESULT result = firewallAuthorizedApplications->Item(path,
        &firewallAuthorizedApplication);
    if (FAILED(result)) {
        return  false;
    }
    if (!firewallAuthorizedApplication) {
        return false;
    }

    // determine whether or not the application is enabled
    VARIANT_BOOL enabled;
    result = firewallAuthorizedApplication->get_Enabled(&enabled);
    if (FAILED(result)) {
        return false;
    }
    if (VARIANT_FALSE == enabled) {
        return false;
    }

    // determine whether or not the scope is all
    NET_FW_SCOPE scope;
    result = firewallAuthorizedApplication->get_Scope(&scope);
    if (FAILED(result)) {
        return false;
    }
    if (NET_FW_SCOPE_ALL != scope) {
        return false;
    }

    return true;
}

/**
 * Remove an executable from the firewall.
 *
 * @param path A <code>CComBSTR</code> path to the executable.
 * @return True if the executable could be removed, false
 *      otherwise.
 */
bool Win32Firewall::removeExecutable(CComBSTR path) {
    // initialize the firewall interface
    if (!initialize()) {
        return false;
    }
    // remove the application
    HRESULT result = firewallAuthorizedApplications->Remove(path);
    if (FAILED(result)) {
        return false;
    }

    return true;
}
