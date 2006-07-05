/*
 * Created On: Jun 29, 2006 8:58:22 AM
 * $Id$
 */
package com.thinkparity.model.parity.api.events;

import com.thinkparity.model.parity.model.container.ContainerModel;

/**
 * <b>Title:</b>thinkParity Container Adapter<br>
 * <b>Description:</b>The container adapter is a convenience class which
 * implements the container listener interface. To use this class; simply extend
 * it and override the required apis.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 * @see ContainerListener
 * @see ContainerEvent
 * @see ContainerModel#addListener
 */
public class ContainerAdapter implements ContainerListener {

    /**
     * @see com.thinkparity.model.parity.api.events.ContainerListener#containerClosed(com.thinkparity.model.parity.api.events.ContainerEvent)
     * 
     */
    public void containerClosed(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.model.parity.api.events.ContainerListener#containerCreated(com.thinkparity.model.parity.api.events.ContainerEvent)
     * 
     */
    public void containerCreated(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.model.parity.api.events.ContainerListener#containerDeleted(com.thinkparity.model.parity.api.events.ContainerEvent)
     * 
     */
    public void containerDeleted(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.model.parity.api.events.ContainerListener#containerReactivated(com.thinkparity.model.parity.api.events.ContainerEvent)
     * 
     */
    public void containerReactivated(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.model.parity.api.events.ContainerListener#documentAdded(com.thinkparity.model.parity.api.events.ContainerEvent)
     * 
     */
    public void documentAdded(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.model.parity.api.events.ContainerListener#documentRemoved(com.thinkparity.model.parity.api.events.ContainerEvent)
     * 
     */
    public void documentRemoved(final ContainerEvent e) {}
}
