/*
 * Created On: July 31, 2006, 5:27 PM
 */
package com.thinkparity.browser.application.browser.display.avatar;

import javax.swing.Icon;
import javax.swing.JLabel;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.Constants.Icons.BrowserTitle;
import com.thinkparity.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.browser.platform.action.Data;

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
        setResizeEdges(Resizer.FormLocation.LEFT);
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

    private void archiveJLabelMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_archiveJLabelMouseClicked
        selectTab(MainTitleAvatar.Tab.ARCHIVE);
    }//GEN-LAST:event_archiveJLabelMouseClicked

    private void archiveJLabelMouseEntered(java.awt.event.MouseEvent e) {//GEN-FIRST:event_archiveJLabelMouseEntered
        highlightTab(MainTitleAvatar.Tab.ARCHIVE, archiveJLabel, BrowserTitle.ARCHIVE_TAB_ROLLOVER);
    }//GEN-LAST:event_archiveJLabelMouseEntered

    private void archiveJLabelMouseExited(java.awt.event.MouseEvent e) {//GEN-FIRST:event_archiveJLabelMouseExited
        unhighlightTab(MainTitleAvatar.Tab.ARCHIVE, archiveJLabel, BrowserTitle.ARCHIVE_TAB);
    }//GEN-LAST:event_archiveJLabelMouseExited

    private void contactsJLabelMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_contactsJLabelMouseClicked
        selectTab(MainTitleAvatar.Tab.CONTACT);
    }//GEN-LAST:event_contactsJLabelMouseClicked

    private void contactsJLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contactsJLabelMouseEntered
        highlightTab(MainTitleAvatar.Tab.CONTACT, contactsJLabel, BrowserTitle.CONTACTS_TAB_ROLLOVER);
    }//GEN-LAST:event_contactsJLabelMouseEntered

    private void contactsJLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contactsJLabelMouseExited
        unhighlightTab(MainTitleAvatar.Tab.CONTACT, contactsJLabel, BrowserTitle.CONTACTS_TAB);
    }//GEN-LAST:event_contactsJLabelMouseExited

    private void containersJLabelMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_containersJLabelMouseClicked
        selectTab(MainTitleAvatar.Tab.CONTAINER);
    }//GEN-LAST:event_containersJLabelMouseClicked

    private void containersJLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_containersJLabelMouseEntered
        highlightTab(MainTitleAvatar.Tab.CONTAINER, containersJLabel, BrowserTitle.CONTAINERS_TAB_ROLLOVER);
    }//GEN-LAST:event_containersJLabelMouseEntered

    private void containersJLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_containersJLabelMouseExited
        unhighlightTab(MainTitleAvatar.Tab.CONTAINER, containersJLabel, BrowserTitle.CONTAINERS_TAB);
    }//GEN-LAST:event_containersJLabelMouseExited

    /**
     * Highlight a tab. If the tab is not selected; the label's icon will be
     * set to that provided.
     * 
     * @param tab
     *            A tab.
     * @param jLabel
     *            A <code>JLabel</code> representing the tab.
     * @param icon
     *            An <code>Icon</code>.
     * @see #unhighlightTab(com.thinkparity.browser.application.browser.display.avatar.MainTitleAvatar.Tab,
     *      JLabel, Icon)
     */
    private void highlightTab(final MainTitleAvatar.Tab tab,
            final JLabel jLabel, final Icon icon) {
        if (this.tab != tab) {
            jLabel.setIcon(icon);
        }
    }

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

        archiveJLabel = new javax.swing.JLabel();
        nameJLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setOpaque(false);
        containersJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BrowserTitle_ContainersTabSelected.png")));
        containersJLabel.setMaximumSize(new java.awt.Dimension(78, 20));
        containersJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                containersJLabelMouseClicked(e);
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                containersJLabelMouseEntered(e);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                containersJLabelMouseExited(e);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        add(containersJLabel, gridBagConstraints);

        contactsJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BrowserTitle_ContactsTab.png")));
        contactsJLabel.setMaximumSize(new java.awt.Dimension(78, 18));
        contactsJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                contactsJLabelMouseClicked(e);
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                contactsJLabelMouseEntered(e);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                contactsJLabelMouseExited(e);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        add(contactsJLabel, gridBagConstraints);

        archiveJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BrowserTitle_ArchiveTab.png")));
        archiveJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                archiveJLabelMouseClicked(e);
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                archiveJLabelMouseEntered(e);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                archiveJLabelMouseExited(e);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        add(archiveJLabel, gridBagConstraints);

        nameJLabel.setFont(Fonts.DefaultFontBold);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 2, 0);
        add(nameJLabel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

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
        switch(tab) {
        case ARCHIVE:
            archiveJLabel.setIcon(BrowserTitle.ARCHIVE_TAB_SELECTED);

            contactsJLabel.setIcon(BrowserTitle.CONTACTS_TAB);
            containersJLabel.setIcon(BrowserTitle.CONTAINERS_TAB);
            break;
        case CONTACT:
            contactsJLabel.setIcon(BrowserTitle.CONTACTS_TAB_SELECTED);

            archiveJLabel.setIcon(BrowserTitle.ARCHIVE_TAB);
            containersJLabel.setIcon(BrowserTitle.CONTAINERS_TAB);
            break;
        case CONTAINER:
            containersJLabel.setIcon(BrowserTitle.CONTAINERS_TAB_SELECTED);

            archiveJLabel.setIcon(BrowserTitle.ARCHIVE_TAB);
            contactsJLabel.setIcon(BrowserTitle.CONTACTS_TAB);
            break;
        default:
            Assert.assertUnreachable("UNKNOWN TAB");
        }
    }

    /**
     * Select a tab.  Here we set the input in the main title avatar to contain
     * the selected tab.
     * 
     * @param tab
     *            A tab.
     */
    private void selectTab(final MainTitleAvatar.Tab tab) {
        final Data data = (Data) ((Data) mainTitleAvatar.getInput()).clone();
        data.set(MainTitleAvatar.DataKey.TAB, tab);
        mainTitleAvatar.setInput(data);
    }

    /**
     * Unhighlight a tab. If the tab is not selected; the label's icon will be
     * set to that provided.
     * 
     * @param tab
     *            A tab.
     * @param jLabel
     *            A <code>JLabel</code> representing the tab.
     * @param icon
     *            An <code>Icon</code>.
     * @see #highlightTab(com.thinkparity.browser.application.browser.display.avatar.MainTitleAvatar.Tab,
     *      JLabel, Icon)
     */
    private void unhighlightTab(final MainTitleAvatar.Tab tab,
            final JLabel jLabel, final Icon icon) {
        if (this.tab != tab) {
            jLabel.setIcon(icon);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel archiveJLabel;
    private javax.swing.JLabel contactsJLabel;
    private javax.swing.JLabel containersJLabel;
    private javax.swing.JLabel nameJLabel;
    // End of variables declaration//GEN-END:variables
}
