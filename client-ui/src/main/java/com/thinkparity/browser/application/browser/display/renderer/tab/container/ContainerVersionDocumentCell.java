
/**
 * Created On: 17-Jul-06 1:57:42 PM
 */
package com.thinkparity.browser.application.browser.display.renderer.tab.container;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.border.Border;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.Constants.InsetFactors;
import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentImage;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.browser.platform.Platform.Connection;

import com.thinkparity.model.parity.model.document.Document;

/**
 * @author rob_masako@shaw.ca, raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class ContainerVersionDocumentCell extends Document implements TabCell  {
    
    /** The cell's text foreground color. */
    private static final Color TEXT_FG;

    /** Maximum length of a document cell's text. */
    private static final Integer TEXT_MAX_LENGTH;

    static {
        TEXT_FG = Color.BLACK;
        TEXT_MAX_LENGTH = 60;
    }

    /** An image cache. */
    private final MainCellImageCache imageCache;

    /** The document's version. */
    private final ContainerVersionCell version;

    /** Create a CellDocument. */
    public ContainerVersionDocumentCell(final ContainerVersionCell version, final Document document) {
        super(document.getCreatedBy(), document.getCreatedOn(), document.getDescription(),
                document.getFlags(), document.getUniqueId(), document.getName(), document.getUpdatedBy(),
                document.getUpdatedOn());
        setId(document.getId());
        setRemoteInfo(document.getRemoteInfo());
        setState(document.getState());
        this.version = version;
        this.imageCache = new MainCellImageCache();
    }
    
    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getBackground()
     */
    public BufferedImage getBackground() {
        return imageCache.read(DocumentImage.BG_DEFAULT);
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getBackgroundSelected()
     */
    public BufferedImage getBackgroundSelected() {
        return imageCache.read(DocumentImage.BG_SEL_DEFAULT);
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getBorder()
     */
    public Border getBorder() {
        return null;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getInfoIcon()
     */
    public ImageIcon getInfoIcon() {
        return null;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getNodeIcon()
     */
    public ImageIcon getNodeIcon() {
        return null;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getNodeIconSelected()
     */
    public ImageIcon getNodeIconSelected() {
        return null;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getParent()
     * 
     */
    public TabCell getParent() {
        return version;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getText()
     */
    public String getText() {
        if(TEXT_MAX_LENGTH < getName().length()) {
            return getName().substring(0, TEXT_MAX_LENGTH - 1 - 3) + "...";
        }
        else {
            return getName();
        }
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getTextFont()
     */
    public Font getTextFont() {
        return BrowserConstants.Fonts.DefaultFont;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getTextForeground()
     */
    public Color getTextForeground() {
        return TEXT_FG;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getTextInsetFactor()
     */
    public Float getTextInsetFactor() {
        return InsetFactors.LEVEL_2;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getToolTip()
     */
    public String getToolTip() {
        if(TEXT_MAX_LENGTH < getName().length()) { return getName(); }
        else { return null; }
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(com.thinkparity.browser.platform.Platform.Connection, java.awt.Component, java.awt.event.MouseEvent, int, int)
     */
    public void triggerPopup(Connection connection, Component invoker, MouseEvent e, int x, int y) {
        throw Assert.createNotYetImplemented("ContainerVersionDocumentCell#triggerPopup");
    }
}
