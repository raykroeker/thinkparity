/*
 * Created On: 6-Oct-06 2:06:21 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelAvatar;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContainerAvatar extends TabPanelAvatar<ContainerModel> {

    /**
     * Create ContainerAvatar.
     * 
     */
    public ContainerAvatar() {
        super(AvatarId.TAB_CONTAINER, new ContainerModel());
    }
}
