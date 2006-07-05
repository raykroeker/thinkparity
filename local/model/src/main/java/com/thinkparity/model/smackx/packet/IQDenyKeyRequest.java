/*
 * Dec 12, 2005
 */
package com.thinkparity.model.smackx.packet;

import java.util.UUID;

import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQDenyKeyRequest extends IQArtifact {

	/**
	 * The fully qualified jid to deny the request of.
	 */
	private String qualifiedJID;

	/**
	 * Create a IQDenyKeyRequest.
	 * 
	 * @param artifactUUID
	 *            The artifact unique id.
	 */
	public IQDenyKeyRequest(final UUID artifactUUID, final JabberId jabberId) {
		super(Action.DENYKEYREQUEST, artifactUUID);
		setQualifiedJID(jabberId.getQualifiedJabberId());
	}

	/**
	 * @see com.thinkparity.model.smackx.packet.IQArtifact#getChildElementXML()
	 * 
	 */
	public String getChildElementXML() {
		return new StringBuffer(startQueryXML())
			.append(getArtifactUUIDXML())
			.append(getJIDXML())
			.append(finishQueryXML())
			.toString();
	}

	/**
	 * Obtain the qualified jid.
	 * 
	 * @return The qualified jid.
	 */
	public String getQualifiedJID() { return qualifiedJID; }

	/**
	 * Set the qualified jid.
	 * 
	 * @param qualifiedJID
	 *            The fully qualified jid to set.
	 */
	public void setQualifiedJID(final String qualifiedJID) {
		this.qualifiedJID = qualifiedJID;
	}

	/**
	 * Obtain the jid xml.
	 * 
	 * @return The jid xml.
	 * @see IQDenyKeyRequest#getQualifiedJID()
	 */
	private String getJIDXML() {
		return new StringBuffer("<jid>")
			.append(getQualifiedJID())
			.append("</jid>")
			.toString();
	}
}
