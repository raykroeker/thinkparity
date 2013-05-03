/*
 * Created On:  22-Dec-07 6:23:39 PM
 */
package com.thinkparity.ophelia.browser.platform.action.workspace;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.model.workspace.configuration.ProxyConfiguration;

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
public final class UpdateProxyConfiguration extends AbstractSystemAction {

    /** The system application. */
    private final SystemApplication system;

    /**
     * Create UpdateProxyConfiguration.
     * 
     * @param system
     *            A <code>SystemApplication</code>.
     */
    public UpdateProxyConfiguration(final SystemApplication system) {
        super(ActionId.WORKSPACE_UPDATE_PROXY_CONFIGURATION);
        this.system = system;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     *
     */
    @Override
    protected void invoke(final Data data) {
        final ProxyConfiguration configuration = (ProxyConfiguration) data.get(DataKey.CONFIGURATION);
        if (null == configuration) {
            final ProxyConfiguration existing = getWorkspace().getConfiguration().readProxyConfiguration();
            SwingUtil.ensureDispatchThread(new Runnable() {

                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    system.displayUpdateConfigurationDialog(existing);
                }
            });
        } else {
            getWorkspace().getConfiguration().updateProxyConfiguration(configuration);
        }
    }

    /** <b>Title:</b>Update Data Key<br> */
    public enum DataKey { CONFIGURATION }
}
