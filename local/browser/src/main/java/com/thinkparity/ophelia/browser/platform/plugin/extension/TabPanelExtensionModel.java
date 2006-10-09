/*
 * Created On: Sep 21, 2006 2:53:55 PM
 */
package com.thinkparity.ophelia.browser.platform.plugin.extension;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @param <TEMCP>
 *            The extension content provider type.
 */
public abstract class TabPanelExtensionModel<TEMCP extends TabExtensionModelContentProvider>
        extends TabPanelModel {

    /** The <code>TabPanelExtension</code>. */
    private final TabPanelExtension extension;

    /**
     * Create TabPanelExtensionModel.
     * 
     */
    protected TabPanelExtensionModel(final TabPanelExtension extension) {
        super();
        this.extension = extension;
    }

    /**
     * Obtain the extension.
     *
     * @return The TabExtension.
     */
    protected TabPanelExtension getExtension() {
        return extension;
    }
}