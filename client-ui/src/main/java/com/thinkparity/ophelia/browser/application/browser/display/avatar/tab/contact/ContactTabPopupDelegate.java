/*
 * Created On:  Saturday Dec 9 2006, 21:48
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.thinkparity.codebase.model.contact.Contact;

import com.thinkparity.ophelia.model.contact.IncomingInvitation;
import com.thinkparity.ophelia.model.contact.OutgoingInvitation;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarPopupDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact.ContactTabModel.Ordering;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ContactTabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.PopupDelegate;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.DefaultPopupDelegate;
import com.thinkparity.ophelia.browser.platform.action.contact.AcceptIncomingInvitation;
import com.thinkparity.ophelia.browser.platform.action.contact.DeclineIncomingInvitation;
import com.thinkparity.ophelia.browser.platform.action.contact.Delete;
import com.thinkparity.ophelia.browser.platform.action.contact.DeleteOutgoingInvitation;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ContactTabPopupDelegate extends DefaultPopupDelegate implements
        PopupDelegate, TabAvatarPopupDelegate {

    /** The <code>ContactTabModel</code>. */
    private final ContactTabModel model;

    /**
     * Create ContactTabPopupDelegate.
     * 
     * @param model
     *            The <code>ContactTabModel</code>.
     */
    ContactTabPopupDelegate(final ContactTabModel model) {
        super();
        this.model = model;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.PopupDelegate#showForContact(com.thinkparity.codebase.model.contact.Contact)
     * 
     */
    public void showForContact(final Contact contact) {
        final Data deleteData = new Data(1);
        deleteData.set(Delete.DataKey.CONTACT_ID, contact.getId());
        add(ActionId.CONTACT_DELETE, deleteData);
        show();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.PopupDelegate#showForInvitation(com.thinkparity.ophelia.model.contact.IncomingInvitation)
     *
     */
    public void showForInvitation(final IncomingInvitation invitation) {
        if (isOnline()) {
            final Data acceptData = new Data(1);
            acceptData.set(AcceptIncomingInvitation.DataKey.INVITATION_ID, invitation.getId());
            add(ActionId.CONTACT_ACCEPT_INCOMING_INVITATION, acceptData);

            final Data declineData = new Data(1);
            declineData.set(DeclineIncomingInvitation.DataKey.INVITATION_ID, invitation.getId());
            add(ActionId.CONTACT_DECLINE_INCOMING_INVITATION, declineData);
            show();
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.PopupDelegate#showForInvitation(com.thinkparity.ophelia.model.contact.OutgoingInvitation)
     *
     */
    public void showForInvitation(final OutgoingInvitation invitation) {
        if (isOnline()) {
            final Data deleteData = new Data(1);
            deleteData.set(DeleteOutgoingInvitation.DataKey.INVITATION_ID, invitation.getId());
            add(ActionId.CONTACT_DELETE_OUTGOING_INVITATION, deleteData);
            show();
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanelPopupDelegate#showForPanel(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     * 
     */
    public void showForPanel(final TabPanel tabPanel) {
        showForContact(((ContactTabPanel) tabPanel).getContact());
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarPopupDelegate#showForSort()
     * 
     */
    public void showForSort() {
        for (final Ordering ordering : Ordering.values()) {
            add(ordering);
        }
        show();
    }

    private void add(final Ordering ordering) {
        final JMenuItem item = new JMenuItem(model.getString(ordering));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (isSortApplied(ordering)) {
                    if (ordering.isAscending()) {
                        model.applySort(ordering, Boolean.FALSE);
                    } else {
                        model.removeSort(ordering);
                    }
                } else {
                    model.applySort(ordering, Boolean.TRUE);
                }
            }
        });
        add(item);
    }

    /**
     * Determine if the ordering is currently applied.
     * 
     * @param ordering
     *            An <code>Ordering</code>.
     * @return True if it is applied.
     */
    private boolean isSortApplied(final Ordering ordering) {
        return model.isSortApplied(ordering);
    }

    /**
     * Determine whether or not we are online.
     * 
     * @return True if we are online.
     */
    private boolean isOnline() {
        return model.isOnline().booleanValue();
    }
}
