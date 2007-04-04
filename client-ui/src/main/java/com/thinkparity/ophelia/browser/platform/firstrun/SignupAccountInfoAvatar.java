/*
 * SignupAccountInfoAvatar.java
 *
 * Created on April 2, 2007, 10:04 AM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.profile.Reservation;
import com.thinkparity.codebase.model.session.Credentials;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.BrowserPlatform;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.platform.LearnMore;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 *
 * @author  user
 */
public class SignupAccountInfoAvatar extends Avatar
        implements SignupPage {

    /** The minimum password length. */
    private static final int MINIMUM_PASSWORD_LENGTH;

    /** The minimum username length. */
    private static final int MINIMUM_USERNAME_LENGTH;

    static {
        MINIMUM_PASSWORD_LENGTH = 6;
        MINIMUM_USERNAME_LENGTH = 6;
    }

    /** The most recent unacceptable username <code>String</code>. */
    private String unacceptableUsername;

    /** The <code>Platform</code>. */
    private final Platform platform;

    /** The list of <code>Reservation</code>s. */
    private final List<Reservation> reservations;

    /** The  <code>SignupDelegate</code>. */
    private SignupDelegate signupDelegate;

    /** Creates new form SignupAccountInfoAvatar */
    public SignupAccountInfoAvatar() {
        super("SignupAvatar.AccountInfo", BrowserConstants.DIALOGUE_BACKGROUND);
        this.platform = BrowserPlatform.getInstance();
        this.reservations = new ArrayList<Reservation>();
        initComponents();
        initDocumentHandlers();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     * 
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_PLATFORM_SIGNUP_ACCOUNT;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getNextPageName()
     */
    public String getNextPageName() {
        return SignupPageId.PROFILE_INFORMATION.toString();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getPageName()
     */
    public String getPageName() {
        return getSignupPageId().toString();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getPreviousPageName()
     */
    public String getPreviousPageName() {
        return SignupPageId.LICENSE_AGREEMENT.toString();
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getState() */
    public State getState() {
        throw Assert.createNotYetImplemented("SignupAvatar.AccountInfo#getState");
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#isAvatarBackgroundImage()
     */
    @Override
    public Boolean isAvatarBackgroundImage() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#isFirstPage()
     */
    public Boolean isFirstPage() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#isLastPage()
     */
    public Boolean isLastPage() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#isNextOk()
     */
    public Boolean isNextOk() {
        if (!isInputValid()) {
            return Boolean.FALSE;
        }
        reserveUsername();
        return (!containsInputErrors());
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        reloadAccountTypeRadioButtons();
        // Don't bother validating input if the signup delegate has not been assigned yet
        // since the goal is to enable or disable the 'next' button
        if (null != signupDelegate) {
            validateInput();
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#saveData()
     */
    public void saveData() {
        ((Data) input).set(SignupData.DataKey.CREDENTIALS, extractCredentials());
        ((Data) input).set(SignupData.DataKey.RESERVATION, lookupReservation(extractUsername()));
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#setSignupDelegate(com.thinkparity.ophelia.browser.platform.firstrun.SignupDelegate)
     */
    public void setSignupDelegate(final SignupDelegate signupDelegate) {
        this.signupDelegate = signupDelegate;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.ophelia.browser.platform.util.State)
     * 
     */
    public void setState(final State state) {
        throw Assert.createNotYetImplemented("SignupAvatar.AccountInfo#setState");
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
        final String confirmPassword = extractConfirmPassword();
        if (null == username)
            addInputError(Separator.Space.toString());
        if (null != username && username.length() < MINIMUM_USERNAME_LENGTH)
            addInputError(getString("ErrorUsernameTooShort", new Object[] {MINIMUM_USERNAME_LENGTH}));
        if (null != username && null != unacceptableUsername && username.equals(unacceptableUsername))
            addInputError(getString("ErrorUsernameTaken", new Object[] {username}));
        if (null == password)
            addInputError(Separator.Space.toString());
        if (null != password && password.length() < MINIMUM_PASSWORD_LENGTH)
            addInputError(getString("ErrorPasswordTooShort", new Object[] {MINIMUM_PASSWORD_LENGTH}));
        if (null == confirmPassword)
            addInputError(Separator.Space.toString());
        if (null != password && null != confirmPassword &&
                !password.equals(confirmPassword))
            addInputError(getString("ErrorPasswordsDoNotMatch"));

        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
            errorMessageJLabel.setText(getInputErrors().get(0));
        }
        signupDelegate.enableNextButton(!containsInputErrors());
        checkUsernameJButton.setEnabled(null != username && username.length() >= MINIMUM_USERNAME_LENGTH);
    }

    /**
     * Extract the confirm password from the control.
     * 
     * @return The password <code>String</code>.
     */
    private String extractConfirmPassword() {
        return SwingUtil.extract(confirmPasswordJPasswordField, Boolean.TRUE);
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

    /**
     * Get the signup page id.
     * 
     * @return A <code>SignupPageId</code>.
     */
    private SignupPageId getSignupPageId() {
        return SignupPageId.ACCOUNT_INFORMATION;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.ButtonGroup accountTypeButtonGroup = new javax.swing.ButtonGroup();
        final javax.swing.JLabel accountTypeTitleJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel loginInfoTitleJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel usernameJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel passwordJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel confirmPasswordJLabel = new javax.swing.JLabel();

        setOpaque(false);
        accountTypeTitleJLabel.setFont(Fonts.DialogFont);
        accountTypeTitleJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.AccountInfo.AccountTypeTitle"));

        accountTypeButtonGroup.add(accountTypeGuestJRadioButton);
        accountTypeGuestJRadioButton.setFont(Fonts.DialogFont);
        accountTypeGuestJRadioButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.AccountInfo.AccountTypeGuest"));
        accountTypeGuestJRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        accountTypeGuestJRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        accountTypeGuestJRadioButton.setOpaque(false);

        accountTypeButtonGroup.add(accountTypeStandardJRadioButton);
        accountTypeStandardJRadioButton.setFont(Fonts.DialogFont);
        accountTypeStandardJRadioButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.AccountInfo.AccountTypeStandard"));
        accountTypeStandardJRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        accountTypeStandardJRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        accountTypeStandardJRadioButton.setOpaque(false);

        accountTypeButtonGroup.add(accountTypeBackupJRadioButton);
        accountTypeBackupJRadioButton.setFont(Fonts.DialogFont);
        accountTypeBackupJRadioButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.AccountInfo.AccountTypeBackup"));
        accountTypeBackupJRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        accountTypeBackupJRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        accountTypeBackupJRadioButton.setOpaque(false);

        learnMoreJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.AccountInfo.LearnMore"));
        learnMoreJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                learnMoreJLabelMousePressed(evt);
            }
        });

        loginInfoTitleJLabel.setFont(Fonts.DialogFont);
        loginInfoTitleJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.AccountInfo.LoginInfoTitle"));

        usernameJLabel.setFont(Fonts.DialogFont);
        usernameJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.AccountInfo.UsernameLabel"));

        usernameJTextField.setFont(Fonts.DialogTextEntryFont);

        checkUsernameJButton.setFont(Fonts.DialogButtonFont);
        checkUsernameJButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.AccountInfo.CheckUsernameButton"));
        checkUsernameJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkUsernameJButtonActionPerformed(evt);
            }
        });

        passwordJLabel.setFont(Fonts.DialogFont);
        passwordJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.AccountInfo.PasswordLabel"));

        passwordJPasswordField.setFont(Fonts.DialogTextEntryFont);

        confirmPasswordJLabel.setFont(Fonts.DialogFont);
        confirmPasswordJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.AccountInfo.ConfirmPasswordLabel"));

        confirmPasswordJPasswordField.setFont(Fonts.DialogTextEntryFont);

        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
        errorMessageJLabel.setText("!Error Message!");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(accountTypeTitleJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(accountTypeBackupJRadioButton)
                                .addGap(102, 102, 102)
                                .addComponent(learnMoreJLabel))
                            .addComponent(accountTypeGuestJRadioButton)
                            .addComponent(accountTypeStandardJRadioButton)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(loginInfoTitleJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(errorMessageJLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(passwordJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(usernameJLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(confirmPasswordJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(usernameJTextField)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(checkUsernameJButton))
                                            .addComponent(passwordJPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(confirmPasswordJPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(23, 23, 23)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(accountTypeTitleJLabel)
                .addGap(14, 14, 14)
                .addComponent(accountTypeGuestJRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accountTypeStandardJRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(accountTypeBackupJRadioButton)
                    .addComponent(learnMoreJLabel))
                .addGap(27, 27, 27)
                .addComponent(loginInfoTitleJLabel)
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameJLabel)
                    .addComponent(checkUsernameJButton)
                    .addComponent(usernameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordJLabel)
                    .addComponent(passwordJPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmPasswordJLabel)
                    .addComponent(confirmPasswordJPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addComponent(errorMessageJLabel)
                .addContainerGap(27, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void checkUsernameJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkUsernameJButtonActionPerformed
        reserveUsername();
    }//GEN-LAST:event_checkUsernameJButtonActionPerformed

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
        confirmPasswordJPasswordField.getDocument().addDocumentListener(documentListener);
    }

    /**
     * Determine if the username has been reserved.
     * 
     * @param username
     *            The username <code>String</code>.
     * @return true if the username has been reserved, false otherwise.
     */
    private Boolean isReserved(final String username) {
        return (null != lookupReservation(username));
    }

    private void learnMoreJLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_learnMoreJLabelMousePressed
        platform.runLearnMore(LearnMore.Topic.SIGNUP);
    }//GEN-LAST:event_learnMoreJLabelMousePressed

    /**
     * Lookup the reservation. Returns null if not found.
     * 
     * @param username
     *            The username <code>String</code>.
     * @return The <code>Reservation</code>.
     */
    private Reservation lookupReservation(final String username) {
        for (final Reservation reservation : reservations) {
            if (reservation.getUsername().equals(username)) {
                return reservation;
            }
        }
        return null;
    }

    /**
     * Reload the account type radio buttons.
     */
    private void reloadAccountTypeRadioButtons() {
        accountTypeStandardJRadioButton.setSelected(true);
    }

    /**
     * Reserve the username.
     */
    private void reserveUsername() {
        final String username = extractUsername();
        if (isReserved(username)) {
            showUsernameReservedMessage(username);
        } else {
            final Reservation reservation = ((SignupProvider) contentProvider).readReservation(username);
            if (null == reservation) {
                unacceptableUsername = username;
                validateInput();
            } else {
                reservations.add(reservation);
                showUsernameReservedMessage(username);
            }
        }
    }

    /**
     * Show the username reserved message.
     * 
     * @param username
     *            The username <code>String</code>.
     */
    private void showUsernameReservedMessage(final String username) {
        errorMessageJLabel.setForeground(Colours.DIALOG_TEXT_FG);
        errorMessageJLabel.setText(getString("UsernameReservedMessage", new Object[] {username}));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JRadioButton accountTypeBackupJRadioButton = new javax.swing.JRadioButton();
    private final javax.swing.JRadioButton accountTypeGuestJRadioButton = new javax.swing.JRadioButton();
    private final javax.swing.JRadioButton accountTypeStandardJRadioButton = new javax.swing.JRadioButton();
    private final javax.swing.JButton checkUsernameJButton = ButtonFactory.create();
    private final javax.swing.JPasswordField confirmPasswordJPasswordField = new javax.swing.JPasswordField();
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel learnMoreJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JPasswordField passwordJPasswordField = new javax.swing.JPasswordField();
    private final javax.swing.JTextField usernameJTextField = new javax.swing.JTextField();
    // End of variables declaration//GEN-END:variables
    
}
