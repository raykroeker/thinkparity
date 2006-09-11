/*
 * Created On: Jan 19, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.container;


import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.13
 */
public class RemoveDocument extends AbstractAction {

	/**
	 * Create RemoveDocument.
	 * 
	 * @param browser
	 *            The browser application.
	 */
	public RemoveDocument(final Browser browser) {
		super(ActionId.CONTAINER_REMOVE_DOCUMENT);
	}

	/**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
	public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
	    getContainerModel().removeDocument(containerId, documentId);
	}

	public enum DataKey { CONTAINER_ID, DOCUMENT_ID }
}
