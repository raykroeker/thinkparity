/*
 * Created On: Jan 16, 2006
 */
package com.thinkparity.browser.application.browser.display.provider;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.application.browser.display.provider.dialog.contact.ReadContactProvider;
import com.thinkparity.browser.application.browser.display.provider.dialog.profile.ResetPasswordProvider;
import com.thinkparity.browser.application.browser.display.provider.dialog.profile.UpdateProvider;
import com.thinkparity.browser.application.browser.display.provider.dialog.profile.VerifyEMailProvider;
import com.thinkparity.browser.application.browser.display.provider.tab.archive.ArchiveProvider;
import com.thinkparity.browser.application.browser.display.provider.tab.contact.ContactProvider;
import com.thinkparity.browser.application.browser.display.provider.tab.container.ContainerProvider;
import com.thinkparity.browser.platform.util.model.ModelFactory;

import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.contact.ContactModel;
import com.thinkparity.model.parity.model.container.ContainerModel;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.parity.model.profile.ProfileModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.user.UserModel;

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

    /** A thinkParity system message interface. */
	protected final SystemMessageModel systemMessageModel;

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
		this.systemMessageModel = modelFactory.getSystemMessageModel(getClass());
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
            provider = new ReadContactProvider(profile, contactModel);
            break;
        case TAB_ARCHIVE:
            provider = new ArchiveProvider(profile);
            break;
        case TAB_CONTACT:
            provider = new ContactProvider(profile, contactModel, userModel);
            break;
        case TAB_CONTAINER:
            provider = new ContainerProvider(profile, containerModel, documentModel);
            break;
        case DIALOG_PROFILE_RESET_PASSWORD:
            provider = new ResetPasswordProvider(profile, profileModel);
            break;
        case DIALOG_PROFILE_UPDATE:
            provider = new UpdateProvider(profile, profileModel);
            break;
        case DIALOG_PROFILE_VERIFY_EMAIL:
            provider = new VerifyEMailProvider(profile, profileModel);
            break;
        default:
            throw Assert.createUnreachable("[UNKNOWN AVATAR ID]");
        }
        return provider;
    }
}
