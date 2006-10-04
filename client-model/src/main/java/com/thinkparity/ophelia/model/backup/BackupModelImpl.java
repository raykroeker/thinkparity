/*
 * Generated On: Oct 04 06 09:40:46 AM
 */
package com.thinkparity.ophelia.model.backup;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.util.sort.ComparatorBuilder;
import com.thinkparity.ophelia.model.util.sort.ModelSorter;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Backup Model Implementation</br>
 * <b>Description:</b><br>
 *
 * @author CreateModel.groovy
 * @version 1.1.2.1
 */
final class BackupModelImpl extends AbstractModelImpl {

    /** A default artifact comparator. */
    private final Comparator<Artifact> defaultComparator;

    /** A default artifact filter. */
    private final Filter<? super Artifact> defaultFilter;

    /** A default artifact version comparator. */
    private final Comparator<ArtifactVersion> defaultVersionComparator;

    /** A default artifact version filter. */
    private final Filter<ArtifactVersion> defaultVersionFilter;

    /**
     * Create BackupModelImpl.
     *
     * @param environment
     *      A thinkParity <code>Environment</code>.
     * @param workspace
     *		The thinkParity <code>Workspace</code>.
     */
    BackupModelImpl(final Environment environment, final Workspace workspace) {
        super(environment, workspace);
        this.defaultComparator = new ComparatorBuilder().createByName();
        this.defaultFilter = FilterManager.createDefault();
        this.defaultVersionComparator = new ComparatorBuilder().createVersionById(Boolean.TRUE);
        this.defaultVersionFilter = FilterManager.createDefault();
    }

    Container readContainer(final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        try {
            assertBackupOnline();
            return getSessionModel().readBackupContainer(localUserId(), uniqueId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the containers from the backup.
     * 
     * @return A <code>List&lt;Container&gt;</code>.
     */
    List<Container> readContainers() {
        logger.logApiId();
        return readContainers(defaultComparator);
    }

    /**
     * Read the containers from the backup.
     * 
     * @param comparator
     *            A <code>Comparator&lt;Artifact&gt;</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    List<Container> readContainers(final Comparator<Artifact> comparator) {
        logger.logApiId();
        return readContainers(comparator, defaultFilter);
    }

    /**
     * Read the containers from the backup.
     * 
     * @param comparator
     *            A <code>Comparator&lt;Artifact&gt;</code>.
     * @param filter
     *            A <code>Filter&lt;? super Artifact&gt;</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    List<Container> readContainers(final Comparator<Artifact> comparator, final Filter<? super Artifact> filter) {
        logger.logApiId();
        try {
            assertBackupOnline();
            final List<Container> containers =
                getSessionModel().readBackupContainers(localUserId());
            FilterManager.filter(containers, filter);
            ModelSorter.sortContainers(containers, comparator);
            return containers;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the containers from the backup.
     * 
     * @param filter
     *            A <code>Filter&lt;Artifact&gt;</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    List<Container> readContainers(final Filter<? super Artifact> filter) {
        logger.logApiId();
        return readContainers(defaultComparator, filter);
    }

    List<ContainerVersion> readContainerVersions(final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        return readContainerVersions(uniqueId, defaultVersionComparator,
                defaultVersionFilter);
    }

    List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Comparator<ArtifactVersion> comparator) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("comparator", comparator);
        return readContainerVersions(uniqueId, comparator,
                defaultVersionFilter);
    }


    List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("comparator", comparator);
        logger.logVariable("filter", filter);
        try {
            assertBackupOnline();
            final List<ContainerVersion> versions =
                getSessionModel().readBackupContainerVersions(
                        localUserId(), uniqueId);
            FilterManager.filter(versions, filter);
            ModelSorter.sortContainerVersions(versions, comparator);
            return versions;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Filter<? super ArtifactVersion> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("filter", filter);
        return readContainerVersions(uniqueId, defaultVersionComparator,
                filter);
    }

    List<Document> readDocuments(final UUID uniqueId,
            final Long versionId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        return readDocuments(uniqueId, versionId, defaultComparator,
                defaultFilter);
    }

    List<Document> readDocuments(final UUID uniqueId,
            final Long versionId, final Comparator<Artifact> comparator) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("comparator", comparator);
        return readDocuments(uniqueId, versionId, comparator, defaultFilter);
    }

    List<Document> readDocuments(final UUID uniqueId,
            final Long versionId, final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("filter", filter);
        try {
            assertBackupOnline();
            final List<Document> documents =
                getSessionModel().readBackupDocuments(localUserId(),
                        uniqueId, versionId);
            FilterManager.filter(documents, filter);
            ModelSorter.sortDocuments(documents, comparator);
            return documents;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    List<Document> readDocuments(final UUID uniqueId, final Long versionId,
            final UUID documentUniqueId, final Filter<? super Artifact> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("filter", filter);
        return readDocuments(uniqueId, versionId, defaultComparator, filter);
    }

    List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final UUID documentUniqueId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("documentUniqueId", documentUniqueId);
        return readDocumentVersions(uniqueId, versionId, documentUniqueId,
                defaultVersionComparator, defaultVersionFilter);
    }

    List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final UUID documentUniqueId,
            final Comparator<ArtifactVersion> comparator) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("documentUniqueId", documentUniqueId);
        logger.logVariable("comparator", comparator);
        return readDocumentVersions(uniqueId, versionId, documentUniqueId,
                comparator, defaultVersionFilter);
    }

    List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final UUID documentUniqueId,
            final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("documentUniqueId", documentUniqueId);
        logger.logVariable("comparator", comparator);
        try {
            assertBackupOnline();
            final List<DocumentVersion> versions =
                getSessionModel().readBackupDocumentVersions(
                        localUserId(), uniqueId, versionId, documentUniqueId);
            FilterManager.filter(versions, filter);
            ModelSorter.sortDocumentVersions(versions, comparator);
            return versions;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final UUID documentUniqueId,
            final Filter<? super ArtifactVersion> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("documentUniqueId", documentUniqueId);
        logger.logVariable("filter", filter);
        return readDocumentVersions(uniqueId, versionId, documentUniqueId,
                defaultVersionComparator, filter);
    }

    List<JabberId> readTeamIds(final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        try {
            assertBackupOnline();
            return getSessionModel().readBackupTeamIds(localUserId(),
                    uniqueId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Assert that the backup server is online.
     *
     */
    private void assertBackupOnline() {
        Assert.assertTrue(isBackupOnline(),
                "The backup server is not online.");
    }

    /**
     * Determine whether or not the backup is online or not.
     * 
     * @return True if the backup is online; false otherwise.
     */
    private Boolean isBackupOnline() {
        // TODO Determine if the backup is actually online or not.
        return Boolean.TRUE;
    }
}
