/*
 * Jan 25, 2006
 */
package com.thinkparity.browser.platform.action;

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
 * Package: com.thinkparity.browser.platform.action.platform.browser<br>
 * Class: Restore
 * <li>Action id: CONTAINER_ADD_DOCUMENT<br>
 * Package: com.thinkparity.browser.platform.action.container<br>
 * Class: AddDocument
 * </ol>
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.26
 */
public enum ActionId {
    ARTIFACT_APPLY_FLAG_SEEN, CONTACT_ACCEPT_INCOMING_INVITATION,
    CONTACT_CREATE_INCOMING_INVITATION, CONTACT_DECLINE_INCOMING_INVITATION,
    CONTACT_DELETE, CONTACT_DELETE_OUTGOING_INVITATION, CONTACT_READ,
    CONTACT_SEARCH, CONTAINER_ADD_DOCUMENT, CONTAINER_CREATE,
    CONTAINER_CREATE_DRAFT, CONTAINER_DELETE, CONTAINER_DELETE_DRAFT,
    CONTAINER_PUBLISH, CONTAINER_REMOVE_DOCUMENT, CONTAINER_REVERT_DOCUMENT,
    CONTAINER_SEARCH, CONTAINER_SHARE, DOCUMENT_OPEN, DOCUMENT_OPEN_VERSION,
    DOCUMENT_RENAME, DOCUMENT_UPDATE_DRAFT, HELP, HELP_ABOUT,
    PLATFORM_BROWSER_MOVE_TO_FRONT, PLATFORM_BROWSER_RESTORE, PLATFORM_LOGIN,
    PLATFORM_LOGOUT, PLATFORM_QUIT, PLATFORM_RESTART, PROFILE_ADD_EMAIL,
    PROFILE_REMOVE_EMAIL, PROFILE_UPDATE, PROFILE_VERIFY_EMAIL, SIGN_UP
}
