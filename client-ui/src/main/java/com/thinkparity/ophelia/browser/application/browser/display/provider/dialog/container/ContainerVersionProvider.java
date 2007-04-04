/**
 * Created On: 18-Oct-06 6:29:19 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.user.UserModel;

import com.thinkparity.ophelia.browser.application.browser.display.provider.SingleContentProvider;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContainerVersionProvider extends SingleContentProvider {
    
    /** A thinkParity container interface. */
    private final ContainerModel containerModel;
    
    /** A thinkParity user interface. */
    private final UserModel userModel;
    
    /** Create ContainerVersionProvider. */
    public ContainerVersionProvider(final ProfileModel profileModel,
            final ContainerModel containerModel, UserModel userModel) {
        super(profileModel);
        this.containerModel = containerModel;
        this.userModel = userModel;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.provider.SingleContentProvider#getElement(java.lang.Object)
     * 
     */
    public Object getElement(final Object input) {
        throw Assert.createNotYetImplemented("ContainerVersionProvider#getElement");
    }
    
    /**
     * Read a container version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.          
     * @return A ContainerVersion.
     */
    public ContainerVersion readVersion(final Long containerId, final Long versionId) {
        return containerModel.readVersion(containerId, versionId);
    }
    
    /**
     * Read a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>.
     */
    public User readUser(final JabberId userId) {
        return userModel.read(userId);
    }
}
