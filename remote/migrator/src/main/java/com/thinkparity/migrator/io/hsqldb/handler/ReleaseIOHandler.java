/*
 * May 9, 2006 12:27:36 PM
 * $Id$
 */
package com.thinkparity.migrator.io.hsqldb.handler;

import java.util.LinkedList;
import java.util.List;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;
import com.thinkparity.migrator.io.hsqldb.HypersonicException;
import com.thinkparity.migrator.io.hsqldb.HypersonicSession;

/**
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class ReleaseIOHandler extends AbstractIOHandler implements
        com.thinkparity.migrator.io.handler.ReleaseIOHandler {

    /** Sql to count the library relationships. */
    private static final String SQL_COUNT_LIBRARIES =
            new StringBuffer("select COUNT(RLR.LIBRARY_ID) LIBRARY_COUNT ")
            .append("from RELEASE_LIBRARY_REL RLR ")
            .append("where RLR.RELEASE_ID=?")
            .toString();

    /** Sql to create a release. */
    private static final String SQL_CREATE =
        new StringBuffer("insert into RELEASE ")
        .append("(RELEASE_GROUP_ID,RELEASE_ARTIFACT_ID,RELEASE_VERSION,")
        .append("CREATED_ON) ")
        .append("values (?,?,?,NOW())")
        .toString();

    /** Sql to create a release library relationship. */
    private static final String SQL_CREATE_LIBRARY_REL =
        new StringBuffer("insert into RELEASE_LIBRARY_REL ")
        .append("(RELEASE_ID,LIBRARY_ID) ")
        .append("values (?,?)")
        .toString();

    /** Sql to delete a release. */
    private static final String SQL_DELETE =
        new StringBuffer("delete from RELEASE ")
        .append("where RELEASE_ID=?")
        .toString();

    /** Sql to delete all library relationships. */
    private static final String SQL_DELETE_LIBRARY_REL =
        new StringBuffer("delete from  RELEASE_LIBRARY_REL ")
        .append("where RELEASE_ID=?")
        .toString();

    /** Sql to read all releases. */
    private static final String SQL_READ_ALL =
        new StringBuffer("select R.RELEASE_ID,")
        .append("R.RELEASE_GROUP_ID,R.RELEASE_ARTIFACT_ID,R.RELEASE_VERSION,R.CREATED_ON ")
        .append("from RELEASE R ")
        .append("order by R.CREATED_ON asc")
        .toString();

    private static final String SQL_READ_BY_ARTIFACT_ID_GROUP_ID_VERSION =
        new StringBuffer("select R.RELEASE_ID,")
        .append("R.RELEASE_GROUP_ID,R.RELEASE_ARTIFACT_ID,R.RELEASE_VERSION,R.CREATED_ON ")
        .append("from RELEASE R ")
        .append("where R.RELEASE_ARTIFACT_ID=? ")
        .append("and R.RELEASE_GROUP_ID=? ")
        .append("and R.RELEASE_VERSION=?")
        .toString();

    /** Sql to read a release. */
    private static final String SQL_READ_BY_ID =
        new StringBuffer("select R.RELEASE_ID,")
        .append("R.RELEASE_GROUP_ID,R.RELEASE_ARTIFACT_ID,R.RELEASE_VERSION,R.CREATED_ON ")
        .append("from RELEASE R ")
        .append("where R.RELEASE_ID=?")
        .toString();

    /** Sql to read the latest release name. */
    private static final String SQL_READ_LATEST =
        new StringBuffer("select MAX(R.RELEASE_ID) RELEASE_ID ")
        .append("from RELEASE R ")
        .append("where R.RELEASE_ARTIFACT_ID=? ")
        .append("and R.RELEASE_GROUP_ID=?")
        .toString();

    /** Sql to read the libraries for a release. */
    private static final String SQL_READ_LIBRARY_REL =
        new StringBuffer("select L.LIBRARY_ARTIFACT_ID,L.LIBRARY_GROUP_ID,")
        .append("L.LIBRARY_ID,L.LIBRARY_TYPE_ID,L.LIBRARY_VERSION,")
        .append("L.LIBRARY_PATH,L.CREATED_ON ")
        .append("from LIBRARY L ")
        .append("inner join RELEASE_LIBRARY_REL RLR ")
        .append("on L.LIBRARY_ID = RLR.LIBRARY_ID ")
        .append("inner join RELEASE R ")
        .append("on R.RELEASE_ID = RLR.RELEASE_ID ")
        .append("where R.RELEASE_ID=?")
        .toString();

    /** The library io implementation. */
    private final LibraryIOHandler libraryIO;

    /** Create ReleaseIOHandler. */
    public ReleaseIOHandler() {
        super();
        this.libraryIO = new LibraryIOHandler();
    }

    /**
     * @see com.thinkparity.migrator.io.handler.ReleaseIOHandler#create(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public Long create(final String artifactId, final String groupId,
            final String version) throws HypersonicException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE);
            session.setString(1, groupId);
            session.setString(2, artifactId);
            session.setString(3, version);
            if(1 != session.executeUpdate())
                throw new HypersonicException(
                        "[RMIGRATOR] [IO] [HYPERSONIC HANDLER] [RELEASE] [CREATE] [COULD NOT EXECUTE UPDATE]");

            session.commit();
            return session.getIdentity();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.migrator.io.handler.ReleaseIOHandler#createLibraryRel(java.lang.Long, java.lang.Long)
     */
    public void createLibraryRel(final Long releaseId, final Long libraryId) throws HypersonicException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_LIBRARY_REL);
            session.setLong(1, releaseId);
            session.setLong(2, libraryId);
            if(1 != session.executeUpdate())
                throw new HypersonicException(
                        "[RMIGRATOR] [IO] [HYPERSONIC HANDLER] [RELEASE] [CREATE LIBRARY REL] [COULD NOT EXECUTE UPDATE]");

            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    public void delete(final Long releaseId) throws HypersonicException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE);
            session.setLong(1, releaseId);
            if(1 != session.executeUpdate())
                throw new HypersonicException(
                        "[RMIGRATOR] [IO] [HYPERSONIC HANDLER] [RELEASE] [DELETE] [COULD NOT EXECUTE UPDATE]");

            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    public void deleteLibraryRel(final Long releaseId)
            throws HypersonicException {
        final HypersonicSession session = openSession();
        try {
            final Integer libraryCount = countLibraries(session, releaseId);

            session.prepareStatement(SQL_DELETE_LIBRARY_REL);
            session.setLong(1, releaseId);
            if(libraryCount != session.executeUpdate())
                throw new HypersonicException(
                        "[RMIGRATOR] [IO] [HYPERSONIC HANDLER] [RELEASE] [DELETE LIBRARY REL] [COULD NOT EXECUTE UPDATE]");

            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.migrator.io.handler.ReleaseIOHandler#read(java.lang.Long)
     */
    public Release read(final Long releaseId) throws HypersonicException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_BY_ID);
            session.setLong(1, releaseId);
            session.executeQuery();

            if(session.nextResult()) { return extractRelease(session); }
            else { return null; }
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * Read a release.
     * 
     * @param artifactId
     *      An artifact id.
     * @param groupid
     *      A group id.
     * @param version.
     *      A version.
     * @return A release.
     * @throws HypersonicException
     */
    public Release read(final String artifactId, final String groupId,
            final String version) throws HypersonicException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_BY_ARTIFACT_ID_GROUP_ID_VERSION);
            session.setString(1, artifactId);
            session.setString(2, groupId);
            session.setString(3, version);
            session.executeQuery();

            if(session.nextResult()) { return extractRelease(session); }
            else { return null; }
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.migrator.io.handler.ReleaseIOHandler#readAll()
     * 
     */
    public List<Release> readAll() throws HypersonicException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_ALL);
            session.executeQuery();

            final List<Release> releases = new LinkedList<Release>();
            while(session.nextResult()) { releases.add(extractRelease(session)); }
            return releases;
        }
        catch(final HypersonicException hx) { throw hx; }
        finally { session.close(); }
    }

    /** @see com.thinkparity.migrator.io.handler.ReleaseIOHandler#readLatest() */
    public Release readLatest(final String artifactId, final String groupId)
            throws HypersonicException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_LATEST);
            session.setString(1, artifactId);
            session.setString(2, groupId);
            session.executeQuery();

            if(session.nextResult()) {
                return read(session.getLong("RELEASE_ID"));
            }
            else { return null; }
        }
        catch(final HypersonicException hx) { throw hx; }
        finally { session.close(); }
    }

    /**
     * Read a list of libraries.
     *
     * @param releaseId
     *      A release id.
     * @return A list of libraries.
     */
    public List<Library> readLibraryRel(final Long releaseId)
            throws HypersonicException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_LIBRARY_REL);
            session.setLong(1, releaseId);
            session.executeQuery();

            final List<Library> libraries = new LinkedList<Library>();
            while(session.nextResult()) {
                libraries.add(libraryIO.extractLibrary(session));
            }
            return libraries;
        }
        catch(final HypersonicException hx) { throw hx; }
        finally { session.close(); }
    }

    Release extractRelease(final HypersonicSession session) {
        final Release release = new Release();
        release.setArtifactId(session.getString("RELEASE_ARTIFACT_ID"));
        release.setCreatedOn(session.getCalendar("CREATED_ON"));
        release.setGroupId(session.getString("RELEASE_GROUP_ID"));
        release.setId(session.getLong("RELEASE_ID"));
        release.setVersion(session.getString("RELEASE_VERSION"));
        return release;
    }

    private Integer countLibraries(final HypersonicSession session,
            final Long releaseId) throws HypersonicException {
        session.prepareStatement(SQL_COUNT_LIBRARIES);
        session.setLong(1, releaseId);
        session.executeQuery();

        if(session.nextResult()) { return session.getInteger("LIBRARY_COUNT"); }
        else { return null; }
    }
}
