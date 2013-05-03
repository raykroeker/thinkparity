/*
 * Created On: 9-Oct-06 11:24:08 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab;

import java.awt.Component;
import java.awt.event.MouseEvent;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class TabListModel extends TabModel {

    /**
     * Create TabListModel.
     *
     */
    protected TabListModel() {
        super();
    }

    /**
     * Trigger a double click on a tab cell.
     * 
     * @param tabCell
     *            A <code>TabCell</code>.
     */
    protected void triggerDoubleClick(final TabCell tabCell) {}

    /**
     * Trigger an expand for a tab cell.
     * 
     * @param tabCell
     *            A <code>TabCell</code>.
     */
    public void triggerExpand(final TabCell tabCell) {}

    /**
     * Trigger a popup menu for a tab cell.
     * 
     * @param tabCell
     *            A <code>TabCell</code>.
     * @param invoker
     *            The <code>Component</code> invoking the popup.
     * @param e
     *          The <code>MouseEvent</code>.
     */
    protected void triggerPopup(final TabCell tabCell, final Component invoker,
            final MouseEvent e) {}

}
