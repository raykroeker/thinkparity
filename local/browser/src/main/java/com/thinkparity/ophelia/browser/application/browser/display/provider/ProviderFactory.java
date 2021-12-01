/*
 * Created On: Jan 16, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.backup.BackupModel;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.help.HelpModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.session.SessionModel;
import com.thinkparity.ophelia.model.user.UserModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.backup.RestoreBackupProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.contact.CreateOutgoingEMailInvitationProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container.ContainerVersionProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container.PublishContainerProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container.RenameDocumentProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container.UpdateDraftCommentProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile.UpdateAccountProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile.UpdatePasswordProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile.UpdateProfileProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile.VerifyEMailProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.tab.contact.ContactProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.tab.container.ContainerProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.tab.help.HelpProvider;
import com.thinkparity.ophelia.browser.platform.firstrun.SignupProvider;
import com.thinkparity.ophelia.browser.util.ModelFactory;

import org.apache.log4j.Logger;

/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class ProviderFactory {

	/** Singleton instance. */
	private static final ProviderFactory SINGLETON;

	static { SINGLETON = new ProviderFactory(); }

   
	/**
     * Obtain the content provider for an avatar.
     * 
     * @param avatarId
     *            An avatar id.
     * @return A content provider.
     */
    public static ContentProvider getProvider(final AvatarId avatarId) {
        return SINGLETON.doGetProvider(avatarId);
    }

    /** An instance of <code>ArtifactModel</code>. */
	protected final ArtifactModel artifactModel;

    /** An instance of <code>BackupModel</code>. */
    protected final BackupModel backupModel;

    /** An instance of <code>ContactModel</code>. */
    protected final ContactModel contactModel;

    /** An instance of <code>ContainerModel</code>. */
    protected final ContainerModel containerModel;

    /** An instance of <code>DocumentModel</code>. */
	protected final DocumentModel documentModel;

    /** An instance of <code>HelpModel</code>. */
    protected final HelpModel helpModel;

    /** An apache logger. */
	protected final Logger logger;
    
    /** An instance of <code>SessionModel</code>. */
	protected final SessionModel sessionModel;

    /** An instance of <code>UserModel</code>. */
    protected final UserModel userModel;

    /** An instance of <code>ProfileModel</code>. */
    private final ProfileModel profileModel;

    /** The thinkParity <code>Workspace</code>. */
    private final Workspace workspace;

    /**
     * Create ProviderFactory.
     * 
     */
	private ProviderFactory() {
		super();
		final ModelFactory modelFactory = ModelFactory.getInstance();
		this.artifactModel = modelFactory.getArtifactModel(getClass());
        this.backupModel = modelFactory.getBackupModel(getClass());
		this.contactModel = modelFactory.getContactModel(getClass());
        this.containerModel = modelFactory.getContainerModel(getClass());
		this.documentModel = modelFactory.getDocumentModel(getClass());
        this.helpModel = modelFactory.getHelpModel(getClass());
        this.profileModel = modelFactory.getProfileModel(getClass());
		this.sessionModel = modelFactory.getSessionModel(getClass());
        this.userModel = modelFactory.getUserModel(getClass());
        this.workspace = modelFactory.getWorkspace(getClass());

		this.logger = Logger.getLogger(getClass());
	}

	/**
     * Obtain the content provider for an avatar.
     * 
     * @param avatarId
     *            An avatar id.
     * @return A content provider.
     */
    private ContentProvider doGetProvider(final AvatarId avatarId) {
        final ContentProvider provider;
        switch(avatarId) {
        case DIALOG_BACKUP_RESTORE:
            provider = new RestoreBackupProvider(profileModel);
            break;
        case DIALOG_CONTACT_CREATE_OUTGOING_EMAIL_INVITATION:
            provider = new CreateOutgoingEMailInvitationProvider(profileModel, contactModel);
            break;
        case DIALOG_CONTAINER_PUBLISH:
            provider = new PublishContainerProvider(profileModel, containerModel, userModel, contactModel);
            break; 
        case DIALOG_CONTAINER_RENAME_DOCUMENT:
            provider = new RenameDocumentProvider(profileModel, containerModel);
            break;   
        case DIALOG_CONTAINER_UPDATE_DRAFT_COMMENT:
            provider = new UpdateDraftCommentProvider(profileModel, containerModel);
            break;
        case DIALOG_CONTAINER_VERSION_COMMENT:
            provider = new ContainerVersionProvider(profileModel, containerModel);
            break;
        case DIALOG_PLATFORM_SIGNUP:
        case DIALOG_PLATFORM_SIGNUP_ACCOUNT:
        case DIALOG_PLATFORM_SIGNUP_AGREEMENT:
        case DIALOG_PLATFORM_SIGNUP_CREDENTIALS:
        case DIALOG_PLATFORM_SIGNUP_FINISH:
        case DIALOG_PLATFORM_SIGNUP_INTRO:
        case DIALOG_PLATFORM_SIGNUP_LOGIN:
        case DIALOG_PLATFORM_SIGNUP_PAYMENT:
        case DIALOG_PLATFORM_SIGNUP_PAYMENT_PLAN:
        case DIALOG_PLATFORM_SIGNUP_PROFILE:
        case DIALOG_PLATFORM_SIGNUP_SUMMARY:
        case DIALOG_PLATFORM_SIGNUP_UPDATE_CONFIGURATION:
            provider = new SignupProvider(profileModel, workspace);
            break;
        case DIALOG_PROFILE_UPDATE:
            provider = new UpdateProfileProvider(profileModel);
            break;
        case DIALOG_PROFILE_UPDATE_PAYMENT_INFO:
            provider = new UpdateAccountProvider(profileModel, sessionModel);
            break;
        case DIALOG_PROFILE_UPDATE_PASSWORD:
            provider = new UpdatePasswordProvider(profileModel);
            break;
        case DIALOG_PROFILE_VERIFY_EMAIL:
            provider = new VerifyEMailProvider(profileModel, sessionModel);
            break;
        case MAIN_STATUS:
            provider = new MainStatusProvider(profileModel, contactModel, containerModel, sessionModel);
            break;
        case TAB_CONTACT:
            provider = new ContactProvider(profileModel, contactModel);
            break;
        case TAB_CONTAINER:
            provider = new ContainerProvider(profileModel, contactModel, containerModel, documentModel, userModel);
            break;
        case TAB_HELP:
            provider = new HelpProvider(profileModel, helpModel);
            break;
        default:
            throw Assert.createUnreachable("[UNKNOWN AVATAR ID]");
        }
        return provider;
    }
}
