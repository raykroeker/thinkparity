/*
 * Mar 10, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar;

import java.util.HashMap;
import java.util.Map;

import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AvatarRegistry {

	/**
	 * The underlying avatar registry.
	 * 
	 */
	private static Map<AvatarId, Object> REGISTRY;

	static {
		final Integer avatarCount = AvatarId.values().length;
		REGISTRY = new HashMap<AvatarId, Object>(avatarCount, 1.0F);
	}

	/**
	 * Create a AvatarRegistry.
	 * 
	 */
	public AvatarRegistry() { super(); }

	/**
	 * Determine whether or not the registry contains the avatar.
	 * 
	 * @param avatarId
	 *            The avatar id.
	 * @return True if the registry contains the avatar; false otherwise.
	 */
	public Boolean contains(final AvatarId avatarId) {
		synchronized(REGISTRY) { return REGISTRY.containsKey(avatarId); }
	}

	/**
	 * Obtain the avatar for the given id.
	 * 
	 * @param avatarId
	 *            The avatar id.
	 * @return The avatar; or null if the avatar has not yet been displayed.
	 */
	public Avatar get(final AvatarId avatarId) {
		synchronized(REGISTRY) { return (Avatar) REGISTRY.get(avatarId); }
	}

	/**
	 * Set the avatar in the registry.
	 * 
	 * @param avatarId
	 *            The avatar id.
	 * @param avatar
	 *            The avatar.
	 */
	Avatar put(final AvatarId avatarId, final Avatar avatar) {
		synchronized(REGISTRY) { return (Avatar) REGISTRY.put(avatarId, avatar); }
	}
}
