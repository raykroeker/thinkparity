/*
 * Created On:  1-Dec-06 9:11:07 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel;

import javax.swing.Icon;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DefaultCell implements Cell {

    /** The cell <code>Icon</code>. */
    private Icon icon;

    /** The cell text <code>String</code>. */
    private String text;

    /**
     * Create DefaultVersionsCell.
     *
     */
    public DefaultCell() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.VersionsCell#getIcon()
     *
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.VersionsCell#getText()
     *
     */
    public String getText() {
        return text;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.VersionsCell#invokeAction()
     * 
     */
    public void invokeAction() {}

    /**
     * Set the cell icon.
     * 
     * @param icon
     *            An <code>Icon</code>.
     */
    public void setIcon(final Icon icon) {
        this.icon = icon;
    }

    /**
     * Set the cell text.
     * 
     * @param text
     *            The text <code>String</code>.
     */
    public void setText(final String text) {
        this.text = text;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.VersionsCell#showPopup()
     * 
     */
    public void showPopup() {
    }
}
