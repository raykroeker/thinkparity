/*
 * Generated On: Sep 01 06 10:06:21 AM
 */
package com.thinkparity.ophelia.model.archive;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.filter.Filter;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.archive.monitor.OpenMonitor;
import com.thinkparity.ophelia.model.util.Opener;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Archive Model<br>
 * <b>Description:</b><br>
 *
 * @author CreateModel.groovy
 * @version 1.1.2.1
 */
public class ArchiveModel extends AbstractModel<ArchiveModelImpl> {

	/**
	 * Create a thinkParity Archive interface.
	 * 
	 * @param context
	 *            A thinkParity model context.
	 * @return A thinkParity Archive interface.
	 */
	public static InternalArchiveModel getInternalModel(final Context context,
            final Environment environment, final Workspace workspace) {
		return new InternalArchiveModel(context, environment, workspace);
	}


    /**
	 * Create a thinkParity Archive interface.
	 * 
	 * @return A thinkParity Archive interface.
	 */
	public static ArchiveModel getModel(final Environment environment,
            final Workspace workspace) {
		return new ArchiveModel(environment, workspace);
	}

    /**
	 * Create ArchiveTabModel.
	 *
	 * @param workspace
	 *		The thinkParity workspace.
	 */
	protected ArchiveModel(final Environment environment,
            final Workspace workspace) {
		super(new ArchiveModelImpl(environment, workspace));
	}

    public void openDocumentVersion(final OpenMonitor monitor,
            final UUID uniqueId, final Long versionId,
            final String versionName, final long versionSize,
            final Opener opener) {
        synchronized (getImplLock()) {
            getImpl().openDocumentVersion(monitor, uniqueId, versionId,
                    versionName, versionSize, opener);
        }
    }

    public Container readContainer(final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readContainer(uniqueId);
        }
    }

	public List<Container> readContainers() {
        synchronized (getImplLock()) {
            return getImpl().readContainers();
        }
    }

	public List<Container> readContainers(final Comparator<Artifact> comparator) {
        synchronized (getImplLock()) {
            return getImpl().readContainers(comparator);
        }
    }

    public List<Container> readContainers(final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) {
        synchronized (getImplLock()) {
            return getImpl().readContainers(comparator, filter);
        }
    }

    public List<Container> readContainers(final Filter<? super Artifact> filter) {
        synchronized (getImplLock()) {
            return getImpl().readContainers(filter);
        }
    }

    /**
     * Read a list of versions for a container.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>List&lt;ContainerVersion&gt;</code>.
     */
    public List<ContainerVersion> readContainerVersions(final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readContainerVersions(uniqueId);
        }
    }

    public List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Comparator<ArtifactVersion> comparator) {
        synchronized (getImplLock()) {
            return getImpl().readContainerVersions(uniqueId, comparator);
        }
    }

    public List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter) {
        synchronized (getImplLock()) {
            return getImpl().readContainerVersions(uniqueId, comparator,
                    filter);
        }
    }

    public List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Filter<? super ArtifactVersion> filter) {
        synchronized (getImplLock()) {
            return getImpl().readContainerVersions(uniqueId, filter);
        }
    }

    public List<Document> readDocuments(final UUID uniqueId,
            final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().readDocuments(uniqueId, versionId);
        }
    }

    public List<Document> readDocuments(final UUID uniqueId,
            final Long versionId, final Comparator<Artifact> comparator) {
        synchronized (getImplLock()) {
            return getImpl().readDocuments(uniqueId, versionId, comparator);
        }
    }

    public List<Document> readDocuments(final UUID uniqueId,
            final Long versionId, final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) {
        synchronized (getImplLock()) {
            return getImpl().readDocuments(uniqueId, versionId, comparator,
                    filter);
        }
    }

    public List<Document> readDocuments(final UUID uniqueId,
            final Long versionId, final UUID documentUniqueId,
            final Filter<? super Artifact> filter) {
        synchronized (getImplLock()) {
            return getImpl().readDocuments(uniqueId, versionId,
                    documentUniqueId, filter);
        }
    }

    public Map<DocumentVersion, Delta> readDocumentVersionDeltas(
            final UUID uniqueId, final Long compareVersionId) {
        synchronized (getImplLock()) {
            return getImpl().readDocumentVersionDeltas(uniqueId,
                    compareVersionId);
        }
    }

    public Map<DocumentVersion, Delta> readDocumentVersionDeltas(
            final UUID uniqueId, final Long compareVersionId,
            final Long compareToVersionId) {
        synchronized (getImplLock()) {
            return getImpl().readDocumentVersionDeltas(uniqueId,
                    compareVersionId, compareToVersionId);
        }
    }

    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().readDocumentVersions(uniqueId, versionId);
        }
    }

    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final Comparator<ArtifactVersion> comparator) {
        synchronized (getImplLock()) {
            return getImpl().readDocumentVersions(uniqueId, versionId,
                    comparator);
        }
    }

    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter) {
        synchronized (getImplLock()) {
            return getImpl().readDocumentVersions(uniqueId, versionId,
                    comparator, filter);
        }
    }

    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final Filter<? super ArtifactVersion> filter) {
        synchronized (getImplLock()) {
            return getImpl().readDocumentVersions(uniqueId, versionId, filter);
        }
    }

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
            final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().readPublishedTo(uniqueId, versionId);
        }
    }

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
            final Long versionId, final Comparator<User> comparator) {
        synchronized (getImplLock()) {
            return getImpl().readPublishedTo(uniqueId, versionId, comparator);
        }
    }

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
            final Filter<? super User> filter) {
        synchronized (getImplLock()) {
            return getImpl().readPublishedTo(uniqueId, versionId, comparator, filter);
        }
    }

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
            final Long versionId, final Filter<? super User> filter) {
        synchronized (getImplLock()) {
            return getImpl().readPublishedTo(uniqueId, versionId, filter);
        }
    }

    public List<TeamMember> readTeam(final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readTeam(uniqueId);
        }
    }
}
