/**
 * Created On: 3-May-07 11:41:12 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface PanelSelectionManager {

    /**
     * Determine whether or not a cell is selected.
     * 
     * @param cell
     *            The <code>Cell</code>.
     * @return True if it is selected.
     */
    public Boolean isSelected(final Cell cell);
}
