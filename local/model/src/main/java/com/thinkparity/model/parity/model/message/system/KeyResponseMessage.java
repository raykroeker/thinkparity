/*
 * Feb 24, 2006
 */
package com.thinkparity.model.parity.model.message.system;

import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class KeyResponseMessage extends SystemMessage {

	private Long artifactId;

	private Boolean didAcceptRequest;

	private JabberId responseFrom;

	/**
	 * Create a KeyResponseMessage.
	 */
	public KeyResponseMessage() {
		super();
	}

	/**
	 * @return Returns the didAcceptRequest.
	 */
	public Boolean didAcceptRequest() {
		return didAcceptRequest;
	}

	/**
	 * @return Returns the artifactId.
	 */
	public Long getArtifactId() {
		return artifactId;
	}

	/**
	 * @return Returns the responseFrom.
	 */
	public JabberId getResponseFrom() { return responseFrom; }

	/**
	 * @param artifactId The artifactId to set.
	 */
	public void setArtifactId(Long artifactId) {
		this.artifactId = artifactId;
	}

	/**
	 * @param didAcceptRequest The didAcceptRequest to set.
	 */
	public void setDidAcceptRequest(Boolean didAccept) {
		this.didAcceptRequest = didAccept;
	}

	/**
	 * @param responseFrom The responseFrom to set.
	 */
	public void setResponseFrom(final JabberId responseFrom) {
		this.responseFrom = responseFrom;
	}
}
