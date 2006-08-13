/*
 * Created On: Thu Jun 01 2006 16:37 PDT
 * $Id$
 */
package com.thinkparity.browser.platform.action.document;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

/**
 * Rename is run when the user rename's an artifact.
 *
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class Revert extends AbstractAction {

	/**
	 * Create Rename.
	 * 
     * @param browser
     *      The browser application.
	 */
	public Revert(final Browser browser) {
		super(ActionId.DOCUMENT_REVERT);
	}

	/** @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data) */
	public void invoke(final Data data) {
		throw Assert.createNotYetImplemented("Revert#invoke");
	}

	public enum DataKey { DOCUMENT_ID }
}
