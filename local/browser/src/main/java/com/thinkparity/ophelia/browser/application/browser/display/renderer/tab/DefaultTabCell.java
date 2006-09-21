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

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.Constants.InsetFactors;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache.TabCellIcon;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache.TabCellImage;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class DefaultTabCell implements TabCell {

    /** The border for the bottom of the cell. */
    protected static final Border BORDER_BOTTOM;

    /** The border for the top of the cell. */
    protected static final Border BORDER_TOP;
    
    /** The border for the top of child cells. */
    protected static final Border BORDER_CHILD_TOP;

    /** The border for the top of a group of cells. */
    private static final Border BORDER_GROUP_TOP;
    
    /** The cell's text foreground color. */
    protected static final Color TEXT_FG;
    
    /** The cell's text foreground colour for mouse over cells. */
    protected static final Color TEXT_FG_MOUSEOVER;
    
    /** Maximum length of a container cell's text. */
    protected static final Integer TEXT_MAX_LENGTH;

    static {
        BORDER_BOTTOM = new BottomBorder(Colours.MAIN_CELL_DEFAULT_BORDER);
        BORDER_TOP = new TopBorder(Colours.MAIN_CELL_DEFAULT_BORDER, new Insets(2,0,0,0));
        BORDER_CHILD_TOP = new TopBorder(Colours.MAIN_CELL_DEFAULT_BORDER_CHILD, new Insets(2,0,0,0));
        BORDER_GROUP_TOP = new TopBorder(Colours.MAIN_CELL_DEFAULT_BORDER_GROUP, 2, new Insets(3,0,0,0));
        
        TEXT_FG = Colors.Browser.TabCell.TEXT;
        TEXT_FG_MOUSEOVER = Colors.Browser.TabCell.TEXT_MOUSEOVER;
        
        TEXT_MAX_LENGTH = 100;
    }
    
    /** An image cache. */
    protected final MainCellImageCache imageCache;
    
    /** A popup menu item factory. */
    protected final PopupItemFactory popupItemFactory;
    
    /** A flag indicating the expand\collapse status. */
    protected Boolean expanded = Boolean.FALSE;
    
    /** A flag indicating the mouse over status. */
    protected Boolean mouseOver = Boolean.FALSE;

    /** Create DefaultTabCell. */
    protected DefaultTabCell() {
        super();
        this.imageCache = new MainCellImageCache();
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
    public String getText(TextGroup textGroup) { 
        String s = getTextNoClipping(textGroup);
        if ((textGroup == TextGroup.MAIN_TEXT) && (s!=null)) {
            if (TEXT_MAX_LENGTH < s.length()) {
                s = s.substring(0, TEXT_MAX_LENGTH - 1 - 3) + "...";                    
            }
            if (isMouseOver() && isChildren()) {
                s = "<HTML><u>" + s + "</u>";
            }
        }
        return s;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextNoClipping(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     */
    public String getTextNoClipping(TextGroup textGroup) {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextFont(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     */
    public Font getTextFont(TextGroup textGroup) {
        return BrowserConstants.Fonts.DefaultFont;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextForeground(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     */
    public Color getTextForeground(TextGroup textGroup) {
        if (isMouseOver() && (textGroup==TextGroup.MAIN_TEXT) && isChildren()) {
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getToolTip()
     */
    public String getToolTip() {
        if (TEXT_MAX_LENGTH < getTextNoClipping(TextGroup.MAIN_TEXT).length()) { return getTextNoClipping(TextGroup.MAIN_TEXT); }
        else { return null; }
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
    public void triggerDoubleClickAction(Browser browser) {}

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#isExpanded()
     */
    public Boolean isExpanded() {
        return expanded;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#setExpanded(java.lang.Boolean)
     */
    public Boolean setExpanded(Boolean expand) {
        if (this.expanded != expand && isChildren()) {
            this.expanded = expand;
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#isMouseOver()
     */
    public Boolean isMouseOver() {
        return mouseOver;
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#setMouseOver(java.lang.Boolean)
     */
    public void setMouseOver(Boolean mouseOver) {
        this.mouseOver = mouseOver;  
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#isChildren()
     */
    public Boolean isChildren() {
        return Boolean.FALSE;
    }  
}
