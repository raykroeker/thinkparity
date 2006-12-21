/*
 * Created On: Sep 1, 2006 8:17:27 AM
 */
package com.thinkparity.ophelia.browser.plugin.archive.tab;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.archive.ArchiveModel;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.view.DocumentView;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabExtensionModelContentProvider;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ArchiveTabProvider extends TabExtensionModelContentProvider {

    /** An artifact archive provider. */
    private final ArchiveModel archiveModel;

    /**
     * Create ArchiveTabProvider.
     * 
     * @param profile
     *            The local user profile.
     * @param archiveModel
     *            A thinkParity archive interface.
     */
    public ArchiveTabProvider(final ArchiveModel archiveModel) {
        super();
        this.archiveModel = archiveModel;
    }

    /**
     * Read a container from the archive.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>Container</code>.
     */
    public Container readContainer(final UUID uniqueId) {
        return archiveModel.readContainer(uniqueId);
    }

    /**
     * Read a list of containers from the archive.
     * 
     * @return A <code>List&lt;Container&gt;</code>.
     */
    public List<Container> readContainers() {
        return archiveModel.readContainers();
    }

    /**
     * Read a list of container version documents from the archive.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    public List<Document> readContainerVersionDocuments(
            final UUID uniqueId, final Long versionId) {
        return archiveModel.readDocuments(uniqueId, versionId);
    }

    public List<DocumentView> readDocumentVersionDeltas(
            final UUID uniqueId, final Long compareVersionId) {
        final Map<DocumentVersion, Delta> versions =
            archiveModel.readDocumentVersionDeltas(uniqueId, compareVersionId);
        final List<DocumentView> views = new ArrayList<DocumentView>(versions.size());
        DocumentView view;
        for (final Entry<DocumentVersion, Delta> entry : versions.entrySet()) {
            view = new DocumentView();
            view.setVersion(entry.getKey());
            view.setDelta(entry.getValue());
            views.add(view);
        }
        return views;
    }

    public List<DocumentView> readDocumentViews(final UUID uniqueId,
            final Long compareVersionId, final Long compareToVersionId) {
        final Map<DocumentVersion, Delta> versions =
            archiveModel.readDocumentVersionDeltas(uniqueId, compareVersionId,
                    compareToVersionId);
        final List<DocumentView> views = new ArrayList<DocumentView>(versions.size());
        DocumentView view;
        for (final Entry<DocumentVersion, Delta> entry : versions.entrySet()) {
            view = new DocumentView();
            view.setVersion(entry.getKey());
            view.setDelta(entry.getValue());
            views.add(view);
        }
        return views;
    }

    public Map<User, ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId) {
        return archiveModel.readPublishedTo(uniqueId, versionId);
    }

    public List<TeamMember> readTeam(final UUID uniqueId) {
        return archiveModel.readTeam(uniqueId);
    }

    /**
     * Read a list of container versions from the archive.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    public List<ContainerVersion> readVersions(final UUID uniqueId) {
        return archiveModel.readContainerVersions(uniqueId);
    }
}
