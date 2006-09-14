/*
 * Created On: Feb 21, 2006
 * $Id$
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.model.audit.AuditEventType;
import com.thinkparity.ophelia.model.audit.event.*;
import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;
import com.thinkparity.ophelia.model.io.db.hsqldb.SessionManager;
import com.thinkparity.ophelia.model.io.md.MetaData;
import com.thinkparity.ophelia.model.io.md.MetaDataType;

/**
 * The audit io handler reads\writes information to the audit tables in the
 * hsql database.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AuditIOHandler extends AbstractIOHandler implements
		com.thinkparity.ophelia.model.io.handler.AuditIOHandler {

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
     * Create AuditIOHandler.
     * 
     * @param sessionManager
     *            A hypersonic <code>SessionManager</code>.
     */
	public AuditIOHandler(SessionManager sessionManager) {
        super(sessionManager);
        this.userIO = new UserIOHandler(sessionManager);
	}

	public void audit(final AddTeamMemberConfirmEvent event)
            throws HypersonicException {
        final Session session = openSession();
        try {
            audit(session, event);

            auditMetaData(session, event,
                    MetaDataType.USER_ID, MetaDataKey.TEAM_MEMBER, event.getTeamMember().getLocalId());

            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }


	public void audit(final AddTeamMemberEvent event) throws HypersonicException {
        final Session session = openSession();
        try {
            audit(session, event);

            auditMetaData(session, event,
                    MetaDataType.USER_ID, MetaDataKey.TEAM_MEMBER, event.getTeamMember().getLocalId());

            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.AuditIOHandler#audit(com.thinkparity.ophelia.model.audit.event.ArchiveEvent)
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
	 * @see com.thinkparity.ophelia.model.io.handler.AuditIOHandler#audit(com.thinkparity.ophelia.model.audit.event.CloseEvent)
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
	 * @see com.thinkparity.ophelia.model.io.handler.AuditIOHandler#audit(com.thinkparity.ophelia.model.audit.event.CreateEvent)
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
	 * @see com.thinkparity.ophelia.model.io.handler.AuditIOHandler#audit(com.thinkparity.ophelia.model.audit.event.CreateEvent)
	 * 
	 */
	public void audit(final CreateRemoteEvent event) throws HypersonicException {
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
	 * @see com.thinkparity.ophelia.model.io.handler.AuditIOHandler#audit(com.thinkparity.ophelia.model.audit.event.KeyRequestDeniedEvent)
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
	 * @see com.thinkparity.ophelia.model.io.handler.AuditIOHandler#audit(com.thinkparity.ophelia.model.audit.event.KeyResponseDeniedEvent)
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

	public void audit(final PublishEvent event) throws HypersonicException {
        final Session session = openSession();
        try {
            audit(session, event);
            auditVersion(session, event, event.getArtifactVersionId());

            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

	/**
     * @see com.thinkparity.ophelia.model.io.handler.AuditIOHandler#audit(com.thinkparity.ophelia.model.audit.event.ReactivateEvent)
     * 
     */
    public void audit(final ReactivateEvent event) throws HypersonicException {
        final Session session = openSession();
        try {
            audit(session, event);
            auditVersion(session, event, event.getArtifactVersionId());

            auditMetaData(session, event,
                    MetaDataType.USER_ID, MetaDataKey.REACTIVATED_BY, event.getReactivatedBy().getLocalId());
            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
	 * @see com.thinkparity.ophelia.model.io.handler.AuditIOHandler#audit(com.thinkparity.ophelia.model.audit.event.ReceiveEvent)
	 * 
	 */
	public void audit(final ReceiveEvent event) throws HypersonicException {
		final Session session = openSession();
		try {
			audit(session, event);
			auditVersion(session, event, event.getArtifactVersionId());

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
	 * @see com.thinkparity.ophelia.model.io.handler.AuditIOHandler#audit(com.thinkparity.ophelia.model.audit.event.ReceiveKeyEvent)
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

    
	/** @see com.thinkparity.ophelia.model.io.handler.AuditIOHandler#audit(com.thinkparity.ophelia.model.audit.event.RenameEvent) */
	public void audit(final RenameEvent event) throws HypersonicException {
		final Session session = openSession();
		try {
			audit(session, event);

            auditMetaData(session, event,
                MetaDataType.STRING, MetaDataKey.RENAMED_FROM, event.getFrom());
            auditMetaData(session, event,
                MetaDataType.STRING, MetaDataKey.RENAMED_TO, event.getTo());

			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.AuditIOHandler#audit(com.thinkparity.ophelia.model.audit.event.RequestKeyEvent)
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
     * @see com.thinkparity.ophelia.model.io.handler.AuditIOHandler#audit(com.thinkparity.ophelia.model.audit.event.SendConfirmEvent)
     */
    public void audit(final SendConfirmEvent event)
            throws HypersonicException {
        final Session session = openSession();
        try {
            audit(session, event);
            auditVersion(session, event, event.getArtifactVersionId());

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
	 * @see com.thinkparity.ophelia.model.io.handler.AuditIOHandler#audit(com.thinkparity.ophelia.model.audit.event.SendEvent)
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
	 * @see com.thinkparity.ophelia.model.io.handler.AuditIOHandler#audit(com.thinkparity.ophelia.model.audit.event.SendKeyEvent)
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
	 * @see com.thinkparity.ophelia.model.io.handler.AuditIOHandler#delete(java.lang.Long)
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
	 * @see com.thinkparity.ophelia.model.io.handler.AuditIOHandler#list(java.lang.Long)
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
        case ADD_TEAM_MEMBER:
            return extractAddTeamMember(session);
        case ADD_TEAM_MEMBER_CONFIRM:
            return extractAddTeamMemberConfirm(session);
		case ARCHIVE:
			return extractArchive(session);
		case CLOSE:
			return extractClose(session);
        case SEND_CONFIRM:
            return extractSendConfirm(session);
		case CREATE:
			return extractCreate(session);
        case CREATE_REMOTE:
            return extractCreateRemote(session);
		case KEY_RESPONSE_DENIED:
			return extractKeyResponseDenied(session);
		case KEY_REQUEST_DENIED:
			return extractKeyRequestDenied(session);
        case PUBLISH:
            return extractPublish(session);
        case REACTIVATE:
            return extractReactivate(session);
		case RECEIVE:
			return extractReceive(session);
		case RECEIVE_KEY:
			return extractReceiveKey(session);
        case RENAME:
            return extractRename(session);
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

	private AuditEvent extractAddTeamMember(final Session session) {
        final AddTeamMemberEvent event = new AddTeamMemberEvent();
        event.setArtifactId(session.getLong("ARTIFACT_ID"));
        event.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
        event.setCreatedOn(session.getCalendar("CREATED_ON"));
        event.setId(session.getLong("ARTIFACT_AUDIT_ID"));

        final MetaData[] metaData = readMetaData(event.getId(), MetaDataKey.TEAM_MEMBER);
        event.setTeamMember(userIO.read((Long) metaData[0].getValue()));

        return event;
    }

    private AuditEvent extractAddTeamMemberConfirm(final Session session) {
        final AddTeamMemberConfirmEvent event = new AddTeamMemberConfirmEvent();
        event.setArtifactId(session.getLong("ARTIFACT_ID"));
        event.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
        event.setCreatedOn(session.getCalendar("CREATED_ON"));
        event.setId(session.getLong("ARTIFACT_AUDIT_ID"));

        final MetaData[] metaData = readMetaData(event.getId(), MetaDataKey.TEAM_MEMBER);
        event.setTeamMember(userIO.read((Long) metaData[0].getValue()));

        return event;
    }

    private ArchiveEvent extractArchive(final Session session) {
		final ArchiveEvent event = new ArchiveEvent();
        event.setArtifactId(session.getLong("ARTIFACT_ID"));
        event.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
        event.setCreatedOn(session.getCalendar("CREATED_ON"));
        event.setId(session.getLong("ARTIFACT_AUDIT_ID"));

        return event;
	}

    private CloseEvent extractClose(final Session session) {
		final CloseEvent event = new CloseEvent();
		event.setArtifactId(session.getLong("ARTIFACT_ID"));
		event.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
		event.setCreatedOn(session.getCalendar("CREATED_ON"));
		event.setId(session.getLong("ARTIFACT_AUDIT_ID"));

		final MetaData[] metaData = readMetaData(event.getId(), MetaDataKey.CLOSED_BY);
		event.setClosedBy(userIO.read((Long) metaData[0].getValue()));

		return event;
	}

    private CreateEvent extractCreate(final Session session) {
		final CreateEvent event = new CreateEvent();
		event.setArtifactId(session.getLong("ARTIFACT_ID"));
		event.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
		event.setCreatedOn(session.getCalendar("CREATED_ON"));
		event.setId(session.getLong("ARTIFACT_AUDIT_ID"));
		return event;
	}

	private CreateRemoteEvent extractCreateRemote(final Session session) {
		final CreateRemoteEvent event = new CreateRemoteEvent();
		event.setArtifactId(session.getLong("ARTIFACT_ID"));
		event.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
		event.setCreatedOn(session.getCalendar("CREATED_ON"));
		event.setId(session.getLong("ARTIFACT_AUDIT_ID"));

        final MetaData[] metaData = readMetaData(event.getId(), MetaDataKey.RECEIVED_FROM);
        event.setReceivedFrom(userIO.read((Long) metaData[0].getValue()));

		return event;
	}

    private KeyRequestDeniedEvent extractKeyRequestDenied(final Session session) {
		final KeyRequestDeniedEvent event = new KeyRequestDeniedEvent();
		event.setArtifactId(session.getLong("ARTIFACT_ID"));
		event.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
		event.setCreatedOn(session.getCalendar("CREATED_ON"));
		event.setId(session.getLong("ARTIFACT_AUDIT_ID"));

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

		final MetaData[] metaData = readMetaData(event.getId(), MetaDataKey.REQUESTED_BY);
		event.setRequestedBy(userIO.read((Long) metaData[0].getValue()));

		return event;
	}

	private AuditEvent extractPublish(final Session session) {
        final PublishEvent event = new PublishEvent();
        event.setArtifactId(session.getLong("ARTIFACT_ID"));
        event.setArtifactVersionId(session.getLong("ARTIFACT_VERSION_ID"));
        event.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
        event.setCreatedOn(session.getCalendar("CREATED_ON"));
        event.setId(session.getLong("ARTIFACT_AUDIT_ID"));

        return event;
    }

    private ReactivateEvent extractReactivate(final Session session) {
        final ReactivateEvent event = new ReactivateEvent();
        event.setArtifactId(session.getLong("ARTIFACT_ID"));
        event.setArtifactVersionId(session.getLong("ARTIFACT_VERSION_ID"));
        event.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
        event.setCreatedOn(session.getCalendar("CREATED_ON"));
        event.setId(session.getLong("ARTIFACT_AUDIT_ID"));

        final MetaData[] metaData = readMetaData(event.getId(), MetaDataKey.REACTIVATED_BY);
        event.setReactivatedBy(userIO.read((Long) metaData[0].getValue()));

        return event;
    }
	private ReceiveEvent extractReceive(final Session session) {
		final ReceiveEvent event = new ReceiveEvent();
		event.setArtifactId(session.getLong("ARTIFACT_ID"));
		event.setArtifactVersionId(session.getLong("ARTIFACT_VERSION_ID"));
		event.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
		event.setCreatedOn(session.getCalendar("CREATED_ON"));
		event.setId(session.getLong("ARTIFACT_AUDIT_ID"));

		final MetaData[] metaData = readMetaData(event.getId(), MetaDataKey.RECEIVED_FROM);
		event.setReceivedFrom(userIO.read((Long) metaData[0].getValue()));

		return event;
	}

	private ReceiveKeyEvent extractReceiveKey(final Session session) {
		final ReceiveKeyEvent event = new ReceiveKeyEvent();
		event.setArtifactId(session.getLong("ARTIFACT_ID"));
		event.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
		event.setCreatedOn(session.getCalendar("CREATED_ON"));
		event.setId(session.getLong("ARTIFACT_AUDIT_ID"));

		final MetaData[] metaData = readMetaData(event.getId(), MetaDataKey.RECEIVED_FROM);
		event.setReceivedFrom(userIO.read((Long) metaData[0].getValue()));

		return event;
	}

    private AuditEvent extractRename(final Session session) {
        final RenameEvent event = new RenameEvent();
        event.setArtifactId(session.getLong("ARTIFACT_ID"));
        event.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
        event.setCreatedOn(session.getCalendar("CREATED_ON"));
        event.setId(session.getLong("ARTIFACT_AUDIT_ID"));

        MetaData[] metaData = readMetaData(event.getId(), MetaDataKey.RENAMED_FROM);
        event.setFrom((String) metaData[0].getValue());

        metaData = readMetaData(event.getId(), MetaDataKey.RENAMED_TO);
        event.setTo((String) metaData[0].getValue());

        return event;
    }

	private RequestKeyEvent extractRequestKey(final Session session) {
		final RequestKeyEvent event = new RequestKeyEvent();
		event.setArtifactId(session.getLong("ARTIFACT_ID"));
		event.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
		event.setCreatedOn(session.getCalendar("CREATED_ON"));
		event.setId(session.getLong("ARTIFACT_AUDIT_ID"));

		MetaData[] metaData = readMetaData(event.getId(), MetaDataKey.REQUESTED_BY);
		event.setRequestedBy(userIO.read((Long) metaData[0].getValue()));

		metaData = readMetaData(event.getId(), MetaDataKey.REQUESTED_FROM);
		event.setRequestedFrom(userIO.read((Long) metaData[0].getValue()));

		return event;
	}

	private SendEvent extractSend(final Session session) {
		final SendEvent event = new SendEvent();
		event.setArtifactId(session.getLong("ARTIFACT_ID"));
		event.setArtifactVersionId(session.getLong("ARTIFACT_VERSION_ID"));
		event.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
		event.setCreatedOn(session.getCalendar("CREATED_ON"));
		event.setId(session.getLong("ARTIFACT_AUDIT_ID"));

		final MetaData[] metaData = readMetaData(event.getId(), MetaDataKey.SENT_TO);
        event.setSentTo(userIO.read((Long) metaData[0].getValue()));

		return event;
	}

    /**
     * Extract the send confirm event.
     * 
     * @param session
     *            The db session.
     * @return The send confirm event.
     */
    private SendConfirmEvent extractSendConfirm(final Session session) {
        final SendConfirmEvent event = new SendConfirmEvent();
        event.setArtifactId(session.getLong("ARTIFACT_ID"));
        event.setArtifactVersionId(session.getLong("ARTIFACT_VERSION_ID"));
        event.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
        event.setCreatedOn(session.getCalendar("CREATED_ON"));
        event.setId(session.getLong("ARTIFACT_AUDIT_ID"));

        final MetaData[] metaData = readMetaData(event.getId(), MetaDataKey.CONFIRMED_BY);
        event.setConfirmedBy(userIO.read((Long) metaData[0].getValue()));

        return event;
    }

    private SendKeyEvent extractSendKey(final Session session) {
		final SendKeyEvent event = new SendKeyEvent();
		event.setArtifactId(session.getLong("ARTIFACT_ID"));
		event.setArtifactVersionId(session.getLong("ARTIFACT_VERSION_ID"));
		event.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
		event.setCreatedOn(session.getCalendar("CREATED_ON"));
		event.setId(session.getLong("ARTIFACT_AUDIT_ID"));

		final MetaData[] metaData = readMetaData(event.getId(), MetaDataKey.SENT_TO);
		event.setSentTo(userIO.read((Long) metaData[0].getValue()));

		return event;
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

    private enum MetaDataKey {
        CLOSED_BY, CONFIRMED_BY, DENIED_BY, REACTIVATED_BY, RECEIVED_BY,
        RECEIVED_FROM, RENAMED_FROM, RENAMED_TO, REQUESTED_BY, REQUESTED_FROM,
        SENT_TO, TEAM_MEMBER
    }
}
