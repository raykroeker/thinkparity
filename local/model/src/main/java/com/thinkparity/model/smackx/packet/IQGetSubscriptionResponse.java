/*
 * Feb 16, 2006
 */
package com.thinkparity.model.smackx.packet;

import java.util.List;
import java.util.UUID;

import org.jivesoftware.smack.packet.IQ;

import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQGetSubscriptionResponse extends IQ {

	private UUID artifactUniqueId;

	private List<JabberId> jids;

	/**
	 * Create a IQGetSubscriptionResponse.
	 */
	public IQGetSubscriptionResponse(final UUID artifactUniqueId,
			final List<JabberId> jids) {
		super();
		this.artifactUniqueId = artifactUniqueId;
		this.jids = jids;
	}

	/**
	 * @return Returns the artifactUniqueId.
	 */
	public UUID getArtifactUniqueId() {
		return artifactUniqueId;
	}

	/**
	 * @see org.jivesoftware.smack.packet.IQ#getChildElementXML()
	 * 
	 */
	public String getChildElementXML() { return null; }

	/**
	 * @return Returns the jids.
	 */
	public List<JabberId> getJids() { return jids; }
}
