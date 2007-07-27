/*
 * UpdateAccountAvatar.java
 *
 * Created on July 25, 2007, 4:36 PM
 */

package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.JTextComponent;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.text.JTextFieldLengthFilter;

import com.thinkparity.codebase.model.profile.ProfileConstraints;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.component.TextFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile.UpdateAccountProvider;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 *
 * <b>Title:</b>thinkParity OpheliaUI Update Account Avatar<br>
 * <b>Description:</b><br>
 * 
 * @author robert@thinkparity.com
 */
public class UpdateAccountAvatar extends Avatar {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JRadioButton accountTypeGuestJRadioButton = new javax.swing.JRadioButton();
    private final javax.swing.JRadioButton accountTypeStandardJRadioButton = new javax.swing.JRadioButton();
    private final javax.swing.JButton cancelJButton = ButtonFactory.create();
    private final javax.swing.JComboBox cardMonthJComboBox = new javax.swing.JComboBox();
    private final javax.swing.JPanel cardMonthJPanel = new javax.swing.JPanel();
    private final javax.swing.JTextField cardMonthJTextField = TextFactory.create();
    private final javax.swing.JTextField cardNameJTextField = TextFactory.create();
    private final javax.swing.JTextField cardNumberJTextField = TextFactory.create();
    private final javax.swing.JTextField cardSecurityCodeJTextField = TextFactory.create();
    private final javax.swing.JComboBox cardTypeJComboBox = new javax.swing.JComboBox();
    private final javax.swing.JPanel cardTypeJPanel = new javax.swing.JPanel();
    private final javax.swing.JTextField cardTypeJTextField = TextFactory.create();
    private final javax.swing.JComboBox cardYearJComboBox = new javax.swing.JComboBox();
    private final javax.swing.JPanel cardYearJPanel = new javax.swing.JPanel();
    private final javax.swing.JTextField cardYearJTextField = TextFactory.create();
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JButton okJButton = ButtonFactory.create();
    // End of variables declaration//GEN-END:variables

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel cardMonthModel;

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel cardTypeModel;

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel cardYearModel;

    /** An instance of <code>ProfileConstraints</code>. */
    private final ProfileConstraints profileConstraints;

    /** Create UpdateAccountAvatar */
    public UpdateAccountAvatar() {
        super("UpdateAccountAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        this.profileConstraints = ProfileConstraints.getInstance();
        this.cardMonthModel = new DefaultComboBoxModel();
        this.cardTypeModel = new DefaultComboBoxModel();
        this.cardYearModel = new DefaultComboBoxModel();
        initCardMonthModel();
        initCardTypeModel();
        initCardYearModel();
        initComponents();
        addValidationListeners();
        addFocusListener();
        bindKeys();
    }

    public AvatarId getId() {
        return AvatarId.DIALOG_PROFILE_UPDATE_ACCOUNT;
    }

    public State getState() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     * 
     */
    public void reload() {
        reloadAccountTypeRadioButtons();
        reloadCreditCardControls();
        validateInput();
    }

    public void setState(final State state) {
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#validateInput()
     */
    @Override
    protected void validateInput() {
        super.validateInput();

        // check online
        final Boolean online = isOnline();
        if (!online) {
            addInputError(getString("ErrorOffline"));
        }

        // check for user changes
        final Boolean inputChanged = isInputChanged();
        if (!inputChanged) {
            addInputError(Separator.Space.toString());
        }

        // check input credit card data
        if (inputChanged && accountTypeStandardJRadioButton.isSelected()) {
            // check for blank required fields
            if (null == extractInputCardName() ||
                null == extractInputCardNumber() ||
                null == extractInputCardType()) {
                addInputError(getString("ErrorMissingCreditCardData"));
            }

            // check for minimum length security code
            final String securityCode = extractInputCardSecurityCode();
            final Integer minLength = profileConstraints.getCreditCardSecurityCode().getMinLength();
            final Integer maxLength = profileConstraints.getCreditCardSecurityCode().getMaxLength();
            if (null == securityCode && !cardSecurityCodeJTextField.isFocusOwner()) {
                addInputError(getString("ErrorMissingCreditCardData"));
            } else if (null == securityCode || securityCode.length() < minLength) {
                addInputError(getString("ErrorSecurityCode", new Object[] {minLength, maxLength}));
            }
        }

        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
        }
        okJButton.setEnabled(!containsInputErrors());
    }

    private void accountTypeGuestJRadioButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accountTypeGuestJRadioButtonActionPerformed
        validateInput();
        enableCreditCardControls(Boolean.FALSE);
    }//GEN-LAST:event_accountTypeGuestJRadioButtonActionPerformed

    private void accountTypeStandardJRadioButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accountTypeStandardJRadioButtonActionPerformed
        validateInput();
        enableCreditCardControls(Boolean.TRUE);
    }//GEN-LAST:event_accountTypeStandardJRadioButtonActionPerformed

    /**
     * Add a focus listener.
     */
    private void addFocusListener() {
        cardSecurityCodeJTextField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                validateInput();
            }
            public void focusLost(FocusEvent e) {
                validateInput();
            }
        });
    }

    /**
     * Add validation listeners.
     */
    private void addValidationListeners() {
        addValidationListener(cardNumberJTextField);
        addValidationListener(cardNameJTextField);
        addValidationListener(cardSecurityCodeJTextField);
        final ItemListener itemListener = new ItemListener() {
            public void itemStateChanged(final ItemEvent e) {
                validateInput();
            }
        };
        cardTypeJComboBox.addItemListener(itemListener);
        cardMonthJComboBox.addItemListener(itemListener);
        cardYearJComboBox.addItemListener(itemListener);
    }

    /**
     * Bind the escape and enter keys.
     */
    private void bindKeys() {
        bindEscapeKey("Cancel", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });
        bindEnterKey("Enter", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                if (cancelJButton.isFocusOwner()) {
                    cancelJButtonActionPerformed(e);
                } else {
                    okJButtonActionPerformed(e);
                }
            }
        });
    }

    private void cancelJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    /**
     * Change the credit card.
     * 
     * @param cardType
     *            The credit card type <code>String</code>.
     * @param cardNumber
     *            The credit card number <code>String</code>.
     * @param cardName
     *            The credit card name <code>String</code>.
     * @param cardMonth
     *            The credit card month <code>Long</code>.
     * @param cardYear
     *            The credit card year <code>Long</code>.
     * @param cardSecurityCode
     *            The credit card security code <code>String</code>.
     */
    private void changeCreditCard(final String cardType,
            final String cardNumber, final String cardName,
            final Long cardMonth, final Long cardYear,
            final String cardSecurityCode) {
        // TODO
    }

    /**
     * Downgrade the account type.
     */
    private void downgradeAccountType() {
        // TODO
    }

    /**
     * Enable or disable credit card controls in the dialog.
     * 
     * @param enable
     *            A <code>Boolean</code>, enable or disable credit card controls.
     */
    private void enableCreditCardControls(final Boolean enable) {
        final String card = enable ? "standard" : "guest";
        ((java.awt.CardLayout)cardTypeJPanel.getLayout()).show(cardTypeJPanel, card);
        ((java.awt.CardLayout)cardMonthJPanel.getLayout()).show(cardMonthJPanel, card);
        ((java.awt.CardLayout)cardYearJPanel.getLayout()).show(cardYearJPanel, card);
        setEditable(cardNameJTextField, enable);
        setEditable(cardNumberJTextField, enable);
        setEditable(cardSecurityCodeJTextField, enable);
        setEditable(cardTypeJTextField, enable);
        setEditable(cardMonthJTextField, enable);
        setEditable(cardYearJTextField, enable);
    }

    /**
     * Extract the input credit card month.
     * 
     * @return The credit card month <code>Long</code>.
     */
    private Long extractInputCardMonth() {
        if (cardMonthJComboBox.getSelectedIndex() >= 0) {
            return Long.valueOf((String)cardMonthJComboBox.getSelectedItem());
        } else {
            return null;
        }
    }

    /**
     * Extract the input credit card name.
     * 
     * @return The credit card name <code>String</code>.
     */
    private String extractInputCardName() {
        return SwingUtil.extract(cardNameJTextField, Boolean.TRUE);
    }

    /**
     * Extract the input credit card number.
     * 
     * @return The credit card number <code>String</code>.
     */
    private String extractInputCardNumber() {
        return SwingUtil.extract(cardNumberJTextField, Boolean.TRUE);
    }

    /**
     * Extract the input credit card security code.
     * 
     * @return The credit card security code <code>String</code>.
     */
    private String extractInputCardSecurityCode() {
        return SwingUtil.extract(cardSecurityCodeJTextField, Boolean.TRUE);
    }

    /**
     * Extract the input credit card type.
     * 
     * @return The credit card type <code>String</code>.
     */
    private String extractInputCardType() {
        if (cardTypeJComboBox.getSelectedIndex() >= 0) {
            final String cardType = (String) cardTypeJComboBox.getSelectedItem();
            return (0 == cardType.length() ? null : cardType);
        } else {
            return null;
        }
    }

    /**
     * Extract the input credit card year.
     * 
     * @return The credit card year <code>Long</code>.
     */
    private Long extractInputCardYear() {
        if (cardYearJComboBox.getSelectedIndex() >= 0) {
            return Long.valueOf((String)cardYearJComboBox.getSelectedItem());
        } else {
            return null;
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
        final javax.swing.ButtonGroup accountTypeButtonGroup = new javax.swing.ButtonGroup();
        final javax.swing.JLabel accountTypeTitleJLabel = new javax.swing.JLabel();
        final javax.swing.JSeparator jSeparator1 = new javax.swing.JSeparator();
        final javax.swing.JLabel creditInfoTitleJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel cardTypeJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel cardNumberJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel cardNameJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel cardExpiryDateJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel cardSecurityCodeJLabel = new javax.swing.JLabel();

        okJButton.setFont(Fonts.DialogButtonFont);
        okJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateAccountAvatar.OK"));
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setFont(Fonts.DialogButtonFont);
        cancelJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateAccountAvatar.Cancel"));
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });

        accountTypeTitleJLabel.setFont(Fonts.DialogFont);
        accountTypeTitleJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateAccountAvatar.AccountTypeTitle"));

        accountTypeButtonGroup.add(accountTypeStandardJRadioButton);
        accountTypeStandardJRadioButton.setFont(Fonts.DialogFont);
        accountTypeStandardJRadioButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateAccountAvatar.AccountTypeStandard"));
        accountTypeStandardJRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        accountTypeStandardJRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        accountTypeStandardJRadioButton.setOpaque(false);
        accountTypeStandardJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accountTypeStandardJRadioButtonActionPerformed(evt);
            }
        });

        accountTypeButtonGroup.add(accountTypeGuestJRadioButton);
        accountTypeGuestJRadioButton.setFont(Fonts.DialogFont);
        accountTypeGuestJRadioButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateAccountAvatar.AccountTypeGuest"));
        accountTypeGuestJRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        accountTypeGuestJRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        accountTypeGuestJRadioButton.setOpaque(false);
        accountTypeGuestJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accountTypeGuestJRadioButtonActionPerformed(evt);
            }
        });

        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
        errorMessageJLabel.setText("!Error Message!");

        creditInfoTitleJLabel.setFont(Fonts.DialogFont);
        creditInfoTitleJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateAccountAvatar.CreditInfoTitle"));

        cardTypeJLabel.setFont(Fonts.DialogFont);
        cardTypeJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateAccountAvatar.CardType"));

        cardTypeJPanel.setLayout(new java.awt.CardLayout());

        cardTypeJPanel.setOpaque(false);
        cardTypeJTextField.setFont(Fonts.DialogTextEntryFont);
        cardTypeJPanel.add(cardTypeJTextField, "guest");

        cardTypeJComboBox.setFont(Fonts.DialogTextEntryFont);
        cardTypeJComboBox.setModel(cardTypeModel);
        cardTypeJPanel.add(cardTypeJComboBox, "standard");

        cardNumberJLabel.setFont(Fonts.DialogFont);
        cardNumberJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateAccountAvatar.CardNumber"));

        cardNumberJTextField.setFont(Fonts.DialogTextEntryFont);
        ((AbstractDocument) cardNumberJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getCreditCardNumber()));

        cardNameJLabel.setFont(Fonts.DialogFont);
        cardNameJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateAccountAvatar.CardName"));

        cardMonthJPanel.setLayout(new java.awt.CardLayout());

        cardMonthJPanel.setOpaque(false);
        cardMonthJTextField.setFont(Fonts.DialogTextEntryFont);
        cardMonthJPanel.add(cardMonthJTextField, "guest");

        cardMonthJComboBox.setFont(Fonts.DialogTextEntryFont);
        cardMonthJComboBox.setModel(cardMonthModel);
        cardMonthJPanel.add(cardMonthJComboBox, "standard");

        cardNameJTextField.setFont(Fonts.DialogTextEntryFont);
        ((AbstractDocument) cardNameJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getCreditCardName()));

        cardExpiryDateJLabel.setFont(Fonts.DialogFont);
        cardExpiryDateJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateAccountAvatar.CardDate"));

        cardYearJPanel.setLayout(new java.awt.CardLayout());

        cardYearJPanel.setOpaque(false);
        cardYearJTextField.setFont(Fonts.DialogTextEntryFont);
        cardYearJPanel.add(cardYearJTextField, "guest");

        cardYearJComboBox.setFont(Fonts.DialogTextEntryFont);
        cardYearJComboBox.setModel(cardYearModel);
        cardYearJPanel.add(cardYearJComboBox, "standard");

        cardSecurityCodeJLabel.setFont(Fonts.DialogFont);
        cardSecurityCodeJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateAccountAvatar.SecurityCode"));

        cardSecurityCodeJTextField.setFont(Fonts.DialogTextEntryFont);
        ((AbstractDocument) cardSecurityCodeJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getCreditCardSecurityCode()));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(okJButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelJButton))
                    .addComponent(accountTypeTitleJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(accountTypeStandardJRadioButton)
                            .addComponent(accountTypeGuestJRadioButton))
                        .addGap(112, 112, 112))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addComponent(creditInfoTitleJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cardNameJLabel)
                            .addComponent(cardTypeJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cardNumberJLabel)
                            .addComponent(cardExpiryDateJLabel)
                            .addComponent(cardSecurityCodeJLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cardNumberJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cardTypeJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cardNameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(cardSecurityCodeJTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                                    .addComponent(cardMonthJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cardYearJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(errorMessageJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelJButton, okJButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(accountTypeTitleJLabel)
                .addGap(14, 14, 14)
                .addComponent(accountTypeStandardJRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accountTypeGuestJRadioButton)
                .addGap(15, 15, 15)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(creditInfoTitleJLabel)
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cardTypeJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cardNumberJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cardNumberJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cardNameJLabel)
                            .addComponent(cardNameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cardTypeJLabel)
                        .addGap(58, 58, 58)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cardMonthJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardYearJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardExpiryDateJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardSecurityCodeJLabel)
                    .addComponent(cardSecurityCodeJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(errorMessageJLabel)
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelJButton)
                    .addComponent(okJButton))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cardNumberJTextField, cardTypeJLabel, cardTypeJPanel});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cardExpiryDateJLabel, cardSecurityCodeJTextField});

    }// </editor-fold>//GEN-END:initComponents

    /**
     * Determine if the input has changed from the original state.
     * 
     * @return true if the input changed; false otherwise.
     */
    private Boolean isInputChanged() {
        return accountTypeGuestJRadioButton.isSelected() ||
           null != extractInputCardName() ||
           null != extractInputCardNumber() ||
           null != extractInputCardSecurityCode() ||
           null != extractInputCardType();
    }

    /**
     * Determine if the model is online.
     * 
     * @return True if the model is online.
     */
    private boolean isOnline() {
        return ((UpdateAccountProvider) contentProvider).isOnline().booleanValue();
    }

    private void okJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okJButtonActionPerformed
        if (isInputValid()) {
            disposeWindow();
            if (accountTypeGuestJRadioButton.isSelected()) {
                downgradeAccountType();
            } else {
                changeCreditCard(extractInputCardType(),
                        extractInputCardNumber(), extractInputCardName(),
                        extractInputCardMonth(), extractInputCardYear(),
                        extractInputCardSecurityCode());
            }
        }
    }//GEN-LAST:event_okJButtonActionPerformed

    /**
     * Reload the account type radio buttons.
     */
    private void reloadAccountTypeRadioButtons() {
        // this dialog is only displayed for standard accounts
        accountTypeStandardJRadioButton.setSelected(true);
    }

    /**
     * Reload the credit card controls.
     */
    private void reloadCreditCardControls() {
        cardTypeJComboBox.setSelectedIndex(0);
        cardNumberJTextField.setText(null);
        cardNameJTextField.setText(null);
        cardMonthJComboBox.setSelectedIndex(0);
        cardYearJComboBox.setSelectedIndex(0);
        cardSecurityCodeJTextField.setText(null);
        enableCreditCardControls(Boolean.TRUE);
    }

    /**
     * Enable or disable text entry for a text component.
     * 
     * @param jTextComponent
     *            A swing <code>JTextComponent</code>.
     * @param enable
     *            The enable <code>Boolean</code>.
     */
    protected void setEditable(final JTextComponent jTextComponent, final Boolean enable) {
        jTextComponent.setEditable(enable);
        jTextComponent.setFocusable(enable);
        jTextComponent.setOpaque(enable);
        if (enable) {
            jTextComponent.setForeground(Colours.DIALOG_TEXT_FG);
        } else {
            jTextComponent.setForeground(Colours.TRANSPARENT);
        }
    }

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
