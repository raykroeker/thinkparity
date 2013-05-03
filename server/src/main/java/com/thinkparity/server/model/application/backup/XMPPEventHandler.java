/*
 * Created On:  20-Apr-07 11:32:59 AM
 */
package com.thinkparity.desdemona.model.backup;

import java.util.HashMap;
import java.util.Map;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftCreatedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactReceivedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberAddedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberRemovedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;
import com.thinkparity.codebase.model.util.xmpp.event.container.PublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.container.PublishedNotificationEvent;
import com.thinkparity.codebase.model.util.xmpp.event.container.VersionPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.container.VersionPublishedNotificationEvent;

import com.thinkparity.ophelia.model.InternalModelFactory;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Backup Service Event Dispatcher<br>
 * <b>Description:</b>Translate the xmpp event generated by the model into
 * appropriate backup implmentation event calls.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class XMPPEventHandler {

    /**
     * A <code>Map</code> of xmpp event types to their
     * <code>XMPPEventListener</code>.
     */
    private final Map<Class<? extends XMPPEvent>, XMPPEventListener> listeners;

    /**
     * Create BackupServiceEventDispatcher.
     * 
     */
    XMPPEventHandler() {
        super();
        this.listeners = new HashMap<Class<? extends XMPPEvent>, XMPPEventListener>();
    }

    /**
     * Handle an xmpp event.
     * 
     * @param event
     *            An <code>XMPPEvent</code>.
     */
    public void handleEvent(final User user, final XMPPEvent event) {
        synchronized (this) {
            handleEventImpl(user, event);
        }
    }

    /**
     * Start the event dispatcher.
     *
     */
    void start(final InternalModelFactory modelFactory) {
        synchronized (this) {
            startImpl(modelFactory);
        }
    }

    /**
     * Stop the event dispatcher.
     *
     */
    void stop() {
        synchronized (this) {
            stopImpl();
        }
    }

    /**
     * Add an xmpp event listener.
     * 
     * @param eventClass
     *            The xmpp event <code>Class</code>.
     * @param listener
     *            An <codee>XMPPEventListener</code>.
     */
    private void addListener(final Class<? extends XMPPEvent> eventClass,
            final XMPPEventListener listener) {
        Assert.assertNotTrue(listeners.containsKey(eventClass),
                "XMPP event listener {0} already registered", eventClass
                .getSimpleName());
        listeners.put(eventClass, listener);
    }

    /**
     * Obtain a typed event listener for an xmpp event.
     * 
     * @param event
     *            An <code>XMPPEvent</code>>
     * @return An <code>XMPPEventListener</code>.
     */
    private XMPPEventListener getListener(final XMPPEvent event) {
        return (XMPPEventListener) listeners.get(event.getClass());
    }

    /**
     * The handle event implementation.
     * 
     * @param user
     *            A <code>User</code>.
     * @param event
     *            An <code>XMPPEvent</code>.
     */
    private void handleEventImpl(final User user, final XMPPEvent event) {
        getListener(event).handleEvent(user, event);
    }

    /**
     * The start event dispatcher implementation.
     * 
     */
    private void startImpl(final InternalModelFactory modelFactory) {
        // artifact events
        addListener(ArtifactDraftCreatedEvent.class, new XMPPEventListener() {
            public void handleEvent(final User user, final XMPPEvent event) {
                modelFactory.getArtifactModel().handleDraftCreated(
                        (ArtifactDraftCreatedEvent) event);
            }
        });
        addListener(ArtifactDraftDeletedEvent.class, new XMPPEventListener() {
            public void handleEvent(final User user, final XMPPEvent event) {
                modelFactory.getArtifactModel().handleDraftDeleted(
                        (ArtifactDraftDeletedEvent) event);
            }
        });
        addListener(ArtifactReceivedEvent.class, new XMPPEventListener() {
            public void handleEvent(final User user, final XMPPEvent event) {
                modelFactory.getArtifactModel().handleReceived(
                        (ArtifactReceivedEvent) event);
            }
        });
        addListener(ArtifactTeamMemberAddedEvent.class, new XMPPEventListener() {
            public void handleEvent(final User user, final XMPPEvent event) {
                modelFactory.getArtifactModel().handleTeamMemberAdded(
                        (ArtifactTeamMemberAddedEvent) event);
            }
        });
        addListener(ArtifactTeamMemberRemovedEvent.class, new XMPPEventListener() {
            public void handleEvent(final User user, final XMPPEvent event) {
                modelFactory.getArtifactModel().handleTeamMemberRemoved(
                        (ArtifactTeamMemberRemovedEvent) event);
            }
        });
        // container events
        addListener(PublishedEvent.class, new XMPPEventListener() {
            public void handleEvent(final User user, final XMPPEvent event) {
                modelFactory.getContainerModel().handleEvent(
                        (PublishedEvent) event);
            }
        });
        addListener(PublishedNotificationEvent.class, new XMPPEventListener() {
            public void handleEvent(final User user, final XMPPEvent event) {
                final PublishedNotificationEvent pne = (PublishedNotificationEvent) event;
                modelFactory.getContainerModel().handleEvent(pne);
            }
        });
        addListener(VersionPublishedEvent.class, new XMPPEventListener() {
            public void handleEvent(final User user, final XMPPEvent event) {
                modelFactory.getContainerModel().handleEvent(
                        (VersionPublishedEvent) event);
            }
        });
        addListener(VersionPublishedNotificationEvent.class, new XMPPEventListener() {
            public void handleEvent(final User user, final XMPPEvent event) {
                final VersionPublishedNotificationEvent vpne = (VersionPublishedNotificationEvent) event;
                modelFactory.getContainerModel().handleEvent(vpne);
            }
        });
    }

    /**
     * The stop implementation.
     *
     */
    private void stopImpl() {
        listeners.clear();
    }
}
