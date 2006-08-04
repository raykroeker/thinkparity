/**
 * Created On: 3-Aug-06 4:47:01 PM
 * $Id$
 */
package com.thinkparity.browser.platform.action.container;

import javax.swing.Icon;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class CreateDraft extends AbstractAction {

    private static final Icon ICON;

    private static final ActionId ID;

    private static final String NAME;

    static {
        ICON = null;
        ID = ActionId.CREATE_DRAFT;
        NAME = "Create Draft";
    }

    /**
     * The browser application.
     * 
     */
    private final Browser browser;

    /**
     * Create a Create.
     * 
     * @param browser
     *            The browser application.
     */
    public CreateDraft(final Browser browser) {
        super("Container.CreateDraft", ID, NAME, ICON);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) throws Exception {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        getContainerModel().createDraft(containerId);
        browser.fireContainerDraftCreated(containerId, Boolean.FALSE);          
    }

    public enum DataKey { CONTAINER_ID }
}
