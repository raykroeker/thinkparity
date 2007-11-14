/*
 * Created On: Jan 20, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.ConfirmAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.FileChooserAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.ShowLicenseAgreementAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.StatusAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.backup.RestoreBackupAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.contact.CreateOutgoingEMailInvitationAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.*;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.*;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact.ContactTabAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container.ContainerTabAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.help.HelpTabAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.event.EventDispatcherFactory;
import com.thinkparity.ophelia.browser.application.browser.display.provider.ProviderFactory;
import com.thinkparity.ophelia.browser.platform.BrowserPlatform;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.avatar.ErrorAvatar;
import com.thinkparity.ophelia.browser.platform.avatar.ErrorDetailsAvatar;
import com.thinkparity.ophelia.browser.platform.firstrun.*;

/**
 * <b>Title:</b>thinkParity OpheliaUI Avatar Factory<br>
 * <b>Description:</b>A singleton instance of an avatar instance factory.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.45
 */
public final class AvatarFactory {

	/** A singleton instance. */
	private static final AvatarFactory SINGLETON;

	static {
        final Platform platform = BrowserPlatform.getInstance();
        SINGLETON = new AvatarFactory(platform);
	}

	/**
	 * Create an avatar.
	 * 
	 * @param id
	 *            The avatar id.
	 * @return The avatar.
	 */
	public static Avatar create(final AvatarId id) {
		return SINGLETON.doCreate(id);
	}

	/** The <code>AvatarRegistry</code>. */
	private final AvatarRegistry avatarRegistry;

    /** The thinkParity <code>Platform</code>. */
    private final Platform platform;

	/**
     * Create AvatarFactory.
     * 
     */
	private AvatarFactory(final Platform platform) {
		super();
		this.avatarRegistry = new AvatarRegistry();
        this.platform = platform;
	}

