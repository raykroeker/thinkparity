/*
 * Nov 29, 2005
 */
package com.thinkparity.model.smackx.packet;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.packet.IQ;

import com.thinkparity.model.log4j.ModelLoggerFactory;

/**
 * Abstraction of an xmpp internet query. Used primarily to insert a logger and
 * some common functionality.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class IQParity extends IQ {

	/**
	 * IQ xml namepsace.
	 */
	private static final String ROOT_NAMESPACE = "jabber:iq:parity";

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger = ModelLoggerFactory.getLogger(getClass());

	/**
	 * Create a IQParity.
	 */
	protected IQParity() { super(); }

	/**
	 * Obtain the xml namespace.
	 * 
	 * @return The xml namespace.
	 */
	protected String getRootNamespace() { return ROOT_NAMESPACE; }
}
