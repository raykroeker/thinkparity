/*
 * Created On:  Nov 19, 2007 11:45:32 AM
 */
package com.thinkparity.ophelia.support.ui.avatar;

import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Map;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class AvatarRegistry {

    /** A message format pattern for the illegal state registry. */
    private static final String ILLEGAL_STATE_REGISTERED_PATTERN;

    /** The avatar registry map. */
    private static final Map<String, Avatar> REGISTRY;

    static {
        REGISTRY = new Hashtable<String, Avatar>(1, 0.75F);
    }

    static {
        ILLEGAL_STATE_REGISTERED_PATTERN = "Avatar \"{0}\" has already been registered.";
    }

    /**
     * Create AvatarRegistry.
     *
     */
    public AvatarRegistry() {
        super();
    }

    /**
     * Determine if an avatar is registered.
     * 
     * @param id
     *            A <code>String</code>.
     * @return A <code>Boolean</code>.
     */
    public Boolean isRegistered(final String id) {
        return Boolean.valueOf(REGISTRY.containsKey(id));
    }

    /**
     * Lookup an avatar.
     * 
     * @param id
     *            A <code>String</code>.
     * @return A <code>Avatar</code>.
     */
    public Avatar lookup(final String id) {
        return REGISTRY.get(id);
    }

    /**
     * Register an avatar.
     * 
     * @param id
     *            A <code>String</code>.
     * @param avatar
     *            A <code>Avatar</code>.
     */
    void register(final String id, final Avatar avatar) {
        synchronized (REGISTRY) {
            if (REGISTRY.containsKey(id)) {
                throw newIllegalStateRegistered(id);
            } else {
                REGISTRY.put(id, avatar);
            }
        }
    }

    /**
     * Instantiate an illegal state exception for a registered state.
     * 
     * @param id
     *            A <code>String</code>.
     */
    private IllegalStateException newIllegalStateRegistered(final String id) {
        return new IllegalStateException(MessageFormat.format(
                ILLEGAL_STATE_REGISTERED_PATTERN, id));
    }
}
