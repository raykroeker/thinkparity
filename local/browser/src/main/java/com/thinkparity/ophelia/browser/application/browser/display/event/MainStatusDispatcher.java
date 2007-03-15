/*
 * Created On:  14-Mar-07 2:36:36 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.event;

import com.thinkparity.ophelia.model.backup.BackupModel;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.events.BackupAdapter;
import com.thinkparity.ophelia.model.events.BackupEvent;
import com.thinkparity.ophelia.model.events.BackupListener;
import com.thinkparity.ophelia.model.events.ContactAdapter;
import com.thinkparity.ophelia.model.events.ContactEvent;
import com.thinkparity.ophelia.model.events.ContactListener;
import com.thinkparity.ophelia.model.events.ContainerAdapter;
import com.thinkparity.ophelia.model.events.ContainerEvent;
import com.thinkparity.ophelia.model.events.ContainerListener;
import com.thinkparity.ophelia.model.events.ProfileAdapter;
import com.thinkparity.ophelia.model.events.ProfileEvent;
import com.thinkparity.ophelia.model.events.ProfileListener;
import com.thinkparity.ophelia.model.events.SessionAdapter;
import com.thinkparity.ophelia.model.events.SessionListener;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.session.SessionModel;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.MainStatusAvatar;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher;

/**
 * <b>Title:</b>thinkParity OpheliaUI Main Status Event Dispatcher<br>
 * <b>Description:</b>The status event dispatcher monitors changes to the
 * user's profile as well as the backup, if the backup feature is enabled.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class MainStatusDispatcher implements
        EventDispatcher<MainStatusAvatar> {

    /** A <code>BackupListener</code>. */
    private BackupListener backupListener;

    /** An instance of <code>BackupModel</code>. */
    private final BackupModel backupModel;

    /** A <code>ContactListener</code>. */
    private ContactListener contactListener;

    /** An instance of <code>ContactModel</code>. */
    private final ContactModel contactModel;

    /** A <code>ContainerListener</code>. */
    private ContainerListener containerListener;

    /** An instance of <code>ContainerModel</code>. */
    private final ContainerModel containerModel;

    /** A <code>ProfileListener</code>. */
    private ProfileListener profileListener;

    /** An instance of <code>ProfileModel</code>. */
    private final ProfileModel profileModel;

    /** A <code>SessionListener</code>. */
    private SessionListener sessionListener;

    /** An instance of <code>SessionModel</code>. */
    private final SessionModel sessionModel;

    /**
     * Create MainStatusDispatcher.
     * 
     * @param backupModel
     *            An instance of <code>BackupModel</code>.
     * @param contactModel
     *            An instance of <code>ContactModel</code>.
     * @param containerModel
     *            An instance of <code>ContainerModel</code>.
     * @param profileModel
     *            An instance of <code>ProfileModel</code>.
     */
    public MainStatusDispatcher(final BackupModel backupModel,
            final ContactModel contactModel,
            final ContainerModel containerModel,
            final ProfileModel profileModel, final SessionModel sessionModel) {
        super();
        this.backupModel = backupModel;
        this.contactModel = contactModel;
        this.containerModel = containerModel;
        this.profileModel = profileModel;
        this.sessionModel = sessionModel;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher#addListeners(com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar)
     *
     */
    public void addListeners(final MainStatusAvatar avatar) {
        if (profileModel.isBackupEnabled()) {
            backupListener = new BackupAdapter() {
                @Override
                public void statisticsUpdated(final BackupEvent e) {
                    avatar.fireBackupEvent(e);
                }
            };
            backupModel.addListener(backupListener);
        }
        profileListener = new ProfileAdapter() {
            @Override
            public void profileUpdated(final ProfileEvent e) {
                avatar.fireProfileEvent(e);
                removeListeners(avatar);
                addListeners(avatar);
            }
        };
        profileModel.addListener(profileListener);
        contactListener = new ContactAdapter() {
            @Override
            public void contactCreated(ContactEvent e) {
                avatar.fireContactEvent(e);
            }
            @Override
            public void incomingEMailInvitationAccepted(ContactEvent e) {
                avatar.fireContactEvent(e);
            }
            @Override
            public void incomingEMailInvitationCreated(ContactEvent e) {
                avatar.fireContactEvent(e);
            }
            @Override
            public void incomingEMailInvitationDeclined(ContactEvent e) {
                avatar.fireContactEvent(e);
            }
            @Override
            public void incomingEMailInvitationDeleted(ContactEvent e) {
                avatar.fireContactEvent(e);
            }
            @Override
            public void incomingUserInvitationAccepted(ContactEvent e) {
                avatar.fireContactEvent(e);
            }
            @Override
            public void incomingUserInvitationCreated(ContactEvent e) {
                avatar.fireContactEvent(e);
            }
            @Override
            public void incomingUserInvitationDeclined(ContactEvent e) {
                avatar.fireContactEvent(e);
            }
            @Override
            public void incomingUserInvitationDeleted(final ContactEvent e) {
                avatar.fireContactEvent(e);
            }
        };
        contactModel.addListener(contactListener);
        containerListener = new ContainerAdapter() {
            @Override
            public void containerFlagged(final ContainerEvent e) {
                avatar.fireContainerEvent(e);
            }
            @Override
            public void containerReceived(final ContainerEvent e) {
                avatar.fireContainerEvent(e);
            }
        };
        containerModel.addListener(containerListener);
        sessionListener = new SessionAdapter() {
            @Override
            public void sessionEstablished() {
                avatar.fireSessionEvent();
            }
            @Override
            public void sessionTerminated() {
                avatar.fireSessionEvent();
            }
            @Override
            public void sessionTerminated(final Throwable cause) {
                sessionTerminated();
            }
        };
        sessionModel.addListener(sessionListener);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher#removeListeners(com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar)
     * 
     */
    public void removeListeners(final MainStatusAvatar avatar) {
        if (profileModel.isBackupEnabled()) {
            backupModel.removeListener(backupListener);
            backupListener = null;
        }
        profileModel.removeListener(profileListener);
        profileListener = null;
        contactModel.removeListener(contactListener);
        contactListener = null;
        containerModel.removeListener(containerListener);
        containerListener = null;
        sessionModel.removeListener(sessionListener);
        sessionListener = null;
    }
}
