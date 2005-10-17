/*
 * Jan 20, 2005
 */
package com.thinkparity.model.smackx.document;


import com.thinkparity.model.smackx.PacketX;
import com.thinkparity.model.smackx.XProvider;
import com.thinkparity.model.xmpp.document.XMPPDocument;
import com.thinkparity.model.xstream.XStreamUtil;

/**
 * An xmpp document packet x is a packet extension for an xmpp document. This
 * means that this class knows how to write an xmpp document as an xmpp packet.
 * This is done using the x stream library for xml serialization.
 * 
 * @author raykroeker@gmail.com
 * @version 1.2
 */
public class XMPPDocumentPacketX extends PacketX {

	/**
	 * Element used when constructing\deconstructing xml packets.
	 */
	private static final String ELEMENTNAME = "dvx";

	/**
	 * Obtain this extension's element name.
	 * @return <code>java.lang.String</code>
	 */
	public static String getXElementName() { return ELEMENTNAME; }

	/**
	 * Obtain this extension's provider.
	 * 
	 * @return <code>com.thinkparity.model.smackx.XProvider</code>
	 */
	public static XProvider getXProvider() {
		return new XMPPDocumentXProvider();
	}

	/**
	 * The xmpp document to create a packet extension for.
	 */
	private XMPPDocument xmppDocument;

	/**
	 * Create an empty XMPPDocumentPacketX
	 */
	public XMPPDocumentPacketX() { this(null); }

	/**
	 * Create a XMPPDocumentPacketX
	 * @param documentVersion
	 */
	public XMPPDocumentPacketX(final XMPPDocument xmppDocument) {
		super();
		this.xmppDocument = xmppDocument;
	}

	/**
	 * @see org.jivesoftware.smack.packet.PacketExtension#getElementName()
	 */
	public String getElementName() { return XMPPDocumentPacketX.ELEMENTNAME; }

	/**
	 * Obtain the underlying xmpp document.
	 * 
	 * @return The underlying xmpp document.
	 */
	public XMPPDocument getXMPPDocument () { return xmppDocument; }

	/**
	 * Set the underlying xmpp document.
	 * 
	 * @param xmppDocument
	 *            The xmpp document to set.
	 */
	public void setXMPPDocument(final XMPPDocument xmppDocument) {
		this.xmppDocument = xmppDocument;
	}

	/**
	 * Convert the xmpp document to an xmpp extension xml packet. This
	 * conversion will encode the document to a binary format before returning.
	 * 
	 * @see org.jivesoftware.smack.packet.PacketExtension#toXML()
	 */
	public String toXML() {
		return new StringBuffer("<")
			.append(XMPPDocumentPacketX.ELEMENTNAME)
			.append(" xmlns=\"")
			.append(getNamespace())
			.append("\">")
			.append(encode(XStreamUtil.toXML(xmppDocument)))
			.append("</")
			.append(XMPPDocumentPacketX.ELEMENTNAME)
			.append(">").toString();
	}
}
