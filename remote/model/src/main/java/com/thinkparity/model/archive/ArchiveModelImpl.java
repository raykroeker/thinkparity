/*
 * Generated On: Sep 04 06 04:29:31 PM
 */
package com.thinkparity.model.archive;

import org.xmpp.packet.IQ;

import com.thinkparity.server.model.AbstractModelImpl;
import com.thinkparity.server.model.session.Session;

/**
 * <b>Title:</b>thinkParity Archive Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
class ArchiveModelImpl extends AbstractModelImpl {

    /** Create ArchiveModelImpl. */
    ArchiveModelImpl() {
        super();
    }

    /**
     * Backup an xmpp query.
     * 
     * @param query
     *            An xmpp query <code>IQ</code>.
     */
    void backup(final IQ query) {
        logApiId();
        logVariable("query", query);
        
    }

    /**
     * Create ArchiveModelImpl.
     *
     * @param session
     *      The user's session.
     */
    ArchiveModelImpl(final Session session) {
        super(session);
    }

    /**
     * Start the archive.  This involves starting all of the archive
     * databases.
     *
     */
    public void start() {
        logApiId();
    }

    /**
     * Stop the archive.  This involves stopping all of the archive
     * databases.
     *
     */
    public void stop() {
        logApiId();
    }
}
