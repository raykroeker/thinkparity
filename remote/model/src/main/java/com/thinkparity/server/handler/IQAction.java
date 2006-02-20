package com.thinkparity.server.handler;

import com.thinkparity.server.ParityServerConstants;

/**
 * List of possible actions available within the parity artifact handler.
 * 
 */
public enum IQAction {

	ACCEPTKEYREQUEST, CLOSEARTIFACT, CREATEARTIFACT, DELETEARTIFACT,
	DENYKEYREQUEST, FLAGARTIFACT, GETKEYHOLDER, GETKEYS, GETSUBSCRIPTION,
	PROCESSOFFLINEQUEUE, REQUESTKEY, KEYRESPONSE, SETKEYHOLDER, SUBSCRIBEUSER,
	UNSUBSCRIBEUSER;

	public String getNamespace() {
		return new StringBuffer(ParityServerConstants.IQ_PARITY_INFO_NAMESPACE)
			.append(":")
			.append(toString().toLowerCase()).toString();
	}
}
