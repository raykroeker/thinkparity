/*
 * Created On: Jun 29, 2006 8:58:17 AM
 * $Id$
 */
package com.thinkparity.model.parity.api.events;

import com.thinkparity.model.parity.model.container.ContainerModel;

/**
 * <b>Title:</b>thinkParity Container Listener<br>
 * <b>Description:</b>A container listener is an interface used to notify any
 * and all clients of the local model about changes in a container.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 * @see ContainerAdapter
 * @see ContainerEvent
 * @see ContainerModel#addListener
 */
public interface ContainerListener {

    /**
     * A container was closed.
     * 
     * @param e
     *            The container event.
     */
    public void containerClosed(final ContainerEvent e);

    /**
     * A container was created.
     * 
     * @param e
     *            The container event.
     */
    public void containerCreated(final ContainerEvent e);

    /**
     * A container was deleted.
     * 
     * @param e
     *            The container event.
     */
    public void containerDeleted(final ContainerEvent e);

    /**
     * A container was reactivated.
     * 
     * @param e
     *            The container event.
     */
    public void containerReactivated(final ContainerEvent e);

    /**
     * A document was added to a container.
     * 
     * @param e
     *            The container event.
     */
    public void documentAdded(final ContainerEvent e);

    /**
     * A document was removed from a container.
     * 
     * @param e
     *            The container event.
     */
    public void documentRemoved(final ContainerEvent e);

    /**
     * A container draft was created.
     * 
     * @param e
     *            The container event.
     */
    public void draftCreated(final ContainerEvent e);

    /**
     * A team member was added to a container.
     * 
     * @param e
     *            The container event.
     */
    public void teamMemberAdded(final ContainerEvent e);

    /**
     * A team member was added to a container.
     * 
     * @param e
     *            The container event.
     */
    public void teamMemberRemoved(final ContainerEvent e);
}
