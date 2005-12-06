/*
 * Dec 1, 2005
 */
package com.thinkparity.server.handler.controller;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.server.handler.ElementName;
import com.thinkparity.server.log4j.ServerLoggerFactory;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.artifact.Artifact;
import com.thinkparity.server.model.artifact.ArtifactModel;
import com.thinkparity.server.model.session.Session;

/**
 * An abstraction of the use-case controller from the MVC paradigm. The IQ
 * controller is a stateless class that handles actions from the IQDispatcher.
 * All state is convened in the IQ as well as the Session.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class IQController {

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger = ServerLoggerFactory.getLogger(getClass());

	/**
	 * Session information.
	 */
	private Session session;

	/**
	 * Create a IQController.
	 */
	protected IQController() { super(); }

	/**
	 * Obtain the current session information.
	 * 
	 * @return The session.
	 */
	public Session getSession() { return session; }

	/**
	 * Handle an iq query.
	 * 
	 * @param iq
	 *            The iq query.
	 * @return The resultant query.
	 * @throws ParityServerModelException
	 * @throws UnauthorizedException
	 * @see IQController#createResult(IQ)b
	 */
	public abstract IQ handleIQ(final IQ iq) throws ParityServerModelException,
			UnauthorizedException;

	/**
	 * Set the session for the current iq.
	 * 
	 * @param session
	 *            The session.
	 */
	public void setSession(final Session session) { this.session = session; }

	/**
	 * Create a resultant internet query; using iq as the basis.
	 * 
	 * @param iq
	 *            The basis iq.
	 * @return A result iq.
	 * @see IQ#createResultIQ(org.xmpp.packet.IQ)
	 */
	protected IQ createResult(final IQ iq) { return IQ.createResultIQ(iq); }

	/**
	 * Extract the artifact's unique id from the iq xml document; and build an
	 * artifact reference via the model interface. The required child element
	 * is: &lt;uuid&gt;&lt;/uuid&gt;
	 * 
	 * @param iq
	 *            The iq xml document.
	 * @return The artifact.
	 * @throws ParityServerModelException
	 */
	protected Artifact extractArtifact(final ArtifactModel artifactModel,
			final IQ iq) throws ParityServerModelException {
		return artifactModel.get(extractUUID(iq));
	}

	/**
	 * Extract the artifact's unique id from the iq xml document. The required
	 * child element is: &lt;uuid&gt;&lt;/uuid&gt;
	 * 
	 * @param iq
	 *            The iq xml document.
	 * @return The artifact's unique id.
	 */
	protected UUID extractUUID(final IQ iq) {
		final Element child = iq.getChildElement();
		final Element uuidElement = child.element(ElementName.UUID.getElementName());
		return UUID.fromString((String) uuidElement.getData());
	}

	/**
	 * Obtain a handle to the artifact model.
	 * 
	 * @return A hanlde to the artifact model.
	 */
	protected ArtifactModel getArtifactModel() {
		return ArtifactModel.getModel(getSession());
	}
}
