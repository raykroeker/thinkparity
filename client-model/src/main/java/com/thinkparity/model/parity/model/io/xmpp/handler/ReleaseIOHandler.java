/*
 * Created On: Fri May 12 2006 11:32 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.io.xmpp.handler;

import java.util.LinkedList;
import java.util.List;

import com.thinkparity.model.parity.model.io.xmpp.XMPPException;
import com.thinkparity.model.parity.model.io.xmpp.XMPPSession;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;

/**
 * The implementation of the xmpp release io interface.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReleaseIOHandler extends AbstractIOHandler
    implements com.thinkparity.model.parity.model.io.handler.ReleaseIOHandler {

    /** Create ReleaseIOHandler. */
    public ReleaseIOHandler() { super(); }

    public Long create(final String artifactId, final String groupId,
            final String version, final List<Long> libraryIds)
            throws XMPPException {
        final XMPPSession session = openSession();
        try {
            session.setRemoteMethod("release:create");
            session.setParameter("artifactId", artifactId);
            session.setParameter("groupId", groupId);
            session.setLongParameters("libraryIds", "libraryId", libraryIds);
            session.setParameter("version", version);

            session.execute();
            if(session.containsResult()) {
                final Long releaseId = session.getLong("id");
                session.commit();
                return releaseId;
            }
            else { return null; }
        }
        catch(final Exception x) {
            session.rollback();
            throw new XMPPException(x);
        }
        finally { session.close(); }
    }

    public void delete(final Long releaseId) throws XMPPException {
        final XMPPSession session = openSession();
        try {
            session.setRemoteMethod("release:delete");
            session.setParameter("id", releaseId);
            session.execute();
        }
        catch(final Exception x) { throw new XMPPException(x); }
        finally { session.close(); }
    }

    public Release read(final Long releaseId) throws XMPPException {
        final XMPPSession session = openSession();
        try {
            session.setRemoteMethod("release:read");
            session.setParameter("id", releaseId);
            session.execute();

            if(session.containsResult()) { return extractRelease(session); }
            else { return null; }
        }
        catch(final Exception x) { throw new XMPPException(x); }
        finally { session.close(); }
    }

    public Release read(final String artifactId, final String groupId,
            final String version) throws XMPPException {
        final XMPPSession session = openSession();
        try {
            session.setRemoteMethod("release:read");
            session.setParameter("artifactId", artifactId);
            session.setParameter("groupId", groupId);
            session.setParameter("version", version);
            session.execute();

            if(session.containsResult()) { return extractRelease(session); }
            else { return null; }
        }
        catch(final Exception x) { throw new XMPPException(x); }
        finally { session.close(); }
    }

    /** @see com.thinkparity.browser.bootstrap.io.handler.ReleaseIOHandler#readLatest() */
    public Release readLatest(final String artifactId, final String groupId)
            throws XMPPException {
        final XMPPSession session = openSession();
        try {
            session.setRemoteMethod("release:readlatest");
            session.setParameter("artifactId", artifactId);
            session.setParameter("groupId", groupId);
            session.execute();

            if(session.containsResult()) { return extractRelease(session); }
            else { return null; }
        }
        catch(final Exception x) { throw new XMPPException(x); }
        finally { session.close(); }
    }

    /**
     * Read the libraries.
     *
     * @param releaseId
     * @return A list of libraries.
     */
     public List<Library> readLibraries(final Long releaseId) throws XMPPException {
        final XMPPSession session = openSession();
        try {
            session.setRemoteMethod("release:readlibraries");
            session.setParameter("id", releaseId);
            session.execute();

            if(session.containsResult()) { return session.getLibraries("libraries"); }
            else { return new LinkedList<Library>(); }
        }
        catch(final Exception x) { throw new XMPPException(x); }
        finally { session.close(); }
     }

    /**
     * Extract the release from the session.
     *
     * @param session
     *      The xmpp session.
     * @return A release.
     */
    Release extractRelease(final XMPPSession session) {
        final Release release = new Release();
        release.setArtifactId(session.getString("artifactId"));
        release.setCreatedOn(session.getCalendar("createdOn"));
        release.setGroupId(session.getString("groupId"));
        release.setId(session.getLong("id"));
        release.setVersion(session.getString("version"));
        return release;
    }
}
