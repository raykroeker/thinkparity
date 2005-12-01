/*
 * Nov 28, 2005
 */
package com.thinkparity.server.handler;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.jivesoftware.messenger.ClientSession;
import org.jivesoftware.messenger.XMPPServer;
import org.jivesoftware.messenger.handler.IQHandler;
import org.jivesoftware.messenger.user.UserNotFoundException;
import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;
import org.xmpp.packet.IQ.Type;

import com.thinkparity.server.log4j.ServerLoggerFactory;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.artifact.Artifact;
import com.thinkparity.server.model.artifact.ArtifactModel;
import com.thinkparity.server.model.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
abstract class IQParityAbstractHandler extends IQHandler {

	/**
	 * Id xml element.
	 */
	private static final String ELEMENT_NAME_UUID = "uuid";

	/**
	 * Handle to a parity server logger.
	 */
	protected final Logger logger = ServerLoggerFactory.getLogger(getClass());

	/**
	 * Create an IQParityAbstractHandler.
	 * 
	 * @param name
	 *            The handler's name.
	 */
	protected IQParityAbstractHandler(final String name) { super(name); }

	/**
	 * @see org.jivesoftware.messenger.handler.IQHandler#initialize(org.jivesoftware.messenger.XMPPServer)
	 */
	public void initialize(XMPPServer server) {
		super.initialize(server);
		logger.debug(getInfo());
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
	 */
	protected Artifact getArtifact(final IQ iq) throws ParityServerModelException {
		return getArtifactModel().get(getArtifactUUID(iq));
	}

	/**
	 * Extract the artifact's unique id from the iq xml document. The required
	 * child element is: &lt;uuid&gt;&lt;/uuid&gt;
	 * 
	 * @param iq
	 *            The iq xml document.
	 * @return The artifact's unique id.
	 */
	protected UUID getArtifactUUID(final IQ iq) {
		final Element child = iq.getChildElement();
		final Element uuidElement = child.element(IQParityAbstractHandler.ELEMENT_NAME_UUID);
		return UUID.fromString((String) uuidElement.getData());
	}

	/**
	 * Obtain a handle to the artifact model.
	 * 
	 * @return A handle to the artifact model.
	 */
	protected ArtifactModel getArtifactModel() { return ArtifactModel.getModel(); }

	/**
	 * Obtain the user representing the sender of the iq.
	 * 
	 * @param iq
	 *            The iq.
	 * @return The from user.
	 * @throws UserNotFoundException
	 */
	protected User getFromUser(final IQ iq) throws UserNotFoundException {
		final ClientSession session = sessionManager.getSession(iq.getFrom());
		return new User(session.getUsername());
	}

	/**
	 * Determine whether or not iq is of type get.
	 * 
	 * @param iq
	 *            The internet query to test.
	 * @return True if the iq type is get; false otherwise.
	 * @see Type#get
	 */
	protected Boolean isTypeGet(final IQ iq) {
		if(iq.getType().equals(Type.get)) { return Boolean.TRUE; }
		return Boolean.FALSE;
	}

	/**
	 * Determine whether or not the iq is of type set.
	 * 
	 * @param iq
	 *            The internet query to test.
	 * @return True if the iq type is set; false otherwise.
	 * @see Type#set
	 */
	protected Boolean isTypeSet(final IQ iq) {
		if(iq.getType().equals(Type.set)) { return Boolean.TRUE; }
		return Boolean.FALSE;
	}

	/**
	 * Translate an error into a packet error.
	 * 
	 * @param errorMessage
	 *            The error message.
	 * @param error
	 *            The error.
	 * @return The packet error.
	 */
	protected PacketError translate(final String errorMesage,
			final Throwable error) {
		return new PacketError(PacketError.Condition.internal_server_error);
	}
}