	/**
     * Create an avatar; set its provider (if required) and register it.
     * 
     * @param id
     *            The avatar id.
     * @return The avatar.
     */
	private Avatar doCreate(final AvatarId id) {
		final Avatar avatar;
		switch (id) {
		case MAIN_CONTENT:
		    avatar = newAvatar(MainContentAvatar.class);
		    break;
		case MAIN_STATUS:
		    avatar = newAvatar(MainStatusAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            avatar.setEventDispatcher(EventDispatcherFactory.getDispatcher(id));
		    break;
		case MAIN_TITLE:
		    avatar = newAvatar(MainTitleAvatar.class);
            avatar.setEventDispatcher(EventDispatcherFactory.getDispatcher(id));
		    break;

        case TAB_CONTAINER:
            avatar = newAvatar(ContainerTabAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            avatar.setEventDispatcher(EventDispatcherFactory.getDispatcher(id));
            break;
        case TAB_CONTACT:
            avatar = newAvatar(ContactTabAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            avatar.setEventDispatcher(EventDispatcherFactory.getDispatcher(id));
			break;
        case TAB_HELP:
            avatar = newAvatar(HelpTabAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            avatar.setEventDispatcher(EventDispatcherFactory.getDispatcher(id));
            break;

        case DIALOG_CONFIRM:
            avatar = newAvatar(ConfirmAvatar.class);
            break;
        case DIALOG_ERROR:
            avatar = newAvatar(ErrorAvatar.class);
            break;
        case DIALOG_ERROR_DETAILS:
            avatar = newAvatar(ErrorDetailsAvatar.class);
            break;
        case DIALOG_FILE_CHOOSER:
            avatar = newAvatar(FileChooserAvatar.class);
            break;
        case DIALOG_SHOW_LICENSE_AGREEMENT:
            avatar = newAvatar(ShowLicenseAgreementAvatar.class);
            break;
        case DIALOG_STATUS:
            avatar = newAvatar(StatusAvatar.class);
            break;

        case DIALOG_BACKUP_RESTORE:
            avatar = newAvatar(RestoreBackupAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;

        case DIALOG_CONTACT_CREATE_OUTGOING_EMAIL_INVITATION:
            avatar = newAvatar(CreateOutgoingEMailInvitationAvatar.class);
            break;

        case DIALOG_CONTAINER_CREATE:
            avatar = newAvatar(CreateContainerAvatar.class);
            break;
        case DIALOG_CONTAINER_PUBLISH:
            avatar = newAvatar(PublishContainerAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_CONTAINER_RENAME:
            avatar = newAvatar(RenameContainerAvatar.class);
            break;
        case DIALOG_CONTAINER_RENAME_DOCUMENT:
            avatar = newAvatar(RenameDocumentAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_CONTAINER_TEAM_MEMBER_INFO:
            avatar = newAvatar(TeamMemberInfoAvatar.class);
            break;
        case DIALOG_CONTAINER_UPDATE_DRAFT_COMMENT:
            avatar = newAvatar(UpdateDraftCommentAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_CONTAINER_VERSION_COMMENT:
            avatar = newAvatar(ContainerVersionCommentAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;

        case DIALOG_PLATFORM_SIGNUP:
            avatar = newAvatar(SignupAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_PLATFORM_SIGNUP_ACCOUNT:
            avatar = newAvatar(SignupAccountInfoAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_PLATFORM_SIGNUP_AGREEMENT:
            avatar = newAvatar(SignupLicenseAgreementAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_PLATFORM_SIGNUP_CREDENTIALS:
            avatar = newAvatar(SignupCredentialsAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_PLATFORM_SIGNUP_FINISH:
            avatar = newAvatar(SignupFinishAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_PLATFORM_SIGNUP_INTRO:
            avatar = newAvatar(SignupIntroAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_PLATFORM_SIGNUP_LOGIN:
            avatar = newAvatar(SignupLoginAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_PLATFORM_SIGNUP_PAYMENT:
            avatar = newAvatar(SignupPaymentInfoAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_PLATFORM_SIGNUP_PAYMENT_PLAN:
            avatar = newAvatar(SignupPaymentPlanAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_PLATFORM_SIGNUP_PROFILE:
            avatar = newAvatar(SignupProfileInfoAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_PLATFORM_SIGNUP_SUMMARY:
            avatar = newAvatar(SignupSummaryAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;

        case DIALOG_PROFILE_UPDATE:
            avatar = newAvatar(UpdateProfileAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_PROFILE_UPDATE_PAYMENT_INFO:
            avatar = newAvatar(UpdatePaymentInfoAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_PROFILE_UPDATE_PASSWORD:
            avatar = newAvatar(UpdatePasswordAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_PROFILE_UPGRADE_ACCOUNT:
            avatar = newAvatar(UpgradeAccountAvatar.class);
            break;
        case DIALOG_PROFILE_UPGRADE_ACCOUNT_AGREEMENT:
            avatar = newAvatar(UpgradeAccountAgreementAvatar.class);
            break;
        case DIALOG_PROFILE_UPGRADE_ACCOUNT_INTRO:
            avatar = newAvatar(UpgradeAccountIntroAvatar.class);
            break;
        case DIALOG_PROFILE_UPGRADE_ACCOUNT_PAYMENT:
            avatar = newAvatar(UpgradeAccountPaymentAvatar.class);
            break;
        case DIALOG_PROFILE_UPGRADE_ACCOUNT_SUMMARY:
            avatar = newAvatar(UpgradeAccountSummaryAvatar.class);
            break;
        case DIALOG_PROFILE_VERIFY_EMAIL:
            avatar = newAvatar(VerifyEMailAvatar.class);
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
		default:
            throw Assert.createUnreachable("UNKNOWN AVATAR");
		}
		return avatar;
	}

    /**
     * Create an avatar.
     * 
     * @param avatarImplementation
     *            An avatar implementation <code>Class</code>.
     * @return An instance of the avatar.
     */
    private final <T extends Avatar> T newAvatar(
            final Class<T> avatarImplementation) {
        try {
            final T avatarInstance = (T) avatarImplementation.newInstance();
            avatarInstance.initialize(platform);
            register(avatarInstance);
            return avatarInstance;
        } catch (final IllegalAccessException ix) {
            throw new BrowserException("Could not create instance of " + avatarImplementation.getName() + ".", ix);
        } catch (final InstantiationException ix) {
            throw new BrowserException("Could not create instance of " + avatarImplementation.getName() + ".", ix);
        }
    }

    /**
     * Register an avatar in the registry.
     * 
     * @param avatar
     *            The avatar to register.
     */
    private void register(final Avatar avatar) {
        Assert.assertNotTrue(avatarRegistry.contains(avatar.getId()),
                "Avatar {0} already registered.", avatar);
        avatarRegistry.put(avatar.getId(), avatar);
    }
}
