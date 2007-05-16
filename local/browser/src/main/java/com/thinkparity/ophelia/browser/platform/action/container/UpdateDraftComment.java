/**
 * Created On: 16-May-07 3:25:49 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public final class UpdateDraftComment extends AbstractBrowserAction {

    /** The browser application. */
    private final Browser browser;

    /**
     * Create a UpdateNote.
     * 
     * @param browser
     *            The browser application.
     */
    public UpdateDraftComment(final Browser browser) {
        super(ActionId.CONTAINER_UPDATE_DRAFT_COMMENT);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final String comment = (String) data.get(DataKey.COMMENT);

        if (null == comment) {
            // launch the dialog
            browser.displayUpdateDraftCommentDialog(containerId);
        } else {
            // update the note
            getContainerModel().updateDraftComment(containerId, comment);
        }                  
    }

    public enum DataKey { CONTAINER_ID, COMMENT }
}
