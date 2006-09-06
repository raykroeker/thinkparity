/*
 * Created On: Jun 22, 2006 2:51:33 PM
 */
package com.thinkparity.wildfire.handler;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.jivesoftware.util.JiveProperties;

import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;
import com.thinkparity.codebase.log4j.Log4JHelper;

import com.thinkparity.model.Constants.JivePropertyNames;
import com.thinkparity.model.artifact.ArtifactModel;
import com.thinkparity.model.artifact.ArtifactType;
import com.thinkparity.model.contact.ContactModel;
import com.thinkparity.model.container.ContainerModel;
import com.thinkparity.model.profile.ProfileEMail;
import com.thinkparity.model.profile.ProfileModel;
import com.thinkparity.model.session.Session;
import com.thinkparity.model.xmpp.IQReader;
import com.thinkparity.model.xmpp.IQWriter;


/**
 * <b>Title:</b>thinkParity Model Controller <br>
 * <b>Description:</b>An abstraction of an xmpp controller.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.9
 */
public abstract class AbstractHandler extends
        com.thinkparity.codebase.wildfire.handler.AbstractHandler {

    /** A thinkParity artifact model interface. */
    private ArtifactModel artifactModel;

    /** A thinkParity contact model interface. */
    private ContactModel contactModel;

    /** A thinkParity container model interface. */
    private ContainerModel containerModel;

    /** A custom iq reader. */
    private IQReader iqReader;

    /** A custom iq writer. */
    private IQWriter iqWriter;

    /** An apache logger. */
    private final Logger logger;

    /** A thinkParity profile interface. */
    private ProfileModel profileModel;

    /** A thinkParity user session. */
    private Session session;

    /** Create AbstractHandler. */
    public AbstractHandler(final String action) {
        super(action);
        this.logger = Logger.getLogger(getClass());
    }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#createReader(org.xmpp.packet.IQ)
     */
    public IQReader createReader(final IQ iq) {
        iqReader = new IQReader(iq);
        return iqReader;
    }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#createWriter(org.xmpp.packet.IQ)
     */
    public IQWriter createWriter(final IQ iq) {
        iqWriter = new IQWriter(iq);
        return iqWriter;
    }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#handleIQ(org.xmpp.packet.IQ)
     */
    public IQ handleIQ(final IQ iq) throws UnauthorizedException {
        synchronized (IQHandler.SERIALIZER) {
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
            this.artifactModel = ArtifactModel.getModel(session);
            this.contactModel = ContactModel.getModel(session);
            this.containerModel = ContainerModel.getModel(session);
            this.profileModel = ProfileModel.getModel(session);
            logVariable("iq", iq);
            final IQ response = super.handleIQ(iq);
            logVariable("response", response);
            return response;
        }
    }

    /**
     * Obtain the artifact model.
     * 
     * @return The artifact model.
     * @see #handleIQ(IQ)
     */
    protected ArtifactModel getArtifactModel() { return artifactModel; }

    /**
     * Obtain the contact model.
     * @return The contact model.
     * @see #handleIQ(IQ)
     */
    protected ContactModel getContactModel() { return contactModel; }

    /**
     * Obtain the contianer model.
     * 
     * @return The container model.
     * @see #handleIQ(IQ)
     */
    protected ContainerModel getContainerModel() { return containerModel; }

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
     * Obtain the thinkParity profile interface.
     * 
     * @return A thinkParity profile interface.
     */
    protected ProfileModel getProfileModel() { return profileModel; }

    /** Log an api id. */
    protected final void logApiId() {
        if(logger.isInfoEnabled()) {
            logger.info(MessageFormat.format("[{0}] [{1}] [{2}]",
                    session.getJabberId().getUsername(),
                    StackUtil.getCallerClassName(),
                    StackUtil.getCallerMethodName()));
        }
    }

    /** Log a trace id. */
    protected final void logTraceId() {
        if (logger.isInfoEnabled()) {
            logger.info(MessageFormat.format("[{0}] [{1}] [{2}:{3}]",
                    session.getJabberId().getUsername(),
                    StackUtil.getCallerClassName(),
                    StackUtil.getCallerMethodName(),
                    StackUtil.getCallerLineNumber()));
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
     * Read an artifact type parameter.
     * 
     * @param name
     *            The parameter name.
     * @return The artifact type.
     */ 
    protected final ArtifactType readArtifactType(final String name) {
        return iqReader.readArtifactType(name);
    }

    /**
     * Read a unique id parameter.
     * 
     * @param name
     *            The parameter name.
     * @return The unique id.
     */
    protected final UUID readUUID(final String name) {
        return iqReader.readUUID(name);
    }

    protected final void writeProfileEMails(final String name,
            final List<ProfileEMail> profileEMails) {
        iqWriter.writeProfileEMails(name, profileEMails);
    }
}
