/*
 * Created On:  25-Dec-06 10:27:15 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.event;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.model.backup.BackupModel;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.session.SessionModel;

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

    /** An instance of <code>BackupModel</code>. */
    private final BackupModel backupModel;

    /** An instance of <code>ContainerModel</code>. */
    private final ContainerModel containerModel;

    /** An instance of <code>ContactModel</code>. */
    private final ContactModel contactModel;

    /** An instance of <code>ProfileModel</code>. */
    private final ProfileModel profileModel;

    /** An instance of <code>SessionModel</code>. */
    private final SessionModel sessionModel;

    /**
     * Create EventDispatcherFactory.
     *
     */
    private EventDispatcherFactory() {
        super();
        final ModelFactory modelFactory = ModelFactory.getInstance();
        this.backupModel = modelFactory.getBackupModel(getClass());
        this.contactModel = modelFactory.getContactModel(getClass());
        this.containerModel = modelFactory.getContainerModel(getClass());
        this.profileModel = modelFactory.getProfileModel(getClass());
        this.sessionModel = modelFactory.getSessionModel(getClass());
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
        case MAIN_STATUS:
            eventDispatcher = new MainStatusDispatcher(backupModel,
                    contactModel, containerModel, profileModel, sessionModel);
            break;
        default:
            throw Assert.createUnreachable("No dispatcher available for avatar:  {0}", avatarId);
        }
        return eventDispatcher;
    }
}
