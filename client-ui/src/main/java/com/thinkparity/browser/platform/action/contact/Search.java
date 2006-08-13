/*
 * Mar 29, 2006
 */
package com.thinkparity.browser.platform.action.contact;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Search extends AbstractAction {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

	/** Create Search. */
	public Search(final Browser browser) {
		super(ActionId.CONTACT_SEARCH);
	}

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) {
        throw Assert.createNotYetImplemented("Search#invoke");
	}

	public enum DataKey { EXPRESSION }
}
