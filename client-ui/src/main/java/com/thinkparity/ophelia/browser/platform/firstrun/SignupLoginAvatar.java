/*
 * SignupLoginAvatar.java
 *
 * Created on April 23, 2007, 4:29 PM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 *
 * @author  user
 */
public class SignupLoginAvatar extends DefaultSignupPage implements LoginSwingDisplay {
    
    /** Flag indicating if this is an existing account or a new account. */
    Boolean existingAccount;
    
    /** Flag indicating if backup is supported. */
    Boolean backupAccount;

    /** Creates new form SignupLoginAvatar */
    public SignupLoginAvatar() {
        super("SignupAvatar.Login", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
        SignupLoginHelper.getInstance().setSignupLoginAvatar(this);
        existingAccount = Boolean.FALSE;
        backupAccount = Boolean.FALSE;
    }

    /** Example
    final FeatureSet featureSet = (FeatureSet) ((Data) input).get(DataKey.FEATURE_SET);
    if (featureSet == FeatureSet.FREE) {
        return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_ACCOUNT);
    } else {
        return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_PAYMENT);
    }
    */
    
    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.LoginSwingDisplay#dispose()
     */
    public void dispose() {
        disposeWindow();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.LoginSwingDisplay#installProgressBar()
     */
    public void installProgressBar() {
        loginJProgressBar.setIndeterminate(true);
        progressBarJPanel.setVisible(true);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.LoginSwingDisplay#resetProgressBar()
     */
    public void resetProgressBar() {
        reload();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.LoginSwingDisplay#setDetermination(java.lang.Integer)
     */
    public void setDetermination(final Integer steps) {
        loginJProgressBar.setMinimum(0);
        loginJProgressBar.setMaximum(steps);
        loginJProgressBar.setIndeterminate(false);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.LoginSwingDisplay#setValidCredentials(java.lang.Boolean)
     */
    public void setValidCredentials(final Boolean validCredentials) {
        SignupLoginHelper.getInstance().setValidCredentials(validCredentials);
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

    private void reloadProgressBar() {
        progressBarJPanel.setVisible(false);
        /* The space is deliberate (as opposed to an empty string) in
         * order to maintain vertical spacing. */
        stepJLabel.setText(" ");
        validate();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     * 
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_PLATFORM_SIGNUP_LOGIN;
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
        return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_CREDENTIALS);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#isNextOk()
     */
    public Boolean isNextOk() {
        if (!isInputValid()) {
            return Boolean.FALSE;
        }
        // TODO
        return !containsInputErrors();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.DefaultSignupPage#isLastPage()
     */
    @Override
    public Boolean isLastPage() {
        return Boolean.TRUE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        reloadProgressBar();
        validateInput();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.DefaultSignupPage#reloadData()
     */
    @Override
    public void reloadData() {
        this.existingAccount = (Boolean) ((Data) input).get(SignupData.DataKey.EXISTING_ACCOUNT);
        reloadExplanation();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#saveData()
     */
    public void saveData() {
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
            .addComponent(stepJLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
            .addComponent(loginJProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
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
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressBarJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(explanationJLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(warningJLabel))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(warningJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(explanationJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addComponent(progressBarJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Reload the explanation field.
     */
    private void reloadExplanation() {
        final String explanation;
        if (!existingAccount) {
            explanation = getString("ExplanationNewAccount");
        } else if (backupAccount) {
            explanation = getString("ExplanationBackup");
        } else {
            explanation = getString("ExplanationNoBackup");
        }
        explanationJLabel.setText(explanation);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel explanationJLabel = new javax.swing.JLabel();
    private final javax.swing.JProgressBar loginJProgressBar = new javax.swing.JProgressBar();
    private final javax.swing.JPanel progressBarJPanel = new javax.swing.JPanel();
    private final javax.swing.JLabel stepJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel warningJLabel = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables
}
