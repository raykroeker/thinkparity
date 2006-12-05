/**
 * Created On: 4-Dec-06 10:50:56 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container;

import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.ophelia.model.container.ContainerModel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class CreateContainerProvider extends FlatContentProvider {

    /** A thinkParity container interface. */
    private final ContainerModel containerModel;
    
    /** Create CreateContainerProvider. */
    public CreateContainerProvider(final Profile profile,
            final ContainerModel containerModel) {
        super(profile);
        this.containerModel = containerModel;
    }

    @Override
    public Object[] getElements(Object input) {
        throw Assert.createNotYetImplemented("CreateContainerProvider#getElements");
    }

    /**
     * Read the list of containers.
     * 
     * @param containerId
     *            A container id <code>Long</code>.       
     * @return An array of <code>Document</code>.
     */
    public List<Container> readContainers() {
        return containerModel.read();
    }
}
