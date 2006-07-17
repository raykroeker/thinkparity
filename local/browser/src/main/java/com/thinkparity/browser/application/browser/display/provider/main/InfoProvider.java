/*
 * Mar 23, 2006
 */
package com.thinkparity.browser.application.browser.display.provider.main;

import com.thinkparity.browser.application.browser.display.provider.CompositeSingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;

import com.thinkparity.model.parity.model.profile.Profile;

/**
 * Provider for the info bar.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class InfoProvider extends CompositeSingleContentProvider {

	/** An array of single providers. */
	private final SingleContentProvider[] singleProviders;

	/**
     * Create a InfoProvider.
     * 
     * @param profile
     *            A thinkParity profile.
     * @param dModel
     *            The parity document  interface.
     */
	public InfoProvider(final Profile profile) {
		super(profile);
		this.singleProviders = new SingleContentProvider[] {};
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
