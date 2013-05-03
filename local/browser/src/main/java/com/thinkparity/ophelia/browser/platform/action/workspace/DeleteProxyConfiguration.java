/*
 * Created On:  22-Dec-07 6:23:39 PM
 */
package com.thinkparity.ophelia.browser.platform.action.workspace;

import com.thinkparity.ophelia.browser.application.system.SystemApplication;
import com.thinkparity.ophelia.browser.platform.action.AbstractSystemAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b>thinkParity Ophelia UI Workspace Update Proxy Configuration
 * Action<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DeleteProxyConfiguration extends AbstractSystemAction {

    /**
     * Create DeleteProxyConfiguration.
     * 
     * @param system
     *            A <code>SystemApplication</code>.
     */
    public DeleteProxyConfiguration(final SystemApplication system) {
        super(ActionId.WORKSPACE_DELETE_PROXY_CONFIGURATION);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     *
     */
    @Override
    protected void invoke(final Data data) {
        getWorkspace().getConfiguration().deleteProxyConfiguration();
    }
}
