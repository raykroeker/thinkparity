/*
 * Created On: Sep 21, 2006 2:53:55 PM
 */
package com.thinkparity.ophelia.browser.platform.plugin.extension;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @param <TEMCP>
 *            The extension content provider type.
 */
public abstract class TabExtensionModel<TEMCP extends TabExtensionModelContentProvider>
        extends TabModel {

    /**
     * Create ExtensionTabModel.
     * 
     */
    protected TabExtensionModel() {
        super();
    }
}