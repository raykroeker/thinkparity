/**
 * Created On: Jan 25, 2007 3:13:40 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser;

import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.DefaultPopupDelegate;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class BrowserPopupDelegate extends DefaultPopupDelegate {

    /**
     * Create a BrowserPopupDelegate.
     */
    public BrowserPopupDelegate() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.DefaultPopupDelegate#show()
     */
    @Override
    public void show() {
        prepareMenu();
        super.show();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate#showForVersion(com.thinkparity.codebase.model.container.ContainerVersion,
     *      java.util.Map, java.util.Map,
     *      com.thinkparity.codebase.model.user.User)
     * 
     */
    private void prepareMenu() {  
        add(ActionId.CONTAINER_CREATE, Data.emptyData());
        add(ActionId.CONTACT_CREATE_OUTGOING_EMAIL_INVITATION, Data.emptyData());
        
        addSeparator();
        
        add(ActionId.PLATFORM_BROWSER_OPEN_HELP, Data.emptyData());
    }
}
