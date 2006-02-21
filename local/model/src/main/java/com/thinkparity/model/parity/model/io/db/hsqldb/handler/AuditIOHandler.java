/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.io.db.hsqldb.handler;

import java.util.HashSet;
import java.util.Set;

import com.thinkparity.model.parity.model.audit.event.*;
import com.thinkparity.model.parity.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.model.parity.model.io.db.hsqldb.Session;
import com.thinkparity.model.parity.model.md.MetaDataType;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AuditIOHandler extends AbstractIOHandler implements
		com.thinkparity.model.parity.model.io.handler.AuditIOHandler {

	private static final String SQL_AUDIT =
		new StringBuffer("insert into ARTIFACT_AUDIT ")
		.append("(ARTIFACT_AUDIT_TYPE_ID,ARTIFACT_ID,CREATED_ON,CREATED_BY) ")
		.append("values (?,?,?,?)")
		.toString();

	private static final String SQL_AUDIT_META_DATA =
		new StringBuffer("insert into ARTIFACT_AUDIT_META_DATA ")
		.append("(ARTIFACT_AUDIT_ID,META_DATA_ID) ")
		.append("values (?,?)")
		.toString();

	private static final String SQL_AUDIT_VERSION =
		new StringBuffer("insert into ARTIFACT_AUDIT_VERSION ")
		.append("(ARTIFACT_AUDIT_ID,ARTIFACT_ID,ARTIFACT_VERSION_ID) ")
		.append("values (?,?,?)")
		.toString();

	private static final String SQL_DELETE_AUDIT =
		new StringBuffer("delete from ARTIFACT_AUDIT ")
		.append("where ARTIFACT_ID=?")
		.toString();

	private static final String SQL_DELETE_AUDIT_META_DATA =
		new StringBuffer("delete from ARTIFACT_AUDIT_META_DATA ")
		.append("where META_DATA_ID=?")
		.toString();

	private static final String SQL_DELETE_AUDIT_VERSION =
		new StringBuffer("delete from ARTIFACT_AUDIT_VERSION ")
		.append("where ARTIFACT_ID=?")
		.toString();

	private static final String SQL_LIST_AUDIT_IDS =
		new StringBuffer("select ARTIFACT_AUDIT_ID ")
		.append("from ARTIFACT_AUDIT ")
		.append("where ARTIFACT_ID=?")
		.toString();

	private static final String SQL_LIST_META_DATA_IDS =
		new StringBuffer("select META_DATA_ID ")
		.append("from ARTIFACT_AUDIT_META_DATA ")
		.append("where ARTIFACT_AUDIT_ID=?")
		.toString();

	private final MetaDataIOHandler metaDataIO;

	/**
	 * Create an AuditIOHandler.
	 * 
	 */
	public AuditIOHandler() {
		super();
		this.metaDataIO = new MetaDataIOHandler();
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.AuditIOHandler#audit(com.thinkparity.model.parity.model.audit.event.CloseEvent)
	 * 
	 */
	public void audit(final CloseEvent closeEvent) throws HypersonicException {
		final Session session = openSession();
		try {
			audit(session, closeEvent);
			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.AuditIOHandler#audit(com.thinkparity.model.parity.model.audit.event.CreateEvent)
	 * 
	 */
	public void audit(final CreateEvent createEvent) throws HypersonicException {
		final Session session = openSession();
		try {
			audit(session, createEvent);
			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.AuditIOHandler#audit(com.thinkparity.model.parity.model.audit.event.ReceiveEvent)
	 * 
	 */
	public void audit(final ReceiveEvent receiveEvent)
			throws HypersonicException {
		final Session session = openSession();
		try {
			audit(session, receiveEvent);
			auditVersion(session, receiveEvent, receiveEvent.getArtifactVersionId());

			auditMetaData(session, receiveEvent,
					MetaDataType.STRING, MetaDataKey.RECEIVED_FROM,
					receiveEvent.getReceivedFrom());
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.commit(); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.AuditIOHandler#audit(com.thinkparity.model.parity.model.audit.event.ReceiveKeyEvent)
	 * 
	 */
	public void audit(final ReceiveKeyEvent receiveKeyEvent)
			throws HypersonicException {
		final Session session = openSession();
		try {
			audit(session, receiveKeyEvent);
			auditVersion(session, receiveKeyEvent,
					receiveKeyEvent.getArtifactVersionId());
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.AuditIOHandler#audit(com.thinkparity.model.parity.model.audit.event.SendEvent)
	 * 
	 */
	public void audit(final SendEvent sendEvent) throws HypersonicException {
		final Session session = openSession();
		try {
			audit(session, sendEvent);
			auditVersion(session, sendEvent, sendEvent.getArtifactVersionId());

			for(final String sentTo : sendEvent.getSentTo()) {
				auditMetaData(session, sendEvent,
						MetaDataType.STRING, MetaDataKey.SENT_TO, sentTo);
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
	 * @see com.thinkparity.model.parity.model.io.handler.AuditIOHandler#audit(com.thinkparity.model.parity.model.audit.event.SendKeyEvent)
	 * 
	 */
	public void audit(final SendKeyEvent sendKeyEvent) throws HypersonicException {
		final Session session = openSession();
		try {
			audit(session, sendKeyEvent);
			auditVersion(session, sendKeyEvent, sendKeyEvent.getArtifactVersionId());

			auditMetaData(session, sendKeyEvent,
					MetaDataType.STRING, MetaDataKey.SENT_TO,
					sendKeyEvent.getSentTo());
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.AuditIOHandler#delete(java.lang.Long)
	 * 
	 */
	public void delete(final Long artifactId) throws HypersonicException {
		final Session session = openSession();
		try {
			final Long[] auditIds = listAuditIds(session, artifactId);
			final Long[] metaDataIds = listMetaDataIds(session, auditIds);

			metaDataIO.delete(session, metaDataIds);

			session.prepareStatement(SQL_DELETE_AUDIT_META_DATA);
			for(final Long metaDataId : metaDataIds) {
				session.setLong(1, metaDataId);
				if(1 != session.executeUpdate())
					throw new HypersonicException("Could not delete audit meta data.");
			}

			session.prepareStatement(SQL_DELETE_AUDIT_VERSION);
			session.setLong(1, artifactId);
			int rowsDeleted = session.executeUpdate();
			if(0 !=  rowsDeleted && 1 != rowsDeleted)
				throw new HypersonicException("Could not delete audit version.");

			session.prepareStatement(SQL_DELETE_AUDIT);
			session.setLong(1, artifactId);
			if(1 != session.executeUpdate())
				throw new HypersonicException("Could not delete audit.");

			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * Create the abstract audit event.
	 * 
	 * @param session
	 *            The database session.
	 * @param auditEvent
	 *            The audit event.
	 */
	private void audit(final Session session,
			final AbstractAuditEvent auditEvent) {
		session.prepareStatement(SQL_AUDIT);
		session.setTypeAsInteger(1, auditEvent.getType());
		session.setLong(2, auditEvent.getArtifactId());
		session.setCalendar(3, auditEvent.getCreatedOn());
		session.setString(4, auditEvent.getCreatedBy());
		if(1 != session.executeUpdate())
			throw new HypersonicException("Could not create audit.");

		auditEvent.setId(session.getIdentity());
	}

	private void auditMetaData(final Session session,
			final AbstractAuditEvent auditEvent,
			final MetaDataType metaDataType, final MetaDataKey metaDataKey,
			final Object metaDataValue) {
		final Long metaDataId = 
			metaDataIO.create(session, metaDataType, metaDataKey, metaDataValue);

		session.prepareStatement(SQL_AUDIT_META_DATA);
		session.setLong(1, auditEvent.getId());
		session.setLong(2, metaDataId);
		if(1 != session.executeUpdate())
			throw new HypersonicException("Could not audit meta data.");
	}

	private void auditVersion(final Session session,
			final AbstractAuditEvent auditEvent, final Long versionId) {
		session.prepareStatement(SQL_AUDIT_VERSION);
		session.setLong(1, auditEvent.getId());
		session.setLong(2, auditEvent.getArtifactId());
		session.setLong(3, versionId);
		if(1 != session.executeUpdate())
			throw new HypersonicException("Could not audit version.");
	}

	private Long[] listAuditIds(final Session session, final Long artifactId)
			throws HypersonicException {
		session.prepareStatement(SQL_LIST_AUDIT_IDS);
		session.setLong(1, artifactId);
		session.executeQuery();
		final Set<Long> auditIds = new HashSet<Long>();
		while(session.nextResult()) {
			auditIds.add(session.getLong("ARTIFACT_AUDIT_ID"));
		}
		return auditIds.toArray(new Long[] {});
	}

	private Long[] listMetaDataIds(final Session session, final Long[] auditIds)
			throws HypersonicException {
		session.prepareStatement(SQL_LIST_META_DATA_IDS);
		final Set<Long> metaDataIds = new HashSet<Long>();
		for(final Long auditId : auditIds) {
			session.setLong(1, auditId);
			session.executeQuery();
			while(session.nextResult()) {
				metaDataIds.add(session.getLong("META_DATA_ID"));
			}
		}
		return metaDataIds.toArray(new Long[] {});
	}

	private enum MetaDataKey { RECEIVED_FROM, SENT_TO }
}
