/*
 * SignupPaymentPlanAvatar.java
 *
 * Created on October 3, 2007, 12:32 PM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.text.AbstractDocument;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.constraint.IllegalValueException;
import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.text.JTextFieldLengthFilter;

import com.thinkparity.codebase.model.profile.payment.PaymentPlanCredentials;
import com.thinkparity.codebase.model.profile.payment.PaymentPlanCredentialsConstraints;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;

import com.thinkparity.ophelia.model.profile.ReservationExpiredException;
import com.thinkparity.ophelia.model.session.OfflineException;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.component.TextFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.firstrun.SignupData.DataKey;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 * <b>Title:</b>thinkParity Ophelia UI Platform First Run Payment Plan Avatar<br>
 * <b>Description:</b>The avatar that allows a user to select an existing
 * payment plan by specifying a username/password.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class SignupPaymentPlanAvatar extends DefaultSignupPage {
    
    /** Input constraisnts. */
    private static final PaymentPlanCredentialsConstraints constraints;
    
    static {
        constraints = PaymentPlanCredentialsConstraints.getInstance();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel errorJLabel = LabelFactory.create(Fonts.DialogFont, Colours.DIALOG_ERROR_TEXT_FG);
    private final javax.swing.JRadioButton infoJRadioButton = new javax.swing.JRadioButton();
    private final javax.swing.JTextField nameJTextField = TextFactory.create(Fonts.DialogFont);
    private final javax.swing.JPasswordField passwordJPasswordField = TextFactory.createPassword(Fonts.DialogFont);
    private final javax.swing.JRadioButton planJRadioButton = new javax.swing.JRadioButton();
    // End of variables declaration//GEN-END:variables

    /**
     * Create SignupPaymentPlanAvatar.
     *
     */
    public SignupPaymentPlanAvatar() {
        super("SignupAvatar.PaymentPlan", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
        initComponentFocusListeners();
        addValidationListeners();
        addSelectionListeners();
    }

    /**
     * Obtain the avatar id.
     * 
     * 
     * @return The avatar id.
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_PLATFORM_SIGNUP_PAYMENT_PLAN;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getNextPageName()
     *
     */
    @Override
    public String getNextPageName() {
        if (infoJRadioButton.isSelected()) {
            return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_PAYMENT);
        } else if (planJRadioButton.isSelected()) {
            return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_SUMMARY);
        } else {
            throw Assert.createUnreachable("Unexpected radio selection.");
        }
    }
    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getPreviousPageName()
     *
     */
    @Override
    public String getPreviousPageName() {
        return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_ACCOUNT);
    }

    /**
     * Obtain the avatar's state information.
     * 
     * 
     * @return The avatar's state information.
     */
    public State getState() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.DefaultSignupPage#isNextOk()
     *
     */
    @Override
    public Boolean isNextOk() {
        if (super.isNextOk()) {
            if (containsInputErrors()) {
                return Boolean.FALSE;
            } else {
                if (infoJRadioButton.isSelected()) {
                    if (containsInputErrors()) {
                        return Boolean.FALSE;
                    } else {
                        return Boolean.TRUE;
                    }
                } else if (planJRadioButton.isSelected()) {
                    createProfile();

                    if (containsInputErrors()) {
                        return Boolean.FALSE;
                    } else {
                        return Boolean.TRUE;
                    }
                } else {
                    throw Assert.createUnreachable("Unexpected radio selection.");
                }
            }
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     *
     */
    @Override
    public void reload() {
        infoJRadioButton.setSelected(true);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#saveData()
     *
     */
    @Override
    public void saveData() {
        if (infoJRadioButton.isSelected()) {
            /* do nothing */
        } else if (planJRadioButton.isSelected()) {
            ((Data) input).set(DataKey.PAYMENT_CREDENTIALS, extractCredentials());
        } else {
            Assert.assertUnreachable("Unexpected radio selection.");
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.DefaultSignupPage#setDefaultFocus()
     *
     */
    @Override
    public void setDefaultFocus() {
        planJRadioButton.requestFocusInWindow();
    }

    /**
     * Set the avatar state.
     * 
     * 
     * @param state
     *            The avatar's state information.
     */
    public void setState(final State state) {
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.DefaultSignupPage#validateInput()
     *
     */
    @Override
    public void validateInput() {
        super.validateInput();

        if (infoJRadioButton.isSelected()) {
            /* no checking required */
        } else if (planJRadioButton.isSelected()) {
            final String username = extractName();
            final String password = extractPassword();
            if (null == username) {
                addInputError(getString("ErrorNoUsername"));
            } else {
                try {
                    constraints.getName().validate(username);
                } catch (final IllegalValueException ivx) {
                    switch (ivx.getReason()) {
                    case TOO_SHORT:
                        addInputError("ErrorUsernameTooShort",
                                constraints.getName().getMinLength());
                        break;
                    case FORMAT:
                    case ILLEGAL:
                    case NULL:
                    case TOO_LARGE:
                    case TOO_LONG:
                    case TOO_SMALL:
                    default:
                        Assert.assertUnreachable("Unexpected reason {0}.", ivx.getReason());
                    }
                }
            }
            if (null == password) {
                addInputError(getString("ErrorNoPassword"));
            } else {
                try {
                    constraints.getPassword().validate(password);
                } catch (final IllegalValueException ivx) {
                    switch (ivx.getReason()) {
                    case TOO_SHORT:
                        addInputError("ErrorUsernameTooShort",
                                constraints.getPassword().getMinLength());
                        break;
                    case FORMAT:
                    case ILLEGAL:
                    case NULL:
                    case TOO_LARGE:
                    case TOO_LONG:
                    case TOO_SMALL:
                    default:
                        Assert.assertUnreachable("Unexpected reason {0}.", ivx.getReason());
                    }
                }
    
            }
        } else {
            Assert.assertUnreachable("Unexpected radio selection.");
        }

        /* note that we only disable the button; we do not write error text */
        if (containsInputErrors()) {
            errorJLabel.setText(Separator.Space.toString());
            signupDelegate.enableNextButton(Boolean.FALSE);
        } else {
            errorJLabel.setText(Separator.Space.toString());
            signupDelegate.enableNextButton(Boolean.TRUE);
        }
    }

    /**
     * Add radio button selection listeners.
     * 
     */
    private void addSelectionListeners() {
        infoJRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                reloadPlanEnabled();
                validateInput();
            }
        });
        planJRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                reloadPlanEnabled();
                validateInput();
            }
        });
    }

    /**
     * Add validation listeners to the input controls.
     * 
     */
    private void addValidationListeners() {
        addValidationListener(nameJTextField);
        addValidationListener(passwordJPasswordField);
    }

    /**
     * Create the profile.
     * 
     */
    private void createProfile() {
        saveData();
        signupDelegate.enableNextButton(Boolean.FALSE);
        SwingUtil.setCursor(this, java.awt.Cursor.WAIT_CURSOR);
        errorJLabel.setText(getString("SigningUp"));
        errorJLabel.paintImmediately(0, 0, errorJLabel.getWidth(),
                errorJLabel.getHeight());
        try {
            getSignupHelper().createPlanProfile();
        } catch (final OfflineException ox) {
            logger.logError(ox, "An offline error has occured.");
            addInputError(getSharedString("ErrorOffline"));
        } catch (final InvalidCredentialsException icx) {
            logger.logWarning(icx, "The plan name/password are invalid.");
            addInputError(getString("ErrorInvalidCredentials"));
        } catch (final ReservationExpiredException rex) {
            logger.logWarning(rex, "The username/e-mail reservation has expired.");
            addInputError(getSharedString("ErrorReservationExpired"));
        } catch (final Throwable t) {
            logger.logFatal(t, "An unexpected error has occured.");
            addInputError(getSharedString("ErrorUnexpected"));
        }
        errorJLabel.setText(Separator.Space.toString());
        if (containsInputErrors()) {
            errorJLabel.setText(getInputErrors().get(0));
            signupDelegate.enableNextButton(Boolean.FALSE);
        } else {
            signupDelegate.enableNextButton(Boolean.TRUE);
        }
        SwingUtil.setCursor(this, null);
    }

    /**
     * Extract the credentials.  If either the username or the password are not
     * set; null is returned.
     * 
     * @return A <code>PaymentPlanCredentials</code>.
     */
    private PaymentPlanCredentials extractCredentials() {
        final PaymentPlanCredentials credentials;
        if (isSetName() && isSetPassword()) {
            credentials = new PaymentPlanCredentials();
            credentials.setName(extractName());
            credentials.setPassword(extractPassword());
        } else {
            credentials = null;
        }
        return credentials;
    }

    /**
     * Extract the password.
     * 
     * @return A <code>String</code>.
     */
    private String extractPassword() {
        return SwingUtil.extract(passwordJPasswordField, Boolean.TRUE);
    }

    /**
     * Extract the name.
     * 
     * @return A <code>String</code>.
     */
    private String extractName() {
        return SwingUtil.extract(nameJTextField, Boolean.TRUE);
    }

    /**
     * Initialize focus listeners on the username/password controls to validate
     * input.
     * 
     */
    private void initComponentFocusListeners() {
        final FocusListener listener = new FocusAdapter() {
            /**
             * @see java.awt.event.FocusAdapter#focusGained(java.awt.event.FocusEvent)
             *
             */
            @Override
            public void focusGained(final FocusEvent e) {
                validateInput();
            }
            /**
             * @see java.awt.event.FocusAdapter#focusLost(java.awt.event.FocusEvent)
             *
             */
            @Override
            public void focusLost(final FocusEvent e) {
                validateInput();
            }
        };
        nameJTextField.addFocusListener(listener);
        passwordJPasswordField.addFocusListener(listener);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.ButtonGroup optionButtonGroup;

        optionButtonGroup = new javax.swing.ButtonGroup();
        final javax.swing.JLabel nameJLabel = LabelFactory.create(Fonts.DialogFont);
        final javax.swing.JLabel passwordJLabel = LabelFactory.create(Fonts.DialogFont);
        final javax.swing.JLabel eaJLabel = LabelFactory.create(Fonts.DialogFont);

        setOpaque(false);
        nameJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PaymentPlan.NameLabel"));

        passwordJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PaymentPlan.PasswordLabel"));

        nameJTextField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) nameJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(constraints.getName()));

        passwordJPasswordField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) passwordJPasswordField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(constraints.getPassword()));

        eaJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PaymentPlan.EmbeddedAssistance"));
        eaJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        errorJLabel.setText("!Error!");

        optionButtonGroup.add(infoJRadioButton);
        infoJRadioButton.setFont(Fonts.DialogFont);
        infoJRadioButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PaymentPlan.OptionInfo"));
        infoJRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        infoJRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        infoJRadioButton.setOpaque(false);

        optionButtonGroup.add(planJRadioButton);
        planJRadioButton.setFont(Fonts.DialogFont);
        planJRadioButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PaymentPlan.OptionPlan"));
        planJRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        planJRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        planJRadioButton.setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(eaJLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                            .addComponent(errorJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(infoJRadioButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(passwordJLabel)
                                    .addComponent(nameJLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(passwordJPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(nameJTextField, 0, 0, Short.MAX_VALUE)))
                            .addComponent(planJRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {nameJTextField, passwordJPasswordField});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(eaJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(infoJRadioButton)
                .addGap(19, 19, 19)
                .addComponent(planJRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameJLabel)
                    .addComponent(nameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordJLabel)
                    .addComponent(passwordJPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(errorJLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Determine if the password is set.
     * 
     * @return True if it is set.
     */
    private boolean isSetPassword() {
        return null != SwingUtil.extract(passwordJPasswordField);
    }

    /**
     * Determine if the name is set.
     * 
     * @return True if it is set.
     */
    private boolean isSetName() {
        return null != SwingUtil.extract(nameJTextField);
    }

    /**
     * Reload the plan enabled based upon the selected radio button.
     * 
     */
    private void reloadPlanEnabled() {
        if (planJRadioButton.isSelected()) {
            nameJTextField.setEnabled(true);

            passwordJPasswordField.setEnabled(true);
        } else if (infoJRadioButton.isSelected()) {
            nameJTextField.setText(null);
            nameJTextField.setEnabled(false);

            passwordJPasswordField.setText(null);
            passwordJPasswordField.setEnabled(false);
        } else {
            Assert.assertUnreachable("Unexpected selection.");
        }
    }
}
