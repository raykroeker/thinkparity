/*
 * Dec 11, 2005
 */
package com.thinkparity.model.smackx.packet;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.log4j.ModelLoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQKeyRequestProvider implements IQProvider {

	/**
	 * Assertion message for a null artifact unique id.
	 */
	private static final String ASSERT_ARTIFACT_UUID =
		"The artifact unique id could not be extracted from the key request.";

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger = ModelLoggerFactory.getLogger(getClass());

	/**
	 * Create a IQKeyRequestProvider.
	 */
	public IQKeyRequestProvider() { super(); }

	/**
	 * @see org.jivesoftware.smack.provider.IQProvider#parseIQ(org.xmlpull.v1.XmlPullParser)
	 */
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		logger.info("parseIQ(XmlPullParser)");
		logger.debug(parser);
		UUID artifactUUID = null;

		Integer attributeCount, depth, eventType;
		String name, namespace, prefix, text;
		Boolean isComplete = Boolean.FALSE;
		while(Boolean.FALSE == isComplete) {
			isComplete = Boolean.TRUE;

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

			// found the start of the uuid tag
			if(XmlPullParser.START_TAG == eventType && "uuid".equals(name)) {
				isComplete = Boolean.FALSE;
			}
			// text of the uuid tag
			else if(XmlPullParser.TEXT == eventType) {
				artifactUUID = UUID.fromString(parser.getText());
				isComplete = Boolean.FALSE;
			}
			// end of the uuid tag
			else if(XmlPullParser.END_TAG == eventType && "uuid".equals(name)) {}
		}
		Assert.assertNotNull(ASSERT_ARTIFACT_UUID, artifactUUID);
		return (null == artifactUUID ? null : new IQKeyRequest(artifactUUID));
	}
}
