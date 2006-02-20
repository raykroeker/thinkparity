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
	 * Finish xml tag for action. 
	 */
	private static final String XML_ACTION_FINISH = "</action>";
	/**
	 * Start xml tag for action.
	 */
	private static final String XML_ACTION_START = "<action>";

	/**
	 * Finish xml tag for the query.
	 */
	private static final String XML_QUERY_FINISH = "</query>";

	private static final String XML_QUERY_NAMESPACE_ROOT = "jabber:iq:parity:";

	/**
	 * Start xml tag for the query.
	 */
	private static final String XML_QUERY_START = "<query xmlns=\"";

	/**
	 * Action to perform.
	 */
	protected final Action action;

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger = ModelLoggerFactory.getLogger(getClass());

	/**
	 * Create a parity iq.
	 * 
	 * @param action
	 *            The action.
	 */
	protected IQParity(final Action action) {
		super();
		this.action = action;
	}

	/**
	 * Obtain the action.
	 * 
	 * @return The action.
	 */
	public Action getAction() { return action; }

	/**
	 * @see org.jivesoftware.smack.packet.IQ#getChildElementXML()
	 * 
	 */
	public String getChildElementXML() {
		final String xml = new StringBuffer(startQueryXML())
			.append(finishQueryXML()).toString();
		logger.debug(xml);
		return xml;
	}

	/**
	 * Finish xml for the query.
	 * 
	 * @return The finish xml tag for the query.
	 */
	protected String finishQueryXML() { return IQParity.XML_QUERY_FINISH; }

	/**
	 * Obtain the xml tag for the action.
	 * 
	 * @return The xml tag for the action.
	 */
	protected String getActionXML() {
		return new StringBuffer(IQParity.XML_ACTION_START)
			.append(getAction().toString())
			.append(IQParity.XML_ACTION_FINISH).toString();
	}

	/**
	 * Start the xml tag for the query.
	 * 
	 * @return The start xml tag for the query.
	 */
	protected String startQueryXML() {
		return new StringBuffer(IQParity.XML_QUERY_START)
			.append(IQParity.XML_QUERY_NAMESPACE_ROOT)
			.append(action.toString().toLowerCase())
			.append("\">").toString();
	}

	/**
	 * Artifact actions that are possible to perform.
	 * 
	 */
	protected enum Action {
		ACCEPTKEYREQUEST, CLOSEARTIFACT, CREATEARTIFACT, DELETEARTIFACT,
		DENYKEYREQUEST, FLAGARTIFACT, GETKEYHOLDER, GETKEYS, GETSUBSCRIPTION,
		PROCESSOFFLINEQUEUE, REQUESTKEY, SUBSCRIBEUSER, UNSUBSCRIBEUSER
	}
}
