/*
 * Created On: Aug 2, 2006 4:34:37 PM
 */
package com.thinkparity.model.parity.model.container;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public enum ContainerDraftArtifactState {

    ADDED(0), NONE(1), REMOVED(2), UPDATED(3);
    
    /**
     * Obtain a state from its id.
     * 
     * @param id
     *            The state id.
     * @return The state.
     */
    public static ContainerDraftArtifactState fromId(final Integer id) {
        switch(id) {
        case 0: return ADDED;
        case 1: return NONE;
        case 2: return REMOVED;
        case 3: return UPDATED;
        default:
            throw Assert.createUnreachable("[CONTAINER] [UNKNOWN DRAFT ARTIFACT STATE]");
        }
    }

    /**
     * The state id.
     * 
     */
    private Integer id;

    /**
     * Create ContainerDraftArtifactState.
     * 
     * @param id
     *            The state id.
     */
    private ContainerDraftArtifactState(final Integer id) { this.id = id; }

    /**
     * Obtain the state id.
     * 
     * @return The state id.
     */
    public Integer getId() { return id; }
}
