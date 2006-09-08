/*
 * Apr 5, 2006
 */
package com.thinkparity.desdemona.wildfire.handler.document;

import java.io.IOException;
import java.util.zip.DataFormatException;

import org.dom4j.Element;
import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.codebase.CompressionUtil;


import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.thinkparity.desdemona.model.ParityServerModelException;
import com.thinkparity.desdemona.model.document.DocumentModel;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.util.dom4j.ElementName;
import com.thinkparity.desdemona.wildfire.handler.IQAction;
import com.thinkparity.desdemona.wildfire.handler.IQHandler;

/**
 * Handle the send of a document to a set of users.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SendDocument extends IQHandler {

    /**
     * Create a SendDocument.
     * 
     */
    public SendDocument() { super(IQAction.DOCUMENTSEND); }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.IQHandler#handleIQ(org.xmpp.packet.IQ,
     *      com.thinkparity.desdemona.model.session.Session)
     * 
     */
    public IQ handleIQ(final IQ iq, final Session session)
            throws ParityServerModelException, UnauthorizedException {
        logApiId();
        final DocumentModel dModel = getDocumentModel(session);
        dModel.sendVersion(extractJabberIdSet(iq), extractUniqueId(iq),
                extractVersionId(iq), extractName(iq), extractBytes(iq));
        return createResult(iq);
    }

    /**
     * Extract bytes from the xmpp internet query.
     * 
     * @param iq
     *            The xmpp internet query.
     * @return The bytes.
     */
    private byte[] extractBytes(final IQ iq) {
        final Element e = iq.getChildElement();
        final Element bytes = getElement(e, ElementName.BYTES);
        final String bytesData = (String) bytes.getData();
        try { return CompressionUtil.decompress(Base64.decode(bytesData)); }
        catch(final DataFormatException dfx) { throw new RuntimeException(dfx); }
        catch(final IOException iox) { throw new RuntimeException(iox); }
    }
}
