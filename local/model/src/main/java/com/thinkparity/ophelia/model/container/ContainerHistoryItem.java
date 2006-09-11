/*
 * Created On: Jul 6, 2006 8:58:01 PM
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.ophelia.model.audit.HistoryItem;

/**
 * <b>Title:</b>thinkParity Container History Item<br>
 * <b>Description:</b>A thinkParity container history implementation.
 * 
 * @author raymond@thinkparity.com
 * @see HistoryItem
 */
public class ContainerHistoryItem extends HistoryItem {

    /** A container id. */
    private Long containerId;

    /** A container version id. */
    private Long versionId;

    /** Create ContainerHistoryItem. */
    public ContainerHistoryItem() { super(); }

    /**
     * Obtain the container id.
     *
     * @return The Long.
     */
    public Long getContainerId() { return containerId; }

    /**
     * Obtain the versionId
     *
     * @return The Long.
     */
    public Long getVersionId() { return versionId; }

    /**
     * Determine if the version id is set.
     * 
     * @return True if the version id is set; false otherwise.
     */
    public Boolean isSetVersionId() { return null != versionId; }

    /**
     * Set the container id.
     * 
     * @param containerId
     *            The container id.
     */
    public void setContainerId(final Long containerId) {
        this.containerId = containerId;
    }

    /**
     * Set versionId.
     *
     * @param versionId The Long.
     */
    public void setVersionId(final Long versionId) {
        this.versionId = versionId;
    }
}
