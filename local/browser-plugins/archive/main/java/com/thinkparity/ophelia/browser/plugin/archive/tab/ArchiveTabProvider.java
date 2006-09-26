/*
 * Created On: Sep 1, 2006 8:17:27 AM
 */
package com.thinkparity.ophelia.browser.plugin.archive.tab;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.browser.platform.plugin.extension.TabExtensionModelContentProvider;
import com.thinkparity.ophelia.model.archive.ArchiveModel;

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

    /**
     * Read a list of container versions from the archive.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    public List<ContainerVersion> readContainerVersions(final UUID uniqueId) {
        return archiveModel.readContainerVersions(uniqueId);
    }
}
