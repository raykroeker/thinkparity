/*
 * Feb 8, 2006
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentContent;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.document.DocumentVersionContent;

import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;
import com.thinkparity.ophelia.model.io.db.hsqldb.SessionManager;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentIOHandler extends AbstractIOHandler implements
		com.thinkparity.ophelia.model.io.handler.DocumentIOHandler {

    /** Sql to create a document. */
	private static final String SQL_CREATE =
		new StringBuffer("insert into DOCUMENT (DOCUMENT_ID) ")
		.append("values (?)")
		.toString();

    /** Sql to create a document version. */
	private static final String SQL_CREATE_VERSION =
		new StringBuffer("insert into DOCUMENT_VERSION ")
		.append("(DOCUMENT_ID,DOCUMENT_VERSION_ID,CONTENT,CONTENT_ENCODING,")
		.append("CONTENT_CHECKSUM,CONTENT_COMPRESSION) ")
		.append("values (?,?,?,?,?,?) ")
		.toString();

    private static final String SQL_DELETE =
		new StringBuffer("delete from DOCUMENT ")
		.append("where DOCUMENT_ID=?")
		.toString();

    private static final String SQL_DELETE_VERSION =
		new StringBuffer("delete from DOCUMENT_VERSION ")
		.append("where DOCUMENT_ID=? and DOCUMENT_VERSION_ID=?")
		.toString();

	private static final String SQL_GET =
		new StringBuffer("select A.ARTIFACT_ID,A.ARTIFACT_NAME,")
		.append("A.ARTIFACT_STATE_ID,A.ARTIFACT_TYPE_ID,A.ARTIFACT_UNIQUE_ID,")
		.append("A.CREATED_BY,A.CREATED_ON,A.UPDATED_BY,A.UPDATED_ON,")
		.append("ARI.UPDATED_BY REMOTE_UPDATED_BY,")
		.append("ARI.UPDATED_ON REMOTE_UPDATED_ON ")
		.append("from DOCUMENT D inner join ARTIFACT A on D.DOCUMENT_ID=A.ARTIFACT_ID ")
        .append("left join ARTIFACT_REMOTE_INFO ARI ")
		.append("on A.ARTIFACT_ID=ARI.ARTIFACT_ID ")
		.append("where A.ARTIFACT_ID=?")
		.toString();

	private static final String SQL_GET_BY_UNIQUE_ID =
		new StringBuffer("select A.ARTIFACT_ID,A.ARTIFACT_NAME,")
		.append("A.ARTIFACT_STATE_ID,A.ARTIFACT_TYPE_ID,A.ARTIFACT_UNIQUE_ID,")
		.append("A.CREATED_BY,A.CREATED_ON,A.UPDATED_BY,A.UPDATED_ON,")
		.append("ARI.UPDATED_BY REMOTE_UPDATED_BY,")
		.append("ARI.UPDATED_ON REMOTE_UPDATED_ON ")
		.append("from DOCUMENT D inner join ARTIFACT A on D.DOCUMENT_ID=A.ARTIFACT_ID ")
        .append("left join ARTIFACT_REMOTE_INFO ARI on A.ARTIFACT_ID=ARI.ARTIFACT_ID ")
		.append("where A.ARTIFACT_UNIQUE_ID=?")
		.toString();

	/** Sql to read a document version. */
    private static final String SQL_GET_VERSION =
		new StringBuffer("select DOCUMENT_ID,DOCUMENT_VERSION_ID,")
		.append("ARTIFACT_NAME,ARTIFACT_TYPE,ARTIFACT_UNIQUE_ID,")
		.append("CONTENT_CHECKSUM,CONTENT_COMPRESSION,CONTENT_ENCODING,")
        .append("CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON ")
		.append("from DOCUMENT_VERSION DV ")
        .append("inner join ARTIFACT_VERSION AV on DV.DOCUMENT_ID = AV.ARTIFACT_ID ")
        .append("and DV.DOCUMENT_VERSION_ID = AV.ARTIFACT_VERSION_ID ")
		.append("where DV.DOCUMENT_ID=? and DV.DOCUMENT_VERSION_ID=?")
		.toString();

	private static final String SQL_LIST =
		new StringBuffer("select D.CONTAINER_ID,A.ARTIFACT_ID,A.ARTIFACT_NAME,")
		.append("A.ARTIFACT_STATE_ID,A.ARTIFACT_TYPE_ID,A.ARTIFACT_UNIQUE_ID,")
		.append("A.CREATED_BY,A.CREATED_ON,A.UPDATED_BY,A.UPDATED_ON,")
		.append("ARI.UPDATED_BY REMOTE_UPDATED_BY,")
		.append("ARI.UPDATED_ON REMOTE_UPDATED_ON ")
		.append("from DOCUMENT D inner join ARTIFACT A on D.DOCUMENT_ID=A.ARTIFACT_ID ")
        .append("left join ARTIFACT_REMOTE_INFO ARI on A.ARTIFACT_ID=ARI.ARTIFACT_ID ")
		.append("order by A.ARTIFACT_ID asc")
		.toString();

    private static final String SQL_LIST_VERSIONS =
		new StringBuffer("select DOCUMENT_ID,DOCUMENT_VERSION_ID,")
		.append("ARTIFACT_NAME,ARTIFACT_TYPE,ARTIFACT_UNIQUE_ID,")
		.append("CONTENT_CHECKSUM,CONTENT_COMPRESSION,CONTENT_ENCODING,")
        .append("CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON ")
		.append("from DOCUMENT_VERSION DV ")
        .append("inner join ARTIFACT_VERSION AV on DV.DOCUMENT_ID=AV.ARTIFACT_ID ")
        .append("and DV.DOCUMENT_VERSION_ID=AV.ARTIFACT_VERSION_ID ")
		.append("where DV.DOCUMENT_ID=? ")
		.append("order by DV.DOCUMENT_VERSION_ID asc")
		.toString();

	/** Sql to open a stream to the document version's content. */
    private static final String SQL_OPEN_STREAM =
            new StringBuffer("select DV.CONTENT ")
            .append("from DOCUMENT_VERSION DV ")
            .append("where DV.DOCUMENT_ID=? and DV.DOCUMENT_VERSION_ID=?")
            .toString();

	/** Sql to read a document version content. */
    private static final String SQL_READ_VERSION_CONTENT =
            new StringBuffer("select DV.DOCUMENT_ID,DV.DOCUMENT_VERSION_ID,")
            .append("DV.CONTENT,DV.CONTENT_CHECKSUM,DV.CONTENT_COMPRESSION,")
            .append("DV.CONTENT_ENCODING,AV.ARTIFACT_NAME,AV.ARTIFACT_TYPE,")
            .append("AV.ARTIFACT_UNIQUE_ID,AV.CREATED_BY,AV.CREATED_ON,")
            .append("AV.UPDATED_BY,AV.UPDATED_ON ")
            .append("from DOCUMENT_VERSION DV ")
            .append("inner join ARTIFACT_VERSION AV on DV.DOCUMENT_ID=AV.ARTIFACT_ID ")
            .append("and DV.DOCUMENT_VERSION_ID=AV.ARTIFACT_VERSION_ID ")
            .append("where DV.DOCUMENT_ID=? and DV.DOCUMENT_VERSION_ID=?")
            .toString();

	private static final String SQL_UPDATE =
		new StringBuffer("update ARTIFACT ")
		.append("set UPDATED_ON=?,ARTIFACT_NAME=?,ARTIFACT_STATE_ID=? ")
		.append("where ARTIFACT_ID=?")
		.toString();

    private static final String SQL_UPDATE_CONTENT =
		new StringBuffer("update DOCUMENT ")
		.append("set CONTENT=?,CONTENT_ENCODING=?,CONTENT_CHECKSUM=?,")
		.append("CONTENT_COMPRESSION=? ")
		.append("where ARTIFACT_ID=?")
		.toString();

	private static final String SQL_UPDATE_VERISON =
            new StringBuffer("update ARTIFACT_VERSION ")
            .append("set ARTIFACT_NAME=? ")
            .append("where ARTIFACT_ID=? and ARTIFACT_VERSION_ID=?")
            .toString();

	/**
	 * Generic artifact io.
	 * 
	 */
	private final ArtifactIOHandler artifactIO;

    /**
     * Create a RemoteDocumentHandler.
     * 
     * @param sessionManager
     *            A hypersonic <code>SessionManager</code>.
     */
	public DocumentIOHandler(final SessionManager sessionManager) {
		super(sessionManager);
		this.artifactIO = new ArtifactIOHandler(sessionManager);
	}

    /**
     * @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#create(com.thinkparity.codebase.model.document.Document)
     * 
     */
	public void create(final Document document) throws HypersonicException {
		final Session session = openSession();
		try {
			artifactIO.create(session, document);

			session.prepareStatement(SQL_CREATE);
			session.setLong(1, document.getId());
			if(1 != session.executeUpdate())
				throw new HypersonicException("Could not create document.");

			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
     * @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#createVersion(com.thinkparity.codebase.model.document.DocumentVersion,
     *      com.thinkparity.codebase.model.document.DocumentVersionContent)
     * 
     */
	public void createVersion(final DocumentVersion version,
            final DocumentVersionContent versionContent) {
		final Session session = openSession();
		try {
			artifactIO.createVersion(session, version);

			session.prepareStatement(SQL_CREATE_VERSION);
			session.setLong(1, version.getArtifactId());
			session.setLong(2, version.getVersionId());
			session.setBytes(3, versionContent.getContent());
			session.setString(4, version.getEncoding());
			session.setString(5, version.getChecksum());
			session.setInt(6, version.getCompression());
			if(1 != session.executeUpdate())
                throw new HypersonicException("Could not create document version.");

			version.setVersionId(version.getVersionId());
			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#delete(java.lang.Long)
	 * 
	 */
	public void delete(final Long documentId) throws HypersonicException {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_DELETE);
			session.setLong(1, documentId);
			if(1 != session.executeUpdate())
				throw new HypersonicException("Could not delete document.");
			artifactIO.deleteRemoteInfo(session, documentId);
			artifactIO.delete(session, documentId);
			session.commit();
		}
		catch(final RuntimeException rx) {
			session.rollback();
			throw rx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#deleteVersion(java.lang.Long,
	 *      java.lang.Long)
	 * 
	 */
	public void deleteVersion(final Long documentId, Long versionId)
			throws HypersonicException {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_DELETE_VERSION);
			session.setLong(1, documentId);
			session.setLong(2, versionId);
			if(1 != session.executeUpdate())
				throw new HypersonicException("Could not delete version.");
			
			artifactIO.deleteVersion(session, documentId, versionId);
			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#get(java.lang.Long)
	 * 
	 */
	public Document get(final Long documentId) {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_GET);
			session.setLong(1, documentId);
			session.executeQuery();

			if(session.nextResult()) { return extractDocument(session); }
			else { return null; }
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#get(java.util.UUID)
	 */
	public Document get(final UUID documentUniqueId) {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_GET_BY_UNIQUE_ID);
			session.setUniqueId(1, documentUniqueId);
			session.executeQuery();

			if(session.nextResult()) { return extractDocument(session); }
			else { return null; }
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
			
	}

	/**
     * @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#getVersion(java.lang.Long,
     *      java.lang.Long)
     * 
     */
	public DocumentVersion getVersion(final Long documentId, final Long versionId) {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_GET_VERSION);
			session.setLong(1, documentId);
			session.setLong(2, versionId);
			session.executeQuery();
			if(session.nextResult()) { return extractVersion(session); }
			else { return null; }
		}
		catch(final RuntimeException rx) {
			session.rollback();
			throw rx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#list()
	 */
	public List<Document> list() {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_LIST);
			session.executeQuery();
			
			final List<Document> documents = new LinkedList<Document>();
			while(session.nextResult()) {
				documents.add(extractDocument(session));
			}
			return documents;
		}
		catch(final RuntimeException rx) {
			session.rollback();
			throw rx;
		}
		finally { session.close(); }
	}

    /**
	 * @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#listVersions(java.lang.Long)
	 */
	public List<DocumentVersion> listVersions(final Long documentId) {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_LIST_VERSIONS);
			session.setLong(1, documentId);
			session.executeQuery();

			final List<DocumentVersion> versions = new LinkedList<DocumentVersion>();
			while(session.nextResult()) {
				versions.add(extractVersion(session));
			}
			return versions;
		}
		finally { session.close(); }
	}

    /**
     * @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#openStream(java.lang.Long, java.lang.Long)
     */
    public InputStream openStream(final Long documentId, final Long versionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_OPEN_STREAM);
            session.setLong(1, documentId);
            session.setLong(2, versionId);
            session.executeQuery();
            if(session.nextResult()) { return session.getInputStream("CONTENT"); }
            else { return null; }
        }
        finally { session.close(); }
    }

	/**
     * @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#readLatestVersion(java.lang.Long)
     * 
     */
	public DocumentVersion readLatestVersion(final Long documentId) {
		final Session session = openSession();
		Long latestVersionId = null;
		try {
			latestVersionId =
				artifactIO.getLatestVersionId(session, documentId);
			return getVersion(documentId, latestVersionId);
		}
		catch(final RuntimeException rx) {
			session.rollback();
			throw rx;
		}
		finally { session.close(); }
	}

	/**
     * @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#readLatestVersionContent(java.lang.Long)
     * 
     */
	public DocumentVersionContent readLatestVersionContent(final Long documentId) {
        final Session session = openSession();
        Long latestVersionId = null;
        try {
            latestVersionId =
                artifactIO.getLatestVersionId(session, documentId);
            return readVersionContent(documentId, latestVersionId);
        }
        finally { session.close(); }
	}

	/**
     * Read the document version content.
     * 
     * @param documentId
     *            The document id.
     * @param versionId
     *            The version id.
     * @return The version content.
     */
    public DocumentVersionContent readVersionContent(final Long documentId,
            final Long versionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_VERSION_CONTENT);
            session.setLong(1, documentId);
            session.setLong(2, versionId);
            session.executeQuery();

            if(session.nextResult()) { return extractVersionContent(session); }
            else { return null; }
        }
        finally { session.close(); }
    }

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#update(com.thinkparity.codebase.model.document.Document)
	 */
	public void update(final Document document) {
		logger.logWarning("Update is misleading.  Only updated on, name, state, and flag information is being set.");
		final Session session = openSession();
		try {
			artifactIO.setFlags(session, document.getId(), document.getFlags());

			session.prepareStatement(SQL_UPDATE);
			session.setCalendar(1, document.getUpdatedOn());
            session.setString(2, document.getName());
            session.setStateAsInteger(3, document.getState());
			session.setLong(4, document.getId());
			if(1 != session.executeUpdate())
				throw new HypersonicException("Could not update document.");

			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

    
    /**
	 * @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#updateContent(com.thinkparity.codebase.model.document.DocumentContent)
	 */
	public void updateContent(final DocumentContent content) {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_UPDATE_CONTENT);
			session.setBytes(1, content.getContent());
			session.setString(2, "???");
			session.setString(3, content.getChecksum());
			session.setInt(4, -1);
			session.setLong(5, content.getDocumentId());
			session.executeUpdate();

			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

    /** @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#updateVersion(com.thinkparity.codebase.model.document.DocumentVersion) */
    public void updateVersion(final DocumentVersion documentVersion) throws HypersonicException {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_VERISON);
            session.setString(1, documentVersion.getName());
            session.setLong(2, documentVersion.getArtifactId());
            session.setLong(3, documentVersion.getVersionId());
            if(1 != session.executeUpdate())
                throw new HypersonicException("[DOCUMENT IO] [UPDATE VERSION]");

            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * Extract the document.
     * 
     * @param session
     *            The database session.
     * @return The document.
     */
	Document extractDocument(final Session session) {
		final Document d = new Document();
		d.setCreatedBy(session.getString("CREATED_BY"));
		d.setCreatedOn(session.getCalendar("CREATED_ON"));
		d.setId(session.getLong("ARTIFACT_ID"));
		d.setName(session.getString("ARTIFACT_NAME"));
		d.setRemoteInfo(artifactIO.extractRemoteInfo(session));
		d.setState(session.getStateFromInteger("ARTIFACT_STATE_ID"));
		d.setType(session.getTypeFromInteger("ARTIFACT_TYPE_ID"));
		d.setUniqueId(session.getUniqueId("ARTIFACT_UNIQUE_ID"));
		d.setUpdatedBy(session.getString("UPDATED_BY"));
		d.setUpdatedOn(session.getCalendar("UPDATED_ON"));

		d.setFlags(artifactIO.getFlags(d.getId()));
		return d;
	}

	/**
     * Extract the version.
     * 
     * @param session
     *            The database session.
     * @return The version.
     */
	DocumentVersion extractVersion(final Session session) {
		final DocumentVersion dv = new DocumentVersion();
		dv.setArtifactId(session.getLong("DOCUMENT_ID"));
		dv.setArtifactType(session.getTypeFromString("ARTIFACT_TYPE"));
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

		dv.setMetaData(getVersionMetaData(dv.getArtifactId(), dv.getVersionId()));
		return dv;
	}

	/**
	 * Obtain the version meta data.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @param versionId
	 *            The version id.
	 * @return The version meta data.
	 */
	Properties getVersionMetaData(final Long artifactId, final Long versionId) {
		final Session session = openSession();
		try {
			return artifactIO.getVersionMetaData(session, artifactId, versionId);
		}
		finally { session.close(); }
	}

	/**
	 * Extract the document version content from the session.
	 * 
	 * @param session
	 *            The session.
	 * @return The document version content.
	 */
	private DocumentVersionContent extractVersionContent(final Session session) {
		final DocumentVersionContent dvc = new DocumentVersionContent();
		dvc.setContent(session.getBytes("CONTENT"));
        dvc.setVersion(extractVersion(session));
		return dvc;
	}


}
