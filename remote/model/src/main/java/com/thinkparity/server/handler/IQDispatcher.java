/*
 * Nov 30, 2005
 */
package com.thinkparity.server.handler;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.jivesoftware.messenger.IQHandlerInfo;
import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.jivesoftware.messenger.handler.IQHandler;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.PacketError;

import com.thinkparity.server.ParityServerConstants;
import com.thinkparity.server.handler.controller.IQController;
import com.thinkparity.server.log4j.ServerLoggerFactory;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.session.Session;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQDispatcher extends IQHandler {

	private static final String IQ_DISPATCHER_NAME;

	private static final IQHandlerInfo IQ_INFO;

	private static final String IQ_INFO_NAME;

	private static final String IQ_INFO_NAMESPACE;

	static {
		IQ_DISPATCHER_NAME = ParityServerConstants.DISPATCHER_NAME;

		IQ_INFO_NAME = ParityServerConstants.IQ_PARITY_INFO_NAME;
		IQ_INFO_NAMESPACE = ParityServerConstants.IQ_PARITY_INFO_NAMESPACE;
		IQ_INFO = new IQHandlerInfo(IQ_INFO_NAME, IQ_INFO_NAMESPACE);
	}

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger = ServerLoggerFactory.getLogger(getClass());

	/**
	 * Create a IQDispatcher.
	 */
	public IQDispatcher() { super(IQ_DISPATCHER_NAME); }

	/**
	 * @see org.jivesoftware.messenger.handler.IQHandler#getInfo()
	 */
	public IQHandlerInfo getInfo() { return IQ_INFO; }

	/**
	 * @see org.jivesoftware.messenger.handler.IQHandler#handleIQ(org.xmpp.packet.IQ)
	 */
	public IQ handleIQ(final IQ iq) throws UnauthorizedException {
		logger.info("handleIQ(IQ)");
		logger.debug(iq);
		final IQAction iqAction = extractIQAction(iq);
		logger.debug(iqAction);
		final IQController iqController = iqAction.getController();
		iqController.setSession(new Session() {
			final JID jid = iq.getFrom();
			public JID getJID() { return jid; }
			
		});
		try { return iqController.handleIQ(iq); }
		catch(ParityServerModelException psmx) {
			logger.error("handleIQ(IQ)", psmx);
			return translate(iq, "handleIQ(IQ)", psmx);
		}
	}

	/**
	 * Translate an error into a packet error.
	 * 
	 * @param iq
	 *            The original iq.
	 * @param errorMesage
	 *            The error message.
	 * @param error
	 *            The error.
	 * @return The error result iq.
	 */
	protected IQ translate(final IQ iq, final String errorMesage, final Throwable error) {
		final IQ errorResult = IQ.createResultIQ(iq);
		errorResult.setError(new PacketError(PacketError.Condition.internal_server_error));
		return errorResult;
	}

	/**
	 * Extract the action (api) from the iq xml document.
	 * 
	 * @param iq
	 *            The iq xml document.
	 * @return The action.
	 */
	private IQAction extractIQAction(final IQ iq) {
		final Element child = iq.getChildElement();
		final Element actionElement = child.element(ElementName.ACTION.getElementName());
		return IQAction.valueOf((String) actionElement.getData());
	}
}
