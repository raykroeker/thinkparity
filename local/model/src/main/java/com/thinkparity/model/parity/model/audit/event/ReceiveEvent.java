/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.xmpp.user.User;



/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ReceiveEvent extends AuditEvent {

	private Long artifactVersionId;

	private User receivedFrom;

	/**
	 * Create a ReceiveEvent.
	 */
	public ReceiveEvent() {
		super();
	}

	/**
	 * @return Returns the artifactVersionId.
	 */
	public Long getArtifactVersionId() {
		return artifactVersionId;
	}

	/**
	 * @return Returns the receivedFrom.
	 */
	public User getReceivedFrom() { return receivedFrom; }

	/**
	 * @param artifactVersionId The artifactVersionId to set.
	 */
	public void setArtifactVersionId(Long artifactVersionId) {
		this.artifactVersionId = artifactVersionId;
	}

	/**
	 * @param receivedFrom The receivedFrom to set.
	 */
	public void setReceivedFrom(final User receivedFrom) {
		this.receivedFrom = receivedFrom;
	}
}
