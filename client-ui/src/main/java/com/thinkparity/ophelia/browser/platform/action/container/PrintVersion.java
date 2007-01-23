/**
 * Created On: 1-Sep-06 3:03:28 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import java.io.File;

import org.jdesktop.jdic.desktop.DesktopException;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;



import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.util.jdic.DesktopUtil;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.util.Printer;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class PrintVersion extends AbstractAction {

    /**
     * Create Export.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public PrintVersion(final Browser browser) {
        super(ActionId.CONTAINER_PRINT_VERSION);
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

        final Browser browser = getBrowserApplication();
        if(browser.confirm("ContainerPrintVersion.ConfirmPrintMessage", new Object[] {
                container.getName(), version.getCreatedOn().getTime() })) {
            containerModel.printVersion(containerId, versionId, new Printer() {
                public void print(final File file) {
                    try {
                        DesktopUtil.print(file);
                    } catch (final DesktopException dx) {
                        throw translateError(dx);
                    }
                }
            });
        }
    }

    public enum DataKey { CONTAINER_ID, VERSION_ID }
}
