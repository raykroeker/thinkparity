/*
 * Created On:  1-Dec-06 8:58:11 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

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
     * Display a popup menu for the version's document version.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @param delta
     *            A <code>Delta</code>.
     */
    public void showForDocument(final DocumentVersion version, final Delta delta);

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
     * Display a popup menu for a user.
     * 
     * @param user
     *            A <code>User</code>.
     */
    public void showForUser(final User user);

    /**
     * Display a popup menu for the version.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     */
    public void showForVersion(final ContainerVersion version);
}
