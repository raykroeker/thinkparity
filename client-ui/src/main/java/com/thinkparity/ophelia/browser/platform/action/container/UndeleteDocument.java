/**
 * Created On: 1-Sep-06 4:15:11 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class UndeleteDocument extends AbstractAction {

    /**
     * Create Export.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public UndeleteDocument(final Browser browser) {
        super(ActionId.CONTAINER_UNDELETE_DOCUMENT);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
        getContainerModel().revertDocument(containerId, documentId);   
    }
    
    public enum DataKey { CONTAINER_ID, DOCUMENT_ID }
}
