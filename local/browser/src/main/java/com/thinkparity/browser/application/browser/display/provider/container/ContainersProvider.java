/**
 * Created On: 13-Jul-06 11:42:46 AM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.provider.container;

import java.util.LinkedList;
import java.util.List;

import com.thinkparity.browser.application.browser.display.avatar.container.CellContainer;
import com.thinkparity.browser.application.browser.display.avatar.container.CellDocument;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.contact.ContactModel;
import com.thinkparity.model.parity.model.container.Container;
import com.thinkparity.model.parity.model.container.ContainerModel;
import com.thinkparity.model.parity.model.container.ContainerVersion;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.profile.Profile;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContainersProvider extends CompositeFlatSingleContentProvider {
    
    private final SingleContentProvider containerProvider;
    
    private final FlatContentProvider containersProvider;

    //private final SingleContentProvider documentProvider;

    private final FlatContentProvider documentsProvider;

    //private final FlatContentProvider historyProvider;

    /** Provides a list of contact that can quickly be shared with. */
    //private final FlatContentProvider quickShareContactProvider;

    /** Provides a list of contact that can be shared with. */
    //private final FlatContentProvider shareContactProvider;

    //private final SingleContentProvider systemMessageProvider;

    //private final FlatContentProvider systemMessagesProvider;

    /** Provides a list of document team members. */
    //private final FlatContentProvider teamProvider;

    /** The set of providers. */
    private final FlatContentProvider[] flatProviders;   
    private final SingleContentProvider[] singleProviders;
    
    /**
     * Create a ContainersProvider.
     * 
     * @param profile
     *            A thinkParity profile.
     * @param aModel
     *            A thinkParity artifact interface.
     * @param ctrModel
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
            final ContainerModel ctrModel, final DocumentModel dModel,
            final ContactModel cModel, final SystemMessageModel mModel) {
        super(profile);
        this.containerProvider = new SingleContentProvider(profile) {
            public Object getElement(final Object input) {
                final Long containerId = (Long) input;
                final Container container = ctrModel.read(containerId);
                return toDisplay(container, ctrModel, dModel);
            }
        };
        this.containersProvider = new FlatContentProvider(profile) {
            public Object[] getElements(final Object input) {
                return toDisplay(ctrModel.read(), ctrModel, dModel);
            }            
        };
        /*
         this.documentProvider = new SingleContentProvider(profile) {
            public Object getElement(final Object input) {
                final Long documentId = (Long) input;
                try {
                    final Document document = dModel.get(documentId);
                    return toDisplay(profile, document, ctrModel, aModel, dModel);
                }
                catch(final ParityException px) { throw new RuntimeException(px); }
            }
        };
        */
        this.documentsProvider = new FlatContentProvider(profile) {
            public Object[] getElements(final Object input) {
                final CellContainer c = (CellContainer) input;
                try {
                    final ContainerVersion containerVersion = ctrModel.readLatestVersion(c.getId());
                    return toDisplay(c, dModel, ctrModel.readDocuments(c.getId(),containerVersion.getVersionId()));
                }
                catch(final ParityException px) { throw new RuntimeException(px); }
            }

        };
/*        this.historyProvider = new FlatContentProvider() {
            public Object[] getElements(final Object input) {
                final MainCellDocument mcd = (MainCellDocument) input;
                return toDisplay(aModel, loggedInUserId, mcd, dModel.readHistory(mcd.getId()));
            }
        };
        this.quickShareContactProvider = new QuickShareProvider(aModel, cModel);
        this.systemMessageProvider = new SingleContentProvider() {
            public Object getElement(final Object input) {
                final Long systemMessageId = assertNotNullLong(
                        "The main provider's system message provider " +
                        "requires non-null java.lang.Long input.", input);
                final SystemMessage systemMessage = mModel.read(systemMessageId);
                switch(systemMessage.getType()) {
                case INFO:
                case CONTACT_INVITATION:
                case CONTACT_INVITATION_RESPONSE:
                    return systemMessage;
                case KEY_REQUEST:
                case KEY_RESPONSE:
                    return null;
                default:
                    throw Assert.createUnreachable("Unknown message type:  " + systemMessage.getType());
                }
            }
        };
        this.systemMessagesProvider = new FlatContentProvider() {
            public Object[] getElements(final Object input) {
                try {
                    final Collection<SystemMessage> messages =
                            mModel.readForNonArtifacts();
                    return messages.toArray(new SystemMessage[] {});
                }
                catch(final ParityException px) { throw new RuntimeException(px); }
            }
        };
        this.shareContactProvider = new ShareProvider(aModel, cModel);
        this.teamProvider = new FlatContentProvider() {
            public Object[] getElements(final Object input) {
                final MainCellDocument mcd = (MainCellDocument) input;
                return toDisplay(mcd, readTeam(aModel, mcd.getId(), loggedInUserId));
            }
        };*/
        this.flatProviders = new FlatContentProvider[] {containersProvider, documentsProvider};  //historyProvider, systemMessagesProvider, quickShareContactProvider, shareContactProvider, teamProvider, containersProvider};
        this.singleProviders = new SingleContentProvider[] {containerProvider}; //documentProvider, systemMessageProvider};
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

