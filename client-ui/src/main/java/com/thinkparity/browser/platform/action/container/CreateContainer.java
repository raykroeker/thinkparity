/**
 * Created On: 12-Jul-06 4:43:56 PM
 * $Id$
 */
package com.thinkparity.browser.platform.action.container;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Icon;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.action.document.CreateDocuments.DataKey;

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
        NAME = "CreateContainer";
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
        final Integer numFiles = (Integer) data.get(DataKey.NUM_FILES);
        
        if ((null == containerName) || (containerName.length() == 0 )) {
            // Launch the NewContainerDialog to get the container name.
            // If the user presses OK, it will call back into this action
            // with the name provided.
            if (numFiles>0) {
                final List<File> files = getDataFiles(data, DataKey.FILES);
                browser.displayNewContainerDialog(files);
            }
            else {
                browser.displayNewContainerDialog();
            }
        }
        else {
            // Create the container
            Container container = getContainerModel().create(containerName);
            browser.fireContainerCreated(container.getId(), Boolean.FALSE);
            
            // If there are files then add them to the container
            if ((null!=container) && (numFiles>0)) {
                final List<File> files = getDataFiles(data, DataKey.FILES);
                browser.runCreateDocuments(container.getId(),files);
            }
        }                   
    }

    public enum DataKey { NAME, NUM_FILES, FILES }

}
