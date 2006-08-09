/*
 * Feb 20, 2006
 */
package com.thinkparity.model.smackx.packet;

import java.util.UUID;

import org.apache.log4j.Logger;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;

import org.xmlpull.v1.XmlPullParser;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQCloseArtifactProvider implements IQProvider {

	protected final Logger logger;

	/**
	 * Create a IQCloseArtifactProvider.
	 */
	public IQCloseArtifactProvider() {
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
		UUID artifactUniqueId = null;

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
				isComplete = Boolean.TRUE;
				parser.next();
				artifactUniqueId = UUID.fromString(parser.getText());
				parser.next();
			}
		}
		Assert.assertNotNull("", artifactUniqueId);
		return new IQCloseArtifact(artifactUniqueId);
	}
}
