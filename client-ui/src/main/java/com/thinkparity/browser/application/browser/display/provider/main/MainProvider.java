/*
 * Mar 22, 2006
 */
package com.thinkparity.browser.application.browser.display.provider.main;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.thinkparity.browser.application.browser.display.avatar.main.MainCellDocument;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellHistoryItem;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.document.history.HistoryItem;
import com.thinkparity.model.parity.model.message.system.SystemMessage;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.session.SessionModel;
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

	private final SingleContentProvider[] singleProviders;

	private final SingleContentProvider systemMessageProvider;

    private final FlatContentProvider historyProvider;

	private final FlatContentProvider systemMessagesProvider;

	/**
     * Create a MainProvider.
     * 
     * @param aModel
     *            The parity artifact interface.
     * @param dModel
     *            The parity document interface.
     * @param sModel
     *            The parity session interface.
     * @param systemMessageModel
     *            The parity system message interface.
     */
	public MainProvider(final ArtifactModel artifactModel,
            final DocumentModel dModel, final SessionModel sModel,
            final SystemMessageModel systemMessageModel,
            final JabberId loggedInUserId) {
		super();
		this.documentProvider = new SingleContentProvider() {
			public Object getElement(final Object input) {
				final Long documentId = (Long) input;
				try {
					final Document document = dModel.get(documentId);
                    return toDisplay(document, artifactModel, dModel);
				}
				catch(final ParityException px) { throw new RuntimeException(px); }
			}
		};
		this.documentsProvider = new FlatContentProvider() {
			public Object[] getElements(final Object input) {
				try {
					// sort by:
					// 	+> remote update ? later b4 earlier
					//	+> last update ? later b4 earlier
					final AbstractArtifactComparator sort =
						new RemoteUpdatedOnComparator(Boolean.FALSE);
					sort.add(new UpdatedOnComparator(Boolean.FALSE));

					return toDisplay(dModel.list(sort), artifactModel, dModel);
				}
				catch(final ParityException px) { throw new RuntimeException(px); }
			}

		};
		this.historyProvider = new FlatContentProvider() {
            public Object[] getElements(final Object input) {
                final MainCellDocument mcd = (MainCellDocument) input;
                try { return toDisplay(artifactModel, loggedInUserId, mcd, dModel.readHistory(mcd.getId())); }
                catch(final ParityException px) { throw new RuntimeException(px); }
            }
        };
        this.systemMessageProvider = new SingleContentProvider() {
			public Object getElement(final Object input) {
				final Long systemMessageId = assertNotNullLong(
						"The main provider's system message provider " +
						"requires non-null java.lang.Long input.", input);
				final SystemMessage systemMessage;
				try { systemMessage = systemMessageModel.read(systemMessageId); }
				catch(final ParityException px) { throw new RuntimeException(px); }
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
						systemMessageModel.readForNonArtifacts();
					return messages.toArray(new SystemMessage[] {});
				}
				catch(final ParityException px) { throw new RuntimeException(px); }
			}
		};
		this.flatProviders = new FlatContentProvider[] {documentsProvider, historyProvider, systemMessagesProvider};
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
            final ArtifactModel aModel, final DocumentModel dModel)
            throws ParityException {
		final List<MainCellDocument> display = new LinkedList<MainCellDocument>();

		for(final Document d : documents) {
			display.add(toDisplay(d, aModel, dModel));
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
            final ArtifactModel aModel, final DocumentModel dModel)
            throws ParityException {
		if(null == document) { return null; }
		else {
			final MainCellDocument mcd = new MainCellDocument(document, dModel);
            mcd.setKeyRequests(aModel.readKeyRequests(document.getId()));
			return mcd;
		}
	}

    /**
     * Convert a document and its history into a list of displayable history
     * items.
     * 
     * @param sModel
     *            The session model.
     * @param document
     *            A document.
     * @param history
     *            A document's history.
     * @return A displayable history.
     */
	private MainCellHistoryItem[] toDisplay(final ArtifactModel aModel,
            final JabberId loggedInUserId,
            final MainCellDocument document,
            final Collection<HistoryItem> history) {
	    final List<MainCellHistoryItem> display = new LinkedList<MainCellHistoryItem>();
        final Integer count = history.size();
        Integer index = 0;
        Set<User> dTeam;
        for(final HistoryItem hi : history) {
            dTeam = aModel.readTeam(document.getId());
            for(final Iterator<User> i = dTeam.iterator(); i.hasNext();) {
                if(i.next().getId().equals(loggedInUserId)) { i.remove(); }
            }
            display.add(new MainCellHistoryItem(
                    document, hi, dTeam, count, index++));
        }
        return display.toArray(new MainCellHistoryItem[] {});
    }
}
