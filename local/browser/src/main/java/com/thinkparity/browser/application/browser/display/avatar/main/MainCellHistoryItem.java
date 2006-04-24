/*
 * Apr 12, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.HistoryItemIcon;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.HistoryItemImage;
import com.thinkparity.browser.platform.util.l10n.MainCellL18n;

import com.thinkparity.model.parity.model.document.history.HistoryItem;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MainCellHistoryItem extends HistoryItem implements MainCell {

    /** The history item count. */
    private final Integer count;

    /** The history item's document. */
    private final MainCellDocument document;

    /** The document team. */
    private final Set<User> documentTeam;

    /** A flag indicating the group selection status. */
    private Boolean groupSelected = Boolean.FALSE;

    /** An image cache. */
    private final MainCellImageCache imageCache;

    /** The index of the history item in the list. */
    private final Integer index;

    /** Localisation access. */
    private final MainCellL18n l18n;

    /**
     * Create a MainCellHistoryItem.
     * 
     */
    public MainCellHistoryItem(final MainCellDocument document,
            final HistoryItem historyItem, final Set<User> documentTeam,
            final Integer count, final Integer index) {
        super();
        this.document = document;
        this.documentTeam = documentTeam;
        this.count = count;
        this.imageCache = new MainCellImageCache();
        this.index = index;
        this.l18n = new MainCellL18n("HistoryListItem");
        setDate(historyItem.getDate());
        setDocumentId(historyItem.getDocumentId());
        setEvent(historyItem.getEvent());
        setVersionId(historyItem.getVersionId());
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#canImport()
     * 
     */
    public boolean canImport() { return false; }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     * 
     */
    public boolean equals(final Object obj) {
        if(null != obj && obj instanceof MainCellHistoryItem) {
            return ((MainCellHistoryItem) obj).index.equals(index) &&
                ((MainCellHistoryItem) obj).document.equals(document);
        }
        return false;
    }


    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBackground()
     * 
     */
    public BufferedImage getBackground() {
        if(document.isClosed()) { return imageCache.read(HistoryItemImage.BG_CLOSED); }
        else if(document.isUrgent()) { return imageCache.read(HistoryItemImage.BG_URGENT); }
        else { return imageCache.read(HistoryItemImage.BG_DEFAULT); }
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBackgroundSelected()
     * 
     */
    public BufferedImage getBackgroundSelected() {
        if(document.isClosed()) { return imageCache.read(HistoryItemImage.BG_SEL_CLOSED); }
        else if(document.isUrgent()) { return imageCache.read(HistoryItemImage.BG_SEL_URGENT); }
        else { return imageCache.read(HistoryItemImage.BG_SEL_DEFAULT); }
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBorder()
     * 
     */
    public Border getBorder() {
        return BorderFactory.createEmptyBorder();
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBorderSelected()
     *
     */
     public Border getBorderSelected() { return BorderFactory.createLineBorder(BrowserConstants.Colours.MAIN_CELL_DEFAULT_BORDER2); }

    /**
     * Obtain the document.
     * 
     * @return The main cell document.
     */
    public MainCellDocument getDocument() { return document; }

    /**
     * Obtain the document team.
     * 
     * @return The document team.
     */
    public Set<User> getDocumentTeam() { return documentTeam; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getInfoIcon()
     * 
     */
    public ImageIcon getInfoIcon() {
        return imageCache.read(HistoryItemIcon.INFO_DEFAULT);
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getNodeIcon()
     * 
     */
    public ImageIcon getNodeIcon() {
        return imageCache.read(HistoryItemIcon.NODE_DEFAULT);
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getNodeIconSelected()
     * 
     */
    public ImageIcon getNodeIconSelected() {
        return imageCache.read(HistoryItemIcon.NODE_DEFAULT_SELECTED);
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getText()
     * 
     */
    public String getText() {
        final String localKey;
        final Object[] arguments;
        if(isSetVersionId()) {
            localKey = "ItemVersionText";
            arguments = new Object[] {
                getDate().getTime(),
                getVersionId(),
                getEvent()
            };
        }
        else {
            localKey = "ItemText";
            arguments = new Object[] {
                getDate().getTime(),
                getEvent()
            };
        }
        return l18n.getString(localKey, arguments);
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextFont()
     * 
     */
    public Font getTextFont() { return BrowserConstants.SmallFont; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextForeground()
     * 
     */
    public Color getTextForeground() { return document.getTextForeground(); }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextInsetFactor()
     * 
     */
    public Float getTextInsetFactor() { return 3.0F; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getToolTip()
     */
    public String getToolTip() { return null; }

    /**
     * @see java.lang.Object#hashCode()
     * 
     */
    public int hashCode() { return index.hashCode() & document.hashCode(); }

    public Boolean isGroupSelected() { return groupSelected; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#populatePopupMenu(java.awt.event.MouseEvent,
     *      javax.swing.JPopupMenu)
     * 
     */
    public void populatePopupMenu(MouseEvent e, JPopupMenu jPopupMenu) {}

    public void setGroupSelected(final Boolean groupSelected) {
        this.groupSelected = groupSelected;
    }

    /**
     * Determine whether or not this item is the last item.
     *
     * @return True if this is the last item; false otherwise.
     */
    private Boolean isLastItem() { return index == count - 1; }
}
