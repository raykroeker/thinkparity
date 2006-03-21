/*
 * Mar 10, 2006
 */
package com.thinkparity.browser.platform.application.window;

import java.awt.Dimension;
import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

/**
 * Simple map of the avatar being displayed to the size of the window required.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class WindowSize {

	/**
	 * Default window size.
	 * 
	 */
	private static final Dimension defaultSize;

	/**
	 * Map of avatar ids to dimensions.
	 * 
	 */
	private static final Map<AvatarId, Object> sizeMap;

	static {
		// DIMENSION Default Window 300x300
		defaultSize = new Dimension(300, 300);

		sizeMap = new Hashtable<AvatarId, Object>(2, 1.0F);
		// DIMENSION History Window 249x484
		sizeMap.put(AvatarId.DOCUMENT_HISTORY3, new Dimension(249, 484));
		// DIMENSION Invite Contact Window 409x196
		sizeMap.put(AvatarId.SESSION_INVITE_CONTACT, new Dimension(409, 196));
		// DIMENSION Manage Contacts Window 520x321
		sizeMap.put(AvatarId.SESSION_MANAGE_CONTACTS, new Dimension(520, 321));
		// DIMENSION Send Document Window 405x308
		sizeMap.put(AvatarId.SESSION_SEND_FORM, new Dimension(405, 308));
		// DIMENSION Platform Login Window
		sizeMap.put(AvatarId.PLATFORM_LOGIN, new Dimension(385, 154));
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
		this.logger = LoggerFactory.getLogger(getClass());
	}

	/**
	 * Obtain the window size for an avatar.
	 * 
	 * @param avatarId
	 *            The avatar id.
	 * @return The window size.
	 */
	Dimension get(final AvatarId avatarId) {
		if(sizeMap.containsKey(avatarId)) {
			return (Dimension) sizeMap.get(avatarId);
		}
		else {
			logger.warn("No window size for:  " + avatarId);
			return defaultSize;
		}
	}
}
