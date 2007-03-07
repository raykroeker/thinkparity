/*
 * Created On: Jan 16, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.session.SessionModel;
import com.thinkparity.ophelia.model.user.UserModel;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.contact.ReadContactProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container.ContainerVersionProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container.PublishContainerProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container.RenameDocumentProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile.UpdatePasswordProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile.UpdateProfileProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile.VerifyEMailProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.tab.archive.ArchiveTabProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.tab.contact.ContactProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.tab.container.ContainerProvider;
import com.thinkparity.ophelia.browser.util.ModelFactory;

import org.apache.log4j.Logger;

/**
 * @author raykroeker@gmail.com
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

    /** A thinkParity artifact interface. */
	protected final ArtifactModel artifactModel;

    /** A thinkParity contact interface. */
    protected final ContactModel contactModel;

    /** A thinkParity container interface. */
    protected final ContainerModel containerModel;

    /** The parity document interface. */
	protected final DocumentModel documentModel;

    /** An apache logger. */
	protected final Logger logger;
    
	/** A thinkParity session interface. */
	protected final SessionModel sessionModel;

    /** A thinkParity user interface. */
    protected final UserModel userModel;

	/** The local user's profile. */
    private final Profile profile;

    /** A thinkParity profile interface. */
    private final ProfileModel profileModel;

	/** Create ProviderFactory. */
	private ProviderFactory() {
		super();
		final ModelFactory modelFactory = ModelFactory.getInstance();
		this.artifactModel = modelFactory.getArtifactModel(getClass());
		this.contactModel = modelFactory.getContactModel(getClass());
        this.containerModel = modelFactory.getContainerModel(getClass());
		this.documentModel = modelFactory.getDocumentModel(getClass());
        this.profileModel = modelFactory.getProfileModel(getClass());
		this.sessionModel = modelFactory.getSessionModel(getClass());
        this.userModel = modelFactory.getUserModel(getClass());

		this.logger = Logger.getLogger(getClass());
        this.profile = profileModel.read();
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
        case DIALOG_CONTACT_READ:
            provider = new ReadContactProvider(profile, userModel);
            break;
        case DIALOG_CONTAINER_PUBLISH:
            provider = new PublishContainerProvider(profile, containerModel, userModel, contactModel);
            break; 
        case DIALOG_CONTAINER_RENAME_DOCUMENT:
            provider = new RenameDocumentProvider(profile, containerModel);
            break;     
        case DIALOG_CONTAINER_VERSION_COMMENT:
            provider = new ContainerVersionProvider(profile, containerModel, userModel);
            break;
        case DIALOG_PROFILE_UPDATE:
            provider = new UpdateProfileProvider(profile, profileModel);
            break;
        case DIALOG_PROFILE_UPDATE_PASSWORD:
            provider = new UpdatePasswordProvider(profile, profileModel);
            break;
        case DIALOG_PROFILE_VERIFY_EMAIL:
            provider = new VerifyEMailProvider(profile, profileModel);
            break;
        case TAB_ARCHIVE:
            provider = new ArchiveTabProvider(profile, contactModel, containerModel, documentModel, userModel);
            break;
        case TAB_CONTACT:
            provider = new ContactProvider(profile, profileModel, contactModel, userModel);
            break;
        case TAB_CONTAINER:
            provider = new ContainerProvider(profile, contactModel, containerModel, documentModel, userModel);
            break;
        default:
            throw Assert.createUnreachable("[UNKNOWN AVATAR ID]");
        }
        return provider;
    }
}
