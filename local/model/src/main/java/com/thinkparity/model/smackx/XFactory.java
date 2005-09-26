/*
 * Jun 6, 2005
 */
package com.thinkparity.model.smackx;


import com.thinkparity.model.parity.api.document.DocumentVersion;
import com.thinkparity.model.smackx.packet.DocumentVersionX;

/**
 * XFactory
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class XFactory {

	/**
	 * Create a new packet extension based upon a given object.
	 * 
	 * @param object
	 * @return The new packet extension for the given object.
	 * @throws UnsupportedXTypeException
	 */
	public static PacketX createPacketX(final Object object)
			throws UnsupportedXTypeException {
		if (object instanceof DocumentVersion) {
			return createDocumentVersionX(object);
		}
		throw new UnsupportedXTypeException(object);
	}

	/**
	 * Create a packet extension for
	 * <code>com.thinkparity.model.parity.api.document.DocumentVersion</code>
	 * 
	 * @param object
	 *            <code>java.lang.Object</code>
	 * @return <code>com.thinkparity.model.smackx.packet.DocumentVersionX</code>
	 */
	private static PacketX createDocumentVersionX(final Object object) {
		return new DocumentVersionX((DocumentVersion) object);
	}

	/**
	 * Create a XFactory [Singleton]
	 */
	private XFactory() { super(); }
}
