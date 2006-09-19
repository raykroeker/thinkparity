/*
 * Created On: Sep 1, 2006 3:34:43 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.Border;

import com.thinkparity.codebase.swing.border.BottomBorder;
import com.thinkparity.codebase.swing.border.TopBorder;


import com.thinkparity.ophelia.browser.Constants.InsetFactors;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DefaultTabCell implements TabCell {

    /** The border for the bottom of the container cell. */
    private static final Border BORDER_BOTTOM;

    /** The border for the top of the first container cell. */
    private static final Border BORDER_TOP_0;

    /** The border for the top of the rest of the container cells. */
    private static final Border BORDER_TOP_N;

    static {
        BORDER_BOTTOM = new BottomBorder(Colours.MAIN_CELL_DEFAULT_BORDER1);
        BORDER_TOP_0 = new TopBorder(Colours.MAIN_CELL_DEFAULT_BORDER1, new Insets(2,0,0,0));
        BORDER_TOP_N = new TopBorder(Color.WHITE);
    }

    /** Create DefaultTabCell. */
    protected DefaultTabCell() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getBackground()
     */
    public BufferedImage getBackground() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getBackgroundSelected()
     */
    public BufferedImage getBackgroundSelected() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getBorder(int)
     */
    public Border getBorder(final int index, final Boolean isFirstInGroup, final Boolean lastCell) {
        final Border topBorder;
        if (0 == index) {
            topBorder = BORDER_TOP_0;
        } else {
            topBorder = BORDER_TOP_N;
        }
        return BorderFactory.createCompoundBorder(topBorder, BORDER_BOTTOM);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getNodeIcon()
     */
    public ImageIcon getNodeIcon() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getNodeIconSelected()
     */
    public ImageIcon getNodeIconSelected() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getParent()
     */
    public TabCell getParent() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getSecondaryText()
     */
    public String getSecondaryText() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getText()
     */
    public String getText() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextFont()
     */
    public Font getTextFont() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextForeground()
     */
    public Color getTextForeground() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextInsetFactor()
     */
    public Float getTextInsetFactor() {
        return InsetFactors.LEVEL_0;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getToolTip()
     */
    public String getToolTip() {
        return getText();
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#isFirstInGroup()
     */
    public Boolean isFirstInGroup() {
        return Boolean.FALSE;
    }   

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(com.thinkparity.ophelia.browser.platform.Platform.Connection, java.awt.Component, java.awt.event.MouseEvent)
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e) {}
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerDoubleClickAction(com.thinkparity.ophelia.browser.application.browser.Browser)
     */
    public void triggerDoubleClickAction(Browser browser) {
        return;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#isExpanded()
     */
    public Boolean isExpanded() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#setExpanded(java.lang.Boolean)
     */
    public Boolean setExpanded(Boolean expand) {
        return Boolean.FALSE;
    }
}
