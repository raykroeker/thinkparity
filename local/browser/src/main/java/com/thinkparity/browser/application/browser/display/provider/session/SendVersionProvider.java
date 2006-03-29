/*
 * Mar 29, 2006
 */
package com.thinkparity.browser.application.browser.display.provider.session;

import java.util.Iterator;
import java.util.List;

import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;

import com.thinkparity.codebase.Pair;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotOfTypeAssertion;
import com.thinkparity.codebase.assertion.NullPointerAssertion;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SendVersionProvider extends CompositeFlatSingleContentProvider {

	private final SingleContentProvider documentProvider;

	private final FlatContentProvider[] flatProviders;

	private final SingleContentProvider[] singleProviders;

	private final FlatContentProvider teamProvider;

	private final SingleContentProvider versionProvider;

	/**
	 * Create a SendVersionProvider.
	 */
	public SendVersionProvider(final DocumentModel dModel,
			final SessionModel sModel, final JabberId loggedInUser) {
		super();
		this.documentProvider = new SingleContentProvider() {
			public Object getElement(final Object input) {
				final Long documentId = assertValidInput(input);
				try { return dModel.get(documentId); }
				catch(final ParityException px) { throw new RuntimeException(px); }
			}
		};
		this.teamProvider = new FlatContentProvider() {
			public Object[] getElements(final Object input) {
				final Long artifactId = assertValidInput(input);
				try {
					final List<Contact> team =
						sModel.readArtifactContacts(artifactId);
					for(final Iterator<Contact> i = team.iterator(); i.hasNext();) {
						if(i.next().getId().equals(loggedInUser)) { i.remove(); }
					}
					return team.toArray(new Contact[] {});
				}
				catch(final ParityException px) { throw new RuntimeException(px); }
			}
		};
		this.versionProvider = new SingleContentProvider() {
			public Object getElement(final Object input) {
				final Long documentId = (Long) ((Pair) input).getFirst();
				final Long versionId = (Long) ((Pair) input).getSecond();
				try { return dModel.getVersion(documentId, versionId); }
				catch(final ParityException px) { throw new RuntimeException(px); }
			}
		};
		this.flatProviders = new FlatContentProvider[] {teamProvider};
		this.singleProviders = new SingleContentProvider[] {documentProvider, versionProvider};
	}

	/**
	 * @see com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider#getElement(java.lang.Integer, java.lang.Object)
	 * 
	 */
	public Object getElement(final Integer index, final Object input) {
		return singleProviders[index].getElement(input);
	}

	/**
	 * @see com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider#getElements(java.lang.Integer, java.lang.Object)
	 * 
	 */
	public Object[] getElements(Integer index, Object input) {
		return flatProviders[index].getElements(input);
	}

	/**
	 * Assert the input is valid.
	 * 
	 * @param input
	 *            The input
	 * 
	 * @return The document id if the input is valid.
	 * @throws NullPointerAssertion
	 *             <ul>
	 *             <li>If input is null
	 *             </ul>
	 * @throws NotOfTypeAssertion
	 *             <ul>
	 *             <li>If input is not of type: java.lang.Long
	 *             </ul>
	 */
	private Long assertValidInput(final Object input) {
		Assert.assertNotNull(
				"The send artifact provider requires a document id:  java.lang.Long.",
				input);
		Assert.assertOfType(
				"The send artifact provider requires a document id:  java.lang.Long.",
				Long.class, input);
		return (Long) input;
	}
}
