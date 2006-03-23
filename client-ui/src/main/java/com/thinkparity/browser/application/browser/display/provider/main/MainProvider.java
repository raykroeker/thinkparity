/*
 * Mar 22, 2006
 */
package com.thinkparity.browser.application.browser.display.provider.main;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.browser.application.browser.display.avatar.main.DisplayDocument;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatContentProvider;
import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.message.system.SystemMessage;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.sort.AbstractArtifactComparator;
import com.thinkparity.model.parity.model.sort.ComparatorBuilder;
import com.thinkparity.model.parity.model.sort.HasBeenSeenComparator;
import com.thinkparity.model.parity.model.sort.UpdatedOnComparator;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MainProvider extends CompositeFlatContentProvider {

	private final FlatContentProvider documentsProvider;

	private final FlatContentProvider[] flatContentProviders;

	private final FlatContentProvider systemMessagesProvider;

	/**
	 * Create a MainProvider.
	 */
	public MainProvider(final ArtifactModel artifactModel,
			final DocumentModel documentModel,
			final SystemMessageModel systemMessageModel) {
		super();
		this.documentsProvider = new FlatContentProvider() {
			public Object[] getElements(final Object input) {
				try {
					// sort by:
					// 	+> hasBeenSeen ? true b4 false
					//	+> last update ? earlier b4 later
					//  +> name ? alpha order
					final AbstractArtifactComparator sort =
						new HasBeenSeenComparator(Boolean.FALSE);
					sort.add(new UpdatedOnComparator(Boolean.FALSE));
					sort.add(new ComparatorBuilder().createByName(Boolean.TRUE));
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
		this.flatContentProviders = new FlatContentProvider[] {documentsProvider, systemMessagesProvider};
	}

	/**
	 * @see com.thinkparity.browser.application.browser.display.provider.CompositeFlatContentProvider#getElements(java.lang.Integer,
	 *      java.lang.Object)
	 * 
	 */
	public Object[] getElements(final Integer index, final Object input) {
		Assert.assertNotNull("Index cannot be null.", index);
		Assert.assertTrue(
				"Index must lie within [0," + (flatContentProviders.length - 1) + "]",
				index >= 0 && index < flatContentProviders.length);
		return flatContentProviders[index].getElements(input);
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
}
