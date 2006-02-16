/*
 * Feb 16, 2006
 */
package com.thinkparity.model.smackx.packet;

import java.util.List;
import java.util.UUID;

import org.jivesoftware.smack.packet.IQ;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQGetSubscriptionResponse extends IQ {

	/**
	 * Create a IQGetSubscriptionResponse.
	 */
	public IQGetSubscriptionResponse(final UUID artifactUniqueId,
			final List<String> jids) {
		super();
		this.artifactUniqueId = artifactUniqueId;
		this.jids = jids;
	}

	/**
	 * @see org.jivesoftware.smack.packet.IQ#getChildElementXML()
	 * 
	 */
	public String getChildElementXML() { return null; }

	private UUID artifactUniqueId;

	private List<String> jids;

	/**
	 * @return Returns the artifactUniqueId.
	 */
	public UUID getArtifactUniqueId() {
		return artifactUniqueId;
	}

	/**
	 * @return Returns the jids.
	 */
	public List<String> getJids() {
		return jids;
	}
}
