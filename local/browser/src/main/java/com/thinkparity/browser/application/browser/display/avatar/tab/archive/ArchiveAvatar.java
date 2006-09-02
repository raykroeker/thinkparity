/*
 * Created On: Sept 1, 2006, 8:10 AM
 */
package com.thinkparity.browser.application.browser.display.avatar.tab.archive;

import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.application.browser.display.avatar.tab.TabAvatar;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCell;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ArchiveAvatar extends TabAvatar<ArchiveModel> {
    
    /** Create ArchiveAvatar. */
    public ArchiveAvatar() {
        super(AvatarId.TAB_ARCHIVE, new ArchiveModel());
    }

    /**
     * Synchronize the artifact in the tab's list.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @param remote
     *            A <code>Boolean</code> indicating whether or not this was
     *            the result of a remote event.
     */
    public void syncArtifact(final Long artifactId, final Boolean remote) {
        final TabCell selectedCell = getSelectedCell();
        model.synchronizeArtifact(artifactId, remote);
        setSelectedCell(selectedCell);
    }
}
