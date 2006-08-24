/*
 * Created On: Jan 20, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.text.MessageFormat;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.application.browser.display.avatar.dialog.ErrorAvatar;
import com.thinkparity.browser.application.browser.display.avatar.dialog.RenameAvatar;
import com.thinkparity.browser.application.browser.display.avatar.dialog.contact.CreateInvitationAvatar;
import com.thinkparity.browser.application.browser.display.avatar.dialog.contact.ReadContactAvatar;
import com.thinkparity.browser.application.browser.display.avatar.dialog.container.CreateContainerAvatar;
import com.thinkparity.browser.application.browser.display.avatar.dialog.profile.UpdateProfileAvatar;
import com.thinkparity.browser.application.browser.display.avatar.tab.contact.ContactAvatar;
import com.thinkparity.browser.application.browser.display.avatar.tab.container.ContainerAvatar;
import com.thinkparity.browser.application.browser.display.provider.ProviderFactory;
import com.thinkparity.browser.platform.application.dialog.ConfirmDialog;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;

/**
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class AvatarFactory {

	/** A singleton instance. */
	private static final AvatarFactory SINGLETON;

	static { SINGLETON = new AvatarFactory(); }

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

	/** The avatar registry. */
	private final AvatarRegistry avatarRegistry;

    /** An apache logger. */
    private final Logger logger;

	/** Create AvatarFactory */
	private AvatarFactory() {
		super();
		this.avatarRegistry = new AvatarRegistry();
        this.logger = Logger.getLogger(getClass());
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
		switch(id) {
        case TAB_CONTAINER:
            avatar = new ContainerAvatar();
            avatar.setContentProvider(ProviderFactory.getProvider(avatar.getId()));            
            break;
        case TAB_CONTACT:
            avatar = new ContactAvatar();
            avatar.setContentProvider(ProviderFactory.getProvider(avatar.getId()));
            break;
        case MAIN_CONTENT:
            avatar = new MainContentAvatar();
            break;
        case MAIN_STATUS:
            avatar = new MainStatusAvatar();
            break;
		case MAIN_TITLE:
			avatar = new MainTitleAvatar();
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
        case DIALOG_RENAME:
            avatar = new RenameAvatar();
            break;
        case DIALOG_CONTACT_READ:
            avatar = new ReadContactAvatar();
            avatar.setContentProvider(ProviderFactory.getProvider(avatar.getId()));
            break;
        case DIALOG_CONTAINER_CREATE:
            avatar = new CreateContainerAvatar();
            break;
        case DIALOG_PROFILE_UPDATE:
            avatar = new UpdateProfileAvatar();
            break;
		default: throw Assert.createUnreachable("UNKNOWN AVATAR");
		}
		register(avatar);
		return avatar;
	}

	/**
	 * Register an avatar in the registry.
	 * 
	 * @param avatar
	 *            The avatar to register.
	 */
	private void register(final Avatar avatar) {
        logger.info(MessageFormat.format("AVATAR {0} REGISTERED", avatar.getId().toString()));
		Assert.assertIsNull(
				"Avatar " + avatar.getId() + " already registered.",
				avatarRegistry.put(avatar.getId(), avatar));
	}
}
