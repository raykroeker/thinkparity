/*
 * Created On:  8-Dec-06 3:21:38 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface TabDelegate {

    /**
     * Toggle the expansion of a panel.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     */
    public void toggleExpansion(final TabPanel tabPanel);
}
