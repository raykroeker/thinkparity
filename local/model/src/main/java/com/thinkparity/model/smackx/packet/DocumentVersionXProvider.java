/*
 * Jun 6, 2005
 */
package com.thinkparity.model.smackx.packet;

import java.io.UnsupportedEncodingException;


import com.thinkparity.model.parity.api.document.DocumentVersion;
import com.thinkparity.model.parity.xml.XmlUtil;
import com.thinkparity.model.smackx.XProvider;

import org.jivesoftware.smack.packet.PacketExtension;
import org.xmlpull.v1.XmlPullParser;

/**
 * DocumentVersionXProvider
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentVersionXProvider extends XProvider {

	private static final String xElementName =
		DocumentVersionX.getXElementName();

	private static final String xNamespace =
		DocumentVersionX.getXNamespace();

	/**
	 * Create a DocumentVersionXProvider
	 */
	public DocumentVersionXProvider() { super(); }

	/**
	 * Use the xmlPullParser to create a DocumentVersionX packet extension.
	 * @see org.jivesoftware.smack.provider.PacketExtensionProvider#parseExtension(org.xmlpull.v1.XmlPullParser)
	 */
	public PacketExtension parseExtension(final XmlPullParser parser)
			throws Exception {
		DocumentVersionX documentVersionX = null;

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

	private DocumentVersionX extractExtension(final XmlPullParser parser)
			throws UnsupportedEncodingException {
		final String currentTagText = getTagText(parser);
		final DocumentVersion documentVersion =
			(DocumentVersion) XmlUtil.fromExtensionXml(currentTagText);
		final DocumentVersionX documentVersionX = new DocumentVersionX();
		documentVersionX.setDocumentVersion(documentVersion);
		return documentVersionX;
	}
}
