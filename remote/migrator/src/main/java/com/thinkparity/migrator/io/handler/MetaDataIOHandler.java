/*
 * Feb 22, 2006
 */
package com.thinkparity.migrator.io.handler;

import com.thinkparity.migrator.io.hsqldb.HypersonicException;
import com.thinkparity.migrator.io.md.MetaData;
import com.thinkparity.migrator.io.md.MetaDataType;

/**
 * @author raykroeker@gmail.com
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
