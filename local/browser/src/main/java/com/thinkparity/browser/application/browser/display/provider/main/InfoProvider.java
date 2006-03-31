/*
 * Mar 23, 2006
 */
package com.thinkparity.browser.application.browser.display.provider.main;

import com.thinkparity.browser.application.browser.display.provider.CompositeSingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.session.SessionModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class InfoProvider extends CompositeSingleContentProvider {

	/**
	 * A contact provider.
	 * 
	 */
	private final SingleContentProvider contactProvider;

	/**
	 * A document count provider.
	 * 
	 */
	private final SingleContentProvider countProvider;

	/**
	 * A list of the single providers.
	 * 
	 */
	private final SingleContentProvider[] singleProviders;

	/**
     * Create a InfoProvider.
     * 
     * @param dModel
     *            The parity document interface.
     * @param sModel
     *            The parity session interface.
     */
	public InfoProvider(final DocumentModel dModel, final SessionModel sModel) {
		super();
		this.contactProvider = new SingleContentProvider() {
			public Object getElement(final Object input) {
				try { return sModel.readContact(); }
				catch(final ParityException px) { throw new RuntimeException(px); }
			}
		};
		this.countProvider = new SingleContentProvider() {
			public Object getElement(final Object input) {
				// TODO Create a statistics model to obtain and build
				// reports
				try { return dModel.list().size(); }
				catch(final ParityException px) { throw new RuntimeException(px); }
			}
		};
		this.singleProviders = new SingleContentProvider[] {countProvider, contactProvider};
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
