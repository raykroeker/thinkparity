/**
 * Created On: 24-May-07 12:23:03 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.event.tab.help;

import com.thinkparity.ophelia.model.help.HelpModel;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.help.HelpTabAvatar;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class HelpTabDispatcher implements EventDispatcher<HelpTabAvatar> {

    /**
     * Create HelpTabDispatcher.
     */
    public HelpTabDispatcher(final HelpModel helpModel) {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher#addListeners(com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar)
     */
    public void addListeners(final HelpTabAvatar avatar) {
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher#removeListeners(com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar)
     */
    public void removeListeners(final HelpTabAvatar avatar) {
    }
}
