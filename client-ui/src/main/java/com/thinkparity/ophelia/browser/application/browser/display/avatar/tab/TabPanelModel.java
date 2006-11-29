/*
 * Created On:  October 7, 2006, 10:36 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab;

import java.awt.event.MouseEvent;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;

/**
 * <b>Title:</b>thinkParity Tab Panel Model<br>
 * <b>Description:</b>A model for all tab panels.<br>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public abstract class TabPanelModel extends TabModel {

    /** An apache logger wrapper. */
    protected final Log4JWrapper logger;

    /**
     * Create TabPanelModel.
     *
     */
    public TabPanelModel() {
        super();
        this.logger = new Log4JWrapper(getClass());
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
     * Trigger an expand event for a tab panel.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     */
    public void triggerExpand(final TabPanel tabPanel) {}
}
