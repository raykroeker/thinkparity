/**
 * Created On: 14-Sep-06 11:28:47 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.browser.Constants.InsetFactors;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.contact.Read;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContainerVersionSentToUserCell extends DefaultTabCell implements TabCell {
    
    /** The user's parent cell. */
    private final ContainerVersionSentToCell versionSentTo;
    
    /** The user's parent version (grandparent cell). */
    private final ContainerVersionCell version;
    
    /** The user associated with this cell. */
    private User user;

    /** Create ContainerVersionSentToCell. */
    public ContainerVersionSentToUserCell(final ContainerVersionSentToCell versionSentTo, final User user) { 
        this.user = new User();
        this.user.setId(user.getId());
        this.user.setLocalId(user.getLocalId());
        this.user.setName(user.getName());
        this.user.setOrganization(user.getOrganization());
        this.user.setTitle(user.getTitle());
        this.versionSentTo = versionSentTo;
        this.version = (ContainerVersionCell) versionSentTo.getParent();
    }
    
    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#equals(java.lang.Object)
     * 
     */
    public boolean equals(final Object obj) {
        if (null != obj && obj instanceof ContainerVersionSentToUserCell) {           
            return ((ContainerVersionSentToUserCell) obj).user.equals(user) &&
                   ((ContainerVersionSentToUserCell) obj).version.equals(version);
        }
        return false;
    }
    
    /**
     * Get the user Id.
     * 
     * @return The user id.
     */
    public JabberId getId() {
        return user.getId();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getBorder(int)
     * 
     */
    public Border getBorder(final int index, final Boolean isFirstInGroup, final Boolean lastCell) {
        return getParent().getBorder(index, isFirstInGroup, lastCell);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getParent()
     * 
     */
    public TabCell getParent() {
        return versionSentTo;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextNoClipping(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     *
     */
    public String getTextNoClipping(TextGroup textGroup) {
        if (textGroup == TextGroup.MAIN_TEXT) {
            String text = new String(user.getName());
            if (user.isSetTitle() || user.isSetOrganization()) {
                text += "  (";
                if (user.isSetTitle()) {
                    text += user.getTitle();
                    if (user.isSetOrganization()) {
                        text += ", ";
                    }
                }
                if (user.isSetOrganization()) {
                    text += user.getOrganization();
                }
                text += ")";
            }
            return text;
        } else {
            return null;
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextInsetFactor()
     * 
     */
    public Float getTextInsetFactor() {
        return InsetFactors.LEVEL_3;
    }

    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#hashCode()
     * 
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
            .append(user.getId()).append("/")
            .append(version.getArtifactId()).append("/")
            .append(version.getVersionId())
            .toString();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(com.thinkparity.ophelia.browser.platform.Platform.Connection, java.awt.Component, java.awt.event.MouseEvent, int, int)
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();

        final Data readData = new Data(1);
        readData.set(Read.DataKey.CONTACT_ID, user.getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_READ, readData));

        jPopupMenu.show(invoker, e.getX(), e.getY());
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerDoubleClickAction(com.thinkparity.ophelia.browser.application.browser.Browser)
     */
    public void triggerDoubleClickAction(Browser browser) {  
        browser.runReadContact(user.getId());
    }
}
