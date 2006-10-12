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

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.border.BottomBorder;
import com.thinkparity.codebase.swing.border.TopBorder;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.Constants.InsetFactors;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCacheTest;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache.TabCellIcon;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache.TabCellImage;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class DefaultTabCell implements TabCell {

    /** The border for the bottom of the cell. */
    private static final Border BORDER_BOTTOM;

    /** The border for the top of child cells. */
    private static final Border BORDER_CHILD_TOP;
    
    /** The border for the top of the cell. */
    private static final Border BORDER_TOP;
    
    /** The border for the top of a group of cells. */
    private static final Border BORDER_GROUP_TOP;   

    /** The cell's text foreground color. */
    private static final Color TEXT_FG;
    
    /** The cell's text foreground colour for mouse over cells. */
    private static final Color TEXT_FG_MOUSEOVER;
    
    /** Maximum length of a container cell's text. */
    private static final Integer TEXT_MAX_LENGTH;   

    static {
        BORDER_BOTTOM = new BottomBorder(Colours.MAIN_CELL_DEFAULT_BORDER);
        BORDER_TOP = new TopBorder(Colours.MAIN_CELL_DEFAULT_BORDER, new Insets(2,0,0,0));
        BORDER_CHILD_TOP = new TopBorder(Colours.MAIN_CELL_DEFAULT_BORDER_CHILD, new Insets(2,0,0,0));
        BORDER_GROUP_TOP = new TopBorder(Colours.MAIN_CELL_DEFAULT_BORDER_GROUP, 3, new Insets(4,0,0,0));
        
        TEXT_FG = Colors.Browser.TabCell.TEXT;
        TEXT_FG_MOUSEOVER = Colors.Browser.TabCell.TEXT_MOUSEOVER;
        
        TEXT_MAX_LENGTH = 100;
    }
    
    /** A flag indicating the expand\collapse status. */
    protected Boolean expanded = Boolean.FALSE;
    
    /** An image cache. */
    protected final MainCellImageCache imageCache;
    protected final MainCellImageCacheTest imageCacheTest;
    
    /** A flag indicating the mouse over status. */
    protected Boolean mouseOver = Boolean.FALSE;
    
    /** A popup menu item factory. */
    protected final PopupItemFactory popupItemFactory;

    /** Whether or not the cell is being hovered over. */
    private Boolean hover;

    /** Create DefaultTabCell. */
    protected DefaultTabCell() {
        super();
        this.imageCache = new MainCellImageCache();
        this.imageCacheTest = new MainCellImageCacheTest();
        this.popupItemFactory = PopupItemFactory.getInstance();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getBackground()
     */
    public BufferedImage getBackground() {
        return imageCache.read(TabCellImage.BG_DEFAULT);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getBackgroundSelected()
     */
    public BufferedImage getBackgroundSelected() {
        return imageCache.read(TabCellImage.BG_SEL_DEFAULT);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getBorder(int)
     */
    public Border getBorder(final int index, final Boolean isFirstInGroup, final Boolean lastCell) {
        final Border topBorder;
        if (getParent() != null) {
            topBorder = BORDER_CHILD_TOP;
        } else if (isFirstInGroup) {
            topBorder = BORDER_GROUP_TOP;
        } else {
            topBorder = BORDER_TOP;
        }
        
        if (lastCell) {
            return BorderFactory.createCompoundBorder(topBorder, BORDER_BOTTOM);
        } else {
            return topBorder;
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getNodeIcon()
     */
    public ImageIcon getNodeIcon() {
        if (!isChildren()) {
            return imageCache.read(TabCellIcon.NODE_NOCHILDREN);            
        } else if (isExpanded()) {
            return imageCache.read(TabCellIcon.NODE_EXPANDED);
        } else {
            return imageCache.read(TabCellIcon.NODE_COLLAPSED);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getNodeIconSelected()
     */
    public ImageIcon getNodeIconSelected() {
        if (!isChildren()) {
            return imageCache.read(TabCellIcon.NODE_NOCHILDREN); 
        } else if (isExpanded()) {
            return imageCache.read(TabCellIcon.NODE_SEL_EXPANDED);
        } else {
            return imageCache.read(TabCellIcon.NODE_SEL_COLLAPSED);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getParent()
     */
    public TabCell getParent() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getText(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     */
    public String getText(final TextGroup textGroup) { 
        final String nonClippedText = getTextNoClipping(textGroup);
        switch (textGroup) {
        case WEST:
            final String clippedText;
            if (TEXT_MAX_LENGTH < nonClippedText.length()) {
                clippedText = nonClippedText.substring(0, TEXT_MAX_LENGTH - 1 - 3) + "...";                    
            } else {
                clippedText = nonClippedText;
            }

            if (isMouseOver() && isChildren()) {
                return "<html><u>" + clippedText + "</u></html>";
            } else {
                return clippedText;
            }
        case EAST:
            if (null == nonClippedText) {
                return null;
            } else {
                if (TEXT_MAX_LENGTH < nonClippedText.length()) {
                    return nonClippedText.substring(0, TEXT_MAX_LENGTH - 1 - 3) + "...";                    
                } else {
                    return nonClippedText;
                }
            }
        default:
            throw Assert.createUnreachable("UNKNOWN TEXT GROUP");
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextFont(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     */
    public Font getTextFont(final TextGroup textGroup) {
        return BrowserConstants.Fonts.DefaultFont;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextForeground(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     */
    public Color getTextForeground(final TextGroup textGroup) {
        if (isMouseOver() && textGroup == TextGroup.WEST && isChildren()) {
            return TEXT_FG_MOUSEOVER;
        } else {
            return TEXT_FG;
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextInsetFactor()
     */
    public Float getTextInsetFactor() {
        return InsetFactors.LEVEL_0;
    }   

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextNoClipping(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     */
    public abstract String getTextNoClipping(final TextGroup textGroup);

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getToolTip()
     */
    public String getToolTip() {
        return getTextNoClipping(TextGroup.WEST);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#isChildren()
     */
    public Boolean isChildren() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerDoubleClickAction(com.thinkparity.ophelia.browser.application.browser.Browser)
     */
    public Boolean isExpanded() {
        return expanded;
    }   

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#isFirstInGroup()
     */
    public Boolean isFirstInGroup() {
        return Boolean.FALSE;
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#isHover()
     */
    public Boolean isHover() {
        return hover;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#isMouseOver()
     */
    public Boolean isMouseOver() {
        return mouseOver;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#setExpanded(java.lang.Boolean)
     */
    public void setExpanded(final Boolean expanded) {
        this.expanded = expanded;
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#setMouseOver(java.lang.Boolean)
     */
    public void setMouseOver(final Boolean mouseOver) {
        this.mouseOver = mouseOver;  
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#setHover(java.lang.Boolean)
     */
    public void triggerDoubleClickAction(final Browser browser) {}

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(com.thinkparity.ophelia.browser.platform.Platform.Connection, java.awt.Component, java.awt.event.MouseEvent)
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e) {}
}
