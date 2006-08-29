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
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import com.thinkparity.browser.Constants.InsetFactors;
import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentImage;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.browser.platform.Platform.Connection;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.action.container.RemoveDocument;
import com.thinkparity.browser.platform.action.container.RevertDocument;
import com.thinkparity.browser.platform.action.document.Open;
import com.thinkparity.browser.platform.action.document.Rename;
import com.thinkparity.browser.platform.util.l10n.MainCellL18n;

import com.thinkparity.model.parity.model.document.Document;

/**
 * @author rob_masako@shaw.ca, raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class DraftDocumentCell extends Document implements TabCell  {
    
    /** The cell's text foreground color. */
    private static final Color TEXT_FG;

    /** Maximum length of a document cell's text. */
    private static final Integer TEXT_MAX_LENGTH;

    static {
        TEXT_FG = Color.BLACK;
        TEXT_MAX_LENGTH = 60;
    }

    /** The document's draft. */
    private final DraftCell draft;

    /** An image cache. */
    private final MainCellImageCache imageCache;

    /** Localization for the draft document cell. */
    private final MainCellL18n localization;

    /** A popup menu item factory. */
    private final PopupItemFactory popupItemFactory;

    /**
     * Create MainCellDraftDocument.
     * 
     * @param draftDisplay
     *            A draft display cell.
     * @param document
     *            A document.
     */
    public DraftDocumentCell(final DraftCell draft, final Document document) {
        super(document.getCreatedBy(), document.getCreatedOn(), document.getDescription(),
                document.getFlags(), document.getUniqueId(), document.getName(), document.getUpdatedBy(),
                document.getUpdatedOn());
        setId(document.getId());
        setRemoteInfo(document.getRemoteInfo());
        setState(document.getState());
        this.draft = draft;
        this.imageCache = new MainCellImageCache();
        this.localization = new MainCellL18n("DraftDocumentCell");
        this.popupItemFactory = PopupItemFactory.getInstance();
    }

    /**
     * @see com.thinkparity.model.parity.model.artifact.Artifact#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (null != obj && obj instanceof DraftDocumentCell) {
            return ((DraftDocumentCell) obj).getId().equals(getId()) &&
                ((DraftDocumentCell) obj).draft.getContainerId().equals(draft.getContainerId());
        }
        return false;
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
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getBorder(int)
     */
    public Border getBorder(final int index) {
        return getParent().getBorder(index);
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
     */
    public TabCell getParent() {
        return draft;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getText()
     */
    public String getText() {
        final String messageKey = new StringBuffer("Text.")
                .append(draft.getState(getId())).toString();
        return getString(messageKey, getName());
    }
    
    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getSecondaryText()
     * 
     */
    public String getSecondaryText() {
        return null;
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
     * @see com.thinkparity.model.parity.model.artifact.Artifact#hashCode()
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
                .append(getId()).append("/")
                .append(draft.getContainerId())
                .toString();
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(com.thinkparity.browser.platform.Platform.Connection, java.awt.Component, java.awt.event.MouseEvent, int, int)
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e, final int x,
            final int y) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();

        final Data openData = new Data(1);
        openData.set(Open.DataKey.DOCUMENT_ID, getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.DOCUMENT_OPEN, openData));

        final Data renameData = new Data(1);
        renameData.set(Rename.DataKey.DOCUMENT_ID, getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.DOCUMENT_RENAME, renameData));

        jPopupMenu.addSeparator();

        final Data revertData = new Data(2);
        revertData.set(RevertDocument.DataKey.CONTAINER_ID, draft.getContainerId());
        revertData.set(RevertDocument.DataKey.DOCUMENT_ID, getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_REVERT_DOCUMENT, revertData));

        final Data removeData = new Data(1);
        removeData.set(RemoveDocument.DataKey.CONTAINER_ID, draft.getContainerId());
        removeData.set(RemoveDocument.DataKey.DOCUMENT_ID, getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_REMOVE_DOCUMENT, removeData));
        jPopupMenu.show(invoker, x, y);

    }

    /**
     * Obtain localized text.
     * 
     * @param messagKey
     *            The message key.
     * @param messageArguments
     *            The message arguments.
     * @return Localized text.
     */
    private String getString(final String messageKey,
            final Object... messageArguments) {
        return localization.getString(messageKey, messageArguments);
    }
}
