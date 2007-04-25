/*
 * SignupPaymentInfoAvatar.java
 *
 * Created on April 7, 2007, 11:26 AM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.DefaultComboBoxModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.model.profile.ReservationExpiredException;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.action.platform.LearnMore;

/**
 *
 * @author  user
 */
public class SignupPaymentInfoAvatar extends DefaultSignupPage {

    /** TODO remove this later. */
    /** The beta activation code. */
    private static String BETA_ACTIVATION_CODE;
    static {
        BETA_ACTIVATION_CODE = "thinkParity Beta 2007";
    }

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel cardMonthModel;

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel cardTypeModel;

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel cardYearModel;

    /** Final year in the year list. */
    private static final int FINAL_YEAR = 2020;

    /** Creates new form SignupPaymentInfoAvatar */
    public SignupPaymentInfoAvatar() {
        super("SignupAvatar.PaymentInfo", BrowserConstants.DIALOGUE_BACKGROUND);
        this.cardMonthModel = new DefaultComboBoxModel();
        this.cardTypeModel = new DefaultComboBoxModel();
        this.cardYearModel = new DefaultComboBoxModel();
        initCardMonthModel();
        initCardTypeModel();
        initCardYearModel();
        initComponents();
        initDocumentHandler();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     * 
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_PLATFORM_SIGNUP_PAYMENT;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getNextPageName()
     */
    public String getNextPageName() {
        return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_SUMMARY);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getPreviousPageName()
     */
    public String getPreviousPageName() {
        return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_ACCOUNT);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#isNextOk()
     */
    public Boolean isNextOk() {
        if (!isInputValid()) {
            return Boolean.FALSE;
        }
        if (!containsInputErrors()) {
            signup();
        }
        return !containsInputErrors();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    public void reload() {
        // TODO For now, hide the payment fields. When add them back, remember to make them local variables.
        explanationJLabel.setVisible(false);
        cardNameJLabel.setVisible(false);
        cardNameJTextField.setVisible(false);
        cardNumberJLabel.setVisible(false);
        cardNumberJTextField.setVisible(false);
        cardTypeJComboBox.setVisible(false);
        cardTypeJLabel.setVisible(false);
        cardMonthJComboBox.setVisible(false);
        cardYearJComboBox.setVisible(false);
        expiryDateJLabel.setVisible(false);
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
        // TODO This is temporary, remove when beta is done.
        final String betaActivationCode = SwingUtil.extract(betaActivationCodeJTextField, Boolean.TRUE);
        if (null == betaActivationCode) {
            addInputError(Separator.Space.toString());
        } else if (!betaActivationCode.equalsIgnoreCase(BETA_ACTIVATION_CODE)) {
            addInputError(getString("ErrorIncorrectBetaActivationCode"));
        }

        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
        }

        if (isSignupDelegateInitialized()) {
            signupDelegate.enableNextButton(!containsInputErrors());
        }
    }

    private void betaActivationLearnMoreJLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_betaActivationLearnMoreJLabelMousePressed
        // TODO Remove this after beta.
        platform.runLearnMore(LearnMore.Topic.BETA);
    }//GEN-LAST:event_betaActivationLearnMoreJLabelMousePressed

    /**
     * Create a new profile.
     */
    private void createProfile() {
        try {
            getSignupHelper().createProfile();
        } catch (final ReservationExpiredException rex) {
            addInputError(getString("ErrorReservationExpired"));
        } catch (final Throwable t) {
            addInputError(getString("ErrorCreateAccount"));
        }
    }

    /**
     * Initialize the list of credit card months.
     */
    private void initCardMonthModel() {
        DecimalFormat formatter = new DecimalFormat("00");
        for (int month = 1; month <= 12; month++) {
            this.cardMonthModel.addElement(formatter.format(month));
        }
    }

    /**
     * Initialize the list of credit card types.
     */
    private void initCardTypeModel() {
        for (final CardType cardType : CardType.values()) {
            final StringBuffer key = new StringBuffer("CreditCardType.").append(cardType.nameKey);
            this.cardTypeModel.addElement(getString(key.toString()));
        }
    }

