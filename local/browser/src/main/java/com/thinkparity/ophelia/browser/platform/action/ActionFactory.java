/*
 * Jan 10, 2006
 */
package com.thinkparity.ophelia.browser.platform.action;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.AbstractFactory;
import com.thinkparity.ophelia.browser.platform.BrowserPlatform;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;
import com.thinkparity.ophelia.browser.platform.plugin.extension.ActionExtension;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ActionFactory extends AbstractFactory {

    /** The singleton instance. */
	private static final ActionFactory SINGLETON;

	static { SINGLETON = new ActionFactory(); }

    /**
     * Create a thinkParity action.
     * 
     * @param actionExtension
     *            An <code>ActionExtension</code>.
     * @return An <code>AbstractAction</code>.
     */
    public static AbstractAction create(final ActionExtension extension) {
        return SINGLETON.doCreateAction(extension);
    }

    /**
     * Create a thinkParity action.
     * 
     * @param id
     *            An action id.
     * @return A thinkParity action.
     * @see ActionId
     */
    public static AbstractAction create(final ActionId id) {
        return SINGLETON.doCreateAction(id);
    }

	/** The action registry. */
    private final ActionRegistry actionRegistry;

    /** The thinkParity browser application. */
    private final Browser browser;

    /** The thinkParity platform. */
    private final Platform platform;

	/** Create ActionFactory. */
	private ActionFactory() {
        super();
        this.actionRegistry = new ActionRegistry();
        this.browser = (Browser) new ApplicationRegistry().get(ApplicationId.BROWSER);
        this.platform = BrowserPlatform.getInstance();
    }

    /**
     * Create a thinkParity action.
     * 
     * @param actionExtension
     *            An <code>ActionExtension</code>.
     * @return An <code>AbstractAction</code>.
     */
    public AbstractAction doCreateAction(final ActionExtension extension) {
        final AbstractAction action = extension.createAction();
        register(action, extension);
        return action;
    }

    /**
	 * Create an action for the browser application.
	 * 
	 * @param actionId
	 *            The action id.
	 * @param browser
	 *            The thinkParity browser application.
	 * @return The action.
	 */
	private AbstractAction doCreateAction(final ActionId id) {
        final AbstractAction action;
		switch (id) {
        case ARTIFACT_APPLY_FLAG_SEEN:
            action = new com.thinkparity.ophelia.browser.platform.action.artifact.ApplyFlagSeen(browser);
            break;
        case ARTIFACT_REMOVE_FLAG_SEEN:
            action = new com.thinkparity.ophelia.browser.platform.action.artifact.RemoveFlagSeen(browser);
            break;

        case CONTACT_ACCEPT_INCOMING_INVITATION:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.AcceptIncomingInvitation(browser);
            break;
        case CONTACT_COLLAPSE:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.Collapse(browser);
            break;
		case CONTACT_CREATE_INCOMING_INVITATION:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.CreateIncomingInvitation(browser);
            break;
		case CONTACT_DECLINE_INCOMING_INVITATION:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.DeclineIncomingInvitation(browser);
            break;
		case CONTACT_DELETE:
		    action = new com.thinkparity.ophelia.browser.platform.action.contact.Delete(browser);
            break;
        case CONTACT_DELETE_OUTGOING_INVITATION:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.DeleteOutgoingInvitation(browser);
            break;
        case CONTACT_DISPLAY_INVITATION_INFO:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.DisplayContactInvitationInfo(browser);
            break;
        case CONTACT_EXPAND:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.Expand(browser);
            break;
		case CONTACT_READ:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.Read(browser);
            break;
        case CONTACT_SHOW:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.Show(browser);
            break;

		case CONTAINER_ADD_BOOKMARK:
            action = new com.thinkparity.ophelia.browser.platform.action.container.AddBookmark(browser);
            break;
        case CONTAINER_ADD_DOCUMENT:
            action = new com.thinkparity.ophelia.browser.platform.action.container.AddDocument(browser);
            break;
        case CONTAINER_ARCHIVE:
            action = new com.thinkparity.ophelia.browser.platform.action.container.Archive(browser);
            break;
        case CONTAINER_COLLAPSE:
            action = new com.thinkparity.ophelia.browser.platform.action.container.Collapse(browser);
            break;
		case CONTAINER_CREATE:
            action = new com.thinkparity.ophelia.browser.platform.action.container.Create(browser);
            break;
		case CONTAINER_CREATE_DRAFT:
            action = new com.thinkparity.ophelia.browser.platform.action.container.CreateDraft(browser);
            break;
		case CONTAINER_DELETE:
		    action = new com.thinkparity.ophelia.browser.platform.action.container.Delete(browser);
		    break;
        case CONTAINER_DELETE_DRAFT:
            action = new com.thinkparity.ophelia.browser.platform.action.container.DeleteDraft(browser);
            break;
        case CONTAINER_DISPLAY_FLAG_SEEN_INFO:
            action = new com.thinkparity.ophelia.browser.platform.action.container.DisplayFlagSeenInfo(browser);
            break;
        case CONTAINER_DISPLAY_VERSION_INFO:
            action = new com.thinkparity.ophelia.browser.platform.action.container.DisplayVersionInfo(browser);
            break;
        case CONTAINER_EXPAND:
            action = new com.thinkparity.ophelia.browser.platform.action.container.Expand(browser);
            break;
        case CONTAINER_EXPORT:
            action = new com.thinkparity.ophelia.browser.platform.action.container.Export(browser);
            break;
        case CONTAINER_EXPORT_VERSION:
            action = new com.thinkparity.ophelia.browser.platform.action.container.ExportVersion(browser);
            break;            
        case CONTAINER_PRINT_DRAFT:
            action = new com.thinkparity.ophelia.browser.platform.action.container.PrintDraft(browser);
            break;
        case CONTAINER_PRINT_VERSION:
            action = new com.thinkparity.ophelia.browser.platform.action.container.PrintVersion(browser);
            break;
        case CONTAINER_PUBLISH:
            action = new com.thinkparity.ophelia.browser.platform.action.container.Publish(browser);
            break;
        case CONTAINER_PUBLISH_VERSION:
            action = new com.thinkparity.ophelia.browser.platform.action.container.PublishVersion(browser);
            break;
        case CONTAINER_READ_VERSION:
            action = new com.thinkparity.ophelia.browser.platform.action.container.ReadVersion(browser);
            break;
        case CONTAINER_REMOVE_BOOKMARK:
            action = new com.thinkparity.ophelia.browser.platform.action.container.RemoveBookmark(browser);
            break;
        case CONTAINER_REMOVE_DOCUMENT:
            action = new com.thinkparity.ophelia.browser.platform.action.container.RemoveDocument(browser);
            break;
        case CONTAINER_RENAME:
            action = new com.thinkparity.ophelia.browser.platform.action.container.Rename(browser);
            break;
        case CONTAINER_RENAME_DOCUMENT:
            action = new com.thinkparity.ophelia.browser.platform.action.container.RenameDocument(browser);
            break;
        case CONTAINER_RESTORE:
            action = new com.thinkparity.ophelia.browser.platform.action.container.Restore(browser);
            break;
        case CONTAINER_REVERT_DOCUMENT:
            action = new com.thinkparity.ophelia.browser.platform.action.container.RevertDocument(browser);
            break;
        case CONTAINER_SHOW:
            action = new com.thinkparity.ophelia.browser.platform.action.container.Show(browser);
            break;
        case CONTAINER_SUBSCRIBE:
            action = new com.thinkparity.ophelia.browser.platform.action.container.Subscribe(browser);
            break;
        case CONTAINER_UNDELETE_DOCUMENT:
            action = new com.thinkparity.ophelia.browser.platform.action.container.UndeleteDocument(browser);
            break;
        case CONTAINER_UNSUBSCRIBE:
            action = new com.thinkparity.ophelia.browser.platform.action.container.Unsubscribe(browser);
            break;

        case DOCUMENT_OPEN:
            action = new com.thinkparity.ophelia.browser.platform.action.document.Open(browser);
            break;
		case DOCUMENT_OPEN_VERSION:
            action = new com.thinkparity.ophelia.browser.platform.action.document.OpenVersion(browser);
            break;
        case DOCUMENT_PRINT_DRAFT:
            action = new com.thinkparity.ophelia.browser.platform.action.document.PrintDraft(browser);
            break;
        case DOCUMENT_PRINT_VERSION:
            action = new com.thinkparity.ophelia.browser.platform.action.document.PrintVersion(browser);
            break;
        case DOCUMENT_UPDATE_DRAFT:
            action = new com.thinkparity.ophelia.browser.platform.action.document.UpdateDraft(browser);
            break;

		case PLATFORM_BROWSER_DISPLAY_INFO:
		    action = new com.thinkparity.ophelia.browser.platform.action.platform.browser.DisplayInfo(browser);
		    break;
		case PLATFORM_BROWSER_ICONIFY:
		    action = new com.thinkparity.ophelia.browser.platform.action.platform.browser.Iconify(platform);
		    break;
        case PLATFORM_BROWSER_MOVE_TO_FRONT:
            action = new com.thinkparity.ophelia.browser.platform.action.platform.browser.MoveToFront(platform);
            break;
		case PLATFORM_BROWSER_OPEN_HELP:
		    action = new com.thinkparity.ophelia.browser.platform.action.platform.browser.OpenHelp(browser);
		    break;            
		case PLATFORM_BROWSER_RESTORE:
		    action = new com.thinkparity.ophelia.browser.platform.action.platform.browser.Restore(platform);
		    break;
        case PLATFORM_QUIT:
            action = new com.thinkparity.ophelia.browser.platform.action.platform.Quit(platform);
            break;
        case PLATFORM_RESTART:
            action = new com.thinkparity.ophelia.browser.platform.action.platform.Restart(platform);
            break;

        case PROFILE_ADD_EMAIL:
            action = new com.thinkparity.ophelia.browser.platform.action.profile.AddEmail(browser);
            break;
        case PROFILE_REMOVE_EMAIL:
            action = new com.thinkparity.ophelia.browser.platform.action.profile.RemoveEmail(browser);
            break;
        case PROFILE_RESET_PASSWORD:
            action = new com.thinkparity.ophelia.browser.platform.action.profile.ResetPassword(browser);
            break;
        case PROFILE_SIGN_UP:
            action = new com.thinkparity.ophelia.browser.platform.action.profile.SignUp(browser);
            break;
        case PROFILE_UPDATE:
            action = new com.thinkparity.ophelia.browser.platform.action.profile.Update(browser);
            break;
        case PROFILE_UPDATE_PASSWORD:
            action = new com.thinkparity.ophelia.browser.platform.action.profile.UpdatePassword(browser);
            break;
        case PROFILE_VERIFY_EMAIL:
            action = new com.thinkparity.ophelia.browser.platform.action.profile.VerifyEmail(browser);
            break;

        default:
			throw Assert.createUnreachable("UNKNOWN ACTION ID");
		}
        register(action);
        return action;
	}

    /**
     * Register an action.
     * 
     * @param action
     *            A thinkParity action.
     */
    private void register(final AbstractAction action) {
        if (actionRegistry.contains(action.getId())) {
            logger.logWarning("Action {0} already registered.", action);
        }
        actionRegistry.put(action);
    }

    /**
     * Register an action.
     * 
     * @param action
     *            A thinkParity action.
     */
    private void register(final AbstractAction action,
            final ActionExtension actionExtension) {
        Assert.assertNotTrue(actionRegistry.contains(actionExtension),
                "Action {0} already registered.", action);
        actionRegistry.put(action, actionExtension);
    }
}
