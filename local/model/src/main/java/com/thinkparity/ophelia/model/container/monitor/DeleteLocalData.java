/*
 * Created On:  27-Aug-07 12:56:00 PM
 */
package com.thinkparity.ophelia.model.container.monitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.model.container.Container;

/**
 * <b>Title:</b>thinkParity Ophelia Model Container Monitor Publish Data<br>
 * <b>Description:</b>The data produced by the model and consumed by the UI
 * when displaying publish progress.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DeleteLocalData {

    /** A container to delete. */
    private Container deleteContainer;

    /** A list of containers to delete. */
    private final List<Container> deleteContainers;

    /**
     * Create DeleteLocalData.
     *
     */
    public DeleteLocalData() {
        super();
        this.deleteContainers = new ArrayList<Container>();
    }

    /**
     * Obtain the deleteContainer.
     *
     * @return A <code>Container</code>.
     */
    public Container getDeleteContainer() {
        return deleteContainer;
    }

    /**
     * Obtain the delete containers.
     *
     * @return A <code>List<Container></code>.
     */
    public List<Container> getDeleteContainers() {
        return Collections.unmodifiableList(deleteContainers);
    }

    /**
     * Set the delete container.
     *
     * @param deleteContainer
     *		A <code>Container</code>.
     */
    public void setDeleteContainer(final Container deleteContainer) {
        this.deleteContainer = deleteContainer;
    }

    /**
     * Set the delete containers.
     * 
     * @param deleteContainers
     *            A <code>List<Container></code>.
     */
    public void setDeleteContainers(final List<Container> deleteContainers) {
        this.deleteContainers.clear();
        this.deleteContainers.addAll(deleteContainers);
    }
}
