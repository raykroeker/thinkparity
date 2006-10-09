/*
 * Created On:  October 7, 2006, 10:36 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab;

import java.awt.Component;
import java.awt.event.MouseEvent;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;

/**
 * <b>Title:</b>thinkParity Tab Panel Model<br>
 * <b>Description:</b>A model for all tab panels.<br>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public abstract class TabPanelModel extends TabModel {
    
    /**
     * Create TabPanelModel.
     *
     */
    public TabPanelModel() {
        super();
    }

    /**
     * Trigger a double click on a tab cell.
     * 
     * 
     * @param tabCell
     *            A <code>TabCell</code>.
     */
    protected void triggerDoubleClick(final MouseEvent e) {}

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#triggerExpand(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell)
     */
    protected final void triggerExpand(final TabCell tabCell) {}

    /**
     * Trigger an expand event for a tab panel.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     */
    public void triggerExpand(final TabPanel tabPanel) {}

    /**
     * Trigger a popup menu for a tab cell.
     * 
     * 
     * @param tabCell
     *            A <code>TabCell</code>.
     * @param invoker
     *            The <code>Component</code> invoking the popup.
     * @param e
     *          The <code>MouseEvent</code>.
     */
    protected final void triggerPopup(final TabCell tabCell, final Component invoker, final MouseEvent e) {}
}
