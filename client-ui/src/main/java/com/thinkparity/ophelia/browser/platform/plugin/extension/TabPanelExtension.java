/*
 * Created On: Sep 21, 2006 11:03:19 AM
 */
package com.thinkparity.ophelia.browser.platform.plugin.extension;

import com.thinkparity.ophelia.browser.platform.plugin.PluginAbstractExtension;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class TabPanelExtension<TPEA extends TabPanelExtensionAvatar<TPEM>,
        TPEM extends TabPanelExtensionModel, ECP extends TabExtensionModelContentProvider>
        extends PluginAbstractExtension {

    /**
     * Create TabExtension.
     * 
     */
    protected TabPanelExtension() {
        super();
    }

    /**
     * Create the tab's avatar.
     * 
     * @return A tab avatar.
     */
    public abstract TPEA createAvatar();

    /**
     * Obtain the tab avatar's content provider.
     * 
     * @return A <code>ContentProvider</code>.
     */
    public abstract ECP getProvider();

    public abstract String getText();
}
