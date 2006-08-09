/*
 * Generated On: Jun 27 06 12:13:12 PM
 */
package com.thinkparity.model.parity.model.container;

import com.thinkparity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.artifact.Artifact;

/**
 * <b>Title:</b>thinkParity Container<br>
 * <b>Description:</b>
 * 
 * @author CreateModel.groovy; raymond@thinkparity.com
 * @version 1.1.2.3
 */
public class Container extends Artifact {

    /** A flag indicating whether or not there exists a local draft. */
    private Boolean localDraft;

    /** Create Container. */
	public Container() { super(); }

    /**
     * @see com.thinkparity.model.parity.model.artifact.Artifact#getType()
     * 
     */
    public ArtifactType getType() { return ArtifactType.CONTAINER; }

    /**
     * @see #isSetLocalDraft()
     */
    public Boolean isSetDraft() { return isSetLocalDraft(); }

    /**
     * Determine if there exists a local draft.
     * 
     * @return True if there exists a local draft; false otherwise.
     */
    public Boolean isSetLocalDraft() { return localDraft; }

    /**
     * Set the local draft flag.
     * 
     * @param localDraft
     *            The local draft flag.
     */
    public void setLocalDraft(final Boolean localDraft) {
        this.localDraft = localDraft;
    }
}
