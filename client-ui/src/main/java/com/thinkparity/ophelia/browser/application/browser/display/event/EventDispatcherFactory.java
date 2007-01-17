/*
 * Created On:  25-Dec-06 10:27:15 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.event;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.model.container.ContainerModel;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.event.tab.archive.ArchiveTabDispatcher;
import com.thinkparity.ophelia.browser.application.browser.display.event.tab.container.ContainerTabDispatcher;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher;
import com.thinkparity.ophelia.browser.util.ModelFactory;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class EventDispatcherFactory {

    /** An <code>EventDispatcherFactory</code> singleton. */
    private static final EventDispatcherFactory SINGLETON;

    static {
        SINGLETON = new EventDispatcherFactory();
    }

    /**
     * Obtain the event dispatcher for an avatar.
     * 
     * @param avatarId
     *            An <code>AvatarId</code>.
     * @return An <code>EventDispatcher</code>.
     */
    public static EventDispatcher getDispatcher(final AvatarId avatarId) {
        return SINGLETON.doGetDispatcher(avatarId);
    }

    /** The <code>ContainerModel</code>. */
    private final ContainerModel containerModel;

    /**
     * Create EventDispatcherFactory.
     *
     */
    private EventDispatcherFactory() {
        super();
        final ModelFactory modelFactory = ModelFactory.getInstance();
        this.containerModel = modelFactory.getContainerModel(getClass());
    }

    /**
     * Obtain the event dispatcher for an avatar.
     * 
     * @param avatarId
     *            An <code>AvatarId</code>.
     * @return An <code>EventDispatcher</code>.
     */
    private EventDispatcher doGetDispatcher(final AvatarId avatarId) {
        final EventDispatcher eventDispatcher;
        switch (avatarId) {
        case TAB_ARCHIVE:
            eventDispatcher = new ArchiveTabDispatcher(containerModel);
            break;
        case TAB_CONTAINER:
            eventDispatcher = new ContainerTabDispatcher(containerModel);
            break;
        default:
            throw Assert.createUnreachable("No dispatcher available for avatar:  {0}", avatarId);
        }
        return eventDispatcher;
    }
}
