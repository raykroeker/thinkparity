/*
 * Created On: Aug 22, 2006 2:46:05 PM
 */
package com.thinkparity.desdemona.wildfire.handler.artifact;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DeleteDraft extends AbstractHandler {

    /**
     * Create DeleteDraft.
     *
     */
    public DeleteDraft() {
        super("artifact:deletedraft");
    }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider,
     *      com.thinkparity.desdemona.util.service.ServiceRequestReader,
     *      com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     * 
     */
    @Override
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        logger.logApiId();
        deleteDraft(provider, reader.readJabberId("userId"),
                reader.readJabberIds("team", "teamMember"),
                reader.readUUID("uniqueId"));
    }

    /**
     * Delete an artifact draft.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    private void deleteDraft(final ServiceModelProvider context,
            final JabberId userId, final List<JabberId> team,
            final UUID uniqueId) {
        context.getArtifactModel().deleteDraft(userId, team, uniqueId);
    }
}
