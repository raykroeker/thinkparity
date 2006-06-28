/*
 * Created On: Jun 25, 2006 11:17:52 AM
 * $Id$
 */
package com.thinkparity.migrator.application.migrator;

import java.util.HashMap;
import java.util.Map;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.migrator.application.AbstractApplication;
import com.thinkparity.migrator.application.ApplicationId;
import com.thinkparity.migrator.application.ApplicationListener;
import com.thinkparity.migrator.application.ApplicationStatus;
import com.thinkparity.migrator.application.migrator.avatar.Avatar;
import com.thinkparity.migrator.application.migrator.avatar.AvatarFactory;
import com.thinkparity.migrator.application.migrator.avatar.AvatarId;
import com.thinkparity.migrator.application.migrator.avatar.AvatarRegistry;
import com.thinkparity.migrator.platform.Platform;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class Application extends AbstractApplication
        implements com.thinkparity.migrator.application.Application {

    /** The singleton application instance. */
    private static Application SINGLETON;

    /**
     * Obtain the application instance.
     * 
     * @return The application.
     */
    public static Application getInstance() { return SINGLETON; }

    /** The avatar input lookup. */
    private final Map<AvatarId, Object> avatarInputLookup;

    /** The avatar registry. */
    private final AvatarRegistry avatarRegistry;

    /** The application event dispatcher. */
    private EventDispatcher eventDispatcher;

    /** The migrator window. */
    private Window window;

    /**
     * Create Application.
     * 
     * @param platform
     *            The migrator platform.
     */
    public Application(final Platform platform) {
        super(platform);
        this.avatarInputLookup = new HashMap<AvatarId, Object>(AvatarId.values().length, 1.0F);
        this.avatarRegistry = new AvatarRegistry();
        SINGLETON = this;
    }

    /**
     * @see com.thinkparity.migrator.application.Application#addListener(com.thinkparity.migrator.application.ApplicationListener)
     */
    public void addListener(ApplicationListener l) {
        throw Assert.createNotYetImplemented("Application#addListener");
    }

    /**
     * Display the release summary.
     *
     */
    public void displayReleaseSummary() {
        displayAvatar(AvatarId.RELEASE_SUMMARY);
    }

    /**
     * @see com.thinkparity.migrator.application.Application#end(com.thinkparity.migrator.platform.Platform)
     */
    public void end(Platform platform) {
        throw Assert.createNotYetImplemented("Application#end");
    }

    /**
     * @see com.thinkparity.migrator.application.Application#getId()
     */
    public ApplicationId getId() { return ApplicationId.MIGRATOR; }

    /**
     * @see com.thinkparity.migrator.application.Application#hibernate(com.thinkparity.migrator.platform.Platform)
     */
    public void hibernate(Platform platform) {
        throw Assert.createNotYetImplemented("Application#hibernate");
    }

    /**
     * @see com.thinkparity.migrator.application.Application#removeListener(com.thinkparity.migrator.application.ApplicationListener)
     */
    public void removeListener(ApplicationListener l) {
        throw Assert.createNotYetImplemented("Application#removeListener");
    }

    /**
     * @see com.thinkparity.migrator.application.Application#restore(com.thinkparity.migrator.platform.Platform)
     */
    public void restore(Platform platform) {
        throw Assert.createNotYetImplemented("Application#restore");
    }

    /**
     * @see com.thinkparity.migrator.application.Application#start(com.thinkparity.migrator.platform.Platform)
     */
    public void start(final Platform platform) {
        assertStatusChange(ApplicationStatus.STARTING);
        setStatus(ApplicationStatus.STARTING);

        eventDispatcher = new EventDispatcher();
        eventDispatcher.start();

        assertStatusChange(ApplicationStatus.RUNNING);
        openWindow();
        setStatus(ApplicationStatus.RUNNING);
        notifyStart();
    }

    /**
     * Display the avatar.
     * 
     * @param avatarId
     *            An avatar id.
     */
    private void displayAvatar(final AvatarId id) {
        Assert.assertNotNull("", id);
        final Avatar avatar = lookupAvatar(id);
        final Object input = lookupAvatarInput(id);
        if(null == input) {}
        else { avatar.setInput(input); }

        window.displayAvatar(avatar);
    }

    /**
     * Lookup an avatar. If the avatar does not yet exist in the registry; it is
     * created via the factory.
     * 
     * @param id
     *            The avatar id.
     * @return The avatar.
     */
    private Avatar lookupAvatar(final AvatarId id) {
        final Avatar avatar;
        if(avatarRegistry.contains(id)) { avatar = avatarRegistry.get(id); }
        else { avatar = AvatarFactory.create(this, id); }
        return avatar;
    }

    /**
     * Lookup an avatar's input.
     * 
     * @param id
     *            The avatar id.
     * @return The avatar input. If no input is available; null is returned.
     */
    private Object lookupAvatarInput(final AvatarId id) {
        return avatarInputLookup.get(id);
    }

    /** Open the migrator window. */
    private void openWindow() {
        window = new Window(this);
        window.open();
    }
}
