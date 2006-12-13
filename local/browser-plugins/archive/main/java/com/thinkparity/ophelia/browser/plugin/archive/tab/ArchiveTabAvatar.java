/*
 * Created On: Sept 1, 2006, 8:10 AM
 */
package com.thinkparity.ophelia.browser.plugin.archive.tab;

import java.util.UUID;

import com.thinkparity.ophelia.browser.platform.plugin.extension.TabPanelExtension;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabPanelExtensionAvatar;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ArchiveTabAvatar extends TabPanelExtensionAvatar<ArchiveTabModel> {

    /**
     * Create ArchiveTabAvatar.
     * 
     */
    ArchiveTabAvatar(final TabPanelExtension extension) {
        super(extension, new ArchiveTabModel(extension));
        model.setSession(getSession());
        setSortByDelegate(model);
    }

    /**
     * Synchronize a container.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     */
    void synchronizeContainer(final UUID uniqueId) {
        model.synchronizeContainer(uniqueId);
    }
}
