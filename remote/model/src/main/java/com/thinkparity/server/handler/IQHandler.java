/*
 * Dec 1, 2005
 */
package com.thinkparity.server.handler;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.dom4j.Branch;
import org.dom4j.Element;
import org.jivesoftware.messenger.IQHandlerInfo;
import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.PacketError;

import com.thinkparity.server.ParityServerConstants;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.artifact.Artifact;
import com.thinkparity.server.model.artifact.ArtifactModel;
import com.thinkparity.server.model.session.Session;
import com.thinkparity.server.org.apache.log4j.ServerLoggerFactory;
import com.thinkparity.server.org.dom4j.ElementName;
import com.thinkparity.server.org.jivesoftware.messenger.JIDBuilder;

/**
 * An abstraction of the use-case controller from the MVC paradigm. The IQ
 * controller is a stateless class that handles actions from the IQDispatcher.
 * All state is convened in the IQ as well as the Session.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class IQHandler extends
		org.jivesoftware.messenger.handler.IQHandler {

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger = ServerLoggerFactory.getLogger(getClass());

	/**
	 * Handler information for the iq.
	 */
	private final IQHandlerInfo iqHandlerInfo;

	/**
	 * Create a IQController.
	 */
	protected IQHandler(final IQAction action) {
		super(action.toString());
		this.iqHandlerInfo = new IQHandlerInfo(
				ParityServerConstants.IQ_PARITY_INFO_NAME,
				action.getNamespace());
	}

	/**
	 * @see org.jivesoftware.messenger.handler.IQHandler#getInfo()
	 */
	public IQHandlerInfo getInfo() {
		logger.info("getInfo()");
		logger.debug(iqHandlerInfo);
		return iqHandlerInfo;
	}

	/**
	 * @see org.jivesoftware.messenger.handler.IQHandler#handleIQ(org.xmpp.packet.IQ)
	 */
	public IQ handleIQ(final IQ iq) throws UnauthorizedException {
		logger.info("handleIQ(IQ)");
		logger.debug(iq);
		try {
			final Session session = new Session() {
				final JID jid = iq.getFrom();
				public JID getJID() { return jid; }
			};
			return handleIQ(iq, session);
		}
		catch(ParityServerModelException psmx) {
			logger.error("handleIQ(IQ)", psmx);
			return translate(iq, "handleIQ(IQ)", psmx);
		}
	}

	/**
	 * Handle an iq query.
	 * 
	 * @param iq
	 *            The iq query.
	 * @return The resultant query.
	 * @throws ParityServerModelException
	 * @throws UnauthorizedException
	 * @see IQHandler#createResult(IQ)
	 */
	public abstract IQ handleIQ(final IQ iq, final Session session)
			throws ParityServerModelException, UnauthorizedException;

	/**
	 * Simplicity method to add an element of an enumerated name to another
	 * element.
	 * 
	 * @param element
	 *            The element.
	 * @param elementName
	 *            The new element's name.
	 * @return The new element.
	 * @see Branch#addElement(java.lang.String)
	 */
	protected Element addElement(final Element element,
			final ElementName elementName) {
		return element.addElement(elementName.getName());
	}

	/**
	 * Build an xmpp jid for the given username.
	 * 
	 * @param username
	 *            The username.
	 * @return The xmpp jid.
	 */
	protected JID buildJID(final String username) {
		return JIDBuilder.build(username);
	}

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
		final Element uuidElement = child.element(ElementName.UUID.getName());
		return UUID.fromString((String) uuidElement.getData());
	}

	/**
	 * Obtain a handle to the artifact model.
	 * 
	 * @return A hanlde to the artifact model.
	 */
	protected ArtifactModel getArtifactModel(final Session session) {
		return ArtifactModel.getModel(session);
	}

	/**
	 * Simplicity method to extract a named element from an element.
	 * 
	 * @param element
	 *            The element.
	 * @param elementName
	 *            The element name.
	 * @return The extracted element.
	 * @see Element#element(java.lang.String)
	 */
	protected Element getElement(final Element element,
			final ElementName elementName) {
		return element.element(elementName.getName());
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
	 * Extract the jive id from the iq.
	 * 
	 * @param iq
	 *            The iq.
	 * @return The jive id.
	 */
	protected JID extractJID(final IQ iq) {
		final Element childElement = iq.getChildElement();
		final Element jidElement = getElement(childElement, ElementName.USERNAME);
		return buildJID((String) jidElement.getData());
	}
}
