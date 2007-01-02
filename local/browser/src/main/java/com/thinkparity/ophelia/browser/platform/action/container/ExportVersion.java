/**
 * Created On: 13-Sep-06 3:33:56 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import java.io.File;

import com.thinkparity.ophelia.browser.Constants;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ExportVersion extends AbstractAction  {
    
    /** The browser application. */
    private final Browser browser;
    
    /** An instance of the link action. */
    private final ExportFileLink exportFileLink;

    /**
     * Create ExportVersion.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public ExportVersion(final Browser browser) {
        super(ActionId.CONTAINER_EXPORT_VERSION);
        this.browser = browser;
        this.exportFileLink = new ExportFileLink(localization.getString("ExportFileCreated"));
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final Long versionId = (Long) data.get(DataKey.VERSION_ID);
        final File directory = Constants.Directories.USER_DATA; 
        final File file = getContainerModel().exportVersion(directory, containerId, versionId);
        
        exportFileLink.setFile(file);
        browser.setStatus(exportFileLink); 
    } 
    
    public enum DataKey { CONTAINER_ID, VERSION_ID }
}
