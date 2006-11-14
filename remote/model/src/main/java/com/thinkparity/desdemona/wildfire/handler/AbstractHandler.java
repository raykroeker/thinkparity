/*
 * Created On: Jun 22, 2006 2:51:33 PM
 */
package com.thinkparity.desdemona.wildfire.handler;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.desdemona.model.archive.ArchiveModel;
import com.thinkparity.desdemona.model.artifact.ArtifactModel;
import com.thinkparity.desdemona.model.backup.BackupModel;
import com.thinkparity.desdemona.model.contact.ContactModel;
import com.thinkparity.desdemona.model.container.ContainerModel;
import com.thinkparity.desdemona.model.profile.ProfileModel;
import com.thinkparity.desdemona.model.queue.QueueModel;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.model.stream.StreamModel;
import com.thinkparity.desdemona.model.user.UserModel;
import com.thinkparity.desdemona.util.xmpp.IQReader;
import com.thinkparity.desdemona.util.xmpp.IQWriter;
import com.thinkparity.desdemona.wildfire.util.SessionUtil;

import org.jivesoftware.wildfire.auth.UnauthorizedException;
import org.xmpp.packet.IQ;


/**
 * <b>Title:</b>thinkParity Model Controller <br>
 * <b>Description:</b>An abstraction of an xmpp controller.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.9
 */
public abstract class AbstractHandler extends
        com.thinkparity.codebase.wildfire.handler.AbstractHandler {

    /** An apache iq logger. */
    static final Log4JWrapper IQ_LOGGER;

    /**
     * A synchronization lock used to serialize all incoming iq handler
     * requests.
     */
    static final Object SERIALIZER;

    static {
        IQ_LOGGER = new Log4JWrapper("XMPP_IQ");
        SERIALIZER = new Object();
    }

    /** A thinkParity archive interface. */
    private ArchiveModel archiveModel;

    /** A thinkParity stream interface. */
    private StreamModel streamModel;

    /** A thinkParity artifact interface. */
    private ArtifactModel artifactModel;

    /** A thinkParity backup interface. */
    private BackupModel backupModel;

    /** A thinkParity contact interface. */
    private ContactModel contactModel;

    /** A thinkParity container interface. */
    private ContainerModel containerModel;

    /** A custom iq reader. */
    private IQReader iqReader;

    /** A custom iq writer. */
    private IQWriter iqWriter;

    /** An apache logger. */
    protected final Log4JWrapper logger;

    /** A thinkParity profile interface. */
    private ProfileModel profileModel;

    /** A thinkParity queue interface. */
    private QueueModel queueModel;

    /** A thinkParity user session. */
    private Session session;

    /** A thinkParity user interface. */
    private UserModel userModel;

    /** Create AbstractHandler. */
    public AbstractHandler(final String action) {
        super(action);
        this.logger = new Log4JWrapper(getClass());
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
        iqWriter = new IQWriter(iq, logger);
        return iqWriter;
    }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#handleIQ(org.xmpp.packet.IQ)
     */
    public IQ handleIQ(final IQ iq) throws UnauthorizedException {
        synchronized (SERIALIZER) {
            this.session = SessionUtil.getInstance().createSession(iq);
            this.archiveModel = ArchiveModel.getModel(session);
            this.artifactModel = ArtifactModel.getModel(session);
            this.backupModel = BackupModel.getModel(session);
            this.contactModel = ContactModel.getModel(session);
            this.containerModel = ContainerModel.getModel(session);
            this.profileModel = ProfileModel.getModel(session);
            this.queueModel = QueueModel.getModel(session);
            this.streamModel = StreamModel.getModel(session);
            this.userModel = UserModel.getModel(session);
            final IQ response = super.handleIQ(iq);
            IQ_LOGGER.logVariable("iq", iq);
            IQ_LOGGER.logVariable("iq response", response);
            return response;
        }
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
     * Obtain a thinkParity stream interface.
     * 
     * @return A thinkParity stream interface.
     */
    protected StreamModel getStreamModel() {
        return streamModel;
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
    protected BackupModel getBackupModel() {
        return backupModel;
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
     * Obtain a thinkParity queue interface.
     * 
     * @return A thinkParity queue interface.
     */
    protected QueueModel getQueueModel() {
        return queueModel;
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
