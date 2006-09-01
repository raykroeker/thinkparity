/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */

/**
 * The naming convention for avatar ids is as follows:
 * <FUNCTION>_<SECTION>_<COMPONENT>_<ACTION>_<CONTENT>
 * 
 * Function:  What does the avatar display?  [dialog,main,tab]
 * Section:   For which portion of the ui does the avatar display? [platform]
 * Component: For which component does the avatar display? [container, contact]
 * Action:    For which action does the avatar display? [create invitation, read, create, update team]
 * Content:   What content does the avatar display? [status,title,content,error]
 * 
 * Note that either action or content are required and that section is only required for the platform.
 */
public enum AvatarId {
    DIALOG_CONFIRM, DIALOG_CONTACT_CREATE_OUTGOING_INVITATION,
    DIALOG_CONTACT_READ, DIALOG_CONTAINER_CREATE, DIALOG_ERROR,
    DIALOG_PLATFORM_LOGIN, DIALOG_PROFILE_RESET_PASSWORD, DIALOG_PROFILE_UPDATE,
    DIALOG_PROFILE_VERIFY_EMAIL, DIALOG_RENAME, MAIN_CONTENT, MAIN_STATUS,
    MAIN_TITLE, TAB_ARCHIVE, TAB_CONTACT, TAB_CONTAINER
}
