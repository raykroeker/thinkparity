/*
 * Feb 24, 2006
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;
import com.thinkparity.ophelia.model.io.db.hsqldb.SessionManager;
import com.thinkparity.ophelia.model.io.md.MetaData;
import com.thinkparity.ophelia.model.io.md.MetaDataType;
import com.thinkparity.ophelia.model.message.ContactInvitationMessage;
import com.thinkparity.ophelia.model.message.ContactInvitationResponseMessage;
import com.thinkparity.ophelia.model.message.KeyRequestMessage;
import com.thinkparity.ophelia.model.message.KeyResponseMessage;
import com.thinkparity.ophelia.model.message.SystemMessage;
import com.thinkparity.ophelia.model.message.SystemMessageType;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SystemMessageIOHandler extends AbstractIOHandler implements
		com.thinkparity.ophelia.model.io.handler.SystemMessageIOHandler {

	/**
	 * Sql to create the main system message.
	 * 
	 */
	private static final String SQL_CREATE =
		new StringBuffer("insert into SYSTEM_MESSAGE ")
		.append("(SYSTEM_MESSAGE_TYPE_ID) ")
		.append("values (?)")
		.toString();

	/**
	 * Sql to create a system message relation ship.
	 * 
	 */
	private static final String SQL_CREATE_META_DATA =
		new StringBuffer("insert into SYSTEM_MESSAGE_META_DATA ")
		.append("(SYSTEM_MESSAGE_ID,META_DATA_ID) ")
		.append("values (?,?)")
		.toString();

	/**
	 * Sql to delete a system message.
	 * 
	 */
	private static final String SQL_DELETE_MESSAGE =
		new StringBuffer("delete from SYSTEM_MESSAGE ")
		.append("where SYSTEM_MESSAGE_ID=?")
		.toString();

	/**
	 * Sql to delete all of the system message's meta data links.
	 * 
	 */
	private static final String SQL_DELETE_MESSAGE_META_DATA =
		new StringBuffer("delete from SYSTEM_MESSAGE_META_DATA ")
		.append("where SYSTEM_MESSAGE_ID=?")
		.toString();

	/**
	 * Sql to read the system messages.
	 * 
	 */
	private static final String SQL_READ =
		new StringBuffer("select SYSTEM_MESSAGE_ID,SYSTEM_MESSAGE_TYPE_ID ")
		.append("from SYSTEM_MESSAGE ")
		.append("order by SYSTEM_MESSAGE_ID asc")
		.toString();

	/**
	 * Sql to obtain a single system message.
	 * 
	 */
	private static final String SQL_READ_BY_MESSAGE_ID =
		new StringBuffer("select SYSTEM_MESSAGE_ID,SYSTEM_MESSAGE_TYPE_ID ")
		.append("from SYSTEM_MESSAGE ")
		.append("where SYSTEM_MESSAGE_ID=?")
		.toString();

	/**
	 * Sql to read a presence request system message by its requested by meta
	 * data.
	 * 
	 */
	private static final String SQL_READ_BY_META_DATA =
		new StringBuffer("select SYSTEM_MESSAGE_ID,SYSTEM_MESSAGE_TYPE_ID ")
		.append("from SYSTEM_MESSAGE SM inner join SYSTEM_MESSAGE_META_DATA SMMD ")
		.append("on SM.SYSTEM_MESSAGE_ID = SMMD.SYSTEM_MESSAGE_ID ")
		.append("inner join META_DATA MD on SMMD.META_DATA_ID = MD.META_DATA_ID ")
		.append("where MD.KEY=? and MD.VALUE=?")
		.toString();

	/**
	 * Sql to read a presence request system message by its requested by meta
	 * data.
	 * 
	 */
	private static final String SQL_READ_BY_TYPE_BY_META_DATA =
		new StringBuffer("select SYSTEM_MESSAGE_ID,SYSTEM_MESSAGE_TYPE_ID ")
		.append("from SYSTEM_MESSAGE SM inner join SYSTEM_MESSAGE_META_DATA SMMD ")
		.append("on SM.SYSTEM_MESSAGE_ID = SMMD.SYSTEM_MESSAGE_ID ")
		.append("inner join META_DATA MD on SMMD.META_DATA_ID = MD.META_DATA_ID ")
		.append("where SM.SYSTEM_MESSAGE_TYPE_ID=? ")
		.append("and (MD.KEY=? and MD.VALUE=?)")
		.toString();

	/**
	 * Sql to read a system message from the database.
	 * 
	 */
	private static final String SQL_READ_META_DATA =
		new StringBuffer("select MD.META_DATA_ID,MD.META_DATA_TYPE_ID,MD.KEY,")
		.append("MD.VALUE ")
		.append("from SYSTEM_MESSAGE SM inner join SYSTEM_MESSAGE_META_DATA SMMD ")
		.append("on SM.SYSTEM_MESSAGE_ID = SMMD.SYSTEM_MESSAGE_ID ")
		.append("inner join META_DATA MD on SMMD.META_DATA_ID = MD.META_DATA_ID ")
		.append("where SM.SYSTEM_MESSAGE_ID=? and MD.KEY=?")
		.toString();

	private static final String SQL_READ_META_DATA_IDS_BY_MESSAGE_ID =
		new StringBuffer("select META_DATA_ID ")
		.append("from SYSTEM_MESSAGE_META_DATA ")
		.append("where SYSTEM_MESSAGE_ID=?")
		.toString();

	/**
     * Create SystemMessageIOHandler.
     * 
     * @param sessionManager
     *            A hypersonic <code>SessionManager</code>.
     */
	public SystemMessageIOHandler(final SessionManager sessionManager) {
        super(sessionManager);
	}

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.SystemMessageIOHandler#create(com.thinkparity.model.parity.model.message.system.PresenceRequestMessage)
	 * 
	 */
	public void create(final ContactInvitationMessage contactInvitation)
			throws HypersonicException {
		final Session session = openSession();
		try {
			create(session, contactInvitation);

			createMetaData(session, contactInvitation,
					MetaDataType.JABBER_ID, MetaDataKey.INVITED_BY,
					contactInvitation.getInvitedBy());

			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
		
	}

	public void create(
			final ContactInvitationResponseMessage contactInvitationResponse)
			throws HypersonicException {
		final Session session = openSession();
		try {
			create(session, contactInvitationResponse);

			createMetaData(session, contactInvitationResponse,
					MetaDataType.JABBER_ID, MetaDataKey.RESPONSE_FROM,
					contactInvitationResponse.getResponseFrom());

			createMetaData(session, contactInvitationResponse,
					MetaDataType.BOOLEAN, MetaDataKey.DID_ACCEPT_REQUEST,
					contactInvitationResponse.didAcceptInvitation());

			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
		
	}

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.SystemMessageIOHandler#create(com.thinkparity.ophelia.model.message.KeyRequestMessage)
	 * 
	 */
	public void create(final KeyRequestMessage keyRequestMessage)
			throws HypersonicException {
		final Session session = openSession();
		try {
			create(session, keyRequestMessage);

			createMetaData(session, keyRequestMessage, MetaDataType.LONG,
					MetaDataKey.ARTIFACT_ID,
					keyRequestMessage.getArtifactId());
			createMetaData(session, keyRequestMessage, MetaDataType.JABBER_ID,
					MetaDataKey.REQUESTED_BY,
					keyRequestMessage.getRequestedBy());

			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.SystemMessageIOHandler#create(com.thinkparity.ophelia.model.message.KeyResponseMessage)
	 * 
	 */
	public void create(final KeyResponseMessage keyResponseMessage)
			throws HypersonicException {
		final Session session = openSession();
		try {
			create(session, keyResponseMessage);

			createMetaData(session, keyResponseMessage, MetaDataType.LONG,
					MetaDataKey.ARTIFACT_ID,
					keyResponseMessage.getArtifactId());
			createMetaData(session, keyResponseMessage, MetaDataType.JABBER_ID,
					MetaDataKey.RESPONSE_FROM,
					keyResponseMessage.getResponseFrom());
			createMetaData(session, keyResponseMessage, MetaDataType.BOOLEAN,
					MetaDataKey.DID_ACCEPT_REQUEST,
					keyResponseMessage.didAcceptRequest());

			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.SystemMessageIOHandler#delete(java.lang.Long)
	 * 
	 */
	public void delete(final Long messageId) throws HypersonicException {
		final Session session = openSession();
		try {
			final Long[] metaDataIds = readMetaDataIds(session, messageId);

			// delete all system message meta data links
			session.prepareStatement(SQL_DELETE_MESSAGE_META_DATA);
			session.setLong(1, messageId);
			session.executeUpdate();

			// delete all meta data
			for(final Long metaDataId : metaDataIds)
				getMetaDataIO().delete(session, metaDataId);

			// delete the message
			session.prepareStatement(SQL_DELETE_MESSAGE);
			session.setLong(1, messageId);
			if(1 != session.executeUpdate())
				throw new HypersonicException("Could not delete message.");

			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.SystemMessageIOHandler#read()
	 * 
	 */
	public List<SystemMessage> read() throws HypersonicException {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_READ);
			session.executeQuery();
			final List<SystemMessage> messages = new LinkedList<SystemMessage>();
			while(session.nextResult()) { messages.add(extract(session)); }
			return messages;
		}
		catch(final HypersonicException hx) {
			session.rollback();;
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.SystemMessageIOHandler#read(java.lang.Long)
	 * 
	 */
	public SystemMessage read(final Long messageId) throws HypersonicException {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_READ_BY_MESSAGE_ID);
			session.setLong(1, messageId);
			session.executeQuery();
			if(session.nextResult()) { return extract(session); }
			else { return null; }
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.SystemMessageIOHandler#readPresenceRequest(com.thinkparity.codebase.jabber.JabberId)
	 * 
	 */
	public ContactInvitationMessage readContactInvitation(
			final JabberId invitedBy) throws HypersonicException {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_READ_BY_META_DATA);
			session.setTypeAsString(1, MetaDataKey.INVITED_BY);
			session.setQualifiedUsername(2, invitedBy);
			session.executeQuery();
			if(session.nextResult()) { return extractInvitation(session); }
			else { return null; }
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	public ContactInvitationResponseMessage readContactInvitationResponse(
			final JabberId responseBy) throws HypersonicException {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_READ_BY_META_DATA);
			session.setTypeAsString(1, MetaDataKey.RESPONSE_FROM);
			session.setQualifiedUsername(2, responseBy);
			session.executeQuery();
			if(session.nextResult()) { return extractInvitationResponse(session); }
			else { return null; }
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	public List<SystemMessage> readForArtifact(final Long artifactId)
			throws HypersonicException {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_READ_BY_META_DATA);
			session.setTypeAsString(1, MetaDataKey.ARTIFACT_ID);
			session.setLong(2, artifactId);
			session.executeQuery();
			final List<SystemMessage> messages = new LinkedList<SystemMessage>();
			while(session.nextResult()) { messages.add(extract(session)); }
			return messages;
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	public List<SystemMessage> readForArtifact(final Long artifactId,
			final SystemMessageType type) throws HypersonicException {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_READ_BY_TYPE_BY_META_DATA);
			session.setTypeAsInteger(1, type);
			session.setTypeAsString(2, MetaDataKey.ARTIFACT_ID);
			session.setLong(3, artifactId);
			session.executeQuery();
			final List<SystemMessage> messages = new LinkedList<SystemMessage>();
			while (session.nextResult()) {
				messages.add(extract(session));
			}
			return messages;
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	private void create(final Session session, final SystemMessage systemMessage)
			throws HypersonicException {
		session.prepareStatement(SQL_CREATE);
		session.setTypeAsInteger(1, systemMessage.getType());
		if(1 != session.executeUpdate())
			throw new HypersonicException("Could not create system message.");

		systemMessage.setId(session.getIdentity());
	}

	private void createMetaData(final Session session,
			final SystemMessage systemMessage, final MetaDataType metaDataType,
			final MetaDataKey metaDataKey, final Object metaDataValue) {
		final Long metaDataId = getMetaDataIO().create(session, metaDataType,
				metaDataKey.toString(), metaDataValue);

		session.prepareStatement(SQL_CREATE_META_DATA);
		session.setLong(1, systemMessage.getId());
		session.setLong(2, metaDataId);
		if(1 != session.executeUpdate())
			throw new HypersonicException("Could not create system message meta data.");
	}

	private SystemMessage extract(final Session session) {
		final SystemMessageType messageType =
			session.getSystemMessageTypeFromInteger("SYSTEM_MESSAGE_TYPE_ID");
		switch(messageType) {
		case CONTACT_INVITATION: return extractInvitation(session);
		case CONTACT_INVITATION_RESPONSE: return extractInvitationResponse(session);
		case KEY_REQUEST: return extractKeyRequest(session);
		case KEY_RESPONSE: return extractKeyResponse(session);
		default:
			throw Assert.createUnreachable("Unknown system message type:  " + messageType);
		}
	}

	/**
	 * Extract the presence request system message from the database session.
	 * 
	 * @param session
	 *            the database session.
	 * @return The presence request system message.
	 */
	private ContactInvitationMessage extractInvitation(final Session session) {
		final ContactInvitationMessage presenceRequest = new ContactInvitationMessage();
		extractSystemMessage(session, presenceRequest);

		final MetaData[] metaData =
			readMetaData(presenceRequest.getId(), MetaDataKey.INVITED_BY);
		presenceRequest.setInvitedBy((JabberId) metaData[0].getValue());

		return presenceRequest;
	}

	/**
	 * Extract the contact invitation response from the database session.
	 * 
	 * @param session
	 *            The database session.
	 * @return The contact invitation response.
	 */
	private ContactInvitationResponseMessage extractInvitationResponse(
			final Session session) {
		final ContactInvitationResponseMessage message = new ContactInvitationResponseMessage();
		extractSystemMessage(session, message);

		MetaData[] metaData =
			readMetaData(message.getId(), MetaDataKey.RESPONSE_FROM);
		message.setResponseFrom((JabberId) metaData[0].getValue());

		metaData =
			readMetaData(message.getId(), MetaDataKey.DID_ACCEPT_REQUEST);
		message.setDidAcceptInvitation((Boolean) metaData[0].getValue());

		return message;
	}

	/**
	 * Extract the key request system message from the database session.
	 * 
	 * @param session
	 *            The database session.
	 * @return The key request system message.
	 */
	private SystemMessage extractKeyRequest(final Session session) {
		final KeyRequestMessage keyRequest = new KeyRequestMessage();
		extractSystemMessage(session, keyRequest);

		MetaData[] metaData = readMetaData(keyRequest.getId(), MetaDataKey.ARTIFACT_ID);
		keyRequest.setArtifactId((Long) metaData[0].getValue());

		metaData = readMetaData(keyRequest.getId(), MetaDataKey.REQUESTED_BY);
		keyRequest.setRequestedBy((JabberId) metaData[0].getValue());

		return keyRequest;
	}

	/**
	 * Extract the key response system message from the database session.
	 * 
	 * @param session
	 *            The database session.
	 * @return The key response system message.
	 */
	private SystemMessage extractKeyResponse(final Session session) {
		final KeyResponseMessage keyResponse = new KeyResponseMessage();
		extractSystemMessage(session, keyResponse);

		MetaData[] metaData = readMetaData(keyResponse.getId(), MetaDataKey.ARTIFACT_ID);
		keyResponse.setArtifactId((Long) metaData[0].getValue());

		metaData = readMetaData(keyResponse.getId(), MetaDataKey.DID_ACCEPT_REQUEST);
		keyResponse.setDidAcceptRequest((Boolean) metaData[0].getValue());

		metaData = readMetaData(keyResponse.getId(), MetaDataKey.RESPONSE_FROM);
		keyResponse.setResponseFrom((JabberId) metaData[0].getValue());

		return keyResponse;
	}

	/**
	 * Extract the system message common values from the database session and
	 * set the bean properties.
	 * 
	 * @param session
	 *            The database session.
	 * @param systemMessage
	 *            The system message.
	 */
	private void extractSystemMessage(final Session session,
			final SystemMessage systemMessage) {
		systemMessage.setId(session.getLong("SYSTEM_MESSAGE_ID"));
		systemMessage.setType(session.getSystemMessageTypeFromInteger("SYSTEM_MESSAGE_TYPE_ID"));
	}

	private MetaData[] readMetaData(final Long systemMessageId,
			final MetaDataKey metaDataKey) {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_READ_META_DATA);
			session.setLong(1, systemMessageId);
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

	private Long[] readMetaDataIds(final Session session, final Long messageId)
			throws HypersonicException {
		session.prepareStatement(SQL_READ_META_DATA_IDS_BY_MESSAGE_ID);
		final Set<Long> metaDataIds = new HashSet<Long>();
		session.setLong(1, messageId);
		session.executeQuery();
		while(session.nextResult()) {
			metaDataIds.add(session.getLong("META_DATA_ID"));
		}
		return metaDataIds.toArray(new Long[] {});
	}

	private enum MetaDataKey {
		ARTIFACT_ID, DID_ACCEPT_REQUEST, INVITED_BY, REQUESTED_BY, RESPONSE_FROM
	}

}
