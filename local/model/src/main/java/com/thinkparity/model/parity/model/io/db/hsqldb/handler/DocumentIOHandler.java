/*
 * Feb 8, 2006
 */
package com.thinkparity.model.parity.model.io.db.hsqldb.handler;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import com.thinkparity.model.parity.model.artifact.ArtifactRemoteInfo;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentContent;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.document.DocumentVersionContent;
import com.thinkparity.model.parity.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.model.parity.model.io.db.hsqldb.Session;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentIOHandler extends AbstractIOHandler implements
		com.thinkparity.model.parity.model.io.handler.DocumentIOHandler {

	private static final String SQL_CREATE =
		new StringBuffer("insert into DOCUMENT (")
		.append("ARTIFACT_ID,CONTENT,CONTENT_ENCODING,CONTENT_CHECKSUM,")
		.append("CONTENT_COMPRESSION) ")
		.append("values (?,?,?,?,?)")
		.toString();

	private static final String SQL_CREATE_VERSION =
		new StringBuffer("insert into DOCUMENT_VERSION ")
		.append("(ARTIFACT_ID,ARTIFACT_VERSION_ID,CONTENT,CONTENT_ENCODING,")
		.append("CONTENT_CHECKSUM,CONTENT_COMPRESSION) ")
		.append("values (?,?,?,?,?,?) ")
		.toString();

	private static final String SQL_DELETE =
		new StringBuffer("delete from DOCUMENT ")
		.append("where ARTIFACT_ID=?")
		.toString();

	private static final String SQL_DELETE_VERSION =
		new StringBuffer("delete from DOCUMENT_VERSION ")
		.append("where ARTIFACT_ID=? and ARTIFACT_VERSION_ID=?")
		.toString();

	private static final String SQL_GET =
		new StringBuffer("select A.ARTIFACT_ID,A.ARTIFACT_NAME,")
		.append("A.ARTIFACT_STATE_ID,A.ARTIFACT_TYPE_ID,A.ARTIFACT_UNIQUE_ID,")
		.append("A.CREATED_BY,A.CREATED_ON,A.UPDATED_BY,A.UPDATED_ON,")
		.append("ARI.UPDATED_BY REMOTE_UPDATED_BY,")
		.append("ARI.UPDATED_ON REMOTE_UPDATED_ON ")
		.append("from ARTIFACT A left join ARTIFACT_REMOTE_INFO ARI ")
		.append("on A.ARTIFACT_ID=ARI.ARTIFACT_ID ")
		.append("where A.ARTIFACT_ID=?")
		.toString();

	private static final String SQL_GET_BY_UNIQUE_ID =
		new StringBuffer("select A.ARTIFACT_ID,A.ARTIFACT_NAME,")
		.append("A.ARTIFACT_STATE_ID,A.ARTIFACT_TYPE_ID,A.ARTIFACT_UNIQUE_ID,")
		.append("A.CREATED_BY,A.CREATED_ON,A.UPDATED_BY,A.UPDATED_ON,")
		.append("ARI.UPDATED_BY REMOTE_UPDATED_BY,")
		.append("ARI.UPDATED_ON REMOTE_UPDATED_ON ")
		.append("from ARTIFACT A left join ARTIFACT_REMOTE_INFO ARI ")
		.append("on A.ARTIFACT_ID=ARI.ARTIFACT_ID ")
		.append("where A.ARTIFACT_UNIQUE_ID=?")
		.toString();

	private static final String SQL_GET_CONTENT =
		new StringBuffer("select ARTIFACT_ID,CONTENT,CONTENT_ENCODING,")
		.append("CONTENT_CHECKSUM,CONTENT_COMPRESSION ")
		.append("from DOCUMENT ")
		.append("where ARTIFACT_ID=?")
		.toString();

	private static final String SQL_GET_DOCUMENT_VERSION =
		new StringBuffer("select ARTIFACT_ID,ARTIFACT_VERSION_ID,CONTENT,")
		.append("CONTENT_ENCODING,CONTENT_CHECKSUM,CONTENT_COMPRESSION ")
		.append("from DOCUMENT_VERSION ")
		.append("where ARTIFACT_ID=? and ARTIFACT_VERSION_ID=?")
		.toString();

	private static final String SQL_GET_VERSION =
		new StringBuffer("select ARTIFACT_ID,ARTIFACT_VERSION_ID,")
		.append("ARTIFACT_NAME,ARTIFACT_TYPE,ARTIFACT_UNIQUE_ID,")
		.append("CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON ")
		.append("from ARTIFACT_VERSION ")
		.append("where ARTIFACT_ID=? and ARTIFACT_VERSION_ID=?")
		.toString();

	private static final String SQL_LIST =
		new StringBuffer("select A.ARTIFACT_ID,A.ARTIFACT_NAME,")
		.append("A.ARTIFACT_STATE_ID,A.ARTIFACT_TYPE_ID,A.ARTIFACT_UNIQUE_ID,")
		.append("A.CREATED_BY,A.CREATED_ON,A.UPDATED_BY,A.UPDATED_ON,")
		.append("ARI.UPDATED_BY REMOTE_UPDATED_BY,")
		.append("ARI.UPDATED_ON REMOTE_UPDATED_ON ")
		.append("from ARTIFACT A left join ARTIFACT_REMOTE_INFO ARI ")
		.append("on A.ARTIFACT_ID=ARI.ARTIFACT_ID ")
		.append("order by A.ARTIFACT_ID asc")
		.toString();

	private static final String SQL_LIST_VERSIONS =
		new StringBuffer("select ARTIFACT_ID,ARTIFACT_VERSION_ID,")
		.append("ARTIFACT_NAME,ARTIFACT_TYPE,ARTIFACT_UNIQUE_ID,")
		.append("CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON ")
		.append("from ARTIFACT_VERSION ")
		.append("where ARTIFACT_ID=? ")
		.append("order by ARTIFACT_VERSION_ID asc")
		.toString();

	private static final String SQL_UPDATE =
		new StringBuffer("update ARTIFACT ")
		.append("set UPDATED_ON=? ")
		.append("where ARTIFACT_ID=?")
		.toString();

	private static final String SQL_UPDATE_CONTENT =
		new StringBuffer("update DOCUMENT ")
		.append("set CONTENT=?,CONTENT_ENCODING=?,CONTENT_CHECKSUM=?,")
		.append("CONTENT_COMPRESSION=? ")
		.append("where ARTIFACT_ID=?")
		.toString();

	/**
	 * Generic artifact io.
	 * 
	 */
	private final ArtifactIOHandler artifactIO;

	/**
	 * Create a DocumentIOHandler.
	 * 
	 */
	public DocumentIOHandler() {
		super();
		this.artifactIO = new ArtifactIOHandler();
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.DocumentIOHandler#create(com.thinkparity.model.parity.model.document.Document,
	 *      com.thinkparity.model.parity.model.document.DocumentContent)
	 * 
	 */
	public void create(final Document document, final DocumentContent content)
			throws HypersonicException {
		final Session session = openSession();
		try {
			artifactIO.create(session, document);

			session.prepareStatement(SQL_CREATE);
			session.setLong(1, document.getId());
			session.setBytes(2, content.getContent());
			session.setString(3, "???");
			session.setString(4, content.getChecksum());
			session.setInt(5, -1);
			if(1 != session.executeUpdate())
				throw new HypersonicException("Unable to create.");

			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.DocumentIOHandler#createVersion(com.thinkparity.model.parity.model.document.DocumentVersion,
	 *      com.thinkparity.model.parity.model.document.DocumentVersionContent)
	 * 
	 */
	public void createVersion(final DocumentVersion version,
			final DocumentVersionContent versionContent) {
		final Session session = openSession();
		try {
			artifactIO.createVersion(session, version);

			final DocumentContent documentContent =
				versionContent.getDocumentContent();
			session.prepareStatement(SQL_CREATE_VERSION);
			session.setLong(1, version.getArtifactId());
			session.setLong(2, version.getVersionId());
			session.setBytes(3, documentContent.getContent());
			session.setString(4, "???");
			session.setString(5, documentContent.getChecksum());
			session.setInt(6, -1);
			session.executeUpdate();

			versionContent.setVersionId(version.getVersionId());

			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.DocumentIOHandler#createVersion(java.lang.Long,
	 *      com.thinkparity.model.parity.model.document.DocumentVersion,
	 *      com.thinkparity.model.parity.model.document.DocumentVersionContent)
	 * 
	 */
	public void createVersion(final Long versionId,
			final DocumentVersion version,
			final DocumentVersionContent versionContent) {
		final Session session = openSession();
		try {
			artifactIO.createVersion(session, versionId, version);

			final DocumentContent documentContent =
				versionContent.getDocumentContent();
			session.prepareStatement(SQL_CREATE_VERSION);
			session.setLong(1, version.getArtifactId());
			session.setLong(2, version.getVersionId());
			session.setBytes(3, documentContent.getContent());
			session.setString(4, "???");
			session.setString(5, documentContent.getChecksum());
			session.setInt(6, -1);
			if(1 != session.executeUpdate())
				throw new HypersonicException("Could not create version.");

			versionContent.setVersionId(version.getVersionId());

			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.DocumentIOHandler#delete(java.lang.Long)
	 * 
	 */
	public void delete(final Long documentId) throws HypersonicException {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_DELETE);
			session.setLong(1, documentId);
			if(1 != session.executeUpdate())
				throw new HypersonicException("Cannot delete.");
			
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
	 * @see com.thinkparity.model.parity.model.io.handler.DocumentIOHandler#deleteVersion(java.lang.Long,
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
	 * @see com.thinkparity.model.parity.model.io.handler.DocumentIOHandler#get(java.lang.Long)
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
	 * @see com.thinkparity.model.parity.model.io.handler.DocumentIOHandler#get(java.util.UUID)
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
	 * @see com.thinkparity.model.parity.model.io.handler.DocumentIOHandler#getContent(java.lang.Long)
	 * 
	 */
	public DocumentContent getContent(final Long documentId) {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_GET_CONTENT);
			session.setLong(1, documentId);
			session.executeQuery();

			if(session.nextResult()) { return extractDocumentContent(session); }
			else { return null; }
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.DocumentIOHandler#getLatestVersion(java.lang.Long)
	 * 
	 */
	public DocumentVersion getLatestVersion(final Long documentId) {
		final Session session = openSession();
		Long latestVersionId = null;
		try {
			latestVersionId =
				artifactIO.getLatestVersionId(session, documentId);
		}
		catch(final RuntimeException rx) {
			session.rollback();
			throw rx;
		}
		finally { session.close(); }
		return getVersion(documentId, latestVersionId);
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.DocumentIOHandler#getVersion(java.lang.Long,
	 *      java.lang.Long)
	 * 
	 */
	public DocumentVersion getVersion(Long documentId, Long versionId) {
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
	 * @see com.thinkparity.model.parity.model.io.handler.DocumentIOHandler#getVersionContent(java.lang.Long,
	 *      java.lang.Long)
	 * 
	 */
	public DocumentVersionContent getVersionContent(final Long documentId,
			final Long versionId) {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_GET_DOCUMENT_VERSION);
			session.setLong(1, documentId);
			session.setLong(2, versionId);
			session.executeQuery();

			if(session.nextResult()) { return extractVersionContent(session); }
			else { return null; }
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.DocumentIOHandler#list()
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
	 * @see com.thinkparity.model.parity.model.io.handler.DocumentIOHandler#listVersions(java.lang.Long)
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
	 * @see com.thinkparity.model.parity.model.io.handler.DocumentIOHandler#update(com.thinkparity.model.parity.model.document.Document)
	 */
	public void update(final Document document) {
		logger.warn("Update is misleading.  Only updated on timestamp\flags are being set.");
		final Session session = openSession();
		try {
			artifactIO.setFlags(session, document.getId(), document.getFlags());

			session.prepareStatement(SQL_UPDATE);
			session.setCalendar(1, document.getUpdatedOn());
			session.setLong(2, document.getId());
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
	 * @see com.thinkparity.model.parity.model.io.handler.DocumentIOHandler#updateContent(com.thinkparity.model.parity.model.document.DocumentContent)
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

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.DocumentIOHandler#updateState(java.lang.Long,
	 *      com.thinkparity.model.parity.model.artifact.ArtifactState)
	 * 
	 */
	public void updateState(final Long documentId, final ArtifactState state)
			throws HypersonicException {
		final Session session = openSession();
		try {
			artifactIO.updateState(session, documentId, state);
			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	private Document extractDocument(final Session session) {
		final Document d = new Document();
		d.setCreatedBy(session.getString("CREATED_BY"));
		d.setCreatedOn(session.getCalendar("CREATED_ON"));
		d.setId(session.getLong("ARTIFACT_ID"));
		d.setName(session.getString("ARTIFACT_NAME"));
		d.setState(session.getStateFromInteger("ARTIFACT_STATE_ID"));
		d.setType(session.getTypeFromInteger("ARTIFACT_TYPE_ID"));
		d.setUniqueId(session.getUniqueId("ARTIFACT_UNIQUE_ID"));
		d.setUpdatedBy(session.getString("UPDATED_BY"));
		d.setUpdatedOn(session.getCalendar("UPDATED_ON"));

		d.setFlags(artifactIO.getFlags(d.getId()));

		final ArtifactRemoteInfo remoteInfo = new ArtifactRemoteInfo();
		remoteInfo.setUpdatedBy(session.getQualifiedUsername("REMOTE_UPDATED_BY"));
		remoteInfo.setUpdatedOn(session.getCalendar("REMOTE_UPDATED_ON"));
		d.setRemoteInfo(remoteInfo);
		return d;
	}

	/**
	 * Extract the document content from the session.
	 * @param session The session.
	 * @return The document content.
	 */
	private DocumentContent extractDocumentContent(final Session session) {
		final DocumentContent dc = new DocumentContent();
		dc.setChecksum(session.getString("CONTENT_CHECKSUM"));
		dc.setContent(session.getBytes("CONTENT"));
		dc.setDocumentId(session.getLong("ARTIFACT_ID"));
		return dc;
	}

	private DocumentVersion extractVersion(final Session session) {
		final DocumentVersion dv = new DocumentVersion();
		dv.setArtifactId(session.getLong("ARTIFACT_ID"));
		dv.setArtifactType(session.getTypeFromString("ARTIFACT_TYPE"));
		dv.setArtifactUniqueId(session.getUniqueId("ARTIFACT_UNIQUE_ID"));
		dv.setCreatedBy(session.getString("CREATED_BY"));
		dv.setCreatedOn(session.getCalendar("CREATED_ON"));
		dv.setName(session.getString("ARTIFACT_NAME"));
		dv.setUpdatedBy(session.getString("UPDATED_BY"));
		dv.setUpdatedOn(session.getCalendar("UPDATED_ON"));
		dv.setVersionId(session.getLong("ARTIFACT_VERSION_ID"));

		dv.setMetaData(
				getVersionMetaData(dv.getArtifactId(), dv.getVersionId()));
		return dv;
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
		dvc.setDocumentContent(extractDocumentContent(session));
		dvc.setDocumentId(session.getLong("ARTIFACT_ID"));
		dvc.setVersionId(session.getLong("ARTIFACT_VERSION_ID"));
		return dvc;
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
	private Properties getVersionMetaData(final Long artifactId,
			final Long versionId) {
		final Session session = openSession();
		try {
			return artifactIO.getVersionMetaData(session, artifactId, versionId);
		}
		finally { session.close(); }
	}


}
