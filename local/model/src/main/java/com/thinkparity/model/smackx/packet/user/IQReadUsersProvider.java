/*
 * Mar 1, 2006
 */
package com.thinkparity.model.smackx.packet.user;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.VCardProvider;

import org.xmlpull.v1.XmlPullParser;

import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.model.Constants.VCardFields;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQReadUsersProvider implements IQProvider {

	/**
	 * An apache logger.
	 * 
	 */
	private final Logger logger;

	/**
	 * Handle to the smack VCard provider.
	 * 
	 */
	private final VCardProvider vCardProvider;

	/**
	 * Create a IQReadUsersProvider.
	 */
	public IQReadUsersProvider() {
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
		final Set<User> users = new HashSet<User>();

		Integer attributeCount, depth, eventType;
		String name, namespace, prefix, text;
		Boolean isComplete = Boolean.FALSE;
		User user = null;
		VCard userVCard = null;
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

			if(XmlPullParser.START_TAG == eventType && "users".equals(name)) {}
			else if(XmlPullParser.START_TAG == eventType && "user".equals(name)) {
				user = new User();
			}
			else if(XmlPullParser.START_TAG == eventType && "jid".equals(name)) {
				parser.next();
				user.setId(JabberIdBuilder.parseQualifiedJabberId(parser.getText()));
			}
			else if(XmlPullParser.START_TAG == eventType && "vcard".equals(name)) {
				parser.next();
				userVCard = (VCard) vCardProvider.parseIQ(parser);
				user.setName(userVCard.getFirstName(), userVCard.getLastName());
				user.setOrganization(userVCard.getOrganization());
                user.setTitle(userVCard.getField(VCardFields.TITLE));
			}
			else if(XmlPullParser.END_TAG == eventType && "user".equals(name)) {
				users.add(user);
				user = null;
			}
			else if(XmlPullParser.END_TAG == eventType && "users".equals(name)) {
				isComplete = Boolean.TRUE;
				parser.next();
			}
		}
		return new IQReadUsersResult(users);
	}
}
