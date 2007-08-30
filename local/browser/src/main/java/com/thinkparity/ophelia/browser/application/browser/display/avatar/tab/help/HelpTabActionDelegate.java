/**
 * Created On: 24-May-07 10:26:42 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.help;

import com.thinkparity.ophelia.model.help.HelpTopic;

import com.thinkparity.ophelia.browser.application.browser.DefaultBrowserActionDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabButtonActionDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.help.ActionDelegate;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class HelpTabActionDelegate extends DefaultBrowserActionDelegate
        implements ActionDelegate, TabButtonActionDelegate {

    /**
     * Create HelpTabActionDelegate.
     * 
     * @param model
     *            The <code>HelpTabModel</code>.
     */
    HelpTabActionDelegate(final HelpTabModel model) {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.help.ActionDelegate#invokeForHelpTopic(com.thinkparity.ophelia.model.help.HelpTopic)
     */
    public void invokeForHelpTopic(final HelpTopic helpTopic) {}

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabButtonActionDelegate#invokeForTabButton()
     */
    public void invokeForTabButton() {}

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabButtonActionDelegate#isTabButtonActionAvailable()
     */
    public Boolean isTabButtonActionAvailable() {
        return Boolean.FALSE;
    }
}
