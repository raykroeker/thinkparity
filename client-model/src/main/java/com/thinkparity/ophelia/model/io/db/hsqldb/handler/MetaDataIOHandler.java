/*
 * Feb 21, 2006
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;
import com.thinkparity.ophelia.model.io.db.hsqldb.SessionManager;
import com.thinkparity.ophelia.model.io.md.MetaData;
import com.thinkparity.ophelia.model.io.md.MetaDataType;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MetaDataIOHandler extends AbstractIOHandler implements
		com.thinkparity.ophelia.model.io.handler.MetaDataIOHandler {

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

	/** Sql to update a meta data value. */
	private static final String SQL_UPDATE =
		new StringBuffer("update META_DATA ")
		.append("set META_DATA_TYPE_ID=?,KEY=?,VALUE=? ")
		.append("where META_DATA_ID=?")
		.toString();

	/**
     * Create a MetaDataIOHandler.
     * 
     * @param sessionManager
     *            A hypersonic <code>SessionManager</code>.
     */
	public MetaDataIOHandler(final SessionManager sessionManager) {
        super(sessionManager);
	}

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.MetaDataIOHandler#create(com.thinkparity.ophelia.model.io.md.MetaDataType,
	 *      java.lang.String, java.lang.Object)
	 * 
	 */
	public Long create(final MetaDataType metaDataType,
			final String metaDataKey, final Object metaDataValue)
			throws HypersonicException {
		final Session session = openSession();
		try {
			final Long metaDataId =
				create(session, metaDataType, metaDataKey, metaDataValue);
			session.commit();
			return metaDataId;
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.MetaDataIOHandler#delete(java.lang.Long)
	 * 
	 */
	public void delete(final Long metaDataId) throws HypersonicException {
		final Session session = openSession();
		try {
			delete(session, metaDataId);
			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.MetaDataIOHandler#read(java.lang.Long)
	 */
	public MetaData read(final Long metaDataId) throws HypersonicException {
		final Session session = openSession();
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
	 * @see com.thinkparity.ophelia.model.io.handler.MetaDataIOHandler#update(java.lang.Long,
	 *      com.thinkparity.ophelia.model.io.md.MetaDataType,
	 *      java.lang.String, java.lang.Object)
	 * 
	 */
	public void update(final Long metaDataId, final MetaDataType metaDataType,
			final String metaDataKey, final Object metaDataValue)
			throws HypersonicException {
		final Session session = openSession();
		try {
			update(session, metaDataId, metaDataType, metaDataKey, metaDataValue);
			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
	 * Create a new meta data value.
	 * 
	 * @param session
	 *            The database session.
	 * @param metaDataType
	 *            The meta data type.
	 * @param metaDataKey
	 *            The meta data key.
	 * @param metaDataValue
	 *            The meta data value.
	 * @return The meta data identity.
	 * @throws HypersonicException
	 */
	Long create(final Session session, final MetaDataType metaDataType,
			final String metaDataKey, final Object metaDataValue)
			throws HypersonicException {
		session.prepareStatement(SQL_CREATE);
		session.setTypeAsInteger(1, metaDataType);
		session.setString(2, metaDataKey.toString());
		setValue(session, 3, metaDataType, metaDataValue);
		if(1 != session.executeUpdate())
			throw new HypersonicException("Could not create meta data:  " + metaDataKey);
		return session.getIdentity();
	}

	/**
	 * Delete a meta data value.
	 * 
	 * @param session
	 *            The database session.
	 * @param metaDataId
	 *            The meta data id.
	 * @throws HypersonicException
	 */
	void delete(final Session session, final Long metaDataId)
			throws HypersonicException {
		session.prepareStatement(SQL_DELETE);
		session.setLong(1, metaDataId);
		if(1 != session.executeUpdate())
			throw new HypersonicException("Could not delete meta data.");
	}

	/**
	 * Extract the meta data value from the session.
	 * 
	 * @param session
	 *            The database session.
	 * @param metaDataType
	 *            The meta data type.
	 * @param columnName
	 *            The column name.
	 * @return The meta data value.
	 * 
	 * @see #setValue(Session, Integer, MetaDataType, Object)
	 */
	Object extractValue(final Session session,
			final MetaDataType metaDataType, final String columnName) {
		switch(metaDataType) {
		case BOOLEAN:
			return Boolean.valueOf(session.getString(columnName));
		case LONG:
		case USER_ID:
			return Long.valueOf(session.getString(columnName));
		case STRING:
			return session.getString(columnName);
		case JABBER_ID:
			return JabberIdBuilder.parseQualifiedUsername(session.getString(columnName));
		default:
			throw Assert.createUnreachable("Unknown meta data type:  " + metaDataType);
		}
	}

	/**
	 * Read a meta data value.
	 * 
	 * @param session
	 *            The database session.
	 * @param metaDataId
	 *            The meta data id.
	 * @return The meta data.
	 */
	MetaData read(final Session session, final Long metaDataId) {
		session.prepareStatement(SQL_READ_BY_ID);
		session.setLong(1, metaDataId);
		if(session.nextResult()) { return extract(session); }
		else { return null; }
	}

	/**
	 * Update a meta data item.
	 * 
	 * @param session
	 *            The database session.
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
	void update(final Session session, final Long metaDataId,
			final MetaDataType metaDataType, final String metaDataKey,
			final Object metaDataValue) throws HypersonicException {
		session.prepareStatement(SQL_UPDATE);
		session.setTypeAsInteger(1, metaDataType);
		session.setString(2, metaDataKey);
		setValue(session, 3, metaDataType, metaDataValue);
        session.setLong(4, metaDataId);
		if(1 != session.executeUpdate())
			throw new HypersonicException("Could not update meta data.");
	}

	/**
	 * Extract the meta data from the database session.
	 * 
	 * @param session
	 *            The database session.
	 * @return The meta data.
	 */
	private MetaData extract(final Session session) {
		final MetaData metaData = new MetaData();
		metaData.setId(session.getLong("META_DATA_ID"));
		metaData.setKey(session.getString("KEY"));
		metaData.setType(session.getMetaDataTypeFromInteger("META_DATA_TYPE_ID"));
		metaData.setValue(extractValue(session, metaData.getType(), "VALUE"));
		return metaData;
	}

	/**
	 * Set the meta data value in the session.
	 * 
	 * @param session
	 *            The database session.
	 * @param index
	 *            The column index in the statement.
	 * @param metaDataType
	 *            The meta data type.
	 * @param value
	 *            The meta data value.
	 * 
	 * @see #extractValue(Session, MetaDataType, String)
	 */
	private void setValue(final Session session, final Integer index,
			final MetaDataType metaDataType, final Object value) {
		switch(metaDataType) {
		case BOOLEAN:
			session.setString(index, ((Boolean) value).toString());
			break;
		case LONG:
        case USER_ID:
			session.setString(index, ((Long) value).toString());
			break;
		case STRING:
			session.setString(index, (String) value);
			break;
		case JABBER_ID:
			session.setQualifiedUsername(index, (JabberId) value);
			break;
		default: Assert.assertUnreachable("Unknown meta data type:  " + metaDataType);
		}
	}

}
