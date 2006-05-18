/*
 * May 9, 2006 12:27:36 PM
 * $Id$
 */
package com.thinkparity.migrator.io.hsqldb.handler;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.io.hsqldb.HypersonicException;
import com.thinkparity.migrator.io.hsqldb.HypersonicSession;

/**
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class LibraryIOHandler extends AbstractIOHandler implements
        com.thinkparity.migrator.io.handler.LibraryIOHandler {

    /** Sql to create a library. */
    private static final String SQL_CREATE =
        new StringBuffer("insert into LIBRARY")
        .append("(LIBRARY_TYPE_ID,LIBRARY_GROUP_ID,LIBRARY_ARTIFACT_ID,")
        .append("LIBRARY_VERSION) ")
        .append("values (?,?,?,?)")
        .toString();

    /** Sql to create a library's bytes. */
    private static final String SQL_CREATE_BYTES =
        new StringBuffer("insert into LIBRARY_BYTES")
        .append("(LIBRARY_ID,LIBRARY_BYTES) ")
        .append("values (?,?)")
        .toString();

    /** Sql to read a library. */
    private static final String SQL_READ =
        new StringBuffer("select L.LIBRARY_ID,L.LIBRARY_TYPE_ID,")
        .append("L.LIBRARY_GROUP_ID,L.LIBRARY_ARTIFACT_ID,L.LIBRARY_VERSION ")
        .append("from LIBRARY L ")
        .append("where L.LIBRARY_ID=?")
        .toString();

    private static final String SQL_READ_BY_ARTIFACT_ID_GROUP_ID_TYPE_VERSION =
        new StringBuffer("select L.LIBRARY_ID,L.LIBRARY_TYPE_ID,")
        .append("L.LIBRARY_GROUP_ID,L.LIBRARY_ARTIFACT_ID,L.LIBRARY_VERSION ")
        .append("from LIBRARY L ")
        .append("where L.LIBRARY_ARTIFACT_ID=? ")
        .append("and L.LIBRARY_GROUP_ID=? ")
        .append("and L.LIBRARY_TYPE_ID=? ")
        .append("and L.LIBRARY_VERSION=?")
        .toString();

    /** Sel to read a library's bytes. */
    private static final String SQL_READ_BYTES =
        new StringBuffer("select LB.LIBRARY_BYTES ")
        .append("from LIBRARY_BYTES LB ")
        .append("where LB.LIBRARY_ID=?")
        .toString();

    /** Create LibraryIOHandler. */
    public LibraryIOHandler() { super(); }

    /**
     * @see com.thinkparity.migrator.io.handler.LibraryIOHandler#create(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public Long create(final String artifactId, final String groupId,
            final Library.Type type, final String version)
            throws HypersonicException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE);
            session.setTypeAsInteger(1, type);
            session.setString(2, groupId);
            session.setString(3, artifactId);
            session.setString(4, version);
            if(1 != session.executeUpdate())
                throw new HypersonicException(
                        "[RMIGRATOR] [IO] [HYPERSONIC HANDLER] [LIBRARY] [CREATE] [COULD NOT EXECUTE UPDATE]");

            session.commit();
            return session.getIdentity();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /** @see com.thinkparity.migrator.io.handler.LibraryIOHandler#createBytes(java.lang.Long, java.lang.Byte[]) */
    public void createBytes(final Long libraryId, final Byte[] bytes)
            throws HypersonicException {
        final HypersonicSession session = openSession();
        try {
            final byte[] smallBytes = new byte[bytes.length];
            for(int i = 0; i < bytes.length; i++) { smallBytes[i] = bytes[i]; }

            session.prepareStatement(SQL_CREATE_BYTES);
            session.setLong(1, libraryId);
            session.setBytes(2, smallBytes);
            if(1 != session.executeUpdate())
                throw new HypersonicException(
                        "[RMIGRATOR] [IO] [HYPERSONIC HANDLER] [LIBRARY] [CREATE BYTES] [COULD NOT EXECUTE UPDATE]");

            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }


    /**
     * @see com.thinkparity.migrator.io.handler.LibraryIOHandler#read(java.lang.Long)
     */
    public Library read(final Long libraryId) throws HypersonicException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ);
            session.setLong(1, libraryId);
            session.executeQuery();

            if(session.nextResult()) { return extractLibrary(session); }
            else { return null; }
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /** @see com.thinkparity.migrator.io.handler.LibraryIOHandler#read(java.lang.String, java.lang.String, com.thinkparity.migrator.Library.Type, java.lang.String) */
    public Library read(final String artifactId, final String groupId,
            final Library.Type type, final String version)
            throws HypersonicException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_BY_ARTIFACT_ID_GROUP_ID_TYPE_VERSION);
            session.setString(1, artifactId);
            session.setString(2, groupId);
            session.setTypeAsInteger(3, type);
            session.setString(4, version);
            session.executeQuery();

            if(session.nextResult()) { return extractLibrary(session); }
            else { return null; }
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /** @see com.thinkparity.migrator.io.handler.LibraryIOHandler#readBytes(java.lang.Long) */
    public Byte[] readBytes(final Long libraryId) throws HypersonicException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_BYTES);
            session.setLong(1, libraryId);
            session.executeQuery();

            if(session.nextResult()) { return extractLibraryBytes(session); }
            else { return null; }
        }
        catch(final HypersonicException hx) {
            throw hx;
        }
        finally { session.close(); }
    }

    Library extractLibrary(final HypersonicSession session) {
        final Library library = new Library();
        library.setArtifactId(session.getString("LIBRARY_ARTIFACT_ID"));
        library.setGroupId(session.getString("LIBRARY_GROUP_ID"));
        library.setId(session.getLong("LIBRARY_ID"));
        library.setType(session.getLibraryTypeFromInteger("LIBRARY_TYPE_ID"));
        library.setVersion(session.getString("LIBRARY_VERSION"));
        return library;
    }

    Byte[] extractLibraryBytes(final HypersonicSession session) {
        final byte[] smallBytes = session.getBytes("LIBRARY_BYTES");
        final Byte[] bigBytes = new Byte[smallBytes.length];
        for(int i = 0; i < smallBytes.length; i++) { bigBytes[i] = smallBytes[i]; }
        return bigBytes;
    }
}
