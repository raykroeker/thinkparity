/**
 * Created On: Jan 2, 2007 4:56:03 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.contact;

import java.awt.event.ActionEvent;
import java.util.List;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.MainTitleAvatar;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.LinkAction;
import com.thinkparity.ophelia.model.contact.IncomingInvitation;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class DisplayContactInvitationInfo extends AbstractAction {
    
    /** The browser application. */
    private final Browser browser;
    
    /** An instance of the link action. */
    private final DisplayContactInvitationInfoLink displayContactInvitationInfoLink;
    
    /**
     * Create a DisplayContactInvitationInfo.
     * 
     * @param browser
     *            The browser application.
     */
    public DisplayContactInvitationInfo(final Browser browser) {
        super(ActionId.CONTACT_DISPLAY_INVITATION_INFO);
        this.browser = browser;
        this.displayContactInvitationInfoLink = new DisplayContactInvitationInfoLink(browser);
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final List<IncomingInvitation> invitations = getContactModel().readIncomingInvitations();
        final int numberInvitations = invitations.size();
        final Long firstInvitationId;
        if (numberInvitations > 0) {
            firstInvitationId = invitations.get(0).getId();
        } else {
            firstInvitationId = null; 
        }

        displayContactInvitationInfoLink.setFirstInvitationId(firstInvitationId);
        displayContactInvitationInfoLink.setNumberInvitations(numberInvitations);
        browser.setStatus(displayContactInvitationInfoLink);                   
    }
    
    public class DisplayContactInvitationInfoLink implements LinkAction {
        
        /** The browser application. */
        private final Browser browser;
        
        /** The invitation Id */
        private Long firstInvitationId;
        
        /** The number of invitations */
        private int numberInvitations;
        
        public DisplayContactInvitationInfoLink(final Browser browser) {
            super();
            this.browser = browser;
        }
        
        /**
         * Set the invitation id.
         * 
         * @param firstInvitationId
         *            The invitation Id.
         */
        public void setFirstInvitationId(final Long firstInvitationId) {
            this.firstInvitationId = firstInvitationId;
        }
        
        /**
         * Set the number of invitations.
         * 
         * @param numberInvitations
         *          The number of invitations.
         */
        public void setNumberInvitations(final int numberInvitations) {
            this.numberInvitations = numberInvitations;
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getAction()
         */
        public javax.swing.Action getAction() {
            return new javax.swing.AbstractAction() {
                public void actionPerformed(final ActionEvent e) {
                    browser.selectTab(MainTitleAvatar.TabId.CONTACT);
                    browser.showContactInvitation(firstInvitationId);
                }
            };
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getActionName()
         */
        public String getActionName() {
            return ("DisplayContactInvitationInfoLink");
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getIntroText()
         */
        public String getIntroText() {
            return localization.getString("ContactInvitationIntro");
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getLinkType()
         */
        public LinkType getLinkType() {
            if (0 == numberInvitations) {
                return LinkType.CLEAR_SHOW_ALWAYS;
            } else {
                return LinkType.SHOW_ALWAYS;
            }
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getLinkText()
         */
        public String getLinkText() {
            if (1 == numberInvitations) {
                return localization.getString("OneInvitation", new Object[] {numberInvitations});
            } else {
                return localization.getString("ManyInvitations", new Object[] {numberInvitations});
            }
        }
    }
}
