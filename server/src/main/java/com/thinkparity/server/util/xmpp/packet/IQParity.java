/*
 * Nov 29, 2005
 */
package com.thinkparity.desdemona.util.xmpp.packet;

import org.apache.log4j.Logger;

import org.xmpp.packet.IQ;

/**
 * Abstraction of an xmpp internet query. Used primarily to insert a logger and
 * some common functionality.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public abstract class IQParity extends IQ {

	/**
	 * IQAction to perform.
	 */
	protected final Action action;

    /** An apache logger. */
	protected final Logger logger;

	/**
	 * Create a IQParity.
	 */
	protected IQParity(final Action action) {
		super();
		this.action = action;
        this.logger = Logger.getLogger(getClass());
	}

	/**
	 * Obtain the action.
	 * 
	 * @return The action.
	 */
	public Action getAction() { return action; }

	/**
	 * Artifact actions that are possible to perform.
	 * 
	 */
	public enum Action {
		ACCEPTCONTACTINVITATION, ARTIFACTCONFIRMRECEIPT, ARTIFACTREADCONTACTS, CLOSE, CONTAINERREACTIVATE, CREATE, DECLINECONTACTINVITATION, DOCUMENTREACTIVATE, DOCUMENTSEND, FLAG, GETKEYHOLDER, GETKEYS, GETSUBSCRIPTION, INVITECONTACT, NOTIFYTEAMMEMBERADDED, NOTIFYTEAMMEMBERREMOVED, READCONTACTS, READUSERS, REQUESTKEY, SUBSCRIBE, UNSUBSCRIBE
	}
}
