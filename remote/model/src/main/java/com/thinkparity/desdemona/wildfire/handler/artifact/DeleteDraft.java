/*
 * Created On: Aug 22, 2006 2:46:05 PM
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
public class DeleteDraft extends AbstractHandler {

    /** Create DeleteDraft. */
    public DeleteDraft() { super("artifact:deletedraft"); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        deleteDraft(readJabberId("userId"),
                readJabberIds("team", "teamMember"), readUUID("uniqueId"));
    }

    /**
     * Delete an artifact draft.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    private void deleteDraft(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId) {
        getArtifactModel().deleteDraft(userId, team, uniqueId);
    }
}
