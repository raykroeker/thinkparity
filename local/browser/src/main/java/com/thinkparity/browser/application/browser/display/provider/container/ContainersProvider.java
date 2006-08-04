/**
 * Created On: 13-Jul-06 11:42:46 AM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.provider.container;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.application.browser.display.avatar.container.CellContainer;
import com.thinkparity.browser.application.browser.display.avatar.container.CellDocument;
import com.thinkparity.browser.application.browser.display.avatar.container.MainCellContainerVersion;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;

import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.artifact.ArtifactVersion;
import com.thinkparity.model.parity.model.contact.ContactModel;
import com.thinkparity.model.parity.model.container.Container;
import com.thinkparity.model.parity.model.container.ContainerModel;
import com.thinkparity.model.parity.model.container.ContainerVersion;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.parity.model.sort.VersionIdComparator;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContainersProvider extends CompositeFlatSingleContentProvider {
    
    private final SingleContentProvider containerProvider;
    
    private final FlatContentProvider containersProvider;

    /** The flat container version data provider. */
    private final FlatContentProvider containerVersionsProvider;

    private final FlatContentProvider documentsProvider;

    /** The flat list content providers. */
    private final FlatContentProvider[] flatProviders;
    
    /** The single content providers. */
    private final SingleContentProvider[] singleProviders;

    /** Version id comparator. */
    private final Comparator<ArtifactVersion> versionIdAscending =
            new VersionIdComparator(Boolean.TRUE);

    /**
     * Create a ContainersProvider.
     * 
     * @param profile
     *            A thinkParity profile.
     * @param aModel
     *            A thinkParity artifact interface.
     * @param containerModel
     *            A thinkParity container interface.
     * @param dModel
     *            A thinkParity document interface.          
     * @param cModel
     *            A thinkParity contact interface.
     * @param mModel
     *            A thinkParity system message interface.
     * @param loggedInUser
     *            The thinkParity session user.
     */
    public ContainersProvider(final Profile profile, final ArtifactModel aModel,
            final ContainerModel containerModel, final DocumentModel dModel,
            final ContactModel cModel, final SystemMessageModel mModel) {
        super(profile);
        this.containerProvider = new SingleContentProvider(profile) {
            public Object getElement(final Object input) {
                final Long containerId = (Long) input;
                return toDisplay(containerModel.read(containerId));
            }
        };
        this.containerVersionsProvider = new FlatContentProvider(profile) {
            public Object[] getElements(final Object input) {
                Assert.assertNotNull("", input);
                Assert.assertOfType("", CellContainer.class, input);
                final CellContainer container = (CellContainer) input;
                return toDisplay(container, containerModel.readVersions(container.getId(), versionIdAscending));
            }
        };
        this.containersProvider = new FlatContentProvider(profile) {
            public Object[] getElements(final Object input) {
                return toDisplay(containerModel.read(), containerModel, dModel);
            }            
        };
        this.documentsProvider = new FlatContentProvider(profile) {
            public Object[] getElements(final Object input) { return new CellDocument[] {}; }
        };
        this.flatProviders = new FlatContentProvider[] {containersProvider, containerVersionsProvider, documentsProvider};
        this.singleProviders = new SingleContentProvider[] {containerProvider};
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider#getElement(java.lang.Integer,
     *      java.lang.Object)
     * 
     */
    public Object getElement(final Integer index, final Object input) {
        Assert.assertNotNull("Index cannot be null.", index);
        Assert.assertTrue(
                "Index must lie within [0," + (singleProviders.length - 1) + "]",
                index >= 0 && index < singleProviders.length);
        return singleProviders[index].getElement(input);
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.provider.CompositeFlatContentProvider#getElements(java.lang.Integer,
     *      java.lang.Object)
     * 
     */
    public Object[] getElements(final Integer index, final Object input) {
        Assert.assertNotNull("Index cannot be null.", index);
        Assert.assertTrue(
                "Index must lie within [0," + (flatProviders.length - 1) + "]",
                index >= 0 && index < flatProviders.length);
        return flatProviders[index].getElements(input);
    }

    /**
     * Create a dislplay for a list of container versions.
     * 
     * @param container
     *            A container.
     * @param versions
     *            A list of container versions.
     * @return A list of container version displays.
     */
    private MainCellContainerVersion[] toDisplay(final CellContainer container,
            final List<ContainerVersion> versions) {
        final List<MainCellContainerVersion> display = new ArrayList<MainCellContainerVersion>(versions.size());
        for(final ContainerVersion version : versions) {
            display.add(new MainCellContainerVersion(container, version));
        }
        return display.toArray(new MainCellContainerVersion[] {});
    }

    /**
     * Obtain a displayable version of a container.
     * 
     * @param container
     *          The container
     * @param ctrModel
     *          The parity container interface.
     * @param dModel
     *          The parity document interface.
     *          
     * @return The displayable container.
     */
    private CellContainer toDisplay(final Container container) {
        return null == container ? null : new CellContainer(container);
    }      

    /**
     * Obtain a displayable version of a list of containers.
     * 
     * @param containers
     *          The containers
     * @param ctrModel
     *          The parity container interface.
     * @param dModel
     *          The parity document interface.
     *          
     * @return The displayable containers.
     */
    private CellContainer[] toDisplay(final List<Container> containers,
            final ContainerModel ctrModel, final DocumentModel dModel) {
        final List<CellContainer> display = new ArrayList<CellContainer>();
        for(final Container container : containers) {
            display.add(toDisplay(container));
        }
        return display.toArray(new CellContainer[] {});      
    }
}
