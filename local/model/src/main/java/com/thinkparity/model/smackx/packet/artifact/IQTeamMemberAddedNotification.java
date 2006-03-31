/*
 * Mar 30, 2006
 */
package com.thinkparity.model.smackx.packet.artifact;

import java.util.UUID;

import org.jivesoftware.smack.packet.IQ;

import com.thinkparity.model.xmpp.contact.Contact;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQTeamMemberAddedNotification extends IQ {

	private UUID artifactUniqueId;

	/**
	 * 
	 */
	private Contact newTeamMember;

	/**
	 * Create a IQTeamMemberAddedNotification.
	 * 
	 */
	public IQTeamMemberAddedNotification(final UUID artifactUniqueId,
			final Contact newTeamMember) {
		super();
		this.artifactUniqueId = artifactUniqueId;
		this.newTeamMember = newTeamMember;
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
     * Obtain the new team member contact info.
     * 
     * @return The contact info.
     */
	public Contact getNewTeamMember() { return newTeamMember; }
}
