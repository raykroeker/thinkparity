/*
 * Mar 29, 2006
 */
package com.thinkparity.browser.platform.action.container;

import java.util.List;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.model.index.IndexHit;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Search extends AbstractAction {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

	/** The browser application. */
	private final Browser browser;

	/** Create Search. */
	public Search(final Browser browser) {
		super(ActionId.CONTAINER_SEARCH);
		this.browser = browser;
	}

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) {
		final String expression = (String) data.get(DataKey.EXPRESSION);

        final List<IndexHit> searchResult = getIndexModel().searchContainers(expression);
        browser.removeSearchFilter();
        browser.applySearchFilter(searchResult);
	}

	public enum DataKey { EXPRESSION }
}
