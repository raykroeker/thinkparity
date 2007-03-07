/**
 * Created On: 1-Sep-06 3:03:28 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.jdesktop.jdic.desktop.DesktopException;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.util.jdic.DesktopUtil;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.container.ContainerVersionPrinter;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class PrintVersion extends AbstractBrowserAction {

    /** The browser application. */
    private final Browser browser;

    /**
     * Create PrintVersion.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public PrintVersion(final Browser browser) {
        super(ActionId.CONTAINER_PRINT_VERSION);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final Long versionId = (Long) data.get(DataKey.VERSION_ID);
        final ContainerModel containerModel = getContainerModel();
        final Container container = containerModel.read(containerId);
        final ContainerVersion version = containerModel.readVersion(containerId, versionId);

        if (DesktopUtil.isPrintServiceAvailable()) {
            if (browser.confirm("ContainerPrintVersion.ConfirmPrintMessage", new Object[] {
                    container.getName(), version.getCreatedOn().getTime() })) {
                containerModel.printVersion(containerId, versionId, new ContainerVersionPrinter() {
                    public void print(final DocumentVersion version, final InputStream content) {
                        final File file;
                        try {
                            file = File.createTempFile("", version.getName());
                            file.deleteOnExit();
                            FileUtil.write(content, file);
                        } catch (final IOException iox) {
                            throw translateError(iox);
                        }
                        if (DesktopUtil.isPrintable(file)) {
                            try {
                                DesktopUtil.print(file);
                            } catch (final DesktopException dx) {
                                throw translateError(dx);
                            }
                        } else {
                            browser.displayErrorDialog("ErrorPrintVersionNotPrintable", new Object[] {version.getName()});         
                        }
                    }
                });
            }
        } else {
            browser.displayErrorDialog("ErrorPrintVersionNoPrinter");
        }
    }

    public enum DataKey { CONTAINER_ID, VERSION_ID }
}
