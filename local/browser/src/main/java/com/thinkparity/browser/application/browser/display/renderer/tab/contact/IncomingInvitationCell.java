/*
 * Created On: Aug 14, 2006 11:57:59 AM
 */
package com.thinkparity.browser.application.browser.display.renderer.tab.contact;

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

import com.thinkparity.browser.Constants.DateFormats;
import com.thinkparity.browser.Constants.InsetFactors;
import com.thinkparity.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.browser.platform.Platform.Connection;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.action.contact.AcceptIncomingInvitation;
import com.thinkparity.browser.platform.action.contact.DeclineIncomingInvitation;
import com.thinkparity.browser.platform.util.l10n.MainCellL18n;

import com.thinkparity.model.parity.model.contact.IncomingInvitation;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class IncomingInvitationCell extends IncomingInvitation implements TabCell {

    /** A thinkParity fuzzy date format. */
    private final FuzzyDateFormat fuzzyDateFormat;

    /** The invited by user. */
    private User invitedByUser;

    /** The invitation cell localization. */
    private final MainCellL18n localization;

    /** A popup menu item factory. */
    private final PopupItemFactory popupItemFactory;

    /** Create IncomingInvitationCell. */
    public IncomingInvitationCell() {
        super();
        this.fuzzyDateFormat = DateFormats.FUZZY;
        this.localization = new MainCellL18n("IncomingInvitation");
        this.popupItemFactory = PopupItemFactory.getInstance();
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getBackground()
     */
    public BufferedImage getBackground() {
        return null;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getBackgroundSelected()
     */
    public BufferedImage getBackgroundSelected() {
        return null;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getBorder(int)
     */
    public Border getBorder(int index) {
        return null;
    }

    /**
     * Obtain the invitedBy
     *
     * @return The User.
     */
    public User getInvitedByUser() {
        return invitedByUser;
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
        return null;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getText()
     */
    public String getText() {
        return localization.getString("Text", new Object[] {
                invitedByUser.getName(), fuzzyDateFormat.format(createdOn) });
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
        return Fonts.DefaultFont;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getTextForeground()
     */
    public Color getTextForeground() {
        return null;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getTextInsetFactor()
     */
    public Float getTextInsetFactor() {
        return InsetFactors.LEVEL_0;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getToolTip()
     */
    public String getToolTip() {
        return getText();
    }

    /**
     * Set invitedBy.
     *
     * @param invitedBy The User.
     */
    public void setInvitedByUser(final User invitedBy) {
        this.invitedByUser = invitedBy;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(com.thinkparity.browser.platform.Platform.Connection, java.awt.Component, java.awt.event.MouseEvent, int, int)
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e, final int x,
            final int y) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        switch(connection) {
        case ONLINE:
            final Data acceptData = new Data(1);
            acceptData.set(AcceptIncomingInvitation.DataKey.INVITATION_ID, getId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_ACCEPT_INCOMING_INVITATION, acceptData));

            final Data declineData = new Data(1);
            declineData.set(DeclineIncomingInvitation.DataKey.INVITATION_ID, getId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_DECLINE_INCOMING_INVITATION, declineData));

            jPopupMenu.show(invoker, x, y);
            break;
        case OFFLINE:
            break;
        default:
            Assert.assertUnreachable("UNKNOWN CONECTION");
        }
    }
}
