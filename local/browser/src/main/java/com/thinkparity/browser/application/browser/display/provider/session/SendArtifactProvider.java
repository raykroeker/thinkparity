/*
 * Created On: Mar 14, 2006
 */
package com.thinkparity.browser.application.browser.display.provider.session;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotOfTypeAssertion;
import com.thinkparity.codebase.assertion.NullPointerAssertion;

import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.contact.ShareProvider;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.contact.ContactModel;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.profile.Profile;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SendArtifactProvider extends CompositeFlatSingleContentProvider {

	/**
	 * A document provider.
	 * 
	 */
	private final SingleContentProvider documentProvider;

	/**
	 * A list of flat providers.
	 * 
	 */
	private final FlatContentProvider[] flatProviders;

	/** A share contact provider. */
    private final FlatContentProvider shareContactProvider;

    /**
	 * A list of all of the single result content providers.
	 * 
	 */
	private final SingleContentProvider[] singleProviders;

	/**
     * Create a SendArtifactProvider.
     * 
     * @param profile
     *            A thinkParity profile.
     * @param aModel
     *            A thinkParity artifact interface.
     * @param cModel
     *            A thinkParity contact interface.
     * @param dModel
     *            A thinkParity document interface.
     */
	public SendArtifactProvider(final Profile profile, final ArtifactModel aModel,
            final ContactModel cModel, final DocumentModel dModel) {
		super(profile);
		this.shareContactProvider = new ShareProvider(profile, aModel, cModel);
		this.documentProvider = new SingleContentProvider(profile) {
			public Object getElement(final Object input) {
				final Long documentId = assertValidInput(input);
				try { return dModel.get(documentId); }
				catch(final ParityException px) { throw new RuntimeException(px); }
			}
		};
		this.flatProviders = new FlatContentProvider[] {shareContactProvider};
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
