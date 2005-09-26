/*
 * May 31, 2005
 */
package com.thinkparity.model.smackx.packet;

import com.thinkparity.model.smackx.XListener;

import org.jivesoftware.smack.packet.Packet;

/**
 * DocumentVersionXListener
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class DocumentVersionXListener extends XListener {

	/**
	 * Element name for the extension.
	 */
	private static final String xElementName =
		DocumentVersionX.getXElementName();

	/**
	 * Namespace for the extension.
	 */
	private static final String xNamespace =
		DocumentVersionX.getXNamespace();

	/**
	 * @see org.jivesoftware.smack.PacketListener#processPacket(org.jivesoftware.smack.packet.Packet)
	 */
	public void processPacket(final Packet packet) {
		final DocumentVersionX documentVersionX =
			(DocumentVersionX) packet.getExtension(xElementName, xNamespace);
		processDocumentVersion(documentVersionX);
	}

	/**
	 * Process the document version extension.
	 * 
	 * @param documentVersionX
	 *            <code>com.thinkparity.model.smackx.packet.DocumentVersionX</code>
	 */
	public abstract void processDocumentVersion(
			final DocumentVersionX documentVersionX);
}
