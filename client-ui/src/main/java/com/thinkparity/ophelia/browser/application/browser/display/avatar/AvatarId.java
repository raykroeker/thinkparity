/*
 * Jan 20, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar;

/**
 * The naming convention for avatar ids is as follows:
 * <pre>
 * <FUNCTION>_<SECTION>_<COMPONENT>_<ACTION>_<CONTENT>
 * </pre>
 * 
 * Function:  What does the avatar display?  [dialog,main,tab]
 * Section:   For which portion of the ui does the avatar display? [platform]
 * Component: For which component does the avatar display? [container, contact]
 * Action:    For which action does the avatar display? [create invitation, read, create, update team]
 * Content:   What content does the avatar display? [status,title,content,error]
 * 
 * Note that either action or content are required and that section is only required for the platform.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum AvatarId {
    DIALOG_CONFIRM, DIALOG_CONTACT_INFO, DIALOG_CONTAINER_CREATE,
    DIALOG_CONTAINER_PUBLISH, DIALOG_CONTAINER_RENAME,
    DIALOG_CONTAINER_RENAME_DOCUMENT, DIALOG_CONTAINER_UPDATE_DRAFT_COMMENT,
    DIALOG_CONTAINER_VERSION_COMMENT,
    DIALOG_ERROR, DIALOG_ERROR_DETAILS, DIALOG_FILE_CHOOSER,
    DIALOG_PLATFORM_CONFIRM_SYNCHRONIZE, DIALOG_PLATFORM_FIREWALL_ACCESS_ERROR,
    DIALOG_PLATFORM_SIGNUP, DIALOG_PLATFORM_SIGNUP_ACCOUNT,
    DIALOG_PLATFORM_SIGNUP_AGREEMENT, DIALOG_PLATFORM_SIGNUP_CREDENTIALS,
    DIALOG_PLATFORM_SIGNUP_FIREWALL_ACCESS_ERROR,
    DIALOG_PLATFORM_SIGNUP_INTRO,
    DIALOG_PLATFORM_SIGNUP_LOGIN, DIALOG_PLATFORM_SIGNUP_PAYMENT,
    DIALOG_PLATFORM_SIGNUP_PROFILE, DIALOG_PLATFORM_SIGNUP_SUMMARY,
    DIALOG_PROFILE_UPDATE, DIALOG_PROFILE_UPDATE_ACCOUNT,
    DIALOG_PROFILE_UPDATE_PASSWORD,
    DIALOG_PROFILE_VERIFY_EMAIL, DIALOG_STATUS,
    MAIN_CONTENT, MAIN_STATUS,
    MAIN_TITLE, TAB_CONTACT, TAB_CONTAINER, TAB_HELP
}
