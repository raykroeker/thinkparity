/*
 * Created On: Thu Jun 01 2006 16:37 PDT
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;


import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * Rename is run when the user rename's an artifact.
 *
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class RevertDocument extends AbstractAction {

	/**
	 * Create RevertDocument.
	 * 
     * @param browser
     *      The browser application.
	 */
	public RevertDocument(final Browser browser) {
		super(ActionId.CONTAINER_REVERT_DOCUMENT);
	}

	/** @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data) */
	public void invoke(final Data data) {
	    final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
        getContainerModel().revertDocument(containerId, documentId);
	}

	public enum DataKey { CONTAINER_ID, DOCUMENT_ID }
}
