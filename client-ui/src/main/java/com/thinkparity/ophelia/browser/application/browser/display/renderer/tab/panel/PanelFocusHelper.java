/**
 * Created On: 2-May-07 1:27:36 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel;


/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class PanelFocusHelper {

    /** A singleton instance. */
    private static final PanelFocusHelper SINGLETON;

    static {
        SINGLETON = new PanelFocusHelper();
    }

    /** The focus */
    private Focus focus;

    /**
     * Create PanelFocusHelper.
     */
    private PanelFocusHelper() {
        super();
        focus = Focus.NONE;
    }

    /**
     * Get the focus.
     * 
     * @return The <code>Focus</code>.
     */
    public static Focus getFocus() {
        return SINGLETON.focus;
    }

    /**
     * Set the focus.
     * 
     * @param focus
     *            A <code>Focus</code>.
     */
    public static void setFocus(final Focus focus) {
        SINGLETON.focus = focus;
    }

    public enum Focus { EAST, NONE, PANEL, WEST }
}
