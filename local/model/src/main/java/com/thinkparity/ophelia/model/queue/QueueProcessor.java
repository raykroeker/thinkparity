/*
 * Created On:  14-Jul-07 1:10:36 PM
 */
package com.thinkparity.ophelia.model.queue;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.thinkparity.codebase.crypto.DecryptFile;
import com.thinkparity.codebase.delegate.CancelException;
import com.thinkparity.codebase.delegate.Cancelable;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.crypto.Secret;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.stream.StreamException;
import com.thinkparity.codebase.model.stream.download.DownloadFile;
import com.thinkparity.codebase.model.util.xmpp.event.*;
import com.thinkparity.codebase.model.util.xmpp.event.container.PublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.container.PublishedNotificationEvent;
import com.thinkparity.codebase.model.util.xmpp.event.container.VersionPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.container.VersionPublishedNotificationEvent;
import com.thinkparity.codebase.model.util.xmpp.event.profile.ActiveEvent;
import com.thinkparity.codebase.model.util.xmpp.event.profile.payment.PaymentEvent;
import com.thinkparity.codebase.model.util.xmpp.event.profile.payment.PaymentPlanArrearsEvent;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.LocalContentEvent;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.contact.InternalContactModel;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.container.event.LocalPublishedEvent;
import com.thinkparity.ophelia.model.container.event.LocalVersionPublishedEvent;
import com.thinkparity.ophelia.model.crypto.InternalCryptoModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.migrator.InternalMigratorModel;
import com.thinkparity.ophelia.model.profile.InternalProfileModel;
import com.thinkparity.ophelia.model.session.OfflineException;
import com.thinkparity.ophelia.model.workspace.Workspace;

import com.thinkparity.network.NetworkException;

