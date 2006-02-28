/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.xmpp.JabberId;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SendKeyEvent extends AuditEvent {

	private Long artifactVersionId;

	private JabberId sentTo;

	/**
	 * Create a SendKeyEvent.
	 */
	public SendKeyEvent() {
		super();
	}

	/**
	 * @return Returns the artifactVersionId.
	 */
	public Long getArtifactVersionId() {
		return artifactVersionId;
	}

	/**
	 * @return Returns the sentTo.
	 */
	public JabberId getSentTo() {
		return sentTo;
	}

	/**
	 * @param artifactVersionId The artifactVersionId to set.
	 */
	public void setArtifactVersionId(Long artifactVersionId) {
		this.artifactVersionId = artifactVersionId;
	}

	/**
	 * @param sentTo The sentTo to set.
	 */
	public void setSentTo(final JabberId sentTo) {
		this.sentTo = sentTo;
	}
}
