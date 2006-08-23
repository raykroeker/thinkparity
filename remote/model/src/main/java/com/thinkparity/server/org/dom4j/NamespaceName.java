/*
 * Dec 8, 2005
 */
package com.thinkparity.server.org.dom4j;

import com.thinkparity.codebase.Constants.Xml;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum NamespaceName {

    IQ_ACCEPT_CONTACT_INVITATION("acceptcontactinvitation"),
	IQ_ACCEPT_KEY_REQUEST("acceptkeyrequest"),
	IQ_ARTIFACT_CONFIRM_RECEIPT("artifactconfirmreceipt"),
	IQ_ARTIFACT_FLAG("artifactflag"), IQ_ARTIFACT_READ_CONTACTS("artifactreadcontacts"),
	IQ_CLOSE_ARTIFACT("closeartifact"),
	IQ_CONTAINER_REACTIVATE("containerreactivate"),
	IQ_DECLINE_CONTACT_INVITATION("declinecontactinvitation"), IQ_DENY_KEY_REQUEST("denykeyrequest"),
    IQ_DOCUMENT_REACTIVATE("documentreactivate"),
    IQ_DOCUMENT_SEND("documentsend"),
    IQ_GET_KEYHOLDER("getkeyholder"),
	IQ_GET_KEYS("getkeys"), IQ_GET_SUBSCRIPTION("getsubscriptions"),
	IQ_INVITE_CONTACT("invitecontact"), IQ_KEY_REQUEST("keyrequest"),
	IQ_NOTIFY_TEAM_MEMBER_ADDED("notifyteammemberadded"), IQ_NOTIFY_TEAM_MEMBER_REMOVED("notifyteammemberremoved"),
	IQ_READ_CONTACTS("readcontacts"),
	IQ_READ_USERS("readusers");;

	/** The namespace suffix. */
	private final String name;

	/**
	 * Create a NamespaceName.
	 * 
	 * @param name
	 *            The name.
	 */
	private NamespaceName(final String name) { this.name = name; }

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return new StringBuffer(Xml.NAMESPACE).append(":").append(name).toString();
	}
}
