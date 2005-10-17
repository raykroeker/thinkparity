/*
 * May 31, 2005
 */
package com.thinkparity.model.smackx.document;

import org.jivesoftware.smack.packet.Packet;

import com.thinkparity.model.smackx.ISmackXConstants;
import com.thinkparity.model.smackx.XListener;
import com.thinkparity.model.xmpp.document.XMPPDocument;

/**
 * The xmpp document extension listener provides a means for extracting the
 * extension from the packet that has been delivered.
 * 
 * @author raykroeker@gmail.com
 * @version 1.2
 */
public abstract class XMPPDocumentXListener extends XListener {

	/**
	 * Element name for the extension.
	 */
	private static final String xElementName =
		XMPPDocumentPacketX.getXElementName();

	/**
	 * Receive the xmpp document.
	 * 
	 * @param xmppDocument
	 *            The xmpp document.
	 */
	public abstract void documentRecieved(final XMPPDocument xmppDocument);

	/**
	 * @see org.jivesoftware.smack.PacketListener#processPacket(org.jivesoftware.smack.packet.Packet)
	 */
	public void processPacket(final Packet packet) {
		final XMPPDocumentPacketX documentVersionX =
			(XMPPDocumentPacketX) packet.getExtension(xElementName, ISmackXConstants.NAMESPACE);
		documentRecieved(documentVersionX.getXMPPDocument());
	}
}
