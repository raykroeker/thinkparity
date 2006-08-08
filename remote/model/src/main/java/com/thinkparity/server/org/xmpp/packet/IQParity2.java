/*
 * Nov 29, 2005
 */
package com.thinkparity.server.org.xmpp.packet;

import java.util.UUID;

import org.apache.log4j.Logger;

import org.dom4j.Element;
import org.xmpp.packet.IQ;

import com.thinkparity.server.org.dom4j.ElementBuilder;
import com.thinkparity.server.org.dom4j.ElementName;
import com.thinkparity.server.org.dom4j.NamespaceName;
import com.thinkparity.server.org.xmpp.packet.IQParity.Action;

/**
 * Abstraction of an xmpp internet query. Used primarily to insert a logger and
 * some common functionality.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public abstract class IQParity2 extends IQ {

	/** The internet query's action. */
	protected final Action action;

    /** An apache logger. */
	protected final Logger logger;

	/** The root xml element. */
    protected final Element rootElement;

    /**
     * Create a parity internet query.
     * 
     * @param action
     *            The internet query action.
     * @param The
     *            internet query namespace.
     */
	protected IQParity2(final Action action, final NamespaceName namespaceName) {
		super();
        this.action = action;
        this.logger = Logger.getLogger(getClass());
		this.rootElement = setChildElement(
                ElementName.QUERY.getName(),
                namespaceName.getName());
	}

	/**
	 * Obtain the action.
	 * 
	 * @return The action.
	 */
	public Action getAction() { return action; }

    /**
     * Set the document unique id.
     * 
     * @param uniqueId
     *            The document unique id.
     */
    public void setUniqueId(final UUID uniqueId) {
        ElementBuilder.addElement(rootElement, ElementName.UUID, uniqueId);
    }
}
