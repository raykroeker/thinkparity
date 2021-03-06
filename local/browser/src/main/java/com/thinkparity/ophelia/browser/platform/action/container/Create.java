/**
 * Created On: 12-Jul-06 4:43:56 PM
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import java.io.File;
import java.util.List;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;


/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class Create extends AbstractBrowserAction {

    /** The browser application. */
    private final Browser browser;

    /**
     * Create a Create.
     * 
     * @param browser
     *            The browser application.
     */
    public Create(final Browser browser) {
        super(ActionId.CONTAINER_CREATE);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final String containerName = (String) data.get(DataKey.NAME);
        final List<File> files = getDataFiles(data, DataKey.FILES);

        if (null == containerName) {
            // Launch the NewContainerDialog to get the container name.
            // If the user presses OK, it will call back into this action
            // with the name provided.
            if (null != files) {
                browser.displayCreateContainerDialog(files);
            } else {
                browser.displayCreateContainerDialog();
            }
        } else {
            // Create the container
            final Container container = getContainerModel().create(containerName);
            getContainerModel().applyFlagSeen(container.getId());

            // Clear search and filter
            browser.showAllTabPanels();

            // if there are files then add them to the container
            if (null != files) {
                browser.runAddContainerDocuments(container.getId(), files.toArray(new File[] {}));
            } else {
                // otherwise run the add document action
                browser.runAddContainerDocuments(container.getId());
            }
        }                   
    }

    public enum DataKey { NAME, FILES }
}
