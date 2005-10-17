/*
 * Jun 6, 2005
 */
package com.thinkparity.model.smackx;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.thinkparity.codebase.StringUtil;

import com.thinkparity.model.parity.util.Base64;
import com.thinkparity.model.parity.util.log4j.ModelLoggerFactory;

/**
 * XProvider
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class XProvider implements PacketExtensionProvider {

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger =
		ModelLoggerFactory.getLogger(XProvider.class);

	/**
	 * Create a XProvider
	 */
	protected XProvider() { super(); }

	/**
	 * Decode the xml string from its base 64 encoding.
	 * 
	 * @param encodedXML
	 *            The base 64 encoded xml.
	 * @return The decoded xml string.
	 * @throws UnsupportedEncodingException
	 */
	protected String decodeXML(final String encodedXML)
			throws UnsupportedEncodingException {
		logger.debug(encodedXML);
		final byte[] decodedXMLBytes = Base64.decodeBytes(encodedXML);
		logger.debug(decodedXMLBytes);
		return new String(
				decodedXMLBytes, StringUtil.Charset.ISO_8859_1.getCharsetName());
	}

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
