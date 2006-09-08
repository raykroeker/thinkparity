/*
 * Feb 28, 2006
 */
package com.thinkparity.desdemona.util.xmpp.packet.contact;

import java.util.List;

import org.dom4j.Element;


import com.thinkparity.desdemona.model.contact.Contact;
import com.thinkparity.desdemona.util.dom4j.ElementBuilder;
import com.thinkparity.desdemona.util.dom4j.ElementName;
import com.thinkparity.desdemona.util.dom4j.NamespaceName;
import com.thinkparity.desdemona.util.xmpp.packet.IQParity;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQReadContacts extends IQParity {

	public IQReadContacts(final List<Contact> contacts) {
		this(Action.READCONTACTS, NamespaceName.IQ_READ_CONTACTS, contacts);
        logger.debug(contacts);
	}

	/**
	 * Create a IQReadContacts.
	 * 
	 * @param contacts
	 *            contacts
	 */
	protected IQReadContacts(final Action action,
			final NamespaceName namespaceName, final List<Contact> contacts) {
		super(action);
		final Element element = setChildElement(
				ElementName.QUERY.getName(), namespaceName.getName());

		// contacts
		final Element contactsElement = ElementBuilder.addElement(element, ElementName.CONTACTS);
		Element contactElement;
		Element vCardElement;
		for(final Contact contact : contacts) {
            logger.debug(contact);
            logger.debug(contact.getVCard());
			// contact
			contactElement = ElementBuilder.addElement(contactsElement, ElementName.CONTACT);
			// jid
			ElementBuilder.addElement(contactElement, ElementName.JID, contact.getId().getQualifiedJabberId());
			// vcard
			vCardElement = ElementBuilder.addElement(contactElement, ElementName.VCARD);
			vCardElement.add(contact.getVCard());
		}
	}
}
