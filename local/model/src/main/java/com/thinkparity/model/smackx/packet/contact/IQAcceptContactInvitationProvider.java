/*
 * Feb 28, 2006
 */
package com.thinkparity.model.smackx.packet.contact;

import org.apache.log4j.Logger;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;

import org.xmlpull.v1.XmlPullParser;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.JabberIdBuilder;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQAcceptContactInvitationProvider implements IQProvider {

	/**
	 * An apache logger.
	 * 
	 */
	protected final Logger logger;

	/**
	 * Create a IQAcceptContactInvitationProvider.
	 * 
	 */
	public IQAcceptContactInvitationProvider() {
		super();
		this.logger = Logger.getLogger(getClass());
	}

	/**
	 * @see org.jivesoftware.smack.provider.IQProvider#parseIQ(org.xmlpull.v1.XmlPullParser)
	 * 
	 */
	public IQ parseIQ(final XmlPullParser parser) throws Exception {
		logger.info("parseIQ(XmlPullParser)");
		logger.debug(parser);
		JabberId contact = null;

		Integer attributeCount, depth, eventType;
		String name, namespace, prefix, text;
		Boolean isComplete = Boolean.FALSE;
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

			if(XmlPullParser.START_TAG == eventType && "jid".equals(name)) {
				isComplete = Boolean.TRUE;
				parser.next();
				contact = JabberIdBuilder.parseQualifiedJabberId(parser.getText());
				parser.next();
			}
		}
		Assert.assertNotNull("Cannot extract contact info.", contact);
		return new IQAcceptContactInvitation(contact);
	}

}
