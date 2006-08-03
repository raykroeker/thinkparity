/*
 * Created On: Aug 3, 2006 9:56:26 AM
 */
package com.thinkparity.model.parity.model.user;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public enum TeamMemberState {

    CONFIRMED(0), PENDING(1);
    
    /**
     * Obtain a state from its id.
     * 
     * @param id
     *            The state id.
     * @return The state.
     */
    public static TeamMemberState fromId(final Integer id) {
        switch(id) {
        case 0: return CONFIRMED;
        case 1: return PENDING;
        default:
            throw Assert.createUnreachable("[CONTAINER] [UNKNOWN ARTIFACT TEAM MEMBER STATE]");
        }
    }

    /**
     * The state id.
     * 
     */
    private Integer id;

    /**
     * Create ArtifactTeamMemberState.
     * 
     * @param id
     *            The state id.
     */
    private TeamMemberState(final Integer id) { this.id = id; }

    /**
     * Obtain the state id.
     * 
     * @return The state id.
     */
    public Integer getId() { return id; }
}
