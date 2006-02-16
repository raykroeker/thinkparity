/*
 * Feb 14, 2006
 */
package com.thinkparity.model.smackx.packet;

import java.util.UUID;

import org.jivesoftware.smack.packet.IQ;

/**
 * A query to the parity server for the current artifact key holder.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQGetKeyHolderResponse extends IQ {

	private UUID artifactUniqueId;

	private String username;

	/**
	 * Create a IQGetKeyHolder.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 * @param username
	 *            The key holder username.
	 */
	public IQGetKeyHolderResponse(final UUID artifactUniqueId,
			final String username) {
		super();
		this.artifactUniqueId = artifactUniqueId;
		this.username = username;
	}

	public String getUsername() { return username; }

	public UUID getArtifactUniqueId() { return artifactUniqueId; }

	/**
	 * @see org.jivesoftware.smack.packet.IQ#getChildElementXML()
	 * 
	 */
	public String getChildElementXML() { return null; }
}
