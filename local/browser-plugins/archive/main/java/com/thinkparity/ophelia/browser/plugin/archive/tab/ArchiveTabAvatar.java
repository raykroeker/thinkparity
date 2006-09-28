/*
 * Created On: Sept 1, 2006, 8:10 AM
 */
package com.thinkparity.ophelia.browser.plugin.archive.tab;

import java.util.UUID;

import com.thinkparity.ophelia.browser.platform.plugin.extension.TabExtension;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabExtensionAvatar;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ArchiveTabAvatar extends TabExtensionAvatar<ArchiveTabModel> {

    /** Create ArchiveTabAvatar. */
    ArchiveTabAvatar(final TabExtension extension) {
        super(extension, new ArchiveTabModel(extension));
        installMouseOverTracker();
    }

    /**
     * Synchronize a container.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     */
    void synchronizeContainer(final UUID uniqueId) {
        saveSelection();
        model.synchronizeContainer(uniqueId);
        restoreSelection();
    }
}
