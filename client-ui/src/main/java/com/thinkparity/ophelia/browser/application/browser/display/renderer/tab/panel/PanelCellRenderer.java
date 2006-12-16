/**
 * Created On: 14-Dec-06 4:53:03 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel;



/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface PanelCellRenderer {
    
    /**
     * This method is always called before the panel is added and painted.
     * 
     * @param cell
     *            A <code>Cell</code>.
     * @param index
     *            An <code>int</code> index.
     */
    public void renderComponent(final Cell cell, final int index);
}
