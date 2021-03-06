/*
 * Generated On: Oct 04 06 09:40:46 AM
 */
package com.thinkparity.ophelia.model.backup;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.artifact.PublishedToEMail;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b>thinkParity Internal Backup Model<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.6
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalBackupModel extends BackupModel {

    /**
     * Read a container.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>Container</code>.
     */
    public Container readContainer(final UUID uniqueId);

    /**
     * Read the containers from the backup.
     * 
     * @return A <code>List</code> of <code>Container</code>s.
     */
    public List<Container> readContainers();

    /**
     * Read the containers from the backup.
     * 
     * @param comparator
     *            A <code>Comparator&lt;Artifact&gt;</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    public List<Container> readContainers(final Comparator<Artifact> comparator);

    /**
     * Read the containers from the backup.
     * 
     * @param comparator
     *            A <code>Comparator&lt;Artifact&gt;</code>.
     * @param filter
     *            A <code>Filter&lt;? super Artifact&gt;</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    public List<Container> readContainers(
            final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter);

    public List<ContainerVersion> readContainerVersions(final UUID uniqueId);

    public List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Comparator<ArtifactVersion> comparator);

    public List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter);

    public List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Filter<? super ArtifactVersion> filter);

    public List<Document> readDocuments(final UUID uniqueId,
            final Long versionId);

    public List<Document> readDocuments(final UUID uniqueId,
            final Long versionId, final Comparator<Artifact> comparator);

    public List<Document> readDocuments(final UUID uniqueId,
            final Long versionId, final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter);

    public List<Document> readDocuments(final UUID uniqueId,
            final Long versionId, final UUID documentUniqueId,
            final Filter<? super Artifact> filter);

    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId);

    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final Comparator<ArtifactVersion> comparator);

    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter);

    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final Filter<? super ArtifactVersion> filter);

    /**
     * Read a list of team members the container version was published to.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    public List<ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId);

    /**
     * Read a list of team members the container version was published to.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param comparator
     *            A <code>Comparator&lt;User&gt;</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    public List<ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId, final Comparator<ArtifactReceipt> comparator);

    /**
     * Read a list of team members the container version was published to.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param comparator
     *            A <code>Comparator&lt;User&gt;</code>.
     * @param filter
     *            A <code>Filter&lt;? super User&gt;</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    public List<ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId, final Comparator<ArtifactReceipt> comparator,
            final Filter<? super ArtifactReceipt> filter);

    /**
     * Read a list of team members the container version was published to.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param filter
     *            A <code>Filter&lt;? super User&gt;</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    public List<ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId, final Filter<? super ArtifactReceipt> filter);

    /**
     * Read a list of published to e-mail addresses.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @param versionId
     *            An artifact version id <code>Long</code>.
     * @return A <code>List<PublishedToEMail></code>.
     */
    public List<PublishedToEMail> readPublishedToEMails(final UUID uniqueId,
            final Long versionId);

    /**
     * @see com.thinkparity.ophelia.model.archive.ArchiveReader#readTeamIds(java.util.UUID)
     */
    public List<JabberId> readTeamIds(final UUID uniqueId);
}
