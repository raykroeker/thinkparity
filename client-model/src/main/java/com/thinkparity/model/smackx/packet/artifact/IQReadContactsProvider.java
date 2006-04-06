/*
 * Mar 1, 2006
 */
package com.thinkparity.model.smackx.packet.artifact;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.xmlpull.v1.XmlPullParser;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.xmpp.JabberIdBuilder;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

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
	 * Create a IQReadUsersProvider.
	 * 
	 */
	public IQReadContactsProvider() {
		super();
		this.logger = ModelLoggerFactory.getLogger(getClass());
		this.vCardProvider = new VCardProvider();
	}

	/**
	 * @see org.jivesoftware.smack.provider.IQProvider#parseIQ(org.xmlpull.v1.XmlPullParser)
	 * 
	 */
	public IQ parseIQ(final XmlPullParser parser) throws Exception {
		logger.info("parseIQ(XmlPullParser)");
		logger.debug(parser);
		final Set<User> contacts = new HashSet<User>();

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
				contact.setFirstName(contactVCard.getFirstName());
				contact.setLastName(contactVCard.getLastName());
				contact.setOrganization(contactVCard.getOrganization());
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
