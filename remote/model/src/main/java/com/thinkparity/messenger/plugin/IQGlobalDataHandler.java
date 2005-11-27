/*
 * Nov 25, 2005
 */
package com.thinkparity.messenger.plugin;

import org.jivesoftware.messenger.IQHandlerInfo;
import org.jivesoftware.messenger.XMPPServer;
import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.jivesoftware.messenger.handler.IQHandler;
import org.jivesoftware.util.Log;
import org.xmpp.packet.IQ;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQGlobalDataHandler extends IQHandler {

	/**
	 * Global data storage.
	 */
	private final GlobalDataStorage globalDataStorage;

	/**
	 * Definition of the iq.
	 */
	private final IQHandlerInfo info;

	/**
	 * Create an IQGlobalDataHandler.
	 */
	public IQGlobalDataHandler() {
		super(GlobalDataPluginConstants.IQ_GD_HANDLER_NAME);
		Log.info(GlobalDataPluginConstants.IQ_GD_HANDLER_NAME);
		this.globalDataStorage = new GlobalDataStorage();
		this.info = new IQHandlerInfo(
				GlobalDataPluginConstants.IQ_GD_NAME,
				GlobalDataPluginConstants.IQ_GD_NAMESPACE);
	}

	/**
	 * @see org.jivesoftware.messenger.handler.IQHandler#getInfo()
	 */
	public IQHandlerInfo getInfo() { return info; }

	/**
	 * @see org.jivesoftware.messenger.handler.IQHandler#handleIQ(org.xmpp.packet.IQ)
	 */
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		if(isGetQuery(packet)) { Log.info("isGetQuery():true"); }
		else if(isSetQuery(packet)) { Log.info("isSetQuery():true"); }
//else {
			final IQ reply = IQ.createResultIQ(packet);
			reply.setChildElement(
					GlobalDataPluginConstants.IQ_GD_NAME,
					GlobalDataPluginConstants.IQ_GD_NAMESPACE);
			return reply;
//}
	}

	/**
	 * @see org.jivesoftware.messenger.handler.IQHandler#initialize(org.jivesoftware.messenger.XMPPServer)
	 */
	public void initialize(XMPPServer server) {
		super.initialize(server);
		globalDataStorage.initialize(server);
	}

	/**
	 * Determine whether or not the iq is a get query.
	 * 
	 * @param iq
	 *            The iq query.
	 * @return True if the iq query is of type get; false otherwise.
	 */
	private Boolean isGetQuery(final IQ iq) {
		if(null != iq) {
			if(IQ.Type.get.equals(iq.getType())) { return Boolean.TRUE; }
		}
		return Boolean.FALSE;
	}

	/**
	 * Determine whether or not the iq is a set query.
	 * 
	 * @param iq
	 *            The iq.
	 * @return True if the query is of type set; false otherwise.
	 */
	private Boolean isSetQuery(final IQ iq) {
		if(null != iq) {
			if(IQ.Type.set.equals(iq.getType())) { return Boolean.TRUE; }
		}
		return Boolean.FALSE;
	}
}
