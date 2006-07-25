/*
 * ButtonPanel2.java
 *
 * Created on April 30, 2006, 12:51 PM
 */

package com.thinkparity.browser.application.browser.display.avatar.title;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.event.MouseInputAdapter;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.platform.Platform.Connection;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.ImageIOUtil;

import com.thinkparity.codebase.assertion.Assert;

/**
 *
 * @author  raymond
 */
public class ButtonPanel2 extends AbstractJPanel {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** Close label icon. */
    private static final Icon CLOSE_ICON;

    /** Close label rollover icon. */
    private static final Icon CLOSE_ROLLOVER_ICON;

    /** Contacts label icon. */
    private static final Icon CONTACTS_ICON;

    /** Contacts label disabled icon. */
    private static final Icon CONTACTS_DISABLED_ICON;

    /** Contacts label rollover icon. */
    private static final Icon CONTACTS_ROLLOVER_ICON;

    /** Help label icon. */
    private static final Icon HELP_ICON;

    /** Help label rollover icon. */
    private static final Icon HELP_ROLLOVER_ICON;

    /** Min label icon. */
    private static final Icon MIN_ICON;

    /** Min label rollover icon. */
    private static final Icon MIN_ROLLOVER_ICON;

    static {
        CLOSE_ICON = ImageIOUtil.readIcon("CloseButton.png");
        CLOSE_ROLLOVER_ICON = ImageIOUtil.readIcon("CloseButtonRollover.png");

        CONTACTS_ICON = ImageIOUtil.readIcon("ContactsButton.png");
        CONTACTS_DISABLED_ICON = ImageIOUtil.readIcon("ContactsButtonDisabled.png");
        CONTACTS_ROLLOVER_ICON = ImageIOUtil.readIcon("ContactsButtonRollover.png");

        HELP_ICON = ImageIOUtil.readIcon("HelpButton.png");
        HELP_ROLLOVER_ICON = ImageIOUtil.readIcon("HelpButtonRollover.png");

        MIN_ICON = ImageIOUtil.readIcon("MinimizeButton.png");
        MIN_ROLLOVER_ICON = ImageIOUtil.readIcon("MinimizeButtonRollover.png");
    }

    /** The avatar the button panel resides upon. */
    private final Avatar avatar;

    /** The contacts button mouse adapter. */
    private final MouseAdapter contactMouseAdapter;

