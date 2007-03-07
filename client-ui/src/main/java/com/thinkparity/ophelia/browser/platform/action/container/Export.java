/**
 * Created On: 1-Sep-06 3:00:09 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.model.container.ContainerModel;

import com.thinkparity.ophelia.browser.Constants;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class Export extends AbstractBrowserAction {
    
    /**
     * Generate a directory name for export.
     * 
     * @param container
     *            A <code>Container</code>.
     * @return A directory name <code>String</code>.
     */
    private static String exportFileName(final Container container) {
        return MessageFormat.format("{0}.zip", container.getName());
    }
    
    /** The browser application. */
    private final Browser browser;

    /** An instance of the link action. */
    private final ExportFileLink exportFileLink;

    /**
     * Create Export.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public Export(final Browser browser) {
        super(ActionId.CONTAINER_EXPORT);
        this.browser = browser;
        this.exportFileLink = new ExportFileLink(localization.getString("ExportFileCreated"));
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);        
        final ContainerModel containerModel = getContainerModel();
        final Container container = containerModel.read(containerId);
        final File file = new File(Constants.Directories.USER_DATA, exportFileName(container));
        if (file.exists()) {
            if (browser.confirm("Export.ConfirmOverwrite", new Object[] {file.getName()})) {
                if (file.delete()) {
                    export(containerModel, file, container);
                } else {
                    browser.displayErrorDialog("Export.CannotDelete",
                            new Object[] {file.getName()});
                }
            }
        } else {
            export(containerModel, file, container);
        }
    }

    /**
     * Export a container via the model to the file.
     * 
     * @param containerModel
     *            A <code>ContainerModel</code>.
     * @param file
     *            A <code>File</code>.
     * @param containerId
     *            A container id <code>Long</code>.
     */
    private void export(final ContainerModel containerModel, final File file,
            final Container container) {
        try {
            final OutputStream outputStream = new FileOutputStream(file);
            try {
                containerModel.export(outputStream, container.getId());
            } finally {
                try {
                    outputStream.flush();
                } finally {
                    outputStream.close();
                }
            }
            exportFileLink.setFile(file);
            browser.setStatus(exportFileLink);
        } catch (final IOException iox) {
            browser.displayErrorDialog("Export.CannotExport",
                    new Object[] {container.getName()});
        }
    }

    public enum DataKey { CONTAINER_ID }
}
