/*
 * Created On:  15-May-07 3:41:47 PM
 */
package com.thinkparity.ophelia.model.container.delegate;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactReceivedEvent;

import com.thinkparity.ophelia.model.container.ContainerDelegate;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HandleReceived extends ContainerDelegate {

    /** A <code>Container</code>. */
    private Container container;

    /** An <code>ArtifactReceivedEvent</code>. */
    private ArtifactReceivedEvent event;

    /**
     * Create HandleReceived.
     *
     */
    public HandleReceived() {
        super();
    }

    /**
     * Obtain container.
     *
     * @return A Container.
     */
    public Container getContainer() {
        return container;
    }

    /**
     * Run the handle received delegate.
     *
     */
    public void handleReceived() {
        final Long containerId = artifactIO.readId(event.getUniqueId());
        final User receivedBy = getUserModel().readLazyCreate(event.getReceivedBy());
        // update the user published to info
        final ArtifactReceipt receipt = containerIO.readPublishedToReceipt(
                containerId, event.getVersionId(), event.getPublishedOn(),
                receivedBy);
        if (null == receipt) {
            containerIO.createPublishedTo(containerId, event.getVersionId(),
                    receivedBy, event.getPublishedOn());
        }
        containerIO.updatePublishedTo(containerId, event.getVersionId(),
                event.getPublishedOn(), event.getReceivedBy(),
                event.getReceivedOn());
        container = read(containerId);
    }

    /**
     * Set event.
     *
     * @param event
     *		A ArtifactReceivedEvent.
     */
    public void setEvent(ArtifactReceivedEvent event) {
        this.event = event;
    }
}