    /** Creates new form ButtonPanel2 */
    public ButtonPanel2(final Avatar avatar, final MouseInputAdapter mouseInputAdapter) {
        super("BrowserTitleAvatar.ButtonPanel");
        this.avatar = avatar;
        addMouseListener(mouseInputAdapter);
        addMouseMotionListener(mouseInputAdapter);
        //setTransferHandler(new CreateDocumentTxHandler(getBrowser()));
        //CopyActionEnforcer.applyEnforcer(this);
        this.contactMouseAdapter = new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                contactsJLabelMouseClicked(e);
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                contactsJLabelMouseEntered(e);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                contactsJLabelMouseExited(e);
            }
        };
        initComponents();

        reloadConnectionStatus(getBrowser().getConnection());
    }

    /**
     * Reload the connection status.
     * 
     * @param connection
     *            The connection status.
     */
    public void reloadConnectionStatus(final Connection connection) {
        if(connection == Connection.OFFLINE) {
            contactsJLabel.setIcon(CONTACTS_DISABLED_ICON);
            contactsJLabel.removeMouseListener(contactMouseAdapter);
        }
        else if(connection == Connection.ONLINE) {
            contactsJLabel.setIcon(CONTACTS_ICON);
            contactsJLabel.addMouseListener(contactMouseAdapter);
        }
        else {
            Assert.assertUnreachable("[LBROWSER] [APPLICATION] [BROWSER] [DISPLAY] [AVATAR] [BUTTON PANEL] [RELOAD CONNECTION STATUS] [UNREACHABLE SWITCH CASE");
        }
    }

    private void contactsJLabelMouseClicked(final MouseEvent e) {
        getBrowser().displaySessionManageContacts();
        contactsJLabelMouseExited(e);
    }

    private void contactsJLabelMouseExited(final MouseEvent e) {                                           
        ((JLabel) e.getSource()).setIcon(CONTACTS_ICON);
    }                                          

    private void contactsJLabelMouseEntered(final MouseEvent e) {                                            
        ((JLabel) e.getSource()).setIcon(CONTACTS_ROLLOVER_ICON);
    }                                           
   

    /**
     * Obtain the browser application.
     * 
     * @return The browser application.
     */
    private Browser getBrowser() { return avatar.getController(); }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JLabel closeJLabel;
        java.awt.GridBagConstraints gridBagConstraints;
        javax.swing.JLabel hPaddingJLabel;
        javax.swing.JLabel helpJLabel;
        javax.swing.JLabel minimizeJLabel;

        closeJLabel = LabelFactory.create(CLOSE_ICON);
        minimizeJLabel = LabelFactory.create(MIN_ICON);
        helpJLabel = LabelFactory.create(HELP_ICON);
        contactsJLabel = LabelFactory.create(CONTACTS_ICON);
        hPaddingJLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setOpaque(false);
        closeJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/CloseButton.png")));
        closeJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                closeJLabelMouseClicked(e);
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                closeJLabelMouseEntered(e);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                closeJLabelMouseExited(e);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 8);
        add(closeJLabel, gridBagConstraints);

        minimizeJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/MinimizeButton.png")));
        minimizeJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                minimizeJLabelMouseClicked(e);
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                minimizeJLabelMouseEntered(e);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                minimizeJLabelMouseExited(e);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(6, 12, 0, 2);
        add(minimizeJLabel, gridBagConstraints);

        helpJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/HelpButton.png")));
        helpJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                helpJLabelMouseClicked(e);
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                helpJLabelMouseEntered(e);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                helpJLabelMouseExited(e);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        add(helpJLabel, gridBagConstraints);

        contactsJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ContactsButton.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        add(contactsJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(hPaddingJLabel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void closeJLabelMouseExited(java.awt.event.MouseEvent e) {//GEN-FIRST:event_closeJLabelMouseExited
        ((JLabel) e.getSource()).setIcon(CLOSE_ICON);
    }//GEN-LAST:event_closeJLabelMouseExited

    private void closeJLabelMouseEntered(java.awt.event.MouseEvent e) {//GEN-FIRST:event_closeJLabelMouseEntered
        ((JLabel) e.getSource()).setIcon(CLOSE_ROLLOVER_ICON);
    }//GEN-LAST:event_closeJLabelMouseEntered

    private void closeJLabelMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_closeJLabelMouseClicked
        getBrowser().closeBrowserWindow();
        closeJLabelMouseExited(e);
    }//GEN-LAST:event_closeJLabelMouseClicked

    private void minimizeJLabelMouseExited(java.awt.event.MouseEvent e) {//GEN-FIRST:event_minimizeJLabelMouseExited
        ((JLabel) e.getSource()).setIcon(MIN_ICON);
    }//GEN-LAST:event_minimizeJLabelMouseExited

    private void minimizeJLabelMouseEntered(java.awt.event.MouseEvent e) {//GEN-FIRST:event_minimizeJLabelMouseEntered
        ((JLabel) e.getSource()).setIcon(MIN_ROLLOVER_ICON);
    }//GEN-LAST:event_minimizeJLabelMouseEntered

    private void minimizeJLabelMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_minimizeJLabelMouseClicked
        getBrowser().minimize();
        minimizeJLabelMouseExited(e);
    }//GEN-LAST:event_minimizeJLabelMouseClicked

    private void helpJLabelMouseExited(java.awt.event.MouseEvent e) {//GEN-FIRST:event_helpJLabelMouseExited
        ((JLabel) e.getSource()).setIcon(HELP_ICON);
    }//GEN-LAST:event_helpJLabelMouseExited

    private void helpJLabelMouseEntered(java.awt.event.MouseEvent e) {//GEN-FIRST:event_helpJLabelMouseEntered
        ((JLabel) e.getSource()).setIcon(HELP_ROLLOVER_ICON);
    }//GEN-LAST:event_helpJLabelMouseEntered

    private void helpJLabelMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_helpJLabelMouseClicked
        getBrowser().runAddContact();
        helpJLabelMouseExited(e);
    }//GEN-LAST:event_helpJLabelMouseClicked
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel contactsJLabel;
    // End of variables declaration//GEN-END:variables
    
}
