/*
 * Mar 23, 2006
 */
package com.thinkparity.browser.application.browser.display.provider.main;

import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.DocumentModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class InfoProvider extends SingleContentProvider {

	/**
	 * A document count provider.
	 * 
	 */
	private final SingleContentProvider countProvider;

	/**
	 * Create a InfoProvider.
	 * 
	 * @param dModel
	 *            The parity document interface.
	 */
	public InfoProvider(final DocumentModel dModel) {
		super();
		this.countProvider = new SingleContentProvider() {
			public Object getElement(final Object input) {
				// TODO Create a statistics model to obtain and build
				// reports
				try { return dModel.list().size(); }
				catch(final ParityException px) { throw new RuntimeException(px); }
			}
		};
	}

	/**
	 * @see com.thinkparity.browser.application.browser.display.provider.SingleContentProvider#getElement(java.lang.Object)
	 * 
	 */
	public Object getElement(final Object input) {
		return countProvider.getElement(input);
	}
}
