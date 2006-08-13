/*
 * Feb 28, 2006
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
public class DeclineInvitation extends AbstractAction {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** Create DeclineInvitation. */
	public DeclineInvitation(final Browser browser) {
		super(ActionId.CONTACT_DECLINE_INVITATION);
	}

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) {
	    throw Assert.createNotYetImplemented("DeclineInvitation#invoke");
	}

	public enum DataKey { SYSTEM_MESSAGE_ID }
}
