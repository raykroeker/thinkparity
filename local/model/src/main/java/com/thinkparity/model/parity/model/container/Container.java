/*
 * Generated On: Jun 27 06 12:13:12 PM
 */
package com.thinkparity.model.parity.model.container;

import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.user.TeamMember;

/**
 * <b>Title:</b>thinkParity Container<br>
 * <b>Description:</b>
 * 
 * @author CreateModel.groovy; raymond@thinkparity.com
 * @version 1.1.2.3
 */
public class Container extends Artifact {

    /** The current draft owner. */
    private TeamMember draftOwner;

    /** A draft for the container. */
    private ContainerDraft localDraft;

    /** Create Container. */
	public Container() { super(); }

    /**
     * Obtain the draftOwner
     *
     * @return The TeamMember.
     */
    public TeamMember getDraftOwner() { return draftOwner; }

	/**
     * Obtain the draft
     *
     * @return The ContainerDraft.
     */
    public ContainerDraft getLocalDraft() { return localDraft; }

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
    public Boolean isSetLocalDraft() { return null != localDraft; }

    /**
     * Determine whether or not the draft owner is set.
     * 
     * @return True if the draft owner is set.
     */
    public Boolean isSetDraftOwner() { return null != draftOwner; }

    /**
     * Set draft.
     *
     * @param draft The ContainerDraft.
     */
    public void setDraft(final ContainerDraft localDraft) {
        this.localDraft = localDraft;
    }

    /**
     * Set draftOwner.
     *
     * @param draftOwner The TeamMember.
     */
    public void setDraftOwner(final TeamMember draftOwner) {
        this.draftOwner = draftOwner;
    }

    /**
     * Set localDraft.
     *
     * @param localDraft The ContainerDraft.
     */
    public void setLocalDraft(ContainerDraft localDraft) {
        this.localDraft = localDraft;
    }
}
