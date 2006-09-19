/**
 * Created On: 13-Sep-06 4:09:05 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.border.Border;

import com.thinkparity.ophelia.browser.Constants.InsetFactors;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentIcon;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentImage;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.util.localization.MainCellL18n;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContainerVersionSentToCell implements TabCell {
       
    /** The cell's text foreground color. */
    private static final Color TEXT_FG;

    static {      
        TEXT_FG = Color.BLACK;
    }

    /** The version. */
    private final ContainerVersionCell version;

    /** A flag indicating the expand\collapse status. */
    private Boolean expanded = Boolean.FALSE;
    
    /** The number of users (child cells) */
    private Integer numberOfUsers = 0;

    /** An image cache. */
    private final MainCellImageCache imageCache;
    
    /** The draft cell localization. */
    private final MainCellL18n localization; 

    /** Create ContainerVersionSentToCell. */
    public ContainerVersionSentToCell(final ContainerVersionCell version) {      
        this.version = version;
        this.imageCache = new MainCellImageCache();
        this.localization = new MainCellL18n("MainCellContainerVersionSentTo");
    }

    /**
     * Obtain the background image for a cell.
     * 
     * 
     * @return A buffered image.
     */
    public BufferedImage getBackground() {
        return imageCache.read(DocumentImage.BG_DEFAULT);
    }
    
    /**
     * Obtain the background image for a selected cell.
     * 
     * 
     * @return A buffered image.
     */
    public BufferedImage getBackgroundSelected() {
        return imageCache.read(DocumentImage.BG_SEL_DEFAULT);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getBorder(int)
     * 
     */
    public Border getBorder(final int index, final Boolean isFirstInGroup, final Boolean lastCell) {
        return getParent().getBorder(index, isFirstInGroup, lastCell);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getNodeIcon()
     * 
     */
    public ImageIcon getNodeIcon() {
        if (isExpanded()) {
            return imageCache.read(DocumentIcon.NODE_EXPANDED);
        } else {
            return imageCache.read(DocumentIcon.NODE_DEFAULT);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getNodeIconSelected()
     * 
     */
    public ImageIcon getNodeIconSelected() {
        if (isExpanded()) {
            return imageCache.read(DocumentIcon.NODE_SEL_EXPANDED);
        } else {
            return imageCache.read(DocumentIcon.NODE_SEL_DEFAULT);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getParent()
     * 
     */
    public TabCell getParent() {
        return version;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getText()
     * 
     */
    public String getText() {
        final Integer number = numberOfUsers;
        return localization.getString("Text", new Object[] {number});
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getSecondaryText()
     * 
     */
    public String getSecondaryText() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextFont()
     * 
     */
    public Font getTextFont() { return Fonts.DefaultFont; }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextForeground()
     * 
     */
    public Color getTextForeground() { return TEXT_FG; }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextInsetFactor()
     * 
     */
    public Float getTextInsetFactor() {
        return InsetFactors.LEVEL_2;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getToolTip()
     * 
     */
    public String getToolTip() {
        return null;
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#isFirstInGroup()
     * 
     */
    public Boolean isFirstInGroup() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#hashCode()
     * 
     */
    @Override
    public int hashCode() { return super.hashCode(); }
    
    /**
     * Set the number of child users.
     * 
     * @param users
     *            The number of users.
     */
    public void setNumberOfUsers(final Integer numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(com.thinkparity.ophelia.browser.platform.Platform.Connection, java.awt.Component, java.awt.event.MouseEvent, int, int)
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e) {
        // No popup.
    }
      
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerDoubleClickAction(com.thinkparity.ophelia.browser.application.browser.Browser)
     */
    public void triggerDoubleClickAction(Browser browser) {       
    }
    
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
        if (this.expanded != expand) {
            this.expanded = expand;
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }  
}
