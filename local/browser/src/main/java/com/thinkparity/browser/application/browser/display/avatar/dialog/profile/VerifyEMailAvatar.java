/*
 * Created on August 28, 2006, 12:59 PM
 */
package com.thinkparity.browser.application.browser.display.avatar.dialog.profile;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.model.profile.ProfileEMail;

/**
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class VerifyEMailAvatar extends Avatar {

    /** Creates new form VerifyEMailDialog */
    public VerifyEMailAvatar() {
        super(AvatarId.DIALOG_PROFILE_VERIFY_EMAIL);
        initComponents();
    }
    
    /**
     * Obtain the avatar id.
     * 
     * 
     * @return The avatar id.
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_PROFILE_VERIFY_EMAIL;
    }

    /**
     * Obtain the avatar's state information.
     * 
     * 
     * @return The avatar's state information.
     */
    public State getState() {
        return null;
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        reloadEmbeddedAssistance(readEmail());
    }

    /**
     * Set the avatar state.
     * 
     * 
     * @param state
     *            The avatar's state information.
     */
    public void setState(final State state) {}

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    private String extractInputKey() {
        return SwingUtil.extract(keyJTextField);
    }

    private Long getInputEmailId() {
        if (null == input) {
            return null;
        } else {
            return (Long) ((Data) input).get(DataKey.EMAIL_ID);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JButton cancelJButton;
        javax.swing.JLabel keyJLabel;
        javax.swing.JButton verifyJButton;

        embeddedAssistanceJLabel = new javax.swing.JLabel();
        keyJLabel = new javax.swing.JLabel();
        keyJTextField = new javax.swing.JTextField();
        verifyJButton = new javax.swing.JButton();
        cancelJButton = new javax.swing.JButton();

        embeddedAssistanceJLabel.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("DIALOG_PROFILE_VERIFY_EMAIL.EmbeddedAssisstance"));

        keyJLabel.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("DIALOG_PROFILE_VERIFY_EMAIL.VerificationKeyLabel"));

        verifyJButton.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("DIALOG_PROFILE_VERIFY_EMAIL.VerifyButton"));
        verifyJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                verifyJButtonActionPerformed(e);
            }
        });

        cancelJButton.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("DIALOG_PROFILE_VERIFY_EMAIL.CancelButton"));
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(embeddedAssistanceJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(keyJLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(keyJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(cancelJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(verifyJButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(embeddedAssistanceJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(keyJLabel)
                    .add(keyJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 18, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(verifyJButton)
                    .add(cancelJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private ProfileEMail readEmail() {
        final Long emailId = getInputEmailId();
        if (null == emailId) {
            return null;
        } else {
            return (ProfileEMail) ((SingleContentProvider) contentProvider)
                    .getElement(emailId);
        }
    }

    private void reloadEmbeddedAssistance(final ProfileEMail email) {
        embeddedAssistanceJLabel.setText("");
        if (null != email) {
            embeddedAssistanceJLabel.setText(getString("EmbeddedAssisstance", new Object[] { email.getEmail() }));
        }
    }

    private void verifyJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_verifyJButtonActionPerformed
        final ProfileEMail email = readEmail();
        getController().runVerifyProfileEmail(email.getEmailId(), extractInputKey());
        disposeWindow();
    }//GEN-LAST:event_verifyJButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel embeddedAssistanceJLabel;
    private javax.swing.JTextField keyJTextField;
    // End of variables declaration//GEN-END:variables

    public enum DataKey { EMAIL_ID }
}
