/*
 * Apr 16, 2005
 */
package com.thinkparity.model.parity.api;

/**
 * ParityObjectVersion
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class ParityObjectVersion implements ParityXmlSerializable {

	/**
	 * Contains the version id of the parity version object.
	 */
	private String versionId;

	/**
	 * Create a ParityObjectVersion
	 * @param versionId <code>java.lang.String</code>
	 */
	protected ParityObjectVersion(final String versionId) {
		super();
		this.versionId = versionId;
	}

	/**
	 * Obtain versionId.
	 * @return <code>String</code>
	 */
	public String getVersion() { return versionId; }
}
