/*
 * Jun 6, 2005
 */
package com.thinkparity.model.smackx.document;

import java.io.UnsupportedEncodingException;

import org.jivesoftware.smack.packet.PacketExtension;
import org.xmlpull.v1.XmlPullParser;

import com.thinkparity.model.smackx.XProvider;
import com.thinkparity.model.xmpp.document.XMPPDocument;
import com.thinkparity.model.xstream.XStreamUtil;

/**
 * XMPPDocumentXProvider
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class XMPPDocumentXProvider extends XProvider {

	private static final String xElementName =
		XMPPDocumentPacketX.getXElementName();

	/**
	 * Create a XMPPDocumentXProvider
	 */
	public XMPPDocumentXProvider() { super(); }

	/**
	 * Use the xmlPullParser to create a XMPPDocumentPacketX packet extension.
	 * @see org.jivesoftware.smack.provider.PacketExtensionProvider#parseExtension(org.xmlpull.v1.XmlPullParser)
	 */
	public PacketExtension parseExtension(final XmlPullParser parser)
			throws Exception {
		XMPPDocumentPacketX documentVersionX = null;

		Boolean isDone = Boolean.FALSE;
		while(Boolean.FALSE == isDone) {
			String currentTag = getTag(parser);
			Integer currentTagType = getTagType(parser);

			switch(currentTagType) {
				case XmlPullParser.START_TAG:
					break;
				case XmlPullParser.TEXT:
					documentVersionX = extractExtension(parser);
					break;
				case XmlPullParser.END_TAG:
					if(xElementName.equals(currentTag)) { isDone = Boolean.TRUE; }
					break;
				default:
					break;
			}

			nextTag(parser);			
		}

		return documentVersionX;
	}

	private XMPPDocumentPacketX extractExtension(final XmlPullParser parser)
			throws UnsupportedEncodingException {
		final XMPPDocument xmppDocument =
			(XMPPDocument) XStreamUtil.fromXML(decodeXML(getTagText(parser)));
		final XMPPDocumentPacketX xmppDocumentPacketX = new XMPPDocumentPacketX();
		xmppDocumentPacketX.setXMPPDocument(xmppDocument);
		return xmppDocumentPacketX;
	}
}
