/*
 * Created On: Aug 22, 2006 2:46:05 PM
 */
package com.thinkparity.wildfire.handler.artifact;

import java.util.UUID;

import com.thinkparity.wildfire.handler.AbstractHandler;

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
