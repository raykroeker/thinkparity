/*
 * Created On: Jun 22, 2006 2:51:33 PM
 */
package com.thinkparity.desdemona.wildfire.handler;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

import org.jivesoftware.util.JiveProperties;
import org.jivesoftware.wildfire.auth.UnauthorizedException;

import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;
import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.desdemona.model.Constants.JivePropertyNames;
import com.thinkparity.desdemona.model.archive.ArchiveModel;
import com.thinkparity.desdemona.model.artifact.ArtifactModel;
import com.thinkparity.desdemona.model.contact.ContactModel;
import com.thinkparity.desdemona.model.container.ContainerModel;
import com.thinkparity.desdemona.model.profile.ProfileModel;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.model.user.UserModel;
import com.thinkparity.desdemona.util.xmpp.IQReader;
import com.thinkparity.desdemona.util.xmpp.IQWriter;


/**
 * <b>Title:</b>thinkParity Model Controller <br>
 * <b>Description:</b>An abstraction of an xmpp controller.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.9
 */
public abstract class AbstractHandler extends
        com.thinkparity.codebase.wildfire.handler.AbstractHandler {

    /** A thinkParity artifact interface. */
    private ArtifactModel artifactModel;

    /** A thinkParity archive interface. */
    private ArchiveModel archiveModel;

    /** A thinkParity contact interface. */
    private ContactModel contactModel;

    /** A thinkParity container interface. */
    private ContainerModel containerModel;

    /** A custom iq reader. */
    private IQReader iqReader;

    /** A custom iq writer. */
    private IQWriter iqWriter;

    /** An apache logger. */
    private final Log4JWrapper logger;

    /** A thinkParity profile interface. */
    private ProfileModel profileModel;

    /** A thinkParity user session. */
    private Session session;

    /** A thinkParity user interface. */
    private UserModel userModel;

    /** Create AbstractHandler. */
    public AbstractHandler(final String action) {
        super(action);
        this.logger = new Log4JWrapper();
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
            this.archiveModel = ArchiveModel.getModel(session);
            this.artifactModel = ArtifactModel.getModel(session);
            this.contactModel = ContactModel.getModel(session);
            this.containerModel = ContainerModel.getModel(session);
            this.profileModel = ProfileModel.getModel(session);
            this.userModel = UserModel.getModel(session);
            logVariable("iq", iq);
            final IQ response = super.handleIQ(iq);
            logVariable("response", response);
            return response;
        }
    }

    /**
     * Obtain a thinkParity artifact interface.
     * 
     * @return A thinkParity artifact interface.
     */
    protected ArtifactModel getArtifactModel() {
        return artifactModel;
    }

    /**
     * Obtain a thinkParity archive interface.
     * 
     * @return A thinkParity archive interface.
     */
    protected ArchiveModel getArchiveModel() {
        return archiveModel;
    }

    /**
     * Obtain a thinkParity contact interface.
     * 
     * @return A thinkParity contact interface.
     */
    protected ContactModel getContactModel() {
        return contactModel;
    }

    /**
     * Obtain a thinkParity container interface.
     * 
     * @return A thinkParity container interface.
     */
    protected ContainerModel getContainerModel() {
        return containerModel;
    }

    /**
     * Obtain an error id.
     * 
     * @return An error id.
     */
    protected final Object getErrorId(final Throwable t) {
        return MessageFormat.format("[{0}] [{1}] [{2}] - [{3}]",
                    session.getJabberId().getUsername(),
                    StackUtil.getFrameClassName(2),
                    StackUtil.getFrameMethodName(2),
                    t.getMessage());
    }

    /**
     * Obtain a thinkParity profile interface.
     * 
     * @return A thinkParity profile interface.
     */
    protected ProfileModel getProfileModel() {
        return profileModel;
    }

    /**
     * Obtain a thinkParity user interface.
     * 
     * @return A thinkParity user interface.
     */
    protected UserModel getUserModel() {
        return userModel;
    }

    /** Log an api id. */
    protected final void logApiId() {
        logger.logApiId();
    }

    /** Log a trace id. */
    protected final void logTraceId() {
        logger.logApiId();
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
    protected final <V> V logVariable(final String name, final V value) {
        return logger.logVariable(name, value);
    }

    /**
     * Marshal a list of containers to the xml response.
     * 
     * @param containers
     *            A list of containers.
     */
    protected final void marshalContainers(final List<Container> containers) {
        iqWriter.marshalContainers(containers);
    }

    /**
     * Marshal a list of containers to the xml response.
     * 
     * @param containers
     *            A list of containers.
     */
    protected final void marshalContainerVersions(
            final List<ContainerVersion> versions) {
        iqWriter.marshalContainerVersions(versions);
    }

    /**
     * Marshal a list of documents to the xml response.
     * 
     * @param documents
     *            A <code>List&lt;Document&gt;</code>.
     */
    protected final void marshalDocuments(final List<Document> documents) {
        iqWriter.marshalDocuments(documents);
    }

    /**
     * Marshal a list of document versions to the xml response.
     * 
     * @param versions
     *            A <code>List&lt;DocumentVersion&gt;</code>.
     */
    protected final void marshalDocumentVersions(final List<DocumentVersion> versions) {
        iqWriter.marshalDocumentVersions(versions);
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
