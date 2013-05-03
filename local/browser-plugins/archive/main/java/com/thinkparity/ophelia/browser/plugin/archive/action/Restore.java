/*
 * Created On: Sep 24, 2006 4:08:23 PM
 */
package com.thinkparity.ophelia.browser.plugin.archive.action;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.browser.platform.plugin.PluginServices;
import com.thinkparity.ophelia.browser.platform.plugin.extension.ActionExtension;
import com.thinkparity.ophelia.browser.platform.plugin.extension.ActionExtensionAction;
import com.thinkparity.ophelia.model.container.ContainerModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Restore extends ActionExtensionAction {
    
    /**
     * Create Restore.
     * 
     */
    public Restore(final PluginServices services, final ActionExtension extension) {
        super(services, extension);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.plugin.extension.ActionExtensionAction#invoke(java.lang.Object)
     */
    @Override
    protected void invoke(final Object selection) {
        final Container container = (Container) selection;

        final ContainerModel containerModel = getContainerModel();
        if(confirm("ConfirmRestore", container.getName())) {
            containerModel.restore(container.getUniqueId());
        }
    }
}
