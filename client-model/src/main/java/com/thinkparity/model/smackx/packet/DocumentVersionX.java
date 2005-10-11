/*
 * Jan 20, 2005
 */
package com.thinkparity.model.smackx.packet;


import com.thinkparity.model.parity.api.document.DocumentVersion;
import com.thinkparity.model.smackx.PacketX;
import com.thinkparity.model.smackx.XProvider;
import com.thinkparity.model.xstream.XStreamUtil;

/**
 * DocumentVersion
 * DocumentVersion is an XMPP extension for the parity DocumentVersion object.
 * It provides the capability to send inline binary files via a message
 * extension.
 * @author raykroeker@gmail.com
 * @version 1.3
 */
public class DocumentVersionX extends PacketX {

	/**
	 * Element used when constructing\deconstructing xml packets.
	 */
	private static final String ELEMENTNAME = "dvx";

	/**
	 * Namespace used when constructing\deconstructing xml packets.
	 */
	private static final String NAMESPACE = "http://thinkparity.com/parity/xmpp/extensions";

	/**
	 * Obtain this extension's element name.
	 * @return <code>java.lang.String</code>
	 */
	public static String getXElementName() { return ELEMENTNAME; }

	/**
	 * Obtain this extension's namespace.
	 * @return <code>java.lang.String</code>
	 */
	public static String getXNamespace() { return NAMESPACE; }

	/**
	 * Obtain this extension's provider.
	 * 
	 * @return <code>com.thinkparity.model.smackx.XProvider</code>
	 */
	public static XProvider getXProvider() {
		return new DocumentVersionXProvider();
	}

	/**
	 * Contains the underlying documnentVersion.
	 */
	private DocumentVersion documentVersion;

	/**
	 * Create an empty DocumentVersionX
	 */
	public DocumentVersionX() { this(null); }

	/**
	 * Create a DocumentVersionX
	 * @param documentVersion
	 */
	public DocumentVersionX(final DocumentVersion documentVersion) {
		super();
		this.documentVersion = documentVersion;
	}

	/**
	 * Obtain documentVersion.
	 * @return <code>DocumentVersion</code>
	 */
	public DocumentVersion getDocumentVersion() {
		return documentVersion;
	}

	/**
	 * @see org.jivesoftware.smack.packet.PacketExtension#getElementName()
	 */
	public String getElementName() { return DocumentVersionX.ELEMENTNAME; }

	/**
	 * @see org.jivesoftware.smack.packet.PacketExtension#getNamespace()
	 */
	public String getNamespace() { return DocumentVersionX.NAMESPACE; }

	/**
	 * @see com.thinkparity.codebase.log4j.Loggable#logMe()
	 */
	public StringBuffer logMe() { return null;}

	/**
	 * Set the value of documentVersion.
	 * @param documentVersion <code>DocumentVersion</code>
	 */
	public void setDocumentVersion(DocumentVersion documentVersion) {
		this.documentVersion = documentVersion;
	}

	/**
	 * @see org.jivesoftware.smack.packet.PacketExtension#toXML()
	 */
	public String toXML() {
		return new StringBuffer("<")
			.append(DocumentVersionX.ELEMENTNAME)
			.append(" xmlns=\"")
			.append(DocumentVersionX.NAMESPACE)
			.append("\">")
			.append(XStreamUtil.toExtensionXml(documentVersion))
			.append("</")
			.append(DocumentVersionX.ELEMENTNAME)
			.append(">").toString();
	}
}
