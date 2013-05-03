/*
 * Created On:  Nov 19, 2007 9:44:11 AM
 */
package com.thinkparity.ophelia.support.ui.action;

import java.io.File;

import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.support.ui.avatar.Avatar;
import com.thinkparity.ophelia.support.ui.avatar.AvatarFactory;
import com.thinkparity.ophelia.support.ui.avatar.AvatarRegistry;
import com.thinkparity.ophelia.support.ui.window.Window;
import com.thinkparity.ophelia.support.ui.window.WindowFactory;
import com.thinkparity.ophelia.support.ui.window.WindowRegistry;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ActionContext {

    /** An avatar factory. */
    private final AvatarFactory avatarFactory;

    /** An avatar registry. */
    private final AvatarRegistry avatarRegistry;

    /** The environment. */
    private Environment environment;

    /** A window factory. */
    private final WindowFactory windowFactory;

    /** A window registry. */
    private final WindowRegistry windowRegistry;

    /**
     * Create ActionContext.
     *
     */
    ActionContext() {
        super();
        this.avatarFactory = AvatarFactory.getInstance();
        this.avatarRegistry = new AvatarRegistry();
        this.windowFactory = WindowFactory.getInstance();
        this.windowRegistry = new WindowRegistry();
    }

    /**
     * Obtain environment.
     *
     * @return A Environment.
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Obtain the image directory.
     * 
     * @return A <code>File</code>.
     */
    public File getImageDirectory() {
        return new File(com.thinkparity.bootstrap.Constants.Directories.ThinkParity.Directory,
                System.getProperty(com.thinkparity.bootstrap.Constants.PropertyNames.ThinkParity.Image)); 
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

    /**
     * Lookup a window.
     * 
     * @param id
     *            A <code>String</code>.
     * @return A <code>Window</code>.
     */
    public Window lookupWindow(final String id) {
        if (windowRegistry.isRegistered(id)) {
            return windowRegistry.lookup(id);
        } else {
            return windowFactory.newWindow(id);
        }
    }

    /**
     * Set environment.
     *
     * @param environment
     *		A Environment.
     */
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }
}
