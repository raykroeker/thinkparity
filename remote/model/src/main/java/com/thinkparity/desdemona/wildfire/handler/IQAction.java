package com.thinkparity.desdemona.wildfire.handler;

import com.thinkparity.codebase.Constants.Xml;

/**
 * List of possible actions available within the parity artifact handler.
 * 
 */
public enum IQAction {

	ACCEPTCONTACTINVITATION, ACCEPTKEYREQUEST, ARTIFACTCONFIRMRECEIPT, CLOSEARTIFACT, CREATEARTIFACT,
	DELETEARTIFACT, DECLINECONTACTINVITATION, DENYKEYREQUEST, DOCUMENTSEND, FLAGARTIFACT,
	GETKEYHOLDER, GETKEYS, GETSUBSCRIPTION, PROCESSOFFLINEQUEUE,
	READCONTACTS, ARTIFACTREADCONTACTS,READUSERS, REQUESTKEY, KEYRESPONSE, SETKEYHOLDER, SUBSCRIBEUSER,
	UNSUBSCRIBEUSER;

	public String getNamespace() {
		return new StringBuffer(Xml.NAMESPACE)
			.append(":")
			.append(toString().toLowerCase()).toString();
	}
}
