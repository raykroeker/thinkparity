/**
 * Created On: 18-Oct-06 6:29:19 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.browser.application.browser.display.provider.SingleContentProvider;
import com.thinkparity.ophelia.model.container.ContainerModel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContainerVersionCommentProvider extends SingleContentProvider {
    
    /** A thinkParity container interface. */
    private final ContainerModel containerModel;
    
    /** Create ContainerVersionCommentProvider. */
    public ContainerVersionCommentProvider(final Profile profile,
            final ContainerModel containerModel) {
        super(profile);
        this.containerModel = containerModel;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.provider.SingleContentProvider#getElement(java.lang.Object)
     * 
     */
    public Object getElement(final Object input) {
        throw Assert.createNotYetImplemented("ContainerVersionCommentProvider#getElement");
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
