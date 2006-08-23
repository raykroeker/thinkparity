/*
 * Created On: Aug 22, 2006 2:46:05 PM
 */
package com.thinkparity.server.handler.artifact;

import java.util.UUID;

import com.thinkparity.server.handler.AbstractController;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DeleteDraft extends AbstractController {

    /** Create DeleteDraft. */
    public DeleteDraft() { super("artifact:deletedraft"); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     */
    @Override
    public void service() {
        logApiId();
        deleteDraft(readUUID("uniqueId"));
    }

    /**
     * Delete an artifact draft.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    private void deleteDraft(final UUID uniqueId) {
        getArtifactModel().deleteDraft(uniqueId);
    }
}
