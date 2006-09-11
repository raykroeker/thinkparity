/*
 * Generated On: Jun 27 06 12:13:12 PM
 */
package com.thinkparity.codebase.model.container;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactType;

/**
 * <b>Title:</b>thinkParity Container<br>
 * <b>Description:</b>
 * 
 * @author CreateModel.groovy; raymond@thinkparity.com
 * @version 1.1.2.3
 */
public class Container extends Artifact {

    /** A flag indicating whether or not there exists a draft. */
    private Boolean draft;

    /** A flag indicating whether or not the draft is local. */
    private Boolean localDraft;

    /** Create Container. */
	public Container() { super(); }

    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#getType()
     * 
     */
    public ArtifactType getType() { return ArtifactType.CONTAINER; }

    /**
     * Determine whether or not there exists a draft.
     * 
     * @return True if there exists a draft; false otherwise.
     */
    public Boolean isDraft() { return draft; }

    /**
     * Determine whether or not the draft is local.
     * 
     * @return True if the draft is local; false otherwise.
     */
    public Boolean isLocalDraft() {
        return localDraft;
    }

    /**
     * Set the draft flag.
     * 
     * @param draft
     *            The draft flag.
     */
    public void setDraft(final Boolean draft) {
        this.draft = draft;
    }

    /**
     * Set the draft local flag.
     * 
     * @param localDraft
     *            The local draft flag.
     */
    public void setLocalDraft(final Boolean localDraft) {
        this.localDraft = localDraft;
    }
}
