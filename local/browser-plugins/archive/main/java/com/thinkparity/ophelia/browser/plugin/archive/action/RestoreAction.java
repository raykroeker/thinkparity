/*
 * Created On: Sep 24, 2006 4:09:25 PM
 */
package com.thinkparity.ophelia.browser.plugin.archive.action;

import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.plugin.PluginServices;
import com.thinkparity.ophelia.browser.platform.plugin.extension.ActionExtension;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class RestoreAction extends ActionExtension {

    /** The localization base name. */
    private static final String LOCALIZATION_BASE_NAME;

    /** The localization context. */
    private static final String LOCALIZATION_CONTEXT;

    static {
        LOCALIZATION_BASE_NAME = "localization/Archive_Messages";
        LOCALIZATION_CONTEXT = "RestoreAction";
    }

    /** The plugin services. */
    private PluginServices services;

    /** Create RestoreAction. */
    public RestoreAction() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.plugin.extension.ActionExtension#createAction()
     */
    @Override
    public AbstractAction createAction() {
        return new Restore(services, this);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.plugin.PluginExtension#initialize(com.thinkparity.ophelia.browser.platform.plugin.PluginServices)
     */
    public void initialize(final PluginServices services) {
        this.services = services;
        initializeLocalization(services, LOCALIZATION_BASE_NAME, LOCALIZATION_CONTEXT);
        initializeServices(services);
    }
}
