package com.thinkparity.server.handler;

import com.thinkparity.server.ParityServerConstants;

/**
 * List of possible actions available within the parity artifact handler.
 * 
 */
public enum IQAction {

	/**
	 * Create an artifact.
	 */
	CREATEARTIFACT,

	/**
	 * Flag an artifact.
	 */
	FLAGARTIFACT,

	/**
	 * Get an artifact's keyholder.
	 */
	GETKEYHOLDER,

	/**
	 * Request an artifact's key.
	 */
	REQUESTKEY,

	/**
	 * Set an artifact's keyholder.
	 */
	SETKEYHOLDER,

	/**
	 * Subscribe a user to an artifact.
	 */
	SUBSCRIBEUSER,

	/**
	 * Unsubscribe a user from an artifact.
	 */
	UNSUBSCRIBEUSER;

	public String getNamespace() {
		return new StringBuffer(ParityServerConstants.IQ_PARITY_INFO_NAMESPACE)
			.append(":")
			.append(toString().toLowerCase()).toString();
	}
}
