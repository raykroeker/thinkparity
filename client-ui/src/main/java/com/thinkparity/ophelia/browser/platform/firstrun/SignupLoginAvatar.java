/*
 * SignupLoginAvatar.java
 *
 * Created on April 23, 2007, 4:29 PM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

import java.awt.Graphics;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.migrator.Feature;

import com.thinkparity.ophelia.model.Constants.Product.Features;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 *
 * @author robert@thinkparity.com
 */
public class SignupLoginAvatar extends DefaultSignupPage implements LoginSwingDisplay {

    /** Progress bar indeterminate flag. */
    private Boolean indeterminate;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel explanationJLabel = new javax.swing.JLabel();
    private final javax.swing.JProgressBar loginJProgressBar = new javax.swing.JProgressBar();
    private final javax.swing.JPanel progressBarJPanel = new javax.swing.JPanel();
    private final javax.swing.JLabel stepJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel warningJLabel = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables

    /** Creates new form SignupLoginAvatar */
    public SignupLoginAvatar() {
        super("SignupAvatar.Login", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
        SignupLoginHelper.getInstance().setLoginSwingDisplay(this);
        indeterminate = Boolean.TRUE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.LoginSwingDisplay#dispose()
     */
    public void dispose() {
        disposeWindow();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_PLATFORM_SIGNUP_LOGIN;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.DefaultSignupPage#getKeyNextButton()
     */
    @Override
    public String getKeyNextButton() {
        return "Login.NextButton";
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getNextPageName()
     */
    public String getNextPageName() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getPreviousPageName()
     */
    public String getPreviousPageName() {
        // User is not allowed to go back once the login is in progress.
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.LoginSwingDisplay#installProgressBar(java.lang.String)
     */
    public void installProgressBar(final String message) {
        loginJProgressBar.setIndeterminate(indeterminate.booleanValue());
        if (signupDelegate.isCurrentPage(this)) {
            progressBarJPanel.setVisible(true);
        }
        if (null != message) {
            stepJLabel.setText(message);
        } else {
            stepJLabel.setText(" ");
        }
        validate();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.DefaultSignupPage#isLastPage()
     */
    @Override
    public Boolean isLastPage() {
        return Boolean.TRUE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#isNextOk()
     */
    public Boolean isNextOk() {
        // The next button is handled in a special way on this page.
        // See confirmRestore() in SignupCredentialsAvatar.
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        reloadProgressBar();
        setVisibleButtons(Boolean.TRUE);
        validateInput();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.DefaultSignupPage#reloadData()
     */
    @Override
    public void reloadData() {
        reloadExplanation();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.LoginSwingDisplay#resetProgressBar()
     */
    public void resetProgressBar() {
        reload();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#saveData()
     */
    public void saveData() {
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.DefaultSignupPage#setDefaultFocus()
     */
    @Override
    public void setDefaultFocus() {
        signupDelegate.setFocusNextButton();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.LoginSwingDisplay#setDetermination(java.lang.Integer)
     */
    public void setDetermination(final Integer steps) {
        loginJProgressBar.setMinimum(0);
        loginJProgressBar.setMaximum(steps);
        indeterminate = Boolean.FALSE;
        loginJProgressBar.setIndeterminate(indeterminate.booleanValue());
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.LoginSwingDisplay#setError(java.lang.String)
     */
    public void setError(final String errorMessageKey) {
        if (signupDelegate.isCurrentPage(this)) {
            String message = getSharedString(errorMessageKey);
            if (message.startsWith("!")) {
                message = getString(errorMessageKey);
            }
            errorMessageJLabel.setText(message);
        } else {
            final LoginCredentialsDisplay display = SignupLoginHelper.getInstance().getLoginCredentialsDisplay();
            Assert.assertNotNull("The login credentials display null.", display);
            display.setError(errorMessageKey);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.LoginSwingDisplay#updateProgress(java.lang.Integer, java.lang.String)
     */
    public void updateProgress(final Integer step, final String note) {
        loginJProgressBar.setValue(step);
        if (null != note && 0 < note.trim().length()) {
            stepJLabel.setText(note);
        } else {
            stepJLabel.setText(" ");
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#validateInput()
     *
     */
    @Override
    public final void validateInput() {
        super.validateInput();

        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
        }
        if (isSignupDelegateInitialized()) {
            signupDelegate.enableNextButton(!containsInputErrors());
        }
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics g2 = g.create();
        try {
            // Draw the logo.
            g2.drawImage(Images.BrowserTitle.LOGO_LARGE,
                    (getWidth() - Images.BrowserTitle.LOGO_LARGE.getWidth()) / 2, 35,
                    Images.BrowserTitle.LOGO_LARGE.getWidth(),
                    Images.BrowserTitle.LOGO_LARGE.getHeight(), SignupLoginAvatar.this);
        }
        finally { g2.dispose(); }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setOpaque(false);
        warningJLabel.setFont(Fonts.DialogFontBold);
        warningJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Login.WarningNoBackup"));

        explanationJLabel.setFont(Fonts.DialogFont);
        explanationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Login.ExplanationNoBackup"));
        explanationJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
        errorMessageJLabel.setText("!Error Message!");

        progressBarJPanel.setOpaque(false);
        stepJLabel.setFont(Fonts.DialogFont);
        stepJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Login.progressBarJPanel.stepJLabel"));

        loginJProgressBar.setBorder(null);
        loginJProgressBar.setBorderPainted(false);
        loginJProgressBar.setMaximumSize(new java.awt.Dimension(32767, 23));
        loginJProgressBar.setMinimumSize(new java.awt.Dimension(10, 23));
        loginJProgressBar.setOpaque(false);
        loginJProgressBar.setPreferredSize(new java.awt.Dimension(146, 23));

        javax.swing.GroupLayout progressBarJPanelLayout = new javax.swing.GroupLayout(progressBarJPanel);
        progressBarJPanel.setLayout(progressBarJPanelLayout);
        progressBarJPanelLayout.setHorizontalGroup(
            progressBarJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(stepJLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
            .addComponent(loginJProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
        );
        progressBarJPanelLayout.setVerticalGroup(
            progressBarJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(progressBarJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(stepJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loginJProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(explanationJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(warningJLabel)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(progressBarJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(errorMessageJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(166, 166, 166)
                .addComponent(warningJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(explanationJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(errorMessageJLabel)
                .addGap(17, 17, 17)
                .addComponent(progressBarJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Determine if backup is enabled.
     * 
     * @param features
     *            A list of <code>Feature</code>.
     * @return true if backup is enabled.
     */
    private Boolean isBackupEnabled(final List<Feature> features) {
        for (final Feature feature : features) {
            if (Features.BACKUP.equals(feature.getName())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Reload the explanation field.
     */
    private void reloadExplanation() {
        final Boolean existingAccount = (Boolean)((Data) input).get(SignupData.DataKey.EXISTING_ACCOUNT);
        final List<Feature> features = ((Data) input).getList(SignupData.DataKey.FEATURES);
        final Boolean backupEnabled = isBackupEnabled(features);
        final String warning;
        final String explanation;
        if (!existingAccount) {
            warning = getString("WarningNewAccount");
            explanation = getString("ExplanationNewAccount");
        } else if (backupEnabled) {
            warning = getString("WarningBackup");
            explanation = getString("ExplanationBackup");
        } else {
            warning = getString("WarningNoBackup");
            explanation = getString("ExplanationNoBackup");
        }
        warningJLabel.setText(warning);
        explanationJLabel.setText(explanation);
    }

    /**
     * Reload the progress bar (make it invisible).
     */
    private void reloadProgressBar() {
        progressBarJPanel.setVisible(false);
        /* The space is deliberate (as opposed to an empty string) in
         * order to maintain vertical spacing. */
        stepJLabel.setText(" ");
        validate();
    }

    /**
     * Show or hide the next and cancel buttons.
     * 
     * @param visible
     *            Visible <code>Boolean</code>.
     */
    private void setVisibleButtons(final Boolean visible) {
        if (isSignupDelegateInitialized()) {
            signupDelegate.setVisibleNextButton(visible);
            signupDelegate.setVisibleCancelButton(visible);
        }
    }
}
