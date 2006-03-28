/*
 * Mar 22, 2006
 */
package com.thinkparity.browser.application.browser.display.provider.main;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.browser.application.browser.display.avatar.main.DisplayDocument;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.message.system.SystemMessage;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.sort.AbstractArtifactComparator;
import com.thinkparity.model.parity.model.sort.RemoteUpdatedOnComparator;
import com.thinkparity.model.parity.model.sort.UpdatedOnComparator;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MainProvider extends CompositeFlatSingleContentProvider {

	private final SingleContentProvider documentProvider;

	private final FlatContentProvider documentsProvider;

	private final FlatContentProvider[] flatProviders;

	private final SingleContentProvider[] singleProviders;

	private final FlatContentProvider systemMessagesProvider;

	/**
	 * Create a MainProvider.
	 * 
	 * @param artifactModel
	 *            The parity artifact interface.
	 * @param documentModel
	 *            The parity document interface.
	 * @param systemMessageModel
	 *            The parity system message interface.
	 */
	public MainProvider(final ArtifactModel artifactModel,
			final DocumentModel documentModel,
			final SystemMessageModel systemMessageModel) {
		super();
		this.documentProvider = new SingleContentProvider() {
			public Object getElement(final Object input) {
				final Long documentId = assertNotNullLong(
						"The main provider's document provider requires " +
						"non-null java.lang.Long input.", input);
				try {
					final Document document = documentModel.get(documentId);
					return toDisplay(document, artifactModel);
				}
				catch(final ParityException px) { throw new RuntimeException(px); }
			}
		};
		this.documentsProvider = new FlatContentProvider() {
			public Object[] getElements(final Object input) {
				try {
					// sort by:
					// 	+> hasBeenSeen ? true b4 false
					//	+> last update ? earlier b4 later
					//  +> name ? alpha order
					final AbstractArtifactComparator sort =
						new RemoteUpdatedOnComparator(Boolean.FALSE);
					sort.add(new UpdatedOnComparator(Boolean.FALSE));
					final Collection<Document> documents = documentModel.list(sort);
					return toDisplay(documents, artifactModel);
				}
				catch(final ParityException px) { throw new RuntimeException(px); }
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
		this.flatProviders = new FlatContentProvider[] {documentsProvider, systemMessagesProvider};
		this.singleProviders = new SingleContentProvider[] {documentProvider};
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
	 * Convert a list of documents and the system messages for that document
	 * into a display document.
	 * 
	 * @param documents
	 *            The documents.
	 * @param systemMessageModel
	 *            The parity system message interface.
	 * @return The display documents.
	 */
	private DisplayDocument[] toDisplay(final Collection<Document> documents,
			final ArtifactModel artifactModel) throws ParityException {
		final List<DisplayDocument> display = new LinkedList<DisplayDocument>();

		DisplayDocument dd;
		for(final Document d : documents) {
			dd = new DisplayDocument();

			dd.setDocument(d);
			dd.setKeyRequests(artifactModel.readKeyRequests(d.getId()));

			display.add(dd);
		}
		return display.toArray(new DisplayDocument[] {});
	}

	private DisplayDocument toDisplay(final Document document,
			final ArtifactModel artifactModel) throws ParityException {
		if(null == document) { return null; }
		else {
			final DisplayDocument dd = new DisplayDocument();
			
			dd.setDocument(document);
			dd.setKeyRequests(artifactModel.readKeyRequests(document.getId()));

			return dd;
		}
	}
}
