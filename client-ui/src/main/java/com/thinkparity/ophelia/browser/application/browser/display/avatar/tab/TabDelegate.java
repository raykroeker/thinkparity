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
     * Determine if the next panel is expanded.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     * @return True if the next panel is expanded.
     */
    public Boolean isNextPanelExpanded(final TabPanel tabPanel);

    /**
     * Select the panel.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     */
    public void selectPanel(final TabPanel tabPanel);

    /**
     * Toggle the expansion of a panel.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     */
    public void toggleExpansion(final TabPanel tabPanel);
}
