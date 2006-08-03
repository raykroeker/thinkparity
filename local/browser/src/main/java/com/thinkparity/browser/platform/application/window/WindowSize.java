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
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

/**
 * Simple map of the avatar being displayed to the size of the window required.
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
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
        // DIMENSION Confirmation Dialogue 300x125
        sizeMap.put(AvatarId.CONFIRM_DIALOGUE, new Dimension(300, 125));
        // DIMENSION Error Dialogue 300x125
        sizeMap.put(AvatarId.ERROR_DIALOGUE, new Dimension(300, 125));
        // DIMENSION Rename Dialogue 300x125
        sizeMap.put(AvatarId.RENAME_DIALOGUE, new Dimension(300, 70));
		// DIMENSION Send Dialogue 405x308
		sizeMap.put(AvatarId.SESSION_SEND_FORM, new Dimension(405, 265));
		// DIMENSION Send Version Window 405x308
		sizeMap.put(AvatarId.SESSION_SEND_VERSION, new Dimension(405, 308));
		// DIMENSION Platform Login Window 385x173
		sizeMap.put(AvatarId.PLATFORM_LOGIN, new Dimension(358, 129));

		// DIMENSION Invite Partner Dialogue 520x330
		sizeMap.put(AvatarId.SESSION_INVITE_PARTNER, new Dimension(520, 330));
		// DIMENSION Manage Contacts Dialogue 520x330
		sizeMap.put(AvatarId.SESSION_MANAGE_CONTACTS, new Dimension(520, 330));
		// DIMENSION Search Partner Dialogue 520x330
		sizeMap.put(AvatarId.SESSION_SEARCH_PARTNER, new Dimension(520, 330));
        // DIMENSION Add Team Member
        sizeMap.put(AvatarId.ADD_TEAM_MEMBER, new Dimension(392 + 8, 233 + 8));
        // DIMENSION Contact Info
        sizeMap.put(AvatarId.CONTACT_INFO_DIALOGUE, new Dimension(300,170));
        // DIMENSION New Container
        sizeMap.put(AvatarId.NEW_CONTAINER_DIALOGUE, new Dimension(350,150));
        // DIMENSION Manage Team
        sizeMap.put(AvatarId.MANAGE_TEAM, new Dimension(470, 350));
        // DIMENSION Invite Contact
        sizeMap.put(AvatarId.INVITE, new Dimension(300,200));
        // DIMENSION Add Contact
        sizeMap.put(AvatarId.CONTACT_ADD, new Dimension(300, 125));
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
