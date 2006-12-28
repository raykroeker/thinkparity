/*
 * Created On:  12-Dec-06 12:56:07 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class EmptyCell extends DefaultCell implements EastCell {

    /** An <code>EmptyCell</code> singleton. */
    private static final EmptyCell SINGLETON;

    static {
        SINGLETON = new EmptyCell();
    }

    /**
     * Obtain an empty cell.
     * 
     * @return An <code>EmptyCell</code>.
     */
    public static EmptyCell getEmptyCell() {
        return SINGLETON;
    }

    /**
     * Create EmptyCell.
     *
     */
    private EmptyCell() {
        super();
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#isActionAvailable()
     */
    @Override
    public Boolean isActionAvailable() {
        return Boolean.FALSE;
    }
}
