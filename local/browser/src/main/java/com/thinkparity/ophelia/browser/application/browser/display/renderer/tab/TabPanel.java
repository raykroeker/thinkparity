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
     * Collapse the panel.
     * 
     * @param animate
     *            Animate <code>boolean</code>.
     */
    public void collapse(final boolean animate);

    /**
     * Expand the panel.
     * 
     * @param animate
     *            Animate <code>boolean</code>.
     */
    public void expand(final boolean animate);

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

    /**
     * Determine if the panel is currently animating.
     * 
     * @return True if the panel is currently animating.
     */
    public Boolean isAnimating();

    /**
     * Determine if the panel is currently expanded.
     * 
     * @return True if the panel is currently expanded.
     */
    public Boolean isExpanded();

    /**
     * Determine if data required for the expanded panel has been set.
     * 
     * @return true if data required for the expanded panel has been set, false otherwise.
     */
    public Boolean isSetExpandedData();

    /**
     * Refresh content that might become out of date, for example,
     * fuzzy dates.
     */
    public void refresh();

    /**
     * Expand or collapse the panel without animation.
     * 
     * @param expanded
     *            Expanded <code>Boolean</code>.
     */
    public void setExpanded(final Boolean expanded);

    /**
     * Set selected.
     * 
     * @param selected
     *            Selected <code>Boolean</code>.
     */
    public void setSelected(final Boolean selected);
}
