/*
 * Generated On: Sep 01 06 10:06:21 AM
 */
package com.thinkparity.ophelia.model.archive;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.AbstractModel;
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

    public Container readContainer(final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readContainer(uniqueId);
        }
    }
}
