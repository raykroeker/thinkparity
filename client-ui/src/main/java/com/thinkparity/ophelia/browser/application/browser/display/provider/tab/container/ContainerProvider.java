/**
 * Created On: 13-Jul-06 11:42:46 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.tab.container;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.SingleContentProvider;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerVersionCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerVersionDocumentCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerVersionSentToCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerVersionSentToUserCell;
import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.document.DocumentModel;

/**
 * <b>Title:</b>thinkParity Container TabId Provider<br>
 * <b>Description:</b>A thinkParity container tab provider reads from the
 * various thinkParity model interfaces to provide the container tab with its
 * data.
 * 
 * It currently provides both display and non-display data.
 * 
 * @author rob_masako@shaw.ca; raymond@thinkparity.com
 * @version 1.1.2.5
 */
public class ContainerProvider extends CompositeFlatSingleContentProvider {

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

    /** A container id list provider (search). */
    private final FlatContentProvider searchResults;

    /** Containers the container; is draft modified; and draft providers. */
    private final SingleContentProvider[] singleProviders;

    /** Reads a list of documents. */
    private final FlatContentProvider versionDocuments;
    
    /** Reads a list of users. */
    private final FlatContentProvider versionUsers;

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
    public ContainerProvider(final Profile profile,
            final ContainerModel containerModel,
            final DocumentModel documentModel) {
        super(profile);
        this.containerProvider = new SingleContentProvider(profile) {
            public Object getElement(final Object input) {
                final Long containerId = (Long) input;
                return toDisplay(containerModel.read(containerId), containerModel);
            }
        };
        this.versions = new FlatContentProvider(profile) {
            public Object[] getElements(final Object input) {
                Assert.assertNotNull("[NULL INPUT]", input);
                Assert.assertOfType("[INPUT IS OF WRONG TYPE]", ContainerCell.class, input);
                final ContainerCell container = (ContainerCell) input;
                return toDisplay(container, containerModel.readVersions(container.getId()));
            }
        };
        this.containers = new FlatContentProvider(profile) {
            public Object[] getElements(final Object input) {
                return toDisplay(containerModel.read(), containerModel);
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
                Assert.assertOfType("[INPUT IS OF WRONG TYPE]", ContainerVersionCell.class, input);
                final ContainerVersionCell typedInput = (ContainerVersionCell) input;
                final Long containerId = typedInput.getArtifactId();
                final Long versionId = typedInput.getVersionId();
                return toDisplay(typedInput, containerModel.readDocuments(containerId, versionId));
            }
        };
        this.versionUsers = new FlatContentProvider(profile) {
            @Override
            public Object[] getElements(final Object input) {
                Assert.assertNotNull("[NULL INPUT]", input);
                Assert.assertOfType("[INPUT IS OF WRONG TYPE]", ContainerVersionSentToCell.class, input);
                final ContainerVersionSentToCell typedInput = (ContainerVersionSentToCell) input;
                final Long containerId = ((ContainerVersionCell)typedInput.getParent()).getArtifactId();
                final Long versionId = ((ContainerVersionCell)typedInput.getParent()).getVersionId();
                return toDisplay(typedInput, containerModel.readPublishedTo(containerId, versionId),
                        containerModel.readSharedWith(containerId, versionId));
            }
        };
        this.draftProvider = new SingleContentProvider(profile) {
            public Object getElement(Object input) {
                Assert.assertNotNull("[NULL INPUT]", input);
                Assert.assertOfType("[INPUT IS OF WRONG TYPE]", Long.class, input);
                return containerModel.readDraft((Long) input);
            }
        };
        this.searchResults = new FlatContentProvider(profile) {
            @Override
            public Object[] getElements(final Object input) {
                Assert.assertNotNull("NULL INPUT", input);
                Assert.assertOfType("INPUT IS OF WRONG TYPE", String.class, input);
                return containerModel.search((String) input).toArray(new Long[] {});
            }
        };
        this.flatProviders = new FlatContentProvider[] {
                containers, versions, versionDocuments, versionUsers, searchResults
        };
        this.singleProviders = new SingleContentProvider[] {containerProvider, draftProvider, documentIsDraftModifiedProvider};
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeFlatSingleContentProvider#getElement(java.lang.Integer,
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeFlatContentProvider#getElements(java.lang.Integer,
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
     * Obtain a displayable version of a container.
     * 
     * @param container
     *          The container
     * @param ctrModel
     *          The parity container interface.
     *          
     * @return The displayable container.
     */
    private ContainerCell toDisplay(final Container container,
            final ContainerModel ctrModel) {
        if (null == container) {
            return null;
        } else {
            final ContainerDraft containerDraft = ctrModel.readDraft(container.getId());
            return new ContainerCell(container, containerDraft);
        }
    }

    /**
     * Create a displayable list of container versions.
     * 
     * @param container
     *            A container.
     * @param versions
     *            A list of container versions.
     * @return A list of container version displays.
     */
    private ContainerVersionCell[] toDisplay(final ContainerCell container,
            final List<ContainerVersion> versions) {
        final List<ContainerVersionCell> display = new ArrayList<ContainerVersionCell>(versions.size());
        for(final ContainerVersion version : versions) {
            display.add(new ContainerVersionCell(container, version));
        }
        return display.toArray(new ContainerVersionCell[] {});
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
    private ContainerVersionDocumentCell toDisplay(
            final ContainerVersionCell version, final Document document) {
        final ContainerVersionDocumentCell display = new ContainerVersionDocumentCell(version, document);
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
    private ContainerVersionDocumentCell[] toDisplay(
            final ContainerVersionCell version,
            final List<Document> documents) {
        final List<ContainerVersionDocumentCell> list =
            new ArrayList<ContainerVersionDocumentCell>(documents.size());
        for(final Document document : documents) {
            list.add(toDisplay(version, document));
        }
        return list.toArray(new ContainerVersionDocumentCell[] {});
    }
    
    /**
     * Create a display user for a version.
     * 
     * @param sentToCell
     *            A "sent-to" cell (parent of sent-to users).
     * @param user
     *            A user.
     * @return A display user.
     */
    private ContainerVersionSentToUserCell toDisplay(
            final ContainerVersionSentToCell sentToCell, final User user) {
        final ContainerVersionSentToUserCell display = new ContainerVersionSentToUserCell(sentToCell, user);
        return display;
    }
    
    /**
     * Create an array of display sent-to users for a version.
     * 
     * @param sentToCell
     *            A "sent-to" cell (parent of sent-to users).
     * @param versionUsersPublishedTo
     *            A list of users.
     * @param versionUsersSharedWith
     *            A list of users.
     * @return An array of display users.
     */
    private ContainerVersionSentToUserCell[] toDisplay(
            final ContainerVersionSentToCell sentToCell,
            final List<User> versionUsersPublishedTo,
            final List<User> versionUsersSharedWith) {
        final Integer size = versionUsersPublishedTo.size() + versionUsersSharedWith.size();
        final List<ContainerVersionSentToUserCell> list =
            new ArrayList<ContainerVersionSentToUserCell>(size);
        for(final User user : versionUsersPublishedTo) {
            list.add(toDisplay(sentToCell, user));
        }
        for(final User user : versionUsersSharedWith) {
            list.add(toDisplay(sentToCell, user));
        }
        return list.toArray(new ContainerVersionSentToUserCell[] {});
    }

    /**
     * Obtain a displayable version of a list of containers.
     * 
     * @param containers
     *          The containers
     * @param ctrModel
     *          The parity container interface.
     *          
     * @return The displayable containers.
     */
    private ContainerCell[] toDisplay(final List<Container> containers,
            final ContainerModel ctrModel) {
        final List<ContainerCell> display = new ArrayList<ContainerCell>();
        for(final Container container : containers) {
            display.add(toDisplay(container, ctrModel));
        }
        return display.toArray(new ContainerCell[] {});      
    }
}
