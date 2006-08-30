/*
 * Created On: July 31, 2006, 5:27 PM
 */
package com.thinkparity.browser.application.browser.display.avatar;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.Constants.Icons;
import com.thinkparity.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.browser.application.browser.display.avatar.tab.contact.ContactAvatar;
import com.thinkparity.browser.application.browser.display.avatar.tab.container.ContainerAvatar;

import com.thinkparity.model.parity.model.profile.Profile;

/**
 * <b>Title:</b>thinkParity Main Title Tabs<br>
 * <b>Description:</b>The tabs in the title portion of the UI are a simple
 * panel with two images controlling the contexts.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class MainTitleAvatarTabPanel extends MainTitleAvatarAbstractPanel {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** A thinkParity user's profile. */
    private Profile profile;

    /** The tab. */
    private MainTitleAvatar.Tab tab;

    /** Creates new form BrowserTitleTabs */
    public MainTitleAvatarTabPanel() {
        super("BrowserTitleTabs");
        initComponents();
    }

    /**
     * Set localProfile.
     *
     * @param localProfile The Profile.
     */
    void setProfile(final Profile profile) {
        this.profile = profile;
        reloadDisplay();
    }

    /**
     * Select a tab.
     * 
     * @param tab
     *            A tab.
     */
    void setTab(final MainTitleAvatar.Tab tab) {
        this.tab = tab;
        reloadDisplay();
    }

    private void contactsJLabelMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_contactsJLabelMouseClicked
        if (2 == e.getClickCount()) {
            /* NOCOMMIT */
            ((ContactAvatar) new AvatarRegistry().get(AvatarId.TAB_CONTACT)).reload();
        }
        setTab(MainTitleAvatar.Tab.CONTACT);
    }//GEN-LAST:event_contactsJLabelMouseClicked

    private void containersJLabelMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_containersJLabelMouseClicked
        if (2 == e.getClickCount()) {
            /* NOCOMMIT */
            ((ContainerAvatar) new AvatarRegistry().get(AvatarId.TAB_CONTAINER)).reload();
        }
        setTab(MainTitleAvatar.Tab.CONTAINER);
    }//GEN-LAST:event_containersJLabelMouseClicked

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        containersJLabel = new javax.swing.JLabel();
        contactsJLabel = new javax.swing.JLabel();

        nameJLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setOpaque(false);
        containersJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BrowserTitle_ContainersTabSelected.png")));
        containersJLabel.setMaximumSize(new java.awt.Dimension(78, 20));
        containersJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                containersJLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                containersJLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                containersJLabelMouseExited(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        add(containersJLabel, gridBagConstraints);

        contactsJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BrowserTitle_ContactsTab.png")));
        contactsJLabel.setMaximumSize(new java.awt.Dimension(78, 18));
        contactsJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                contactsJLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                contactsJLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                contactsJLabelMouseExited(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        add(contactsJLabel, gridBagConstraints);

        nameJLabel.setFont(Fonts.DefaultFontBold);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 2, 0);
        add(nameJLabel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void containersJLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_containersJLabelMouseExited
        if ((null!=tab) && (tab!=MainTitleAvatar.Tab.CONTAINER)) {
            containersJLabel.setIcon(Icons.BrowserTitle.CONTAINERS_TAB);            
        }
    }//GEN-LAST:event_containersJLabelMouseExited

    private void containersJLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_containersJLabelMouseEntered
        if ((null!=tab) && (tab!=MainTitleAvatar.Tab.CONTAINER)) {
            containersJLabel.setIcon(Icons.BrowserTitle.CONTAINERS_TAB_ROLLOVER);
        }
    }//GEN-LAST:event_containersJLabelMouseEntered

    private void contactsJLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contactsJLabelMouseExited
        if ((null!=tab) && (tab!=MainTitleAvatar.Tab.CONTACT)) {
            contactsJLabel.setIcon(Icons.BrowserTitle.CONTACTS_TAB);            
        }
    }//GEN-LAST:event_contactsJLabelMouseExited

    private void contactsJLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contactsJLabelMouseEntered
        if ((null!=tab) && (tab!=MainTitleAvatar.Tab.CONTACT)) {
            contactsJLabel.setIcon(Icons.BrowserTitle.CONTACTS_TAB_ROLLOVER);
        }
    }//GEN-LAST:event_contactsJLabelMouseEntered

    /**
     * Reload the display.  This will examine the current tab; then call the
     * controller to display the correct avatar; as well as update the images
     * representing the tabs.
     *
     */
    private void reloadDisplay() {
        reloadDisplayTab();
        reloadDisplayName();
    }
    /**
     * Reload the name label.
     * 
     */
    private void reloadDisplayName() {
        nameJLabel.setText("");
        if(null != profile) {
            nameJLabel.setText(profile.getName());
        }
    }
    /**
     * Reload the tab images; and display the correct avatar in the browser.
     * 
     */
    private void reloadDisplayTab() {
        if(null != tab) {
            switch(tab) {
            case CONTACT:
                contactsJLabel.setIcon(Icons.BrowserTitle.CONTACTS_TAB_SELECTED);
                containersJLabel.setIcon(Icons.BrowserTitle.CONTAINERS_TAB);
                mainTitleAvatar.getController().displayTabContactAvatar();
                break;
            case CONTAINER:
                contactsJLabel.setIcon(Icons.BrowserTitle.CONTACTS_TAB);
                containersJLabel.setIcon(Icons.BrowserTitle.CONTAINERS_TAB_SELECTED);
                mainTitleAvatar.getController().displayTabContainerAvatar();
                break;
            default:
                Assert.assertUnreachable("UNKNOWN TAB");
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel contactsJLabel;
    private javax.swing.JLabel containersJLabel;
    private javax.swing.JLabel nameJLabel;
    // End of variables declaration//GEN-END:variables
}
