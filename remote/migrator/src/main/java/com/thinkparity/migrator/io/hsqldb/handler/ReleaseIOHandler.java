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

    /** Sql to create a release. */
    private static final String SQL_CREATE =
        new StringBuffer("insert into RELEASE ")
        .append("(RELEASE_NAME,RELEASE_GROUP_ID,RELEASE_ARTIFACT_ID,")
        .append("RELEASE_VERSION) ")
        .append("values (?,?,?,?)")
        .toString();

    /** Sql to create a release library relationship. */
    private static final String SQL_CREATE_LIBRARY_REL =
        new StringBuffer("insert into RELEASE_LIBRARY_REL ")
        .append("(RELEASE_ID,LIBRARY_ID) ")
        .append("values (?,?)")
        .toString();

    /** Sql to read a release. */
    private static final String SQL_READ =
        new StringBuffer("select R.RELEASE_ID,R.RELEASE_NAME,")
        .append("R.RELEASE_GROUP_ID,R.RELEASE_ARTIFACT_ID,R.RELEASE_VERSION ")
        .append("from RELEASE R ")
        .append("where R.RELEASE_NAME=?")
        .toString();

    /** Sql to read the latest release name. */
    private static final String SQL_READ_LATEST =
        new StringBuffer("select R.RELEASE_NAME ")
        .append("from RELEASE R ")
        .append("where R.RELEASE_ID = (select MAX(R2.RELEASE_ID) from RELEASE R2)")
        .toString();

    /** Sql to read the libraries for a release. */
    private static final String SQL_READ_LIBRARIES =
        new StringBuffer("select L.LIBRARY_ARTIFACT_ID,L.LIBRARY_GROUP_ID,")
        .append("L.LIBRARY_ID,L.LIBRARY_TYPE_ID,L.LIBRARY_VERSION ")
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
            final String name, final String version) throws HypersonicException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE);
            session.setString(1, name);
            session.setString(2, groupId);
            session.setString(3, artifactId);
            session.setString(4, version);
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

    /**
     * @see com.thinkparity.migrator.io.handler.ReleaseIOHandler#read(java.lang.Long)
     */
    public Release read(final String releaseName) throws HypersonicException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ);
            session.setString(1, releaseName);
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

    /** @see com.thinkparity.migrator.io.handler.ReleaseIOHandler#readLatest() */
    public Release readLatest() throws HypersonicException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_LATEST);
            session.executeQuery();

            if(session.nextResult()) {
                return read(session.getString("RELEASE_NAME"));
            }
            else { return null; }
        }
        catch(final HypersonicException hx) { throw hx; }
        finally { session.close(); }
    }

    Release extractRelease(final HypersonicSession session) {
        final Release release = new Release();
        release.setArtifactId(session.getString("RELEASE_ARTIFACT_ID"));
        release.setGroupId(session.getString("RELEASE_GROUP_ID"));
        release.setId(session.getLong("RELEASE_ID"));
        release.setName(session.getString("RELEASE_NAME"));
        release.setVersion(session.getString("RELEASE_VERSION"));

        release.addAllLibraries(readLibraries(session, release.getId()));

        return release;
    }

    private List<Library> readLibraries(final HypersonicSession session,
            final Long releaseId) {
        session.prepareStatement(SQL_READ_LIBRARIES);
        session.setLong(1, releaseId);
        session.executeQuery();

        final List<Library> libraries = new LinkedList<Library>();
        while(session.nextResult()) {
            libraries.add(libraryIO.extractLibrary(session));
        }
        return libraries;
    }
}
