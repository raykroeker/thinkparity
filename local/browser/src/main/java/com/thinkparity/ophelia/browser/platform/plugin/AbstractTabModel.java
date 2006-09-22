/*
 * Created On: Sep 21, 2006 2:53:55 PM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel;
import com.thinkparity.ophelia.browser.application.browser.display.provider.ContentProvider;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class AbstractTabModel<CP extends ContentProvider> extends
        TabModel {

    /** Create ExtensionTabModel. */
    public AbstractTabModel() {
        super();
    }
}