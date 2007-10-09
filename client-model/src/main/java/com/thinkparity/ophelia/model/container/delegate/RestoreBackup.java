/*
 * Created On:  28-Apr-07 11:41:25 AM
 */
package com.thinkparity.ophelia.model.container.delegate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.thinkparity.codebase.Pair;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.crypto.CryptoMonitor;
import com.thinkparity.codebase.crypto.DecryptFile;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.PublishedToEMail;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.crypto.Secret;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.stream.StreamException;
import com.thinkparity.codebase.model.stream.StreamMonitor;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.backup.InternalBackupModel;
import com.thinkparity.ophelia.model.container.ContainerDelegate;
import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.monitor.RestoreBackupData;
import com.thinkparity.ophelia.model.container.monitor.RestoreBackupStep;
import com.thinkparity.ophelia.model.document.CannotLockException;
import com.thinkparity.ophelia.model.document.DocumentFileLock;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.sort.ComparatorBuilder;
import com.thinkparity.ophelia.model.util.sort.ModelSorter;

import com.thinkparity.network.NetworkException;

/**
 * <b>Title:</b>thinkParity OpheliaModel Container Restore Backup Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class RestoreBackup extends ContainerDelegate {

    /** A <code>ProcessMonitor</code>. */
    private ProcessMonitor monitor;

    /** The monitor data. */
    private final RestoreBackupData monitorData;

    /** The date/time of restoration. */
    private Calendar restoredOn;

    /**
     * Create RestoreBackup.
     *
     */
    public RestoreBackup() {
        super();
        this.monitorData = new RestoreBackupData();
    }

    /**
     * Restore from a backup.
     * 
     * @throws CannotLockException
     *             if a local document cannot be locked
     * @throws NetworkException
     *             if an unrecoverable network read error occurs
     * @throws IOException
     *             if an io error occurs
     * @throws StreamException
     *             if an unrecoverable stream protocol error occurs
     * @throws NoSuchPaddingException
     *             if a decryption error occurs
     * @throws NoSuchAlgorithmException
     *             if a decryption error occurs
     * @throws InvalidKeyException
     *             if a decryption error occurs
     */
    public void restoreBackup() throws CannotLockException, NetworkException,
            IOException, StreamException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException {
        restoredOn = getSessionModel().readDateTime();
        final List<Container> containers = read();
        monitorData.setDeleteContainers(containers);
        notifyStepBegin(monitor, RestoreBackupStep.DELETE_CONTAINERS, monitorData);
        final Map<Container, List<Document>> allDocuments = readAllDocuments();
        final Map<Document, DocumentFileLock> allDocumentsLocks = new HashMap<Document, DocumentFileLock>();
        final Map<DocumentVersion, DocumentFileLock> allDocumentsVersionsLocks = new HashMap<DocumentVersion, DocumentFileLock>();
        try {
            for (final List<Document> documents : allDocuments.values()) {
                for (final Document document : documents) {
                    allDocumentsLocks.put(document, lockDocument(document));
                    allDocumentsVersionsLocks.putAll(lockDocumentVersions(document));
                }
            }
            for (final Container container : containers) {
                monitorData.setDeleteContainer(container);
                notifyStepBegin(monitor, RestoreBackupStep.DELETE_CONTAINER, monitorData);
                deleteLocal(container.getId(), allDocuments.get(container),
                        allDocumentsLocks, allDocumentsVersionsLocks);
                notifyStepEnd(monitor, RestoreBackupStep.DELETE_CONTAINER);
            }
        } finally {
            try {
                releaseLocks(allDocumentsLocks.values());
            } finally {
                releaseLocks(allDocumentsVersionsLocks.values());
            }
        }

        // if backup is not enabled for the profile, do nothing
        if (!getProfileModel().isBackupEnabled().booleanValue())
            return;

        final InternalBackupModel backupModel = getBackupModel();
        final List<Container> backupContainers = backupModel.readContainers();
        monitorData.setRestoreContainers(backupContainers);
        notifyStepBegin(monitor, RestoreBackupStep.RESTORE_CONTAINERS, monitorData);
        for (final Container backupContainer : backupContainers) {
            monitorData.setRestoreContainer(backupContainer);
            notifyStepBegin(monitor, RestoreBackupStep.RESTORE_CONTAINER, monitorData);
            restore(backupContainer);
            // NOTE the model needs to apply the flag seen in this single case
            getArtifactModel().applyFlagSeen(backupContainer.getId());
            notifyStepEnd(monitor, RestoreBackupStep.RESTORE_CONTAINER);
        }
        notifyStepEnd(monitor, RestoreBackupStep.RESTORE_CONTAINERS);
        /* restore */
        notifyStepBegin(monitor, RestoreBackupStep.FINALIZE_RESTORE, monitorData);
        containerService.confirmRestoreBackup(getAuthToken(), restoredOn);
        notifyStepEnd(monitor, RestoreBackupStep.FINALIZE_RESTORE);
    }

    /**
     * Set the process monitor.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     */
    public void setMonitor(final ProcessMonitor monitor) {
        this.monitor = monitor;
    }

    /**
     * Download a document version to a local file.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @param file
     *            A <code>File</code>.
     * @return A version <code>File</code>.
     * @throws NetworkException
     *             if an unrecoverable network read error has occured
     * @throws IOException
     *             if a file write error has occured
     * @throws StreamException
     *             if an unercoverable stream protocol error has occured
     * @throws NoSuchPaddingException
     *             if a decryption error occurs
     * @throws NoSuchAlgorithmException
     *             if a decryption error occurs
     * @throws InvalidKeyException
     *             if a decryption error occurs
     */
    private void download(final DocumentVersion version, final File file)
            throws NetworkException, IOException, StreamException,
            NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException {
        final String suffix = MessageFormat.format("-{0}",  version.getArtifactName());
        final File downloadFile = createTempFile(suffix);
        try {
            getDocumentModel().newDownloadFile(newStreamMonitor(version),
                    version).download(downloadFile);
            final Secret secret = getCryptoModel().readSecret(version);
            final Key key = new SecretKeySpec(secret.getKey(), secret.getAlgorithm());
            synchronized (getBufferLock()) {
                new DecryptFile(newCryptoMonitor(), secret.getAlgorithm()).decrypt(
                        key, downloadFile, file, getBufferArray());
            }
        } finally {
            // TEMPFILE - RestoreBackup#download(DocumentVersion)
            downloadFile.delete();
        }
    }

    /**
     * Create a crypto monitor that looks for decrypted bytes.
     * 
     * @return A <code>CryptoMonitor</code>.
     */
    private CryptoMonitor newCryptoMonitor() {
        return new CryptoMonitor() {
            /**
             * @see com.thinkparity.codebase.crypto.CryptoMonitor#chunkDecrypted(int)
             *
             */
            @Override
            public void chunkDecrypted(final int chunkSize) {
                monitorData.setBytes(chunkSize);
                notifyStepBegin(monitor, RestoreBackupStep.RESTORE_DOCUMENT_VERSION_DECRYPT_BYTES, monitorData);
                notifyStepEnd(monitor, RestoreBackupStep.RESTORE_DOCUMENT_VERSION_DECRYPT_BYTES);
            }
            /**
             * @see com.thinkparity.codebase.crypto.CryptoMonitor#chunkEncrypted(int)
             *
             */
            @Override
            public void chunkEncrypted(final int chunkSize) {
                Assert.assertUnreachable("Cannot encrypt from here.");
            }
        };
    }

    /**
     * Create a stream monitor for the download.
     * 
     * @return A <code>StreamMonitor</code>.
     */
    private StreamMonitor newStreamMonitor(final DocumentVersion version) {
        return new StreamMonitor() {
            /**
             * @see com.thinkparity.codebase.model.stream.StreamMonitor#chunkReceived(int)
             *
             */
            @Override
            public void chunkReceived(final int chunkSize) {
                monitorData.setBytes(chunkSize);
                notifyStepBegin(monitor, RestoreBackupStep.RESTORE_DOCUMENT_VERSION_DOWNLOAD_BYTES, monitorData);
                notifyStepEnd(monitor, RestoreBackupStep.RESTORE_DOCUMENT_VERSION_DOWNLOAD_BYTES);
            }
            /**
             * @see com.thinkparity.codebase.model.stream.StreamMonitor#chunkSent(int)
             *
             */
            @Override
            public void chunkSent(final int chunkSize) {
                Assert.assertUnreachable("Cannot upload from here.");
            }
            /**
             * @see com.thinkparity.codebase.model.stream.StreamMonitor#getName()
             *
             */
            @Override
            public String getName() {
                return MessageFormat.format("RestoreBackup#newStreamMonitor({0})",
                        version.getArtifactName());
            }
            /**
             * @see com.thinkparity.codebase.model.stream.StreamMonitor#reset()
             *
             */
            @Override
            public void reset() {
                monitorData.setBytes(0);
                notifyStepBegin(monitor, RestoreBackupStep.RESET_RESTORE_DOCUMENT_VERSION, monitorData);
                notifyStepEnd(monitor, RestoreBackupStep.RESET_RESTORE_DOCUMENT_VERSION);
            }
        };
    }

    /**
     * Read a map of all documents for all containers. This will look in the
     * draft as well as all versions.
     * 
     * @return A <code>Map</code> of <code>Container</code>s to their
     *         <code>Document</code>s.
     */
    private Map<Container, List<Document>> readAllDocuments() {
        final List<Container> containers = read();
        final Map<Container, List<Document>> allDocuments =
            new HashMap<Container, List<Document>>(containers.size(), 1.0F);
        for (final Container container : containers) {
            allDocuments.put(container, readAllDocuments(container.getId()));
        }
        return allDocuments;
    }

    /**
     * Restore a container to the local database.
     * 
     * @param container
     *            A container.
     * @throws NetworkException
     *             if an unrecoverable network read error occurs
     * @throws IOException
     *             if an io error occurs writing local files
     * @throws StreamException
     *             if an unrecoverable stream protocol error occurs
     * @throws NoSuchPaddingException
     *             if a decryption error occurs
     * @throws NoSuchAlgorithmException
     *             if a decryption error occurs
     * @throws InvalidKeyException
     *             if a decryption error occurs
     */
    private void restore(final Container container) throws NetworkException,
            IOException, StreamException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException {
        final InternalBackupModel backupModel = getBackupModel();
        final InternalDocumentModel documentModel = getDocumentModel();
        final InternalUserModel userModel = getUserModel();
        userModel.readLazyCreate(container.getCreatedBy());
        userModel.readLazyCreate(container.getUpdatedBy());
        containerIO.create(container);
        // restore team info
        final List<JabberId> teamIds = backupModel.readTeamIds(container.getUniqueId());
        for (final JabberId teamId : teamIds) {
            artifactIO.createTeamRel(
                    container.getId(),
                    userModel.readLazyCreate(teamId).getLocalId());
        }
        // restore the draft if one existed
        final InternalSessionModel sessionModel = getSessionModel();
        final JabberId draftOwner = sessionModel.readKeyHolder(
                container.getUniqueId());
        if (draftOwner.equals(User.THINKPARITY.getId())) {
            logger.logInfo("No remote draft exists for {0}.", container.getName());
        } else {
            final List<TeamMember> team = artifactIO.readTeamRel2(container.getId());
            final ContainerDraft draft = new ContainerDraft();
            draft.setLocal(Boolean.FALSE);
            draft.setContainerId(container.getId());
            draft.setOwner(team.get(indexOf(team, draftOwner)));
            containerIO.createDraft(draft);
        }
        // restore version info
        final List<ContainerVersion> versions =
            backupModel.readContainerVersions(container.getUniqueId());
        // we want to restore from first to last chronologically
        ModelSorter.sortContainerVersions(versions, new ComparatorBuilder().createVersionById(Boolean.TRUE));
        List<Document> documents;
        List<DocumentVersion> documentVersions;
        InputStream documentVersionStream;
        ContainerVersion previous;
        List<ArtifactReceipt> publishedTo;
        List<PublishedToEMail> publishedToEMails;
        File versionFile;
        for (final ContainerVersion version : versions) {
            logger.logTrace("Restoring container \"{0}\" version \"{1}.\"",
                    version.getArtifactName(), version.getVersionId());
            userModel.readLazyCreate(version.getCreatedBy());
            userModel.readLazyCreate(version.getUpdatedBy());
            version.setArtifactId(container.getId());
            containerIO.createVersion(version);
            artifactIO.updateFlags(container.getId(), container.getFlags());
            publishedTo = backupModel.readPublishedTo(
                    version.getArtifactUniqueId(), version.getVersionId());
            for (final ArtifactReceipt receipt : publishedTo) {
                containerIO.createPublishedTo(container.getId(),
                        version.getVersionId(),
                        userModel.readLazyCreate(receipt.getUser().getId()),
                        receipt.getPublishedOn());
                if (receipt.isSetReceivedOn()) {
                    containerIO.updatePublishedTo(container.getId(),
                            version.getVersionId(),
                            receipt.getPublishedOn(),
                            receipt.getUser().getId(),
                            receipt.getReceivedOn());
                }
            }
            // published to invitations
            publishedToEMails = backupModel.readPublishedToEMails(
                    version.getArtifactUniqueId(), version.getVersionId());
            for (final PublishedToEMail publishedToEMail : publishedToEMails) {
                containerIO.createPublishedTo(container.getId(),
                        version.getVersionId(), publishedToEMail.getEMail(),
                        publishedToEMail.getPublishedOn());
            }
            // restore version links
            documents = backupModel.readDocuments(container.getUniqueId(), version.getVersionId());
            monitorData.setRestoreDocuments(documents);
            notifyStepBegin(monitor, RestoreBackupStep.RESTORE_DOCUMENTS, monitorData);
            documentVersions = backupModel.readDocumentVersions(container.getUniqueId(), version.getVersionId());
            for (final Document document : documents) {
                logger.logTrace("Restoring container \"{0}\" version \"{1}\" document \"{2}.\"",
                        version.getArtifactName(), version.getVersionId(),
                        document.getName());
                userModel.readLazyCreate(document.getCreatedBy());
                userModel.readLazyCreate(document.getUpdatedBy());
                if (artifactIO.doesExist(document.getUniqueId())) {
                    document.setId(artifactIO.readId(document.getUniqueId()));
                } else {
                    documentIO.create(document);
                }
                monitorData.setRestoreDocumentVersions(documentVersions);
                notifyStepBegin(monitor, RestoreBackupStep.RESTORE_DOCUMENT_VERSIONS, monitorData);
                for (final DocumentVersion documentVersion : documentVersions) {
                    if (documentVersion.getArtifactUniqueId().equals(document.getUniqueId())) {
                        if (!artifactIO.doesVersionExist(document.getId(),
                                documentVersion.getVersionId())) {
                            monitorData.setRestoreDocumentVersion(documentVersion);
                            notifyStepBegin(monitor, RestoreBackupStep.RESTORE_DOCUMENT_VERSION, monitorData);
                            logger.logTrace("Restoring container \"{0}\" version \"{1}\" document \"{2}\" version \"{3}.\"",
                                    version.getArtifactName(), version.getVersionId(),
                                    documentVersion.getArtifactName(), documentVersion.getVersionId());
                            userModel.readLazyCreate(documentVersion.getCreatedBy());
                            userModel.readLazyCreate(documentVersion.getUpdatedBy());
                            documentVersion.setArtifactId(document.getId());
                            versionFile = createTempFile(MessageFormat.format(
                                    "-{0}", documentVersion.getArtifactName()));
                            try {
                                download(documentVersion, versionFile);
                                documentVersionStream = new FileInputStream(versionFile);
                                try {
                                    documentModel.createVersion(document,
                                            documentVersion,
                                            documentVersionStream);
                                } finally {
                                    documentVersionStream.close();
                                }
                            } finally {
                                // TEMPFILE - RestoreBackup#restore(Container)
                                versionFile.delete();
                            }
                            containerIO.addVersion(container.getId(),
                                    version.getVersionId(), document.getId(),
                                    documentVersion.getVersionId(),
                                    document.getType());
                            getIndexModel().indexDocument(container.getId(), document.getId());
                            logger.logTrace("Document version has been restored.");
                            notifyStepEnd(monitor, RestoreBackupStep.RESTORE_DOCUMENT_VERSION);
                            break;
                        }
                    }
                }
                notifyStepEnd(monitor, RestoreBackupStep.RESTORE_DOCUMENT_VERSIONS);

                logger.logTrace("Document has been restored.");
                previous = readPreviousVersion(version.getArtifactId(),
                        version.getVersionId());
                if (null != previous) {
                    containerIO.deleteDelta(version.getArtifactId(),
                            version.getVersionId(), previous.getVersionId());
                    containerIO.createDelta(calculateDelta(read(version.getArtifactId()),
                            version, previous));
                }
            }
            notifyStepEnd(monitor, RestoreBackupStep.RESTORE_DOCUMENTS);
            getIndexModel().indexContainerVersion(new Pair<Long, Long>(
                    version.getArtifactId(), version.getVersionId()));
            // NOTE the model needs to apply the flag seen in this single case
            getArtifactModel().applyFlagSeen(version);
            logger.logTrace("Container version has been restored.");
        }
        // index
        getIndexModel().indexContainer(container.getId());
    }
}
