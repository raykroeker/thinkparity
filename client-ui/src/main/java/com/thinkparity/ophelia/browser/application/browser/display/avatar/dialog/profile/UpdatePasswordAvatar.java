/*
 * Created On: January 30, 2007, 10:02 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.AbstractAction;
import javax.swing.text.AbstractDocument;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.text.JTextFieldLengthFilter;

import com.thinkparity.codebase.model.profile.ProfileConstraints;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile.UpdatePasswordProvider;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 * <b>Title:</b>thinkParity OpheliaUI Update Password Avatar<br>
 * <b>Description:</b><br>
 * 
 * @author robert@thinkparity.com
 * @version 1.1.2.5
 */
public class UpdatePasswordAvatar extends Avatar {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JPasswordField confirmNewPasswordJPasswordField = new javax.swing.JPasswordField();
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel forgotPasswordJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JPasswordField newPasswordJPasswordField = new javax.swing.JPasswordField();
    private final javax.swing.JButton okJButton = ButtonFactory.create();
    private final javax.swing.JPasswordField oldPasswordJPasswordField = new javax.swing.JPasswordField();
    // End of variables declaration//GEN-END:variables

    /** An instance of <code>ProfileConstraints</code>. */
    private final ProfileConstraints profileConstraints;

    /**
     * Create UpdatePasswordAvatar.
     * 
     */
    public UpdatePasswordAvatar() {
        super("UpdatePasswordAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        this.profileConstraints = ProfileConstraints.getInstance();
        initComponents();
        addValidationListener(oldPasswordJPasswordField);
        addValidationListener(newPasswordJPasswordField);
        addValidationListener(confirmNewPasswordJPasswordField);
        initFocusListeners();
        bindEscapeKey();
    }

    public AvatarId getId() {
        return AvatarId.DIALOG_PROFILE_UPDATE_PASSWORD;
    }

    public State getState() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     * 
     */
    public void reload() {
        reloadPassword();
        reloadNewPassword();
        reloadConfirmNewPassword();
        validateInput();
    }

    public void setState(final State state) {
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#validateInput()
     *
     */
    @Override
    public final void validateInput() {
        validateInput(Boolean.FALSE);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#isInputValid()
     */
    @Override
    protected Boolean isInputValid() {
        validateInput(Boolean.TRUE);
        return !containsInputErrors();
    }

    /**
     * Make the escape key behave like cancel.
     */
    private void bindEscapeKey() {
        bindEscapeKey("Cancel", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });
    }

    private void cancelJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    private void confirmNewPasswordJPasswordFieldActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmNewPasswordJPasswordFieldActionPerformed
        okJButtonActionPerformed(evt);
    }//GEN-LAST:event_confirmNewPasswordJPasswordFieldActionPerformed

    /**
     * Extract the confirm new password from the control.
     *
     * @return The confirm new password.
     */
    private String extractConfirmNewPassword() {
        return SwingUtil.extract(confirmNewPasswordJPasswordField, Boolean.TRUE);
    }

    /**
     * Extract the credentials from the profile and the password text control.
     * 
     * @return An instance of <code>Credentials</code>.
     */
    private Credentials extractCredentials() {
        final String password = extractPassword();
        if (null == password) {
            return null;
        } else {
            final Credentials credentials = new Credentials();
            credentials.setPassword(password);
            credentials.setUsername(getSimpleUsername());
            return credentials;
        }
    }

    /**
     * Extract the new password from the control.
     *
     * @return The new password.
     */
    private String extractNewPassword() {
        return SwingUtil.extract(newPasswordJPasswordField, Boolean.TRUE);
    }

    /**
     * Extract the password from the control.
     * 
     * @return The password <code>String</code>.
     */
    private String extractPassword() {
        return SwingUtil.extract(oldPasswordJPasswordField, Boolean.TRUE);
    }

    private void forgotPasswordJLabelMousePressed(final java.awt.event.MouseEvent evt) {//GEN-FIRST:event_forgotPasswordJLabelMousePressed
        getController().runContactUs();
    }//GEN-LAST:event_forgotPasswordJLabelMousePressed

    /**
     * Obtain the simple username from the content provider.
     * 
     * @return A username <code>String</code>.
     */
    private String getSimpleUsername() {
        return ((UpdatePasswordProvider) contentProvider).getSimpleUsername();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.JLabel oldPasswordJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel newPasswordJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel confirmNewPasswordJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel forgotPasswordExplanationJLabel = new javax.swing.JLabel();
        final javax.swing.JButton cancelJButton = ButtonFactory.create();

        oldPasswordJLabel.setFont(Fonts.DialogFont);
        oldPasswordJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdatePasswordAvatar.OldPassword"));

        newPasswordJLabel.setFont(Fonts.DialogFont);
        newPasswordJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdatePasswordAvatar.NewPassword"));

        confirmNewPasswordJLabel.setFont(Fonts.DialogFont);
        confirmNewPasswordJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdatePasswordAvatar.ConfirmNewPassword"));

        oldPasswordJPasswordField.setFont(Fonts.DialogTextEntryFont);
        ((AbstractDocument) oldPasswordJPasswordField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getPassword()));
        oldPasswordJPasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oldPasswordJPasswordFieldActionPerformed(evt);
            }
        });

        newPasswordJPasswordField.setFont(Fonts.DialogTextEntryFont);
        ((AbstractDocument) newPasswordJPasswordField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getPassword()));
        newPasswordJPasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newPasswordJPasswordFieldActionPerformed(evt);
            }
        });

        confirmNewPasswordJPasswordField.setFont(Fonts.DialogTextEntryFont);
        ((AbstractDocument) confirmNewPasswordJPasswordField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getPassword()));
        confirmNewPasswordJPasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmNewPasswordJPasswordFieldActionPerformed(evt);
            }
        });

        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
        errorMessageJLabel.setText("!Error Message!");

        forgotPasswordExplanationJLabel.setFont(Fonts.DialogFont);
        forgotPasswordExplanationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdatePasswordAvatar.ExplanationForgetPassword"));

        forgotPasswordJLabel.setFont(Fonts.DialogFont);
        forgotPasswordJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdatePasswordAvatar.ForgetPassword"));
        forgotPasswordJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                forgotPasswordJLabelMousePressed(evt);
            }
        });

        okJButton.setFont(Fonts.DialogButtonFont);
        okJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdatePasswordAvatar.OK"));
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setFont(Fonts.DialogButtonFont);
        cancelJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdatePasswordAvatar.Cancel"));
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(errorMessageJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(oldPasswordJLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(newPasswordJLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(confirmNewPasswordJLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(oldPasswordJPasswordField, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                            .addComponent(newPasswordJPasswordField, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                            .addComponent(confirmNewPasswordJPasswordField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(forgotPasswordExplanationJLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(forgotPasswordJLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                        .addComponent(okJButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelJButton)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelJButton, okJButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
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
                .addGap(17, 17, 17)
                .addComponent(errorMessageJLabel)
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelJButton)
                    .addComponent(okJButton)
                    .addComponent(forgotPasswordExplanationJLabel)
                    .addComponent(forgotPasswordJLabel))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Initialize the focus listeners.
     */
    private void initFocusListeners() {
        final FocusListener focusListener = new FocusListener() {
            public void focusGained(FocusEvent e) {
                validateInput();
            }
            public void focusLost(FocusEvent e) {
                validateInput();
            }
        };
        newPasswordJPasswordField.addFocusListener(focusListener);
        confirmNewPasswordJPasswordField.addFocusListener(focusListener);
    }

    /**
     * Validate the user's credentials.
     *
     */
    private Boolean isPasswordValid() {
        final Credentials credentials = extractCredentials();
        if (null != credentials) {
            try {
                ((UpdatePasswordProvider) contentProvider).validateCredentials(credentials);
                return Boolean.TRUE;
            } catch (final InvalidCredentialsException icx) {
                addInputError(getString("ErrorInvalidCredentials"));
                errorMessageJLabel.setText(" ");
                if (containsInputErrors())
                    errorMessageJLabel.setText(getInputErrors().get(0));
                okJButton.setEnabled(Boolean.FALSE);

                return Boolean.FALSE;
            }
        } else {
            addInputError(getString("ErrorInvalidCredentials"));
            errorMessageJLabel.setText(" ");
            if (containsInputErrors())
                errorMessageJLabel.setText(getInputErrors().get(0));
            okJButton.setEnabled(Boolean.FALSE);

            return Boolean.FALSE;
        }
    }

    private void newPasswordJPasswordFieldActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newPasswordJPasswordFieldActionPerformed
        okJButtonActionPerformed(evt);
    }//GEN-LAST:event_newPasswordJPasswordFieldActionPerformed

    private void okJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okJButtonActionPerformed
        if (isInputValid()) {
            SwingUtil.setCursor(this, java.awt.Cursor.WAIT_CURSOR);
            errorMessageJLabel.setText(getString("CheckingPassword"));
            errorMessageJLabel.paintImmediately(0, 0, errorMessageJLabel
                    .getWidth(), errorMessageJLabel.getHeight());
            final Boolean passwordValid = isPasswordValid();
            SwingUtil.setCursor(this, null);
            if (passwordValid) {
                disposeWindow();
                updatePassword();
            }
        }
    }//GEN-LAST:event_okJButtonActionPerformed

    private void oldPasswordJPasswordFieldActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oldPasswordJPasswordFieldActionPerformed
        okJButtonActionPerformed(evt);
    }//GEN-LAST:event_oldPasswordJPasswordFieldActionPerformed

    private void reloadConfirmNewPassword() {
        confirmNewPasswordJPasswordField.setText("");
    }

    /**
     * Reload the new password text control.
     *
     */
    private void reloadNewPassword() {
        newPasswordJPasswordField.setText("");
    }

    /**
     * Reload the password text control.
     *
     */
    private void reloadPassword() {
        oldPasswordJPasswordField.setText("");
    }

    /**
     * Run the update profile password action.
     *
     */
    private void updatePassword() {
        final Credentials credentials = extractCredentials();
        final String newPassword = extractNewPassword();
        final String newPasswordConfirm = extractConfirmNewPassword();
        getController().runUpdateProfilePassword(credentials, newPassword,
                newPasswordConfirm);
    }

    /**
     * Validate input.
     * 
     * @param ignoreFocus
     *            A <code>Boolean</code> to ignore focus or not.
     */
    private void validateInput(final Boolean ignoreFocus) {
        super.validateInput();
        final String password = extractPassword();
        final String newPassword = extractNewPassword();
        final String confirmNewPassword = extractConfirmNewPassword();
        final int minimumPasswordLength = profileConstraints.getPassword().getMinLength();

        if (null == password) {
            addInputError(Separator.Space.toString());
        }

        if (null == newPassword) {
            addInputError(Separator.Space.toString());
        } else if (newPassword.length() < minimumPasswordLength) {
            if (ignoreFocus || !newPasswordJPasswordField.isFocusOwner()) {
                addInputError(getString("ErrorPasswordTooShort", new Object[] {minimumPasswordLength}));
            }
        }

        if (null == confirmNewPassword) {
            addInputError(Separator.Space.toString());
        } else if (null != newPassword && !newPassword.equals(confirmNewPassword)) {
            if (ignoreFocus || !confirmNewPasswordJPasswordField.isFocusOwner()) {
                addInputError(getString("ErrorPasswordsDoNotMatch"));
            }
        }

        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
        }
        okJButton.setEnabled(!containsInputErrors());
    }
}
