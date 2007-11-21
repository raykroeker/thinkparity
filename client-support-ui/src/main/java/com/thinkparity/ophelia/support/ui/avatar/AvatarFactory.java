/*
 * Created On:  Nov 19, 2007 10:15:19 AM
 */
package com.thinkparity.ophelia.support.ui.avatar;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class AvatarFactory {

    /** A map of ids to their classes. */
    private static final Map<String, Class<? extends Avatar>> ID_CLASS_MAP;

    /** A message format pattern for the illegal state exception. */
    private static final String ILLEGAL_STATE_INSTANTIATED_PATTERN;

    /** A singleton avatar factory instance. */
    private static AvatarFactory SINGLETON;

    static {
        ID_CLASS_MAP = new HashMap<String, Class<? extends Avatar>>();
        ID_CLASS_MAP.put("/main/main", com.thinkparity.ophelia.support.ui.avatar.main.MainAvatar.class);

        ILLEGAL_STATE_INSTANTIATED_PATTERN = "Window \"{0}\" has already been instantiated.";
        SINGLETON = new AvatarFactory();
    }

    /**
     * Obtain an avatar factory instance.
     * 
     * @return An <code>AvatarFactory</code>.
     */
    public static AvatarFactory getInstance() {
        return SINGLETON;
    }

    /**
     * Instantiate an illegal state exception for an already instantiated
     * avatar .
     * 
     * @param id
     *            A <code>String</code>.
     * @return An <code>IllegalStateException</code>.
     */
    private static IllegalStateException newIllegalStateInstantiated(final String id) {
        return new IllegalStateException(MessageFormat.format(
                ILLEGAL_STATE_INSTANTIATED_PATTERN, id));
    }

    /** An avatar registry. */
    private final AvatarRegistry registry;


    /**
     * Create AvatarFactory.
     *
     */
    private AvatarFactory() {
        super();
        this.registry = new AvatarRegistry();
    }

    /**
     * Obtain an avatar.
     * 
     * @param <T>
     *            An avatar <code>Type</code>.
     * @param avatarClass
     *            A <code>Class<T></code>.
     * @return A <code>T</code>.
     */
    public Avatar newAvatar(final String id) {
        if (registry.isRegistered(id)) {
            throw newIllegalStateInstantiated(id);
        }
        return newAvatar(ID_CLASS_MAP.get(id));
    }

    /**
     * Instantiate an avatar.
     * 
     * @param <T>
     *            A avatar <code>Type</code>.
     * @param avatarClass
     *            A <code>Class<T></code>.
     * @return A <code>T</code>.
     */
    private Avatar newAvatar(final Class<?> avatarClass) {
        try {
            final Avatar avatar = (Avatar) avatarClass.newInstance();
            registry.register(avatar.getId(), avatar);
            return avatar;
        } catch (final IllegalAccessException iax) {
            throw new NoSuchAvatarException(avatarClass);
        } catch (final InstantiationException ix) {
            throw new NoSuchAvatarException(avatarClass);
        }
    }
}
