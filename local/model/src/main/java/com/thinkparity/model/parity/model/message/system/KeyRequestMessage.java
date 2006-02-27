/*
 * Feb 24, 2006
 */
package com.thinkparity.model.parity.model.message.system;

import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class KeyRequestMessage extends SystemMessage {

	private Long artifactId;

	private JabberId requestedBy;

	/**
	 * Create a KeyRequestMessage.
	 * 
	 */
	public KeyRequestMessage() { super(); }

	/**
	 * @return Returns the artifactId.
	 */
	public Long getArtifactId() {
		return artifactId;
	}

	/**
	 * @return Returns the requestedBy.
	 */
	public JabberId getRequestedBy() { return requestedBy; }

	/**
	 * @param artifactId The artifactId to set.
	 */
	public void setArtifactId(Long artifactId) {
		this.artifactId = artifactId;
	}

	/**
	 * @param requestedBy The requestedBy to set.
	 */
	public void setRequestedBy(final JabberId requestFrom) {
		this.requestedBy = requestFrom;
	}
}
