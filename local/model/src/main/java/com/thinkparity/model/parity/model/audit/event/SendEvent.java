/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.audit.event;

import java.util.HashSet;
import java.util.Set;

import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SendEvent extends AuditEvent {

	private Long artifactVersionId;

	private final Set<JabberId> sentTo;

	/**
	 * Create a SendEvent.
	 * 
	 */
	public SendEvent() {
		super();
		this.sentTo = new HashSet<JabberId>();
	}

	public boolean add(final JabberId username) {
		return sentTo.add(username);
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
	public Set<JabberId> getSentTo() { return sentTo; }

	public boolean remove(final String username) {
		return sentTo.remove(username);
	}

	/**
	 * @param artifactVersionId The artifactVersionId to set.
	 */
	public void setArtifactVersionId(Long versionId) {
		this.artifactVersionId = versionId;
	}
}