/**
 * <b>Title:</b>thinkParity Ophelia Model Queue Processor<br>
 * <b>Description:</b>An instance of a queue processor is a simple delegate
 * that knows how to process the remote event queue. Given that this class is
 * the only accessor of the event queue; it is always processed asynchronously.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class QueueProcessor implements Cancelable, Runnable {

    /** A lock to prevent multiple threads processing the same queue. */
    private static final Object QUEUE_LOCK;

    static {
        QUEUE_LOCK = new Object();
    }

    /** A cancel indicator. */
    private boolean cancel;

    /** A delegate used to decrypt files. */
    private DecryptFile decrypter;

    /** A delegate used to download files. */
    private DownloadFile downloader;

    /** A localized event. */
    private LocalContentEvent<?,?> localEvent;

    /** A log4j wrapper. */
    private final Log4JWrapper logger;

    /** An internal model factory. */
    private InternalModelFactory modelFactory;

    /** A run indicator. */
    private boolean running;

    /** A workspace. */
    private Workspace workspace;

    /**
     * Create QueueProcessor.
     *
     */
    public QueueProcessor() {
        super();
        this.cancel = false;
        this.logger = new Log4JWrapper(getClass());
        this.running = false;
    }

    /**
     * Cancel an in-progress download/decryption/inflation.
     * 
     * 
     * @see com.thinkparity.codebase.delegate.Cancelable#cancel()
     */
    public void cancel() throws CancelException {
        logger.logTrace("Begin cancel.");
        this.cancel = true;
        if (null != downloader) {
            downloader.cancel();
        }
        if (null != decrypter) {
            decrypter.cancel();
        }
        if (running) {
            synchronized (this) {
                try {
                    logger.logTrace("Begin wait for cancel.");
                    wait();
                    logger.logTrace("End wait for cancel.");
                } catch (final InterruptedException ix) {
                    logger.logTrace("End cancel.");
                    throw new CancelException(ix);
                }
            }
        }
        logger.logTrace("End cancel.");
    }

    /**
     * @see java.lang.Runnable#run()
     *
     */
    public void run() {
        logger.logTrace("Begin run.");
        running = true;
        try {
            synchronized (QUEUE_LOCK) {
                processEvents(readEvents());
            }
        } finally {
            running = false;
            synchronized (this) {
                notifyAll();
            }
            logger.logTrace("End run.");
        }
    }

    /**
     * Set the model factory.
     *
     * @param modelFactory
     *		An instance of <code>InternalModelFactory</code>.
     */
    public void setModelFactory(InternalModelFactory modelFactory) {
        this.modelFactory = modelFactory;
    }

    /**
     * Set the workspace.
     * 
     * @param workspace
     *            A <code>Workspace</code>.
     */
    public void setWorkspace(final Workspace workspace) {
        this.workspace = workspace;
    }

    /**
     * Clean up the content of the local event. The files are all temporary; so
     * a best-effort is made to delete them.
     * 
     * @param localEvent
     *            A <code>LocalContentEvent<?, File></code>.
     */
    private void cleanUpContent(
            final LocalContentEvent<?, File> localEvent) {
        final List<File> localContent = localEvent.getAllLocalContent();
        for (final File versionFile : localContent) {
            // TEMPFILE - QueueProcessor#cleanUpContent(LocalContentEvent)
            versionFile.delete();
        }
    }

    /**
     * Create a temporary file within the workspace. The workspace will attempt
     * to create a temporary file represents the artifact.
     * 
     * @param suffix
     *            The suffix string to be used in generating the file's name.
     * @return A <code>File</code>.
     * @throws IOException
     */
    private File createTempFile(final String suffix) throws IOException {
        return workspace.createTempFile(suffix);
    }

    /**
     * Decrypt a file.
     * 
     * @param secret
     *            A <code>Secret</code>.
     * @param source
     *            A <code>File</code>.
     * @param target
     *            A <code>File</code>.
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IOException
     */
    private void decrypt(final Secret secret, final File source,
            final File target) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, IOException {
        final Key key = new SecretKeySpec(secret.getKey(), secret.getAlgorithm());
        decrypter = new DecryptFile(secret.getAlgorithm());
        synchronized (workspace.getBufferLock()) {
            try {
                decrypter.decrypt(key, source, target, workspace.getBufferArray());
            } finally {
                decrypter = null;
            }
        }
    }

    /**
     * Delete a queue event.
     * 
     * @param event
     *            An <code>XMPPEvent</code>.
     */
    private void deleteEvent(final XMPPEvent event) {
        getQueueModel().deleteEvent(event);
    }

    /**
     * Download the content to a temporary file for a document version.
     * 
     * @param documentVersion
     *            A <code>DocumentVersion</code>.
     * @return A <code>File</code>.
     */
    private File download(final DocumentVersion version) {
        try {
            final String suffix = MessageFormat.format("-{0}", version.getArtifactName());
            final File downloadFile = createTempFile(suffix);
            try {
                download(version, downloadFile);
                if (cancel) {
                    logger.logInfo("Cancelling queue processor.");
                    return null;
                } else {
                    final File decryptFile = createTempFile(suffix);
                    decrypt(readSecret(version), downloadFile, decryptFile);
                    return decryptFile;
                }
            } finally {
                // TEMPFILE - QueueProcessor#download(DocumentVersion)
                downloadFile.delete();
            }
        } catch (final GeneralSecurityException gsx) {
            logger.logError(gsx, "Could not decrypt file for {0}.", version);
            return null;
        } catch (final NetworkException nx) {
            logger.logError(nx, "Could not download file for {0}.", version);
            return null;
        } catch (final IOException iox) {
            logger.logError(iox, "Could not download file for {0}.", version);
            return null;
        } catch (final StreamException sx) {
            logger.logError(sx, "Could not download file for {0}.", version);
            return null;
        }
    }

    /**
     * Download the document version to the target.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @param target
     *            A target <code>File</code>.
     * @throws NetworkException
     *             if an unrecoverable network read error occurs
     * @throws IOException
     *             if a target io error occurs
     * @throws StreamException
     *             if an unrecoverable stream protocol error occurs
     */
    private void download(final DocumentVersion version, final File target)
            throws NetworkException, IOException, StreamException {
        downloader = getDocumentModel().newDownloadFile(version);
        try {
            downloader.download(target);
        } finally {
            downloader = null;
        }
    }

    /**
     * Obtain an internal artifact model.
     * 
     * @return An instance of <code>InternalArtifactModel</code>.
     */
    private InternalArtifactModel getArtifactModel() {
        return getModelFactory().getArtifactModel();
    }

    /**
     * Obtain an internal contact model.
     * 
     * @return An instance of <code>InternalContactModel</code>.
     */
    private InternalContactModel getContactModel() {
        return getModelFactory().getContactModel();
    }

    /**
     * Obtain an internal container model.
     * 
     * @return An instance of <code>InternalContainerModel</code>.
     */
    private InternalContainerModel getContainerModel() {
        return getModelFactory().getContainerModel();
    }

    /**
     * Obtain an internal crypto model.
     * 
     * @return An instance of <code>InternalCryptoModel</code>.
     */
    private InternalCryptoModel getCryptoModel() {
        return getModelFactory().getCryptoModel();
    }

    /**
     * Obtain an internal document model.
     * 
     * @return An instance of <code>InternalDocumentModel</code>.
     */
    private InternalDocumentModel getDocumentModel() {
        return getModelFactory().getDocumentModel();
    }

    /**
     * Obtain an internal migrator model.
     * 
     * @return An instance of <code>InternalMigratorModel</code>.
     */
    private InternalMigratorModel getMigratorModel() {
        return getModelFactory().getMigratorModel();
    }

    /**
     * Obtain an internal model factory.
     * 
     * @return An instance of <code>InternalModelFactory</code>.
     */
    private InternalModelFactory getModelFactory() {
        return modelFactory;
    }

    /**
     * Obtain an internal profile model.
     * 
     * @return An instance of <code>InternalProfileModel</code>.
     */
    private InternalProfileModel getProfileModel() {
        return getModelFactory().getProfileModel();
    }

    /**
     * Obtain queueModel.
     *
     * @return A InternalQueueModel.
     */
    private InternalQueueModel getQueueModel() {
        return getModelFactory().getQueueModel();
    }

    /**
     * Localize the content for a local content event. For all versions that do
     * not exist locally; the document content is downloaded to a temporary file
     * location which is set within the event.
     * 
     * @param localEvent
     *            A <code>LocalContentEvent<DocumentVersion, File></code>.
     */
    private void localizeContent(
            final LocalContentEvent<DocumentVersion, File> localEvent) {
        final InternalArtifactModel artifactModel = getArtifactModel();
        for (final DocumentVersion version : localEvent.getVersions()) {
            if (!artifactModel.doesVersionExist(version.getArtifactUniqueId(),
                    version.getVersionId())) {
                final File contentFile = download(version);
                if (cancel) {
                    logger.logInfo("Cancelling queue processor.");
                } else {
                    localEvent.setLocalContent(version, contentFile);
                }
            }
        }
    }

    /**
     * Localize the published event. Download the file(s); decrypt them and
     * inflate them.
     * 
     * @param event
     *            A <code>PublishedEvent</code>.
     * @return A <code>LocalContentEvent<?,?></code>.
     */
    private LocalContentEvent<?,?> localizeEvent(final PublishedEvent event) {
        final LocalPublishedEvent localEvent = new LocalPublishedEvent();
        localEvent.setEvent(event);
        localizeContent(localEvent);
        return localEvent;
    }

    /**
     * Localize the version published event. Download the file(s); decrypt them;
     * and inflate them.
     * 
     * @param event
     *            A <code>VersionPublishedEvent</code>.
     * @return A <code>LocalContentEvent<?,?></code>.
     */
    private LocalContentEvent<?,?> localizeEvent(final VersionPublishedEvent event) {
        final LocalVersionPublishedEvent localEvent = new LocalVersionPublishedEvent();
        localEvent.setEvent(event);
        localizeContent(localEvent);
        return localEvent;
    }

    /**
     * Localize the xmpp event. Take key remote events and localize the content
     * by downloading/decrypting and inflating the content.
     * 
     * @param event
     *            An <code>XMPPEvent</code>.
     */
    private void localizeEvent(final XMPPEvent event) {
        if (event.getClass() == PublishedEvent.class) {
            localEvent = localizeEvent((PublishedEvent) event);
        } else if (event.getClass() == VersionPublishedEvent.class) {
            localEvent = localizeEvent((VersionPublishedEvent) event);
        } else {
            localEvent = null;
        }
    }

    /**
     * Process the container published event.
     * 
     * @param localEvent
     *            A <code>LocalPublishedEvent</code>.
     */
    private void processEvent(final LocalPublishedEvent localEvent) {
        try {
            getContainerModel().handleEvent(localEvent);
        } finally {
            cleanUpContent(localEvent);
        }
    }

    /**
     * Process the container version published event.
     * 
     * @param localEvent
     *            A <code>LocalVersionPublishedEvent</code>.
     */
    private void processEvent(final LocalVersionPublishedEvent localEvent) {
        try {
            getContainerModel().handleEvent(localEvent);
        } finally {
            cleanUpContent(localEvent);
        }
    }

    /**
     * Process a queue event.
     * 
     * @param event
     *            A <code>XMPPEvent</code>.
     */
    private void processEvent(final XMPPEvent event) {
        if (event.getClass() == ArtifactDraftCreatedEvent.class) {
            logger.logInfo("Handling artifact draft created.");
            getArtifactModel().handleDraftCreated((ArtifactDraftCreatedEvent) event);
        } else if (event.getClass() == ArtifactDraftDeletedEvent.class) {
            logger.logInfo("Handling artifact draft deleted.");
            getArtifactModel().handleDraftDeleted((ArtifactDraftDeletedEvent) event);
        } else if (event.getClass() == ArtifactReceivedEvent.class) {
            logger.logInfo("Handling artifact draft received.");
            getArtifactModel().handleReceived((ArtifactReceivedEvent) event);
        } else if (event.getClass() == ArtifactTeamMemberAddedEvent.class) {
            logger.logInfo("Handling artifact team member added.");
            getArtifactModel().handleTeamMemberAdded((ArtifactTeamMemberAddedEvent) event);
        } else if (event.getClass() == ArtifactTeamMemberRemovedEvent.class) {
            logger.logInfo("Handling artifact team member removed.");
            getArtifactModel().handleTeamMemberRemoved((ArtifactTeamMemberRemovedEvent) event);
        } else if (event.getClass() == ContactDeletedEvent.class) {
            logger.logInfo("Handling contact deleted.");
            getContactModel().handleContactDeleted((ContactDeletedEvent) event);
        } else if (event.getClass() == ContactEMailInvitationDeclinedEvent.class) {
            logger.logInfo("Handling contact e-mail invitation declined.");
            getContactModel().handleEMailInvitationDeclined((ContactEMailInvitationDeclinedEvent) event);
        } else if (event.getClass() == ContactEMailInvitationDeletedEvent.class) {
            logger.logInfo("Handling contact e-mail invitation deleted.");
            getContactModel().handleEMailInvitationDeleted((ContactEMailInvitationDeletedEvent) event);
        } else if (event.getClass() == ContactEMailInvitationExtendedEvent.class) {
            logger.logInfo("Handling contact e-mail invitation extended.");
            getContactModel().handleEMailInvitationExtended((ContactEMailInvitationExtendedEvent) event);
        } else if (event.getClass() == ContactInvitationAcceptedEvent.class) {
            logger.logInfo("Handling contact invitation accepted.");
            getContactModel().handleInvitationAccepted((ContactInvitationAcceptedEvent) event);
        } else if (event.getClass() == ContactUpdatedEvent.class) {
            logger.logInfo("Handling contact updated.");
            getContactModel().handleContactUpdated((ContactUpdatedEvent) event);
        } else if (event.getClass() == ContactUserInvitationDeclinedEvent.class) {
            logger.logInfo("Handling contact user invitation declined.");
            getContactModel().handleUserInvitationDeclined((ContactUserInvitationDeclinedEvent) event);
        } else if (event.getClass() == ContactUserInvitationDeletedEvent.class) {
            logger.logInfo("Handling contact user invitation deleted.");
            getContactModel().handleUserInvitationDeleted((ContactUserInvitationDeletedEvent) event);
        } else if (event.getClass() == ContactUserInvitationExtendedEvent.class) {
            logger.logInfo("Handling contact user invitation extended.");
            getContactModel().handleUserInvitationExtended((ContactUserInvitationExtendedEvent) event);
        } else if (event.getClass() == PublishedEvent.class) {
            logger.logInfo("Handling container published.");
            processEvent((LocalPublishedEvent) localEvent);
        } else if (event.getClass() == PublishedNotificationEvent.class) {
            logger.logInfo("Handling container published notification.");
            getContainerModel().handleEvent((PublishedNotificationEvent) event);
        } else if (event.getClass() == VersionPublishedEvent.class) {
            logger.logInfo("Handling container version published.");
            processEvent((LocalVersionPublishedEvent) localEvent);
        } else if (event.getClass() == VersionPublishedNotificationEvent.class) {
            logger.logInfo("Handling container version published notification.");
            getContainerModel().handleEvent((VersionPublishedNotificationEvent) event);
        } else if (event.getClass() == PaymentEvent.class) {
            logger.logInfo("Handling profile payment event.");
            getProfileModel().handleEvent((PaymentEvent) event);
        } else if (event.getClass() == PaymentPlanArrearsEvent.class) {
            logger.logInfo("Handling profile payment plan arrears event.");
            getProfileModel().handleEvent((PaymentPlanArrearsEvent) event);
        } else if (event.getClass() == ActiveEvent.class) {
            logger.logInfo("Handling profile disabled.");
            getProfileModel().handleEvent((ActiveEvent) event);
        } else if (event.getClass() == ProductReleaseDeployedEvent.class) {
            logger.logInfo("Handling migrator product release deployed.");
            getMigratorModel().handleProductReleaseDeployed((ProductReleaseDeployedEvent) event);
        } else {
            final String message = MessageFormat.format("No handler for event {0}.", event);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Process a list of queue events.
     * 
     * @param events
     *            A <code>List<XMPPEvent></code>.
     */
    private void processEvents(final List<XMPPEvent> events) {
        boolean isOnline;
        for (final XMPPEvent event : events) {
            if (cancel) {
                logger.logInfo("Cancelling queue processor.");
            } else {
                localizeEvent(event);
                if (cancel) {
                    logger.logInfo("Cancelling queue processor.");
                } else {
                    isOnline = true;
                    try {
                        processEvent(event);
                    } catch (final OfflineException ox) {
                        logger.logWarning(ox,
                                "User session went offline while processing event {0}.",
                                event);
                        isOnline = false;
                        break;
                    } finally {
                        if (isOnline) {
                            /* only delete the event if the processing was
                             * successful or if the error was not network
                             * releated */
                            deleteEvent(event);
                        }
                    }
                }
            }
        }
    }

    /**
     * Read a list of queue events.
     * 
     * @return A <code>List<XMPPEvent></code>.
     */
    private List<XMPPEvent> readEvents() {
        if (cancel) {
            return Collections.emptyList();
        } else {
            return getQueueModel().readEvents();
        }
    }

    /**
     * Read the decryption secret for a document version.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return A <code>Secret</code>.
     */
    private Secret readSecret(final DocumentVersion version) {
        return getCryptoModel().readSecret(version);
    }
}
