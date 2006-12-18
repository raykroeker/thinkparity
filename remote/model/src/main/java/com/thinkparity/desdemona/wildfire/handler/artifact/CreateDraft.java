/*
 * Created On: Aug 1, 2006 5:54:22 PM
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
public final class CreateDraft extends AbstractHandler {

    /**
     * Create CreateDraft.
     *
     */
    public CreateDraft() {
        super("artifact:createdraft");
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
        createDraft(provider, reader.readJabberId("userId"),
                reader.readJabberIds("team", "teamMember"),
                reader.readUUID("uniqueId"));
    }

    /**
     * Create a draft for an artifact.
     * 
     * @param uniqueId
     *            The artifact unique id.
     *            The artifact bytes.
     */
    private void createDraft(final ServiceModelProvider context,
            final JabberId userId, final List<JabberId> team,
            final UUID uniqueId) {
        context.getArtifactModel().createDraft(userId, team, uniqueId);
    }
}
