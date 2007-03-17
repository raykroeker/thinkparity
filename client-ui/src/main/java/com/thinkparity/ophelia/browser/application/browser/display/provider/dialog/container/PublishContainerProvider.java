/**
 * Created On: 2-Aug-06 3:56:43 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container;


import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.browser.application.browser.display.provider.ContentProvider;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.user.UserModel;
import com.thinkparity.ophelia.model.user.UserUtils;

/**
 * <b>Title:</b>thinkParity OpheliaUI Publish Container Provider<br>
 * <b>Description:</b><br>
 * 
 * @author robert@thinkparity.com
 * @version 1.1.2.4
 */
public class PublishContainerProvider extends ContentProvider {

    /** An instance of <code>UserUtils</code>. */
    private static final UserUtils USER_UTIL = UserUtils.getInstance();
    
    /** A contact model */
    private final ContactModel contactModel;
    
    /** A thinkParity container interface. */
    private final ContainerModel containerModel;
    
    /** A thinkParity user interface. */
    private final UserModel userModel;

    /**
     * Create PublishContainerProvider.
     * 
     * @param profile
     *            The user's <code>Profile</code>.
     * @param containerModel
     *            An instance of <code>ContainerModel</code>.
     * @param userModel
     *            An instance of <code>UserModel</code>.
     * @param contactModel
     *            An instance of <code>ContactModel</code>.
     */
    public PublishContainerProvider(final Profile profile,
            final ContainerModel containerModel, final UserModel userModel,
            final ContactModel contactModel) {
        super(profile);
        this.containerModel = containerModel;
        this.contactModel = contactModel;
        this.userModel = userModel;
    }

    /**
     * Get the container name.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return The name <code>String</code>.             
     */
    public String readContainerName(final Long containerId) {
        return containerModel.read(containerId).getName();
    }

    /**
     * Get the comment associated with a container version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.  
     * @return The comment <code>String</code>.              
     */
    public String readContainerVersionComment(final Long containerId,
            final Long versionId) {
        return containerModel.readVersion(containerId, versionId).getComment();
    }

    /**
     * Get the version Id associated with the latest version, or null if
     * there is no version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return The version id <code>Long</code>.
     */
    public Long readLatestVersionId(final Long containerId) {
        final ContainerVersion version = containerModel.readLatestVersion(
                containerId);
        return null == version ? null : version.getVersionId();
    }

    /**
     * Get the users and receipt information associated with this version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return A List of <code><ArtifactReceipt></code>.
     */
    public List<ArtifactReceipt> readLatestVersionPublishedTo(
            final Long containerId) {
        final ContainerVersion latestVersion = containerModel.readLatestVersion(
                containerId);
        return containerModel.readPublishedTo(containerId, latestVersion.getVersionId());
    }

    /**
     * Read the updated by user of the latest version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return The publisher <code>User</code>.
     */
    public User readLatestVersionUpdatedBy(final Long containerId) {
        final ContainerVersion latestVersion = containerModel.readLatestVersion(
                containerId);
        return userModel.read(latestVersion.getUpdatedBy());
    }

    /**
     * Get the publish date.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return The publish date <code>Calendar</code>.
     */
    public Calendar readPublishDate(final Long containerId, final Long versionId) {
        final ContainerVersion version = containerModel.readVersion(containerId,
                versionId);
        return version.getCreatedOn();
    }
    
    /**
     * Read a list of the contacts that the user can publish to.
     * 
     * @param teamMembers
     *            A <code>List</code> of <code>TeamMember</code>s to filter
     *            from the list.
     * @return A <code>List</code> of <code>Contact</code>.
     */
    public List<Contact> readPublishToContacts(
            final List<TeamMember> teamMembers) {
        final List<Contact> contacts = contactModel.readContainerPublishTo();
        for (final TeamMember teamMember : teamMembers)
            USER_UTIL.remove(contacts, teamMember);
        return contacts;
    }
    
    /**
     * Read a list of team members that the user can publish to.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return A <code>List</code> of <code>TeamMember</code>s.
     */
    public List<TeamMember> readPublishToTeam(final Long containerId) {
        return containerModel.readPublishToTeam(containerId);
    }    
}
