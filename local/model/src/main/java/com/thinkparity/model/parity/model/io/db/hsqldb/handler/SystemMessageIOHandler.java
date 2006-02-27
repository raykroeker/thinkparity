/*
 * Feb 24, 2006
 */
package com.thinkparity.model.parity.model.io.db.hsqldb.handler;

import java.util.LinkedList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.model.parity.model.io.db.hsqldb.Session;
import com.thinkparity.model.parity.model.io.md.MetaData;
import com.thinkparity.model.parity.model.io.md.MetaDataType;
import com.thinkparity.model.parity.model.message.system.KeyRequestMessage;
import com.thinkparity.model.parity.model.message.system.KeyResponseMessage;
import com.thinkparity.model.parity.model.message.system.PresenceRequestMessage;
import com.thinkparity.model.parity.model.message.system.SystemMessage;
import com.thinkparity.model.parity.model.message.system.SystemMessageType;
import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SystemMessageIOHandler extends AbstractIOHandler implements
		com.thinkparity.model.parity.model.io.handler.SystemMessageIOHandler {

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
	 * Sql to read the system messages.
	 * 
	 */
	private static final String SQL_READ =
		new StringBuffer("select SYSTEM_MESSAGE_ID,SYSTEM_MESSAGE_TYPE_ID ")
		.append("from SYSTEM_MESSAGE ")
		.append("order by SYSTEM_MESSAGE_ID asc")
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

	/**
	 * Sql to read a presence request system message by its requested by meta
	 * data.
	 * 
	 */
	private static final String SQL_READ_PRESENCE_REQUEST_BY_REQUESTED_BY =
		new StringBuffer("select SYSTEM_MESSAGE_ID,SYSTEM_MESSAGE_TYPE_ID ")
		.append("from SYSTEM_MESSAGE SM inner join SYSTEM_MESSAGE_META_DATA SMMD ")
		.append("on SM.SYSTEM_MESSAGE_ID = SMMD.SYSTEM_MESSAGE_ID ")
		.append("inner join META_DATA MD on SMMD.META_DATA_ID = MD.META_DATA_ID ")
		.append("where MD.KEY=? and MD.VALUE=?")
		.toString();

	/**
	 * Create a SystemMessageIOHandler.
	 * 
	 */
	public SystemMessageIOHandler() { super(); }

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.SystemMessageIOHandler#create(com.thinkparity.model.parity.model.message.system.KeyRequestMessage)
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
	 * @see com.thinkparity.model.parity.model.io.handler.SystemMessageIOHandler#create(com.thinkparity.model.parity.model.message.system.KeyResponseMessage)
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
	 * @see com.thinkparity.model.parity.model.io.handler.SystemMessageIOHandler#create(com.thinkparity.model.parity.model.message.system.PresenceRequestMessage)
	 * 
	 */
	public void create(final PresenceRequestMessage presenceRequestMessage) throws HypersonicException {
		final Session session = openSession();
		try {
			create(session, presenceRequestMessage);

			createMetaData(session, presenceRequestMessage, MetaDataType.JABBER_ID,
						MetaDataKey.REQUESTED_BY,
						presenceRequestMessage.getRequestedBy());

			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
		
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.SystemMessageIOHandler#read()
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
	 * @see com.thinkparity.model.parity.model.io.handler.SystemMessageIOHandler#readPresenceRequest(com.thinkparity.model.xmpp.JabberId)
	 * 
	 */
	public PresenceRequestMessage readPresenceRequest(final JabberId jabberId) throws HypersonicException {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_READ_PRESENCE_REQUEST_BY_REQUESTED_BY);
			session.setTypeAsString(1, MetaDataKey.REQUESTED_BY);
			session.setQualifiedUsername(2, jabberId);
			session.executeQuery();
			if(session.nextResult()) { return extractPresenceRequest(session); }
			else { return null; }
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
		case PRESENCE_REQUEST: return extractPresenceRequest(session);
		case KEY_REQUEST: return extractKeyRequest(session);
		case KEY_RESPONSE: return extractKeyResponse(session);
		default:
			throw Assert.createUnreachable("Unknown system message type:  " + messageType);
		}
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
	 * Extract the presence request system message from the database session.
	 * 
	 * @param session
	 *            the database session.
	 * @return The presence request system message.
	 */
	private PresenceRequestMessage extractPresenceRequest(final Session session) {
		final PresenceRequestMessage presenceRequest = new PresenceRequestMessage();
		extractSystemMessage(session, presenceRequest);

		final MetaData[] metaData = readMetaData(presenceRequest.getId(), MetaDataKey.REQUESTED_BY);
		presenceRequest.setRequestedBy((JabberId) metaData[0].getValue());

		return presenceRequest;
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

	private enum MetaDataKey { ARTIFACT_ID, DID_ACCEPT_REQUEST, REQUESTED_BY, RESPONSE_FROM }
}
