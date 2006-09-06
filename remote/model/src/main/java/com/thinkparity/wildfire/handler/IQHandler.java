/*
 * Dec 1, 2005
 */
package com.thinkparity.wildfire.handler;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;

import org.jivesoftware.messenger.IQHandlerInfo;
import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.jivesoftware.util.JiveProperties;

import org.dom4j.Branch;
import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.PacketError;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.Constants.Xml;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;
import com.thinkparity.codebase.log4j.Log4JHelper;

import com.thinkparity.model.ParityServerModelException;
import com.thinkparity.model.Constants.JivePropertyNames;
import com.thinkparity.model.artifact.Artifact;
import com.thinkparity.model.artifact.ArtifactModel;
import com.thinkparity.model.contact.ContactModel;
import com.thinkparity.model.document.DocumentModel;
import com.thinkparity.model.queue.QueueModel;
import com.thinkparity.model.session.Session;
import com.thinkparity.model.user.UserModel;

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
     * A synchronization lock used to serialize all incoming iq handler
     * requests.
     */
    static final Object SERIALIZER;

    static {
        SERIALIZER = new Object();
    }

    /**
	 * Handle to an apache logger.
	 */
	protected final Logger logger;

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
				Xml.NAME,
				action.getNamespace());
        this.logger = Logger.getLogger(getClass());
	}

	/** @see org.jivesoftware.messenger.handler.IQHandler#getInfo() */
	public IQHandlerInfo getInfo() { return iqHandlerInfo; }

    /** The current session. */
    private Session session;

	/**
	 * @see org.jivesoftware.messenger.handler.IQHandler#handleIQ(org.xmpp.packet.IQ)
	 * 
	 */
	public IQ handleIQ(final IQ iq) throws UnauthorizedException {
        synchronized (SERIALIZER) {
    		try {
    			this.session = new Session() {
                    private final JabberId jabberId = JabberIdBuilder.parseQualifiedJabberId(iq.getFrom().toString());
                    private final JiveProperties jiveProperties = JiveProperties.getInstance();
    
                    public JabberId getJabberId() {
                        return jabberId;
                    }
    
                    public JID getJID() {
                        return iq.getFrom();
                    }
    
                    public String getXmppDomain() {
                        return (String) jiveProperties.get(JivePropertyNames.XMPP_DOMAIN);
                    }
    			};
                logVariable("iq", iq);
    			final IQ resultIQ = handleIQ(iq, session);
                logVariable("resultIQ", resultIQ);
    			return resultIQ;
    		} catch(final Throwable t) {
                Logger.getLogger(getClass()).fatal(getErrorId(t), t);
                return createErrorResponse(iq, t);
    		}
        }
	}

    /**
     * Log a named variable. Note that the logging renderer will be used only
     * for the value.
     * 
     * @param name
     *            A variable name.
     * @param value
     *            A variable.
     * @return The value.
     */
    protected final <T> T logVariable(final String name, final T value) {
        if(logger.isDebugEnabled()) {
            logger.debug(MessageFormat.format("[{0}] [{1}:{2}]",
                    session.getJabberId().getUsername(),
                    name, Log4JHelper.render(logger, value)));
        }
        return value;
    }

    /**
     * Obtain an error id.
     * 
     * @return An error id.
     */
    protected final Object getErrorId(final Throwable t) {
        return MessageFormat.format("{{0}] [{1}] [{2}] - [{3}]",
                    session.getJabberId().getUsername(),
                    StackUtil.getFrameClassName(2),
                    StackUtil.getFrameMethodName(2),
                    t.getMessage());
    }

    /**
     * Create an error response for the query, for the error.
     * 
     * @param iq
     *            The internet query.
     * @param x
     *            The error.
     * @return The error response.
     */
    private IQ createErrorResponse(final IQ iq, final Throwable t) {
        final IQ errorResult = IQ.createResultIQ(iq);

        final PacketError packetError = new PacketError(
                PacketError.Condition.internal_server_error,
                PacketError.Type.cancel, StringUtil.printStackTrace(t));

        errorResult.setError(packetError);
        return errorResult;
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

    protected Set<JabberId> extractJabberIdSet(final IQ iq) {
        final Element childElement = iq.getChildElement();
        final Element jidListElement = getElement(childElement, ElementName.JIDS);
        final List jidElements = getElements(jidListElement, ElementName.JID);
        final Set<JabberId> jabberIds = new HashSet<JabberId>();
        Element jidElement;
        for(final Object o : jidElements) {
            jidElement = (Element) o;
            jabberIds.add(JabberIdBuilder.parseQualifiedJabberId((String) jidElement.getData()));
        }
        return jabberIds;
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

	protected String extractName(final IQ iq) {
        final Element e = iq.getChildElement();
        final Element name = e.element(ElementName.NAME.getName());
        return (String) name.getData();
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
     * Extract a named version id from the xmpp internet query.
     * 
     * @param iq
     *            The xmpp internet query.
     * @return The version id.
     */
    protected Long extractVersionId(final IQ iq) {
        final Element e = iq.getChildElement();
        final Element versionId = e.element(ElementName.VERSIONID.getName());
        return Long.parseLong((String) versionId.getData());
    }

    /** Log an api id. */
    protected final void logApiId() {
        if(logger.isInfoEnabled()) {
            logger.info(MessageFormat.format("[{0}] [{1}] [{2}]",
                    session.getJabberId().getUsername(),
                    StackUtil.getCallerClassName(),
                    StackUtil.getCallerMethodName()));
        }
    }

    /**
     * Obtain the parity artifact interface.
     * 
     * @param session
     *            The user's session.
     * @return The parity artifact interface.
     */
	protected ArtifactModel getArtifactModel(final Session session) {
		return ArtifactModel.getModel(session);
	}

    /**
     * Obtain the parity contact interface.
     * 
     * @param session
     *            The user's session.
     * @return The parity contact interface.
     */
	protected ContactModel getContactModel(final Session session) {
		return ContactModel.getModel(session);
	}

    /**
     * Obtain the parity document interface.
     * 
     * @param session
     *            The user's session.
     * @return The parity document interface.
     */
    protected DocumentModel getDocumentModel(final Session session) {
        return DocumentModel.getModel(session);
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
     * Obtain the parity user interface.
     * 
     * @param session
     *            The user's session.
     * @return The parity user interface.
     */
	protected UserModel getUserModel(final Session session) {
		return UserModel.getModel(session);
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
		errorResult.setError(new PacketError(
				PacketError.Condition.internal_server_error,
				PacketError.Type.cancel, StringUtil.printStackTrace(t)));
		return errorResult;
	}
}
