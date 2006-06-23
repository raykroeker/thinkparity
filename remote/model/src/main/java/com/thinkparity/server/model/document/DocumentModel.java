/*
 * Apr 5, 2006
 */
package com.thinkparity.server.model.document;

import java.util.Set;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.model.AbstractModel;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.session.Session;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentModel extends AbstractModel {

    /**
     * Create a parity document interface.
     * 
     * @param session
     *            The user session.
     * @return The parity document interface.
     */
    public static DocumentModel getModel(final Session session) {
        return new DocumentModel(session);
    }

    /**
     * The implementation.
     * 
     */
    private final DocumentModelImpl impl;

    /**
     * The implementation synchronization lock.
     * 
     */
    private final Object implLock;

    /**
     * Create a DocumentModel.
     * 
     */
    private DocumentModel(final Session session) {
        super();
        this.impl = new DocumentModelImpl(session);
        this.implLock = new Object();
    }

    /**
     * Send a document version to a list of ids.
     * 
     * @param sendTo
     *            The jabber ids to send to.
     * @param uniqueId
     *            The document unique id.
     * @param versionId
     *            The document version id.
     * @param name
     *            The document name.
     * @param content
     *            The document content.
     * @throws ParityServerModelException
     */
    public void sendVersion(final Set<JabberId> sendTo, final UUID uniqueId,
            final Long versionId, final String name, final byte[] content)
            throws ParityServerModelException {
        synchronized(implLock) {
            impl.sendVersion(sendTo, uniqueId, versionId, name, content);
        }
    }
}
