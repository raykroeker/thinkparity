/*
 * Mar 23, 2006
 */
package com.thinkparity.browser.application.browser.display.provider.main;

import com.thinkparity.browser.application.browser.display.provider.CompositeSingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.xmpp.user.User;

/**
 * Provider for the info bar.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class InfoProvider extends CompositeSingleContentProvider {

	/** A user provider. */
	private final SingleContentProvider userProvider;

	/** A document count provider. */
	private final SingleContentProvider countProvider;

	/** An array of single providers. */
	private final SingleContentProvider[] singleProviders;

	/**
     * Create a InfoProvider.
     * 
     * @param localUser
     *            The local user.
     * @param dModel
     *            The parity document  interface.
     */
	public InfoProvider(final User localUser, final DocumentModel dModel) {
		super();
		this.userProvider = new SingleContentProvider() {
			public Object getElement(final Object input) { return localUser; }
		};
		this.countProvider = new SingleContentProvider() {
			public Object getElement(final Object input) {
				// TODO Create a statistics model to obtain and build
				// reports
				try { return dModel.list().size(); }
				catch(final ParityException px) { throw new RuntimeException(px); }
			}
		};
		this.singleProviders = new SingleContentProvider[] {countProvider, userProvider};
	}

	/**
     * @see com.thinkparity.browser.application.browser.display.provider.CompositeSingleContentProvider#getElement(java.lang.Integer,
     *      java.lang.Object)
     * 
     */
	public Object getElement(final Integer index, final Object input) {
		return singleProviders[index].getElement(input);
	}
}
