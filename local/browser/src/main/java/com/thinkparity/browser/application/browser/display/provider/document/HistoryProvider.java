/*
 * Mar 10, 2006
 */
package com.thinkparity.browser.application.browser.display.provider.document;

import java.util.Iterator;
import java.util.Set;

import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.document.history.HistoryItem;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.xmpp.JabberId;
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
     * An artifact team provider.
     * 
     */
    private final FlatContentProvider teamProvider;

	/**
	 * A list of single result content providers.
	 * 
	 */
	private final SingleContentProvider[] singleProviders;

	/**
     * Create a HistoryProvider.
     * 
     * @param loggedInUserId
     *            The logged in user's jabber id.
     * @param dModel
     *            The parity document interface.
     * @param sModel
     *            The parity session interface.
     */
	public HistoryProvider(final JabberId loggedInUserId, final DocumentModel dModel,
            final SessionModel sModel) {
		super();
		this.documentProvider = new SingleContentProvider() {
			public Object getElement(final Object input) {
				Assert.assertNotNull(
						"The history provider requries java.lang.Long input.",
						input);
				Assert.assertOfType(
						"The history provider requries java.lang.Long input.",
						Long.class, input);
				try { return dModel.get((Long) input); }
				catch(final ParityException px) { throw new RuntimeException(px); }
			}
		};
		this.historyProvider = new FlatContentProvider() {
			public Object[] getElements(final Object input) {
				Assert.assertNotNull(
						"The history provider requries java.lang.Long input.",
						input);
				Assert.assertOfType(
						"The history provider requries java.lang.Long input.",
						Long.class, input);
				try { return dModel.readHistory((Long) input).toArray(new HistoryItem[] {}); }
				catch(final ParityException px) { throw new RuntimeException(px); }
			}
		};
        this.teamProvider = new FlatContentProvider() {
            public Object[] getElements(final Object input) {
                final Long documentId = (Long) input;
                final Set<User> team;
                try { team =  sModel.readArtifactTeam(documentId); }
                catch(final ParityException px) { throw new RuntimeException(px); }
                User teamMember;
                for(final Iterator<User> i = team.iterator(); i.hasNext();) {
                    teamMember = i.next();
                    if(teamMember.getId().equals(loggedInUserId)) { i.remove(); }
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
