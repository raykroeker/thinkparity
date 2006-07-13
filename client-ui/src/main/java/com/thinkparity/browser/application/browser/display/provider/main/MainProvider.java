/*
 * Created On: Mar 22, 2006
 */
package com.thinkparity.browser.application.browser.display.provider.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.application.browser.display.avatar.main.MainCellDocument;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellHistoryItem;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellUser;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.contact.QuickShareProvider;
import com.thinkparity.browser.application.browser.display.provider.contact.ShareProvider;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.contact.ContactModel;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentHistoryItem;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.message.system.SystemMessage;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.sort.AbstractArtifactComparator;
import com.thinkparity.model.parity.model.sort.RemoteUpdatedOnComparator;
import com.thinkparity.model.parity.model.sort.UpdatedOnComparator;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MainProvider extends CompositeFlatSingleContentProvider {

	private final SingleContentProvider documentProvider;

	private final FlatContentProvider documentsProvider;

	private final FlatContentProvider[] flatProviders;

	private final FlatContentProvider historyProvider;

	/** Provides a list of contact that can quickly be shared with. */
    private final FlatContentProvider quickShareContactProvider;

    /** Provides a list of contact that can be shared with. */
    private final FlatContentProvider shareContactProvider;

	private final SingleContentProvider[] singleProviders;

    private final SingleContentProvider systemMessageProvider;

    private final FlatContentProvider systemMessagesProvider;

    /** Provides a list of document team members. */
    private final FlatContentProvider teamProvider;

    /**
     * Create a MainProvider.
     * 
     * @param aModel
     *            A thinkParity artifact interface.
     * @param cModel
     *            A thinkParity contact interface.
     * @param dModel
     *            A thinkParity document interface.
     * @param mModel
     *            A thinkParity system message interface.
     * @param loggedInUser
     *            The thinkParity session user.
     */
	public MainProvider(final ArtifactModel aModel, final ContactModel cModel,
            final DocumentModel dModel, final SystemMessageModel mModel,
            final JabberId loggedInUserId) {
		super();
		this.documentProvider = new SingleContentProvider() {
			public Object getElement(final Object input) {
				final Long documentId = (Long) input;
				try {
					final Document document = dModel.get(documentId);
                    return toDisplay(document, aModel, dModel, loggedInUserId);
				}
				catch(final ParityException px) { throw new RuntimeException(px); }
			}
		};
		this.documentsProvider = new FlatContentProvider() {
			public Object[] getElements(final Object input) {
				// sort by:
				// 	+> remote update ? later b4 earlier
				//	+> last update ? later b4 earlier
				final AbstractArtifactComparator sort =
					new RemoteUpdatedOnComparator(Boolean.FALSE);
				sort.add(new UpdatedOnComparator(Boolean.FALSE));
				return new MainCellDocument[] {};
			}

		};
		this.historyProvider = new FlatContentProvider() {
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
        };
		this.flatProviders = new FlatContentProvider[] {documentsProvider, historyProvider, systemMessagesProvider, quickShareContactProvider, shareContactProvider, teamProvider};
		this.singleProviders = new SingleContentProvider[] {documentProvider, systemMessageProvider};
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

	private Long assertNotNullLong(final String assertion, final Object input) {
		Assert.assertNotNull(assertion, input);
		Assert.assertOfType(assertion, Long.class, input);
		return (Long) input;
	}

    private Set<User> readTeam(final ArtifactModel aModel,
            final Long documentId, final JabberId localUserId) {
        final Set<User> team = aModel.readTeam(documentId);
        logger.debug("[] [TEAM SIZE (" + team.size() + "]");
        for(final Iterator<User> i = team.iterator(); i.hasNext();) {
            if(i.next().getId().equals(localUserId)) { i.remove(); }
        }
        return team;
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
	private MainCellHistoryItem[] toDisplay(final ArtifactModel aModel,
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
    }

    /**
     * Obtain a displayable version of a list of documents.
     * 
     * @param documents
     *            The documents.
     * @param aModel
     *            The parity artifact interface.
     * @return The displayable documents.
     */
	private MainCellDocument[] toDisplay(final Collection<Document> documents,
            final ArtifactModel aModel, final DocumentModel dModel,
            final JabberId localUserId) throws ParityException {
		final List<MainCellDocument> display = new LinkedList<MainCellDocument>();

		for(final Document d : documents) {
			display.add(toDisplay(d, aModel, dModel, localUserId));
		}
		return display.toArray(new MainCellDocument[] {});
	}

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
	private MainCellDocument toDisplay(final Document document,
            final ArtifactModel aModel, final DocumentModel dModel, final JabberId localUserId)
            throws ParityException {
		if(null == document) { return null; }
		else {
			final MainCellDocument mcd = new MainCellDocument(
                    dModel, document, readTeam(aModel, document.getId(), localUserId));
            mcd.setKeyRequests(aModel.readKeyRequests(document.getId()));
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
    }
}
