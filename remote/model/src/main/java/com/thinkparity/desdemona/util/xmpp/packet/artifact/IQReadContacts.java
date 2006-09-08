/*
 * Feb 28, 2006
 */
package com.thinkparity.desdemona.util.xmpp.packet.artifact;

import java.util.List;


import com.thinkparity.desdemona.model.contact.Contact;
import com.thinkparity.desdemona.util.dom4j.NamespaceName;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQReadContacts extends
		com.thinkparity.desdemona.util.xmpp.packet.contact.IQReadContacts {

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
