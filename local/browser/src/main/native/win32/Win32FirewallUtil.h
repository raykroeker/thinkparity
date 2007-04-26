#include <jni.h>
#include <netfw.h>

#ifndef _Included_com_thinkparity_ophelia_browser_util_firewall_win32_Win32FirewallUtil
#define _Included_com_thinkparity_ophelia_browser_util_firewall_win32_Win32FirewallUtil
#ifdef __cplusplus
extern "C" {
#endif
/* Inaccessible static: loaded */
/* Inaccessible static: LOGGER */

JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_browser_util_firewall_win32_Win32FirewallUtil_addExecutableNative(JNIEnv*, jobject, jstring, jstring);

JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_browser_util_firewall_win32_Win32FirewallUtil_isEnabledNative(JNIEnv*, jobject);

JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_browser_util_firewall_win32_Win32FirewallUtil_isExceptionAllowedNative(JNIEnv*, jobject);

JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_browser_util_firewall_win32_Win32FirewallUtil_isExecutableEnabledNative(JNIEnv*, jobject, jstring);

JNIEXPORT jboolean JNICALL Java_com_thinkparity_ophelia_browser_util_firewall_win32_Win32FirewallUtil_removeExecutableNative(JNIEnv*, jobject, jstring);

#ifdef __cplusplus
}
#endif
#endif

/**
 * <b>Title:</b>thinkParity OpheliaUI Native Firewall<br>
 * <b>Description:</b><br>
 *
 */
class Win32Firewall {
    public:

        /**
         * Create Win32Firewall.
         *
         */
        Win32Firewall() {
            firewallManager = NULL;
            firewallPolicy = NULL;
            firewallProfile = NULL;
            firewallAuthorizedApplications = NULL;
            firewallAuthorizedApplication = NULL;
        }

        /**
         * Delete Win32Firewall.
         *
         */
        ~Win32Firewall() {
            if (firewallAuthorizedApplication) {
                firewallAuthorizedApplication->Release();
                firewallAuthorizedApplication = NULL;
            }
            if (firewallAuthorizedApplications) {
                firewallAuthorizedApplications->Release();
                firewallAuthorizedApplications = NULL;
            }
            if (firewallProfile) {
                firewallProfile->Release();
                firewallProfile = NULL;
            }
            if (firewallPolicy) {
                firewallPolicy->Release();
                firewallPolicy = NULL;
            }
            if (firewallManager) {
                firewallManager->Release();
                firewallManager = NULL;
            }
        }

        /**
         * Add an executable to the firewall exception list.
         *
         * @param name A <code>CComBSTR</code> exception entry name.
         * @param path A <code>CComBSTR</code> exception entry path.
         * @return True if the excutable was added, false othwerwise.
         */
        bool addExecutable(CComBSTR, CComBSTR);

        /**
         * Initialize the firewall interface.
         *
         */
        bool initialize();

        /**
         * Determine whether or not the firewall is enabled.
         *
         * @return True if the firewall is enabled.
         */
        bool isEnabled();

        /**
         * Determine whether or not an exception can be added to the
         * firewall.
         *
         * @return True if an exception can be added.
         */
        bool isExceptionAllowed();

        /**
         * Determine whether or not the executable path within the firewall has been
         * enabled.
         *
         * @param executablePath
         *      An executable path <code>jstring</code>.
         * @return True if the executable is enabled.
         */
        bool isExecutableEnabled(CComBSTR);

        /**
         * Remove an executable from the firewall.
         *
         * @param path A <code>CComBSTR</code> path to the executable.
         * @return True if the executable could be removed, false
         *      otherwise.
         */
        bool removeExecutable(CComBSTR);

    private:

        /** A <code>INetFwAuthorizedApplications</code>. */
        INetFwAuthorizedApplications* firewallAuthorizedApplications;

        /** A <code>InetFwAuthorizedApplication</code>. */
        INetFwAuthorizedApplication* firewallAuthorizedApplication;

        /** A <code>INetFwMgr</code>. */
        INetFwMgr* firewallManager;

        /** A <code>INetFwPolicy</code>. */
        INetFwPolicy* firewallPolicy;

        /** A <code>INetFwProfile</code>. */
        INetFwProfile* firewallProfile;
};

/**
 * Add an executable to the firewall's exceptions list.
 *
 * @param env A <code>JNIEnv</code> jni environment.
 * @param name A <code>jstring</code> exception entry name.
 * @param path A <code>jstring</code> exception entry path.
 * @return True if the executable could be added, false otherwise.
 */
jboolean addExecutable(JNIEnv*, jstring, jstring);

/**
 * Determine whether or not the firewall is enabled.
 *
 * @return True if the firewall is enabled.
 */
jboolean isEnabled();

/**
 * Determine whether or not exceptions can be added
 * to the firewall.
 *
 * @return True if exceptions can be made, false otherwise.
 */
jboolean isExceptionAllowed();

/**
 * Determine whether or not an executable has been enabled
 * within the firewall.
 *
 * @param env A <code>JNIEnv*</code> jni environment.
 * @param path A <code>jstring</code> path to the executable.
 * @return True if the executable is enabled, false otherwise.
 */
jboolean isExecutableEnabled(JNIEnv*, jstring);

/**
 * Remove an executable from the firewall.
 *
 * @param env A <code>JNIEnv*</code> jni environment.
 * @param path A <code>jstring</code> path to the executable.
 * @return True if the executable could be removed, false
 *      otherwise.
 */
jboolean removeExecutable(JNIEnv*, jstring);

/**
 * Create a com bstr reference from a java string reference.
 *
 * @param env A <code>JNIEnv*</code> jni environment.
 * @param string A <code>jstring</code>.
 */
CComBSTR getCComBSTR(JNIEnv*, jstring);
