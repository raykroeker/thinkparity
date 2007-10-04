/*
 * SignupPaymentPlanAvatar.java
 *
 * Created on October 3, 2007, 12:32 PM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

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
    private final javax.swing.JPasswordField passwordJPasswordField = TextFactory.createPassword(Fonts.DialogFont);
    private final javax.swing.JTextField usernameJTextField = TextFactory.create(Fonts.DialogFont);
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
        return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_SUMMARY);
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
                createProfile();

                if (containsInputErrors()) {
                    return Boolean.FALSE;
                } else {
                    return Boolean.TRUE;
                }
            }
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#saveData()
     *
     */
    @Override
    public void saveData() {
        ((Data) input).set(DataKey.PAYMENT_CREDENTIALS, extractCredentials());
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.DefaultSignupPage#setDefaultFocus()
     *
     */
    @Override
    public void setDefaultFocus() {
        usernameJTextField.requestFocusInWindow();
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

        final String username = extractUsername();
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
     * Add validation listeners to the input controls.
     * 
     */
    private void addValidationListeners() {
        addValidationListener(usernameJTextField);
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
        if (isSetUsername() && isSetPassword()) {
            credentials = new PaymentPlanCredentials();
            credentials.setName(extractUsername());
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
     * Extract the username.
     * 
     * @return A <code>String</code>.
     */
    private String extractUsername() {
        return SwingUtil.extract(usernameJTextField, Boolean.TRUE);
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
        usernameJTextField.addFocusListener(listener);
        passwordJPasswordField.addFocusListener(listener);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.JLabel usernameJLabel = LabelFactory.create(Fonts.DialogFont);
        final javax.swing.JLabel passwordJLabel = LabelFactory.create(Fonts.DialogFont);
        final javax.swing.JLabel eaJLabel = LabelFactory.create(Fonts.DialogFont);

        setOpaque(false);
        usernameJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PaymentPlan.UsernameLabel"));

        passwordJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PaymentPlan.PasswordLabel"));

        usernameJTextField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) usernameJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(constraints.getName()));

        passwordJPasswordField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) passwordJPasswordField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(constraints.getPassword()));

        eaJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PaymentPlan.EmbeddedAssistance"));
        eaJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        errorJLabel.setText("!Error!");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(usernameJLabel)
                            .addComponent(passwordJLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(usernameJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(passwordJPasswordField, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(eaJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(errorJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(eaJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameJLabel)
                    .addComponent(usernameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
     * Determine if the username is set.
     * 
     * @return True if it is set.
     */
    private boolean isSetUsername() {
        return null != SwingUtil.extract(usernameJTextField);
    }
}
