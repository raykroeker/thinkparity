/*
 * Mar 1, 2006
 */
package com.thinkparity.model.smackx.packet.contact;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.VCardProvider;

import org.xmlpull.v1.XmlPullParser;

import com.thinkparity.codebase.VCardBuilder;

import com.thinkparity.model.xmpp.JabberIdBuilder;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQReadContactsProvider implements IQProvider {

	/**
	 * An apache logger.
	 * 
	 */
	private final Logger logger;

	/**
	 * The smack VCard provider.
	 * 
	 */
	private final VCardProvider vCardProvider;

	/**
	 * Create a IQArtifactReadContactsProvider.
	 * 
	 */
	public IQReadContactsProvider() {
		super();
		this.logger = Logger.getLogger(getClass());
		this.vCardProvider = new VCardProvider();
	}

	/**
	 * @see org.jivesoftware.smack.provider.IQProvider#parseIQ(org.xmlpull.v1.XmlPullParser)
	 * 
	 */
	public IQ parseIQ(final XmlPullParser parser) throws Exception {
		logger.info("parseIQ(XmlPullParser)");
		logger.debug(parser);
		final List<Contact> contacts = new ArrayList<Contact>();

		Integer attributeCount, depth, eventType;
		String name, namespace, prefix, text;
		Boolean isComplete = Boolean.FALSE;
		Contact contact = null;
		VCard contactVCard = null;
		while(Boolean.FALSE == isComplete) {
			eventType = parser.next();
			attributeCount = parser.getAttributeCount();
			depth = parser.getDepth();
			name = parser.getName();
			prefix = parser.getPrefix();
			namespace = parser.getNamespace(prefix);
			text = parser.getText();

			logger.debug(attributeCount);
			logger.debug(depth);
			logger.debug(eventType);
			logger.debug(name);
			logger.debug(namespace);
			logger.debug(prefix);
			logger.debug(text);

			if(XmlPullParser.START_TAG == eventType && "contacts".equals(name)) {}
			else if(XmlPullParser.START_TAG == eventType && "contact".equals(name)) {
				contact = new Contact();
			}
			else if(XmlPullParser.START_TAG == eventType && "jid".equals(name)) {
				parser.next();
				contact.setId(JabberIdBuilder.parseQualifiedJabberId(parser.getText()));
			}
			else if(XmlPullParser.START_TAG == eventType && "vcard".equals(name)) {
				parser.next();
				contactVCard = (VCard) vCardProvider.parseIQ(parser);
				contact.setName(contactVCard.getFirstName(), contactVCard.getLastName());
				contact.setOrganization(contactVCard.getOrganization());
                contact.setVCard(VCardBuilder.createVCard(contactVCard));
			}
			else if(XmlPullParser.END_TAG == eventType && "contact".equals(name)) {
				contacts.add(contact);
				contact = null;
			}
			else if(XmlPullParser.END_TAG == eventType && "contacts".equals(name)) {
				isComplete = Boolean.TRUE;
				parser.next();
			}
		}
		return new IQReadContactsResult(contacts);
	}
}
