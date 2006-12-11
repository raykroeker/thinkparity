/*
 * Created On:  1-Dec-06 8:58:11 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.util.Map;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDraft;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanelPopupDelegate;

/**
 * <b>Title:</b>thinkParity Container Tab Popup Delegate<br>
 * <b>Description:</b>Defines the popup show interface for the popup tab
 * panels.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface PopupDelegate extends TabPanelPopupDelegate {

    /**
     * Display a popup menu for a container.
     * 
     * @param container
     *            A <code>Container</code>.
     */
    public void showForContainer(final Container container);

    /**
     * Display a popup menu for a draft.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     */
    public void showForDraft(final Container container,
            final ContainerDraft draft);

    /**
     * Display a popup menu for the version.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     */
    public void showForVersion(final ContainerVersion version,
            final Map<DocumentVersion, Delta> documentVersions,
            final Map<User, ArtifactReceipt> publishedTo);
}
