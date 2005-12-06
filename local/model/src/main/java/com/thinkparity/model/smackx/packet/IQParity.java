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
 * @version 1.1.2.2
 */
public abstract class IQParity extends IQ {

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger = ModelLoggerFactory.getLogger(getClass());

	/**
	 * Create a IQParity.
	 */
	protected IQParity() { super(); }
}
