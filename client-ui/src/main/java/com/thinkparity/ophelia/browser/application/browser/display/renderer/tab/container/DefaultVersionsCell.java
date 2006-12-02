/*
 * Created On:  1-Dec-06 9:11:07 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.Constants.Colors;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DefaultVersionsCell implements VersionsCell {

    private Icon icon;

    private String text;

    private Color textForeground;

    /**
     * Create DefaultVersionsCell.
     *
     */
    public DefaultVersionsCell() {
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.VersionsCell#getTextForeground()
     *
     */
    public Color getTextForeground() {
        return null == textForeground ? Colors.Browser.List.LIST_FG
                : textForeground;
    }

    public void setIcon(final Icon icon) {
        this.icon = icon;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public void setTextForeground(final Color textForeground) {
        this.textForeground = textForeground;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.VersionsCell#showPopup(java.awt.Component, int, int)
     *
     */
    public void showPopup(Component invoker, int x, int y) {
        // TODO Auto-generated method stub
        throw Assert.createNotYetImplemented("");
    }
}
