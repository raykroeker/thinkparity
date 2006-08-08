/*
 * Created On: Apr 8, 2006
 * $Id$
 */
package com.thinkparity.server.handler.artifact;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.server.handler.IQAction;
import com.thinkparity.server.handler.IQHandler;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.artifact.ArtifactModel;
import com.thinkparity.server.model.session.Session;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ConfirmReceipt extends IQHandler {

    /**
     * Create a ConfirmReceipt.
     * @param action
     */
    public ConfirmReceipt() { super(IQAction.ARTIFACTCONFIRMRECEIPT); }

    /**
     * @see com.thinkparity.server.handler.IQHandler#handleIQ(org.xmpp.packet.IQ,
     *      com.thinkparity.server.model.session.Session)
     * 
     */
    public IQ handleIQ(final IQ iq, final Session session)
            throws ParityServerModelException, UnauthorizedException {
        logApiId();
        final ArtifactModel artifactModel = getArtifactModel(session);
        artifactModel.confirmReceipt(extractUniqueId(iq), extractVersionId(iq), extractJabberId(iq));
        return createResult(iq);
    }
}
