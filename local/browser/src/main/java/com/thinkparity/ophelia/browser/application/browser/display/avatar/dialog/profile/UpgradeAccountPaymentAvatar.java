/*
 * UpgradeAccountPaymentAvatar.java
 *
 * Created on July 29, 2007, 3:19 PM
 */

package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile;

import javax.swing.DefaultComboBoxModel;
import javax.swing.text.AbstractDocument;

import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.text.JTextFieldLengthFilter;

import com.thinkparity.codebase.model.profile.payment.PaymentInfo;
import com.thinkparity.codebase.model.profile.payment.PaymentInfoConstraints;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.component.TextFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.action.platform.LearnMore;
import com.thinkparity.ophelia.browser.util.swing.CardNameCellRenderer;

/**
 * <b>Title:</b>thinkParity Ophelia UI Browser Profile Signup<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class UpgradeAccountPaymentAvatar extends DefaultUpgradeAccountPage {

    /** An instance of payment info constraints. */
    private static final PaymentInfoConstraints constraints;

    static {
        constraints = PaymentInfoConstraints.getInstance();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel cardholderNameJLabel;
    private javax.swing.JTextField cardholderNameJTextField;
    private final javax.swing.JComboBox cardMonthJComboBox = new javax.swing.JComboBox();
    private final javax.swing.JTextField cardNumberJTextField = TextFactory.create();
    private final javax.swing.JComboBox cardTypeJComboBox = new javax.swing.JComboBox();
    private final javax.swing.JComboBox cardYearJComboBox = new javax.swing.JComboBox();
    // End of variables declaration//GEN-END:variables

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel cardExpiryMonthModel;

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel cardExpiryYearModel;

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel cardNameModel;

    /**
     * Create UpgradeAccountPaymentAvatar.
     *
     */
    public UpgradeAccountPaymentAvatar() {
        super("UpgradeAccountAvatar.Payment", BrowserConstants.DIALOGUE_BACKGROUND);
        this.cardExpiryMonthModel = new DefaultComboBoxModel();
        this.cardNameModel = new DefaultComboBoxModel();
        this.cardExpiryYearModel = new DefaultComboBoxModel();
        initCardNameModel();
        initExpiryMonthModel();
        initExpiryYearModel();
        initComponents();
        addValidationListeners();
        cardTypeJComboBox.setRenderer(new CardNameCellRenderer("CardName", getLocalization()));
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     * 
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_PROFILE_UPGRADE_ACCOUNT_PAYMENT;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.UpgradeAccountPage#getNextButtonTextKey()
     */
    public String getNextButtonTextKey() {
        return "Payment.NextButton";
    }

    /**)
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.UpgradeAccountPage#getNextPageName()
     */
    public String getNextPageName() {
        return getPageName(AvatarId.DIALOG_PROFILE_UPGRADE_ACCOUNT_SUMMARY);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.UpgradeAccountPage#getPreviousPageName()
     */
    public String getPreviousPageName() {
        return getPageName(AvatarId.DIALOG_PROFILE_UPGRADE_ACCOUNT_AGREEMENT);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.DefaultUpgradeAccountPage#isNextOk()
     *
     */
    @Override
    public Boolean isNextOk() {
        if (super.isNextOk()) {
            signUp();

            if (containsInputErrors()) {
                return Boolean.FALSE;
            } else {
                return Boolean.TRUE;
            }
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        validateInput();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.DefaultUpgradeAccountPage#validateInput()
     */
    @Override
    public void validateInput() {
        super.validateInput();

        final String cardNumber = extractCardNumber();
        if (null == cardNumber) {
            addInputError(getString("ErrorNoNumber"));
        }
        
        if (isUpgradeAccountDelegateInitialized()) {
            upgradeAccountDelegate.enableNextButton(Boolean.TRUE);
        }
    }

    /**
     * Add validation listeners.
     * 
     */
    private void addValidationListeners() {
        addValidationListener(cardholderNameJTextField);
        addValidationListener(cardNumberJTextField);
    }

    /**
     * Extract the card number.
     * 
     * @return A <code>String</code>.
     */
    private String extractCardNumber() {
        return SwingUtil.extract(cardNumberJTextField);
    }

    /**
     * Extract the payment info.
     * 
     * @return A <code>PaymentInfo</code>.
     */
    private PaymentInfo extractPaymentInfo() {
        return utils.extractPaymentInfo(cardMonthJComboBox,
                cardYearJComboBox, cardNumberJTextField, cardTypeJComboBox);
    }

    /**
     * Initialize the list of expiry months.
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
     * Initialize the list of credit card types.
     */
    private void initCardNameModel() {
        for (final PaymentInfo.CardName cardName : PaymentInfo.CardName.values()) {
            cardNameModel.addElement(cardName);
        }
    }

    /**
     * Initialize the list of credit card years.
     */
    private void initExpiryYearModel() {
        final short min = constraints.getCardExpiryYear().getMinValue().shortValue();
        final short max = constraints.getCardExpiryYear().getMaxValue().shortValue();
        for (short year = min; year <= max; year++) {
            cardExpiryYearModel.addElement(Short.valueOf(year));
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.JLabel creditInfoTitleJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel cardTypeJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel cardNumberJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel cardExpiryDateJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel privacyLearnMoreJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
        final javax.swing.JLabel proceedJLabel = new javax.swing.JLabel();
        cardholderNameJLabel = LabelFactory.create(Fonts.DialogFont);
        cardholderNameJTextField = new javax.swing.JTextField();

        setOpaque(false);
        creditInfoTitleJLabel.setFont(Fonts.DialogFont);
        creditInfoTitleJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Payment.CreditInfoTitle"));

        cardTypeJLabel.setFont(Fonts.DialogFont);
        cardTypeJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Payment.CardType"));

        cardTypeJComboBox.setFont(Fonts.DialogTextEntryFont);
        cardTypeJComboBox.setModel(cardNameModel);

        cardNumberJLabel.setFont(Fonts.DialogFont);
        cardNumberJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Payment.CardNumber"));

        cardExpiryDateJLabel.setFont(Fonts.DialogFont);
        cardExpiryDateJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Payment.CardDate"));

        cardNumberJTextField.setFont(Fonts.DialogTextEntryFont);
        ((AbstractDocument) cardNumberJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(constraints.getCardNumber()));

        cardMonthJComboBox.setFont(Fonts.DialogTextEntryFont);
        cardMonthJComboBox.setModel(cardExpiryMonthModel);

        cardYearJComboBox.setFont(Fonts.DialogTextEntryFont);
        cardYearJComboBox.setModel(cardExpiryYearModel);

        privacyLearnMoreJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Payment.PrivacyLearnMore"));
        privacyLearnMoreJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                privacyLearnMoreJLabelMousePressed(e);
            }
        });

        proceedJLabel.setFont(Fonts.DialogFont);
        proceedJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Payment.Proceed"));

        cardholderNameJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Payment.Cardholder"));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(creditInfoTitleJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cardTypeJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cardNumberJLabel)
                            .addComponent(cardExpiryDateJLabel)
                            .addComponent(cardholderNameJLabel))
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cardholderNameJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                            .addComponent(cardNumberJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                            .addComponent(cardTypeJComboBox, 0, 234, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cardMonthJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cardYearJComboBox, 0, 123, Short.MAX_VALUE)))
                        .addGap(36, 36, 36))
                    .addComponent(privacyLearnMoreJLabel)
                    .addComponent(proceedJLabel))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(128, 128, 128)
                .addComponent(creditInfoTitleJLabel)
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardTypeJLabel)
                    .addComponent(cardTypeJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardNumberJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardNumberJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardholderNameJLabel)
                    .addComponent(cardholderNameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardExpiryDateJLabel)
                    .addComponent(cardMonthJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardYearJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addComponent(privacyLearnMoreJLabel)
                .addGap(15, 15, 15)
                .addComponent(proceedJLabel)
                .addContainerGap(23, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void privacyLearnMoreJLabelMousePressed(final java.awt.event.MouseEvent evt) {//GEN-FIRST:event_privacyLearnMoreJLabelMousePressed
        getController().runLearnMore(LearnMore.Topic.PRIVACY);
    }//GEN-LAST:event_privacyLearnMoreJLabelMousePressed

    /**
     * Sign up.
     * 
     */
    private void signUp() {
        getController().runSignup(extractPaymentInfo());
    }
}
