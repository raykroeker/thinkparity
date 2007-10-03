/*
 * InvitationPanel.java
 *
 * Created on October 2, 2007, 11:51 AM
 */

package com.thinkparity.ophelia.browser.application.system.dialog;

import java.util.List;
import java.util.Vector;

import javax.swing.Icon;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.system.SystemApplication;
import com.thinkparity.ophelia.browser.util.ImageIOUtil;

/**
 *
 * @author robert@thinkparity.com
 */
public class InvitationPanel extends SystemPanel {

    /** Close label icon. */
    private static final Icon CLOSE_ICON;

    /** Close label rollover icon. */
    private static final Icon CLOSE_ROLLOVER_ICON;

    /** A singleton list of invitations. */
    private static final List<Invitation> INVITATIONS;

    static {
        CLOSE_ICON = ImageIOUtil.readIcon("Dialog_CloseButton.png");
        CLOSE_ROLLOVER_ICON = ImageIOUtil.readIcon("Dialog_CloseButtonRollover.png");
        INVITATIONS = new Vector<Invitation>();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JButton acceptJButton = ButtonFactory.create();
    private final javax.swing.JButton declineJButton = ButtonFactory.create();
    private final javax.swing.JLabel messageJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel offlineJLabel = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables

    /** The current invitation index. */
    private int invitationIndex;

    /** The online flag. */
    private Boolean online;

    /** The system application. */
    private final SystemApplication systemApplication;

    /** Creates new form InvitationPanel */
    public InvitationPanel(final SystemApplication systemApplication) {
        super();
        this.systemApplication = systemApplication;
        initComponents();
        this.invitationIndex = 0;
        this.online = isOnline();
    }

    /**
     * Close invitations.
     * 
     * @param invitationId
     *            An invitation id <code>String</code>.
     */
    void close(final String invitationId) {
        int index;
        while (-1 < (index = indexOf(invitationId))) {
            logger.logInfo("Closing invitation {0}.", invitationId);
            closeInvitationPanel(index);
        }
    }

    /**
     * Display an invitation.
     * If an invitation is already displayed, the new
     * invitation is not displayed until the old is processed.
     * 
     * @param invitation
     *            A <code>Invitation</code>.
     */
    void display(final Invitation invitation) {
        INVITATIONS.add(invitation);
        invitationIndex = 0;
        if (INVITATIONS.size() == 1) {
            reload();
        }
    }

    /**
     * Reload the connection status.
     */
    void reloadConnection() {
        this.online = isOnline();
        reload();
    }

    private void acceptJButtonActionPerformed(final java.awt.event.ActionEvent e) {//GEN-FIRST:event_acceptJButtonActionPerformed
        INVITATIONS.get(invitationIndex).invokeAccept();
        closeInvitationPanel(invitationIndex);
    }//GEN-LAST:event_acceptJButtonActionPerformed

    /**
     * Close one entry of the invitation panel.
     * 
     * @param index
     *          The index to close.
     */
    private void closeInvitationPanel(final int index) {
        INVITATIONS.remove(index);
        if (INVITATIONS.size() == 0) {
            disposeWindow();
        } else {
            if (invitationIndex >= INVITATIONS.size()) {
                invitationIndex = INVITATIONS.size() - 1;
            }
            reload();
        }
    }

    private void closeJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeJButtonActionPerformed
        ((javax.swing.JButton) evt.getSource()).setIcon(CLOSE_ICON);
        // NOTE Close one invitation, not all. This exposes the next invitation, if any.
        closeInvitationPanel(invitationIndex);
    }//GEN-LAST:event_closeJButtonActionPerformed

    private void closeJButtonMouseEntered(final java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeJButtonMouseEntered
        ((javax.swing.JButton) evt.getSource()).setIcon(CLOSE_ROLLOVER_ICON);
    }//GEN-LAST:event_closeJButtonMouseEntered

    private void closeJButtonMouseExited(final java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeJButtonMouseExited
        ((javax.swing.JButton) evt.getSource()).setIcon(CLOSE_ICON);
    }//GEN-LAST:event_closeJButtonMouseExited

