/*
 * Mar 30, 2006
 */
package com.thinkparity.ophelia.model.util.smackx.packet.artifact;

import java.util.UUID;

import org.jivesoftware.smack.packet.IQ;

import com.thinkparity.codebase.model.user.User;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQTeamMemberAddedNotification extends IQ {

	private UUID artifactUniqueId;

	/**
	 * 
	 */
	private User newTeamMember;

	/**
	 * Create a IQTeamMemberAddedNotification.
	 * 
	 */
	public IQTeamMemberAddedNotification(final UUID artifactUniqueId,
			final User newTeamMember) {
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
	public User getNewTeamMember() { return newTeamMember; }
}
