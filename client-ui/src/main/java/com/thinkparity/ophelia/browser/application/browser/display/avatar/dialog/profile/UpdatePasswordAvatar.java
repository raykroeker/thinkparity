/*
 * ChangePasswordAvatar.java
 *
 * Created on January 30, 2007, 10:02 PM
 */

package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 *
 * @author  user
 */
public class UpdatePasswordAvatar extends Avatar {
    
    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JPasswordField confirmNewPasswordJPasswordField = new javax.swing.JPasswordField();
    private final javax.swing.JLabel forgotPasswordJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JPasswordField newPasswordJPasswordField = new javax.swing.JPasswordField();
    private final javax.swing.JButton okJButton = new javax.swing.JButton();
    private final javax.swing.JPasswordField oldPasswordJPasswordField = new javax.swing.JPasswordField();
    // End of variables declaration//GEN-END:variables
    
    /** Creates new form ChangePasswordAvatar */
    public UpdatePasswordAvatar() {
        super("UpdatePasswordDialog", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
        initDocumentHandlers();
        bindEscapeKey("Cancel", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });
    }

    public AvatarId getId() {
        return AvatarId.DIALOG_PROFILE_UPDATE_PASSWORD;
    }

    public State getState() {
        return null;
    }

    /**
     * Determine whether the user input is valid.
     * This method should return false whenever we want the
     * OK button to be disabled.
     * 
     * @return True if the input is valid; false otherwise.
     */
    public Boolean isInputValid() {
        final String oldPassword = extractOldPassword();
        if (null != oldPassword && 0 < oldPassword.length() && isNewPasswordValid()) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public void reload() {
        reloadOldPassword();
        reloadNewPassword();
        reloadConfirmNewPassword();
        okJButton.setEnabled(Boolean.FALSE);
    }

    public void setState(final State state) {
    }

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    /**
     * Extract the confirm new password from the control.
     *
     * @return The confirm new password.
     */
    private String extractConfirmNewPassword() {
        String password = SwingUtil.extract(confirmNewPasswordJPasswordField);
        if (null!=password) {
            return password.trim();
        } else {
            return password;
        }
    }

    /**
     * Extract the new password from the control.
     *
     * @return The new password.
     */
    private String extractNewPassword() {
        String password = SwingUtil.extract(newPasswordJPasswordField);
        if (null!=password) {
            return password.trim();
        } else {
            return password;
        }
    }

    /**
     * Extract the old password from the control.
     *
     * @return The old password.
     */
    private String extractOldPassword() {
        String password = SwingUtil.extract(oldPasswordJPasswordField);
        if (null!=password) {
            return password.trim();
        } else {
            return password;
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

        final javax.swing.JLabel oldPasswordJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel newPasswordJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel confirmNewPasswordJLabel = new javax.swing.JLabel();
        final javax.swing.JButton cancelJButton = new javax.swing.JButton();
        final javax.swing.JPanel forgotPasswordJPanel = new javax.swing.JPanel();
        final javax.swing.JLabel fillerJLabel = new javax.swing.JLabel();

        oldPasswordJLabel.setFont(Fonts.DialogFont);
        oldPasswordJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("UpdatePasswordDialog.OldPassword"));

        newPasswordJLabel.setFont(Fonts.DialogFont);
        newPasswordJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("UpdatePasswordDialog.NewPassword"));

        confirmNewPasswordJLabel.setFont(Fonts.DialogFont);
        confirmNewPasswordJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("UpdatePasswordDialog.ConfirmNewPassword"));

        oldPasswordJPasswordField.setFont(Fonts.DialogTextEntryFont);

        newPasswordJPasswordField.setFont(Fonts.DialogTextEntryFont);

        confirmNewPasswordJPasswordField.setFont(Fonts.DialogTextEntryFont);

        okJButton.setFont(Fonts.DialogButtonFont);
        okJButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("UpdatePasswordDialog.OK"));
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setFont(Fonts.DialogButtonFont);
        cancelJButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("UpdatePasswordDialog.Cancel"));
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });

        forgotPasswordJPanel.setLayout(new java.awt.GridBagLayout());

        forgotPasswordJPanel.setOpaque(false);
        forgotPasswordJLabel.setFont(Fonts.DialogFont);
        forgotPasswordJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("UpdatePasswordDialog.ForgotPassword"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        forgotPasswordJPanel.add(forgotPasswordJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        forgotPasswordJPanel.add(fillerJLabel, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(oldPasswordJLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(newPasswordJLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(confirmNewPasswordJLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(oldPasswordJPasswordField, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                            .addComponent(newPasswordJPasswordField, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                            .addComponent(confirmNewPasswordJPasswordField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(forgotPasswordJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 138, Short.MAX_VALUE)
                        .addComponent(okJButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelJButton)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelJButton, okJButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oldPasswordJLabel)
                    .addComponent(oldPasswordJPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newPasswordJLabel)
                    .addComponent(newPasswordJPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmNewPasswordJLabel)
                    .addComponent(confirmNewPasswordJPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cancelJButton)
                        .addComponent(okJButton))
                    .addComponent(forgotPasswordJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initDocumentHandlers() {
        final Document oldPasswordDocument = oldPasswordJPasswordField.getDocument();
        oldPasswordDocument.addDocumentListener( new DocumentHandler() );
        final Document newPasswordDocument = newPasswordJPasswordField.getDocument();
        newPasswordDocument.addDocumentListener( new DocumentHandler() );
        final Document confirmNewPasswordDocument = confirmNewPasswordJPasswordField.getDocument();
        confirmNewPasswordDocument.addDocumentListener( new DocumentHandler() );
    }

    /**
     * Determines if the new password is valid. The "confirm new password"
     * and the "new password" must be the same.
     * 
     * @return True if the new password is valid; false otherwise.
     */
    private Boolean isNewPasswordValid() {
        final String newPassword = extractNewPassword();
        final String confirmNewPassword = extractConfirmNewPassword();
        if (null != newPassword && 0 < newPassword.length() &&
            null != confirmNewPassword && 0 < confirmNewPassword.length() &&
            newPassword.equals(confirmNewPassword)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    private void okJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okJButtonActionPerformed
        if (isInputValid()) {
            disposeWindow();
            updatePassword();
        }
    }//GEN-LAST:event_okJButtonActionPerformed

    private void reloadConfirmNewPassword() {
        confirmNewPasswordJPasswordField.setText("");
    }

    private void reloadNewPassword() {
        newPasswordJPasswordField.setText("");
    }

    private void reloadOldPassword() {
        oldPasswordJPasswordField.setText("");
    }

    private void updatePassword() {
        if (isInputValid()) {
            final String password = extractOldPassword();
            final String newPassword = extractNewPassword();
            final String newPasswordConfirm = extractConfirmNewPassword();
            getController().runUpdateProfilePassword(password, newPassword,
                    newPasswordConfirm);
        }
    }

    class DocumentHandler implements DocumentListener {
        public void changedUpdate(final DocumentEvent e) {
            checkInputValid();
        }
        public void insertUpdate(final DocumentEvent e) {
            checkInputValid();
        }
        public void removeUpdate(final DocumentEvent e) {
            checkInputValid();
        }
        private void checkInputValid() {
            if (isInputValid()) {
                okJButton.setEnabled(Boolean.TRUE);
            }
            else {
                okJButton.setEnabled(Boolean.FALSE);
            }
        }
    }
}
