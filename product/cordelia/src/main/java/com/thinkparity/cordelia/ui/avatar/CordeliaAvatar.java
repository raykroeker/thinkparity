/**
 * 
 */
package com.thinkparity.cordelia.ui.avatar;

import com.thinkparity.codebase.ui.application.Application;
import com.thinkparity.codebase.ui.avatar.AvatarId;
import com.thinkparity.codebase.ui.avatar.DefaultAvatar;
import com.thinkparity.codebase.ui.provider.Provider;

import com.thinkparity.cordelia.ui.CordeliaPlatform;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class CordeliaAvatar<T extends Application<CordeliaPlatform>, U extends Provider>
        extends DefaultAvatar<CordeliaPlatform, T, U> {

    /** The avatar's <code>AvatarId</code>. */
    private final AvatarId id;

    /** The <code>CordeliaPlatform</code>. */
    private CordeliaPlatform platform;

    /**
     * Create CordeliaAvatar.
     * 
     */
    protected CordeliaAvatar(final AvatarId id) {
        super();
        this.id = id;
    }

    /**
     * @see com.thinkparity.codebase.ui.avatar.Avatar#getId()
     * 
     */
    public final AvatarId getId() {
        return id;
    }

    /**
     * @see com.thinkparity.codebase.ui.avatar.Avatar#getPlatform()
     * 
     */
    public final CordeliaPlatform getPlatform() {
        return platform;
    }
}
