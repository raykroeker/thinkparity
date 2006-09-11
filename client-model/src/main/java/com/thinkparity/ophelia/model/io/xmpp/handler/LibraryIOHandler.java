/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.ophelia.model.io.xmpp.handler;


import com.thinkparity.codebase.model.migrator.Library;
import com.thinkparity.codebase.model.migrator.LibraryBytes;

import com.thinkparity.ophelia.model.io.xmpp.XMPPException;
import com.thinkparity.ophelia.model.io.xmpp.XMPPSession;

/**
 * The implementation of the xmpp library io interface.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class LibraryIOHandler extends AbstractIOHandler
    implements com.thinkparity.ophelia.model.io.handler.LibraryIOHandler {

    /** Create LibraryIOHandler. */
    public LibraryIOHandler() { super(); }

    public Long create(final String artifactId, final String groupId,
            final String path, final Library.Type type, final String version)
            throws XMPPException {
        final XMPPSession session = openAnonymousSession();
        try {
            session.setRemoteMethod("library:create");
            session.setParameter("artifactId", artifactId);
            session.setParameter("groupId", groupId);
            session.setParameter("path", path);
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

    public void createBytes(final Long libraryId, final byte[] bytes,
            final String checksum) throws XMPPException {
        final XMPPSession session = openAnonymousSession();
        try {
            session.setRemoteMethod("library:createbytes");
            session.setParameter("id", libraryId);
            session.setParameter("bytes", bytes);
            session.setParameter("checksum", checksum);

            session.execute();
            session.commit();
        }
        catch(final Exception x) {
            session.rollback();
            throw new XMPPException(x);
        }
        finally { session.close(); }
    }

    public void delete(final Long libraryId) throws XMPPException {
        final XMPPSession session = openAnonymousSession();
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

    public Library read(final Long libraryId) throws XMPPException {
        final XMPPSession session = openAnonymousSession();
        try {
            session.setRemoteMethod("library:read");
            session.setParameter("id", libraryId);
            session.execute();

            if(session.containsResult()) { return extractLibrary(session); }
            else { return null; }
        }
        finally { session.close(); }
    }

    /** @see com.thinkparity.ophelia.model.io.handler.LibraryIOHandler#read(java.lang.String, java.lang.String, com.thinkparity.codebase.model.migrator.Library.Type, java.lang.String) */
    public Library read(final String artifactId, final String groupId,
            final Library.Type type, final String version) {
        final XMPPSession session = openAnonymousSession();
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

    /** @see com.thinkparity.model.parity.model.io.handler.LibraryHandler#readBytes(java.lang.Long) */
    public LibraryBytes readBytes(final Long libraryId) throws XMPPException {
        final XMPPSession session = openAnonymousSession();
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

    /** @see com.thinkparity.model.parity.model.io.handler.LibraryHandler#readBytes(java.lang.Long) */
    public byte[] readSmallBytes(final Long libraryId) throws XMPPException {
        final XMPPSession session = openAnonymousSession();
        try {
            session.setRemoteMethod("library:readbytes");
            session.setParameter("id", libraryId);
            session.execute();

            if(session.containsResult()) { return extractSmallLibraryBytes(session); }
            else { return null; }
        }
        catch(final Exception x) { throw new XMPPException(x); }
        finally { session.close(); }
    }

    Library extractLibrary(final XMPPSession session) {
        final Library library = new Library();
        library.setArtifactId(session.getString("artifactId"));
        library.setCreatedOn(session.getCalendar("createdOn"));
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
    LibraryBytes extractLibraryBytes(final XMPPSession session) {
        final LibraryBytes libraryBytes = new LibraryBytes();
        libraryBytes.setBytes(session.getBytes("bytes"));
        libraryBytes.setChecksum(session.getString("checksum"));
        libraryBytes.setLibraryId(session.getLong("id"));
        return libraryBytes;
    }

    byte[] extractSmallLibraryBytes(final XMPPSession session) {
        return session.getSmallBytes("bytes");
    }
}
