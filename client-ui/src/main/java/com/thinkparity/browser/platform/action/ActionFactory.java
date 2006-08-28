/*
 * Jan 10, 2006
 */
package com.thinkparity.browser.platform.action;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.AbstractFactory;
import com.thinkparity.browser.platform.BrowserPlatform;
import com.thinkparity.browser.platform.Platform;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.application.ApplicationRegistry;

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
	 * Create an action for the browser application.
	 * 
	 * @param actionId
	 *            The action id.
	 * @param browser
	 *            The thinkParity browser application.
	 * @return The action.
	 */
	private AbstractAction doCreateAction(final ActionId actionId) {
        logApiId();
        debugVariable("actionId", actionId);
        final AbstractAction action;
		switch(actionId) {
        case ARTIFACT_APPLY_FLAG_SEEN:
            action = new com.thinkparity.browser.platform.action.artifact.ApplyFlagSeen(browser);
            break;

        case CONTACT_ACCEPT_INCOMING_INVITATION:
            action = new com.thinkparity.browser.platform.action.contact.AcceptIncomingInvitation(browser);
            break;
		case CONTACT_CREATE_INCOMING_INVITATION:
            action = new com.thinkparity.browser.platform.action.contact.CreateIncomingInvitation(browser);
            break;
		case CONTACT_DECLINE_INCOMING_INVITATION:
            action = new com.thinkparity.browser.platform.action.contact.DeclineIncomingInvitation(browser);
            break;
		case CONTACT_DELETE:
		    action = new com.thinkparity.browser.platform.action.contact.Delete(browser);
            break;
        case CONTACT_DELETE_OUTGOING_INVITATION:
            action = new com.thinkparity.browser.platform.action.contact.DeleteOutgoingInvitation(browser);
            break;
		case CONTACT_READ:
            action = new com.thinkparity.browser.platform.action.contact.Read(browser);
            break;
        case CONTACT_SEARCH:
            action = new com.thinkparity.browser.platform.action.contact.Search(browser);
            break;

		case CONTAINER_ADD_DOCUMENT:
            action = new com.thinkparity.browser.platform.action.container.AddDocument(browser);
            break;
		case CONTAINER_CREATE:
            action = new com.thinkparity.browser.platform.action.container.Create(browser);
            break;
		case CONTAINER_CREATE_DRAFT:
            action = new com.thinkparity.browser.platform.action.container.CreateDraft(browser);
            break;
		case CONTAINER_DELETE:
		    action = new com.thinkparity.browser.platform.action.container.Delete(browser);
		    break;
        case CONTAINER_DELETE_DRAFT:
            action = new com.thinkparity.browser.platform.action.container.DeleteDraft(browser);
            break;
        case CONTAINER_PUBLISH:
            action = new com.thinkparity.browser.platform.action.container.Publish(browser);
            break;
        case CONTAINER_REMOVE_DOCUMENT:
            action = new com.thinkparity.browser.platform.action.container.RemoveDocument(browser);
            break;
        case CONTAINER_SEARCH:
            action = new com.thinkparity.browser.platform.action.container.Search(browser);
            break;
        case CONTAINER_SHARE:
            action = new com.thinkparity.browser.platform.action.container.Share(browser);
            break;

		case DOCUMENT_OPEN:
            action = new com.thinkparity.browser.platform.action.document.Open(browser);
            break;
		case DOCUMENT_OPEN_VERSION:
            action = new com.thinkparity.browser.platform.action.document.OpenVersion(browser);
            break;
		case DOCUMENT_RENAME:
            action = new com.thinkparity.browser.platform.action.document.Rename(browser);
            break;
        case CONTAINER_REVERT_DOCUMENT:
            action = new com.thinkparity.browser.platform.action.container.RevertDocument(browser);
            break;
		case DOCUMENT_UPDATE_DRAFT:
            action = new com.thinkparity.browser.platform.action.document.UpdateDraft(browser);
            break;

        case HELP:
            action = new com.thinkparity.browser.platform.action.help.Help(browser);
            break;            
        case HELP_ABOUT:
            action = new com.thinkparity.browser.platform.action.help.HelpAbout(browser);
            break;

        case PLATFORM_QUIT:
            action = new com.thinkparity.browser.platform.action.platform.Quit(platform);
            break;
        case PLATFORM_RESTART:
            action = new com.thinkparity.browser.platform.action.platform.Restart(platform);
            break;
        case PLATFORM_BROWSER_MOVE_TO_FRONT:
            action = new com.thinkparity.browser.platform.action.platform.browser.MoveToFront(platform);
            break;
        case PLATFORM_BROWSER_RESTORE:
            action = new com.thinkparity.browser.platform.action.platform.browser.Restore(platform);
            break;

        case PROFILE_ADD_EMAIL:
            action = new com.thinkparity.browser.platform.action.profile.AddEmail(browser);
            break;
        case PROFILE_REMOVE_EMAIL:
            action = new com.thinkparity.browser.platform.action.profile.RemoveEmail(browser);
            break;
        case PROFILE_VERIFY_EMAIL:
            action = new com.thinkparity.browser.platform.action.profile.VerifyEmail(browser);
            break;
        case PROFILE_UPDATE:
            action = new com.thinkparity.browser.platform.action.profile.Update(browser);
            break;
            
        case SIGN_UP:
            action = new com.thinkparity.browser.platform.action.signup.SignUp(browser);
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
            logWarning("REGISTRY CONTAINS ACTION " + action.getId());
        }
        actionRegistry.put(action);
    }
}
