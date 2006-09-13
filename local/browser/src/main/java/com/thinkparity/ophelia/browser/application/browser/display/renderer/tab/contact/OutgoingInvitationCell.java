/*
 * Created On: Aug 14, 2006 11:57:59 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import com.thinkparity.codebase.FuzzyDateFormat;
import com.thinkparity.codebase.assertion.Assert;


import com.thinkparity.ophelia.browser.Constants.DateFormats;
import com.thinkparity.ophelia.browser.Constants.InsetFactors;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.contact.DeleteOutgoingInvitation;
import com.thinkparity.ophelia.browser.util.localization.MainCellL18n;
import com.thinkparity.ophelia.model.contact.OutgoingInvitation;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class OutgoingInvitationCell extends OutgoingInvitation implements TabCell {

    /** A thinkParity fuzzy date format. */
    private final FuzzyDateFormat fuzzyDateFormat; 

    /** The invitation cell localization. */
    private final MainCellL18n localization;

    /** A popup menu item factory. */
    private final PopupItemFactory popupItemFactory;

    /** Create OutgoingInvitationCell. */
    public OutgoingInvitationCell() {
        super();
        this.fuzzyDateFormat = DateFormats.FUZZY;
        this.localization = new MainCellL18n("OutgoingInvitation");
        this.popupItemFactory = PopupItemFactory.getInstance();
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
    public Border getBorder(int index, final Boolean isFirstInGroup, final Boolean lastCell) {
        return null;
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getText()
     */
    public String getText() {
        return localization.getString("Text", new Object[] {
                getEmail(), fuzzyDateFormat.format(getCreatedOn()) });
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
     */
    public Font getTextFont() {
        return Fonts.DefaultFont;
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
     * 
     */
    public Boolean isFirstInGroup() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(com.thinkparity.ophelia.browser.platform.Platform.Connection, java.awt.Component, java.awt.event.MouseEvent, int, int)
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        switch(connection) {
        case ONLINE:
            final Data deleteData = new Data(1);
            deleteData.set(DeleteOutgoingInvitation.DataKey.INVITATION_ID, getId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_DELETE_OUTGOING_INVITATION, deleteData));
            jPopupMenu.show(invoker, e.getX(), e.getY());
            break;
        case OFFLINE:
            break;
        default:
            Assert.assertUnreachable("UNKNOWN CONECTION");
        }
    }
}
