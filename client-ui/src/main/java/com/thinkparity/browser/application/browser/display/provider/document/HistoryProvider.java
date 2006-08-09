/*
 * Created On:  Mar 10, 2006
 */
package com.thinkparity.browser.application.browser.display.provider.document;

import java.util.Iterator;
import java.util.Set;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;

import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.document.DocumentHistoryItem;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * The document history provider. Provides a single document and its history.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HistoryProvider extends CompositeFlatSingleContentProvider {

	/**
	 * A document provider.
	 * 
	 */
	private final SingleContentProvider documentProvider;

	/**
	 * A list of flat result content providers.
	 * 
	 */
	private final FlatContentProvider[] flatProviders;

	/**
	 * A history provider.
	 * 
	 */
	private final FlatContentProvider historyProvider;

    /**
	 * A list of single result content providers.
	 * 
	 */
	private final SingleContentProvider[] singleProviders;

	/**
     * An artifact team provider.
     * 
     */
    private final FlatContentProvider teamProvider;

	/**
     * Create a HistoryProvider.
     * 
     * @param profile
     *            A thinkParity profile.
     * @param aModel
     *            A thinkParity artifact interface.
     * @param dModel
     *            The parity document interface.
     * @param sModel
     *            The parity session interface.
     */
	public HistoryProvider(final Profile profile, final ArtifactModel aModel,
            final DocumentModel dModel, final SessionModel sModel) {
		super(profile);
		this.documentProvider = new SingleContentProvider(profile) {
			public Object getElement(final Object input) {
				Assert.assertNotNull(
						"The history provider requries java.lang.Long input.",
						input);
				Assert.assertOfType(
						"The history provider requries java.lang.Long input.",
						Long.class, input);
				return dModel.get((Long) input);
			}
		};
		this.historyProvider = new FlatContentProvider(profile) {
			public Object[] getElements(final Object input) {
				Assert.assertNotNull(
						"The history provider requries java.lang.Long input.",
						input);
				Assert.assertOfType(
						"The history provider requries java.lang.Long input.",
						Long.class, input);
				return dModel.readHistory((Long) input).toArray(new DocumentHistoryItem[] {});
			}
		};
        this.teamProvider = new FlatContentProvider(profile) {
            public Object[] getElements(final Object input) {
                final Long documentId = (Long) input;
                final Set<User> team =  aModel.readTeam(documentId);
                User teamMember;
                for(final Iterator<User> i = team.iterator(); i.hasNext();) {
                    teamMember = i.next();
                    if(teamMember.getId().equals(profile.getId())) { i.remove(); }
                }
                return team.toArray(new Contact[] {});
            }
        };
		this.flatProviders = new FlatContentProvider[] {historyProvider, teamProvider};
		this.singleProviders = new SingleContentProvider[] {documentProvider};
	}

	/**
	 * @see com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider#getElement(java.lang.Integer,
	 *      java.lang.Object)
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

	
	
}
