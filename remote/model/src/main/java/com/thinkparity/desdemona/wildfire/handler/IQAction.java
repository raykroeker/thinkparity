package com.thinkparity.desdemona.wildfire.handler;

import com.thinkparity.codebase.Constants.Xml;

/**
 * List of possible actions available within the parity artifact handler.
 * 
 */
public enum IQAction {

	ACCEPTCONTACTINVITATION, ARTIFACTCONFIRMRECEIPT, ARTIFACTREADCONTACTS,
    CLOSEARTIFACT, CREATEARTIFACT, DECLINECONTACTINVITATION, DELETEARTIFACT,
    DOCUMENTSEND, FLAGARTIFACT, GETKEYHOLDER, GETKEYS, GETSUBSCRIPTION, 
    KEYRESPONSE, PROCESSOFFLINEQUEUE, READCONTACTS, READUSERS, REQUESTKEY,
    SETKEYHOLDER, SUBSCRIBEUSER, UNSUBSCRIBEUSER;

	public String getNamespace() {
		return new StringBuffer(Xml.NAMESPACE)
			.append(":")
			.append(toString().toLowerCase()).toString();
	}
}
