/*
 * SignupForgotPasswordAvatar.java
 *
 * Created on April 23, 2007, 3:30 PM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;

/**
 *
 * @author  user
 */
public class SignupForgotPasswordAvatar extends DefaultSignupPage {

    /** Creates new form SignupForgotPasswordAvatar */
    public SignupForgotPasswordAvatar() {
        super("SignupAvatar.ForgotPassword", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
        initDocumentHandler();
        // TODO Display the correct security question when the page is displayed
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     * 
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_PLATFORM_SIGNUP_FORGOT_PASSWORD;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getNextPageName()
     */
    public String getNextPageName() {
        return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_CREDENTIALS);
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
        // TODO run the command that will send an email with a new password
        return !containsInputErrors();
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
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#validateInput()
     *
     */
    @Override
    public final void validateInput() {
        super.validateInput();
        final String securityAnswer = extractSecurityAnswer();
        if (null == securityAnswer) {
            addInputError(Separator.Space.toString());
        }

        if (isSignupDelegateInitialized()) {
            signupDelegate.enableNextButton(!containsInputErrors());
        }
    }

    /**
     * Extract the security answer from the control.
     * 
     * @return The security answer <code>String</code>.
     */
    private String extractSecurityAnswer() {
        return SwingUtil.extract(answerJTextField, Boolean.TRUE);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.JLabel explanationJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel answerJLabel = new javax.swing.JLabel();

        setOpaque(false);
        explanationJLabel.setFont(Fonts.DialogFont);
        explanationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ForgotPassword.Explanation"));

        questionJLabel.setFont(Fonts.DialogFont);
        questionJLabel.setText("!Security question!");

        answerJLabel.setFont(Fonts.DialogFont);
        answerJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ForgotPassword.Answer"));

        answerJTextField.setFont(Fonts.DialogTextEntryFont);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(explanationJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(answerJLabel)
                                .addGap(41, 41, 41)
                                .addComponent(answerJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(questionJLabel))
                        .addGap(18, 18, 18))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(explanationJLabel)
                .addGap(16, 16, 16)
                .addComponent(questionJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(answerJLabel)
                    .addComponent(answerJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(71, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Initialize document handler.
     */
    private void initDocumentHandler() {
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
        answerJTextField.getDocument().addDocumentListener(documentListener);
    }

    /**
     * Reload the security question.
     */
    // TODO
    /*
    private void reloadSecurityQuestion() {
        final String securityQuestion = (String) ((Data) input).get(SignupData.DataKey.SECURITY_QUESTION);
        questionJLabel.setText(securityQuestion);
    }
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JTextField answerJTextField = new javax.swing.JTextField();
    private final javax.swing.JLabel questionJLabel = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables
}
