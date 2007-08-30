/*
 * RestoreBackupAvatar.java
 *
 * Created on August 28, 2007, 10:26 AM
 */

package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.backup;

import java.awt.Cursor;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.text.AbstractDocument;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.text.JTextFieldLengthFilter;

import com.thinkparity.codebase.model.profile.ProfileConstraints;
import com.thinkparity.codebase.model.session.Credentials;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.backup.RestoreBackupProvider;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 * <b>Title:</b>thinkParity OpheliaUI Restore Backup Avatar<br>
 * <b>Description:</b><br>
 * 
 * @author robert@thinkparity.com
 */
public class RestoreBackupAvatar extends Avatar implements
        RestoreBackupSwingDisplay {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JPanel buttonBarJPanel = new javax.swing.JPanel();
    private final javax.swing.JButton cancelJButton = ButtonFactory.create();
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel explanationJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel forgotPasswordExplanationJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel forgotPasswordJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JPasswordField passwordJPasswordField = new javax.swing.JPasswordField();
    private final javax.swing.JPanel progressBarJPanel = new javax.swing.JPanel();
    private final javax.swing.JButton restoreJButton = ButtonFactory.create();
    private final javax.swing.JProgressBar restoreJProgressBar = new javax.swing.JProgressBar();
    private final javax.swing.JLabel statusJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField usernameJTextField = new javax.swing.JTextField();
    private final javax.swing.JLabel warningJLabel = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables

    /** An instance of <code>ProfileConstraints</code>. */
    private final ProfileConstraints profileConstraints;

    /** Creates new form RestoreBackupAvatar */
    public RestoreBackupAvatar() {
        super("RestoreBackupAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        this.profileConstraints = ProfileConstraints.getInstance();
        initComponents();
        addValidationListener(passwordJPasswordField);
        bindKeys();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.PublishContainerSwingDisplay#dispose()
     */
    public void dispose() {
        showBusyIndicators(Boolean.FALSE);
        disposeWindow();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     */
    @Override
    public AvatarId getId() {
        return AvatarId.DIALOG_BACKUP_RESTORE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getState()
     */
    @Override
    public State getState() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.backup.RestoreBackupSwingDisplay#installProgressBar()
     */
    public void installProgressBar() {
        restoreJProgressBar.setIndeterminate(true);
        progressBarJPanel.setVisible(true);
        buttonBarJPanel.setVisible(false);
        setCloseButtonEnabled(Boolean.FALSE);
        forgotPasswordExplanationJLabel.setText(" ");
        forgotPasswordJLabel.setText(null);
        validate();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     * 
     */
    public void reload() {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                showBusyIndicators(Boolean.FALSE);
                reloadExplanation();
                reloadForgotPassword();
                reloadProgressBar();
                reloadCredentials();
                validateInput();
            }
        });
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.backup.RestoreBackupSwingDisplay#resetProgressBar()
     */
    public void resetProgressBar() {
        reloadForgotPassword();
        reloadProgressBar();
        validateInput();
        showBusyIndicators(Boolean.FALSE);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.backup.RestoreBackupSwingDisplay#setDetermination(java.lang.Integer)
     */
    public void setDetermination(final Integer steps) {
        restoreJProgressBar.setMinimum(0);
        restoreJProgressBar.setMaximum(steps);
        restoreJProgressBar.setIndeterminate(false);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.backup.RestoreBackupSwingDisplay#setError(java.lang.String)
     */
    public void setError(final String errorMessageKey) {
        addInputError(getString(errorMessageKey));
        errorMessageJLabel.setText(getInputErrors().get(0));
    }

    @Override
    public void setState(final State state) {
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.backup.RestoreBackupSwingDisplay#updateProgress(java.lang.Integer, java.lang.String)
     */
    public void updateProgress(final Integer step, final String note) {
        restoreJProgressBar.setValue(step);
        if (null != note) {
            statusJLabel.setText(note);
        } else {
            /* NOTE the space is deliberate (as opposed to an empty string) in
             * order to maintain vertical spacing. */
            statusJLabel.setText(" ");
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#validateInput()
     */
    @Override
    protected void validateInput() {
        super.validateInput();

        final String password = extractPassword();
        if (null == password) {
            addInputError(Separator.Space.toString());
        }

        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
        }
        restoreJButton.setEnabled(!containsInputErrors());
    }

    /**
     * Bind keys to actions.
     */
    private void bindKeys() {
        bindEscapeKey("Cancel", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });
        bindEnterKey("Restore", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                if (cancelJButton.isFocusOwner()) {
                    cancelJButtonActionPerformed(e);
                } else {
                    restoreJButtonActionPerformed(e);
                }
            }
        });
    }

    private void cancelJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    /**
     * Create a swing monitor.
     * 
     * @return A <code>ThinkParitySwingMonitor</code>.
     */
    private ThinkParitySwingMonitor createMonitor() {
        return new RestoreBackupSwingMonitor(this);
    }

    /**
     * Enable or disable text entry in the dialog.
     * 
     * @param enable
     *            The enable <code>Boolean</code>.
     */
    private void enableTextEntry(final Boolean enable) {
        setEditable(passwordJPasswordField, enable);
    }

    /**
     * Extract the credentials.
     * 
     * @return An instance of <code>Credentials</code>.
     */
    private Credentials extractCredentials() {
        final Credentials credentials = new Credentials();
        credentials.setPassword(extractPassword());
        credentials.setUsername(readUsername());
        return credentials;
    }

    /**
     * Extract the password from the control.
     * 
     * @return The password <code>String</code>.
     */
    private String extractPassword() {
        return SwingUtil.extract(passwordJPasswordField, Boolean.TRUE);
    }

    private void forgotPasswordJLabelMousePressed(final java.awt.event.MouseEvent evt) {//GEN-FIRST:event_forgotPasswordJLabelMousePressed
        getController().runContactUs();
    }//GEN-LAST:event_forgotPasswordJLabelMousePressed

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.JLabel usernameJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel passwordJLabel = new javax.swing.JLabel();

        warningJLabel.setFont(Fonts.DialogFontBold);
        warningJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("RestoreBackupAvatar.WarningNoBackup"));

        explanationJLabel.setFont(Fonts.DialogFont);
        explanationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("RestoreBackupAvatar.ExplanationNoBackup"));
        explanationJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        usernameJLabel.setFont(Fonts.DialogFont);
        usernameJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("RestoreBackupAvatar.Username"));

        usernameJTextField.setEditable(false);
        usernameJTextField.setFont(Fonts.DialogFont);
        usernameJTextField.setText("!username!");
        usernameJTextField.setFocusable(false);
        usernameJTextField.setOpaque(false);

        passwordJLabel.setFont(Fonts.DialogFont);
        passwordJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("RestoreBackupAvatar.Password"));

        passwordJPasswordField.setFont(Fonts.DialogTextEntryFont);
        ((AbstractDocument) passwordJPasswordField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getPassword()));

        forgotPasswordExplanationJLabel.setFont(Fonts.DialogFont);
        forgotPasswordExplanationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("RestoreBackupAvatar.ExplanationForgetPassword"));

        forgotPasswordJLabel.setFont(Fonts.DialogFont);
        forgotPasswordJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("RestoreBackupAvatar.ForgetPassword"));
        forgotPasswordJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                forgotPasswordJLabelMousePressed(evt);
            }
        });

        progressBarJPanel.setOpaque(false);
        statusJLabel.setFont(Fonts.DialogFont);
        statusJLabel.setText("Restoring data...");

        restoreJProgressBar.setBorder(null);
        restoreJProgressBar.setBorderPainted(false);
        restoreJProgressBar.setOpaque(false);

        javax.swing.GroupLayout progressBarJPanelLayout = new javax.swing.GroupLayout(progressBarJPanel);
        progressBarJPanel.setLayout(progressBarJPanelLayout);
        progressBarJPanelLayout.setHorizontalGroup(
            progressBarJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(progressBarJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(progressBarJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(restoreJProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addComponent(statusJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE))
                .addContainerGap())
        );
        progressBarJPanelLayout.setVerticalGroup(
            progressBarJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, progressBarJPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(statusJLabel)
                .addGap(12, 12, 12)
                .addComponent(restoreJProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        buttonBarJPanel.setOpaque(false);
        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
        errorMessageJLabel.setText("!Error Message!");
        errorMessageJLabel.setPreferredSize(new java.awt.Dimension(3, 14));

        restoreJButton.setFont(Fonts.DialogButtonFont);
        restoreJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("RestoreBackupAvatar.Restore"));
        restoreJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restoreJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setFont(Fonts.DialogButtonFont);
        cancelJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("RestoreBackupAvatar.Cancel"));
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonBarJPanelLayout = new javax.swing.GroupLayout(buttonBarJPanel);
        buttonBarJPanel.setLayout(buttonBarJPanelLayout);
        buttonBarJPanelLayout.setHorizontalGroup(
            buttonBarJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonBarJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buttonBarJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(errorMessageJLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(buttonBarJPanelLayout.createSequentialGroup()
                        .addComponent(restoreJButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelJButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        buttonBarJPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelJButton, restoreJButton});

        buttonBarJPanelLayout.setVerticalGroup(
            buttonBarJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonBarJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(errorMessageJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(buttonBarJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelJButton)
                    .addComponent(restoreJButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(progressBarJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(buttonBarJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(warningJLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(usernameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(explanationJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(passwordJPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(passwordJLabel)
                    .addComponent(usernameJLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(forgotPasswordExplanationJLabel)
                        .addGap(19, 19, 19)
                        .addComponent(forgotPasswordJLabel)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {passwordJPasswordField, usernameJTextField});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addComponent(warningJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(explanationJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameJLabel)
                    .addComponent(usernameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordJLabel)
                    .addComponent(passwordJPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(forgotPasswordExplanationJLabel)
                    .addComponent(forgotPasswordJLabel))
                .addGap(14, 14, 14)
                .addComponent(buttonBarJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBarJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Determine if the backup feature is available.
     * 
     * @return True if backup is available for the profile.
     */
    private Boolean isBackupEnabled() {
        return ((RestoreBackupProvider)contentProvider).isBackupEnabled();
    }

    /**
     * Get the username.
     * 
     * @return The username <code>String</code>.             
     */
    private String readUsername() {
        return ((RestoreBackupProvider)contentProvider).readUsername();
    }

    /**
     * Reload the username and password controls.
     */
    private void reloadCredentials() {
        usernameJTextField.setText(readUsername());
        passwordJPasswordField.setText("");
    }

    /**
     * Reload the explanation field.
     */
    private void reloadExplanation() {
        if (isBackupEnabled()) {
            warningJLabel.setText(getString("WarningBackup"));
            explanationJLabel.setText(getString("ExplanationBackup"));
        } else {
            warningJLabel.setText(getString("WarningNoBackup"));
            explanationJLabel.setText(getString("ExplanationNoBackup"));
        }
    }

    /**
     * Reload the forgot password fields.
     */
    private void reloadForgotPassword() {
        forgotPasswordExplanationJLabel.setText(getString("ExplanationForgetPassword"));
        forgotPasswordJLabel.setText(getString("ForgetPassword"));
    }

    /**
     * Reload the progress bar.
     */
    private void reloadProgressBar() {
        buttonBarJPanel.setVisible(true);
        setCloseButtonEnabled(Boolean.TRUE);
        progressBarJPanel.setVisible(false);
        /* NOTE the space is deliberate (as opposed to an empty string) in
         * order to maintain vertical spacing. */
        statusJLabel.setText(" ");
        validate();
    }

    private void restoreJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restoreJButtonActionPerformed
        if (isInputValid()) {
            restoreJButton.setEnabled(false);
            showBusyIndicators(Boolean.TRUE);
            getController().runRestoreBackup(createMonitor(), extractCredentials());
        }
    }//GEN-LAST:event_restoreJButtonActionPerformed

    /**
     * Show or remove a busy cursor.
     * 
     * @param busy
     *            The busy <code>Boolean</code>.
     */
    private void showBusyCursor(final Boolean busy) {
        if (busy) {
            final java.awt.Cursor cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
            SwingUtil.setCursor(this, cursor);
            SwingUtil.setCursor(passwordJPasswordField, cursor);
        } else {
            final java.awt.Cursor cursor = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
            SwingUtil.setCursor(passwordJPasswordField, cursor);
            SwingUtil.setCursor(this, null);
        }
    }

    /**
     * Show or remove busy indicators for the dialog.
     * 
     * @param busy
     *            The busy <code>Boolean</code>.
     */
    private void showBusyIndicators(final Boolean busy) {
        showBusyCursor(busy);
        enableTextEntry(!busy);
    }
}
