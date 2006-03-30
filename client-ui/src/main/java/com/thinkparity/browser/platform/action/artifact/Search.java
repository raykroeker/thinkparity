/*
 * Mar 29, 2006
 */
package com.thinkparity.browser.platform.action.artifact;

import java.util.List;

import javax.swing.ImageIcon;

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

	private static final ImageIcon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.ARTIFACT_SEARCH;
		NAME = "Search Artifact";
	}

	/**
	 * The browser application.
	 * 
	 */
	private final Browser browser;

	/**
	 * Create a Search.
	 * 
	 */
	public Search(final Browser browser) {
		super("", ID, NAME, ICON);
		this.browser = browser;
	}

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) throws Exception {
		final String criteria = (String) data.get(DataKey.CRITERIA);

		if(null == criteria || 1 > criteria.length()) {
			browser.removeSearchFilter();
		}
		else {
			final List<IndexHit> searchResult = getIndexModel().search(criteria);
			browser.applySearchFilter(searchResult);
		}
	}

	public enum DataKey { CRITERIA }
}
