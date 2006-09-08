/*
 * Apr 5, 2006
 */
package com.thinkparity.desdemona.model.document;

import java.util.Set;
import java.util.UUID;

import org.jivesoftware.messenger.auth.UnauthorizedException;

import com.thinkparity.codebase.jabber.JabberId;


import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.ParityErrorTranslator;
import com.thinkparity.desdemona.model.ParityServerModelException;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.util.xmpp.packet.document.IQSendDocument;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class DocumentModelImpl extends AbstractModelImpl {

    /**
     * Create a DocumentModelImpl.
     * 
     * @param session
     *            The user's session.
     */
    DocumentModelImpl(final Session session) {
        super(session);
    }

    void sendVersion(final Set<JabberId> sendTo, final UUID uniqueId,
            final Long versionId, final String name, final byte[] content)
            throws ParityServerModelException {
        logApiId();
        logger.debug(sendTo);
        logger.debug(uniqueId);
        logger.debug(versionId);
        logger.debug(name);
        logger.debug(content.length);
        try {
            final IQSendDocument iq = new IQSendDocument();
            iq.setContent(content);
            iq.setName(name);
            iq.setUniqueId(uniqueId);
            iq.setVersionId(versionId);
            for(final JabberId sendToId : sendTo) {
                setFrom(iq, session.getJabberId());
                setTo(iq, sendToId);

                send(sendToId, iq);
            }
        }
        catch(final UnauthorizedException ux) {
            logger.error("[RMODEL] [DOCUMENT] [SEND VERSION] [UN-AUTHORIZED]", ux);
            throw ParityErrorTranslator.translate(ux);
        }
    }
}
