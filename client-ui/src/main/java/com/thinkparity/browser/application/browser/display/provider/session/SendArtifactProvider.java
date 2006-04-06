/*
 * Mar 14, 2006
 */
package com.thinkparity.browser.application.browser.display.provider.session;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;
import com.thinkparity.browser.model.document.WorkingVersion;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotOfTypeAssertion;
import com.thinkparity.codebase.assertion.NullPointerAssertion;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SendArtifactProvider extends CompositeFlatSingleContentProvider {

	/**
	 * An artifact content provider.
	 * 
	 */
	private final FlatContentProvider artifactContactProvider;

	/**
	 * A document provider.
	 * 
	 */
	private final SingleContentProvider documentProvider;

	/**
	 * A document version content provider.
	 * 
	 */
	private FlatContentProvider documentVersionProvider;

	/**
	 * A list of flat providers.
	 * 
	 */
	private final FlatContentProvider[] flatProviders;

	/**
	 * A list of all of the single result content providers.
	 * 
	 */
	private final SingleContentProvider[] singleProviders;

	/**
	 * A user contact provider.
	 * 
	 */
	private final FlatContentProvider userContactProvider;

	/**
	 * Create a SendArtifactProvider.
	 * 
	 */
	public SendArtifactProvider(final DocumentModel documentModel,
			final SessionModel sessionModel, final JabberId loggedInUser) {
		super();
		this.artifactContactProvider = new FlatContentProvider() {
			public Object[] getElements(final Object input) {
				final Long documentId = assertValidInput(input);
				try {
					final Set<User> artifactContacts =
						sessionModel.readArtifactTeam(documentId);
					User teamMember;
					for(final Iterator<User> i = artifactContacts.iterator(); i.hasNext();) {
                        teamMember = i.next();
						if(teamMember.getId().equals(loggedInUser)) { i.remove(); }
					}
					return artifactContacts.toArray(new Contact[] {});
				}
				catch(final ParityException px) { throw new RuntimeException(px); }
			}
		};
		this.documentProvider = new SingleContentProvider() {
			public Object getElement(final Object input) {
				final Long documentId = assertValidInput(input);
				try { return documentModel.get(documentId); }
				catch(final ParityException px) { throw new RuntimeException(px); }
			}
		};
		this.documentVersionProvider = new FlatContentProvider() {
			public Object[] getElements(final Object input) {
				final Long documentId = assertValidInput(input);
				final List<DocumentVersion> versions = new LinkedList<DocumentVersion>();
				try {
					versions.addAll(documentModel.listVersions(documentId));
					if(sessionModel.isLoggedInUserKeyHolder(documentId)) {
						versions.add(0, WorkingVersion.getWorkingVersion());
					}
					return versions.toArray(new DocumentVersion[] {});
				}
				catch(final ParityException px) { throw new RuntimeException(px); }
			}
		};
		this.userContactProvider = new FlatContentProvider() {
			public Object[] getElements(final Object input) {
				try {
					final Set<Contact> contacts = sessionModel.readContacts();
					// remove all team members from the roster list
					final User[] team = (User[]) input;
					if(null != team) {
						for(final User teamMember : team)
                            contacts.remove(teamMember);
					}
					return contacts.toArray(new Contact[] {});
				}
				catch(final ParityException px) { throw new RuntimeException(px); }
			}
		};
		this.flatProviders = new FlatContentProvider[] {userContactProvider, artifactContactProvider, documentVersionProvider};
		this.singleProviders = new SingleContentProvider[] {documentProvider};
	}

	/**
	 * @see com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider#getElement(java.lang.Integer, java.lang.Object)
	 * 
	 */
	public Object getElement(final Integer index, final Object input) {
		return singleProviders[index].getElement(input);
	}

	/**
	 * @see com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider#getElements(java.lang.Integer,
	 *      java.lang.Object)
	 * 
	 */
	public Object[] getElements(final Integer index, final Object input) {
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
