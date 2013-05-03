/**
 * Created On: 18-Oct-06 6:29:19 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container;

import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;

import com.thinkparity.ophelia.browser.application.browser.display.provider.ContentProvider;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContainerVersionProvider extends ContentProvider {

    /** A thinkParity container interface. */
    private final ContainerModel containerModel;

    /**
     * Create ContainerVersionProvider.
     * 
     * @param profileModel
     *            An instance of <code>ProfileModel</code>.
     * @param containerModel
     *            An instance of <code>ContainerModel</code>.
     */
    public ContainerVersionProvider(final ProfileModel profileModel,
            final ContainerModel containerModel) {
        super(profileModel);
        this.containerModel = containerModel;
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
}
