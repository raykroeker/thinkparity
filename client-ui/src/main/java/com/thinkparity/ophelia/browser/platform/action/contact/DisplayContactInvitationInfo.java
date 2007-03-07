/**
 * Created On: Jan 2, 2007 4:56:03 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.contact;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.MainTitleAvatar;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.AbstractLinkAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.model.contact.IncomingInvitation;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class DisplayContactInvitationInfo extends AbstractBrowserAction {
    
    /** The browser application. */
    private final Browser browser;
    
    /** An instance of the link action. */
    private final DisplayContactInvitationInfoLink displayContactInvitationInfoLink;
    
    /** A user search expression <code>String</code>. */
    private String searchExpression;
    
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
        final List<IncomingInvitation> invitations;
        searchExpression = (String) data.get(DataKey.SEARCH_EXPRESSION);
        
        // Prepare the list of incoming invitations
        if (isSearchApplied()) {
            // TODO At the moment search in contacts tab hides all invitations. When this
            // is fixed in contacts tab, fix here too.
            invitations = Collections.emptyList();
        } else {
            invitations = getContactModel().readIncomingInvitations();
        }

        // Build the list of invitation ids
        final List<Long> invitationIds = new ArrayList<Long>();
        for (final IncomingInvitation invitation : invitations) {
            invitationIds.add(invitation.getId());
        }

        displayContactInvitationInfoLink.setInvitationIds(invitationIds);
        browser.setStatus(displayContactInvitationInfoLink);                   
    }
    
    /**
     * Determine if a search is applied.
     * 
     * @return True if a search expression is set.
     */
    private boolean isSearchApplied() { 
        return null != searchExpression;
    }  
    
    public class DisplayContactInvitationInfoLink extends AbstractLinkAction {
        
        /** The browser application. */
        private final Browser browser;
        
        /** The list of not-seen containers. */
        private List<Long> invitationIds;  
        
        public DisplayContactInvitationInfoLink(final Browser browser) {
            super();
            this.browser = browser;
        }
        
        /**
         * Set the list of invitation ids.
         */
        public void setInvitationIds(final List<Long> invitationIds) {
            this.invitationIds = invitationIds;
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getAction()
         */
        public javax.swing.Action getAction() {
            return new javax.swing.AbstractAction() {
                public void actionPerformed(final ActionEvent e) {
                    browser.selectTab(MainTitleAvatar.TabId.CONTACT);
                    browser.showContactInvitation(invitationIds, 0);
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
        public String getIntroText(final Boolean displayedFirst) {
            if (displayedFirst) {
                return localization.getString("ContactInvitationIntro");
            } else {
                return localization.getString("ContactInvitationIntroNotFirst");     
            }
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getLinkType()
         */
        public LinkType getLinkType() {
            if (0 == invitationIds.size()) {
                return LinkType.CLEAR;
            } else {
                return LinkType.SHOW_ALWAYS;
            }
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getLinkText()
         */
        public String getLinkText() {
            if (1 == invitationIds.size()) {
                return localization.getString("OneInvitation", new Object[] {invitationIds.size()});
            } else {
                return localization.getString("ManyInvitations", new Object[] {invitationIds.size()});
            }
        }
        
        /**
         * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getPriority()
         */
        public LinkPriority getPriority() {
            return LinkPriority.LOW;
        }  
    }
    
    public enum DataKey { SEARCH_EXPRESSION }
}
