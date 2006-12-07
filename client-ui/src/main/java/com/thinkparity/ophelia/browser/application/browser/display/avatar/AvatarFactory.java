/*
 * Created On: Jan 20, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.AbstractJPanel;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.DisplayInfoAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.ErrorAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.contact.CreateInvitationAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.contact.UserInfoAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.ContainerVersionCommentAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.CreateContainerAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.ExportAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.PublishContainerAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.RenameContainerAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.RenameDocumentAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.EditProfileAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.ResetPasswordAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.UpdateProfileAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.VerifyEMailAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact.ContactAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container.ContainerTabAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.provider.ProviderFactory;
import com.thinkparity.ophelia.browser.platform.BrowserPlatform;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.application.dialog.ConfirmDialog;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.plugin.PluginExtension;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabListExtension;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabPanelExtension;

/**
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class AvatarFactory {

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

    /**
     * Create an avatar.
     * 
     * @param tabExtension
     *            A <code>TabExtension</code>.
     * @return An <code>Avatar</code>.
     */
    public static Avatar create(final TabListExtension tabListExtension) {
        return SINGLETON.doCreate(tabListExtension);
    }

    /**
     * Create an avatar.
     * 
     * @param tabExtension
     *            A <code>TabExtension</code>.
     * @return An <code>Avatar</code>.
     */
    public static Avatar create(final TabPanelExtension tabPanelExtension) {
        return SINGLETON.doCreate(tabPanelExtension);
    }

	/** The avatar registry. */
	private final AvatarRegistry avatarRegistry;

	/** Create AvatarFactory */
	private AvatarFactory(final Platform platform) {
		super();
		this.avatarRegistry = new AvatarRegistry();
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
		    avatar = new MainContentAvatar();
		    break;
		case MAIN_STATUS:
		    avatar = new MainStatusAvatar();
		    break;
		case MAIN_TITLE:
		    avatar = new MainTitleAvatar();
		    break;

        case TAB_CONTAINER:
            avatar = new ContainerTabAvatar();
            avatar.setContentProvider(ProviderFactory.getProvider(id));            
            break;
        case TAB_CONTACT:
            avatar = new ContactAvatar();
            avatar.setContentProvider(ProviderFactory.getProvider(id));
			break;

        case DIALOG_CONFIRM:
            avatar = new ConfirmDialog();
            break;
        case DIALOG_ERROR:
            avatar = new ErrorAvatar();
            break;

        case DIALOG_CONTACT_CREATE_OUTGOING_INVITATION:
            avatar = new CreateInvitationAvatar();
            break;
        case DIALOG_CONTACT_READ:
            avatar = new UserInfoAvatar();
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;

        case DIALOG_CONTAINER_CREATE:
            avatar = new CreateContainerAvatar();
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_CONTAINER_EXPORT:
            avatar = new ExportAvatar();
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_CONTAINER_PUBLISH:
            avatar = new PublishContainerAvatar();
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_CONTAINER_RENAME:
            avatar = new RenameContainerAvatar();
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_CONTAINER_RENAME_DOCUMENT:
            avatar = new RenameDocumentAvatar();
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_CONTAINER_VERSION_COMMENT:
            avatar = new ContainerVersionCommentAvatar();
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
            
        case DIALOG_PLATFORM_DISPLAY_INFO:
            avatar = new DisplayInfoAvatar();
            break;

        case DIALOG_PROFILE_EDIT:
            avatar = new EditProfileAvatar();
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_PROFILE_RESET_PASSWORD:
            avatar = new ResetPasswordAvatar();
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_PROFILE_UPDATE:
            avatar = new UpdateProfileAvatar();
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
        case DIALOG_PROFILE_VERIFY_EMAIL:
            avatar = new VerifyEMailAvatar();
            avatar.setContentProvider(ProviderFactory.getProvider(id));
            break;
		default:
            throw Assert.createUnreachable("UNKNOWN AVATAR");
		}
		register(avatar);
		return avatar;
	}

    /**
     * Create an avatar.
     * 
     * @param tabExtension
     *            A <code>TabExtension</code>.
     * @return An <code>Avatar</code>.
     */
    private Avatar doCreate(final TabListExtension tabListExtension) {
        final Avatar avatar = tabListExtension.createAvatar();
        avatar.setContentProvider(tabListExtension.getProvider());
        register(avatar, tabListExtension);
        return avatar;
    }

    /**
     * Create an avatar.
     * 
     * @param tabExtension
     *            A <code>TabExtension</code>.
     * @return An <code>Avatar</code>.
     */
    private Avatar doCreate(final TabPanelExtension tabPanelExtension) {
        final Avatar avatar = tabPanelExtension.createAvatar();
        avatar.setContentProvider(tabPanelExtension.getProvider());
        register(avatar, tabPanelExtension);
        return avatar;
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

    /**
     * Register an avatar in the registry.
     * 
     * @param avatar
     *            The avatar to register.
     * @param tabExtension
     *            The <code>TabExtension</code> the avatar belongs to.
     */
    private void register(final AbstractJPanel avatar, final PluginExtension extension) {
        Assert.assertNotTrue(avatarRegistry.contains(extension),
                "Avatar for tab extension {0} already registered.", extension);
        avatarRegistry.put(extension, avatar);
    }
}
