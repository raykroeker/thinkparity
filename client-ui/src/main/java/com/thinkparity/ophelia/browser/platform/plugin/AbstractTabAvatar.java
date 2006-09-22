/*
 * Created On: Sep 21, 2006 2:53:03 PM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatar;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class AbstractTabAvatar<ETM extends AbstractTabModel> extends
        TabAvatar<ETM> {

    /** Create ExtensionTabAvatar. */
    public AbstractTabAvatar(final AvatarId id, final ETM model) {
        super(null, model);
    }
}
