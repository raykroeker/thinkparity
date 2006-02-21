/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.audit.event;

import java.util.HashSet;
import java.util.Set;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SendEvent extends AbstractAuditEvent {

	private Long artifactVersionId;

	private final Set<String> sentTo;

	/**
	 * Create a SendEvent.
	 * 
	 */
	public SendEvent() {
		super();
		this.sentTo = new HashSet<String>();
	}

	public boolean add(final String username) {
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
	public Set<String> getSentTo() { return sentTo; }

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
