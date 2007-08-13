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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.thinkparity.codebase.Pair;
import com.thinkparity.codebase.bzip2.InflateFile;
import com.thinkparity.codebase.crypto.DecryptFile;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.PublishedToEMail;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.crypto.Secret;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.backup.InternalBackupModel;
import com.thinkparity.ophelia.model.container.ContainerDelegate;
import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.monitor.RestoreBackupStep;
import com.thinkparity.ophelia.model.document.CannotLockException;
import com.thinkparity.ophelia.model.document.DocumentFileLock;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.sort.ComparatorBuilder;
import com.thinkparity.ophelia.model.util.sort.ModelSorter;

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

    /**
     * Create RestoreBackup.
     *
     */
    public RestoreBackup() {
        super();
    }

    /**
     * Restore from a backup.
     *
     */
    public void restoreBackup() throws CannotLockException, IOException,
            NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException {
        // if backup is not enabled for the profile, do nothing
        if (!getProfileModel().isBackupEnabled().booleanValue())
            return;
        final List<Container> containers = read();
        notifyDetermine(monitor, containers.size());
        if (0 < containers.size()) {
            notifyStepBegin(monitor, RestoreBackupStep.DELETE_LOCAL_CONTAINER);
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
                    deleteLocal(container.getId(), allDocuments.get(container),
                            allDocumentsLocks, allDocumentsVersionsLocks);
                }
            } finally {
                try {
                    releaseLocks(allDocumentsLocks.values());
                } finally {
                    releaseLocks(allDocumentsVersionsLocks.values());
                }
            }
            notifyStepEnd(monitor, RestoreBackupStep.DELETE_LOCAL_CONTAINER);
        }

        final InternalBackupModel backupModel = getBackupModel();
        final List<Container> backupContainers = backupModel.readContainers();
        notifyDetermine(monitor, backupContainers.size());
        for (final Container backupContainer : backupContainers) {
            notifyStepBegin(monitor, RestoreBackupStep.RESTORE_REMOTE_CONTAINER);
            restore(backupContainer);
            // NOTE the model needs to apply the flag seen in this single case
            getArtifactModel().applyFlagSeen(backupContainer.getId());
            notifyStepEnd(monitor, RestoreBackupStep.RESTORE_REMOTE_CONTAINER);
        }
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
     * Download a document version.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return A version <code>File</code>.
     */
    private void download(final DocumentVersion version, final File file)
            throws IOException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException {
        final String suffix = MessageFormat.format("-{0}",  version.getArtifactName());
        final File downloadFile = createTempFile(suffix);
        try {
            getDocumentModel().newDownloadFile(version).download(downloadFile);
            final Secret secret = getCryptoModel().readSecret(version);
            final Key key = new SecretKeySpec(secret.getKey(), secret.getAlgorithm());
            final File decryptFile = createTempFile(suffix);
            try {
                synchronized (getBufferLock()) {
                    new DecryptFile(secret.getAlgorithm()).decrypt(
                            key, downloadFile, decryptFile, getBufferArray());
                }
                synchronized (getBufferLock()) {
                    new InflateFile(getBuffer()).inflate(decryptFile, file);
                }
            } finally {
                // TEMPFILE - RestoreBackup#download(DocumentVersion)
                decryptFile.delete();
            }
        } finally {
            // TEMPFILE - RestoreBackup#download(DocumentVersion)
            downloadFile.delete();
        }
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
     * @param archiveReader
     *            An archive reader.
     * @throws IOException
     */
    private void restore(final Container container) throws IOException,
            NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException {
        final InternalBackupModel backupModel = getBackupModel();
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
                for (final DocumentVersion documentVersion : documentVersions) {
                    if (documentVersion.getArtifactUniqueId().equals(document.getUniqueId())) {
                        if (!artifactIO.doesVersionExist(document.getId(),
                                documentVersion.getVersionId())) {
                            logger.logTrace("Restoring container \"{0}\" version \"{1}\" document \"{2}\" version \"{3}.\"",
                                    version.getArtifactName(), version.getVersionId(),
                                    documentVersion.getArtifactName(), documentVersion.getVersionId());
                            userModel.readLazyCreate(documentVersion.getCreatedBy());
                            userModel.readLazyCreate(documentVersion.getUpdatedBy());
                            documentVersion.setArtifactId(document.getId());
                            versionFile = createTempFile(MessageFormat.format(
                                    "-{0}", documentVersion.getName()));
                            try {
                                download(documentVersion, versionFile);
                                documentVersionStream = new FileInputStream(versionFile);
                                try {
                                    documentIO.createVersion(documentVersion,
                                            documentVersionStream,
                                            getBufferSize());
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
                            break;
                        }
                    }
                }
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
