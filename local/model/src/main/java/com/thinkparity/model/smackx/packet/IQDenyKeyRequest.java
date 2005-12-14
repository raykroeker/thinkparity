/*
 * Dec 12, 2005
 */
package com.thinkparity.model.smackx.packet;

import java.util.UUID;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQDenyKeyRequest extends IQArtifact {

	private String qualifiedJID;

	/**
	 * Create a IQDenyKeyRequest.
	 * 
	 * @param artifactUUID
	 *            The artifact unique id.
	 */
	public IQDenyKeyRequest(final UUID artifactUUID, final String qualifiedJID) {
		super(Action.DENYKEYREQUEST, artifactUUID);
		setQualifiedJID(qualifiedJID);
	}

	/**
	 * @see com.thinkparity.model.smackx.packet.IQArtifact#getChildElementXML()
	 */
	public String getChildElementXML() {
		return new StringBuffer(startQueryXML())
			.append(getArtifactUUIDXML())
			.append(getJIDXML())
			.append(finishQueryXML())
			.toString();
	}

	/**
	 * @return Returns the username.
	 */
	public String getQualifiedJID() { return qualifiedJID; }

	/**
	 * @param username The username to set.
	 */
	public void setQualifiedJID(final String qualifiedJID) {
		this.qualifiedJID = qualifiedJID;
	}

	private String getJIDXML() {
		return new StringBuffer("<jid>")
			.append(getQualifiedJID())
			.append("</jid>")
			.toString();
	}
}