/*    private Long assertNotNullLong(final String assertion, final Object input) {
        Assert.assertNotNull(assertion, input);
        Assert.assertOfType(assertion, Long.class, input);
        return (Long) input;
    }*/

/*    private Set<User> readTeam(final ArtifactModel aModel,
            final Long documentId, final JabberId localUserId) {
        final Set<User> team = aModel.readTeam(documentId);
        logger.debug("[] [TEAM SIZE (" + team.size() + "]");
        for(final Iterator<User> i = team.iterator(); i.hasNext();) {
            if(i.next().getId().equals(localUserId)) { i.remove(); }
        }
        return team;
    }
*/
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
        final List<CellContainer> display = new LinkedList<CellContainer>();

        for(final Container c : containers) {
            display.add(toDisplay(c, ctrModel, dModel));
        }
        return display.toArray(new CellContainer[] {});      
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
// TO DO need team passed to CellContainer? KeyRequests?
    private CellContainer toDisplay(final Container container,
            final ContainerModel ctrModel, final DocumentModel dModel) {
        if(null == container) {
            return null;
        }
        else {
            final CellContainer cc = new CellContainer(ctrModel, dModel, container);
            try {
                final ContainerVersion containerVersion = ctrModel.readLatestVersion(cc.getId());
                cc.setDocuments(ctrModel.readDocuments(cc.getId(),containerVersion.getVersionId()));
            }
            catch(final ParityException px) { throw new RuntimeException(px); }
//          cc.setKeyRequests(ctrModel.readKeyRequests(container.getId()));
            return cc;
        }   
    }  
    
    /**
     * Obtain a display version of a list of documents.
     * 
     * @param cellContainer
     *          The parent cell container.
     * @param dModel
     *          The parity document interface.
     * @param documents
     *          The documents
     *          
     * @return The displayable documents.
     */
    private CellDocument[] toDisplay(final CellContainer cellContainer,
            final DocumentModel dModel, final List<Document> documents) {
        final List<CellDocument> display = new LinkedList<CellDocument>();

        for(final Document d : documents) {
            display.add(toDisplay(cellContainer, dModel, d));
        }
        return display.toArray(new CellDocument[] {});      
    }
    
    /**
     * Obtain a display version of a document.
     * 
     * @param cellContainer
     *          The parent cell container.
     * @param dModel
     *          The parity document interface.
     * @param document
     *          The document
     *          
     * @return The displayable document.
     */
    private CellDocument toDisplay(final CellContainer cellContainer,
            final DocumentModel dModel, final Document document) {
        if(null == document) {
            return null;
        }
        else {
            final CellDocument cd = new CellDocument(cellContainer, dModel, document);
            return cd;
        }   
    }      
    
    /**
     * Convert a document and its history into a list of displayable history
     * items.
     * 
     * @param aModel
     *            A thinkParity artifact interface.
     * @param localUserId
     *            The thinkParity session user.
     * @param document
     *            A document main cell.
     * @param history
     *            A document history.
     * @return A displayable history.
     */
/*    private MainCellHistoryItem[] toDisplay(final ArtifactModel aModel,
            final JabberId localUserId,
            final MainCellDocument document,
            final List<DocumentHistoryItem> history) {
        final List<MainCellHistoryItem> display = new LinkedList<MainCellHistoryItem>();
        final Integer count = history.size();
        Integer index = 0;
        for(final DocumentHistoryItem item : history) {
            display.add(new MainCellHistoryItem(
                    document, item, readTeam(aModel, document.getId(), localUserId), count, index++));
        }
        return display.toArray(new MainCellHistoryItem[] {});
    }*/

    /**
     * Obtain a displayable version of a list of documents.
     * 
     * @param documents
     *            The documents.
     * @param aModel
     *            The parity artifact interface.
     * @return The displayable documents.
     */
/*    private MainCellDocument[] toDisplay(final List<Document> documents,
            final ContainerModel ctrModel, final ArtifactModel aModel,
            final DocumentModel dModel, final JabberId localUserId)
            throws ParityException {
        final List<MainCellDocument> display = new LinkedList<MainCellDocument>();

        for(final Document d : documents) {
            display.add(toDisplay(d, ctrModel, aModel, dModel, localUserId));
        }
        return display.toArray(new MainCellDocument[] {});
    }*/

    /**
     * Obtain the displable version of the document.
     * 
     * @param document
     *            The document.
     * @param aModel
     *            The parity artifact interface.
     * @return The displayable version of the document.
     * @throws ParityException
     */
/*    private MainCellDocument toDisplay(final Document document,
            final ContainerModel ctrModel, final ArtifactModel aModel,
            final DocumentModel dModel, final JabberId localUserId)
            throws ParityException {
        if(null == document) { return null; }
        else {
            final MainCellDocument mcd = new MainCellDocument(
                    dModel, document, readTeam(aModel, document.getId(), localUserId));
            mcd.setKeyRequests(ctrModel.readKeyRequests(document.getId()));
            return mcd;
        }
    }

    private MainCellUser[] toDisplay(final MainCellDocument mcd, final Set<User> users) {
        final List<MainCellUser> display = new ArrayList<MainCellUser>();
        final Integer count = users.size();
        Integer index = 0;
        for(final User user : users) {
            display.add(new MainCellUser(mcd, count, index++, user));
        }
        return display.toArray(new MainCellUser[] {});
    }*/
}
