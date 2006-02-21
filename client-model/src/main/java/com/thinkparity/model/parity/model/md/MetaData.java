/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.md;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum MetaData {

	VERSION(MetaDataType.STRING);

	private MetaDataType type;

	private MetaData(final MetaDataType type) {
		this.type = type;
	}

	public MetaDataType getType() { return type; }
}
