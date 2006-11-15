/**
 * Created On: 14-Nov-06 2:43:32 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container;

import java.util.Calendar;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.browser.application.browser.display.provider.SingleContentProvider;
import com.thinkparity.ophelia.model.container.ContainerModel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ExportProvider extends SingleContentProvider {
    
    /** A thinkParity container interface. */
    private final ContainerModel containerModel;
    
    /** Create RenameDocumentProvider. */
    public ExportProvider(final Profile profile,
            final ContainerModel containerModel) {
        super(profile);
        this.containerModel = containerModel;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.provider.SingleContentProvider#getElement(java.lang.Object)
     */
    @Override
    public Object getElement(Object input) {
        throw Assert.createNotYetImplemented("ExportProvider#getElement");
    }
    
    /**
     * Get the container name.
     * @param containerId
     *            A container id <code>Long</code>.
     * @return The name <code>String</code>.             
     */
    public String getContainerName(final Long containerId) {
        return containerModel.read(containerId).getName();
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
    public Calendar getPublishDate(final Long containerId, final Long versionId) {
        final ContainerVersion containerVersion = containerModel.readVersion(containerId, versionId);
        return containerVersion.getCreatedOn();
    }
}
