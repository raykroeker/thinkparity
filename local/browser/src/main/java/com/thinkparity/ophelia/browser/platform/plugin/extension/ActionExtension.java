/*
 * Created On: Sep 24, 2006 4:06:28 PM
 */
package com.thinkparity.ophelia.browser.platform.plugin.extension;

import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.plugin.PluginAbstractExtension;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class ActionExtension extends PluginAbstractExtension {

    /**
     * Create ActionExtension.
     * 
     */
    protected ActionExtension() {
        super();
    }

    /**
     * Create an action.
     * 
     * @return An <code>AbstractAction</code>.
     */
    public abstract AbstractAction createAction();

    /**
     * @see com.thinkparity.ophelia.browser.platform.plugin.PluginAbstractExtension#getLocalizedFormattedString(java.lang.String, java.lang.Object[])
     */
    @Override
    protected String getLocalizedFormattedString(String patternKey,
            Object... arguments) {
        return super.getLocalizedFormattedString(patternKey, arguments);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.plugin.PluginAbstractExtension#getLocalizedString(java.lang.String)
     */
    @Override
    protected String getLocalizedString(String key) {
        return super.getLocalizedString(key);
    }
}
