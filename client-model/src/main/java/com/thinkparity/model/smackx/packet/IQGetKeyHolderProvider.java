/*
 * Feb 14, 2006
 */
package com.thinkparity.model.smackx.packet;

import java.util.UUID;

import org.apache.log4j.Logger;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;

import org.xmlpull.v1.XmlPullParser;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQGetKeyHolderProvider implements IQProvider {

	/**
	 * Create a IQGetKeyHolderProvider.
	 */
	public IQGetKeyHolderProvider() {
		super();
		this.logger = Logger.getLogger(getClass());
	}

	/**
	 * @see org.jivesoftware.smack.provider.IQProvider#parseIQ(org.xmlpull.v1.XmlPullParser)
	 */
	public IQ parseIQ(final XmlPullParser parser) throws Exception {
		logger.info("parseIQ(XmlPullParser)");
		logger.debug(parser);
		UUID artifactUniqueId = null;
		JabberId keyHolder = null;

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

			if(XmlPullParser.START_TAG == eventType && "uuid".equals(name)) {
				parser.next();
				artifactUniqueId = UUID.fromString(parser.getText());
				parser.next();
			}
			else if(XmlPullParser.START_TAG == eventType && "jid".equals(name)) {
				isComplete = Boolean.TRUE;
				parser.next();
				keyHolder = JabberIdBuilder.parseQualifiedJabberId(parser.getText());
				parser.next();
			}
		}
		Assert.assertNotNull("", artifactUniqueId);
		Assert.assertNotNull("", keyHolder);
		return new IQGetKeyHolderResponse(artifactUniqueId, keyHolder);
	}

	protected final Logger logger;

}
