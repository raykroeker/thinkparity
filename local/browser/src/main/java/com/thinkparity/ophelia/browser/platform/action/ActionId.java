/*
 * Jan 25, 2006
 */
package com.thinkparity.ophelia.browser.platform.action;

/**
 * <b>Title:</b>thinkParity Action Id<br>
 * <b>Description:</b>The action id enumeration implies a package structure and
 * an action name. The first (N; >= 1) nouns in the action id indicate the
 * package names beneath the root action package. The middle verb and trailing
 * (N; >= 0) nouns are the name of the action implementing class. A caveat to
 * this rule is that nouns should not be repeated.
 * 
 * Examples:
 * <ol>
 * <li> Action id: PLATFORM_BROWSER_RESTORE<br>
 * Package: com.thinkparity.ophelia.browser.platform.action.platform.browser<br>
 * Class: Restore
 * <li>Action id: CONTAINER_ADD_DOCUMENT<br>
 * Package: com.thinkparity.ophelia.browser.platform.action.container<br>
 * Class: AddDocument
 * </ol>
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.26
 */
public enum ActionId {
    ARTIFACT_APPLY_FLAG_SEEN,
    
    CONTACT_ACCEPT_INCOMING_INVITATION, CONTACT_CREATE_INCOMING_INVITATION,
    CONTACT_DECLINE_INCOMING_INVITATION, CONTACT_DELETE,
    CONTACT_DELETE_OUTGOING_INVITATION, CONTACT_READ,
    
    CONTAINER_ADD_BOOKMARK, CONTAINER_ADD_DOCUMENT, CONTAINER_CREATE,
    CONTAINER_CREATE_DRAFT, CONTAINER_DELETE, CONTAINER_DELETE_DRAFT, 
    CONTAINER_DISPLAY_VERSION_INFO, CONTAINER_EXPORT,
    CONTAINER_EXPORT_DRAFT, CONTAINER_EXPORT_VERSION, CONTAINER_PRINT_DRAFT,
    CONTAINER_PRINT_VERSION, CONTAINER_PUBLISH, CONTAINER_PUBLISH_VERSION,
    CONTAINER_REMOVE_BOOKMARK, CONTAINER_REMOVE_DOCUMENT, CONTAINER_RENAME,
    CONTAINER_RENAME_DOCUMENT, CONTAINER_REVERT_DOCUMENT,
    CONTAINER_SUBSCRIBE, CONTAINER_UNDELETE_DOCUMENT, CONTAINER_UNSUBSCRIBE,
    
    DOCUMENT_OPEN, DOCUMENT_OPEN_VERSION, DOCUMENT_PRINT_DRAFT,
    DOCUMENT_PRINT_VERSION, DOCUMENT_UPDATE_DRAFT,
    
    PLATFORM_BROWSER_DISPLAY_INFO, PLATFORM_BROWSER_MOVE_TO_FRONT,
    PLATFORM_BROWSER_OPEN_HELP, PLATFORM_BROWSER_RESTORE, PLATFORM_LOGIN,
    PLATFORM_LOGOUT, PLATFORM_QUIT, PLATFORM_RESTART,
    
    PROFILE_ADD_EMAIL, PROFILE_REMOVE_EMAIL, PROFILE_RESET_PASSWORD, PROFILE_SIGN_UP,
    PROFILE_UPDATE, PROFILE_VERIFY_EMAIL
}
