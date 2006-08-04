/*
 * Generated On: Jun 27 06 12:13:12 PM
 */
package com.thinkparity.model.parity.model.container;

import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactType;

/**
 * <b>Title:</b>thinkParity Container<br>
 * <b>Description:</b>
 * 
 * @author CreateModel.groovy; raymond@thinkparity.com
 * @version 1.1.2.3
 */
public class Container extends Artifact {

    /** A draft for the container. */
    private ContainerDraft draft;

    /** Create Container. */
	public Container() { super(); }

	/**
     * Obtain the draft
     *
     * @return The ContainerDraft.
     */
    public ContainerDraft getDraft() { return draft; }

    /**
     * @see com.thinkparity.model.parity.model.artifact.Artifact#getType()
     * 
     */
    public ArtifactType getType() { return ArtifactType.CONTAINER; }

    /**
     * Determine if the local draft is set.
     * 
     * @return True if the local draft is set.
     */
    public Boolean isSetDraft() { return null != draft; }

    /**
     * Set draft.
     *
     * @param draft The ContainerDraft.
     */
    public void setDraft(final ContainerDraft draft) {
        this.draft = draft;
    }
}
