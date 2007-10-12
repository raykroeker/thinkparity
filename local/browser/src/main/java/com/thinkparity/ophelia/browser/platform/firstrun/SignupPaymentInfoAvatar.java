/*
 * Created On: April 7, 2007, 11:26 AM
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;

import javax.swing.DefaultComboBoxModel;
import javax.swing.text.AbstractDocument;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.text.JTextFieldLengthFilter;

import com.thinkparity.codebase.model.profile.payment.PaymentInfo;
import com.thinkparity.codebase.model.profile.payment.PaymentInfoConstraints;

import com.thinkparity.ophelia.model.profile.ReservationExpiredException;
import com.thinkparity.ophelia.model.session.OfflineException;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.TextFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.firstrun.SignupData.DataKey;
import com.thinkparity.ophelia.browser.util.swing.CardNameCellRenderer;

/**
 * <b>Title:</b>thinkParity Ophelia UI Sign-Up Payment Info Avatar<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class SignupPaymentInfoAvatar extends DefaultSignupPage {

    /** Payment info constraints. */
    private static final PaymentInfoConstraints constraints;

    static {
        constraints = PaymentInfoConstraints.getInstance();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JComboBox cardMonthJComboBox = new javax.swing.JComboBox();
    private final javax.swing.JComboBox cardNameJComboBox = new javax.swing.JComboBox();
    private final javax.swing.JLabel cardNameJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel cardNumberJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField cardNumberJTextField = TextFactory.create();
    private final javax.swing.JComboBox cardYearJComboBox = new javax.swing.JComboBox();
    private final javax.swing.JLabel cardholderNameJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField cardholderNameJTextField = TextFactory.create();
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel expiryDateJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel explanationJLabel = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel cardExpiryMonthModel;

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel cardExpiryYearModel;

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel cardNameModel;

    /**
     * Create SignupPaymentInfoAvatar.
     * 
     */
    public SignupPaymentInfoAvatar() {
        super("SignupAvatar.PaymentInfo", BrowserConstants.DIALOGUE_BACKGROUND);
        this.cardExpiryMonthModel = new DefaultComboBoxModel();
        this.cardNameModel = new DefaultComboBoxModel();
        this.cardExpiryYearModel = new DefaultComboBoxModel();
        initExpiryMonthModel();
        initExpiryYearModel();
        initCardNameModel();
        initComponents();
        addValidationListeners();
        addFocusListeners();
        cardNameJComboBox.setRenderer(new CardNameCellRenderer("CardName", getLocalization()));
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
        if (platform.isInternal()) {
            return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_PAYMENT_PLAN);
        } else {
            return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_ACCOUNT);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#isNextOk()
     */
    public Boolean isNextOk() {
        if (!isInputValid()) {
            return Boolean.FALSE;
        }
        createProfile();
        return !containsInputErrors();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    public void reload() {
        validateInput();
        selectExpiryMonth();
        selectExpiryYear();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#saveData()
     */
    public void saveData() {
        ((Data) input).set(DataKey.PAYMENT_INFO, extractPaymentInfo());
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.DefaultSignupPage#setDefaultFocus()
     * 
     */
    @Override
    public void setDefaultFocus() {
        cardNameJComboBox.requestFocusInWindow();
    }

    /**
     * Validate input.
     */
    @Override
    public void validateInput() {
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
     * Add focus listeners for the input controls.
     */
    private void addFocusListeners() {
        final FocusListener focusListener = new FocusListener() {
            public void focusGained(FocusEvent e) {
                validateInput();
            }
            public void focusLost(FocusEvent e) {
                validateInput();
            }
        };
        cardNumberJTextField.addFocusListener(focusListener);
        cardMonthJComboBox.addFocusListener(focusListener);
        cardYearJComboBox.addFocusListener(focusListener);
    }

    /**
     * Add validation listeners for the text input controls.
     * 
     */
    private void addValidationListeners() {
        addValidationListener(cardNumberJTextField);
        addValidationListener(cardholderNameJTextField);
        final ItemListener itemListener = new ItemListener() {
            public void itemStateChanged(final ItemEvent e) {
                validateInput();
            }
        };
        cardNameJComboBox.addItemListener(itemListener);
        cardMonthJComboBox.addItemListener(itemListener);
        cardYearJComboBox.addItemListener(itemListener);
    }

    /**
     * Create the profile.
     */
    private void createProfile() {
        saveData();
        signupDelegate.enableNextButton(Boolean.FALSE);
        SwingUtil.setCursor(this, java.awt.Cursor.WAIT_CURSOR);
        errorMessageJLabel.setText(getString("SigningUp"));
        errorMessageJLabel.paintImmediately(0, 0, errorMessageJLabel.getWidth(), errorMessageJLabel.getHeight());
        try {
            getSignupHelper().createProfile();
        } catch (final OfflineException ox) {
            logger.logError(ox, "An offline error has occured.");
            addInputError(getSharedString("ErrorOffline"));
        } catch (final ReservationExpiredException rex) {
            logger.logWarning(rex, "The username/e-mail reservation has expired.");
            addInputError(getString("ErrorReservationExpired"));
        } catch (final Throwable t) {
            logger.logFatal(t, "An unexpected error has occured.");
            addInputError(getSharedString("ErrorUnexpected"));
        }
        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
        }
        SwingUtil.setCursor(this, null);
        signupDelegate.enableNextButton(Boolean.TRUE);
    }

    /**
     * Extract the payment info.
     * 
     * @return A <code>PaymentInfo</code>.
     */
    private PaymentInfo extractPaymentInfo() {
        return utils.extractPaymentInfo(cardMonthJComboBox, cardYearJComboBox,
                cardholderNameJTextField, cardNumberJTextField,
                cardNameJComboBox);
    }

    /**
     * Initialize the list of credit card types.
     * 
     */
    private void initCardNameModel() {
        for (final PaymentInfo.CardName cardName : PaymentInfo.CardName.values()) {
            cardNameModel.addElement(cardName);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setOpaque(false);
        explanationJLabel.setFont(Fonts.DialogFont);
        explanationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PaymentInfo.Explanation"));

        cardNameJLabel.setFont(Fonts.DialogFont);
        cardNameJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PaymentInfo.CardType"));

        cardNumberJLabel.setFont(Fonts.DialogFont);
        cardNumberJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PaymentInfo.CardNumber"));

        cardholderNameJLabel.setFont(Fonts.DialogFont);
        cardholderNameJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PaymentInfo.CardName"));

        expiryDateJLabel.setFont(Fonts.DialogFont);
        expiryDateJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PaymentInfo.CardDate"));

        cardNameJComboBox.setFont(Fonts.DialogTextEntryFont);
        cardNameJComboBox.setModel(cardNameModel);

        cardNumberJTextField.setFont(Fonts.DialogTextEntryFont);
        ((AbstractDocument) cardNumberJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(constraints.getCardNumber()));

        cardholderNameJTextField.setFont(Fonts.DialogTextEntryFont);
        ((AbstractDocument) cardholderNameJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(constraints.getCardHolderName()));

        cardMonthJComboBox.setFont(Fonts.DialogTextEntryFont);
        cardMonthJComboBox.setModel(cardExpiryMonthModel);

        cardYearJComboBox.setFont(Fonts.DialogTextEntryFont);
        cardYearJComboBox.setModel(cardExpiryYearModel);

        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
        errorMessageJLabel.setText("!Error Message!");

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
                            .addComponent(cardNameJLabel)
                            .addComponent(cardNumberJLabel)
                            .addComponent(cardholderNameJLabel)
                            .addComponent(expiryDateJLabel))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cardMonthJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cardYearJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cardholderNameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cardNameJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cardNumberJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(explanationJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(errorMessageJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cardholderNameJLabel, expiryDateJLabel});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cardNameJComboBox, cardNumberJTextField, cardholderNameJTextField});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(explanationJLabel)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardNameJLabel)
                    .addComponent(cardNameJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardNumberJLabel)
                    .addComponent(cardNumberJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardholderNameJLabel)
                    .addComponent(cardholderNameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(expiryDateJLabel)
                    .addComponent(cardMonthJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardYearJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(errorMessageJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(167, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    /**
     * Initialize the list of credit card months.
     * 
     */
    private void initExpiryMonthModel() {
        final short min = constraints.getCardExpiryMonth().getMinValue().shortValue();
        final short max = constraints.getCardExpiryMonth().getMaxValue().shortValue();
        for (short month = min; month <= max; month++) {
            cardExpiryMonthModel.addElement(Short.valueOf(month));
        }
    }

    /**
     * Initialize the list of credit card years.
     * 
     */
    private void initExpiryYearModel() {
        final short min = constraints.getCardExpiryYear().getMinValue().shortValue();
        final short max = constraints.getCardExpiryYear().getMaxValue().shortValue();
        for (short year = min; year <= max; year++) {
            cardExpiryYearModel.addElement(Short.valueOf(year));
        }
    }

    /**
     * Select the current month.
     * 
     */
    private void selectExpiryMonth() {
        final Calendar now = DateUtil.getInstance();
        final short nowMonth = (short) (now.get(Calendar.MONTH) + 1);
        cardExpiryMonthModel.setSelectedItem(Short.valueOf(nowMonth));
    }

    /**
     * Select the current year.
     * 
     */
    private void selectExpiryYear() {
        final Calendar now = Calendar.getInstance();
        final short nowYear = (short) now.get(Calendar.YEAR);
        cardExpiryYearModel.setSelectedItem(Short.valueOf(nowYear));
    }

    /**
     * Validate input.
     * 
     * @param ignoreFocus
     *            A <code>Boolean</code> to ignore focus or not.
     */
    private void validateInput(final Boolean ignoreFocus) {
        super.validateInput();
        final PaymentInfo paymentInfo = extractPaymentInfo();

        final int minimumCardNumberLength = constraints.getCardNumber().getMinLength();
        if (isEmpty(paymentInfo.getCardNumber())) {
            addInputError(Separator.Space.toString());
        } else if (paymentInfo.getCardNumber().length() < minimumCardNumberLength) {
            if (ignoreFocus || !cardNumberJTextField.isFocusOwner()) {
                addInputError(getString("ErrorCardNumberInvalid"));
            }
        }

        if (isEmpty(paymentInfo.getCardHolderName())) {
            addInputError(Separator.Space.toString());
        }

        final Calendar now = Calendar.getInstance();
        final short nowYear = (short) now.get(Calendar.YEAR);
        final short nowMonth = (short) (now.get(Calendar.MONTH) + 1);
        if (nowYear == paymentInfo.getCardExpiryYear() &&
                nowMonth > paymentInfo.getCardExpiryMonth()) {
            if (ignoreFocus
                   || (!cardMonthJComboBox.isFocusOwner() && !cardYearJComboBox.isFocusOwner())) {
                addInputError(getString("ErrorCardExpired"));
            }
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
