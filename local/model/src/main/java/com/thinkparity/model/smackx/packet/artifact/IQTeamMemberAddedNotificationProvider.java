/*
 * Mar 30, 2006
 */
package com.thinkparity.model.smackx.packet.artifact;

import java.util.UUID;

import org.apache.log4j.Logger;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.VCardProvider;

import org.xmlpull.v1.XmlPullParser;

import com.thinkparity.model.smack.provider.IQParityProvider;
import com.thinkparity.model.xmpp.JabberIdBuilder;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQTeamMemberAddedNotificationProvider extends IQParityProvider {

	private final Logger logger;

	private final VCardProvider vCardProvider;

	/**
	 * Create a IQTeamMemberAddedNotificationProvider.
	 */
	public IQTeamMemberAddedNotificationProvider() {
		super();
		this.logger = Logger.getLogger(getClass());
		this.vCardProvider = new VCardProvider();
	}

	/**
     * @see com.thinkparity.model.smack.provider.IQParityProvider#parseIQ(org.xmlpull.v1.XmlPullParser)
     * 
     */
	public IQ parseIQ(final XmlPullParser parser) throws Exception {
		logger.info("[LMODEL] [XMPP] [PARSE NEW TEAM MEMBER]");
		logger.debug(parser);
		User user = null;
		UUID artifactUniqueId = null;

		Integer attributeCount, depth, eventType;
		String name, namespace, prefix, text;
		Boolean isComplete = Boolean.FALSE;
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

			if(XmlPullParser.START_TAG == eventType && "uuid".equals(name)) {
				parser.next();
				artifactUniqueId = UUID.fromString(parser.getText());
				parser.next();
			}
			else if(XmlPullParser.END_TAG == eventType && "uuid".equals(name)) {
				parser.next();
			}
			else if(XmlPullParser.START_TAG == eventType && "contact".equals(name)) {
                user = new User();
			}
			else if(XmlPullParser.START_TAG == eventType && "jid".equals(name)) {
				parser.next();
                user.setId(JabberIdBuilder.parseQualifiedJabberId(parser.getText()));
			}
			else if(XmlPullParser.START_TAG == eventType && "vcard".equals(name)) {
				parser.next();
				contactVCard = (VCard) vCardProvider.parseIQ(parser);
                user.setName(contactVCard.getFirstName(), contactVCard.getLastName());
                user.setOrganization(contactVCard.getOrganization());
			}
			else if(XmlPullParser.END_TAG == eventType && "contact".equals(name)) {
				isComplete = Boolean.TRUE;
				parser.next();
			}
		}
		return new IQTeamMemberAddedNotification(artifactUniqueId, user);
	}

	
}
