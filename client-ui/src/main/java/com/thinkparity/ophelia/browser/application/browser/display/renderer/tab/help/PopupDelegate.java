/**
 * Created On: 24-May-07 10:17:05 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.help;

import com.thinkparity.ophelia.model.help.HelpTopic;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanelPopupDelegate;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface PopupDelegate extends TabPanelPopupDelegate {

    /**
     * Display a popup menu for a help topic.
     * 
     * @param helpTopic
     *            A <code>HelpTopic</code>.
     */
    public void showForHelpTopic(final HelpTopic helpTopic);
}
