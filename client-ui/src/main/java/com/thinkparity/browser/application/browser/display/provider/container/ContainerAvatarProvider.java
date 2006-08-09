/**
 * Created On: 13-Jul-06 11:42:46 AM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.provider.container;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.application.browser.display.avatar.container.MainCellContainer;
import com.thinkparity.browser.application.browser.display.avatar.container.MainCellContainerVersion;
import com.thinkparity.browser.application.browser.display.avatar.container.MainCellVersionDocument;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;

import com.thinkparity.model.parity.model.artifact.ArtifactVersion;
import com.thinkparity.model.parity.model.container.Container;
import com.thinkparity.model.parity.model.container.ContainerModel;
import com.thinkparity.model.parity.model.container.ContainerVersion;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.parity.model.sort.VersionIdComparator;

/**
 * <b>Title:</b>thinkParity Container Tab Provider<br>
 * <b>Description:</b>A thinkParity container tab provider reads from the
 * various thinkParity model interfaces to provide the container tab with its
 * data.
 * 
 * It currently provides both display and non-display data.
 * 
 * @author rob_masako@shaw.ca; raymond@thinkparity.com
 * @version 1.1.2.5
 */
public class ContainerAvatarProvider extends CompositeFlatSingleContentProvider {

    /** Reads a single container. */
    private final SingleContentProvider containerProvider;

    /** Reads a list of containers. */
    private final FlatContentProvider containers;

    /** Reads the draft modified property for a document. */
    private final SingleContentProvider documentIsDraftModifiedProvider;

    /** Reads a draft. */
    private final SingleContentProvider draftProvider;

    /** Contains containers; container versions; version documents. */
    private final FlatContentProvider[] flatProviders;

    /** Containers the container; is draft modified; and draft providers. */
    private final SingleContentProvider[] singleProviders;

    /** Reads a list of documents. */
    private final FlatContentProvider versionDocuments;

    /** Used by the container versions provider to sort the list of versions. */
    private final Comparator<ArtifactVersion> versionIdAscending =
            new VersionIdComparator(Boolean.TRUE);

    /** Reads a list of container versions. */
    private final FlatContentProvider versions;

    /**
     * Create ContainerProvider.
     * 
     * @param profile
     *            The local user profile.
     * @param containerModel
     *            A thinkParity container interface.
     * @param documentModel
     *            A thinkParity document interface.
     */
    public ContainerAvatarProvider(final Profile profile,
            final ContainerModel containerModel,
            final DocumentModel documentModel) {
        super(profile);
        this.containerProvider = new SingleContentProvider(profile) {
            public Object getElement(final Object input) {
                final Long containerId = (Long) input;
                return toDisplay(containerModel.read(containerId));
            }
        };
        this.versions = new FlatContentProvider(profile) {
            public Object[] getElements(final Object input) {
                Assert.assertNotNull("[NULL INPUT]", input);
                Assert.assertOfType("[INPUT IS OF WRONG TYPE]", MainCellContainer.class, input);
                final MainCellContainer container = (MainCellContainer) input;
                return toDisplay(container, containerModel.readVersions(container.getId(), versionIdAscending));
            }
        };
        this.containers = new FlatContentProvider(profile) {
            public Object[] getElements(final Object input) {
                return toDisplay(containerModel.read(), containerModel, documentModel);
            }            
        };
        this.documentIsDraftModifiedProvider = new SingleContentProvider(profile) {
            @Override
            public Object getElement(final Object input) {
                Assert.assertNotNull("[INPUT IS NULL]", input);
                Assert.assertOfType("[INPUT IS OF WRONG TYPE]", Long.class, input);
                return documentModel.isDraftModified((Long) input);
            }
        };
        this.versionDocuments = new FlatContentProvider(profile) {
            @Override
            public Object[] getElements(final Object input) {
                Assert.assertNotNull("[NULL INPUT]", input);
                Assert.assertOfType("[INPUT IS OF WRONG TYPE]", MainCellContainerVersion.class, input);
                final MainCellContainerVersion typedInput = (MainCellContainerVersion) input;
                final Long containerId = typedInput.getArtifactId();
                final Long versionId = typedInput.getVersionId();
                return toDisplay(typedInput, containerModel.readDocuments(containerId, versionId));
            }
        };
        this.draftProvider = new SingleContentProvider(profile) {
            public Object getElement(Object input) {
                Assert.assertNotNull("[NULL INPUT]", input);
                Assert.assertOfType("[INPUT IS OF WRONG TYPE]", Long.class, input);
                return containerModel.readDraft((Long) input);
            }
        };
        this.flatProviders = new FlatContentProvider[] {containers, versions, versionDocuments};
        this.singleProviders = new SingleContentProvider[] {containerProvider, draftProvider, documentIsDraftModifiedProvider};
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
    private MainCellContainerVersion[] toDisplay(final MainCellContainer container,
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
    private MainCellContainer toDisplay(final Container container) {
        return null == container ? null : new MainCellContainer(container);
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
    private MainCellContainer[] toDisplay(final List<Container> containers,
            final ContainerModel ctrModel, final DocumentModel dModel) {
        final List<MainCellContainer> display = new ArrayList<MainCellContainer>();
        for(final Container container : containers) {
            display.add(toDisplay(container));
        }
        return display.toArray(new MainCellContainer[] {});      
    }

    /**
     * Create a display document for a version.
     * 
     * @param version
     *            A display version.
     * @param document
     *            A document.
     * @return A display document.
     */
    private MainCellVersionDocument toDisplay(
            final MainCellContainerVersion version, final Document document) {
        final MainCellVersionDocument display = new MainCellVersionDocument(version, document);
        return display;
    }      

    /**
     * Create an array of display documents for a version.
     * 
     * @param version
     *            A display version.
     * @param versionDocuments
     *            A list of documents.
     * @return An array of display documents.
     */
    private MainCellVersionDocument[] toDisplay(
            final MainCellContainerVersion version,
            final List<Document> documents) {
        final List<MainCellVersionDocument> list =
            new ArrayList<MainCellVersionDocument>(documents.size());
        for(final Document document : documents) {
            list.add(toDisplay(version, document));
        }
        return list.toArray(new MainCellVersionDocument[] {});
    }
}
