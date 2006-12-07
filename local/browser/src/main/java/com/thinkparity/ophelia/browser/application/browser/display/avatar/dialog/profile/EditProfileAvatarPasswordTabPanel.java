/*
 * EditProfileAvatarPasswordTabPanel.java
 *
 * Created on December 6, 2006, 2:07 PM
 */

package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.application.browser.Browser;


/**
 *
 * @author robert@thinkparity.com
 */
public class EditProfileAvatarPasswordTabPanel extends EditProfileAvatarAbstractTabPanel {

    /** Creates new form EditProfileAvatarPasswordTabPanel */
    public EditProfileAvatarPasswordTabPanel(final Browser browser, final EditProfileAvatar editProfileAvatar, final String tabName) {
        super(browser, editProfileAvatar, tabName);
        initComponents();
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.EditProfileAvatarAbstractTabPanel#reload(com.thinkparity.codebase.model.profile.Profile)
     */
    @Override
    protected void reload(final Profile profile) {
        reloadOldPassword();
        reloadNewPassword();
        reloadConfirmNewPassword();
    }
        
    private String extractNewPassword() {
        return SwingUtil.extract(newPasswordJPasswordField);
    }

    private String extractConfirmNewPassword() {
        return SwingUtil.extract(confirmNewPasswordJPasswordField);
    }

    private String extractOldPassword() {
        return SwingUtil.extract(oldPasswordJPasswordField);
    }
    
    private void reloadOldPassword() {
        oldPasswordJPasswordField.setText("");
    }
    
    private void reloadNewPassword() {
        newPasswordJPasswordField.setText("");
    }
    
    private void reloadConfirmNewPassword() {
        confirmNewPasswordJPasswordField.setText("");
    }
   
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.EditProfileAvatarAbstractTabPanel#save()
     */
    @Override
    protected void save() {
        final String oldPassword = extractOldPassword();
        if (isInputValid() && (null != oldPassword)) {
            final String newPassword = extractNewPassword();
            final String newPasswordConfirm = extractConfirmNewPassword();
            getController().runUpdateProfile(oldPassword, newPassword, newPasswordConfirm);
        }
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
        final String newPassword = extractNewPassword();
        final String newPasswordConfirm = extractConfirmNewPassword();
        
        if ((null == oldPassword) && (null == newPassword) && (null == newPasswordConfirm)) {
            return Boolean.TRUE;
        } else if ((null != oldPassword) && (null != newPassword) && (null != newPasswordConfirm) &&
                newPassword.equals(newPasswordConfirm)) {
            return Boolean.TRUE;  
        } else {
            return Boolean.FALSE;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        explanationJLabel = new javax.swing.JLabel();
        oldPasswordJLabel = new javax.swing.JLabel();
        oldPasswordJPasswordField = new javax.swing.JPasswordField();
        newPasswordJLabel = new javax.swing.JLabel();
        newPasswordJPasswordField = new javax.swing.JPasswordField();
        confirmNewPasswordJLabel = new javax.swing.JLabel();
        confirmNewPasswordJPasswordField = new javax.swing.JPasswordField();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localization/JPanel_Messages"); // NOI18N
        explanationJLabel.setText(bundle.getString("EditProfileDialogPasswordTabPanel.Explanation")); // NOI18N
        explanationJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        oldPasswordJLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        oldPasswordJLabel.setText(bundle.getString("EditProfileDialogPasswordTabPanel.OldPassword")); // NOI18N

        oldPasswordJPasswordField.setFont(new java.awt.Font("Tahoma", 0, 11));
        oldPasswordJPasswordField.setText("jPasswordField1");

        newPasswordJLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        newPasswordJLabel.setText(bundle.getString("EditProfileDialogPasswordTabPanel.NewPassword")); // NOI18N

        newPasswordJPasswordField.setFont(new java.awt.Font("Tahoma", 0, 11));
        newPasswordJPasswordField.setText("jPasswordField1");

        confirmNewPasswordJLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        confirmNewPasswordJLabel.setText(bundle.getString("EditProfileDialogPasswordTabPanel.ConfirmNewPassword")); // NOI18N

        confirmNewPasswordJPasswordField.setFont(new java.awt.Font("Tahoma", 0, 11));
        confirmNewPasswordJPasswordField.setText("jPasswordField2");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, explanationJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, confirmNewPasswordJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, newPasswordJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, oldPasswordJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .add(16, 16, 16)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, confirmNewPasswordJPasswordField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, newPasswordJPasswordField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, oldPasswordJPasswordField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(explanationJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(oldPasswordJLabel)
                    .add(oldPasswordJPasswordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(newPasswordJLabel)
                    .add(newPasswordJPasswordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(confirmNewPasswordJLabel)
                    .add(confirmNewPasswordJPasswordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(172, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel confirmNewPasswordJLabel;
    private javax.swing.JPasswordField confirmNewPasswordJPasswordField;
    private javax.swing.JLabel explanationJLabel;
    private javax.swing.JLabel newPasswordJLabel;
    private javax.swing.JPasswordField newPasswordJPasswordField;
    private javax.swing.JLabel oldPasswordJLabel;
    private javax.swing.JPasswordField oldPasswordJPasswordField;
    // End of variables declaration//GEN-END:variables

}
