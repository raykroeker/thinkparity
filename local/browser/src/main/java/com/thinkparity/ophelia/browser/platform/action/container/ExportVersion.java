/**
 * Created On: 13-Sep-06 3:33:56 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

import com.thinkparity.codebase.model.container.ContainerVersion;

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
public class ExportVersion extends AbstractBrowserAction  {
    
    /**
     * Generate a directory name for export.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @return A directory name <code>String</code>.
     */
    private static String exportFileName(final ContainerVersion version) {
        return MessageFormat.format("{0} {1,date,MMM dd, yyyy h mm ss a}.zip",
                version.getName(), version.getUpdatedOn().getTime());
    }

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
        final ContainerModel containerModel = getContainerModel();
        final ContainerVersion version = containerModel.readVersion(containerId, versionId);
        final File file = new File(Constants.Directories.USER_DATA, exportFileName(version));
        if (file.exists()) {
            if (browser.confirm("ExportVersion.ConfirmOverwrite", new Object[] {file.getName()})) {
                if (file.delete()) {
                    exportVersion(containerModel, file, version);
                } else {
                    browser.displayErrorDialog("ExportVersion.CannotDelete",
                        new Object[] {file.getName()});
                }
            }
        } else {
            exportVersion(containerModel, file, version);
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
    private void exportVersion(final ContainerModel containerModel,
            final File file, final ContainerVersion version) {
        try {
            final OutputStream outputStream = new FileOutputStream(file);
            try {
                containerModel.exportVersion(outputStream, version.getArtifactId(),
                        version.getVersionId());
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
            browser.displayErrorDialog("ExportVersion.CannotExport",
                        new Object[] {file.getName()});
        }
    } 
    
    public enum DataKey { CONTAINER_ID, VERSION_ID }
}
