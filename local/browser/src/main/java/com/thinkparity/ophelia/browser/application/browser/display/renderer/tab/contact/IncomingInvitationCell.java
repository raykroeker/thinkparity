/*
 * Created On: Aug 14, 2006 11:57:59 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

import com.thinkparity.codebase.FuzzyDateFormat;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.contact.IncomingInvitation;

import com.thinkparity.ophelia.browser.Constants.DateFormats;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.contact.AcceptIncomingInvitation;
import com.thinkparity.ophelia.browser.platform.action.contact.DeclineIncomingInvitation;
import com.thinkparity.ophelia.browser.util.localization.MainCellL18n;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class IncomingInvitationCell extends DefaultInvitationCell {

    /** A thinkParity fuzzy date format. */
    private final FuzzyDateFormat fuzzyDateFormat;

    /** The incoming invitation associated with this cell. */
    private IncomingInvitation invitation;

    /** The invited by user. */
    private User invitedByUser;
    
    /** The invitation cell localization. */
    private final MainCellL18n localization;

    /**
     * Create IncomingInvitationCell.
     *
     * @param invitation
     * @param invitedByUser
     */
    public IncomingInvitationCell(final IncomingInvitation invitation,
            final User invitedByUser) {
        super();
        this.invitation = invitation;
        this.invitedByUser = invitedByUser;
        this.fuzzyDateFormat = DateFormats.FUZZY;
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
        return ((IncomingInvitationCell) obj).invitation.equals(invitation);
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
     * Obtain the invitedBy
     *
     * @return The User.
     */
    public User getInvitedByUser() {
        return invitedByUser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextNoClipping(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     *
     */
    public String getTextNoClipping(TextGroup textGroup) {
        if (textGroup == TextGroup.WEST) {
            return localization.getString("Text", new Object[] {
                    invitedByUser.getName(), fuzzyDateFormat.format(invitation.getCreatedOn()) });
        } else {
            return null;
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerDoubleClickAction(com.thinkparity.ophelia.browser.application.browser.Browser)
     */
    public void triggerDoubleClickAction(final Browser browser) {}

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(com.thinkparity.ophelia.browser.platform.Platform.Connection, java.awt.Component, java.awt.event.MouseEvent, int, int)
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        switch(connection) {
        case ONLINE:
            final Data acceptData = new Data(1);
            acceptData.set(AcceptIncomingInvitation.DataKey.INVITATION_ID, invitation.getId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_ACCEPT_INCOMING_INVITATION, acceptData));

            final Data declineData = new Data(1);
            declineData.set(DeclineIncomingInvitation.DataKey.INVITATION_ID, invitation.getId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_DECLINE_INCOMING_INVITATION, declineData));
            jPopupMenu.show(invoker, e.getX(), e.getY());
            break;
        case OFFLINE:
            break;
        default:
            Assert.assertUnreachable("UNKNOWN CONECTION");
        }
    }   
}
