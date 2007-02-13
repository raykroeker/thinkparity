/**
 * Created On: 1-Sep-06 3:03:28 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import java.io.File;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.util.Printer;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.util.jdic.DesktopUtil;

import org.jdesktop.jdic.desktop.DesktopException;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class PrintDraft extends AbstractAction {

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
                containerModel.printDraft(containerId, new Printer() {
                    public void print(final File file) {
                        if (DesktopUtil.isPrintable(file)) {
                            try {
                                DesktopUtil.print(file);
                            } catch (final DesktopException dx) {
                                throw translateError(dx);
                            }
                        } else {
                            browser.displayErrorDialog("ErrorPrintDraftNotPrintable", new Object[] {container.getName()});         
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
