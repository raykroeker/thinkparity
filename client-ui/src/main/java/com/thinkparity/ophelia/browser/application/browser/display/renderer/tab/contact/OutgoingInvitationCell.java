/*
 * Created On: Aug 14, 2006 11:57:59 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

import com.thinkparity.codebase.FuzzyDateFormat;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.Constants.DateFormats;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
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
public class OutgoingInvitationCell extends DefaultInvitationCell {

    /** A thinkParity fuzzy date format. */
    private final FuzzyDateFormat fuzzyDateFormat; 

    /** The invitation cell localization. */
    private final MainCellL18n localization;
    
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextNoClipping(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     *
     */
    public String getTextNoClipping(TextGroup textGroup) {
        if (textGroup == TextGroup.WEST) {
            return localization.getString("Text", new Object[] {
                    outgoingInvitation.getEmail(), fuzzyDateFormat.format(outgoingInvitation.getCreatedOn()) });
        } else {
            return null;
        }
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.DefaultInvitationCell#getId()
     */
    @Override
    public Long getId() {
        return outgoingInvitation.getId();
    }
}
