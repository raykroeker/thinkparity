/*
 * Feb 22, 2006
 */
package com.thinkparity.ophelia.model.io.handler;


import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.md.MetaData;
import com.thinkparity.ophelia.model.io.md.MetaDataType;

/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public interface MetaDataIOHandler {
	public Long create(final MetaDataType metaDataType,
			final String metaDataKey, final Object metaDataValue) throws HypersonicException;
	public void delete(final Long metaDataId) throws HypersonicException;
	public MetaData read(final Long metaDataId) throws HypersonicException;
	public void update(final Long metaDataId, final MetaDataType metaDataType,
			final String metaDataKey, final Object metaDataValue) throws HypersonicException;
}
