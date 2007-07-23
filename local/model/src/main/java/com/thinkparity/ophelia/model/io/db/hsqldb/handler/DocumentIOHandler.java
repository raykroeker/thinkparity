/*
 * Feb 8, 2006
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.sql.DataSource;

import com.thinkparity.codebase.io.StreamOpener;

import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;

/**
 * <b>Title:</b>thinkParity OpheliaModel Document IO Handler Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.16
 */
public final class DocumentIOHandler extends AbstractIOHandler implements
		com.thinkparity.ophelia.model.io.handler.DocumentIOHandler {

    /** Sql to create a document. */
	private static final String SQL_CREATE =
		new StringBuffer("insert into DOCUMENT (DOCUMENT_ID) ")
		.append("values (?)")
		.toString();

    /** Sql to create a document version. */
	private static final String SQL_CREATE_VERSION =
        new StringBuffer("insert into DOCUMENT_VERSION ")
		.append("(DOCUMENT_ID,DOCUMENT_VERSION_ID,CONTENT,CONTENT_SIZE,")
        .append("CONTENT_CHECKSUM,CHECKSUM_ALGORITHM) ")
		.append("values (?,?,?,?,?,?)")
		.toString();

    private static final String SQL_DELETE =
		new StringBuffer("delete from DOCUMENT ")
		.append("where DOCUMENT_ID=?")
		.toString();

    private static final String SQL_DELETE_VERSION =
		new StringBuffer("delete from DOCUMENT_VERSION ")
		.append("where DOCUMENT_ID=? and DOCUMENT_VERSION_ID=?")
		.toString();

    /** Sql to read a document version. */
    private static final String SQL_GET_VERSION =
		new StringBuffer("select DOCUMENT_ID,DOCUMENT_VERSION_ID,")
		.append("ARTIFACT_NAME,ARTIFACT_TYPE,ARTIFACT_UNIQUE_ID,")
		.append("CONTENT_CHECKSUM,CHECKSUM_ALGORITHM,CONTENT_SIZE,")
        .append("UC.JABBER_ID CREATED_BY,CREATED_ON,UU.JABBER_ID UPDATED_BY,UPDATED_ON,")
        .append("AV.COMMENT,AV.FLAGS,AV.NAME ")
		.append("from DOCUMENT_VERSION DV ")
        .append("inner join ARTIFACT_VERSION AV on DV.DOCUMENT_ID = AV.ARTIFACT_ID ")
        .append("and DV.DOCUMENT_VERSION_ID = AV.ARTIFACT_VERSION_ID ")
        .append("inner join PARITY_USER UC on AV.CREATED_BY=UC.USER_ID ")
        .append("inner join PARITY_USER UU on AV.UPDATED_BY=UU.USER_ID ")
		.append("where DV.DOCUMENT_ID=? and DV.DOCUMENT_VERSION_ID=?")
		.toString();

    private static final String SQL_LIST_VERSIONS =
		new StringBuffer("select DOCUMENT_ID,DOCUMENT_VERSION_ID,")
		.append("ARTIFACT_NAME,ARTIFACT_TYPE,ARTIFACT_UNIQUE_ID,")
		.append("CONTENT_CHECKSUM,CHECKSUM_ALGORITHM,CONTENT_SIZE,")
        .append("UC.JABBER_ID CREATED_BY,CREATED_ON,UU.JABBER_ID UPDATED_BY,")
        .append("UPDATED_ON,AV.COMMENT,AV.FLAGS,AV.NAME ")
		.append("from DOCUMENT_VERSION DV ")
        .append("inner join ARTIFACT_VERSION AV on DV.DOCUMENT_ID=AV.ARTIFACT_ID ")
        .append("and DV.DOCUMENT_VERSION_ID=AV.ARTIFACT_VERSION_ID ")
        .append("inner join PARITY_USER UC on AV.CREATED_BY=UC.USER_ID ")
        .append("inner join PARITY_USER UU on AV.UPDATED_BY=UU.USER_ID ")
		.append("where DV.DOCUMENT_ID=? ")
		.append("order by DV.DOCUMENT_VERSION_ID asc")
		.toString();

	/** Sql to open a stream to the document version's content. */
    private static final String SQL_OPEN_STREAM =
            new StringBuffer("select DV.CONTENT ")
            .append("from DOCUMENT_VERSION DV ")
            .append("where DV.DOCUMENT_ID=? and DV.DOCUMENT_VERSION_ID=?")
            .toString();

    /** Sql to read documents. */
	private static final String SQL_READ =
        new StringBuilder("select A.ARTIFACT_ID,A.ARTIFACT_NAME,")
        .append("A.ARTIFACT_STATE_ID,A.ARTIFACT_TYPE_ID,A.ARTIFACT_UNIQUE_ID,")
        .append("UC.JABBER_ID CREATED_BY,A.CREATED_ON,UU.JABBER_ID UPDATED_BY,")
        .append("A.FLAGS,A.UPDATED_ON ")
        .append("from DOCUMENT D ")
        .append("inner join ARTIFACT A on D.DOCUMENT_ID=A.ARTIFACT_ID ")
        .append("inner join PARITY_USER UC on A.CREATED_BY=UC.USER_ID ")
        .append("inner join PARITY_USER UU on A.UPDATED_BY=UU.USER_ID ")
        .append("order by A.ARTIFACT_ID asc")
		.toString();

	/** Sql to read a document by its primary key. */
    private static final String SQL_READ_PK =
		new StringBuilder("select A.ARTIFACT_ID,A.ARTIFACT_NAME,")
		.append("A.ARTIFACT_STATE_ID,A.ARTIFACT_TYPE_ID,A.ARTIFACT_UNIQUE_ID,")
		.append("UC.JABBER_ID CREATED_BY,A.CREATED_ON,UU.JABBER_ID UPDATED_BY,")
        .append("A.FLAGS,A.UPDATED_ON ")
		.append("from DOCUMENT D ")
        .append("inner join ARTIFACT A on D.DOCUMENT_ID=A.ARTIFACT_ID ")
        .append("inner join PARITY_USER UC on A.CREATED_BY=UC.USER_ID ")
        .append("inner join PARITY_USER UU on A.UPDATED_BY=UU.USER_ID ")
		.append("where A.ARTIFACT_ID=?")
		.toString();

    /** Sql to read a document by its unique index. */
    private static final String SQL_READ_UK =
        new StringBuilder("select A.ARTIFACT_ID,A.ARTIFACT_NAME,")
        .append("A.ARTIFACT_STATE_ID,A.ARTIFACT_TYPE_ID,A.ARTIFACT_UNIQUE_ID,")
        .append("UC.JABBER_ID CREATED_BY,A.CREATED_ON,UU.JABBER_ID UPDATED_BY,")
        .append("A.FLAGS,A.UPDATED_ON ")
        .append("from DOCUMENT D ")
        .append("inner join ARTIFACT A on D.DOCUMENT_ID=A.ARTIFACT_ID ")
        .append("inner join PARITY_USER UC on A.CREATED_BY=UC.USER_ID ")
        .append("inner join PARITY_USER UU on A.UPDATED_BY=UU.USER_ID ")
		.append("where A.ARTIFACT_UNIQUE_ID=?")
		.toString();

    /** Sql to read the version's size. */
    private static final String SQL_READ_VERSION_SIZE =
        new StringBuffer("select DV.CONTENT_SIZE ")
        .append("from DOCUMENT_VERSION DV ")
        .append("inner join ARTIFACT_VERSION AV on DV.DOCUMENT_ID = AV.ARTIFACT_ID ")
        .append("and DV.DOCUMENT_VERSION_ID = AV.ARTIFACT_VERSION_ID ")
        .append("where DV.DOCUMENT_ID=? and DV.DOCUMENT_VERSION_ID=?")
        .toString();

    private static final String SQL_UPDATE =
		new StringBuffer("update ARTIFACT ")
		.append("set UPDATED_ON=?,ARTIFACT_NAME=?,ARTIFACT_STATE_ID=? ")
		.append("where ARTIFACT_ID=?")
		.toString();

	/** Generic artifact io. */
	private final ArtifactIOHandler artifactIO;

	/**
     * Create a DocumentHandler.
     * 
     * @param dataSource
     *            An sql <code>DataSource</code>.
     */
	public DocumentIOHandler(final DataSource dataSource) {
		super(dataSource);
		this.artifactIO = new ArtifactIOHandler(dataSource);
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
		} finally {
            session.close();
        }
	}

    /**
     * @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#createVersion(com.thinkparity.codebase.model.document.DocumentVersion,
     *      java.io.InputStream, java.lang.Integer)
     * 
     */
	public void createVersion(final DocumentVersion version,
            final InputStream content, final Integer bufferSize) {
		final Session session = openSession();
		try {
			artifactIO.createVersion(session, version);

			session.prepareStatement(SQL_CREATE_VERSION);
			session.setLong(1, version.getArtifactId());
			session.setLong(2, version.getVersionId());
            session.setBinaryStream(3, content, version.getSize(), bufferSize);
            session.setLong(4, version.getSize());
			session.setString(5, version.getChecksum());
			session.setString(6, version.getChecksumAlgorithm());
			if(1 != session.executeUpdate())
                throw new HypersonicException("Could not create document version.");

			version.setVersionId(version.getVersionId());
		} finally {
            session.close();
		}
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
			artifactIO.delete(session, documentId);
		} finally {
            session.close();
		}
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
		} finally {
            session.close();
		}
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
			if (session.nextResult()) {
                return extractVersion(session);
			} else {
                return null;
			}
		} finally {
            session.close();
		}
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
		} finally {
            session.close();
		}
	}

	/**
     * @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#openStream(java.lang.Long,
     *      java.lang.Long, com.thinkparity.codebase.io.StreamOpener)
     * 
     */
    public void openStream(final Long documentId, final Long versionId,
            final StreamOpener opener) throws IOException {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_OPEN_STREAM);
            session.setLong(1, documentId);
            session.setLong(2, versionId);
            session.executeQuery();
            if (session.nextResult()) {
                final InputStream stream = session.getBlob("CONTENT");
                try {
                    opener.open(stream);
                } finally {
                    stream.close();
                }
            } else {
                opener.open(null);
            }
        } finally {
            session.close();
        }
    }

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#read()
	 */
	public List<Document> read() {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_READ);
			session.executeQuery();
			final List<Document> documents = new LinkedList<Document>();
			while(session.nextResult()) {
				documents.add(extractDocument(session));
			}
			return documents;
		} finally {
            session.close();
		}
	}

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#read(java.lang.Long)
	 * 
	 */
	public Document read(final Long documentId) {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_READ_PK);
			session.setLong(1, documentId);
			session.executeQuery();
			if (session.nextResult()) {
                return extractDocument(session);
			} else {
                return null;
			}
		} finally {
            session.close();
		}
	}

    /**
	 * @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#read(java.util.UUID)
	 */
	public Document read(final UUID uniqueId) {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_READ_UK);
			session.setUniqueId(1, uniqueId);
			session.executeQuery();
			if (session.nextResult()) {
                return extractDocument(session);
			} else {
                return null;
			}
		} finally {
            session.close();
		}
			
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
		} finally {
            session.close();
		}
	}

	/**
     * @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#readVersionSize(java.lang.Long,
     *      java.lang.Long)
     */
    public Long readVersionSize(final Long documentId, final Long versionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_VERSION_SIZE);
            session.setLong(1, documentId);
            session.setLong(2, versionId);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getLong("CONTENT_SIZE");
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

	/**
     * @see com.thinkparity.ophelia.model.io.handler.DocumentIOHandler#update(com.thinkparity.codebase.model.document.Document)
     * 
     */
	public void update(final Document document) {
		logger.logWarning("Update is misleading.  Only updated on, name, state, and flag information is being set.");
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_UPDATE);
			session.setCalendar(1, document.getUpdatedOn());
            session.setString(2, document.getName());
            session.setStateAsInteger(3, document.getState());
			session.setLong(4, document.getId());
			if(1 != session.executeUpdate())
				throw new HypersonicException("Could not update document.");
		} finally {
            session.close();
		}
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
		d.setCreatedBy(session.getQualifiedUsername("CREATED_BY"));
		d.setCreatedOn(session.getCalendar("CREATED_ON"));
		d.setFlags(session.getArtifactFlags("FLAGS"));
		d.setId(session.getLong("ARTIFACT_ID"));
		d.setName(session.getString("ARTIFACT_NAME"));
		d.setState(session.getStateFromInteger("ARTIFACT_STATE_ID"));
		d.setType(session.getTypeFromInteger("ARTIFACT_TYPE_ID"));
		d.setUniqueId(session.getUniqueId("ARTIFACT_UNIQUE_ID"));
		d.setUpdatedBy(session.getQualifiedUsername("UPDATED_BY"));
		d.setUpdatedOn(session.getCalendar("UPDATED_ON"));
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
		dv.setArtifactName(session.getString("ARTIFACT_NAME"));
		dv.setArtifactType(session.getTypeFromString("ARTIFACT_TYPE"));
		dv.setArtifactUniqueId(session.getUniqueId("ARTIFACT_UNIQUE_ID"));
		dv.setChecksum(session.getString("CONTENT_CHECKSUM"));
        dv.setChecksumAlgorithm(session.getString("CHECKSUM_ALGORITHM"));
        dv.setComment(session.getString("COMMENT"));
		dv.setCreatedBy(session.getQualifiedUsername("CREATED_BY"));
		dv.setCreatedOn(session.getCalendar("CREATED_ON"));
        dv.setFlags(session.getArtifactVersionFlags("FLAGS"));
        dv.setName(session.getString("NAME"));
        dv.setSize(session.getLong("CONTENT_SIZE"));
		dv.setUpdatedBy(session.getQualifiedUsername("UPDATED_BY"));
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
		} finally {
            session.close();
		}
	}
}
