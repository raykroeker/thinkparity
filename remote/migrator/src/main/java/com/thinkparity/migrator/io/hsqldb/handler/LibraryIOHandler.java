/*
 * May 9, 2006 12:27:36 PM
 * $Id$
 */
package com.thinkparity.migrator.io.hsqldb.handler;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.LibraryBytes;
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
        .append("LIBRARY_VERSION,LIBRARY_PATH,CREATED_ON) ")
        .append("values (?,?,?,?,?,NOW())")
        .toString();

    /** Sql to create a library's bytes. */
    private static final String SQL_CREATE_BYTES =
        new StringBuffer("insert into LIBRARY_BYTES")
        .append("(LIBRARY_ID,LIBRARY_BYTES,LIBRARY_BYTES_CHECKSUM) ")
        .append("values (?,?,?)")
        .toString();

    /** Sql to delete a library. */
    private static final String SQL_DELETE =
            new StringBuffer("delete from LIBRARY ")
            .append("where LIBRARY_ID=?")
            .toString();

    /** Sql to delete a library's bytes. */
    private static final String SQL_DELETE_BYTES =
            new StringBuffer("delete from LIBRARY_BYTES ")
            .append("where LIBRARY_ID=?")
            .toString();

    /** Sql to read a library. */
    private static final String SQL_READ =
        new StringBuffer("select L.LIBRARY_ID,L.LIBRARY_TYPE_ID,")
        .append("L.LIBRARY_GROUP_ID,L.LIBRARY_ARTIFACT_ID,L.LIBRARY_VERSION,")
        .append("L.LIBRARY_PATH,L.CREATED_ON ")
        .append("from LIBRARY L ")
        .append("where L.LIBRARY_ID=?")
        .toString();

    private static final String SQL_READ_BY_ARTIFACT_ID_GROUP_ID_TYPE_VERSION =
        new StringBuffer("select L.LIBRARY_ID,L.LIBRARY_TYPE_ID,")
        .append("L.LIBRARY_GROUP_ID,L.LIBRARY_ARTIFACT_ID,L.LIBRARY_VERSION,")
        .append("L.LIBRARY_PATH,L.CREATED_ON ")
        .append("from LIBRARY L ")
        .append("where L.LIBRARY_ARTIFACT_ID=? ")
        .append("and L.LIBRARY_GROUP_ID=? ")
        .append("and L.LIBRARY_TYPE_ID=? ")
        .append("and L.LIBRARY_VERSION=?")
        .toString();

    /** Sel to read a library's bytes. */
    private static final String SQL_READ_BYTES =
        new StringBuffer("select LB.LIBRARY_ID,LB.LIBRARY_BYTES,")
        .append("LB.LIBRARY_BYTES_CHECKSUM ")
        .append("from LIBRARY_BYTES LB ")
        .append("where LB.LIBRARY_ID=?")
        .toString();

    /** Create LibraryIOHandler. */
    public LibraryIOHandler() { super(); }

    /** @see com.thinkparity.migrator.io.handler.LibraryIOHandler#create(java.lang.String, java.lang.String, java.lang.String, com.thinkparity.migrator.Library.Type, java.lang.String) */
    public Long create(final String artifactId, final String groupId,
            final String path, final Library.Type type, final String version)
            throws HypersonicException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE);
            session.setTypeAsInteger(1, type);
            session.setString(2, groupId);
            session.setString(3, artifactId);
            session.setString(4, version);
            session.setString(5, path);
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
    public void createBytes(final Long libraryId, final byte[] bytes,
            final String checksum) throws HypersonicException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_BYTES);
            session.setLong(1, libraryId);
            session.setBytes(2, bytes);
            session.setString(3, checksum);
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
     * Delete a library.
     *
     * @param libraryId
     *      A library id.
     */
    public void delete(final Long libraryId) throws HypersonicException {
        final HypersonicSession session = openSession();
        try {
            deleteBytes(session, libraryId);
            session.prepareStatement(SQL_DELETE);
            session.setLong(1, libraryId);
            if(1 != session.executeUpdate())
                throw new HypersonicException(
                        "[RMIGRATOR] [IO] [HYPERSONIC HANDLER] [LIBRARY] [DELETE] [COULD NOT EXECUTE UPDATE]");

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
    public LibraryBytes readBytes(final Long libraryId) throws HypersonicException {
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
        library.setCreatedOn(session.getCalendar("CREATED_ON"));
        library.setGroupId(session.getString("LIBRARY_GROUP_ID"));
        library.setId(session.getLong("LIBRARY_ID"));
        library.setPath(session.getString("LIBRARY_PATH"));
        library.setType(session.getLibraryTypeFromInteger("LIBRARY_TYPE_ID"));
        library.setVersion(session.getString("LIBRARY_VERSION"));
        return library;
    }

    LibraryBytes extractLibraryBytes(final HypersonicSession session) {
        final LibraryBytes libraryBytes = new LibraryBytes();
        libraryBytes.setBytes(session.getBytes("LIBRARY_BYTES"));
        libraryBytes.setChecksum(session.getString("LIBRARY_BYTES_CHECKSUM"));
        libraryBytes.setLibraryId(session.getLong("LIBRARY_ID"));
        return libraryBytes;
    }

    private void deleteBytes(final HypersonicSession session,
            final Long libraryId) throws HypersonicException {
        session.prepareStatement(SQL_DELETE_BYTES);
        session.setLong(1, libraryId);
        if(1 != session.executeUpdate())
            throw new HypersonicException(
                    "[RMIGRATOR] [IO] [HYPERSONIC HANDLER] [LIBRARY] [DELETE BYTES] [COULD NOT EXECUTE UPDATE]");
    }
}
