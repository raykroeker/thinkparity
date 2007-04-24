/*
 * Created On:  9-Nov-06 10:00:49 AM
 */
package com.thinkparity.desdemona.model.stream;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.desdemona.model.session.Session;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class InternalStreamModel extends StreamModel {

    /**
     * Create InternalStreamModel.
     *
     * @param session
     */
    InternalStreamModel(final Context context) {
        super();
    }

    /**
     * Create InternalStreamModel.
     *
     * @param session
     */
    InternalStreamModel(final Context context, final Session session) {
        super(session);
    }

    /**
     * Create a stream session for an achive user.
     * 
     * @param archiveId
     *            An archive user id <code>JabberId</code>.
     * @param archiveSession
     *            An archive user's <code>Session</code>.
     * @return A stream session.
     */
    public StreamSession createArchiveSession(final JabberId archiveId) {
        synchronized (getImplLock()) {
            return getImpl().createArchiveSession(archiveId);
        }
    }
}
