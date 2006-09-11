/*
 * Feb 17, 2006
 */
package com.thinkparity.ophelia.model.util.smackx.packet;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import org.jivesoftware.smack.packet.IQ;

import org.xmlpull.v1.XmlPullParser;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.model.util.smack.provider.IQParityProvider;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQGetKeysProvider extends IQParityProvider {

	/**
	 * An apache logger.
	 * 
	 */
	protected final Logger logger;

	/**
	 * Create a IQGetKeysProvider.
	 * 
	 */
	public IQGetKeysProvider() {
		super();
		this.logger = Logger.getLogger(getClass());
	}

	/**
	 * @see com.thinkparity.ophelia.model.util.smack.provider.IQParityProvider#parseIQ(org.xmlpull.v1.XmlPullParser)
	 * 
	 */
	public IQ parseIQ(final XmlPullParser parser) throws Exception {
		logger.info("parseIQ(XmlPullParser)");
		logger.debug(parser);
		final List<UUID> keys = new LinkedList<UUID>();

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

			if(XmlPullParser.START_TAG == eventType && "uuids".equals(name)) {}
			else if(XmlPullParser.START_TAG == eventType && "uuid".equals(name)) {
				parser.next();
				keys.add(UUID.fromString(parser.getText()));
				parser.next();
			}
			else if(XmlPullParser.END_TAG == eventType && "uuids".equals(name)) {
				isComplete = Boolean.TRUE;
				parser.next();
			}
		}
		Assert.assertNotNull("", keys);
		return new IQGetKeysResponse(keys);
	}
}
