/*
 * Created On: Sep 21, 2006 2:53:03 PM
 */
package com.thinkparity.ophelia.browser.platform.plugin.extension;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabListAvatar;

/**
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @param <TEM>
 *            The tab extension model type.
 */
public abstract class TabExtensionAvatar<TEM extends TabExtensionModel> extends
        TabListAvatar<TEM> {

    /** The tab extension this avatar belongs to. */
    private final TabExtension extension;

    /**
     * Create ExtensionTabAvatar.
     * 
     * @param model
     *            The tab extension model.
     */
    protected TabExtensionAvatar(final TabExtension extension, final TEM model) {
        super(null, model);
        this.extension = extension;
    }

    /**
     * Obtain the extension
     *
     * @return The TabExtension.
     */
    protected TabExtension getExtension() {
        return extension;
    }
}
