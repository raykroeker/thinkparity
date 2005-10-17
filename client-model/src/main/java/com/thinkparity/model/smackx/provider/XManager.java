/*
 * May 30, 2005
 */
package com.thinkparity.model.smackx.provider;

import org.jivesoftware.smack.provider.ProviderManager;

import com.thinkparity.model.smackx.ISmackXConstants;
import com.thinkparity.model.smackx.XProvider;
import com.thinkparity.model.smackx.document.XMPPDocumentPacketX;

/**
 * XManager This class is used register the packet extensions for the parity
 * software with the underlying smack library. To add an extension, simply write
 * the appropriate implementations of PacketX, XFilter, XListener and XProvider;
 * then add a register call to the static initializer here.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class XManager {

	/**
	 * Register all of the packet extensions for this application.
	 */
	public static void register() {
		register(
				XMPPDocumentPacketX.getXElementName(), XMPPDocumentPacketX.getXProvider());	
	}

	/**
	 * Register an extension provider. The element name, namespace and provider
	 * are registered so that incoming packet extensions can be interpreted
	 * correctly by provider.
	 * 
	 * @param xElementName
	 *            The element name to register.
	 * @param xProvider
	 *            The instance of the provider to register.
	 */
	private static void register(final String xElementName,
			final XProvider xProvider) {
		ProviderManager.addExtensionProvider(
				xElementName, ISmackXConstants.NAMESPACE, xProvider);
	}

	/**
	 * Create a XManager [Singleton]
	 */
	private XManager() { super(); }
}