    /**
     * Initialize the list of credit card years.
     */
    private void initCardYearModel() {
        final Calendar calendar = DateUtil.getInstance();
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        while (calendar.get(Calendar.YEAR) <= FINAL_YEAR) {
            this.cardYearModel.addElement(formatter.format(calendar.getTime()));
            calendar.add(Calendar.YEAR, 1);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.JLabel betaExplanationJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel betaActivationCodeJLabel = new javax.swing.JLabel();
        betaActivationCodeJTextField = new javax.swing.JTextField();

        setOpaque(false);
        explanationJLabel.setFont(Fonts.DialogFont);
        explanationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PaymentInfo.Explanation"));

        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
        errorMessageJLabel.setText("!Error Message!");

        cardTypeJLabel.setFont(Fonts.DialogFont);
        cardTypeJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PaymentInfo.CardType"));

        cardNumberJLabel.setFont(Fonts.DialogFont);
        cardNumberJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PaymentInfo.CardNumber"));

        cardNameJLabel.setFont(Fonts.DialogFont);
        cardNameJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PaymentInfo.CardName"));

        expiryDateJLabel.setFont(Fonts.DialogFont);
        expiryDateJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PaymentInfo.CardDate"));

        cardTypeJComboBox.setFont(Fonts.DialogTextEntryFont);
        cardTypeJComboBox.setModel(cardTypeModel);

        cardNumberJTextField.setFont(Fonts.DialogTextEntryFont);

        cardNameJTextField.setFont(Fonts.DialogTextEntryFont);

        cardMonthJComboBox.setFont(Fonts.DialogTextEntryFont);
        cardMonthJComboBox.setModel(cardMonthModel);

        cardYearJComboBox.setFont(Fonts.DialogTextEntryFont);
        cardYearJComboBox.setModel(cardYearModel);

        betaExplanationJLabel.setFont(Fonts.DialogFont);
        betaExplanationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PaymentInfo.BetaExplanation"));
        betaExplanationJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        betaActivationCodeJLabel.setFont(Fonts.DialogFont);
        betaActivationCodeJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PaymentInfo.BetaActivationCode"));

        betaActivationCodeJTextField.setFont(Fonts.DialogTextEntryFont);

        betaActivationLearnMoreJLabel.setFont(Fonts.DialogFont);
        betaActivationLearnMoreJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PaymentInfo.BetaActivationLearnMore"));
        betaActivationLearnMoreJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                betaActivationLearnMoreJLabelMousePressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(betaExplanationJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(errorMessageJLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 315, Short.MAX_VALUE))
                            .addComponent(explanationJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cardTypeJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cardNumberJLabel)
                                    .addComponent(cardNameJLabel)
                                    .addComponent(expiryDateJLabel))
                                .addGap(23, 23, 23)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cardNameJTextField)
                                    .addComponent(cardNumberJTextField)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cardMonthJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cardYearJComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(cardTypeJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(betaActivationCodeJLabel)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(betaActivationLearnMoreJLabel)
                            .addComponent(betaActivationCodeJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(131, 131, 131)
                .addComponent(betaExplanationJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(betaActivationCodeJLabel)
                    .addComponent(betaActivationCodeJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(betaActivationLearnMoreJLabel)
                .addGap(27, 27, 27)
                .addComponent(explanationJLabel)
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardTypeJLabel)
                    .addComponent(cardTypeJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardNumberJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardNumberJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardNameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardNameJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardMonthJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardYearJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(expiryDateJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(errorMessageJLabel)
                .addGap(36, 36, 36))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Initialize document handler.
     */
    // TODO Remove this after beta.
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
        betaActivationCodeJTextField.getDocument().addDocumentListener(documentListener);
    }

    /**
     * Sign up.
     */
    private void signup() {
        saveData();
        SwingUtil.setCursor(this, java.awt.Cursor.WAIT_CURSOR);
        errorMessageJLabel.setText(getString("SigningUp"));
        errorMessageJLabel.paintImmediately(0, 0, errorMessageJLabel.getWidth(), errorMessageJLabel.getHeight());
        createProfile();
        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
        }
        SwingUtil.setCursor(this, java.awt.Cursor.DEFAULT_CURSOR);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField betaActivationCodeJTextField;
    private final javax.swing.JLabel betaActivationLearnMoreJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JComboBox cardMonthJComboBox = new javax.swing.JComboBox();
    private final javax.swing.JLabel cardNameJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField cardNameJTextField = new javax.swing.JTextField();
    private final javax.swing.JLabel cardNumberJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField cardNumberJTextField = new javax.swing.JTextField();
    private final javax.swing.JComboBox cardTypeJComboBox = new javax.swing.JComboBox();
    private final javax.swing.JLabel cardTypeJLabel = new javax.swing.JLabel();
    private final javax.swing.JComboBox cardYearJComboBox = new javax.swing.JComboBox();
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel expiryDateJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel explanationJLabel = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables

    /**
     * Credit card types
     */
    private enum CardType {
        VISA("Visa"),
        MASTERCARD("MasterCard"),
        AMERICAN_EXPRESS("AmericanExpress"),
        DISCOVER("Discover"),
        DINERS_CLUB("DinersClub"),
        JCB("JCB");

        /** The credit card name localization key */
        private final String nameKey;

        private CardType(final String nameKey) {
            this.nameKey = nameKey;
        }
    }
}
