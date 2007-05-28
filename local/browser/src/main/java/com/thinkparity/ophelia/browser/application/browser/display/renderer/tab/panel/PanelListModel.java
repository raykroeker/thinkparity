/**
 * Created On: 28-May-07 1:39:23 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface PanelListModel {

    /**
     * Get the number of pages.
     * 
     * @return The <code>int</code> number of pages.
     */
    public int getNumberPages();

    /**
     * Get the selected page.
     * 
     * @return The <code>int</code> selected page.
     */
    public int getSelectedPage();

    /**
     * Set the PanelListManager.
     * 
     * @param panelListManager
     *            The <code>PanelListManager</code>.
     */
    public void setPanelListManager(final PanelListManager panelListManager);

    /**
     * Set the selected page.
     * 
     * @param selectedPage
     *            The <code>int</code> selected page.
     */
    public void setSelectedPage(final int selectedPage);
}
