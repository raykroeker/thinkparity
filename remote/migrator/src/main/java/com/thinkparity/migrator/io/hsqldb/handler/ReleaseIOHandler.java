/*
 * May 9, 2006 12:27:36 PM
 * $Id$
 */
package com.thinkparity.migrator.io.hsqldb.handler;

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
    private static final String SQL_READ_BY_NAME =
        new StringBuffer("select R.RELEASE_ID,R.RELEASE_NAME,")
        .append("R.RELEASE_GROUP_ID,R.RELEASE_ARTIFACT_ID,R.RELEASE_VERSION,")
        .append("L.LIBRARY_ID,L.LIBRARY_TYPE_ID,L.LIBRARY_GROUP_ID,")
        .append("L.LIBRARY_ARTIFACT_ID,L.LIBRARY_VERSION ")
        .append("from RELEASE R ")
        .append("inner join RELEASE_LIBRARY_REL RLR on R.RELEASE_ID = RLR.RELEASE_ID ")
        .append("inner join LIBRARY L on RLR.LIBRARY_ID = L.LIBRARY_ID ")
        .append("where R.RELEASE_NAME=?")
        .toString();

    /** Create ReleaseIOHandler. */
    public ReleaseIOHandler() { super(); }

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
                        "[RMIGRATOR] [RELEASE] [IO] [CREATE] [COULD NOT EXECUTE UPDATE]");

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
    public void createLibraryRel(Long releaseId, Long libraryId) throws HypersonicException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_LIBRARY_REL);
            session.setLong(1, releaseId);
            session.setLong(2, libraryId);
            if(1 != session.executeUpdate())
                throw new HypersonicException(
                        "[RMIGRATOR] [RELEASE] [IO] [CREATE LIBRARY REL] [COULD NOT EXECUTE UPDATE]");

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
            session.prepareStatement(SQL_READ_BY_NAME);
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

    Release extractRelease(final HypersonicSession session) {
        final Release release = new Release();
        release.setArtifactId(session.getString("RELEASE_ARTIFACT_ID"));
        release.setGroupId(session.getString("RELEASE_GROUP_ID"));
        release.setId(session.getLong("RELEASE_ID"));
        release.setName(session.getString("RELEASE_NAME"));
        release.setVersion(session.getString("RELEASE_VERSION"));

        
        return release;
    }
}
