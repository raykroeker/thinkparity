/*
 * Created On: Jun 29, 2006 8:58:17 AM
 * $Id$
 */
package com.thinkparity.ophelia.model.events;

import com.thinkparity.codebase.event.EventListener;

/**
 * <b>Title:</b>thinkParity Container Draft Listener<br>
 * <b>Description:</b>A container listener is an interface used to notify any
 * and all clients of the local model about changes in a container.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 * @see ContainerEvent
 */
public interface ContainerDraftListener extends EventListener {

    /**
     * A draft document's state has changed.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void stateChanged(final ContainerEvent e);
}
