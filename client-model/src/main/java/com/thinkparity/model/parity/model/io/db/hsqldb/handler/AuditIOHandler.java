/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.io.db.hsqldb.handler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.audit.AuditEventType;
import com.thinkparity.model.parity.model.audit.event.*;
import com.thinkparity.model.parity.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.model.parity.model.io.db.hsqldb.Session;
import com.thinkparity.model.parity.model.io.md.MetaData;
import com.thinkparity.model.parity.model.io.md.MetaDataType;

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

	/**
	 * Sql to read the audit event.
	 * 
	 */
	private static final String SQL_READ_BY_ARTIFACT_ID =
		new StringBuffer("select AA.ARTIFACT_AUDIT_ID,AA.ARTIFACT_AUDIT_TYPE_ID,")
		.append("AA.ARTIFACT_ID,AA.CREATED_ON,AA.CREATED_BY,")
		.append("AAV.ARTIFACT_VERSION_ID ")
		.append("from ARTIFACT_AUDIT AA ")
		.append("inner join ARTIFACT_AUDIT_TYPE AAT on ")
		.append("AA.ARTIFACT_AUDIT_TYPE_ID=AAT.ARTIFACT_AUDIT_TYPE_ID ")
		.append("left join ARTIFACT_AUDIT_VERSION AAV on ")
		.append("AA.ARTIFACT_AUDIT_ID=AAV.ARTIFACT_AUDIT_ID ")
		.append("where AA.ARTIFACT_ID=?")
		.toString();

	private static final String SQL_READ_META_DATA =
		new StringBuffer("select MD.META_DATA_ID,MD.META_DATA_TYPE_ID,MD.KEY,")
		.append("MD.VALUE ")
		.append("from ARTIFACT_AUDIT AA inner join ARTIFACT_AUDIT_META_DATA AAMD ")
		.append("on AA.ARTIFACT_AUDIT_ID = AAMD.ARTIFACT_AUDIT_ID ")
		.append("inner join META_DATA MD on AAMD.META_DATA_ID = MD.META_DATA_ID ")
		.append("where AA.ARTIFACT_AUDIT_ID=? and MD.KEY=?")
		.toString();

    /** User io. */
    private final UserIOHandler userIO;

    /**
	 * Create an AuditIOHandler.
	 * 
	 */
	public AuditIOHandler() {
        super();
        this.userIO = new UserIOHandler();
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.AuditIOHandler#audit(com.thinkparity.model.parity.model.audit.event.ArchiveEvent)
	 * 
	 */
	public void audit(final ArchiveEvent event)
            throws HypersonicException {
		final Session session = openSession();
		try {
			audit(session, event);
			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}


	/**
	 * @see com.thinkparity.model.parity.model.io.handler.AuditIOHandler#audit(com.thinkparity.model.parity.model.audit.event.CloseEvent)
	 * 
	 */
	public void audit(final CloseEvent event) throws HypersonicException {
		final Session session = openSession();
		try {
			audit(session, event);

			auditMetaData(session, event,
					MetaDataType.USER_ID, MetaDataKey.CLOSED_BY, event.getClosedBy().getLocalId());

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
	public void audit(final CreateEvent event) throws HypersonicException {
		final Session session = openSession();
		try {
			audit(session, event);
			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.AuditIOHandler#audit(com.thinkparity.model.parity.model.audit.event.KeyRequestDeniedEvent)
	 * 
	 */
	public void audit(final KeyRequestDeniedEvent event) throws HypersonicException {
		final Session session = openSession();
		try {
			audit(session, event);

			auditMetaData(session, event,
					MetaDataType.USER_ID, MetaDataKey.DENIED_BY, event.getDeniedBy().getLocalId());
			
			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}


	/**
	 * @see com.thinkparity.model.parity.model.io.handler.AuditIOHandler#audit(com.thinkparity.model.parity.model.audit.event.KeyResponseDeniedEvent)
	 */
	public void audit(final KeyResponseDeniedEvent event) throws HypersonicException {
		final Session session = openSession();
		try {
			audit(session, event);

			auditMetaData(session, event,
					MetaDataType.USER_ID, MetaDataKey.REQUESTED_BY, event.getRequestedBy().getLocalId());

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
	public void audit(final ReceiveEvent event) throws HypersonicException {
		final Session session = openSession();
		try {
			audit(session, event);
			auditVersion(session, event, event.getArtifactVersionId());

			auditMetaData(session, event,
					MetaDataType.USER_ID, MetaDataKey.RECEIVED_FROM, event.getReceivedFrom().getLocalId());
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
	public void audit(final ReceiveKeyEvent event) throws HypersonicException {
		final Session session = openSession();
		try {
			audit(session, event);

			auditMetaData(session, event,
					MetaDataType.USER_ID, MetaDataKey.RECEIVED_FROM, event.getReceivedFrom().getLocalId());

			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.AuditIOHandler#audit(com.thinkparity.model.parity.model.audit.event.RequestKeyEvent)
	 */
	public void audit(final RequestKeyEvent event) throws HypersonicException {
		final Session session = openSession();
		try {
			audit(session, event);

			auditMetaData(session, event,
					MetaDataType.USER_ID, MetaDataKey.REQUESTED_BY, event.getRequestedBy().getLocalId());

			auditMetaData(session, event,
					MetaDataType.USER_ID, MetaDataKey.REQUESTED_FROM, event.getRequestedFrom().getLocalId());

			session.commit();
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
	public void audit(final SendEvent event) throws HypersonicException {
		final Session session = openSession();
		try {
			audit(session, event);
			auditVersion(session, event, event.getArtifactVersionId());

			auditMetaData(session, event,
					MetaDataType.USER_ID, MetaDataKey.SENT_TO, event.getSentTo().getLocalId());

			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
     * @see com.thinkparity.model.parity.model.io.handler.AuditIOHandler#audit(com.thinkparity.model.parity.model.audit.event.SendEventConfirmation)
     */
    public void audit(final SendEventConfirmation event)
            throws HypersonicException {
        final Session session = openSession();
        try {
            audit(session, event);

            auditMetaData(session, event,
                    MetaDataType.USER_ID, MetaDataKey.CONFIRMED_BY, event.getConfirmedBy().getLocalId());

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
	public void audit(final SendKeyEvent event) throws HypersonicException {
		final Session session = openSession();
		try {
			audit(session, event);
			auditVersion(session, event, event.getArtifactVersionId());

			auditMetaData(session, event,
                    MetaDataType.USER_ID, MetaDataKey.SENT_TO, event.getSentTo().getLocalId());

			session.commit();
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

			session.prepareStatement(SQL_DELETE_AUDIT_META_DATA);
			for(final Long metaDataId : metaDataIds) {
				session.setLong(1, metaDataId);
				if(1 != session.executeUpdate())
					throw new HypersonicException("Could not delete audit meta data.");
			}

			for(final Long metaDataId : metaDataIds)
				getMetaDataIO().delete(session, metaDataId);

			session.prepareStatement(SQL_DELETE_AUDIT_VERSION);
			session.setLong(1, artifactId);
			session.executeUpdate();

			session.prepareStatement(SQL_DELETE_AUDIT);
			session.setLong(1, artifactId);
			if(auditIds.length != session.executeUpdate())
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
	 * @see com.thinkparity.model.parity.model.io.handler.AuditIOHandler#list(java.lang.Long)
	 */
	public List<AuditEvent> list(Long artifactId) throws HypersonicException {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_READ_BY_ARTIFACT_ID);
			session.setLong(1, artifactId);
			session.executeQuery();
			final List<AuditEvent> events = new LinkedList<AuditEvent>();
			while(session.nextResult()) { events.add(extract(session)); }
			return events;
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
			final AuditEvent auditEvent) {
		session.prepareStatement(SQL_AUDIT);
		session.setTypeAsInteger(1, auditEvent.getType());
		session.setLong(2, auditEvent.getArtifactId());
		session.setCalendar(3, auditEvent.getCreatedOn());
		session.setLong(4, auditEvent.getCreatedBy().getLocalId());
		if(1 != session.executeUpdate())
			throw new HypersonicException("Could not create audit.");

		auditEvent.setId(session.getIdentity());
	}

	private void auditMetaData(final Session session,
			final AuditEvent auditEvent, final MetaDataType metaDataType,
			final MetaDataKey metaDataKey, final Object metaDataValue) {
		final Long metaDataId = getMetaDataIO().create(session, metaDataType,
				metaDataKey.toString(), metaDataValue);

		session.prepareStatement(SQL_AUDIT_META_DATA);
		session.setLong(1, auditEvent.getId());
		session.setLong(2, metaDataId);
		if(1 != session.executeUpdate())
			throw new HypersonicException("Could not audit meta data.");
	}

	private void auditVersion(final Session session,
			final AuditEvent auditEvent, final Long versionId) {
		session.prepareStatement(SQL_AUDIT_VERSION);
		session.setLong(1, auditEvent.getId());
		session.setLong(2, auditEvent.getArtifactId());
		session.setLong(3, versionId);
		if(1 != session.executeUpdate())
			throw new HypersonicException("Could not audit version.");
	}

	/**
	 * Extract the various audit events from the session.
	 * 
	 * @param session
	 *            The database session.
	 * @return The audit event.
	 */
	private AuditEvent extract(final Session session) {
		final AuditEventType eventType = session.getAuditEventTypeFromInteger("ARTIFACT_AUDIT_TYPE_ID");
		switch(eventType) {
		case ARCHIVE:
			return extractArchive(session);
		case CLOSE:
			return extractClose(session);
        case SEND_CONFIRMATION:
            return extractSendConfirmation(session);
		case CREATE:
			return extractCreate(session);
		case KEY_RESPONSE_DENIED:
			return extractKeyResponseDenied(session);
		case KEY_REQUEST_DENIED:
			return extractKeyRequestDenied(session);
		case RECEIVE:
			return extractReceive(session);
		case RECEIVE_KEY:
			return extractReceiveKey(session);
		case REQUEST_KEY:
			return extractRequestKey(session);
		case SEND:
			return extractSend(session);
		case SEND_KEY:
			return extractSendKey(session);
		default:
			throw Assert.createUnreachable("Unknown event type:  " + eventType);
		}
	}
	private ArchiveEvent extractArchive(final Session session) {
		final ArchiveEvent event = new ArchiveEvent();
        event.setArtifactId(session.getLong("ARTIFACT_ID"));
        event.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
        event.setCreatedOn(session.getCalendar("CREATED_ON"));
        event.setId(session.getLong("ARTIFACT_AUDIT_ID"));
        event.setType(session.getAuditEventTypeFromInteger("ARTIFACT_AUDIT_TYPE_ID"));

        return event;
	}

    private CloseEvent extractClose(final Session session) {
		final CloseEvent closeEvent = new CloseEvent();
		closeEvent.setArtifactId(session.getLong("ARTIFACT_ID"));
		closeEvent.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
		closeEvent.setCreatedOn(session.getCalendar("CREATED_ON"));
		closeEvent.setId(session.getLong("ARTIFACT_AUDIT_ID"));
		closeEvent.setType(session.getAuditEventTypeFromInteger("ARTIFACT_AUDIT_TYPE_ID"));

		final MetaData[] metaData = readMetaData(closeEvent.getId(), MetaDataKey.CLOSED_BY);
		closeEvent.setClosedBy(userIO.read((Long) metaData[0].getValue()));

		return closeEvent;
	}

    private SendEventConfirmation extractSendConfirmation(final Session session) {
        final SendEventConfirmation event = new SendEventConfirmation();
        event.setArtifactId(session.getLong("ARTIFACT_ID"));
        event.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
        event.setCreatedOn(session.getCalendar("CREATED_ON"));
        event.setId(session.getLong("ARTIFACT_AUDIT_ID"));
        event.setType(session.getAuditEventTypeFromInteger("ARTIFACT_AUDIT_TYPE_ID"));

        final MetaData[] metaData = readMetaData(event.getId(), MetaDataKey.CONFIRMED_BY);
        event.setConfirmedBy(userIO.read((Long) metaData[0].getValue()));

        return event;
    }

    private CreateEvent extractCreate(final Session session) {
		final CreateEvent createEvent = new CreateEvent();
		createEvent.setArtifactId(session.getLong("ARTIFACT_ID"));
		createEvent.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
		createEvent.setCreatedOn(session.getCalendar("CREATED_ON"));
		createEvent.setId(session.getLong("ARTIFACT_AUDIT_ID"));
		createEvent.setType(session.getAuditEventTypeFromInteger("ARTIFACT_AUDIT_TYPE_ID"));
		return createEvent;
	}

	private KeyRequestDeniedEvent extractKeyRequestDenied(final Session session) {
		final KeyRequestDeniedEvent event = new KeyRequestDeniedEvent();
		event.setArtifactId(session.getLong("ARTIFACT_ID"));
		event.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
		event.setCreatedOn(session.getCalendar("CREATED_ON"));
		event.setId(session.getLong("ARTIFACT_AUDIT_ID"));
		event.setType(session.getAuditEventTypeFromInteger("ARTIFACT_AUDIT_TYPE_ID"));
		
		final MetaData[] metaData = readMetaData(event.getId(), MetaDataKey.DENIED_BY);
		event.setDeniedBy(userIO.read((Long) metaData[0].getValue()));

		return event;
	}

	private KeyResponseDeniedEvent extractKeyResponseDenied(final Session session) {
		final KeyResponseDeniedEvent event = new KeyResponseDeniedEvent();
		event.setArtifactId(session.getLong("ARTIFACT_ID"));
		event.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
		event.setCreatedOn(session.getCalendar("CREATED_ON"));
		event.setId(session.getLong("ARTIFACT_AUDIT_ID"));
		event.setType(session.getAuditEventTypeFromInteger("ARTIFACT_AUDIT_TYPE_ID"));

		final MetaData[] metaData = readMetaData(event.getId(), MetaDataKey.REQUESTED_BY);
		event.setRequestedBy(userIO.read((Long) metaData[0].getValue()));

		return event;
	}

	private ReceiveEvent extractReceive(final Session session) {
		final ReceiveEvent receiveEvent = new ReceiveEvent();
		receiveEvent.setArtifactId(session.getLong("ARTIFACT_ID"));
		receiveEvent.setArtifactVersionId(session.getLong("ARTIFACT_VERSION_ID"));
		receiveEvent.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
		receiveEvent.setCreatedOn(session.getCalendar("CREATED_ON"));
		receiveEvent.setId(session.getLong("ARTIFACT_AUDIT_ID"));
		receiveEvent.setType(session.getAuditEventTypeFromInteger("ARTIFACT_AUDIT_TYPE_ID"));

		final MetaData[] metaData = readMetaData(receiveEvent.getId(), MetaDataKey.RECEIVED_FROM);
		receiveEvent.setReceivedFrom(userIO.read((Long) metaData[0].getValue()));

		return receiveEvent;
	}

	private ReceiveKeyEvent extractReceiveKey(final Session session) {
		final ReceiveKeyEvent receiveKeyEvent = new ReceiveKeyEvent();
		receiveKeyEvent.setArtifactId(session.getLong("ARTIFACT_ID"));
		receiveKeyEvent.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
		receiveKeyEvent.setCreatedOn(session.getCalendar("CREATED_ON"));
		receiveKeyEvent.setId(session.getLong("ARTIFACT_AUDIT_ID"));
		receiveKeyEvent.setType(session.getAuditEventTypeFromInteger("ARTIFACT_AUDIT_TYPE_ID"));

		final MetaData[] metaData = readMetaData(receiveKeyEvent.getId(), MetaDataKey.RECEIVED_FROM);
		receiveKeyEvent.setReceivedFrom(userIO.read((Long) metaData[0].getValue()));

		return receiveKeyEvent;
	}

	private RequestKeyEvent extractRequestKey(final Session session) {
		final RequestKeyEvent event = new RequestKeyEvent();
		event.setArtifactId(session.getLong("ARTIFACT_ID"));
		event.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
		event.setCreatedOn(session.getCalendar("CREATED_ON"));
		event.setId(session.getLong("ARTIFACT_AUDIT_ID"));
		event.setType(session.getAuditEventTypeFromInteger("ARTIFACT_AUDIT_TYPE_ID"));

		MetaData[] metaData = readMetaData(event.getId(), MetaDataKey.REQUESTED_BY);
		event.setRequestedBy(userIO.read((Long) metaData[0].getValue()));

		metaData = readMetaData(event.getId(), MetaDataKey.REQUESTED_FROM);
		event.setRequestedFrom(userIO.read((Long) metaData[0].getValue()));

		return event;
	}

	private SendEvent extractSend(final Session session) {
		final SendEvent sendEvent = new SendEvent();
		sendEvent.setArtifactId(session.getLong("ARTIFACT_ID"));
		sendEvent.setArtifactVersionId(session.getLong("ARTIFACT_VERSION_ID"));
		sendEvent.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
		sendEvent.setCreatedOn(session.getCalendar("CREATED_ON"));
		sendEvent.setId(session.getLong("ARTIFACT_AUDIT_ID"));
		sendEvent.setType(session.getAuditEventTypeFromInteger("ARTIFACT_AUDIT_TYPE_ID"));

		final MetaData[] metaData = readMetaData(sendEvent.getId(), MetaDataKey.SENT_TO);
        sendEvent.setSentTo(userIO.read((Long) metaData[0].getValue()));

		return sendEvent;
	}

	private SendKeyEvent extractSendKey(final Session session) {
		final SendKeyEvent sendKeyEvent = new SendKeyEvent();
		sendKeyEvent.setArtifactId(session.getLong("ARTIFACT_ID"));
		sendKeyEvent.setArtifactVersionId(session.getLong("ARTIFACT_VERSION_ID"));
		sendKeyEvent.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
		sendKeyEvent.setCreatedOn(session.getCalendar("CREATED_ON"));
		sendKeyEvent.setId(session.getLong("ARTIFACT_AUDIT_ID"));
		sendKeyEvent.setType(session.getAuditEventTypeFromInteger("ARTIFACT_AUDIT_TYPE_ID"));

		final MetaData[] metaData = readMetaData(sendKeyEvent.getId(), MetaDataKey.SENT_TO);
		sendKeyEvent.setSentTo(userIO.read((Long) metaData[0].getValue()));

		return sendKeyEvent;
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

	private MetaData[] readMetaData(final Long auditEventId,
			final MetaDataKey metaDataKey) {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_READ_META_DATA);
			session.setLong(1, auditEventId);
			session.setTypeAsString(2, metaDataKey);
			session.executeQuery();
			final List<MetaData> metaData = new LinkedList<MetaData>();
			while(session.nextResult()) {
				metaData.add(extractMetaData(session));
			}
			return metaData.toArray(new MetaData[] {});
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

    private enum MetaDataKey { CLOSED_BY, CONFIRMED_BY, DENIED_BY, RECEIVED_BY, RECEIVED_FROM, REQUESTED_BY, REQUESTED_FROM, SENT_TO }
}
