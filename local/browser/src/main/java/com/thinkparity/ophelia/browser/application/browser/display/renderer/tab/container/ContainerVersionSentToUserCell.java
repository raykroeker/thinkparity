/**
 * Created On: 14-Sep-06 11:28:47 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;

import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.browser.Constants.InsetFactors;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCacheTest.TabCellIconTest;
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
public class ContainerVersionSentToUserCell extends DefaultTabCell {
    
    /** The parent cell. */
    private TabCell parent;
    
    /** An <code>ArtifactReceipt</code>. */
    private ArtifactReceipt receipt;

    /** A <code>User</code>.*/
    private User user;

    /** Create ContainerVersionSentToCell. */
    public ContainerVersionSentToUserCell() { 
        super();
    }

    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#equals(java.lang.Object)
     * 
     */
    public boolean equals(final Object obj) {
        if (null != obj && obj instanceof ContainerVersionSentToUserCell) {
            final ContainerVersionSentToUserCell cell = (ContainerVersionSentToUserCell) obj;
            return cell.user.equals(user) && cell.parent.equals(parent);
        }
        return false;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getBorder(int)
     * 
     */
    public Border getBorder(final int index, final Boolean isFirstInGroup, final Boolean lastCell) {
        return getParent().getBorder(index, isFirstInGroup, lastCell);
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getParent()
     * 
     */
    public TabCell getParent() {
        return parent;
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getSecondNodeIcon()
     */
    public ImageIcon getSecondNodeIcon() {
        return imageCacheTest.read(TabCellIconTest.CONTACT); 
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextInsetFactor()
     * 
     */
    public Float getTextInsetFactor() {
        return InsetFactors.LEVEL_3;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextNoClipping(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     *
     */
    public String getTextNoClipping(final TextGroup textGroup) {
        final StringBuffer textNoClipping;
        switch(textGroup) {
        case WEST:
            textNoClipping = new StringBuffer();
            if (user.isSetOrganization() && user.isSetTitle()) {
                textNoClipping.append(MessageFormat.format("{0} ({1}, {2})",
                            user.getName(), user.getOrganization(), user.getTitle()));
            } else if (user.isSetOrganization()) {
                textNoClipping.append(MessageFormat.format("{0} ({1})",
                            user.getName(), user.getOrganization()));
            } else if (user.isSetTitle()) {
                textNoClipping.append(MessageFormat.format("{0} ({1})",
                            user.getName(), user.getTitle()));
            } else {
                textNoClipping.append(user.getName());
            }
            if (null != receipt) {
                textNoClipping.append(MessageFormat.format(" - {0,date,yyyy-MM-dd HH:mm:ss.SSS}",
                        receipt.getReceivedOn().getTime()));
            }
            break;
        case EAST:
            textNoClipping = null;
            break;
        default:
            throw Assert.createUnreachable("UNKNOWN TEXT GROUP");
        }
        return null == textNoClipping ? null : textNoClipping.toString();
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
     * Set parent.
     *
     * @param parent The TabCell.
     */
    public void setParent(final TabCell parent) {
        this.parent = parent;
    }

    /**
     * Set receipt.
     *
     * @param receipt The ArtifactReceipt.
     */
    public void setReceipt(final ArtifactReceipt receipt) {
        this.receipt = receipt;
    }

    /**
     * Set user.
     *
     * @param user The User.
     */
    public void setUser(final User user) {
        this.user = user;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
            .append(user)
            .toString();
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerDoubleClickAction(com.thinkparity.ophelia.browser.application.browser.Browser)
     */
    public void triggerDoubleClickAction(Browser browser) {  
        browser.runReadContact(user.getId());
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
}
