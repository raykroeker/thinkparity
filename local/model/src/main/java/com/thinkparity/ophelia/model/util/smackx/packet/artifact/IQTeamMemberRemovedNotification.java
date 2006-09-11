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
public class IQTeamMemberRemovedNotification extends IQ {

	private UUID artifactUniqueId;

	/**
	 * 
	 */
	private User teamMember;

	/**
	 * Create a IQTeamMemberAddedNotification.
	 * 
	 */
	public IQTeamMemberRemovedNotification(final UUID artifactUniqueId,
			final User teamMember) {
		super();
		this.artifactUniqueId = artifactUniqueId;
		this.teamMember = teamMember;
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
	public User getTeamMember() { return teamMember; }
}
