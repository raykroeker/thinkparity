/*
 * Created On: Jun 22, 2006 2:51:33 PM
 * $Id$
 */
package com.thinkparity.server.handler;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.jivesoftware.util.JiveProperties;

import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;
import com.thinkparity.codebase.log4j.Log4JHelper;

import com.thinkparity.model.artifact.ArtifactType;
import com.thinkparity.model.profile.ProfileEMail;
import com.thinkparity.model.profile.ProfileModel;
import com.thinkparity.model.xmpp.IQReader;
import com.thinkparity.model.xmpp.IQWriter;

import com.thinkparity.server.ParityServerConstants.JivePropertyNames;
import com.thinkparity.server.ParityServerConstants.Logging;
import com.thinkparity.server.model.artifact.ArtifactModel;
import com.thinkparity.server.model.contact.ContactModel;
import com.thinkparity.server.model.container.ContainerModel;
import com.thinkparity.server.model.session.Session;

/**
 * <b>Title:</b>thinkParity Model Controller <br>
 * <b>Description:</b>An abstraction of an xmpp controller.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public abstract class AbstractController extends
        com.thinkparity.codebase.controller.AbstractController {

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

    /** A thinkParity profile interface. */
    private ProfileModel profileModel;

    /** Create AbstractController. */
    public AbstractController(final String action) { super(action); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#createReader(org.xmpp.packet.IQ)
     */
    public IQReader createReader(final IQ iq) {
        iqReader = new IQReader(iq);
        return iqReader;
    }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#createWriter(org.xmpp.packet.IQ)
     */
    public IQWriter createWriter(final IQ iq) {
        iqWriter = new IQWriter(iq);
        return iqWriter;
    }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#handleIQ(org.xmpp.packet.IQ)
     */
    public IQ handleIQ(final IQ iq) throws UnauthorizedException {
        final Session session = new Session() {
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
        return super.handleIQ(iq);
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
     * Obtain the thinkParity profile interface.
     * 
     * @return A thinkParity profile interface.
     */
    protected ProfileModel getProfileModel() { return profileModel; }

    /** Log an api id. */
    protected final void logApiId() {
        if(logger.isInfoEnabled()) {
            logger.info(MessageFormat.format("[{0}] [{1}] [{2}]",
                    Logging.CONTROLLER_LOG_ID,
                    StackUtil.getCallerClassName().toUpperCase(),
                    StackUtil.getCallerMethodName().toUpperCase()));
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
            logger.debug(MessageFormat.format("{0}:{1}",
                    name,
                    Log4JHelper.render(logger, value)));
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
