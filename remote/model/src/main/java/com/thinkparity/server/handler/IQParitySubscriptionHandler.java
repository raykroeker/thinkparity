/*
 * Nov 28, 2005
 */
package com.thinkparity.server.handler;

import org.jivesoftware.messenger.IQHandlerInfo;
import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.server.ParityServerConstants;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQParitySubscriptionHandler extends IQParityAbstractHandler {

	/**
	 * Name of the handler.
	 */
	private static final String HANDLER_NAME;

	/**
	 * Contains information representing the IQHandler.
	 */
	private static final IQHandlerInfo IQ_INFO;

	/**
	 * Name of the iq packet.
	 */
	private static final String IQ_INFO_NAME;

	/**
	 * Namespace of the iq packet.
	 */
	private static final String IQ_INFO_NAMESPACE;

	static {
		HANDLER_NAME = ParityServerConstants.IQ_PARITY_SUBSCRIPTION_HANDER_NAME;

		IQ_INFO_NAME = ParityServerConstants.IQ_PARITY_SUBSCRIPTION_HANDLER_INFO_NAME;
		IQ_INFO_NAMESPACE =
			ParityServerConstants.IQ_PARITY_SUBSCRIPTION_HANDLER_INFO_NAMESPACE;
		IQ_INFO = new IQHandlerInfo(IQ_INFO_NAME, IQ_INFO_NAMESPACE);		
	}

	/**
	 * Create a IQParitySubscriptionHandler.
	 */
	public IQParitySubscriptionHandler() {
		super(IQParitySubscriptionHandler.HANDLER_NAME);
	}

	/**
	 * @see org.jivesoftware.messenger.handler.IQHandler#getInfo()
	 */
	public IQHandlerInfo getInfo() { return IQParitySubscriptionHandler.IQ_INFO; }

	/**
	 * @see org.jivesoftware.messenger.handler.IQHandler#handleIQ(org.xmpp.packet.IQ)
	 */
	public IQ handleIQ(IQ packet) throws UnauthorizedException { return null; }
}
