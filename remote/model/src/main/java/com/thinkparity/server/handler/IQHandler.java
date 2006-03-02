/*
 * Dec 1, 2005
 */
package com.thinkparity.server.handler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.dom4j.Branch;
import org.dom4j.Element;
import org.jivesoftware.messenger.IQHandlerInfo;
import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.PacketError;

import com.thinkparity.codebase.StringUtil.Separator;

import com.thinkparity.server.JabberId;
import com.thinkparity.server.JabberIdBuilder;
import com.thinkparity.server.ParityServerConstants;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.artifact.Artifact;
import com.thinkparity.server.model.artifact.ArtifactModel;
import com.thinkparity.server.model.contact.ContactModel;
import com.thinkparity.server.model.queue.QueueModel;
import com.thinkparity.server.model.session.Session;
import com.thinkparity.server.model.user.UserModel;
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
		logger.debug(iqHandlerInfo);
		return iqHandlerInfo;
	}

	/**
	 * @see org.jivesoftware.messenger.handler.IQHandler#handleIQ(org.xmpp.packet.IQ)
	 * 
	 */
	public IQ handleIQ(final IQ iq) throws UnauthorizedException {
		logger.debug(iq);
		try {
			final Session session = new Session() {
				final JID jid = iq.getFrom();
				final JabberId jabberId = JabberIdBuilder.parseQualifiedJabberId(jid.toString());
				public JabberId getJabberId() { return jabberId; }
				public JID getJID() { return jid; }
			};
			logger.debug(session);
			final IQ resultIQ = handleIQ(iq, session);
			logger.debug(resultIQ);
			return resultIQ;
		}
		catch(final UnauthorizedException ux) {
			logger.error("handleIQ(IQ)", ux);
			throw ux;
		}
		catch(final Throwable t) {
			logger.error("handleIQ(IQ)", t);
			return translate(iq, "An un-expected error has occured.", t);
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
		return artifactModel.get(extractUniqueId(iq));
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
		final Element jidElement = getElement(childElement, ElementName.JID);
		return JIDBuilder.buildQualified((String) jidElement.getData());
	}

	protected JabberId extractJabberId(final IQ iq) {
		final Element childElement = iq.getChildElement();
		final Element jidElement = getElement(childElement, ElementName.JID);
		return JabberIdBuilder.parseQualifiedJabberId((String) jidElement.getData());
	}

	protected List<JabberId> extractJabberIds(final IQ iq) {
		final Element childElement = iq.getChildElement();
		final Element jidListElement = getElement(childElement, ElementName.JIDS);
		final List jidElements = getElements(jidListElement, ElementName.JID);
		final List<JabberId> jabberIds = new LinkedList<JabberId>();
		Element jidElement;
		for(final Object o : jidElements) {
			jidElement = (Element) o;
			jabberIds.add(JabberIdBuilder.parseQualifiedJabberId((String) jidElement.getData()));
		}
		return jabberIds;
	}

	/**
	 * Extract the artifact's unique id from the iq xml document. The required
	 * child element is: &lt;uuid&gt;&lt;/uuid&gt;
	 * 
	 * @param iq
	 *            The iq xml document.
	 * @return The artifact's unique id.
	 */
	protected UUID extractUniqueId(final IQ iq) {
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

	protected UserModel getUserModel(final Session session) {
		return UserModel.getModel(session);
	}

	protected ContactModel getContactModel(final Session session) {
		return ContactModel.getModel(session);
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
	 * Grab all elements of the given name from the element.
	 * 
	 * @param element
	 *            The element.
	 * @param elementName
	 *            The element name.
	 * @return All elements.
	 */
	protected List getElements(final Element element,
			final ElementName elementName) {
		return element.elements(elementName.getName());
	}

	protected QueueModel getQueueModel(final Session session) {
		return QueueModel.getModel(session);
	}

	/**
	 * Translate an error into a packet error.
	 * 
	 * @param iq
	 *            The original iq.
	 * @param errorMessage
	 *            The error message.
	 * @param error
	 *            The error.
	 * @return The error result iq.
	 */
	protected IQ translate(final IQ iq, final String errorMessage,
			final Throwable t) {
		final IQ errorResult = IQ.createResultIQ(iq);
		final String text = new StringBuffer(errorMessage)
			.append(Separator.SystemNewLine)
			.append(createTrace(t))
			.toString();
		errorResult.setError(new PacketError(
				PacketError.Condition.internal_server_error,
				PacketError.Type.cancel, text));
		return errorResult;
	}

	private String createTrace(final Throwable t) {
		final StringWriter stringWriter = new StringWriter();
		t.fillInStackTrace();
		t.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}
}
