/*
 * Created On: Sep 21, 2006 2:53:55 PM
 */
package com.thinkparity.ophelia.browser.platform.plugin.extension;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabListModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @param <TEMCP>
 *            The extension content provider type.
 */
public abstract class TabListExtensionModel<TEMCP extends TabExtensionModelContentProvider>
        extends TabListModel {

    /** The <code>TabExtension</code>. */
    private final TabListExtension extension;

    /**
     * Create ExtensionTabModel.
     * 
     */
    protected TabListExtensionModel(final TabListExtension extension) {
        super();
        this.extension = extension;
    }

    /**
     * Obtain the extension.
     *
     * @return The TabExtension.
     */
    protected TabListExtension getExtension() {
        return extension;
    }
}