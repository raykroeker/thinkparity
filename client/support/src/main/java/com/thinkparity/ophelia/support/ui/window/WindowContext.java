/*
 * Created On:  Nov 19, 2007 10:13:27 AM
 */
package com.thinkparity.ophelia.support.ui.window;

import com.thinkparity.ophelia.support.ui.avatar.Avatar;
import com.thinkparity.ophelia.support.ui.avatar.AvatarFactory;
import com.thinkparity.ophelia.support.ui.avatar.AvatarRegistry;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class WindowContext {

    /** An avatar factory. */
    private final AvatarFactory avatarFactory;

    /** An avatar registry. */
    private final AvatarRegistry avatarRegistry;

    /**
     * Create WindowContext.
     *
     */
    WindowContext() {
        super();
        this.avatarFactory = AvatarFactory.getInstance();
        this.avatarRegistry = new AvatarRegistry();
    }

    /**
     * Lookup an avatar.
     * 
     * @param id
     *            A <code>String</code>.
     * @return An <code>Avatar</code>.
     */
    public Avatar lookupAvatar(final String id) {
        if (avatarRegistry.isRegistered(id)) {
            return avatarRegistry.lookup(id);
        } else {
            return avatarFactory.newAvatar(id);
        }
    }
}
