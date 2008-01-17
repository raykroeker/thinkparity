/*
 * SignupCredentialsAvatar.java
 *
 * Created on April 21, 2007, 3:48 PM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.text.AbstractDocument;

import com.thinkparity.common.StringUtil.Separator;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.email.EMailFormatException;
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
 * @author robert@thinkparity.com
 */
public class SignupCredentialsAvatar extends DefaultSignupPage
        implements InitializeMediator, LoginCredentialsDisplay {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JTextField emailJTextField = TextFactory.create();
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel forgotPasswordJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JPasswordField passwordJPasswordField = new javax.swing.JPasswordField();
    // End of variables declaration//GEN-END:variables

    /** The list of <code>Feature</code>. */
    private List<Feature> features;

    /** An instance of <code>ProfileConstraints</code>. */
    private final ProfileConstraints profileConstraints;

    /** Creates new form SignupCredentialsAvatar */
    public SignupCredentialsAvatar() {
        super("SignupAvatar.Credentials", BrowserConstants.DIALOGUE_BACKGROUND);
        this.profileConstraints = ProfileConstraints.getInstance();
        initComponents();
        addValidationListener(emailJTextField);
        addValidationListener(passwordJPasswordField);
        initFocusListener();
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
                    setVisibleButtons(Boolean.FALSE);
                    installProgressBar();
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
        emailJTextField.requestFocusInWindow();
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
        credentials.setEMail(extractEMail());
        return credentials;
    }

    /**
     * Extract the input e-mail from the control.
     * 
     * @return The <code>EMail</code>.
     */
    private EMail extractEMail() {
        final String emailAddress = extractEMailAddress();
        if (null == emailAddress) {
            return null;
        } else {
            try {
                return EMailBuilder.parse(emailAddress);
            } catch (final EMailFormatException emfx) {
                return null;
            }
        }
    }

    /**
     * Extract the input e-mail address string from the control.
     * 
     * @return The e-mail address <code>String</code>.
     */
    private String extractEMailAddress() {
        return SwingUtil.extract(emailJTextField, Boolean.TRUE);
    }

    /**
     * Extract the password from the control.
     * 
     * @return The password <code>String</code>.
     */
    private String extractPassword() {
        return SwingUtil.extract(passwordJPasswordField, Boolean.TRUE);
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
        final javax.swing.JLabel emailJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel passwordJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel forgotPasswordExplanationJLabel = new javax.swing.JLabel();

        setOpaque(false);
        explanationJLabel.setFont(Fonts.DialogFont);
        explanationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Credentials.Explanation"));

        emailJLabel.setFont(Fonts.DialogFont);
        emailJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Credentials.EMail"));

        emailJTextField.setFont(Fonts.DialogTextEntryFont);
        emailJTextField.setMaximumSize(new java.awt.Dimension(275, 2147483647));
        emailJTextField.setMinimumSize(new java.awt.Dimension(275, 20));
        emailJTextField.setPreferredSize(new java.awt.Dimension(275, 20));
        ((AbstractDocument) emailJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getEMail()));

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
        errorMessageJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

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
                            .addComponent(emailJLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(forgotPasswordJLabel)
                            .addComponent(emailJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                            .addComponent(passwordJPasswordField, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE))
                        .addGap(26, 26, 26))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(errorMessageJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                            .addComponent(explanationJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(explanationJLabel)
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailJLabel)
                    .addComponent(emailJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordJLabel)
                    .addComponent(passwordJPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(forgotPasswordExplanationJLabel)
                    .addComponent(forgotPasswordJLabel))
                .addGap(24, 24, 24)
                .addComponent(errorMessageJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(94, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Initialize the focus listener.
     */
    private void initFocusListener() {
        final FocusListener focusListener = new FocusListener() {
            public void focusGained(FocusEvent e) {
                validateInput();
            }
            public void focusLost(FocusEvent e) {
                validateInput();
            }
        };
        emailJTextField.addFocusListener(focusListener);
        passwordJPasswordField.addFocusListener(focusListener);
    }

    /**
     * Install the progress bar.
     */
    private void installProgressBar() {
        final LoginSwingDisplay display = SignupLoginHelper.getInstance().getLoginSwingDisplay();
        Assert.assertNotNull("The login swing display null.", display);
        display.installProgressBar(null);
    }

    /**
     * Determine if the input email is valid.
     * 
     * @return True if the input email is valid; false otherwise.
     */
    private Boolean isEmailAddressValid() {
        final String emailAddress = extractEMailAddress();
        if (null == emailAddress) {
            return Boolean.FALSE;
        } else {
            try {
                return null != EMailBuilder.parse(emailAddress);
            } catch (final EMailFormatException emfx) {
                return Boolean.FALSE;
            }
        }
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
        platform.runLogin(extractCredentials(), createMonitor(), this);
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

    /**
     * Validate input.
     * 
     * @param ignoreFocus
     *            A <code>Boolean</code> to ignore focus or not.
     */
    private void validateInput(final Boolean ignoreFocus) {
        super.validateInput();
        final String emailAddress = extractEMailAddress();
        final String password = extractPassword();

        if (null == emailAddress) {
            addInputError(Separator.Space.toString());
        } else if (!isEmailAddressValid()) {
            if (ignoreFocus || !emailJTextField.isFocusOwner()) {
                addInputError(getString("ErrorInvalidEmail"));
            }
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
}
