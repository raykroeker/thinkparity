/*
 * Created On:  1-Dec-06 9:11:07 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel;

import javax.swing.Icon;

import com.thinkparity.ophelia.browser.util.ImageIOUtil;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DefaultCell implements Cell {

    /** The cell additional text <code>String</code>. */
    private String additionalText;

    /** The cell enabled <code>Boolean</code> flag. */
    private Boolean enabled;

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
        setEnabled(Boolean.TRUE);
        setIcon(ImageIOUtil.readIcon("Invisible16x16.png"));
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.Cell#getAdditionalText()
     *
     */
    public String getAdditionalText() {
        return additionalText;
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.Cell#isActionAvailable()
     */
    public Boolean isActionAvailable() {
        return Boolean.TRUE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.Cell#isEnabled()
     * 
     */
    public Boolean isEnabled() {
        return enabled;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.Cell#isSetAdditionalText()
     * 
     */
    public Boolean isSetAdditionalText() {
        return null != additionalText;
    }

    /**
     * Set the addtional text.
     * 
     * @param additionalText
     *            The additional text <code>String</code>.
     */
    public void setAdditionalText(final String additionalText) {
        this.additionalText = additionalText;
    }

    /**
     * Set the cell's enabled flag.
     * 
     * @param enabled
     *            True if the cell is enabled.
     */
    public void setEnabled(final Boolean enabled) {
        this.enabled = enabled;
    }

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
    public void showPopup() {}
}
