/*
 * Created On: Jun 28, 2006 8:49:07 PM
 */
package com.thinkparity.model.parity.model.io.db.hsqldb.handler;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.container.Container;
import com.thinkparity.model.parity.model.container.ContainerDraft;
import com.thinkparity.model.parity.model.container.ContainerDraftArtifactState;
import com.thinkparity.model.parity.model.container.ContainerVersion;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.model.parity.model.io.db.hsqldb.Session;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ContainerIOHandler extends AbstractIOHandler implements
        com.thinkparity.model.parity.model.io.handler.ContainerIOHandler {

    /** Sql to insert a version relationship. */
    private static final String SQL_ADD_VERSION_REL =
            new StringBuffer("insert into CONTAINER_VERSION_ARTIFACT_VERSION_REL ")
            .append("(CONTAINER_ID,CONTAINER_VERSION_ID,ARTIFACT_ID,")
            .append("ARTIFACT_VERSION_ID,ARTIFACT_TYPE_ID) ")
            .append("values (?,?,?,?,?)")
            .toString();

    /** Sql to create a container. */
    private static final String SQL_CREATE =
            new StringBuffer("insert into CONTAINER ")
            .append("(CONTAINER_ID) ")
            .append("values (?)")
            .toString();

    /** Sql to create a draft. */
    private static final String SQL_CREATE_DRAFT =
            new StringBuffer("insert into CONTAINER_DRAFT ")
            .append("(CONTAINER_DRAFT_ID) ")
            .append("values (?)")
            .toString();

    /** Sql to create a draft artifact relationship. */
    private static final String SQL_CREATE_DRAFT_ARTIFACT_REL =
            new StringBuffer("insert into CONTAINER_DRAFT_ARTIFACT_REL ")
            .append("(CONTAINER_DRAFT_ID,ARTIFACT_ID,ARTIFACT_STATE_ID) ")
            .append("values (?,?,?)")
            .toString();
                    
    /** Sql to create a container version. */
    private static final String SQL_CREATE_VERSION =
            new StringBuffer("insert into CONTAINER_VERSION ")
            .append("(CONTAINER_ID,CONTAINER_VERSION_ID) ")
            .append("values (?,?)")
            .toString();

    /** Sql to delete a container. */
    private static final String SQL_DELETE =
            new StringBuffer("delete from CONTAINER ")
            .append("where CONTAINER_ID=?")
            .toString();

    /** Sql to delete a container version. */
    private static final String SQL_DELETE_VERSION =
            new StringBuffer("delete from CONTAINER_VERSION ")
            .append("where CONTAINER_ID=? and CONTAINER_VERSION_ID=?")
            .toString();

    /** Sql to read a container. */
    private static final String SQL_READ =
            new StringBuffer("select C.CONTAINER_ID,A.ARTIFACT_NAME,")
            .append("A.ARTIFACT_STATE_ID,A.ARTIFACT_TYPE_ID,ARTIFACT_UNIQUE_ID,")
            .append("A.CREATED_BY,A.CREATED_ON,A.UPDATED_BY,A.UPDATED_ON,")
            .append("ARI.UPDATED_BY REMOTE_UPDATED_BY,")
            .append("ARI.UPDATED_ON REMOTE_UPDATED_ON ")
            .append("from CONTAINER C ")
            .append("inner join ARTIFACT A on C.CONTAINER_ID=A.ARTIFACT_ID ")
            .append("left join ARTIFACT_REMOTE_INFO ARI on A.ARTIFACT_ID=ARI.ARTIFACT_ID ")
            .toString();

    /** Sql to read a container. */
    private static final String SQL_READ_BY_CONTAINER_ID =
            new StringBuffer(SQL_READ)
            .append("where C.CONTAINER_ID=?")
            .toString();

    /** Sql to read document versions from the artifact version attachments. */
    private static final String SQL_READ_DOCUMENT_VERSIONS =
            new StringBuffer("select A.ARTIFACT_ID,A.ARTIFACT_NAME,")
            .append("A.ARTIFACT_STATE_ID,A.ARTIFACT_TYPE_ID,")
            .append("A.ARTIFACT_UNIQUE_ID,A.CREATED_BY,A.CREATED_ON,")
            .append("A.UPDATED_BY,A.UPDATED_ON,D.DOCUMENT_ID,")
            .append("DV.CONTENT_CHECKSUM,DV.CONTENT_COMPRESSION,")
            .append("DV.CONTENT_ENCODING,DV.DOCUMENT_VERSION_ID,")
            .append("ARI.UPDATED_BY REMOTE_UPDATED_BY,")
            .append("ARI.UPDATED_ON REMOTE_UPDATED_ON ")
            .append("from CONTAINER_VERSION_ARTIFACT_VERSION_REL CVAVR ")
            .append("inner join ARTIFACT A on CVAVR.ARTIFACT_ID=A.ARTIFACT_ID ")
            .append("inner join DOCUMENT D on A.ARTIFACT_ID=D.DOCUMENT_ID ")
            .append("left join ARTIFACT_REMOTE_INFO ARI on A.ARTIFACT_ID=ARI.ARTIFACT_ID ")
            .append("inner join DOCUMENT_VERSION DV on CVAVR.ARTIFACT_ID=DV.DOCUMENT_ID ")
            .append("and CVAVR.ARTIFACT_VERSION_ID=DV.DOCUMENT_VERSION_ID ")
            .append("where CVAVR.CONTAINER_ID=? and CVAVR.CONTAINER_VERSION_ID=?")
            .toString();

    /** Sql to read documents from the artifact version attachments. */
    private static final String SQL_READ_DOCUMENTS =
            new StringBuffer("select A.ARTIFACT_ID,A.ARTIFACT_NAME,")
            .append("A.ARTIFACT_STATE_ID,A.ARTIFACT_TYPE_ID,")
            .append("A.ARTIFACT_UNIQUE_ID,A.CREATED_BY,A.CREATED_ON,")
            .append("A.UPDATED_BY,A.UPDATED_ON,")
            .append("ARI.UPDATED_BY REMOTE_UPDATED_BY,")
            .append("ARI.UPDATED_ON REMOTE_UPDATED_ON ")
            .append("from CONTAINER_VERSION_ARTIFACT_VERSION_REL CVAVR ")
            .append("inner join ARTIFACT A on CVAVR.ARTIFACT_ID=A.ARTIFACT_ID ")
            .append("inner join DOCUMENT D on A.ARTIFACT_ID=D.DOCUMENT_ID ")
            .append("left join ARTIFACT_REMOTE_INFO ARI on A.ARTIFACT_ID=ARI.ARTIFACT_ID ")
            .append("where CVAVR.CONTAINER_ID=? and CVAVR.CONTAINER_VERSION_ID=?")
            .toString();

    /** Sql to read a draft. */
    private static final String SQL_READ_DRAFT =
            new StringBuffer("select CONTAINER_DRAFT_ID ")
            .append("from CONTAINER_DRAFT CD ")
            .append("where CD.CONTAINER_DRAFT_ID=?")
            .toString();

    /** Sql to read draft documents. */
    private static final String SQL_READ_DRAFT_DOCUMENTS =
        new StringBuffer("select A.ARTIFACT_ID,A.ARTIFACT_NAME,")
        .append("A.ARTIFACT_STATE_ID,A.ARTIFACT_TYPE_ID,")
        .append("A.ARTIFACT_UNIQUE_ID,A.CREATED_BY,A.CREATED_ON,")
        .append("A.UPDATED_BY,A.UPDATED_ON,")
        .append("ARI.UPDATED_BY REMOTE_UPDATED_BY,")
        .append("ARI.UPDATED_ON REMOTE_UPDATED_ON ")
        .append("from CONTAINER_DRAFT_ARTIFACT_REL CDAVR ")
        .append("inner join ARTIFACT A on CDAVR.ARTIFACT_ID=A.ARTIFACT_ID ")
        .append("inner join DOCUMENT D on A.ARTIFACT_ID=D.DOCUMENT_ID ")
        .append("left join ARTIFACT_REMOTE_INFO ARI on A.ARTIFACT_ID=ARI.ARTIFACT_ID ")
        .append("where CDAVR.CONTAINER_DRAFT_ID=?")
        .toString();

    /** Sql to read a container version. */
    private static final String SQL_READ_VERSION =
            new StringBuffer("select CV.CONTAINER_ID,CV.CONTAINER_VERSION_ID,")
            .append("A.ARTIFACT_TYPE_ID,AV.ARTIFACT_UNIQUE_ID,AV.CREATED_BY,")
            .append("AV.CREATED_ON,AV.ARTIFACT_NAME,AV.UPDATED_BY,AV.UPDATED_ON ")
            .append("from CONTAINER_VERSION CV ")
            .append("inner join ARTIFACT_VERSION AV on CV.CONTAINER_ID=AV.ARTIFACT_ID ")
            .append("and CV.CONTAINER_VERSION_ID=AV.ARTIFACT_VERSION_ID ")
            .append("inner join ARTIFACT A on CV.CONTAINER_ID=A.ARTIFACT_ID ")
            .append("where CV.CONTAINER_ID=? and CV.CONTAINER_VERSION_ID=?")
            .toString();

    /** Sql to read container versions. */
    private static final String SQL_READ_VERSIONS =
            new StringBuffer("select CV.CONTAINER_ID,CV.CONTAINER_VERSION_ID,")
            .append("A.ARTIFACT_TYPE_ID,AV.ARTIFACT_UNIQUE_ID,AV.CREATED_BY,")
            .append("AV.CREATED_ON,AV.ARTIFACT_NAME,AV.UPDATED_BY,AV.UPDATED_ON ")
            .append("from CONTAINER_VERSION CV ")
            .append("inner join ARTIFACT_VERSION AV on CV.CONTAINER_ID=AV.ARTIFACT_ID ")
            .append("and CV.CONTAINER_VERSION_ID=AV.ARTIFACT_VERSION_ID ")
            .append("inner join ARTIFACT A on CV.CONTAINER_ID=A.ARTIFACT_ID ")
            .append("where CV.CONTAINER_ID=?")
            .toString();

    /** Sql to remove a version relationship. */
    private static final String SQL_REMOVE_VERSION_REL =
            new StringBuffer("delete from CONTAINER_VERSION_ARTIFACT_VERSION_REL ")
            .append("where CONTAINER_ID=? and CONTAINER_VERSION_ID=? ")
            .append("and ARTIFACT_ID=? and ARTIFACT_VERSION_ID=?")
            .toString();

    /** Sql to remove all version relationships. */
    private static final String SQL_REMOVE_VERSIONS_REL =
            new StringBuffer("delete from CONTAINER_VERSION_ARTIFACT_VERSION_REL ")
            .append("where CONTAINER_ID=? and CONTAINER_VERSION_ID=? ")
            .toString();

    /**
     * Obtain a log4j api id.
     * 
     * @param api
     *            The api.
     * @return A log4j api id.
     */
    protected static StringBuffer getApiId(final String api) {
        return getIOId("[CONTAINER] ").append(api);
    }

    /**
     * Obtain a log4j error id.
     * 
     * @param api
     *            The api.
     * @param error
     *            The error.
     * @return A log4j error id.
     */
    protected static String getErrorId(final String api, final String error) {
        return getApiId(api).append(" ").append(error).toString();
    }

    /** Generic artifact io. */
    private final ArtifactIOHandler artifactIO;

    /** Document io. */
    private final DocumentIOHandler documentIO;

    /** Create ContainerIOHandler. */
    public ContainerIOHandler() {
        super();
        this.artifactIO = new ArtifactIOHandler();
        this.documentIO = new DocumentIOHandler();
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContainerIOHandler#addVersion(java.lang.Long,
     *      java.lang.Long, java.lang.Long, java.lang.Long,
     *      com.thinkparity.model.parity.model.artifact.ArtifactType)
     * 
     */
    public void addVersion(final Long containerId,
            final Long containerVersionId, final Long artifactId,
            final Long artifactVersionId, final ArtifactType artifactType) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_ADD_VERSION_REL);
            session.setLong(1, containerId);
            session.setLong(2, containerVersionId);
            session.setLong(3, artifactId);
            session.setLong(4, artifactVersionId);
            session.setTypeAsInteger(5, artifactType);
            if(1 != session.executeUpdate())
                throw new HypersonicException(getErrorId("[ADD VERSION]", "[CANNOT ADD VERSION]"));
            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContainerIOHandler#create()
     * 
     */
    public void create(final Container container) {
        final Session session = openSession();
        try {
            artifactIO.create(session, container);

            session.prepareStatement(SQL_CREATE);
            session.setLong(1, container.getId());
            if(1 != session.executeUpdate())
                throw new HypersonicException(getErrorId("[CREATE]", "[CANNOT CREATE CONTAINER]"));
            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContainerIOHandler#createDraft(com.thinkparity.model.parity.model.container.ContainerDraft)
     */
    public void createDraft(final ContainerDraft draft) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_DRAFT);
            session.setLong(1, draft.getContainerId());
            if(1 != session.executeUpdate())
                throw new HypersonicException(getErrorId("[CREATE DRAFT]", "[COULD NOT CREATE DRAFT]"));
            
            session.prepareStatement(SQL_CREATE_DRAFT_ARTIFACT_REL);
            session.setLong(1, draft.getContainerId());
            for(final Artifact artifact : draft.getArtifacts()) {
                session.setLong(2, artifact.getId());
                session.setStateAsInteger(3, draft.getArtifactState(artifact.getId()));
                if(1 != session.executeUpdate())
                    throw new HypersonicException(getErrorId("[CREATE DRAFT]", "[COULD NOT CREATE DRAFT DOCUMENT]"));
            }
            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContainerIOHandler#createDraftArtifactRel(java.lang.Long, java.lang.Long, com.thinkparity.model.parity.model.container.ContainerDraftArtifactState)
     */
    public void createDraftArtifactRel(final Long containerId,
            final Long artifactId, final ContainerDraftArtifactState state) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_DRAFT_ARTIFACT_REL);
            session.setLong(1, containerId);
            session.setLong(2, artifactId);
            session.setStateAsInteger(3, state);
            if(1 != session.executeUpdate())
                throw new HypersonicException(getErrorId("[CREATE DRAFT ARTIFACT REL]", "[COULD NOT CREATE DRAFT ARTIFACT REL]"));
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContainerIOHandler#createVersion(com.thinkparity.model.parity.model.container.ContainerVersion)
     * 
     */
    public void createVersion(final ContainerVersion version) {
        final Session session = openSession();
        try {
            artifactIO.createVersion(session, version);
            session.prepareStatement(SQL_CREATE_VERSION);
            session.setLong(1, version.getArtifactId());
            session.setLong(2, version.getVersionId());
            if(1 != session.executeUpdate())
                throw new HypersonicException(getErrorId("[CREATE VERSION]", "[CANNOT CREATE CONTAINER VERSION]"));
            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContainerIOHandler#delete(java.lang.Long)
     * 
     */
    public void delete(final Long containerId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DELETE);
            session.setLong(1, containerId);
            if(1 != session.executeUpdate())
                throw new HypersonicException(getErrorId("[DELETE]", "[CANNOT DELETE CONTAINER]"));
            artifactIO.delete(session, containerId);
            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContainerIOHandler#deleteVersion(java.lang.Long,
     *      java.lang.Long)
     * 
     */
    public void deleteVersion(Long containerId, Long versionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_VERSION);
            session.setLong(1, containerId);
            session.setLong(2, versionId);
            if(1 != session.executeUpdate())
                throw new HypersonicException(getErrorId("[DELETE VERSION]", "[COULD NOT DELETE VERSION]"));
            artifactIO.deleteVersion(session, containerId, versionId);
            session.commit();
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContainerIOHandler#read()
     * 
     */
    public List<Container> read() {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ);
            session.executeQuery();

            final List<Container> containers = new ArrayList<Container>();
            while(session.nextResult()) {
                containers.add(extractContainer(session));
            }
            return containers;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContainerIOHandler#read(java.lang.Long)
     * 
     */
    public Container read(final Long containerId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_BY_CONTAINER_ID);
            session.setLong(1, containerId);
            session.executeQuery();

            if(session.nextResult()) { return extractContainer(session); }
            else { return null; }
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContainerIOHandler#readDocuments(java.lang.Long, java.lang.Long)
     */
    public List<Document> readDocuments(final Long containerId,
            final Long versionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_DOCUMENTS);
            session.setLong(1, containerId);
            session.setLong(2, versionId);
            session.executeQuery();
            final List<Document> documents = new ArrayList<Document>();
            while(session.nextResult()) {
                documents.add(documentIO.extractDocument(session));
            }
            return documents;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContainerIOHandler#readDocumentVersions(java.lang.Long,
     *      java.lang.Long)
     * 
     */
    public List<DocumentVersion> readDocumentVersions(final Long containerId,
            final Long versionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_DOCUMENT_VERSIONS);
            session.setLong(1, containerId);
            session.setLong(2, versionId);
            session.executeQuery();
            final List<DocumentVersion> versions = new ArrayList<DocumentVersion>();
            while(session.nextResult()) {
                versions.add(extractDocumentVersion(session));
            }
            return versions;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContainerIOHandler#readDraft(java.lang.Long)
     */
    public ContainerDraft readDraft(final Long containerId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_DRAFT);
            session.setLong(1, containerId);
            session.executeQuery();
            if(session.nextResult()) { return extractDraft(session); }
            else { return null; }
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContainerIOHandler#readVersion(java.lang.Long,
     *      java.lang.Long)
     * 
     */
    public ContainerVersion readLatestVersion(final Long containerId) {
        final Session session = openSession();
        try {
            return readVersion(containerId, artifactIO.getLatestVersionId(session, containerId));
        }
        finally { session.close(); }
    }

    /**
     * Read a container version.
     * 
     * @param containerId
     *            The container id.
     * @param versionId
     *            The version id.
     * @return The container version.
     */
    public ContainerVersion readVersion(final Long containerId,
            final Long versionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_VERSION);
            session.setLong(1, containerId);
            session.setLong(2, versionId);
            session.executeQuery();
            if(session.nextResult()) { return extractVersion(session); }
            else { return null; }
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContainerIOHandler#readVersions(java.lang.Long)
     * 
     */
    public List<ContainerVersion> readVersions(final Long containerId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_VERSIONS);
            session.setLong(1, containerId);
            session.executeQuery();
            final List<ContainerVersion> versions = new ArrayList<ContainerVersion>();
            while(session.nextResult()) {
                versions.add(extractVersion(session));
            }
            return versions;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContainerIOHandler#removeVersion(java.lang.Long,
     *      java.lang.Long, java.lang.Long, java.lang.Long)
     * 
     */
    public void removeVersion(final Long containerId,
            final Long containerVersionId, final Long artifactId,
            final Long artifactVersionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_REMOVE_VERSION_REL);
            session.setLong(1, containerId);
            session.setLong(2, containerVersionId);
            session.setLong(3, artifactId);
            session.setLong(4, artifactVersionId);
            if(1 != session.executeUpdate())
                throw new HypersonicException(getErrorId("[REMOVE VERSION]", "[CANNOT REMOVE VERSION]"));
            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContainerIOHandler#removeVersions(java.lang.Long, java.lang.Long)
     */
    public void removeVersions(Long containerId, Long versionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_REMOVE_VERSIONS_REL);
            session.setLong(1, containerId);
            session.setLong(2, versionId);
            session.executeUpdate();
            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * Extract a container from the session.
     * 
     * @param session
     *            A database session.
     * @return A container.
     */
    Container extractContainer(final Session session) {
        final Container container = new Container();
        container.setCreatedBy(session.getString("CREATED_BY"));
        container.setCreatedOn(session.getCalendar("CREATED_ON"));
        container.setId(session.getLong("CONTAINER_ID"));
        container.setName(session.getString("ARTIFACT_NAME"));
        container.setRemoteInfo(artifactIO.extractRemoteInfo(session));
        container.setState(session.getStateFromInteger("ARTIFACT_STATE_ID"));
        container.setType(session.getTypeFromInteger("ARTIFACT_TYPE_ID"));
        container.setUniqueId(session.getUniqueId("ARTIFACT_UNIQUE_ID"));
        container.setUpdatedBy(session.getString("UPDATED_BY"));
        container.setUpdatedOn(session.getCalendar("UPDATED_ON"));

        container.setDraft(readDraft(container.getId()));
        container.setFlags(artifactIO.getFlags(container.getId()));
        return container;
    }

    /**
     * Extract the draft from the session.
     * 
     * @param session
     *            A database session.
     * @return A draft.
     */
    ContainerDraft extractDraft(final Session session) {
        final ContainerDraft draft = new ContainerDraft();
        draft.setContainerId(session.getLong("CONTAINER_DRAFT_ID"));
        draft.addAllDocuments(readDraftDocuments(draft.getContainerId()));
        return draft;
    }

    /**
     * Extract a container version from the database session.
     * 
     * @param session
     *            A databsae session.
     * @return A container version.
     */
    ContainerVersion extractVersion(final Session session) {
       final ContainerVersion version = new ContainerVersion();
       version.setArtifactId(session.getLong("CONTAINER_ID"));
       version.setArtifactType(session.getTypeFromInteger("ARTIFACT_TYPE_ID"));
       version.setArtifactUniqueId(session.getUniqueId("ARTIFACT_UNIQUE_ID"));
       version.setCreatedBy(session.getString("CREATED_BY"));
       version.setCreatedOn(session.getCalendar("CREATED_ON"));
       version.setName(session.getString("ARTIFACT_NAME"));
       version.setUpdatedBy(session.getString("UPDATED_BY"));
       version.setUpdatedOn(session.getCalendar("UPDATED_ON"));
       version.setVersionId(session.getLong("CONTAINER_VERSION_ID"));
       version.setMetaData(artifactIO.getVersionMetaData(session, version.getArtifactId(), version.getVersionId()));
       return version;
    }

    private DocumentVersion extractDocumentVersion(final Session session) {
        final DocumentVersion dv = new DocumentVersion();
        dv.setArtifactId(session.getLong("DOCUMENT_ID"));
        dv.setArtifactType(session.getTypeFromInteger("ARTIFACT_TYPE_ID"));
        dv.setArtifactUniqueId(session.getUniqueId("ARTIFACT_UNIQUE_ID"));
        dv.setChecksum(session.getString("CONTENT_CHECKSUM"));
        dv.setCompression(session.getInteger("CONTENT_COMPRESSION"));
        dv.setCreatedBy(session.getString("CREATED_BY"));
        dv.setCreatedOn(session.getCalendar("CREATED_ON"));
        dv.setEncoding(session.getString("CONTENT_ENCODING"));
        dv.setName(session.getString("ARTIFACT_NAME"));
        dv.setUpdatedBy(session.getString("UPDATED_BY"));
        dv.setUpdatedOn(session.getCalendar("UPDATED_ON"));
        dv.setVersionId(session.getLong("DOCUMENT_VERSION_ID"));

        dv.setMetaData(documentIO.getVersionMetaData(dv.getArtifactId(), dv.getVersionId()));
        return dv;
    }

    /**
     * Read the draft documents.
     * 
     * @param draftId
     *            A draft id.
     * @return A list of documents.
     */
    private List<Document> readDraftDocuments(final Long draftId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_DRAFT_DOCUMENTS);
            session.setLong(1, draftId);
            session.executeQuery();
            final List<Document> documents = new ArrayList<Document>();
            while(session.nextResult()) {
                documents.add(documentIO.extractDocument(session));
            }
            return documents;
        }
        finally { session.close(); }
    }
}
