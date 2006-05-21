/*
 * Created On: Fri May 12 2006 11:32 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.io.xmpp.handler;

import com.thinkparity.model.parity.model.io.xmpp.XMPPException;
import com.thinkparity.model.parity.model.io.xmpp.XMPPSession;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Library.Type;

/**
 * The implementation of the xmpp library io interface.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class LibraryIOHandler extends AbstractIOHandler
    implements com.thinkparity.model.parity.model.io.handler.LibraryIOHandler {

    /** Create LibraryIOHandler. */
    public LibraryIOHandler() { super(); }

    public Long create(final String artifactId, final String groupId,
            final Library.Type type, final String version) throws XMPPException {
        final XMPPSession session = openSession();
        try {
            session.setRemoteMethod("library:create");
            session.setParameter("artifactId", artifactId);
            session.setParameter("groupId", groupId);
            session.setParameter("type", type);
            session.setParameter("version", version);

            session.execute();

            if(session.containsResult()) {
                session.commit();
                return session.getLong("id");
            }
            else {
                throw new XMPPException(
                        "[RMODEL] [LIBRARY] [XMPP IO] [UNABLE TO CREATE LIBRARY]");
            }
        }
        catch(final Exception x) {
            session.rollback();
            throw new XMPPException(x);
        }
        finally { session.close(); }
    }

    public void delete(final Long libraryId) throws XMPPException {
        final XMPPSession session = openSession();
        try {
            session.setRemoteMethod("library:delete");
            session.setParameter("id", libraryId);
            session.execute();

        }
        catch(final Exception x) {
            session.rollback();
            throw  new XMPPException(x);
        }
        finally { session.close(); }
    }

    public void createBytes(final Long libraryId, final Byte[] bytes)
            throws XMPPException {
        final XMPPSession session = openSession();
        try {
            session.setRemoteMethod("library:createbytes");
            session.setParameter("id", libraryId);
            session.setParameter("bytes", bytes);

            session.execute();
            session.commit();
        }
        catch(final Exception x) {
            session.rollback();
            throw new XMPPException(x);
        }
        finally { session.close(); }
    }

    public Library read(final Long libraryId) throws XMPPException {
        final XMPPSession session = openSession();
        try {
            session.setRemoteMethod("library:read");
            session.setParameter("id", libraryId);
            session.execute();

            if(session.containsResult()) { return extractLibrary(session); }
            else { return null; }
        }
        finally { session.close(); }
    }

    /** @see com.thinkparity.model.parity.model.io.handler.LibraryIOHandler#read(java.lang.String, java.lang.String, com.thinkparity.migrator.Library.Type, java.lang.String) */
    public Library read(final String artifactId, final String groupId,
            final Type type, final String version) {
        final XMPPSession session = openSession();
        try {
            session.setRemoteMethod("library:read");
            session.setParameter("artifactId", artifactId);
            session.setParameter("groupId", groupId);
            session.setParameter("type", type);
            session.setParameter("version", version);
            session.execute();

            if(session.containsResult()) { return extractLibrary(session); }
            else { return null; }
        }
        catch(final Exception x) { throw new XMPPException(x); }
        finally { session.close(); }
    }

    /** @see com.thinkparity.browser.bootstrap.io.handler.LibraryHandler#readBytes(java.lang.Long) */
    public Byte[] readBytes(final Long libraryId) throws XMPPException {
        final XMPPSession session = openSession();
        try {
            session.setRemoteMethod("library:readbytes");
            session.setParameter("id", libraryId);
            session.execute();

            if(session.containsResult()) { return extractLibraryBytes(session); }
            else { return null; }
        }
        catch(final Exception x) { throw new XMPPException(x); }
        finally { session.close(); }
    }

    Library extractLibrary(final XMPPSession session) {
        final Library library = new Library();
        library.setArtifactId(session.getString("artifactId"));
        library.setGroupId(session.getString("groupId"));
        library.setId(session.getLong("id"));
        library.setType(session.getLibraryType("type"));
        library.setVersion(session.getString("version"));
        return library;
    }

    /**
     * Extract the library's bytes from the session.
     *
     * @param session
     *      The xmpp session.
     * @return The library byte array.
     */
    Byte[] extractLibraryBytes(final XMPPSession session) {
        return session.getBytes("bytes");
    }
}
