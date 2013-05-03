/*
 * Created On:  4-Dec-06 11:02:59 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

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
public interface ActionDelegate {

    /**
     * Get the display name key associated with the 'common' action for a container.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @return The common action display name key <code>String</code> for a container.
     */
    public String getCommonActionForContainerDisplayNameKey(final Container container,
            final ContainerDraft draft);

    /**
     * Get the display name key associated with the 'common' action for the draft.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @return The common action display name key <code>String</code> for the draft.
     */
    public String getCommonActionForDraftDisplayNameKey(final Container container,
            final ContainerDraft draft);

    /**
     * Get the display name key associated with the 'common' action for a version.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param latestVersion
     *            A <code>Boolean</code>, true for the latest version.  
     * @return The common action display name key <code>String</code> for a version.
     */
    public String getCommonActionForVersionDisplayNameKey(final Container container,
            final ContainerDraft draft, final ContainerVersion version,
            final Boolean latestVersion);

    /**
     * Invoke the 'common' action for a container.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     */
    public void invokeCommonActionForContainer(final Container container,
            final ContainerDraft draft);

    /**
     * Invoke the 'common' action for the draft.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     */
    public void invokeCommonActionForDraft(final Container container,
            final ContainerDraft draft);

    /**
     * Invoke the 'common' action for a version.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param latestVersion
     *            A <code>Boolean</code>, true for the latest version.    
     */
    public void invokeCommonActionForVersion(final Container container,
            final ContainerDraft draft, final ContainerVersion version,
            final Boolean latestVersion);

    /**
     * Invoke the delete action for a container.
     * 
     * @param container
     *            A <code>Container</code>.
     */
    public void invokeDeleteForContainer(final Container container);

    /**
     * Invoke the delete action for a draft document.
     * 
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @param document
     *            A <code>Document</code>.
     */
    public void invokeDeleteForDocument(final ContainerDraft draft,
            final Document document);

    /**
     * Invoke the delete action for a draft.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     */
    public void invokeDeleteForDraft(final Container container,
            final ContainerDraft draft);

    /**
     * Invoke the default action for a container.
     * 
     * @param container
     *            A <code>Container</code>.
     */
    public void invokeForContainer(final Container container);

    /**
     * Invoke the default action for a draft document.
     * 
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @param document
     *            A <code>Document</code>.
     */
    public void invokeForDocument(final ContainerDraft draft,
            final Document document);

    /**
     * Invoke the default action for a document version.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @param delta
     *            A <code>Delta</code>.
     */
    public void invokeForDocument(final DocumentVersion version,
            final Delta delta);

    /**
     * Invoke the default action for a draft.
     * 
     * @param draft
     *            A <code>ContainerDraft</code>.
     */
    public void invokeForDraft(final ContainerDraft draft);

    /**
     * Invoke the default action for a user.
     * 
     * @param user
     *            A <code>User</code>.
     */
    public void invokeForUser(final User user);

    /**
     * Invoke the default action for a container version.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     */
    public void invokeForVersion(final ContainerVersion version,
            final Boolean showComment);

    /**
     * Determine if the 'common' action is currently enabled for a container.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @return True if the 'common' action is currently enabled for a container.
     */
    public Boolean isCommonActionForContainerEnabled(final Container container,
            final ContainerDraft draft);

    /**
     * Determine if the 'common' action is currently enabled for the draft.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @return True if the 'common' action is currently enabled for the draft.
     */
    public Boolean isCommonActionForDraftEnabled(final Container container,
            final ContainerDraft draft);

    /**
     * Determine if the 'common' action is currently enabled for a version.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param latestVersion
     *            A <code>Boolean</code>, true for the latest version.  
     * @return True if the 'common' action is currently enabled for a version.
     */
    public Boolean isCommonActionForVersionEnabled(final Container container,
            final ContainerDraft draft,  final ContainerVersion version,
            final Boolean latestVersion);
}
