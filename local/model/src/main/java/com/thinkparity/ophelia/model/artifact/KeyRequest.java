/*
 * Mar 22, 2006
 */
package com.thinkparity.ophelia.model.artifact;

import com.thinkparity.codebase.jabber.JabberId;

/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class KeyRequest {

	private Long artifactId;

	private Long id;

	private JabberId requestedBy;

	private String requestedByName;

	/**
	 * Create a KeyRequestFilter.
	 * 
	 */
	public KeyRequest() { super(); }

	/**
	 * @return Returns the artifactId.
	 */
	public Long getArtifactId() {
		return artifactId;
	}

	/**
	 * @return Returns the requestId.
	 */
	public Long getId() { return id; }

	/**
	 * @return Returns the requestedBy.
	 */
	public JabberId getRequestedBy() {
		return requestedBy;
	}

	/**
	 * @return Returns the requestedByName.
	 */
	public String getRequestedByName() {
		return requestedByName;
	}

	/**
	 * @param artifactId The artifactId to set.
	 */
	public void setArtifactId(Long artifactId) {
		this.artifactId = artifactId;
	}

	/**
	 * @param requestId The requestId to set.
	 */
	public void setId(final Long id) { this.id = id; }

	/**
	 * @param requestedBy The requestedBy to set.
	 */
	public void setRequestedBy(JabberId requestedBy) {
		this.requestedBy = requestedBy;
	}

	/**
	 * @param requestedByName The requestedByName to set.
	 */
	public void setRequestedByName(String requestedByName) {
		this.requestedByName = requestedByName;
	}
}
