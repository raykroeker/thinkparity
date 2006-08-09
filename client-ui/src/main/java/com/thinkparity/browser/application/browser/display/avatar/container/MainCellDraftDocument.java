/**
 * Created On: 17-Jul-06 1:57:42 PM
 */
package com.thinkparity.browser.application.browser.display.avatar.container;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.border.Border;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCell;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentImage;

import com.thinkparity.model.parity.model.document.Document;

/**
 * @author rob_masako@shaw.ca, raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class MainCellDraftDocument extends Document implements MainCell  {
    
    /** The cell's text foreground color. */
    private static final Color TEXT_FG;
    
    /** The cell's text foreground colour for closed documents. */
    private static final Color TEXT_FG_CLOSED;

    /** Maximum length of a document cell's text. */
    private static final Integer TEXT_MAX_LENGTH;

    static {
        TEXT_FG = Color.BLACK;
        TEXT_FG_CLOSED = new Color(127, 131, 134, 255);

        TEXT_MAX_LENGTH = 60;
    }

    /** The document's draft. */
    private final MainCellDraft draftDisplay;
    
    /** An image cache. */
    private final MainCellImageCache imageCache;    

    /**
     * Create MainCellDraftDocument.
     * 
     * @param draftDisplay
     *            A draft display cell.
     * @param document
     *            A document.
     */
    public MainCellDraftDocument(final MainCellDraft draftDisplay,
            final Document document) {
        super(document.getCreatedBy(), document.getCreatedOn(), document.getDescription(),
                document.getFlags(), document.getUniqueId(), document.getName(), document.getUpdatedBy(),
                document.getUpdatedOn());
        setId(document.getId());
        setRemoteInfo(document.getRemoteInfo());
        setState(document.getState());
        this.draftDisplay = draftDisplay;
        this.imageCache = new MainCellImageCache(); 
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#canImport()
     */
    public boolean canImport() { return false; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBackground()
     */
    public BufferedImage getBackground() {
        return imageCache.read(DocumentImage.BG_DEFAULT);
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBackgroundSelected()
     */
    public BufferedImage getBackgroundSelected() {
        return imageCache.read(DocumentImage.BG_SEL_DEFAULT);
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBorder()
     */
    public Border getBorder() {
        return null;
    }

    /**
     * Obtain the container display cell.
     * 
     * @return The container display cell.
     * @see MainCellDraftDocument#getDraftDisplay()
     */
    public MainCellContainer getContainerDisplay() {
        return getDraftDisplay().getContainerDisplay();
    }

    /**
     * Get the parent container id
     */
    public Long getContainerId() { return draftDisplay.getContainerId(); }

    /**
     * Obtain the draft display cell.
     * 
     * @return The draft display cell.
     */
    public MainCellDraft getDraftDisplay() { return draftDisplay; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getInfoIcon()
     */
    public ImageIcon getInfoIcon() {
        return null;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getNodeIcon()
     */
    public ImageIcon getNodeIcon() {
        return null;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getNodeIconSelected()
     */
    public ImageIcon getNodeIconSelected() {
        return null;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getText()
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
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextFont()
     */
    public Font getTextFont() {
        return BrowserConstants.Fonts.DefaultFont;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextForeground()
     */
    public Color getTextForeground() {
        return TEXT_FG;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextInsetFactor()
     */
    public Float getTextInsetFactor() { return 6.0F; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getToolTip()
     */
    public String getToolTip() {
        if(TEXT_MAX_LENGTH < getName().length()) { return getName(); }
        else { return null; }
    }
}
