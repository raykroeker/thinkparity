/**
 * Created On: 1-Sep-06 3:03:28 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.thinkparity.codebase.Constants;
import com.thinkparity.codebase.FileUtil;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.model.container.ContainerDraftPrinter;
import com.thinkparity.ophelia.model.container.ContainerModel;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.util.swing.DesktopUtil;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class PrintDraft extends AbstractBrowserAction {

    /** The browser application. */
    private final Browser browser;

    /**
     * Create PrintDraft.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public PrintDraft(final Browser browser) {
        super(ActionId.CONTAINER_PRINT_DRAFT);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final ContainerModel containerModel = getContainerModel();
        final Container container = containerModel.read(containerId);

        if (DesktopUtil.isPrintServiceAvailable()) {
            if (browser.confirm("ContainerPrintDraft.ConfirmPrintMessage", new Object[] {
                    container.getName() })) {
                containerModel.printDraft(containerId, new ContainerDraftPrinter() {
                    public void print(final Document document, final InputStream content) {
                        final File file;
                        try {
                            file = File.createTempFile(Constants.File.TEMP_FILE_PREFIX, document.getName());
                            file.deleteOnExit();
                            FileUtil.write(content, file);
                        } catch (final IOException iox) {
                            throw translateError(iox);
                        }
                        if (DesktopUtil.isPrintable(file)) {
                            try {
                                DesktopUtil.print(file);
                            } catch (final IOException iox) {
                                throw translateError(iox);
                            }
                        } else {
                            browser.displayErrorDialog("ErrorPrintDraftNotPrintable", new Object[] {document.getName()});         
                        }
                    }
                });
            }
        } else {
            browser.displayErrorDialog("ErrorPrintDraftNoPrinter");
        }
    }

    public enum DataKey { CONTAINER_ID }
}