    private void decideLaterJButtonActionPerformed(final java.awt.event.ActionEvent e) {//GEN-FIRST:event_decideLaterJButtonActionPerformed
        closeInvitationPanel(invitationIndex);
    }//GEN-LAST:event_decideLaterJButtonActionPerformed

    private void declineJButtonActionPerformed(final java.awt.event.ActionEvent e) {//GEN-FIRST:event_declineJButtonActionPerformed
        INVITATIONS.get(invitationIndex).invokeDecline();
        closeInvitationPanel(invitationIndex);
    }//GEN-LAST:event_declineJButtonActionPerformed

    /**
     * Obtain the index of the invitation within the list.
     * 
     * @param invitationId
     *            An invitation id <code>String</code>.
     * @return An <code>int</code> index or -1 if the invitation does not exist.
     */
    private int indexOf(final String invitationId) {
        for (int i = 0; i < INVITATIONS.size(); i++) {
            if (INVITATIONS.get(i).getId().equals(invitationId)) {
                return i;
            }
        }
        return -1;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.JButton closeJButton = new javax.swing.JButton();
        final javax.swing.JLabel logoJLabel = new javax.swing.JLabel();
        final javax.swing.JButton decideLaterJButton = ButtonFactory.create();

        closeJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Dialog_CloseButton.png")));
        closeJButton.setBorderPainted(false);
        closeJButton.setContentAreaFilled(false);
        closeJButton.setFocusPainted(false);
        closeJButton.setFocusable(false);
        closeJButton.setMaximumSize(new java.awt.Dimension(14, 14));
        closeJButton.setMinimumSize(new java.awt.Dimension(14, 14));
        closeJButton.setPreferredSize(new java.awt.Dimension(14, 14));
        closeJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeJButtonMouseExited(evt);
            }
        });
        closeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeJButtonActionPerformed(evt);
            }
        });

        logoJLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logoJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/thinkParityLogo.png")));
        logoJLabel.setFocusable(false);

        messageJLabel.setFont(Fonts.DialogFont);
        messageJLabel.setText("!Invitation Message!");
        messageJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        offlineJLabel.setFont(Fonts.DialogFont);
        offlineJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
        offlineJLabel.setText("!Offline Message!");

        acceptJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SystemApplication.InvitationPanel.Accept"));
        acceptJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptJButtonActionPerformed(evt);
            }
        });

        decideLaterJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SystemApplication.InvitationPanel.DecideLater"));
        decideLaterJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                decideLaterJButtonActionPerformed(evt);
            }
        });

        declineJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SystemApplication.InvitationPanel.Decline"));
        declineJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                declineJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(offlineJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                    .addComponent(closeJButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logoJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                    .addComponent(messageJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(acceptJButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(declineJButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(decideLaterJButton)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {acceptJButton, declineJButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(closeJButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logoJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(messageJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(offlineJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(decideLaterJButton)
                    .addComponent(declineJButton)
                    .addComponent(acceptJButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Determine if the system is online.
     * 
     * @return true if the system is online.
     */
    private Boolean isOnline() {
        switch (systemApplication.getConnection()) {
        case OFFLINE:
            return Boolean.FALSE;
        case ONLINE:
            return Boolean.TRUE;
        default:
            throw Assert.createUnreachable("Unknown connection.");
        }
    }

    /**
     * Reload the invitation panel.
     */
    private void reload() {
        reloadMessage();
        reloadButtons();
        reloadOfflineMessage();
    }

    /**
     * Reload the buttons.
     */
    private void reloadButtons() {
        acceptJButton.setEnabled(online);
        declineJButton.setEnabled(online);
    }

    /**
     * Reload the message.
     */
    private void reloadMessage() {
        messageJLabel.setText(INVITATIONS.get(invitationIndex).getMessage());
    }

    /**
     * Reload the offline message.
     */
    private void reloadOfflineMessage() {
        offlineJLabel.setText(online ? " " : systemApplication.getString("Invitation.ErrorOffline"));
    }
}
