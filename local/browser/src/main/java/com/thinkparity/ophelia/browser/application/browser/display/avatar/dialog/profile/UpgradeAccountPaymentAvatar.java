/*
 * UpgradeAccountPaymentAvatar.java
 *
 * Created on July 29, 2007, 3:19 PM
 */

package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile;

import java.text.DecimalFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.text.AbstractDocument;

import com.thinkparity.codebase.swing.text.JTextFieldLengthFilter;

import com.thinkparity.codebase.model.profile.ProfileConstraints;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.component.TextFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.action.platform.LearnMore;

/**
 *
 * @author robert@thinkparity.com
 */
public class UpgradeAccountPaymentAvatar extends DefaultUpgradeAccountPage {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JComboBox cardMonthJComboBox = new javax.swing.JComboBox();
    private final javax.swing.JTextField cardNameJTextField = TextFactory.create();
    private final javax.swing.JTextField cardNumberJTextField = TextFactory.create();
    private final javax.swing.JTextField cardSecurityCodeJTextField = TextFactory.create();
    private final javax.swing.JComboBox cardTypeJComboBox = new javax.swing.JComboBox();
    private final javax.swing.JComboBox cardYearJComboBox = new javax.swing.JComboBox();
    // End of variables declaration//GEN-END:variables

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel cardMonthModel;

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel cardTypeModel;

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel cardYearModel;

    /** An instance of <code>ProfileConstraints</code>. */
    private final ProfileConstraints profileConstraints;

    /** Creates new form UpgradeAccountPaymentAvatar */
    public UpgradeAccountPaymentAvatar() {
        super("UpgradeAccountAvatar.Payment", BrowserConstants.DIALOGUE_BACKGROUND);
        this.profileConstraints = ProfileConstraints.getInstance();
        this.cardMonthModel = new DefaultComboBoxModel();
        this.cardTypeModel = new DefaultComboBoxModel();
        this.cardYearModel = new DefaultComboBoxModel();
        initCardMonthModel();
        initCardTypeModel();
        initCardYearModel();
        initComponents();
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
        if (isUpgradeAccountDelegateInitialized()) {
            upgradeAccountDelegate.enableNextButton(Boolean.TRUE);
        }
    }

    /**
     * Initialize the list of credit card months.
     */
    private void initCardMonthModel() {
        final Long minValue = profileConstraints.getCreditCardMonth().getMinValue();
        final Long maxValue = profileConstraints.getCreditCardMonth().getMaxValue();
        final DecimalFormat formatter = new DecimalFormat("00");
        for (long month = minValue; month <= maxValue; month++) {
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
        final Long minValue = profileConstraints.getCreditCardYear().getMinValue();
        final Long maxValue = profileConstraints.getCreditCardYear().getMaxValue();
        final DecimalFormat formatter = new DecimalFormat("0000");
        for (long year = minValue; year <= maxValue; year++) {
            this.cardYearModel.addElement(formatter.format(year));
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
        final javax.swing.JLabel cardNameJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel cardExpiryDateJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel cardSecurityCodeJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel privacyLearnMoreJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
        final javax.swing.JLabel proceedJLabel = new javax.swing.JLabel();

        setOpaque(false);
        creditInfoTitleJLabel.setFont(Fonts.DialogFont);
        creditInfoTitleJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Payment.CreditInfoTitle"));

        cardTypeJLabel.setFont(Fonts.DialogFont);
        cardTypeJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Payment.CardType"));

        cardTypeJComboBox.setFont(Fonts.DialogTextEntryFont);
        cardTypeJComboBox.setModel(cardTypeModel);

        cardNumberJLabel.setFont(Fonts.DialogFont);
        cardNumberJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Payment.CardNumber"));

        cardNameJLabel.setFont(Fonts.DialogFont);
        cardNameJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Payment.CardName"));

        cardExpiryDateJLabel.setFont(Fonts.DialogFont);
        cardExpiryDateJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Payment.CardDate"));

        cardSecurityCodeJLabel.setFont(Fonts.DialogFont);
        cardSecurityCodeJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Payment.SecurityCode"));

        cardNumberJTextField.setFont(Fonts.DialogTextEntryFont);
        ((AbstractDocument) cardNumberJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getCreditCardNumber()));

        cardNameJTextField.setFont(Fonts.DialogTextEntryFont);
        ((AbstractDocument) cardNameJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getCreditCardName()));

        cardMonthJComboBox.setFont(Fonts.DialogTextEntryFont);
        cardMonthJComboBox.setModel(cardMonthModel);

        cardYearJComboBox.setFont(Fonts.DialogTextEntryFont);
        cardYearJComboBox.setModel(cardYearModel);

        cardSecurityCodeJTextField.setFont(Fonts.DialogTextEntryFont);
        ((AbstractDocument) cardSecurityCodeJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getCreditCardSecurityCode()));

        privacyLearnMoreJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Payment.PrivacyLearnMore"));
        privacyLearnMoreJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                privacyLearnMoreJLabelMousePressed(evt);
            }
        });

        proceedJLabel.setFont(Fonts.DialogFont);
        proceedJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Payment.Proceed"));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(creditInfoTitleJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cardTypeJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cardNumberJLabel)
                            .addComponent(cardNameJLabel)
                            .addComponent(cardExpiryDateJLabel)
                            .addComponent(cardSecurityCodeJLabel))
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cardNameJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                            .addComponent(cardNumberJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                            .addComponent(cardTypeJComboBox, 0, 292, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(cardSecurityCodeJTextField, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cardMonthJComboBox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cardYearJComboBox, 0, 181, Short.MAX_VALUE)))
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
                    .addComponent(cardNameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardNameJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardExpiryDateJLabel)
                    .addComponent(cardMonthJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardYearJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardSecurityCodeJLabel)
                    .addComponent(cardSecurityCodeJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
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
     * Credit card types
     */
    private enum CardType {
        NONE("None"),
        AMERICAN_EXPRESS("AmericanExpress"),
        DINERS_CLUB("DinersClub"),
        DISCOVER("Discover"),
        JCB("JCB"),
        MASTERCARD("MasterCard"),
        VISA("Visa");

        /** The credit card name localization key */
        private final String nameKey;

        private CardType(final String nameKey) {
            this.nameKey = nameKey;
        }
    }
}
