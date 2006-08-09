/**
 * Created On: 12-Jul-06 4:43:56 PM
 * $Id$
 */
package com.thinkparity.browser.platform.action.container;

import java.io.File;
import java.util.List;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.model.container.Container;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class CreateContainer extends AbstractAction {

    /** The browser application. */
    private final Browser browser;

    /**
     * Create a Create.
     * 
     * @param browser
     *            The browser application.
     */
    public CreateContainer(final Browser browser) {
        super(null, ActionId.CONTAINER_CREATE, null, null);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) throws Exception {
        final String containerName = (String) data.get(DataKey.NAME);
        final List<File> files = getDataFiles(data, DataKey.FILES);

        if (null == containerName) {
            // Launch the NewContainerDialog to get the container name.
            // If the user presses OK, it will call back into this action
            // with the name provided.
            if (null != files) { browser.displayNewContainerDialog(files); }
            else { browser.displayCreateContainerDialog(); }
        }
        else {
            // Create the container
            final Container container = getContainerModel().create(containerName);
            
            // if there are files then add them to the container
            if (null != files) {
                browser.runCreateDocuments(container.getId(), files.toArray(new File[] {}));
            } else {
                // otherwise run the add document action; then manage the team
                browser.runCreateDocuments(container.getId());
                browser.runManageTeam(container.getId());
            }
            browser.fireContainerCreated(container.getId(), Boolean.FALSE);
        }                   
    }

    public enum DataKey { NAME, FILES }

}
