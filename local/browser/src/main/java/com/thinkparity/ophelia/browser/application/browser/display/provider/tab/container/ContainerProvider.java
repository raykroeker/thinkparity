/**
 * Created On: 13-Jul-06 11:42:46 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.tab.container;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;
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
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerVersionDocumentFolderCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerVersionSentToCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerVersionSentToUserCell;
import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.user.UserModel;

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

    /** A thinkParity container interface. */
    private final ContainerModel containerModel;

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
    
    /** A thinkParity user interface. */
    private final UserModel userModel;

    /** Reads a list of documents. */
    private final FlatContentProvider versionDocuments;

    /** Reads a list of users. */
    private final FlatContentProvider versionUsers;

    /**
     * Create ContainerProvider.
     * 
     * @param profile
     *            The local user profile.
     * @param containerModel
     *            A thinkParity container interface.
     * @param documentModel
     *            A thinkParity document interface.
     * @param userModel
     *            A thinkParity user interface.
     */
    public ContainerProvider(final Profile profile,
            final ContainerModel containerModel,
            final DocumentModel documentModel, final UserModel userModel) {
        super(profile);
        this.containerModel = containerModel;
        this.userModel = userModel;
        this.containerProvider = new SingleContentProvider(profile) {
            public Object getElement(final Object input) {
                final Long containerId = (Long) input;
                return toDisplay(containerModel.read(containerId), containerModel);
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
                Assert.assertOfType("[INPUT IS OF WRONG TYPE]", ContainerVersionDocumentFolderCell.class, input);
                final ContainerVersionDocumentFolderCell typedInput = (ContainerVersionDocumentFolderCell) input;
                final Long containerId = ((ContainerVersionCell)typedInput.getParent()).getArtifactId();
                final Long versionId = ((ContainerVersionCell)typedInput.getParent()).getVersionId();
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
                containers, null, versionDocuments, versionUsers, searchResults
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
     * Read a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>.
     */
    public User readUser(final JabberId userId) {
        return userModel.read(userId);
    }
    
    /**
     * Read a list of container versions.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return A <code>List&lt;ContainerVersion&gt;</code>.
     */
    public List<ContainerVersion> readVersions(final Long containerId) {
        return containerModel.readVersions(containerId);
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
     * Create a display document for a version.
     * 
     * @param versionDocumentFolder
     *            A "version document folder" cell (parent of version document).
     * @param document
     *            A document.
     * @return A display document.
     */
    private ContainerVersionDocumentCell toDisplay(
            final ContainerVersionDocumentFolderCell versionDocumentFolder, final Document document) {
        final ContainerVersionDocumentCell display = new ContainerVersionDocumentCell(versionDocumentFolder, document);
        return display;
    }
    
    /**
     * Create an array of display documents for a version.
     * 
     * @param versionDocumentFolder
     *            A "version document folder" cell (parent of version document).
     * @param versionDocuments
     *            A list of documents.
     * @return An array of display documents.
     */
    private ContainerVersionDocumentCell[] toDisplay(
            final ContainerVersionDocumentFolderCell versionDocumentFolder,
            final List<Document> documents) {
        final List<ContainerVersionDocumentCell> list =
            new ArrayList<ContainerVersionDocumentCell>(documents.size());
        for(final Document document : documents) {
            list.add(toDisplay(versionDocumentFolder, document));
        }
        return list.toArray(new ContainerVersionDocumentCell[] {});
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
