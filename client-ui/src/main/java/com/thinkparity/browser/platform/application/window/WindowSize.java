/*
 * Created On: Mar 10, 2006
 * $Id$
 */
package com.thinkparity.browser.platform.application.window;

import java.awt.Dimension;
import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.display.avatar.AvatarId;

/**
 * Simple map of the avatar being displayed to the size of the window required.
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
class WindowSize {

	/** A registry of avatar sizes. */
	private static final Map<AvatarId, Object> REGISTRY;

	static {
		REGISTRY = new Hashtable<AvatarId, Object>(AvatarId.values().length, 1.0F);
        // DIMENSION Confirmation Dialogue 300x125
        REGISTRY.put(AvatarId.DIALOG_CONFIRM, new Dimension(300, 125));
        // DIMENSION Error Dialogue 500x500
        REGISTRY.put(AvatarId.DIALOG_ERROR, new Dimension(550, 500));
        // DIMENSION Rename Dialogue 300x125
        REGISTRY.put(AvatarId.DIALOG_RENAME, new Dimension(300, 70));
		// DIMENSION Platform Login Window 385x173
		REGISTRY.put(AvatarId.DIALOG_PLATFORM_LOGIN, new Dimension(358, 129));

        // DIMENSION New Container
        REGISTRY.put(AvatarId.DIALOG_CONTAINER_CREATE, new Dimension(350,150));
        // DIMENSION Add Contact
        REGISTRY.put(AvatarId.DIALOG_CONTACT_CREATE_OUTGOING_INVITATION, new Dimension(300, 125));
	}

	/**
	 * An apache logger.
	 * 
	 */
	protected final Logger logger;

    /**
	 * Create a WindowSize.
	 */
	WindowSize() {
		super();
		this.logger = Logger.getLogger(getClass());
	}

    /**
     * Determine if a size is set for the avatar.
     * 
     * @param avatarId
     *            An avatar id.
     * @return True if a size has been set; false otherwise.
     */
    Boolean isSetSize(final AvatarId avatarId) {
        return REGISTRY.containsKey(avatarId);
    }

	/**
	 * Obtain the window size for an avatar.
	 * 
	 * @param avatarId
	 *            The avatar id.
	 * @return The window size.
	 */
	Dimension get(final AvatarId avatarId) {
	    return (Dimension) REGISTRY.get(avatarId);
    }
}
