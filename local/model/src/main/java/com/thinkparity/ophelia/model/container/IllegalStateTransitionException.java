/*
 * Created On:  Jul 6, 2007 10:57:47 AM
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.model.artifact.Artifact;

import com.thinkparity.ophelia.model.container.ContainerDraft.ArtifactState;

/**
 * <b>Title:</b>thinkParity Ophelia Model Illegal State Transition Exception<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class IllegalStateTransitionException extends Exception {

    /** The artifact. */
    private final Artifact artifact;

    /** The state to move from. */
    private final ArtifactState from;

    /** The attempted state to move to. */
    private final ArtifactState to;

    /**
     * Create IllegalStateTransitionException.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     * @param from
     *            An <code>ArtifactState</code>.
     * @param to
     *            An <code>ArtifactState</code>.
     */
    IllegalStateTransitionException(final Artifact artifact,
            final ArtifactState from, final ArtifactState to) {
        super();
        this.artifact = artifact;
        this.from = from;
        this.to = to;
    }

    /**
     * Obtain artifact.
     *
     * @return A Artifact.
     */
    public Artifact getArtifact() {
        return artifact;
    }

    /**
     * Obtain from.
     *
     * @return A ArtifactState.
     */
    public ArtifactState getFrom() {
        return from;
    }

    /**
     * Obtain to.
     *
     * @return A ArtifactState.
     */
    public ArtifactState getTo() {
        return to;
    }
}
