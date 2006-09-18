/**
 * Created On: 13-Sep-06 3:02:26 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.model.container.ContainerModel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class Archive  extends AbstractAction {
    
    /**
     * Create Unsubscribe.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public Archive(final Browser browser) {
        super(ActionId.CONTAINER_ARCHIVE);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);

        final ContainerModel containerModel = getContainerModel();
        final Container container = containerModel.read(containerId);
        if( getBrowserApplication().confirm(getId().toString(),
                new Object[] { container.getName() })) {
            containerModel.archive(containerId);
        }
    }

    public enum DataKey { CONTAINER_ID }
}
