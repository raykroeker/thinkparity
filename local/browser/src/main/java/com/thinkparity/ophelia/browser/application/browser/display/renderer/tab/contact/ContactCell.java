/**
 * Created On: 21-Jun-2006 3:08:54 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.contact.Contact;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCellRenderer;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.contact.Delete;
import com.thinkparity.ophelia.browser.platform.action.contact.Read;


/**
 * An extension of a contact that allows the {@link TabCellRenderer} to display
 * a parity contact.
 * 
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContactCell extends DefaultTabCell implements TabCell {
    
    /** The contact associated with this cell. */
    private Contact contact;

    /**
     * Create a CellContact.
     */
    public ContactCell(final Contact contact) {        
        this.contact = new Contact();
        this.contact.setId(contact.getId());
        this.contact.setLocalId(contact.getLocalId());
        this.contact.setName(contact.getName());
        this.contact.setOrganization(contact.getOrganization());
        this.contact.addAllEmails(contact.getEmails());
    }

    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#equals(java.lang.Object)
     * 
     */
    public boolean equals(final Object obj) {
        if (null != obj && obj instanceof ContactCell) {
            return ((ContactCell) obj).contact.equals(contact);
        }
        return false;
    }

    /**
     * Get the contact Id.
     * 
     * @return The contact id.
     */
    public JabberId getId() {
        return contact.getId();
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextNoClipping(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     *
     */
    public String getTextNoClipping(TextGroup textGroup) {
        if (textGroup == TextGroup.MAIN_TEXT) {
            return contact.getName();
        } else {
            return null;
        }
    }

    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#hashCode()
     * 
     */
    public int hashCode() { return contact.hashCode(); }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(com.thinkparity.ophelia.browser.platform.Platform.Connection,
     *      java.awt.Component, java.awt.event.MouseEvent, int, int)
     * 
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();

        final Data readData = new Data(1);
        readData.set(Read.DataKey.CONTACT_ID, contact.getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_READ, readData));

        final Data deleteData = new Data(1);
        deleteData.set(Delete.DataKey.CONTACT_ID, contact.getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_DELETE, deleteData));
        jPopupMenu.show(invoker, e.getX(), e.getY());
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerDoubleClickAction(com.thinkparity.ophelia.browser.application.browser.Browser)
     */
    public void triggerDoubleClickAction(Browser browser) {  
        browser.runReadContact(contact.getId());
    }
}
