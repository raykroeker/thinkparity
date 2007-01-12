/*
 * Generated On: Sep 01 06 10:06:21 AM
 */
package com.thinkparity.ophelia.model.archive;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.filter.Filter;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.archive.monitor.OpenMonitor;
import com.thinkparity.ophelia.model.util.Opener;

/**
 * <b>Title:</b>thinkParity Archive Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.12
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface ArchiveModel {

    /**
     * Open a document version.
     * 
     * @param monitor
     *            An <code>OpenMonitor</code> that will receive progress
     *            updates.
     * @param uniqueId
     *            A document unique id <code>UUID</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param versionName
     *            A version name <code>String</code>.
     * @param versionSize
     *            A version size <code>Long</code>.
     * @param opener
     *            A document <code>Opener</code>.
     */
    public void openDocumentVersion(final OpenMonitor monitor,
            final UUID uniqueId, final Long versionId,
            final String versionName, final Long versionSize,
            final Opener opener);

    /**
     * Read a container.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>Container</code>.
     */
    public Container readContainer(final UUID uniqueId);

    /**
     * Read containers.
     * 
     * @return A <code>List</code> of <code>Container</code>s.
     */
	public List<Container> readContainers();

    /**
     * Read containers.
     * 
     * @param comparator
     *            An <code>Artifact</code> <code>Comparator</code> used to
     *            achieve ordering.
     * @return A <code>List</code> of <code>Container</code>s.
     */
	public List<Container> readContainers(final Comparator<Artifact> comparator);

    /**
     * Read containers.
     * 
     * @param comparator
     *            An <code>Artifact</code> <code>Comparator</code> used to
     *            achieve ordering.
     * @param filter An <code>Artifact</code> <code>Filter</code> used to limit
     *         scope.
     * @return A <code>List</code> of <code>Container</code>s.
     */
    public List<Container> readContainers(final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter);

    /**
     * Read containers.
     * 
     * @param filter An <code>Artifact</code> <code>Filter</code> used to limit
     *         scope.
     * @return A <code>List</code> of <code>Container</code>s.
     */
    public List<Container> readContainers(final Filter<? super Artifact> filter);

    /**
     * Read a list of versions for a container.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>List&lt;ContainerVersion&gt;</code>.
     */
    public List<ContainerVersion> readContainerVersions(final UUID uniqueId);

    /**
     * Read a list of versions for a container.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param comaprator
     *            An <code>ArtifactVersion</code> <code>Comparator</code>
     *            used to order results.
     * @return A <code>List&lt;ContainerVersion&gt;</code>.
     */
    public List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Comparator<ArtifactVersion> comparator);

    /**
     * Read a list of versions for a container.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param comaprator
     *            An <code>ArtifactVersion</code> <code>Comparator</code>
     *            used to order results.
     * @param filter
     *            An <code>ArtifactVersion</code> <code>Filter</code> used
     *            to scope results.
     * @return A <code>List&lt;ContainerVersion&gt;</code>.
     */
    public List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter);

    /**
     * Read a list of versions for a container.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param filter
     *            An <code>ArtifactVersion</code> <code>Filter</code> used
     *            to scope results.
     * @return A <code>List&lt;ContainerVersion&gt;</code>.
     */
    public List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Filter<? super ArtifactVersion> filter);

    /**
     * Read a list of documents for a container version.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List&lt;ContainerVersion&gt;</code>.
     */
    public List<Document> readDocuments(final UUID uniqueId,
            final Long versionId);

    /**
     * Read a list of documents for a container version.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param comaprator
     *            An <code>Artifact</code> <code>Comparator</code>
     *            used to order results.
     * @return A <code>List&lt;ContainerVersion&gt;</code>.
     */
    public List<Document> readDocuments(final UUID uniqueId,
            final Long versionId, final Comparator<Artifact> comparator);

    /**
     * Read a list of documents for a container version.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param comaprator
     *            An <code>Artifact</code> <code>Comparator</code>
     *            used to order results.
     * @param filter
     *            An <code>Artifact</code> <code>Filter</code> used
     *            to scope results.
     * @return A <code>List&lt;ContainerVersion&gt;</code>.
     */
    public List<Document> readDocuments(final UUID uniqueId,
            final Long versionId, final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter);

    /**
     * Read a document version for a container.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param documentUniqueId
     *            A document unique id <code>UUID</code>.
     * @param documentVersionId
     *            A document version id <code>Long</code>.
     * @return A <code>DocumentVersion</code>.
     */
    public DocumentVersion readDocumentVersion(final UUID uniqueId,
            final UUID documentUniqueId, final Long documentVersionId);

    /**
     * Read a list of document versions and their delta information for a
     * container version.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param compareVersionId
     *            A container version id <code>Long</code>.
     * @return A <code>Map</code> of <code>DocumentVersion</code>s and
     *         their <code>Delta<code>s.
     */
    public Map<DocumentVersion, Delta> readDocumentVersionDeltas(
            final UUID uniqueId, final Long compareVersionId);

    /**
     * Read a list of document versions and their delta information for a
     * container version.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param compareVersionId
     *            A container version id <code>Long</code>.
     * @param compareToVersionId
     *            A container version id <code>Long</code>.
     * @return A <code>Map</code> of <code>DocumentVersion</code>s and
     *         their <code>Delta<code>s.
     */
    public Map<DocumentVersion, Delta> readDocumentVersionDeltas(
            final UUID uniqueId, final Long compareVersionId,
            final Long compareToVersionId);

    /**
     * Read a list of document versions for a container version.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List</code> of <code>DocumentVersion</code>s.
     */
    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId);

    /**
     * Read a list of document versions for a container version.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param comparator
     *            An <code>ArtifactVersion</code> <code>Comparator</code>
     *            used to order results.
     * @return A <code>List</code> of <code>DocumentVersion</code>s.
     */
    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final Comparator<ArtifactVersion> comparator);

    /**
     * Read a list of document versions for a container version.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param comparator
     *            An <code>ArtifactVersion</code> <code>Comparator</code>
     *            used to order results.
     * @param filter
     *            An <code>ArtifactVersion</code> <code>Filter</code> used
     *            to filter results.
     * @return A <code>List</code> of <code>DocumentVersion</code>s.
     */
    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter);

    /**
     * Read a list of document versions for a container version.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param filter
     *            An <code>ArtifactVersion</code> <code>Filter</code> used
     *            to filter results.
     * @return A <code>List</code> of <code>DocumentVersion</code>s.
     */
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
    public Map<User, ArtifactReceipt> readPublishedTo(final UUID uniqueId,
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
    public Map<User, ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId, final Comparator<User> comparator);

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
    public Map<User, ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId, final Comparator<User> comparator,
            final Filter<? super User> filter);

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
    public Map<User, ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId, final Filter<? super User> filter);

    /**
     * Read the team for a container.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>List</code> of <code>TeamMember</code>s.
     */
    public List<TeamMember> readTeam(final UUID uniqueId);
}
