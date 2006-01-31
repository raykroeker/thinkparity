/*
 * Jan 30, 2006
 */
package com.thinkparity.browser.ui.action.session;

import java.util.UUID;

import javax.swing.Icon;

import com.thinkparity.browser.ui.action.AbstractAction;
import com.thinkparity.browser.ui.action.ActionId;
import com.thinkparity.browser.ui.action.Data;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class RequestKey extends AbstractAction {

	public enum DataKey { ARTIFACT_ID }

	private static final Icon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.SESSION_REQUEST_KEY;
		NAME = "Request Key";
	}

	/**
	 * Create a RequestKey.
	 * 
	 */
	public RequestKey() { super("Session.RequestKey", ID, NAME, ICON); }

	/**
	 * @see com.thinkparity.browser.ui.action.AbstractAction#invoke(com.thinkparity.browser.ui.action.Data)
	 * 
	 */
	public void invoke(Data data) throws Exception {
		final UUID artifactId = (UUID) data.get(DataKey.ARTIFACT_ID);
		getSessionModel().sendKeyRequest(artifactId);
	}
}
