/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.io.db.hsqldb.handler;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.model.parity.model.io.db.hsqldb.Session;
import com.thinkparity.model.parity.model.md.MetaDataType;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MetaDataIOHandler extends AbstractIOHandler {

	private static final String SQL_CREATE =
		new StringBuffer("insert into META_DATA ")
		.append("(META_DATA_TYPE_ID,KEY,VALUE) ")
		.append("values (?,?,?)")
		.toString();

	private static final String SQL_DELETE =
		new StringBuffer("delete from META_DATA ")
		.append("where META_DATA_ID=?")
		.toString();

	/**
	 * Create a MetaDataIOHandler.
	 * 
	 */
	MetaDataIOHandler() { super(); }

	Long create(final Session session, final MetaDataType metaDataType,
			final Enum<?> metaDataKey, final Object metaDataValue)
			throws HypersonicException {
		session.prepareStatement(SQL_CREATE);
		session.setTypeAsInteger(1, metaDataType);
		session.setString(2, metaDataKey.toString());
		setValue(session, 3, metaDataType, metaDataValue);
		if(1 != session.executeUpdate())
			throw new HypersonicException("Could not create meta data:  " + metaDataKey);
		return session.getIdentity();
	}

	void delete(final Session session, final Long[] metaDataIds)
			throws HypersonicException {
		session.prepareStatement(SQL_DELETE);
		for(final Long metaDataId : metaDataIds) {
			session.setLong(1, metaDataId);
			if(1 != session.executeUpdate())
				throw new HypersonicException("Could not delete meta data.");
		}
	}

	private void setValue(final Session session, final Integer index,
			final MetaDataType metaDataType, final Object value) {
		switch(metaDataType) {
		case STRING:
			session.setString(index, (String) value);
			break;
		default: Assert.assertUnreachable("Unknown meta data type:  " + metaDataType);
		}
	}
}
