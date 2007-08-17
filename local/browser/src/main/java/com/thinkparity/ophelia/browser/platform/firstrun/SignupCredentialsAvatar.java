/*
 * SignupCredentialsAvatar.java
 *
 * Created on April 21, 2007, 3:48 PM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

import java.util.List;

import javax.swing.text.AbstractDocument;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.text.JTextFieldLengthFilter;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.ProfileConstraints;
import com.thinkparity.codebase.model.session.Credentials;

import com.thinkparity.ophelia.model.workspace.InitializeMediator;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.component.TextFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;

/**
 *
 * @author  user
 */
public class SignupCredentialsAvatar extends DefaultSignupPage
        implements InitializeMediator, LoginCredentialsDisplay {

    /** The list of <code>Feature</code>. */
    private List<Feature> features;

    /** An instance of <code>ProfileConstraints</code>. */
    private final ProfileConstraints profileConstraints;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel forgotPasswordJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JPasswordField passwordJPasswordField = new javax.swing.JPasswordField();
    private final javax.swing.JTextField usernameJTextField = TextFactory.create();
    // End of variables declaration//GEN-END:variables

    /** Creates new form SignupCredentialsAvatar */
    public SignupCredentialsAvatar() {
        super("SignupAvatar.Credentials", BrowserConstants.DIALOGUE_BACKGROUND);
        this.profileConstraints = ProfileConstraints.getInstance();
        initComponents();
        addValidationListener(usernameJTextField);
        addValidationListener(passwordJPasswordField);
        SignupLoginHelper.getInstance().setLoginCredentialsDisplay(this);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.InitializeMediator#confirmRestore(java.util.List)
     */
    public Boolean confirmRestore(final List<Feature> features) {
        setFeatures(features);
        saveData();
        SwingUtil.setCursor(this, null);
        signupDelegate.enableNextButton(Boolean.TRUE);
        resetProgressBar();
        signupDelegate.setNextPage();
        synchronized (signupDelegate) {
            try {
                signupDelegate.wait();
                if (!signupDelegate.isCancelled()) {
                    installProgressBar();
                    setVisibleButtons(Boolean.FALSE);
                }
            } catch (final Throwable t) {
                throw new BrowserException(
                        "Error opening the thinkParity signup delegate.", t);
            }
            return !signupDelegate.isCancelled();
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     * 
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_PLATFORM_SIGNUP_CREDENTIALS;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getNextPageName()
     */
    public String getNextPageName() {
        return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_LOGIN);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getPreviousPageName()
     */
    public String getPreviousPageName() {
        return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_INTRO);
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
        // Don't change to the next page the normal way. Instead, let the
        // SignupLoginHelper direct the change to the next page.
        return Boolean.FALSE;
    }

    /**
     * Determine if features have been set.
     * 
     * @return true if features have been set.
     */
    public Boolean isSetFeatures() {
        return (null != features);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        validateInput();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#saveData()
     */
    public void saveData() {
        ((Data) input).set(SignupData.DataKey.CREDENTIALS, extractCredentials());
        if (isSetFeatures()) {
            ((Data) input).set(SignupData.DataKey.FEATURES, features);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.DefaultSignupPage#setDefaultFocus()
     */
    @Override
    public void setDefaultFocus() {
        usernameJTextField.requestFocusInWindow();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.LoginCredentialsDisplay#setError(java.lang.String)
     */
    public void setError(final String errorMessageKey) {
        String message = getSharedString(errorMessageKey);
        if (message.startsWith("!")) {
            message = getString(errorMessageKey);
        }
        errorMessageJLabel.setText(message);
        SwingUtil.setCursor(this, null);
        signupDelegate.enableNextButton(Boolean.TRUE);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#validateInput()
     *
     */
    @Override
    public final void validateInput() {
        super.validateInput();
        final String username = extractUsername();
        final String password = extractPassword();

        if (null == username) {
            addInputError(Separator.Space.toString());
        }
        if (null == password) {
            addInputError(Separator.Space.toString());
        }

        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
        }
        if (isSignupDelegateInitialized()) {
            signupDelegate.enableNextButton(!containsInputErrors());
        }
    }

    /**
     * Create a thinkParity swing monitor.
     * 
     * @return A <code>ThinkParitySwingMonitor</code>.
     */
    private ThinkParitySwingMonitor createMonitor() {
        final LoginSwingDisplay display = SignupLoginHelper.getInstance().getLoginSwingDisplay();
        Assert.assertNotNull("The login swing display null.", display);
        return new LoginSwingMonitor(display);
    }

    /**
     * Extract the credentials.
     * 
     * @return An instance of <code>Credentials</code>.
     */
    private Credentials extractCredentials() {
        final Credentials credentials = new Credentials();
        credentials.setPassword(extractPassword());
        credentials.setUsername(extractUsername().toLowerCase());
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

    /**
     * Extract the username from the control.
     * 
     * @return The username <code>String</code>.
     */
    private String extractUsername() {
        return SwingUtil.extract(usernameJTextField, Boolean.TRUE);
    }

    private void forgotPasswordJLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_forgotPasswordJLabelMousePressed
        platform.runContactUs();
    }//GEN-LAST:event_forgotPasswordJLabelMousePressed

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.JLabel explanationJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel usernameJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel passwordJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel forgotPasswordExplanationJLabel = new javax.swing.JLabel();

        setOpaque(false);
        explanationJLabel.setFont(Fonts.DialogFont);
        explanationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Credentials.Explanation"));

        usernameJLabel.setFont(Fonts.DialogFont);
        usernameJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Credentials.Username"));

        usernameJTextField.setFont(Fonts.DialogTextEntryFont);
        usernameJTextField.setMaximumSize(new java.awt.Dimension(275, 2147483647));
        usernameJTextField.setMinimumSize(new java.awt.Dimension(275, 20));
        usernameJTextField.setPreferredSize(new java.awt.Dimension(275, 20));
        ((AbstractDocument) usernameJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getUsername()));

        passwordJLabel.setFont(Fonts.DialogFont);
        passwordJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Credentials.Password"));

        passwordJPasswordField.setFont(Fonts.DialogTextEntryFont);
        passwordJPasswordField.setMaximumSize(new java.awt.Dimension(275, 2147483647));
        passwordJPasswordField.setMinimumSize(new java.awt.Dimension(275, 20));
        passwordJPasswordField.setPreferredSize(new java.awt.Dimension(275, 20));
        ((AbstractDocument) passwordJPasswordField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getPassword()));

        forgotPasswordExplanationJLabel.setFont(Fonts.DialogFont);
        forgotPasswordExplanationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Credentials.ExplanationForgetPassword"));

        forgotPasswordJLabel.setFont(Fonts.DialogFont);
        forgotPasswordJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Credentials.ForgetPassword"));
        forgotPasswordJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                forgotPasswordJLabelMousePressed(evt);
            }
        });

        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
        errorMessageJLabel.setText("!error message!");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(forgotPasswordExplanationJLabel)
                            .addComponent(passwordJLabel)
                            .addComponent(usernameJLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(forgotPasswordJLabel)
                            .addComponent(usernameJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                            .addComponent(passwordJPasswordField, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE))
                        .addGap(26, 26, 26))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(explanationJLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                            .addComponent(errorMessageJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(explanationJLabel)
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameJLabel)
                    .addComponent(usernameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordJLabel)
                    .addComponent(passwordJPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(forgotPasswordExplanationJLabel)
                    .addComponent(forgotPasswordJLabel))
                .addGap(24, 24, 24)
                .addComponent(errorMessageJLabel)
                .addContainerGap(109, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Install the progress bar.
     */
    private void installProgressBar() {
        final LoginSwingDisplay display = SignupLoginHelper.getInstance().getLoginSwingDisplay();
        Assert.assertNotNull("The login swing display null.", display);
        display.installProgressBar();
    }

    /**
     * Login.
     */
    private void login() {
        saveData();
        signupDelegate.enableNextButton(Boolean.FALSE);
        SwingUtil.setCursor(this, java.awt.Cursor.WAIT_CURSOR);
        errorMessageJLabel.setText(getString("LoggingIn"));
        errorMessageJLabel.paintImmediately(0, 0, errorMessageJLabel.getWidth(), errorMessageJLabel.getHeight());
        final Credentials credentials = extractCredentials();
        platform.runLogin(credentials.getUsername(), credentials.getPassword(), createMonitor(), this);
    }

    /**
     * Reset the progress bar.
     */
    private void resetProgressBar() {
        final LoginSwingDisplay display = SignupLoginHelper.getInstance().getLoginSwingDisplay();
        Assert.assertNotNull("The login swing display null.", display);
        display.resetProgressBar();
    }

    /**
     * Set features.
     * 
     * @param features
     *            A list of <code>Feature</code>.
     */
    private void setFeatures(final List<Feature> features) {
        this.features = features;
    }

    /**
     * Show or hide the next and cancel buttons.
     * 
     * @param visible
     *            Visible <code>Boolean</code>.
     */
    private void setVisibleButtons(final Boolean visible) {
        signupDelegate.setVisibleNextButton(visible);
        signupDelegate.setVisibleCancelButton(visible);
    }
}
