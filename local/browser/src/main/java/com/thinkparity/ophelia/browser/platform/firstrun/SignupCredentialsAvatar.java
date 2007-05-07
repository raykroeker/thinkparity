/*
 * SignupCredentialsAvatar.java
 *
 * Created on April 21, 2007, 3:48 PM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

import java.util.List;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.session.Credentials;

import com.thinkparity.ophelia.model.workspace.InitializeMediator;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;
import com.thinkparity.ophelia.browser.platform.action.platform.LearnMore;

/**
 *
 * @author  user
 */
public class SignupCredentialsAvatar extends DefaultSignupPage
        implements InitializeMediator, LoginCredentialsDisplay {

    /** The list of <code>Feature</code>. */
    private List<Feature> features;

    /** Creates new form SignupCredentialsAvatar */
    public SignupCredentialsAvatar() {
        super("SignupAvatar.Credentials", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
        initDocumentHandlers();
        SignupLoginHelper.getInstance().setLoginCredentialsDisplay(this);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.InitializeMediator#confirmRestore(java.util.List)
     */
    public Boolean confirmRestore(final List<Feature> features) {
        setFeatures(features);
        saveData();
        SwingUtil.setCursor(this, java.awt.Cursor.DEFAULT_CURSOR);
        resetProgressBar();
        signupDelegate.setNextPage();
        synchronized (signupDelegate) {
            try {
                signupDelegate.wait();
                if (!signupDelegate.isCancelled()) {
                    installProgressBar();
                    enableButtons(Boolean.FALSE);
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
     * @see com.thinkparity.ophelia.browser.platform.firstrun.LoginCredentialsDisplay#setValidCredentials(java.lang.Boolean)
     */
    public void setValidCredentials(final Boolean validCredentials) {
        if (!validCredentials) {
            SwingUtil.setCursor(this, java.awt.Cursor.DEFAULT_CURSOR);
            errorMessageJLabel.setText(getString("ErrorInvalidCredentials"));
        }
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
     * Enable or disable the next and cancel buttons.
     * 
     * @param enable
     *            Enable or disable <code>Boolean</code>.
     */
    private void enableButtons(final Boolean enable) {
        signupDelegate.enableNextButton(enable);
        signupDelegate.enableCancelButton(enable);
    }

    /**
     * Extract the credentials.
     * 
     * @return An instance of <code>Credentials</code>.
     */
    private Credentials extractCredentials() {
        final Credentials credentials = new Credentials();
        credentials.setPassword(extractPassword());
        credentials.setUsername(extractUsername());
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

        passwordJLabel.setFont(Fonts.DialogFont);
        passwordJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Credentials.Password"));

        passwordJPasswordField.setFont(Fonts.DialogTextEntryFont);

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
                    .addComponent(explanationJLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(errorMessageJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(passwordJLabel)
                                    .addComponent(usernameJLabel))
                                .addGap(100, 100, 100)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(passwordJPasswordField)
                                    .addComponent(usernameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(21, 21, 21))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(forgotPasswordExplanationJLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(forgotPasswordJLabel)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(explanationJLabel)
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameJLabel)
                    .addComponent(usernameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordJPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordJLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(forgotPasswordExplanationJLabel)
                    .addComponent(forgotPasswordJLabel))
                .addGap(24, 24, 24)
                .addComponent(errorMessageJLabel)
                .addContainerGap(113, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Initialize document handlers.
     */
    private void initDocumentHandlers() {
        final DocumentListener documentListener = new DocumentListener() {
            public void changedUpdate(final DocumentEvent e) {
                validateInput();
            }
            public void insertUpdate(final DocumentEvent e) {
                validateInput();
            }
            public void removeUpdate(final DocumentEvent e) {
                validateInput();
            }
        };
        usernameJTextField.getDocument().addDocumentListener(documentListener);
        passwordJPasswordField.getDocument().addDocumentListener(documentListener);
    }

    /**
     * Install the progress bar.
     */
    private void installProgressBar() {
        final LoginSwingDisplay display = SignupLoginHelper.getInstance().getLoginSwingDisplay();
        Assert.assertNotNull("The login swing display null.", display);
        display.installProgressBar();
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
     * Login.
     */
    private void login() {
        saveData();
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel forgotPasswordJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JPasswordField passwordJPasswordField = new javax.swing.JPasswordField();
    private final javax.swing.JTextField usernameJTextField = new javax.swing.JTextField();
    // End of variables declaration//GEN-END:variables
}
