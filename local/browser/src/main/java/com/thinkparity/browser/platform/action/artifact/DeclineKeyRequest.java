/*
 * Feb 28, 2006
 */
package com.thinkparity.browser.platform.action.artifact;

import javax.swing.ImageIcon;

import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.ParityException;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DeclineKeyRequest extends AbstractAction {

	private static final ImageIcon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.ARTIFACT_DECLINE_KEY_REQUEST;
		NAME = "Decline key request";
	}

	/**
	 * Create a AcceptKeyRequest.
	 * 
	 */
	public DeclineKeyRequest() { super("DeclineKeyRequest", ID, NAME, ICON); }

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) throws Exception {
		final Long keyRequestId = (Long) data.get(DataKey.KEY_REQUEST_ID);
		try { getArtifactModel().declineKeyRequest(keyRequestId); }
		catch(final ParityException px) { throw new RuntimeException(px); }

	}

	public enum DataKey { KEY_REQUEST_ID }
}