/**
 * Created On: 12-Jul-06 4:43:56 PM
 * $Id$
 */
package com.thinkparity.browser.platform.action.container;

import java.util.LinkedList;
import java.util.List;

import javax.swing.Icon;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.model.container.Container;
import com.thinkparity.model.parity.model.document.Document;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class CreateContainer extends AbstractAction {

    private static final Icon ICON;

    private static final ActionId ID;

    private static final String NAME;

    static {
        ICON = null;
        ID = ActionId.CONTAINER_CREATE;
        NAME = "Create Package";
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
    public CreateContainer(final Browser browser) {
        super("Container.Create", ID, NAME, ICON);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) throws Exception {
        final String containerName = (String) data.get(DataKey.NAME);
        if ((null == containerName) || (containerName.length() == 0 )) {
            // Launch the NewContainerDialog. If the user presses OK, it will call
            // this action with a name and end up in the "else" clause below.
            browser.displayNewContainerDialog();
        }
        else {
            Container container = getContainerModel().create(containerName);
            browser.fireContainerCreated(container.getId(), Boolean.FALSE);
        }                   
    }

    public enum DataKey { NAME }

}
