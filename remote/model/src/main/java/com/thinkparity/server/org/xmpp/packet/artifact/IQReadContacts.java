/*
 * Feb 28, 2006
 */
package com.thinkparity.server.org.xmpp.packet.artifact;

import java.util.List;

import com.thinkparity.model.contact.Contact;

import com.thinkparity.server.org.dom4j.NamespaceName;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQReadContacts extends
		com.thinkparity.server.org.xmpp.packet.contact.IQReadContacts {

	/**
	 * Create a IQReadContacts.
	 * 
	 * @param contacts
	 *            contacts
	 */
	public IQReadContacts(final List<Contact> contacts) {
		super(Action.ARTIFACTREADCONTACTS,
				NamespaceName.IQ_ARTIFACT_READ_CONTACTS, contacts);
	}
}
