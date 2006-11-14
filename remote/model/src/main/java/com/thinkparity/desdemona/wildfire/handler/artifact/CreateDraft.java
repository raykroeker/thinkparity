/*
 * Created On: Aug 1, 2006 5:54:22 PM
 */
package com.thinkparity.desdemona.wildfire.handler.artifact;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/** 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class CreateDraft extends AbstractHandler {

    /** Create Reactivate. */
    public CreateDraft() { super("artifact:createdraft"); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     * 
     */
    public void service() {
        logApiId();
        createDraft(readJabberId("userId"),
                readJabberIds("team", "teamMember"), readUUID("uniqueId"));
    }

    /**
     * Create a draft for an artifact.
     * 
     * @param uniqueId
     *            The artifact unique id.
     *            The artifact bytes.
     */
    private void createDraft(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId) {
        getArtifactModel().createDraft(userId, team, uniqueId);
    }
}
