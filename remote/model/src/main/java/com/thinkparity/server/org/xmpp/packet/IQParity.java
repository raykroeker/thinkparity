/*
 * Nov 29, 2005
 */
package com.thinkparity.server.org.xmpp.packet;

import org.apache.log4j.Logger;
import org.xmpp.packet.IQ;

import com.thinkparity.server.org.apache.log4j.ServerLoggerFactory;

/**
 * Abstraction of an xmpp internet query. Used primarily to insert a logger and
 * some common functionality.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public abstract class IQParity extends IQ {

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger = ServerLoggerFactory.getLogger(getClass());

	/**
	 * Create a IQParity.
	 */
	protected IQParity() { super(); }
}
