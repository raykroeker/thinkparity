/*
 * Created On:  1-Dec-06 8:58:11 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import javax.swing.JPopupMenu;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDraft;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface VersionsPopupFactory {

    /**
     * Create a popup menu for a draft document.
     * 
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @param document
     *            A draft <code>Document</code>.
     * @return A <code>JPopupMenu</code>.
     */
    public JPopupMenu createDraftDocumentPopup(final ContainerDraft draft,
            final Document document);

    /**
     * Create a popup menu for a draft.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @return A <code>JPopupMenu</code>.
     */
    public JPopupMenu createDraftPopup(final Container container,
            final ContainerDraft draft);

    /**
     * Create a popup menu for the published by user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return A <code>JPopupMenu</code>.
     */
    public JPopupMenu createPublishedByPopup(final User user);

    /**
     * Create a popup menu for the published by user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return A <code>JPopupMenu</code>.
     */
    public JPopupMenu createPublishedToPopup(final User user,
            final ArtifactReceipt receipt);

    /**
     * Create a popup menu for the version's document version.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @param delta
     *            A <code>Delta</code>.
     * @return A <code>JPopupMenu</code>.
     */
    public JPopupMenu createVersionDocumentPopup(final DocumentVersion version,
            final Delta delta);

    /**
     * Create a popup menu for the version.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @return A <code>JPopupMenu</code>.
     */
    public JPopupMenu createVersionPopup(final ContainerVersion version);
}
