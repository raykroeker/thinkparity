/*
 * Feb 21, 2006
 */
package com.thinkparity.migrator.io.hsqldb.handler;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.migrator.io.hsqldb.HypersonicException;
import com.thinkparity.migrator.io.hsqldb.HypersonicSession;
import com.thinkparity.migrator.io.md.MetaData;
import com.thinkparity.migrator.io.md.MetaDataType;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MetaDataIOHandler extends AbstractIOHandler implements
        com.thinkparity.migrator.io.handler.MetaDataIOHandler {

	/**
	 * Sql to create a meta data item.
	 * 
	 */
	private static final String SQL_CREATE =
		new StringBuffer("insert into META_DATA ")
		.append("(META_DATA_TYPE_ID,KEY,VALUE) ")
		.append("values (?,?,?)")
		.toString();

	/**
	 * Sql to delete a meta data item.
	 * 
	 */
	private static final String SQL_DELETE =
		new StringBuffer("delete from META_DATA ")
		.append("where META_DATA_ID=?")
		.toString();

	/**
	 * Sql to read a meta data value by id.
	 * 
	 */
	private static final String SQL_READ_BY_ID =
		new StringBuffer("select META_DATA_ID,META_DATA_TYPE_ID,KEY,VALUE ")
		.append("from META_DATA ")
		.append("where META_DATA_ID=?")
		.toString();

	/**
	 * Sql to update a meta data value.
	 * 
	 */
	private static final String SQL_UPDATE =
		new StringBuffer("update META_DATA ")
		.append("set META_DATA_TYPE_ID=?,KEY=?,VALUE=? ")
		.append("Where META_DATA_ID=?")
		.toString();

	/**
	 * Create a MetaDataIOHandler.
	 * 
	 */
	public MetaDataIOHandler() { super(); }

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.MetaDataIOHandler#create(com.thinkparity.model.parity.model.io.md.MetaDataType,
	 *      java.lang.String, java.lang.Object)
	 * 
	 */
	public Long create(final MetaDataType metaDataType,
			final String metaDataKey, final Object metaDataValue)
			throws HypersonicException {
		final HypersonicSession HypersonicSession = openSession();
		try {
			final Long metaDataId =
				create(HypersonicSession, metaDataType, metaDataKey, metaDataValue);
			HypersonicSession.commit();
			return metaDataId;
		}
		catch(final HypersonicException hx) {
			HypersonicSession.rollback();
			throw hx;
		}
		finally { HypersonicSession.close(); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.MetaDataIOHandler#delete(java.lang.Long)
	 * 
	 */
	public void delete(final Long metaDataId) throws HypersonicException {
		final HypersonicSession HypersonicSession = openSession();
		try {
			delete(HypersonicSession, metaDataId);
			HypersonicSession.commit();
		}
		catch(final HypersonicException hx) {
			HypersonicSession.rollback();
			throw hx;
		}
		finally { HypersonicSession.close(); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.MetaDataIOHandler#read(java.lang.Long)
	 */
	public MetaData read(final Long metaDataId) throws HypersonicException {
		final HypersonicSession session = openSession();
		try {
			final MetaData metaData = read(session, metaDataId);
            session.commit();
			return metaData;
		}
		catch(final HypersonicException hx) {
            session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.MetaDataIOHandler#update(java.lang.Long,
	 *      com.thinkparity.model.parity.model.io.md.MetaDataType,
	 *      java.lang.String, java.lang.Object)
	 * 
	 */
	public void update(final Long metaDataId, final MetaDataType metaDataType,
			final String metaDataKey, final Object metaDataValue)
			throws HypersonicException {
		final HypersonicSession HypersonicSession = openSession();
		try {
			update(HypersonicSession, metaDataId, metaDataType, metaDataKey, metaDataValue);
			HypersonicSession.commit();
		}
		catch(final HypersonicException hx) {
			HypersonicSession.rollback();
			throw hx;
		}
		finally { HypersonicSession.close(); }
	}

	/**
	 * Create a new meta data value.
	 * 
	 * @param HypersonicSession
	 *            The database HypersonicSession.
	 * @param metaDataType
	 *            The meta data type.
	 * @param metaDataKey
	 *            The meta data key.
	 * @param metaDataValue
	 *            The meta data value.
	 * @return The meta data identity.
	 * @throws HypersonicException
	 */
	Long create(final HypersonicSession HypersonicSession, final MetaDataType metaDataType,
			final String metaDataKey, final Object metaDataValue)
			throws HypersonicException {
		HypersonicSession.prepareStatement(SQL_CREATE);
		HypersonicSession.setTypeAsInteger(1, metaDataType);
		HypersonicSession.setString(2, metaDataKey.toString());
		setValue(HypersonicSession, 3, metaDataType, metaDataValue);
		if(1 != HypersonicSession.executeUpdate())
			throw new HypersonicException("[RMIGRATOR] [IO] [HYPERSONIC HANDLER] [META DATA] [CREATE] [COULD NOT CREATE META DATA]");
		return HypersonicSession.getIdentity();
	}

	/**
	 * Delete a meta data value.
	 * 
	 * @param HypersonicSession
	 *            The database HypersonicSession.
	 * @param metaDataId
	 *            The meta data id.
	 * @throws HypersonicException
	 */
	void delete(final HypersonicSession HypersonicSession, final Long metaDataId)
			throws HypersonicException {
		HypersonicSession.prepareStatement(SQL_DELETE);
		HypersonicSession.setLong(1, metaDataId);
		if(1 != HypersonicSession.executeUpdate())
			throw new HypersonicException("[RMIGRATOR] [IO] [HYPERSONIC HANDLER] [META DATA] [DELETE] [COULD NOT DELETE META DATA]");
	}

	/**
	 * Extract the meta data value from the HypersonicSession.
	 * 
	 * @param HypersonicSession
	 *            The database HypersonicSession.
	 * @param metaDataType
	 *            The meta data type.
	 * @param columnName
	 *            The column name.
	 * @return The meta data value.
	 * 
	 * @see #setValue(HypersonicSession, Integer, MetaDataType, Object)
	 */
	Object extractValue(final HypersonicSession HypersonicSession,
			final MetaDataType metaDataType, final String columnName) {
		switch(metaDataType) {
		case BOOLEAN:
			return Boolean.valueOf(HypersonicSession.getString(columnName));
		case LONG:
		case USER_ID:
			return Long.valueOf(HypersonicSession.getString(columnName));
		case STRING:
			return HypersonicSession.getString(columnName);
		case JABBER_ID:
			return JabberIdBuilder.parseQualifiedUsername(HypersonicSession.getString(columnName));
		default:
			throw Assert.createUnreachable("[RMIGRATOR] [IO] [HYPERSONIC HANDLER] [META DATA] [EXTRACT VALUE] [UNKNOWN META DATA TYPE]");
		}
	}

	/**
	 * Read a meta data value.
	 * 
	 * @param HypersonicSession
	 *            The database HypersonicSession.
	 * @param metaDataId
	 *            The meta data id.
	 * @return The meta data.
	 */
	MetaData read(final HypersonicSession HypersonicSession, final Long metaDataId) {
		HypersonicSession.prepareStatement(SQL_READ_BY_ID);
		HypersonicSession.setLong(1, metaDataId);
		if(HypersonicSession.nextResult()) { return extract(HypersonicSession); }
		else { return null; }
	}

	/**
	 * Update a meta data item.
	 * 
	 * @param HypersonicSession
	 *            The database HypersonicSession.
	 * @param metaDataId
	 *            The meta data id.
	 * @param metaDataType
	 *            The meta data type.
	 * @param metaDataKey
	 *            The meta data key.
	 * @param metaDataValue
	 *            The meta data value.
	 * @throws HypersonicException
	 */
	void update(final HypersonicSession HypersonicSession, final Long metaDataId,
			final MetaDataType metaDataType, final String metaDataKey,
			final Object metaDataValue) throws HypersonicException {
		HypersonicSession.prepareStatement(SQL_UPDATE);
		HypersonicSession.setTypeAsInteger(1, metaDataType);
		HypersonicSession.setString(2, metaDataKey);
		setValue(HypersonicSession, 3, metaDataType, metaDataValue);
		if(1 != HypersonicSession.executeUpdate())
			throw new HypersonicException("[RMIGRATOR] [IO] [HYPERSONIC HANDLER] [META DATA] [UPDATE] [COULD NOT UPDATE META DATA]");
	}

	/**
	 * Extract the meta data from the database HypersonicSession.
	 * 
	 * @param HypersonicSession
	 *            The database HypersonicSession.
	 * @return The meta data.
	 */
	private MetaData extract(final HypersonicSession HypersonicSession) {
		final MetaData metaData = new MetaData();
		metaData.setId(HypersonicSession.getLong("META_DATA_ID"));
		metaData.setKey(HypersonicSession.getString("KEY"));
		metaData.setType(HypersonicSession.getMetaDataTypeFromInteger("META_DATA_TYPE_ID"));
		metaData.setValue(extractValue(HypersonicSession, metaData.getType(), "VALUE"));
		return metaData;
	}

	/**
	 * Set the meta data value in the HypersonicSession.
	 * 
	 * @param HypersonicSession
	 *            The database HypersonicSession.
	 * @param index
	 *            The column index in the statement.
	 * @param metaDataType
	 *            The meta data type.
	 * @param value
	 *            The meta data value.
	 * 
	 * @see #extractValue(HypersonicSession, MetaDataType, String)
	 */
	private void setValue(final HypersonicSession hypersonicSession, final Integer index,
			final MetaDataType metaDataType, final Object value) {
		switch(metaDataType) {
		case BOOLEAN:
            hypersonicSession.setString(index, ((Boolean) value).toString());
			break;
		case LONG:
        case USER_ID:
            hypersonicSession.setString(index, ((Long) value).toString());
			break;
		case STRING:
            hypersonicSession.setString(index, (String) value);
			break;
		case JABBER_ID:
            hypersonicSession.setQualifiedUsername(index, (JabberId) value);
			break;
		default:
            Assert.assertUnreachable("[RMIGRATOR] [IO] [HYPERSONIC HANDLER] [META DATA] [SET VALUE] [UNKNOWN META DATA TYPE]");
		}
	}

}
