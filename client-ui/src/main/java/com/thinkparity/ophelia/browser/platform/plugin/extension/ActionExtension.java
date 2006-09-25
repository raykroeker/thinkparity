/*
 * Created On: Sep 24, 2006 4:06:28 PM
 */
package com.thinkparity.ophelia.browser.platform.plugin.extension;

import com.thinkparity.ophelia.browser.platform.action.AbstractAction;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class ActionExtension extends AbstractExtension {

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
}
