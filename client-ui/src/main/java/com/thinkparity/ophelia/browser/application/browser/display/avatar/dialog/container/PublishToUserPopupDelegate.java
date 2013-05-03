/**
 * Created On: 8-Aug-07 4:02:40 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import javax.swing.JMenuItem;

import com.thinkparity.ophelia.browser.application.browser.DefaultBrowserPopupDelegate;
import com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityMenuItem;
import com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityPopupMenu.MenuBackgroundType;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class PublishToUserPopupDelegate extends DefaultBrowserPopupDelegate {

    /** The maximum number of menu items <code>int</code>. */
    private int maxMenuItems = 0;

    /** The menu width <code>int</code>. */
    private int menuWidth = 0;

    /** The <code>List</code> of <code>PublishToUser</code>. */
    private List<PublishToUser> publishToUsers;

    /**
     * Create a PublishToUserPopupDelegate.
     */
    public PublishToUserPopupDelegate() {
        super();
        publishToUsers = Collections.emptyList();
        setKeyboardRedispatchEnabled(Boolean.TRUE);
    }

    /**
     * Get the list of users.
     * 
     * @return A <code>List</code> of <code>PublishToUser</code>.
     */
    public List<PublishToUser> getPublishToUsers() {
        return publishToUsers;
    }

    /**
     * Set the maximum number of menu items.
     * 
     * @param maxMenuItems
     *            The maximum number of menu items <code>int</code>.
     */
    public void setMaxMenuItems(final int maxMenuItems) {
        this.maxMenuItems = maxMenuItems;
    }

    /**
     * Set the menu width.
     * 
     * @param menuWidth
     *            The width <code>int</code>.
     */
    public void setMenuWidth(final int menuWidth) {
        this.menuWidth = menuWidth;
    }

    /**
     * Set the list of users.
     * 
     * @param publishToUsers
     *            A <code>List</code> of <code>PublishToUser</code>.
     */
    public void setPublishToUsers(final List<PublishToUser> publishToUsers) {
        this.publishToUsers = publishToUsers;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.DefaultPopupDelegate#show()
     */
    @Override
    public void show() {
        prepareMenu();
        setMenuBackgroundType(MenuBackgroundType.GRADIENT);
        super.show();
    }

    /**
     * Add an entry to the menu.
     * 
     * @param user
     *            The <code>PublishToUser</code>.  
     */
    private void addMenuItem(final PublishToUser user) {
        final JMenuItem menuItem = new ThinkParityMenuItem(user.getPopupText());
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                user.getAction().actionPerformed(
                        new ActionEvent(e.getSource(), e.getID(),
                                "PublishToUser", e.getWhen(), e.getModifiers()));
            }
        });
        add(menuItem);
        setMenuPreferredSize(menuItem);
    }

    /**
     * Determine if the maximum menu items has been set.
     * 
     * @return true if the maximum menu items has been set, false otherwise.
     */
    private boolean isSetMaxMenuItems() {
        return (0 != maxMenuItems);
    }

    /**
     * Determine if the menu width has been set.
     * 
     * @return true if the menu width has been set, false otherwise.
     */
    private boolean isSetMenuWidth() {
        return (0 != menuWidth);
    }

    /**
     * Prepare the menu.
     */
    private void prepareMenu() {
        int numMenuItems = publishToUsers.size();
        if (isSetMaxMenuItems() && maxMenuItems < numMenuItems) {
            numMenuItems = maxMenuItems;
        }
        for (int index = 0; index < numMenuItems; index++) {
            addMenuItem(publishToUsers.get(index));
        }
    }

    /**
     * Set the preferred size of the menu.
     * 
     * @param menuItem
     *            The <code>JMenuItem</code>.
     */
    private void setMenuPreferredSize(final JMenuItem menuItem) {
        if (isSetMenuWidth()) {
            final Dimension preferredSize = menuItem.getPreferredSize();
            preferredSize.width = menuWidth;
            menuItem.setPreferredSize(preferredSize);
        }
    }
}
