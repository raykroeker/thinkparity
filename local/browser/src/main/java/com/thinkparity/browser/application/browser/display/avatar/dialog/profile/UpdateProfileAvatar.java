/*
 * Created on August 24, 2006, 9:45 AM
 */
package com.thinkparity.browser.application.browser.display.avatar.dialog.profile;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.renderer.dialog.profile.ProfileEMailCell;
import com.thinkparity.browser.application.browser.display.renderer.dialog.profile.ProfileEMailCellRenderer;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.profile.ProfileEMail;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class UpdateProfileAvatar extends Avatar {
    
    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The avatar model. */
    private final UpdateProfileAvatarModel model;

    /** Creates new form UpdateProfileAvatar */
    public UpdateProfileAvatar() {
        super("UpdateProfileAvatar");
        this.model = new UpdateProfileAvatarModel();
        bindEscapeKey("Cancel", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });
        initComponents();
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getId()
     * 
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_PROFILE_UPDATE;
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getState()
     * 
     */
    public State getState() {
        return null;
    }

    /**
     * @see com.thinkparity.codebase.swing.AbstractJPanel#isInputValid()
     */
    @Override
    public Boolean isInputValid() {
        final String name = extractInputName();
        final String organization = extractInputOrganization();
        final String title = extractInputTitle();
        if (null != name && null != organization && null != title) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        final Profile  profile = readProfile();
        reloadName(profile);
        reloadOrganization(profile);
        reloadTitle(profile);
        reloadEmails(readProfileEmails());
        reloadPassword();
        reloadNewPassword();
        reloadNewPasswordConfirm();
        emailJTextField.requestFocusInWindow();
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
     * 
     */
    public void setState(final State state) {}

    private void addJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_addJButtonActionPerformed
        getController().runAddProfileEmail(extractInputEmail());
        emailJTextField.setText("");
        reloadEmails(readProfileEmails());
    }//GEN-LAST:event_addJButtonActionPerformed

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    private void emailJTextFieldActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_emailJTextFieldActionPerformed
        addJButtonActionPerformed(e);
    }//GEN-LAST:event_emailJTextFieldActionPerformed

    private void emailsJListMouseReleased(java.awt.event.MouseEvent e) {//GEN-FIRST:event_emailsJListMouseReleased
    }//GEN-LAST:event_emailsJListMouseReleased

    private EMail extractInputEmail() {
        return EMailBuilder.parse(SwingUtil.extract(emailJTextField));
    }

    private String extractInputName() {
        return SwingUtil.extract(nameJTextField);
    }

    private String extractInputOrganization() {
        return SwingUtil.extract(organizationJTextField);
    }


    private List<ProfileEMail> extractInputSelectedEmails() {
        return model.getSelectedEmails(emailsJList);
    }

    private String extractInputTitle() {
        return SwingUtil.extract(titleJTextField);
    }

    private String extractNewPassword() {
        return SwingUtil.extract(newPasswordJPasswordField);
    }

    private String extractNewPasswordConfirm() {
        return SwingUtil.extract(newPasswordConfirmJPasswordField);
    }

    private String extractPassword() {
        return SwingUtil.extract(passwordJPasswordField);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JButton addJButton;
        javax.swing.JButton cancelJButton;
        javax.swing.JLabel confirmPasswordJLabel;
        javax.swing.JLabel currentPasswordJLabel;
        javax.swing.JPanel emailPanel;
        javax.swing.JScrollPane emailsJScrollPane;
        javax.swing.JSeparator jSeparator;
        javax.swing.JLabel nameJLabel;
        javax.swing.JLabel organizationJLabel;
        javax.swing.JLabel passwordJLabel;
        javax.swing.JPanel profileJPanel;
        javax.swing.JButton removeJButton;
        javax.swing.JButton resetPasswordJButton;
        javax.swing.JButton saveJButton;
        javax.swing.JLabel titleJLabel;
        javax.swing.JButton verifyJButton;

        profileJPanel = new javax.swing.JPanel();
        nameJLabel = new javax.swing.JLabel();
        organizationJLabel = new javax.swing.JLabel();
        titleJLabel = new javax.swing.JLabel();
        organizationJTextField = new javax.swing.JTextField();
        nameJTextField = new javax.swing.JTextField();
        titleJTextField = new javax.swing.JTextField();
        saveJButton = new javax.swing.JButton();
        cancelJButton = new javax.swing.JButton();
        jSeparator = new javax.swing.JSeparator();
        passwordJLabel = new javax.swing.JLabel();
        confirmPasswordJLabel = new javax.swing.JLabel();
        resetPasswordJButton = new javax.swing.JButton();
        newPasswordJPasswordField = new javax.swing.JPasswordField();
        newPasswordConfirmJPasswordField = new javax.swing.JPasswordField();
        passwordJPasswordField = new javax.swing.JPasswordField();
        currentPasswordJLabel = new javax.swing.JLabel();
        emailPanel = new javax.swing.JPanel();
        emailJTextField = new javax.swing.JTextField();
        addJButton = new javax.swing.JButton();
        emailsJScrollPane = new javax.swing.JScrollPane();
        emailsJList = new javax.swing.JList();
        removeJButton = new javax.swing.JButton();
        verifyJButton = new javax.swing.JButton();

        setOpaque(false);
        profileJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("DIALOG_PROFILE_UPDATE.InnerPanelTitle")));
        profileJPanel.setOpaque(false);
        nameJLabel.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("DIALOG_PROFILE_UPDATE.NameLabel"));

        organizationJLabel.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("DIALOG_PROFILE_UPDATE.OrganizationLabel"));

        titleJLabel.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("DIALOG_PROFILE_UPDATE.TitleLabel"));

        saveJButton.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("DIALOG_PROFILE_UPDATE.SaveButton"));
        saveJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                saveJButtonActionPerformed(e);
            }
        });

        cancelJButton.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("DIALOG_PROFILE_UPDATE.CancelButton"));
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });

        passwordJLabel.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("DIALOG_PROFILE_UPDATE.PasswordLabel"));

        confirmPasswordJLabel.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("DIALOG_PROFILE_UPDATE.ConfirmPasswordLabel"));

        resetPasswordJButton.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("DIALOG_PROFILE_UPDATE.ResetPasswordButton"));
        resetPasswordJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                resetPasswordJButtonActionPerformed(e);
            }
        });

        newPasswordJPasswordField.setFont(nameJTextField.getFont());

        newPasswordConfirmJPasswordField.setFont(nameJTextField.getFont());

        passwordJPasswordField.setFont(nameJLabel.getFont());

        currentPasswordJLabel.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("DIALOG_PROFILE_UPDATE.CurrentPasswordLabel"));

        org.jdesktop.layout.GroupLayout profileJPanelLayout = new org.jdesktop.layout.GroupLayout(profileJPanel);
        profileJPanel.setLayout(profileJPanelLayout);
        profileJPanelLayout.setHorizontalGroup(
            profileJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, profileJPanelLayout.createSequentialGroup()
                .addContainerGap(246, Short.MAX_VALUE)
                .add(cancelJButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(saveJButton)
                .add(10, 10, 10))
            .add(profileJPanelLayout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jSeparator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                .addContainerGap())
            .add(profileJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(profileJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, confirmPasswordJLabel)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, passwordJLabel)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, currentPasswordJLabel)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, titleJLabel)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, nameJLabel)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, organizationJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(profileJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(profileJPanelLayout.createSequentialGroup()
                        .add(2, 2, 2)
                        .add(profileJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(passwordJPasswordField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                            .add(nameJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                            .add(titleJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                            .add(organizationJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)))
                    .add(profileJPanelLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(newPasswordJPasswordField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, profileJPanelLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(profileJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(resetPasswordJButton)
                            .add(newPasswordConfirmJPasswordField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))))
                .addContainerGap())
        );
        profileJPanelLayout.setVerticalGroup(
            profileJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, profileJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(profileJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(nameJLabel)
                    .add(nameJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(profileJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(organizationJLabel)
                    .add(organizationJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(profileJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(titleJLabel)
                    .add(titleJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(profileJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(passwordJPasswordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(currentPasswordJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(profileJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(newPasswordJPasswordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(passwordJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(profileJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(newPasswordConfirmJPasswordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(confirmPasswordJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(profileJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(saveJButton)
                    .add(cancelJButton)
                    .add(resetPasswordJButton))
                .addContainerGap())
        );

        emailPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("DIALOG_PROFILE_UPDATE.EmailPanel")));
        emailPanel.setOpaque(false);
        emailJTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                emailJTextFieldActionPerformed(e);
            }
        });

        addJButton.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("DIALOG_PROFILE_UPDATE.AddEmailButton"));
        addJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                addJButtonActionPerformed(e);
            }
        });

        emailsJList.setModel(model.getEmailsListModel());
        emailsJList.setCellRenderer(new ProfileEMailCellRenderer());
        emailsJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent e) {
                emailsJListMouseReleased(e);
            }
        });

        emailsJScrollPane.setViewportView(emailsJList);

        removeJButton.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("DIALOG_PROFILE_UPDATE.RemoveButton"));
        removeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                removeJButtonActionPerformed(e);
            }
        });

        verifyJButton.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("DIALOG_PROFILE_UPDATE.VerifyButton"));
        verifyJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                verifyJButtonActionPerformed(e);
            }
        });

        org.jdesktop.layout.GroupLayout emailPanelLayout = new org.jdesktop.layout.GroupLayout(emailPanel);
        emailPanel.setLayout(emailPanelLayout);
        emailPanelLayout.setHorizontalGroup(
            emailPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(emailPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(emailPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, emailPanelLayout.createSequentialGroup()
                        .add(removeJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(verifyJButton))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, emailsJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                    .add(emailPanelLayout.createSequentialGroup()
                        .add(emailJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(addJButton)))
                .addContainerGap())
        );
        emailPanelLayout.setVerticalGroup(
            emailPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(emailPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(emailPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(addJButton)
                    .add(emailJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(emailsJScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(emailPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(verifyJButton)
                    .add(removeJButton))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, emailPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, profileJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(profileJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(emailPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Read the profile from the content provider.
     * 
     * @return A <code>Profile</code>.
     */
    private Profile readProfile() {
        return (Profile) ((CompositeFlatSingleContentProvider) contentProvider).getElement(0, null);
    }

    /**
     * Read the profile email addresses from the content provider.
     * 
     * @return A <code>List&lt;ProfileEMail&gt;</code>.
     */
    private List<ProfileEMail> readProfileEmails() {
        final List<ProfileEMail> profileEMails = new ArrayList<ProfileEMail>();
        final ProfileEMail[] array =
            (ProfileEMail[]) ((CompositeFlatSingleContentProvider) contentProvider).getElements(0, null);
        for (final ProfileEMail profileEMail : array) {
            profileEMails.add(profileEMail);
        }
        return profileEMails;
    }

    private void reloadEmails(final List<ProfileEMail> emails) {
        model.clear();
        for (final ProfileEMail email : emails) {
            model.add(toDisplay(email));
        }
    }

    private void reloadName(final Profile profile) {
        nameJTextField.setText("");
        final String name = profile.getName();
        if (null != name) {
            nameJTextField.setText(name);
        }
    }

    private void reloadNewPassword() {
        newPasswordJPasswordField.setText("");
    }

    private void reloadNewPasswordConfirm() {
        newPasswordConfirmJPasswordField.setText("");
    }

    private void reloadOrganization(final Profile profile) {
        organizationJTextField.setText("");
        final String organization = profile.getOrganization();
        if (null != organization) {
            organizationJTextField.setText(organization);
        }
    }
    private void reloadPassword() {
        passwordJPasswordField.setText("");
    }
    private void reloadTitle(final Profile profile) {
        titleJTextField.setText("");
        final String title = profile.getTitle();
        if (null != title) {
            titleJTextField.setText(title);
        }
    }
    private void removeJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_removeJButtonActionPerformed
        final List<ProfileEMail> selectedEmails = extractInputSelectedEmails();
        for (final ProfileEMail selectedEmail : selectedEmails) {
            getController().runRemoveProfileEmail(selectedEmail.getEmailId());
        }
        reloadEmails(readProfileEmails());
    }//GEN-LAST:event_removeJButtonActionPerformed
    private void resetPasswordJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_resetPasswordJButtonActionPerformed
        getController().runResetProfilePassword();
    }//GEN-LAST:event_resetPasswordJButtonActionPerformed
    private void saveJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_saveJButtonActionPerformed
        if (isInputValid()) {
            final String name = extractInputName();
            final String organization = extractInputOrganization();
            final String title = extractInputTitle();
            final String password = extractPassword();
            final String newPassword = extractNewPassword();
            final String newPasswordConfirm = extractNewPasswordConfirm();
            getController().runUpdateProfile(name, organization, title,
                    password, newPassword, newPasswordConfirm);
            disposeWindow();
        }
    }//GEN-LAST:event_saveJButtonActionPerformed
    private ProfileEMailCell toDisplay(final ProfileEMail email) {
        final ProfileEMailCell display = new ProfileEMailCell();
        display.setEmail(email.getEmail());
        display.setEmailId(email.getEmailId());
        display.setProfileId(email.getProfileId());
        display.setVerified(email.isVerified());
        return display;
    }
    private void verifyJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_verifyJButtonActionPerformed
        final List<ProfileEMail> selectedEmails = extractInputSelectedEmails();
        for (final ProfileEMail selectedEmail : selectedEmails) {
            getController().runVerifyProfileEmail(selectedEmail.getEmailId());
        }
        reloadEmails(readProfileEmails());
    }//GEN-LAST:event_verifyJButtonActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField emailJTextField;
    private javax.swing.JList emailsJList;
    private javax.swing.JTextField nameJTextField;
    private javax.swing.JPasswordField newPasswordConfirmJPasswordField;
    private javax.swing.JPasswordField newPasswordJPasswordField;
    private javax.swing.JTextField organizationJTextField;
    private javax.swing.JPasswordField passwordJPasswordField;
    private javax.swing.JTextField titleJTextField;
    // End of variables declaration//GEN-END:variables
}
