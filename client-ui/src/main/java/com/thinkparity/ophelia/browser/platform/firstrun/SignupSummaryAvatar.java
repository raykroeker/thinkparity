/*
 * SignupSummaryAvatar.java
 *
 * Created on April 20, 2007, 10:01 AM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

import java.awt.Graphics;
import java.util.List;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.session.Credentials;

import com.thinkparity.ophelia.model.workspace.InitializeMediator;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.firstrun.SignupData.DataKey;

/**
 *
 * @author  user
 */
public class SignupSummaryAvatar extends DefaultSignupPage implements
        LoginSwingDisplay, InitializeMediator {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JProgressBar loginJProgressBar = new javax.swing.JProgressBar();
    private final javax.swing.JPanel progressBarJPanel = new javax.swing.JPanel();
    private final javax.swing.JLabel stepJLabel = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables

    /** Creates new form SignupSummaryAvatar */
    public SignupSummaryAvatar() {
        super("SignupAvatar.Summary", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.InitializeMediator#confirmRestore(java.util.List)
     */
    public Boolean confirmRestore(final List<Feature> features) {
        // InitializeMediator method. For a new account, this method should not be called.
        return Boolean.TRUE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.LoginSwingDisplay#dispose()
     */
    public void dispose() {
        // LoginSwingDisplay method, the login is complete so finish up.
        disposeWindow();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     * 
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_PLATFORM_SIGNUP_SUMMARY;
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
        // User is not allowed to go back once the account is created.
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.LoginSwingDisplay#installProgressBar()
     */
    public void installProgressBar() {
        loginJProgressBar.setIndeterminate(true);
        progressBarJPanel.setVisible(true);
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
        if (!isInputValid()) {
            return Boolean.FALSE;
        }
        checkOnline();
        if (!isOnline(Boolean.FALSE)) {
            errorMessageJLabel.setText(getSharedString("ErrorOffline"));
            return Boolean.FALSE;
        }
        login();
        return !containsInputErrors();
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
        if (!validCredentials) {
            errorMessageJLabel.setText(getString("ErrorInvalidCredentials"));
            enableButtons(Boolean.TRUE);
            reloadProgressBar();
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
            signupDelegate.enableNextButton(Boolean.TRUE);
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
                    Images.BrowserTitle.LOGO_LARGE.getHeight(), SignupSummaryAvatar.this);
        }
        finally { g2.dispose(); }
    }

    /**
     * Enable or disable the next and cancel buttons.
     * 
     * @param enable
     *            Enable or disable <code>Boolean</code>.
     */
    private void enableButtons(final Boolean enable) {
        signupDelegate.enableNextButton(enable);
        signupDelegate.enableCancelButton(enable);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.JLabel explanationJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel importantNoteJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel explanation2JLabel = new javax.swing.JLabel();

        setOpaque(false);
        explanationJLabel.setFont(Fonts.DialogFont);
        explanationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Summary.Explanation"));

        importantNoteJLabel.setFont(Fonts.DialogFontBold);
        importantNoteJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Summary.ImportantNote"));

        explanation2JLabel.setFont(Fonts.DialogFont);
        explanation2JLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Summary.Explanation2"));
        explanation2JLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
        errorMessageJLabel.setText("!Error Message!");

        progressBarJPanel.setOpaque(false);
        stepJLabel.setFont(Fonts.DialogFont);
        stepJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Summary.progressBarJPanel.stepJLabel"));

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(importantNoteJLabel)
                            .addComponent(explanationJLabel)
                            .addComponent(explanation2JLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(progressBarJPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(errorMessageJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(166, 166, 166)
                .addComponent(explanationJLabel)
                .addGap(45, 45, 45)
                .addComponent(importantNoteJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(explanation2JLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(errorMessageJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(progressBarJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Login.
     */
    private void login() {
        enableButtons(Boolean.FALSE);
        final Credentials credentials = (Credentials) ((Data) input).get(DataKey.CREDENTIALS);
        platform.runLogin(credentials.getUsername(), credentials.getPassword(),
                new LoginSwingMonitor(this), this);
    }

    /**
     * Reload the progress bar.
     */
    private void reloadProgressBar() {
        progressBarJPanel.setVisible(false);
        stepJLabel.setText(" ");
        validate();
    }
}
