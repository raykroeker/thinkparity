/*
 * Feb 28, 2006
 */
package com.thinkparity.browser.platform.action.artifact;

import java.util.List;

import javax.swing.ImageIcon;

import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.artifact.KeyRequest;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DeclineAllKeyRequests extends AbstractAction {

	private static final ImageIcon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.ARTIFACT_DECLINE_ALL_KEY_REQUESTS;
		NAME = "Decline all key requests";
	}

	/**
	 * Create a AcceptKeyRequest.
	 * 
	 */
	public DeclineAllKeyRequests() { super("DeclineKeyRequest", ID, NAME, ICON); }

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) throws Exception {
		final Long artifactId = (Long) data.get(DataKey.ARTIFACT_ID);
		try {
			final ArtifactModel aModel = getArtifactModel();
			final List<KeyRequest> keyRequests = aModel.readKeyRequests(artifactId);
			for(final KeyRequest keyRequest : keyRequests) {
				aModel.declineKeyRequest(keyRequest.getId());
			}
		}
		catch(final ParityException px) { throw new RuntimeException(px); }
	}

	public enum DataKey { ARTIFACT_ID }
}
