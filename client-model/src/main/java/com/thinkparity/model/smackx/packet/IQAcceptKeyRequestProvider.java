/*
 * Dec 12, 2005
 */
package com.thinkparity.model.smackx.packet;

import java.util.UUID;

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
public class IQAcceptKeyRequestProvider implements IQProvider {

	/**
	 * Assertion message for a null artifact unique id.
	 */
	private static final String ASSERT_ARTIFACT_UUID =
		"The artifact unique id could not be extracted from the key request.";

	/**
	 * Assertion message when the username is null.
	 */
	private static final String ASSERT_JABBER_ID =
		"The jabber id could not be extracted from the key request.";

	/** An apache logger. */
	protected final Logger logger = Logger.getLogger(getClass());

	/**
	 * Create a IQAcceptKeyRequestProvider.
	 */
	public IQAcceptKeyRequestProvider() { super(); }

	/**
	 * @see org.jivesoftware.smack.provider.IQProvider#parseIQ(org.xmlpull.v1.XmlPullParser)
	 */
	public IQ parseIQ(final XmlPullParser parser) throws Exception {
		logger.info("parseIQ(XmlPullParser)");
		logger.debug(parser);
		UUID artifactUUID = null;
		JabberId jabberId = null;

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
				artifactUUID = UUID.fromString(parser.getText());
				parser.next();
			}
			else if(XmlPullParser.START_TAG == eventType && "jid".equals(name)) {
				isComplete = Boolean.TRUE;
				parser.next();
				jabberId = JabberIdBuilder.parseQualifiedJabberId(parser.getText());
				parser.next();
			}
		}
		Assert.assertNotNull(ASSERT_ARTIFACT_UUID, artifactUUID);
		Assert.assertNotNull(ASSERT_JABBER_ID, jabberId);
		return new IQAcceptKeyRequest(artifactUUID, jabberId);
	}
}
