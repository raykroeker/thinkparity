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
import com.thinkparity.model.parity.model.session.KeyResponse;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQKeyResponseProvider implements IQProvider {

	/**
	 * Assertion message for a null artifact unique id.
	 */
	private static final String ASSERT_ARTIFACT_UUID =
		"The artifact unique id could not be extracted from the key response.";

	/**
	 * Assertion message for a null key response.
	 */
	private static final String ASSERT_KEY_RESPONSE =
		"The key response could not be extracted from the key response.";

	/**
	 * Assertion message for a malformed response.
	 */
	private static final String ASSERT_UNREACHABLE =
		"Malformed xml.  Could not parse response.";

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger = ModelLoggerFactory.getLogger(getClass());

	/**
	 * Create a IQKeyRequestProvider.
	 */
	public IQKeyResponseProvider() { super(); }

	/**
	 * @see org.jivesoftware.smack.provider.IQProvider#parseIQ(org.xmlpull.v1.XmlPullParser)
	 */
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		logger.info("parseIQ(XmlPullParser)");
		logger.debug(parser);
		UUID artifactUUID = null;
		KeyResponse keyResponse = null;

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

			// found the start of the uuid tag
			if(XmlPullParser.START_TAG == eventType && "uuid".equals(name)) {
				parser.next();
				artifactUUID = UUID.fromString(parser.getText());
				parser.next();
			}
			// found the start of the response tag
			else if(XmlPullParser.START_TAG == eventType && "response".equals(name)) {
				parser.next();
				keyResponse = KeyResponse.valueOf(parser.getText().toUpperCase());
				parser.next();
			}
			// found the end of the response tag; we're done
			else if(XmlPullParser.END_TAG == eventType) {
				if("uuid".equals(name)) { logger.info("End uuid tag."); }
				else if("response".equals(name)) {
					isComplete = Boolean.TRUE;
				}
				else { Assert.assertUnreachable(ASSERT_UNREACHABLE); }
			}
			else { Assert.assertUnreachable(ASSERT_UNREACHABLE); }
		}
		Assert.assertNotNull(ASSERT_ARTIFACT_UUID, artifactUUID);
		Assert.assertNotNull(ASSERT_KEY_RESPONSE, keyResponse);
		return new IQKeyResponse(artifactUUID, keyResponse);
	}
}
