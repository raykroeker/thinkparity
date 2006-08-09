/*
 * Mar 29, 2006
 */
package com.thinkparity.browser.application.browser.display.provider.session;

import java.util.Iterator;
import java.util.Set;

import com.thinkparity.codebase.Pair;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotOfTypeAssertion;
import com.thinkparity.codebase.assertion.NullPointerAssertion;

import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;

import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

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
     * Create SendVersionProvider.
     * 
     * @param profile
     *            A thinkParity profile.
     * @param aModel
     *            A thinkParity artifact interface.
     * @param dModel
     *            A thinkParity document interface.
     * @param sModel
     *            A thinkParity session interface.
     * @param loggeInUser
     *            The session user.
     */
	public SendVersionProvider(final Profile profile,
            final ArtifactModel aModel, final DocumentModel dModel,
            final SessionModel sModel) {
		super(profile);
		this.documentProvider = new SingleContentProvider(profile) {
			public Object getElement(final Object input) {
				final Long documentId = assertValidInput(input);
				return dModel.get(documentId);
			}
		};
		this.teamProvider = new FlatContentProvider(profile) {
			public Object[] getElements(final Object input) {
				final Long artifactId = assertValidInput(input);
				final Set<User> team = aModel.readTeam(artifactId);
				for(final Iterator<User> i = team.iterator(); i.hasNext();) {
					if(i.next().getId().equals(profile.getId())) { i.remove(); }
				}
				return team.toArray(new Contact[] {});
			}
		};
		this.versionProvider = new SingleContentProvider(profile) {
			public Object getElement(final Object input) {
				final Long documentId = (Long) ((Pair) input).getFirst();
				final Long versionId = (Long) ((Pair) input).getSecond();
				return dModel.getVersion(documentId, versionId);
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
