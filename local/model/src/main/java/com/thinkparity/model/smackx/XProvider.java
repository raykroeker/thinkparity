/*
 * Jun 6, 2005
 */
package com.thinkparity.model.smackx;

import java.io.IOException;

import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * XProvider
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class XProvider implements PacketExtensionProvider {

	/**
	 * Create a XProvider
	 */
	protected XProvider() { super(); }

	protected String getTag(final XmlPullParser parser) {
		return parser.getName();
	}

	protected String getTagText(final XmlPullParser parser) {
		return parser.getText();
	}

	protected Integer getTagType(final XmlPullParser parser)
			throws XmlPullParserException { return parser.getEventType(); }

	protected void nextTag(final XmlPullParser parser) throws IOException,
			XmlPullParserException { parser.next(); }
}
