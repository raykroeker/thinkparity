/*
 * Mar 1, 2006
 */
package com.thinkparity.model.smackx.packet.artifact;

import java.util.List;

import org.jivesoftware.smack.packet.IQ;

import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQArtifactReadContactsResult extends IQ {

	private final List<User> artifactContacts;

	/**
     * Create a IQReadUsersResult.
     * 
     * @param contacts
     *            A list of contacts.
     */
	IQArtifactReadContactsResult(final List<User> artifactContacts) {
		super();
		this.artifactContacts = artifactContacts;
	}

	/**
	 * @see org.jivesoftware.smack.packet.IQ#getChildElementXML()
	 * 
	 */
	public String getChildElementXML() { return null; }

    /**
     * Obtain the contacts.
     * 
     * @return A list of contacts.
     */
	public List<User> getContacts() { return artifactContacts; }
}
