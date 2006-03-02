/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.xmpp.JabberId;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ReceiveKeyEvent extends AuditEvent {

	private Long artifactVersionId;

	private JabberId receivedFrom;

	/**
	 * Create a ReceiveKeyEvent.
	 */
	public ReceiveKeyEvent() {
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
	public JabberId getReceivedFrom() {
		return receivedFrom;
	}

	/**
	 * @param artifactVersionId The artifactVersionId to set.
	 */
	public void setArtifactVersionId(Long artifactVersionId) {
		this.artifactVersionId = artifactVersionId;
	}

	/**
	 * @param receivedFrom The receivedFrom to set.
	 */
	public void setReceivedFrom(final JabberId receivedFrom) {
		this.receivedFrom = receivedFrom;
	}
}
