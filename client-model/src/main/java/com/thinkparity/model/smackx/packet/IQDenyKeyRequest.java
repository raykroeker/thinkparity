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

	private String username;

	/**
	 * Create a IQDenyKeyRequest.
	 * 
	 * @param artifactUUID
	 *            The artifact unique id.
	 */
	public IQDenyKeyRequest(final UUID artifactUUID, final String username) {
		super(Action.DENYKEYREQUEST, artifactUUID);
		setUsername(username);
	}

	/**
	 * @see com.thinkparity.model.smackx.packet.IQArtifact#getChildElementXML()
	 */
	public String getChildElementXML() {
		return new StringBuffer(startQueryXML())
			.append(getArtifactUUIDXML())
			.append(getUsernameXML())
			.append(finishQueryXML())
			.toString();
	}

	/**
	 * @return Returns the username.
	 */
	public String getUsername() { return username; }

	/**
	 * @param username The username to set.
	 */
	public void setUsername(String username) { this.username = username; }

	private String getUsernameXML() {
		return new StringBuffer("<username>")
			.append(getUsername())
			.append("</username>")
			.toString();
	}
}
