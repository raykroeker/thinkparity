/**
 * Created On: 2-Aug-06 3:56:43 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container;


import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.user.UserModel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class PublishContainerProvider extends CompositeFlatSingleContentProvider {
    
    /** A thinkParity container interface. */
    private final ContainerModel containerModel;
    
    /** A contact model */
    private final ContactModel contactModel;
    
    /** A thinkParity user interface. */
    private final UserModel userModel;

    /**
     * Create a ManageContactsProvider.
     */
    public PublishContainerProvider(final Profile profile, final ContainerModel containerModel,
            final UserModel userModel, final ContactModel contactModel) {
        super(profile);
        this.containerModel = containerModel;
        this.contactModel = contactModel;
        this.userModel = userModel;
    }
        
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeFlatSingleContentProvider#getElement(java.lang.Integer, java.lang.Object)
     */
    @Override
    public Object getElement(Integer index, Object input) {
        throw Assert.createNotYetImplemented("PublishContainerProvider#getElement");
    }



    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeFlatSingleContentProvider#getElements(java.lang.Integer, java.lang.Object)
     */
    @Override
    public Object[] getElements(Integer index, Object input) {
        throw Assert.createNotYetImplemented("PublishContainerProvider#getElements");
    }

    /**
     * Read the profile.
     */
    public Profile readProfile() {
        return profile;
    }
    
    /**
     * Read the contacts.
     */
    public List<Contact> readContacts() {
        return contactModel.read();
    }
    
    /**
     * Read the team.
     */
    public List<TeamMember> readTeamMembers(final Long containerId) {
        return containerModel.readTeam(containerId); 
    }
    
    /**
     * Get the container name.
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
    public String readContainerVersionComment(final Long containerId, final Long versionId) {
        final ContainerVersion containerVersion = containerModel.readVersion(containerId, versionId);
        if ((null!=containerVersion) && (containerVersion.isSetComment())) {
            return containerVersion.getComment();
        } else {
            return null;
        }
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
        final ContainerVersion containerVersion = containerModel.readLatestVersion(containerId);
        if (null==containerVersion) {
            return null;
        } else {
            return containerVersion.getVersionId();
        }
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
        final ContainerVersion containerVersion = containerModel.readVersion(containerId, versionId);
        return containerVersion.getCreatedOn();
    }
    
    /**
     * Get the publisher.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return The publisher <code>User</code>.
     */
    public User readPublisher(final Long containerId, final Long versionId) {
        final ContainerVersion containerVersion = containerModel.readVersion(containerId, versionId);
        return userModel.read(containerVersion.getUpdatedBy());
    }
    
    /**
     * Get the users and receipt information associated with this version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return A Map<User, ArtifactReceipt>.
     */
    public Map<User, ArtifactReceipt> readVersionUsers(final Long containerId, final Long versionId) {
        return containerModel.readPublishedTo(containerId, versionId);
    }    
}
