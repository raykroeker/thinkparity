/*
 * Created On:  4-Dec-06 12:06:14 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab;

import com.thinkparity.ophelia.browser.platform.action.PopupDelegate;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface TabPanelPopupDelegate extends PopupDelegate {

    /**
     * Show a popup for a panel.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     */
    public void showForPanel(final TabPanel tabPanel);
}
