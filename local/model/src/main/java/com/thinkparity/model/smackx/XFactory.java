/*
 * Jun 6, 2005
 */
package com.thinkparity.model.smackx;


import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.smackx.document.XMPPDocumentPacketX;
import com.thinkparity.model.xmpp.XMPPSerializable;
import com.thinkparity.model.xmpp.document.XMPPDocument;

/**
 * Each xmpp serializable object will have a family of smack packet extension
 * objects associtated with it. The objects are classified as follows.
 * <ul>
 * <li>PacketX - The packet extension for the object. The packet extension
 * knows how to convert the object (xmpp serializable) into an xml packet using
 * the XStream serialization library.
 * <li>XListener - The raw packet listener. This interface delivers the raw
 * xmpp packet to the implementer for processing.
 * <li>XFilter - The event handler for the arrival of the object from another
 * user. When the object arrives here, it has already been parsed into the xmpp
 * serializable object and can be handled as such.
 * <li>XProvider - The provider is responsible for converting the raw xmpp
 * packet into the xmpp serializable object.
 * 
 * @author raykroeker@gmail.com
 * @version 1.2
 */
public class XFactory {

	/**
	 * Singleton instance of the factory.
	 * @see XFactory#singletonLock
	 */
	private static final XFactory singleton;

	/**
	 * Synchronization lock for the singleton.
	 * @see XFactory#singleton
	 */
	private static final Object singletonLock;

	static {
		singleton = new XFactory();
		singletonLock = new Object();
	}

	/**
	 * Create a smack packet extension for a given xmpp serializable object.
	 * 
	 * @param xmppSerializable
	 *            The xmpp serializable object to create an extension for.
	 * @return The smack packet extension.
	 */
	public static PacketX createPacketX(final XMPPSerializable xmppSerializable) {
		synchronized(singletonLock) {
			return singleton.createPacketXImpl(xmppSerializable);
		}
	}

	/**
	 * Create a XFactory [Singleton]
	 */
	private XFactory() { super(); }

	/**
	 * 
	 * @param xmppSerializable
	 * @return
	 */
	private PacketX createPacketXImpl(final XMPPSerializable xmppSerializable) {
		if(xmppSerializable instanceof XMPPDocument) {
			return createXMPPDocumentPacketX((XMPPDocument) xmppSerializable);
		}
		throw Assert.createUnreachable(
				xmppSerializable.getClass() + " does not provide a packet extension.");
	}

	/**
	 * Create an instance of an xmpp document packet extension.
	 * 
	 * @param xmppDocument
	 *            The xmpp document to base the extension on.
	 * @return The xmpp document packet extension.
	 */
	private XMPPDocumentPacketX createXMPPDocumentPacketX(final XMPPDocument xmppDocument) {
		return new XMPPDocumentPacketX(xmppDocument);
	}
}
