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
import com.thinkparity.ophelia.browser.application.browser.Browser;
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
public class OutgoingInvitationCell extends InvitationCell implements TabCell {

    /** A thinkParity fuzzy date format. */
    private final FuzzyDateFormat fuzzyDateFormat; 

    /** The invitation cell localization. */
    private final MainCellL18n localization;

    /** A popup menu item factory. */
    private final PopupItemFactory popupItemFactory;
    
    /** The outgoing invitation associated with this cell. */
    private OutgoingInvitation outgoingInvitation;

    /** Create OutgoingInvitationCell. */
    public OutgoingInvitationCell(final OutgoingInvitation outgoingInvitation) {
        super();
        this.outgoingInvitation = new OutgoingInvitation();
        this.outgoingInvitation.setCreatedBy(outgoingInvitation.getCreatedBy());
        this.outgoingInvitation.setCreatedOn(outgoingInvitation.getCreatedOn());
        this.outgoingInvitation.setEmail(outgoingInvitation.getEmail());
        this.outgoingInvitation.setId(outgoingInvitation.getId());
        this.fuzzyDateFormat = DateFormats.FUZZY;
        this.localization = new MainCellL18n("OutgoingInvitation");
        this.popupItemFactory = PopupItemFactory.getInstance();
    }
    
    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#equals(java.lang.Object)
     * 
     */
    public boolean equals(final Object obj) {
        if (null != obj && obj instanceof OutgoingInvitationCell) {
            return ((OutgoingInvitationCell) obj).outgoingInvitation.equals(outgoingInvitation);
        }
        return false;
    }
    
    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#hashCode()
     * 
     */
    public int hashCode() { return outgoingInvitation.hashCode(); }

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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getText(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     *
     */
    public String getText(TextGroup textGroup) {
        if (textGroup == TextGroup.MAIN_TEXT) {
            return localization.getString("Text", new Object[] {
                    outgoingInvitation.getEmail(), fuzzyDateFormat.format(outgoingInvitation.getCreatedOn()) });
        } else {
            return null;
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextFont(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     *
     */
    public Font getTextFont(TextGroup textGroup) {
        return Fonts.DefaultFont;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextForeground(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     * 
     */
    public Color getTextForeground(TextGroup textGroup) {
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
        return getText(TextGroup.MAIN_TEXT);
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
            deleteData.set(DeleteOutgoingInvitation.DataKey.INVITATION_ID, outgoingInvitation.getId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_DELETE_OUTGOING_INVITATION, deleteData));
            jPopupMenu.show(invoker, e.getX(), e.getY());
            break;
        case OFFLINE:
            break;
        default:
            Assert.assertUnreachable("UNKNOWN CONECTION");
        }
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
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#setExpanded(java.lang.Boolean)
     */
    public Boolean setExpanded(Boolean expand) {
        return Boolean.FALSE;
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#setMouseOver(java.lang.Boolean)
     */
    public void setMouseOver(Boolean mouseOver) {
        return;        
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.InvitationCell#getId()
     */
    @Override
    public Long getId() {
        return outgoingInvitation.getId();
    }
}
