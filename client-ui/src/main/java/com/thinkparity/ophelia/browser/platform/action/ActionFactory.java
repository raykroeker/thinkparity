/*
 * Jan 10, 2006
 */
package com.thinkparity.ophelia.browser.platform.action;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.system.SystemApplication;
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

    /** The thinkParity platform. */
    private final Platform platform;

	/** Create ActionFactory. */
	private ActionFactory() {
        super();
        this.actionRegistry = new ActionRegistry();
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
		case BACKUP_RESTORE:
		    action = new com.thinkparity.ophelia.browser.platform.action.backup.Restore(getBrowser());
		    break;
        case CONTACT_ACCEPT_INCOMING_EMAIL_INVITATION:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.AcceptIncomingEMailInvitation(getBrowser());
            break;
        case CONTACT_ACCEPT_INCOMING_USER_INVITATION:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.AcceptIncomingUserInvitation(getBrowser());
            break;
        case CONTACT_CLEAR_INCOMING_EMAIL_INVITATION_NOTIFICATIONS:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.ClearIncomingEMailInvitationNotifications(getSystem());
            break;
        case CONTACT_CLEAR_INCOMING_USER_INVITATION_NOTIFICATIONS:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.ClearIncomingUserInvitationNotifications(getSystem());
            break;
        case CONTACT_COLLAPSE:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.Collapse(getBrowser());
            break;
        case CONTACT_CREATE_OUTGOING_EMAIL_INVITATION:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.CreateOutgoingEMailInvitation(getBrowser());
            break;
        case CONTACT_CREATE_OUTGOING_USER_INVITATION:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.CreateOutgoingUserInvitation(getBrowser());
            break;
        case CONTACT_DECLINE_INCOMING_EMAIL_INVITATION:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.DeclineIncomingEMailInvitation(getBrowser());
            break;
        case CONTACT_DECLINE_INCOMING_USER_INVITATION:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.DeclineIncomingUserInvitation(getBrowser());
            break;
		case CONTACT_DELETE:
		    action = new com.thinkparity.ophelia.browser.platform.action.contact.Delete(getBrowser());
            break;
        case CONTACT_DELETE_OUTGOING_EMAIL_INVITATION:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.DeleteOutgoingEMailInvitation(getBrowser());
            break;
        case CONTACT_DELETE_OUTGOING_USER_INVITATION:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.DeleteOutgoingUserInvitation(getBrowser());
            break;
        case CONTACT_EXPAND:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.Expand(getBrowser());
            break;
        case CONTACT_SHOW:
            action = new com.thinkparity.ophelia.browser.platform.action.contact.Show(getBrowser());
            break;

		case CONTAINER_ADD_BOOKMARK:
            action = new com.thinkparity.ophelia.browser.platform.action.container.AddBookmark(getBrowser());
            break;
        case CONTAINER_ADD_DOCUMENT:
            action = new com.thinkparity.ophelia.browser.platform.action.container.AddDocument(getBrowser());
            break;
        case CONTAINER_APPLY_FLAG_SEEN:
            action = new com.thinkparity.ophelia.browser.platform.action.container.ApplyFlagSeen(getBrowser());
            break;
        case CONTAINER_CLEAR_NOTIFICATIONS:
            action = new com.thinkparity.ophelia.browser.platform.action.container.ClearNotifications(getSystem());
            break;
        case CONTAINER_COLLAPSE:
            action = new com.thinkparity.ophelia.browser.platform.action.container.Collapse(getBrowser());
            break;
		case CONTAINER_CREATE:
            action = new com.thinkparity.ophelia.browser.platform.action.container.Create(getBrowser());
            break;
		case CONTAINER_CREATE_DRAFT:
            action = new com.thinkparity.ophelia.browser.platform.action.container.CreateDraft(getBrowser());
            break;
		case CONTAINER_DELETE:
		    action = new com.thinkparity.ophelia.browser.platform.action.container.Delete(getBrowser());
		    break;
        case CONTAINER_DELETE_DRAFT:
            action = new com.thinkparity.ophelia.browser.platform.action.container.DeleteDraft(getBrowser());
            break;
        case CONTAINER_DISPLAY_VERSION_INFO:
            action = new com.thinkparity.ophelia.browser.platform.action.container.DisplayVersionInfo(getBrowser());
            break;
        case CONTAINER_EXPAND:
            action = new com.thinkparity.ophelia.browser.platform.action.container.Expand(getBrowser());
            break;
        case CONTAINER_EXPORT:
            action = new com.thinkparity.ophelia.browser.platform.action.container.Export(getBrowser());
            break;
        case CONTAINER_EXPORT_AUDIT_REPORT:
            action = new com.thinkparity.ophelia.browser.platform.action.container.ExportAuditReport(getBrowser());
            break;
        case CONTAINER_READ_TEAM_MEMBER:
            action = new com.thinkparity.ophelia.browser.platform.action.container.ReadTeamMember(getBrowser());
            break;
        case CONTAINER_PRINT_DRAFT:
            action = new com.thinkparity.ophelia.browser.platform.action.container.PrintDraft(getBrowser());
            break;
        case CONTAINER_PRINT_VERSION:
            action = new com.thinkparity.ophelia.browser.platform.action.container.PrintVersion(getBrowser());
            break;
        case CONTAINER_PUBLISH:
            action = new com.thinkparity.ophelia.browser.platform.action.container.Publish(getBrowser());
            break;
        case CONTAINER_PUBLISH_VERSION:
            action = new com.thinkparity.ophelia.browser.platform.action.container.PublishVersion(getBrowser());
            break;
        case CONTAINER_READ_VERSION:
            action = new com.thinkparity.ophelia.browser.platform.action.container.ReadVersion(getBrowser());
            break;
        case CONTAINER_REMOVE_BOOKMARK:
            action = new com.thinkparity.ophelia.browser.platform.action.container.RemoveBookmark(getBrowser());
            break;
        case CONTAINER_REMOVE_DOCUMENT:
            action = new com.thinkparity.ophelia.browser.platform.action.container.RemoveDocument(getBrowser());
            break;
        case CONTAINER_REMOVE_FLAG_SEEN:
            action = new com.thinkparity.ophelia.browser.platform.action.container.RemoveFlagSeen(getBrowser());
            break;
        case CONTAINER_RENAME:
            action = new com.thinkparity.ophelia.browser.platform.action.container.Rename(getBrowser());
            break;
        case CONTAINER_RENAME_DOCUMENT:
            action = new com.thinkparity.ophelia.browser.platform.action.container.RenameDocument(getBrowser());
            break;
        case CONTAINER_REVERT_DOCUMENT:
            action = new com.thinkparity.ophelia.browser.platform.action.container.RevertDocument(getBrowser());
            break;
        case CONTAINER_SHOW:
            action = new com.thinkparity.ophelia.browser.platform.action.container.Show(getBrowser());
            break;
        case CONTAINER_UNDELETE_DOCUMENT:
            action = new com.thinkparity.ophelia.browser.platform.action.container.UndeleteDocument(getBrowser());
            break;
        case CONTAINER_UPDATE_DRAFT_COMMENT:
            action = new com.thinkparity.ophelia.browser.platform.action.container.UpdateDraftComment(getBrowser());
            break;

        case DOCUMENT_OPEN:
            action = new com.thinkparity.ophelia.browser.platform.action.document.Open(getBrowser());
            break;
		case DOCUMENT_OPEN_VERSION:
            action = new com.thinkparity.ophelia.browser.platform.action.document.OpenVersion(getBrowser());
            break;
        case DOCUMENT_PRINT_DRAFT:
            action = new com.thinkparity.ophelia.browser.platform.action.document.PrintDraft(getBrowser());
            break;
        case DOCUMENT_PRINT_VERSION:
            action = new com.thinkparity.ophelia.browser.platform.action.document.PrintVersion(getBrowser());
            break;
        case DOCUMENT_UPDATE_DRAFT:
            action = new com.thinkparity.ophelia.browser.platform.action.document.UpdateDraft(getBrowser());
            break;

        case HELP_EXPAND:
            action = new com.thinkparity.ophelia.browser.platform.action.help.Expand(getBrowser());
            break;
        case HELP_SHOW_MOVIE:
            action = new com.thinkparity.ophelia.browser.platform.action.help.ShowMovie(getBrowser());
            break;

        case PLATFORM_BROWSER_ICONIFY:
            action = new com.thinkparity.ophelia.browser.platform.action.platform.browser.Iconify(getPlatform());
            break;
        case PLATFORM_BROWSER_MOVE_TO_FRONT:
            action = new com.thinkparity.ophelia.browser.platform.action.platform.browser.MoveToFront(getPlatform());
            break;          
		case PLATFORM_BROWSER_RESTORE:
		    action = new com.thinkparity.ophelia.browser.platform.action.platform.browser.Restore(getPlatform());
		    break;
        case PLATFORM_CONTACT_US:
            action = new com.thinkparity.ophelia.browser.platform.action.platform.ContactUs(getPlatform());
            break;
        case PLATFORM_LEARN_MORE:
            action = new com.thinkparity.ophelia.browser.platform.action.platform.LearnMore(getPlatform());
            break;
        case PLATFORM_LOGIN:
            action = new com.thinkparity.ophelia.browser.platform.action.platform.Login(getPlatform());
            break;
        case PLATFORM_OPEN_WEBSITE:
            action = new com.thinkparity.ophelia.browser.platform.action.platform.OpenWebSite(getPlatform());
            break;
        case PLATFORM_SHOW_GETTING_STARTED_MOVIE:
            action = new com.thinkparity.ophelia.browser.platform.action.platform.ShowGettingStartedMovie(getPlatform());
            break;
        case PLATFORM_QUIT:
            action = new com.thinkparity.ophelia.browser.platform.action.platform.Quit(getPlatform());
            break;
        case PLATFORM_RESTART:
            action = new com.thinkparity.ophelia.browser.platform.action.platform.Restart(getPlatform());
            break;

        case PROFILE_CLEAR_EMAIL_UPDATED_NOTIFICATION:
            action = new com.thinkparity.ophelia.browser.platform.action.profile.ClearEMailUpdatedNotification(getSystem());
            break;
        case PROFILE_CLEAR_PROFILE_PASSIVATED_NOTIFICATION:
            action = new com.thinkparity.ophelia.browser.platform.action.profile.ClearProfilePassivatedNotification(getSystem());
            break;
        case PROFILE_SHOW_EMAIL_UPDATED_NOTIFICATION:
            action = new com.thinkparity.ophelia.browser.platform.action.profile.ShowEMailUpdatedNotification(getSystem());
            break;
        case PROFILE_SHOW_PROFILE_PASSIVATED_NOTIFICATION:
            action = new com.thinkparity.ophelia.browser.platform.action.profile.ShowProfilePassivatedNotification(getSystem());
            break;
        case PROFILE_UPDATE:
            action = new com.thinkparity.ophelia.browser.platform.action.profile.Update(getBrowser());
            break;
        case PROFILE_UPDATE_EMAIL:
            action = new com.thinkparity.ophelia.browser.platform.action.profile.UpdateEMail(getBrowser());
            break;
        case PROFILE_UPDATE_PASSWORD:
            action = new com.thinkparity.ophelia.browser.platform.action.profile.UpdatePassword(getBrowser());
            break;
        case PROFILE_UPDATE_PAYMENT_INFO:
            action = new com.thinkparity.ophelia.browser.platform.action.profile.UpdatePaymentInfo(getBrowser());
            break;
        case PROFILE_UPGRADE_ACCOUNT:
            action = new com.thinkparity.ophelia.browser.platform.action.profile.UpgradeAccount(getBrowser());
            break;
        case PROFILE_VERIFY_EMAIL:
            action = new com.thinkparity.ophelia.browser.platform.action.profile.VerifyEMail(getBrowser());
            break;

        case SYSTEM_DISABLE_QUIT:
            action = new com.thinkparity.ophelia.browser.platform.action.platform.system.DisableQuit(getSystem());
            break;
        case SYSTEM_ENABLE_QUIT:
            action = new com.thinkparity.ophelia.browser.platform.action.platform.system.EnableQuit(getSystem());
            break;

        case WORKSPACE_DELETE_PROXY_CONFIGURATION:
            action = new com.thinkparity.ophelia.browser.platform.action.workspace.DeleteProxyConfiguration(getSystem());
            break;
        case WORKSPACE_UPDATE_PROXY_CONFIGURATION:
            action = new com.thinkparity.ophelia.browser.platform.action.workspace.UpdateProxyConfiguration(getSystem());
            break;

        default:
			throw Assert.createUnreachable("UNKNOWN ACTION ID");
		}
        register(action);
        return action;
	}

	/**
     * Obtain the browser application.
     * 
     * @return A <code>Browser</code>.
     */
    private Browser getBrowser() {
        return (Browser) new ApplicationRegistry().get(ApplicationId.BROWSER);
    }

    /**
     * Get the platform.
     */
    private Platform getPlatform() {
        return platform;
    }

    /**
     * Obtain the system application.
     * 
     * @return A <code>SystemApplication</code>.
     */
    private SystemApplication getSystem() {
        return (SystemApplication) new ApplicationRegistry().get(ApplicationId.SYSTEM);
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
