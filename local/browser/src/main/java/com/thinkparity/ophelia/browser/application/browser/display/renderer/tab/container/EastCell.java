/*
 * Created On:  6-Dec-06 12:51:36 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface EastCell extends Cell {

    /**
     * Obtain the additional text.
     * 
     * @return Additiona text <code>String</code>.
     */
    public String getAdditionalText();

    /**
     * Determine whether or not additional text is set.
     * 
     * @return True if the additional text is set.
     */
    public Boolean isSetAdditionalText();
}
