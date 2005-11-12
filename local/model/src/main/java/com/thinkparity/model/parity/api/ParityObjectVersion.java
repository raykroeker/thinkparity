/*
 * Apr 16, 2005
 */
package com.thinkparity.model.parity.api;

/**
 * ParityObjectVersion
 * 
 * @author raykroeker@gmail.com
 * @version 1.2
 */
public abstract class ParityObjectVersion {

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
