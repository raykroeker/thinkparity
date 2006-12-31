/*
 * Created On: 6-Oct-06 2:09:46 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab;

import com.thinkparity.ophelia.browser.platform.action.PopupDelegate;

/**
 * <b>Title:</b>thinkParity Tab Panel<br>
 * <b>Description:</b>A tab panel is the display interface for the tab panel
 * avatar. It is analagous in nature to the tab cell; wherein it provides a
 * display abstraction for the data to display.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface TabPanel {

    /**
	 * Obtain a unique id for the panel.
	 * 
	 * @return An id unique per <code>TabPanelAvatar</code>.
	 */
	public Object getId();

    /**
     * Obtain the popup delegate for the panel.
     * 
     * @return A <code>PopupDelegate</code>.
     */
    public PopupDelegate getPopupDelegate();
    
    public void collapse(final boolean animate);
    
    public void expand(final boolean animate);
    
    public void setExpanded(final Boolean expanded);
}
