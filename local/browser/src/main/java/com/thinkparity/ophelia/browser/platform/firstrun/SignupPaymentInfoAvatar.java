/*
 * Created On: April 7, 2007, 11:26 AM
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import java.util.Calendar;

import javax.swing.DefaultComboBoxModel;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.swing.SwingUtil;

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
        return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_ACCOUNT);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#isNextOk()
     */
    public Boolean isNextOk() {
        if (containsInputErrors()) {
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
     * 
     */
    @Override
    public void validateInput() {
        super.validateInput();

        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
        }

        if (isSignupDelegateInitialized()) {
            signupDelegate.enableNextButton(!containsInputErrors());
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#isInputValid()
     * 
     */
    @Override
    protected Boolean isInputValid() {
        validateInput();
        return !containsInputErrors();
    }

    /**
     * Add validation listeners for the text input controls.
     * 
     */
    private void addValidationListeners() {
        addValidationListener(cardNumberJTextField);
        addValidationListener(cardholderNameJTextField);
    }

    // TODO Remove this after beta.
    /**
     * Create the profile.
     * 
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
                cardNumberJTextField, cardNameJComboBox);
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

        cardholderNameJTextField.setFont(Fonts.DialogTextEntryFont);

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(errorMessageJLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                    .addComponent(explanationJLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(expiryDateJLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cardMonthJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cardYearJComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cardholderNameJLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cardholderNameJTextField))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cardNumberJLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cardNumberJTextField))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cardNameJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cardNameJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cardNameJLabel, cardNumberJLabel, cardholderNameJLabel, expiryDateJLabel});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(explanationJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(errorMessageJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
}
