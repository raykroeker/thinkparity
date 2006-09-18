/*
 * Created On: Apr 8, 2006
 * $Id$
 */
package com.thinkparity.desdemona.wildfire.handler.artifact;

import org.jivesoftware.wildfire.auth.UnauthorizedException;
import org.xmpp.packet.IQ;


import com.thinkparity.desdemona.model.ParityServerModelException;
import com.thinkparity.desdemona.model.artifact.ArtifactModel;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.wildfire.handler.IQAction;
import com.thinkparity.desdemona.wildfire.handler.IQHandler;

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
     * @see com.thinkparity.desdemona.wildfire.handler.IQHandler#handleIQ(org.xmpp.packet.IQ,
     *      com.thinkparity.desdemona.model.session.Session)
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
