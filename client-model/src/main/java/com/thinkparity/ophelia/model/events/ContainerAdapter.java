/*
 * Created On: Jun 29, 2006 8:58:22 AM
 * $Id$
 */
package com.thinkparity.ophelia.model.events;

import com.thinkparity.ophelia.model.container.ContainerModel;

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
     * @see com.thinkparity.ophelia.model.events.ContainerListener#containerBookmarkAdded(com.thinkparity.ophelia.model.events.ContainerEvent)
     * 
     */
    public void containerBookmarkAdded(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#containerBookmarkRemoved(com.thinkparity.ophelia.model.events.ContainerEvent)
     * 
     */
    public void containerBookmarkRemoved(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#containerCreated(com.thinkparity.ophelia.model.events.ContainerEvent)
     * 
     */
    public void containerCreated(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#containerDeleted(com.thinkparity.ophelia.model.events.ContainerEvent)
     * 
     */
    public void containerDeleted(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#containerLatestFlagAdded(com.thinkparity.ophelia.model.events.ContainerEvent)
     * 
     */
    public void containerFlagLatestAdded(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#containerLatestFlagRemoved(com.thinkparity.ophelia.model.events.ContainerEvent)
     * 
     */
    public void containerFlagLatestRemoved(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#containerSeenFlagAdded(com.thinkparity.ophelia.model.events.ContainerEvent)
     * 
     */
    public void containerFlagSeenAdded(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#containerSeenFlagRemoved(com.thinkparity.ophelia.model.events.ContainerEvent)
     * 
     */
    public void containerFlagSeenRemoved(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#containerPublished(com.thinkparity.ophelia.model.events.ContainerEvent)
     * 
     */
    public void containerPublished(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#containerReceived(com.thinkparity.ophelia.model.events.ContainerEvent)
     * 
     */
    public void containerReceived(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#containerRenamed(com.thinkparity.ophelia.model.events.ContainerEvent)
     * 
     */
    public void containerRenamed(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#containerUpdated(com.thinkparity.ophelia.model.events.ContainerEvent)
     * 
     */
    public void containerUpdated(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#containerVersionFlagSeenAdded(com.thinkparity.ophelia.model.events.ContainerEvent)
     *
     */
    public void containerVersionFlagSeenApplied(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#containerVersionFlagSeenRemoved(com.thinkparity.ophelia.model.events.ContainerEvent)
     *
     */
    public void containerVersionFlagSeenRemoved(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#containerVersionPublished(com.thinkparity.ophelia.model.events.ContainerEvent)
     *
     */
    @Override
    public void containerVersionPublished(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#documentAdded(com.thinkparity.ophelia.model.events.ContainerEvent)
     * 
     */
    public void documentAdded(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#documentRemoved(com.thinkparity.ophelia.model.events.ContainerEvent)
     * 
     */
    public void documentRemoved(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#documentReverted(com.thinkparity.ophelia.model.events.ContainerEvent)
     * 
     */
    public void documentReverted(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#draftCreated(com.thinkparity.ophelia.model.events.ContainerEvent)
     * 
     */
    public void draftCreated(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#draftDeleted(com.thinkparity.ophelia.model.events.ContainerEvent)
     * 
     */
    public void draftDeleted(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#draftUpdated(com.thinkparity.ophelia.model.events.ContainerEvent)
     * 
     */
    public void draftUpdated(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#teamMemberAdded(com.thinkparity.ophelia.model.events.ContainerEvent)
     * 
     */
    public void teamMemberAdded(final ContainerEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContainerListener#teamMemberRemoved(com.thinkparity.ophelia.model.events.ContainerEvent)
     * 
     */
    public void teamMemberRemoved(final ContainerEvent e) {}
}
