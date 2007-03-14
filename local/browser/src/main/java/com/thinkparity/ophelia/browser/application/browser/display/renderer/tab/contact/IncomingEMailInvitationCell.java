/*
 * Created On: Aug 14, 2006 11:57:59 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.contact.AcceptIncomingEMailInvitation;
import com.thinkparity.ophelia.browser.platform.action.contact.DeclineIncomingEMailInvitation;
import com.thinkparity.ophelia.browser.util.localization.MainCellL18n;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class IncomingEMailInvitationCell extends DefaultInvitationCell {

    /** An <code>IncomingEMailInvitation</code>. */
    private IncomingEMailInvitation invitation;

    /** The invitation cell localization. */
    private final MainCellL18n localization;

    /**
     * Create IncomingInvitationCell.
     * 
     * @param invitation
     *            An <code>IncomingEMailInvitation</code>.
     */
    public IncomingEMailInvitationCell(final IncomingEMailInvitation invitation) {
        super();
        this.invitation = invitation;
        this.localization = new MainCellL18n("IncomingInvitation");
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
        return ((IncomingEMailInvitationCell) obj).invitation.equals(invitation);
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
    public String getTextNoClipping(final TextGroup textGroup) {
        switch (textGroup) {
        case EAST:
            return null;
        case WEST:
            return localization.getString("Text", new Object[] {
                    invitation.getExtendedBy().getName(),
                    FUZZY_DATE_FORMAT.format(invitation.getCreatedOn()) });
        default:
            throw Assert.createUnreachable("Unknown text group.");
        }
    }
    
    /**
     * @see java.lang.Object#hashCode()
     *
     */
    public int hashCode() {
        return invitation.hashCode();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabCell#triggerDoubleClickAction(com.thinkparity.ophelia.browser.application.browser.Browser)
     * 
     */
    public void triggerDoubleClickAction(final Browser browser) {}

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabCell#triggerPopup(com.thinkparity.ophelia.browser.platform.Platform.Connection,
     *      java.awt.Component, java.awt.event.MouseEvent)
     * 
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        switch(connection) {
        case ONLINE:
            final Data acceptData = new Data(1);
            acceptData.set(AcceptIncomingEMailInvitation.DataKey.INVITATION_ID, invitation.getId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_ACCEPT_INCOMING_EMAIL_INVITATION, acceptData));

            final Data declineData = new Data(1);
            declineData.set(DeclineIncomingEMailInvitation.DataKey.INVITATION_ID, invitation.getId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_DECLINE_INCOMING_EMAIL_INVITATION, declineData));
            jPopupMenu.show(invoker, e.getX(), e.getY());
            break;
        case OFFLINE:
            break;
        default:
            Assert.assertUnreachable("UNKNOWN CONECTION");
        }
    }   
}
