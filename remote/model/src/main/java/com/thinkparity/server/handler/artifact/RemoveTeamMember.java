/*
 * Created On: Aug 2, 2006 1:19:39 PM
 */
package com.thinkparity.server.handler.artifact;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.server.handler.AbstractController;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class RemoveTeamMember extends AbstractController {

    /** Create AddTeamMember. */
    public RemoveTeamMember() { super("artifact:removeteammember"); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     */
    @Override
    public void service() {
        throw Assert.createNotYetImplemented("AddTeamMember#service");
    }
}
