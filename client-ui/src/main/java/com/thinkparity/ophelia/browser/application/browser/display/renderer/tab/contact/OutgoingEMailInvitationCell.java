/*
 * Created On: Aug 14, 2006 11:57:59 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.contact.DeleteOutgoingEMailInvitation;
import com.thinkparity.ophelia.browser.util.localization.MainCellL18n;

/**
 * <b>Title:</b>thinkParity OpheliaUI Contact Outgoing EMail Invitation Cell<br>
 * <b>Description:</b><br>
 * 
 * @author robert@thinkparity.com
 * @version 1.1.2.1
 */
public class OutgoingEMailInvitationCell extends DefaultInvitationCell {

    /** The outgoing invitation associated with this cell. */
    private OutgoingEMailInvitation invitation;
    
    /** The invitation cell localization. */
    private final MainCellL18n localization;

    /**
     * Create OutgoingInvitationCell.
     * 
     * @param outgoingInvitation
     */
    public OutgoingEMailInvitationCell(final OutgoingEMailInvitation invitation) {
        super();
        this.invitation = invitation;
        this.localization = new MainCellL18n("OutgoingEMailInvitation");
    }

    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#equals(java.lang.Object)
     * 
     */
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (null == obj)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return ((OutgoingEMailInvitationCell) obj).invitation.equals(invitation);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.DefaultInvitationCell#getId()
     * 
     */
    @Override
    public Long getId() {
        return invitation.getId();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextNoClipping(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     *
     */
    public String getTextNoClipping(TextGroup textGroup) {
        if (textGroup == TextGroup.WEST) {
            return localization.getString("Text", new Object[] {
                    invitation.getInvitationEMail(),
                    FUZZY_DATE_FORMAT.format(invitation.getCreatedOn()) });
        } else {
            return null;
        }
    }

    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#hashCode()
     * 
     */
    public int hashCode() {
        return invitation.hashCode();
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerDoubleClickAction(com.thinkparity.ophelia.browser.application.browser.Browser)
     */
    public void triggerDoubleClickAction(final Browser browser) {}

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(com.thinkparity.ophelia.browser.platform.Platform.Connection,
     *      java.awt.Component, java.awt.event.MouseEvent, int, int)
     * 
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        switch(connection) {
        case ONLINE:
            final Data deleteData = new Data(1);
            deleteData.set(DeleteOutgoingEMailInvitation.DataKey.INVITATION_ID, invitation.getId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_DELETE_OUTGOING_EMAIL_INVITATION, deleteData));
            jPopupMenu.show(invoker, e.getX(), e.getY());
            break;
        case OFFLINE:
            break;
        default:
            Assert.assertUnreachable("UNKNOWN CONECTION");
        }
    }
}
