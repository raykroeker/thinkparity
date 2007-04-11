/*
 * Created on January 31, 2007, 3:14 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.email.EMailFormatException;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.component.TextFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile.UpdateProfileProvider;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.dialog.profile.LocaleRenderer;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 * <b>Title:</b>thinkParity OpheliaUI Update Profile Avatar<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class UpdateProfileAvatar extends Avatar {

    /** Unavailable email. */
    private String unavailableEmail = null;

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel countryModel;
    
    /** The emails. */
    private List<ProfileEMail> emails;
    
    /** The profile. */
    private Profile profile;

    /** Creates new form UpdateProfileAvatar */
    public UpdateProfileAvatar() {
        super("UpdateProfileDialog", BrowserConstants.DIALOGUE_BACKGROUND);
        this.countryModel = new DefaultComboBoxModel();
        initCountryModel();
        initComponents();
        initDocumentHandlers();
        bindEscapeKey("Cancel", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });
    }

    public AvatarId getId() {
        return AvatarId.DIALOG_PROFILE_UPDATE;
    }

    public State getState() {
        return null;
    }

    /**
     * Determine whether the user input is valid.
     * This method should return false whenever we want the
     * OK button to be disabled.
     * 
     * @return True if the input is valid; false otherwise.
     */
    public Boolean isInputValid() {
        if (isEmpty(extractInputName()) ||
            isEmpty(extractInputTitle()) ||
            isEmpty(extractInputOrganization())) {
            return Boolean.FALSE;
        } else {
            return (isInputEmailValid() && isInputEmailAvailableQuick());
        }
    }

    /**
     * Determine if the verification input is valid.
     * 
     * @return True if the verification input is valid.
     */
    private Boolean isVerifyInputValid() {
        return null != extractVerificationKey();
    }

    /**
     * Extract the verification key.
     * 
     * @return The verification key <code>String</code>.
     */
    private String extractVerificationKey() {
        return SwingUtil.extract(verificationKeyJTextField, Boolean.TRUE);
    }

    public void reload() {
        this.profile = readProfile();
        this.emails = readEMails();
        reload(nameJTextField, profile.getName());
        reload(titleJTextField, profile.getTitle());
        reload(organizationJTextField, profile.getOrganization());
        reload(phoneJTextField, profile.getPhone());
        reload(mobilePhoneJTextField, profile.getMobilePhone());
        reload(addressJTextField, profile.getAddress());
        reload(cityJTextField, profile.getCity());
        reload(provinceJTextField, profile.getProvince());
        reloadCountry(profile);
        reload(postalCodeJTextField, profile.getPostalCode());
        reload(emailJTextField, getEMailString());
        reloadProvinceLabel();
        reloadPostalCodeLabel();
        reloadErrorMessage();
        reloadVerify();
        saveJButton.setEnabled(Boolean.FALSE);
    }

    public void setState(final State state) {
    }

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    private String extract(final javax.swing.JTextField jTextField) {
        return SwingUtil.extract(jTextField, Boolean.TRUE);
    }

    private String extractInputAddress() {
        return extract(addressJTextField);
    }

    private String extractInputCity() {
        return extract(cityJTextField);
    }
    
    private String extractInputCountry() {
        if (countryJComboBox.getSelectedIndex() >= 0) {
            return ((Locale) countryJComboBox.getSelectedItem()).getISO3Country();
        } else {
            return null;
        }
    }

    private String extractInputEmail() {
        return extract(emailJTextField);
    }

    private String extractInputMobilePhone() {
        return extract(mobilePhoneJTextField);
    }

    private String extractInputName() {
        return extract(nameJTextField);
    }

    private String extractInputOrganization() {
        return extract(organizationJTextField);
    }

    private String extractInputPhone() {
        return extract(phoneJTextField);
    }

    private String extractInputPostalCode() {
        return extract(postalCodeJTextField);
    }

    private String extractInputProvince() {
        return extract(provinceJTextField);
    }

    private String extractInputTitle() {
        return extract(titleJTextField);
    }

    /**
     * Obtain the profile e-mail address.
     * 
     * @return A <code>ProfileEMail</code>.
     */
    private ProfileEMail getEMail() {
        return 0 < emails.size() ? emails.get(0) : null;
    }

    /**
     * Obtain the profile e-mail address as a string.
     * 
     * @return A profile e-mail address <code>String</code>.
     */
    private String getEMailString() {
        final ProfileEMail email = getEMail();
        if (null == email) {
            return null;
        } else {
            return email.getEmail().toString();
        }
    }

    /**
     * Get the label text minus the ":" at the end.
     * 
     * @param jLabel
     *            A <code>JLabel</code>.
     * @return The label text less any ":".
     */
    private String getLabelText(final javax.swing.JLabel jLabel) {
        return StringUtil.removeAfter(jLabel.getText(), ":");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        verifyJButton = ButtonFactory.create();
        final javax.swing.JLabel phoneJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel mobilePhoneJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel cityJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel addressJLabel = new javax.swing.JLabel();
        verificationKeyJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
        final javax.swing.JButton cancelJButton = ButtonFactory.create();

        verifyJButton.setFont(Fonts.DialogButtonFont);
        verifyJButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("UpdateProfileDialog.Verify"));
        verifyJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verifyJButtonActionPerformed(evt);
            }
        });

        countryJLabel.setFont(Fonts.DialogFont);
        countryJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("UpdateProfileDialog.Country"));

        nameJLabel.setFont(Fonts.DialogFont);
        nameJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("UpdateProfileDialog.Name"));

        nameJTextField.setText("John McClean");

        emailJTextField.setText("john@nypd.org");

        titleJLabel.setFont(Fonts.DialogFont);
        titleJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("UpdateProfileDialog.UserTitle"));

        emailJLabel.setFont(Fonts.DialogFont);
        emailJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("UpdateProfileDialog.Email"));

        titleJTextField.setText("Over The Hill Cop");

        countryJComboBox.setFont(Fonts.DialogTextEntryFont);
        countryJComboBox.setModel(countryModel);
        countryJComboBox.setRenderer(new LocaleRenderer(getController().getLocale()));

        organizationJLabel.setFont(Fonts.DialogFont);
        organizationJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("UpdateProfileDialog.Organization"));

        organizationJTextField.setText("NYPD");

        phoneJLabel.setFont(Fonts.DialogFont);
        phoneJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("UpdateProfileDialog.Phone"));

        postalCodeJTextField.setText("90210");

        postalCodeJLabel.setFont(Fonts.DialogFont);
        postalCodeJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("UpdateProfileDialog.PostalCode"));

        phoneJTextField.setText("555-555-1111");

        mobilePhoneJLabel.setFont(Fonts.DialogFont);
        mobilePhoneJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("UpdateProfileDialog.MobilePhone"));

        mobilePhoneJTextField.setText("555-555-1111");

        provinceJLabel.setFont(Fonts.DialogFont);
        provinceJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("UpdateProfileDialog.Province"));

        provinceJTextField.setText("NY");

        cityJLabel.setFont(Fonts.DialogFont);
        cityJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("UpdateProfileDialog.City"));

        cityJTextField.setText("NYC");

        addressJLabel.setFont(Fonts.DialogFont);
        addressJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("UpdateProfileDialog.Address"));

        addressJTextField.setText("1234 5th Street");

        verificationKeyJLabel.setFont(Fonts.DialogFont);
        verificationKeyJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("UpdateProfileDialog.VerificationKey"));

        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
        errorMessageJLabel.setText("!Error Message!");

        saveJButton.setFont(Fonts.DialogButtonFont);
        saveJButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("UpdateProfileDialog.OK"));
        saveJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setFont(Fonts.DialogButtonFont);
        cancelJButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("UpdateProfileDialog.Cancel"));
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
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
                        .addComponent(nameJLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(titleJLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(organizationJLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(phoneJLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(mobilePhoneJLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(addressJLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cityJLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(provinceJLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(postalCodeJLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(countryJLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(emailJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(verificationKeyJLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(organizationJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                    .addComponent(phoneJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                    .addComponent(mobilePhoneJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                    .addComponent(addressJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                    .addComponent(cityJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                    .addComponent(provinceJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                    .addComponent(postalCodeJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                    .addComponent(countryJComboBox, 0, 282, Short.MAX_VALUE)
                    .addComponent(emailJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                    .addComponent(titleJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nameJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(verificationKeyJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(verifyJButton)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(237, Short.MAX_VALUE)
                .addComponent(saveJButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelJButton)
                .addGap(12, 12, 12))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(errorMessageJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelJButton, saveJButton, verifyJButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(titleJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(organizationJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(organizationJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phoneJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phoneJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mobilePhoneJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mobilePhoneJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addressJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addressJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cityJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cityJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(provinceJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(provinceJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(postalCodeJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(postalCodeJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(countryJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(countryJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(verifyJButton)
                    .addComponent(verificationKeyJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(verificationKeyJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(errorMessageJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelJButton)
                    .addComponent(saveJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void verifyEMail() {
        final Long emailId = getEMail().getEmailId();
        final String verificationKey = extractVerificationKey();
        getController().runVerifyEmail(emailId, verificationKey);
    }

    private void verifyJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verifyJButtonActionPerformed
        if (isVerifyInputValid()) {
            disposeWindow();
            verifyEMail();
        } else {
            // This is done because we may learn the email is unavailable for the first time here.
            reloadErrorMessage();
            saveJButton.setEnabled(isInputValid());
        }
    }//GEN-LAST:event_verifyJButtonActionPerformed

    /**
     * Initialize the list of countries for the country picklist.
     */
    private void initCountryModel() {
        for (final Locale locale : getController().getAvailableLocales()) {
            this.countryModel.addElement(locale);
        }
    }

    /**
     * Initialize document handlers.
     */
    private void initDocumentHandlers() {
        addressJTextField.getDocument().addDocumentListener(new ChangeHandler());
        cityJTextField.getDocument().addDocumentListener(new ChangeHandler());
        emailJTextField.getDocument().addDocumentListener(new ChangeHandler());
        mobilePhoneJTextField.getDocument().addDocumentListener(new ChangeHandler());
        nameJTextField.getDocument().addDocumentListener(new ChangeHandler());
        organizationJTextField.getDocument().addDocumentListener(new ChangeHandler());
        phoneJTextField.getDocument().addDocumentListener(new ChangeHandler());
        postalCodeJTextField.getDocument().addDocumentListener(new ChangeHandler());
        provinceJTextField.getDocument().addDocumentListener(new ChangeHandler());
        titleJTextField.getDocument().addDocumentListener(new ChangeHandler());
        countryJComboBox.addItemListener(new ChangeHandler());
        emailJTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(final DocumentEvent e) {
                reloadVerify();
            }
            public void insertUpdate(final DocumentEvent e) {
                reloadVerify();
            }
            public void removeUpdate(final DocumentEvent e) {
                reloadVerify();
            }
        });
        verificationKeyJTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(final DocumentEvent e) {
                reloadVerify();
            }
            public void insertUpdate(DocumentEvent e) {
                reloadVerify();
            }
            public void removeUpdate(DocumentEvent e) {
                reloadVerify();
            }
        });
    }

    /**
     * Determine if the string changed.
     * 
     * @param current
     *            A <code>String</code>.
     * @param old
     *            A <code>String</code>.
     * @return true if the string changed; false otherwise.
     */
    private Boolean isChanged(final String current, final String old) {
        if (isEmpty(current) || isEmpty(old)) {
            return (isEmpty(current) && isEmpty(old) ? Boolean.FALSE : Boolean.TRUE);
        } else {
            return (0 != current.compareTo(old));
        }
    }

    /**
     * Determine if the string is empty.
     * 
     * @param text
     *            A <code>String</code>.
     * @return true if the string is null or blank; false otherwise.
     */
    private Boolean isEmpty(final String text) {
        return (null==text || 0==text.length() ? Boolean.TRUE : Boolean.FALSE);
    }

    /**
     * Determine if the input has changed from the original state.
     * 
     * @return true if the input changed; false otherwise.
     */
    private Boolean isInputChanged() {
        if (isChanged(extractInputName(), profile.getName()) ||
            isChanged(extractInputTitle(), profile.getTitle()) ||
            isChanged(extractInputOrganization(), profile.getOrganization()) ||
            isChanged(extractInputPhone(), profile.getPhone()) ||
            isChanged(extractInputMobilePhone(), profile.getMobilePhone()) ||
            isChanged(extractInputAddress(), profile.getAddress()) ||
            isChanged(extractInputCity(), profile.getCity()) ||
            isChanged(extractInputProvince(), profile.getProvince()) ||
            isChanged(extractInputCountry(), profile.getCountry()) ||
            isChanged(extractInputPostalCode(), profile.getPostalCode()) ||
            isChanged(extractInputEmail(), getEMailString())) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * Determine if the input email is available.
     * 
     * @return True if the input email is available; false otherwise.
     */
    private Boolean isInputEmailAvailable() {
        final String inputEmail = extractInputEmail();
        if (isChanged(inputEmail, getEMailString())) {
            final EMail newEmail = EMailBuilder.parse(inputEmail);
            final Boolean available = readIsEmailAvailable(newEmail);
            if (!available) {
                unavailableEmail = inputEmail;
            }
            return available;
        } else {
            return Boolean.TRUE;
        }
    }

    /**
     * Determine if the input email is different from the last
     * known unavailable email.
     * 
     * @return True if the input email is available; false otherwise.
     */
    private Boolean isInputEmailAvailableQuick() {
        if (null != unavailableEmail) {
            return (!unavailableEmail.equals(extractInputEmail()));
        } else {
            return Boolean.TRUE;
        }
    }

    /**
     * Determine if the input email is valid.
     * 
     * @return True if the input email is valid; false otherwise.
     */
    private Boolean isInputEmailValid() {
        if (isEmpty(extractInputEmail())) {
            return Boolean.FALSE;
        } else {
            try {
                EMailBuilder.parse(extractInputEmail());
                return Boolean.TRUE;
            } catch(final EMailFormatException efx) {
                return Boolean.FALSE;
            }
        }
    }

    /**
     * Determines if the United States is the selected country.
     * 
     * @return true if the United States is selected; false otherwise
     */
    private Boolean isUnitedStates() {
        if (countryJComboBox.getSelectedIndex() >= 0) {
            return ((Locale) countryJComboBox.getSelectedItem()).getISO3Country().equals("USA");
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * Action when the OK button is pressed. Maybe update the profile.
     * 
     * @param evt
     *            An <code>ActionEvent</code>.
     */
    private void saveJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveJButtonActionPerformed
        if (isInputValid() && isInputEmailAvailable()) {
            disposeWindow();
            updateProfile();
            updateEmail();
        } else {
            // This is done because we may learn the email is unavailable for the first time here.
            reloadErrorMessage();
            saveJButton.setEnabled(isInputValid());
        }
    }//GEN-LAST:event_saveJButtonActionPerformed

    /**
     * Determine whether or not an e-mail address is available.
     * 
     * @param email
     *            An <code>EMail</code>.
     * @return True if the address is not in use.
     */
    private Boolean readIsEmailAvailable(final EMail email) {
        return ((UpdateProfileProvider) contentProvider).readIsEmailAvailable(email);
    }

    /**
     * Read the profile from the content provider.
     * 
     * @return A <code>Profile</code>.
     */
    private Profile readProfile() {
        return ((UpdateProfileProvider) contentProvider).readProfile();
    }

    /**
     * Read the profile email addresses from the content provider.
     * 
     * @return A <code>List&lt;ProfileEMail&gt;</code>.
     */
    private List<ProfileEMail> readEMails() {
        return ((UpdateProfileProvider) contentProvider).readEMails();
    }

    /**
     * Reload a text field with a value.
     * 
     * @param jTextField
     *            A <code>JTextField</code>.
     * @param value
     *            A value <code>String</code>.
     */
    private void reload(final javax.swing.JTextField jTextField,
            final String value) {
        jTextField.setText(null == value ? "" : value);
    }

    /**
     * Reload the country picklist.
     * 
     * @param profile
     *            A <code>Profile</code>.
     */
    private void reloadCountry(final Profile profile) {
        Locale locale;
        for (int i = 0; i < countryModel.getSize(); i++) {
            locale = (Locale) countryModel.getElementAt(i);
            if (locale.getISO3Country().equals(profile.getCountry())) {
                countryModel.setSelectedItem(locale);
            }
        }
    }

    /**
     * Reload the error message.
     */
    private void reloadErrorMessage() {
        if (isEmpty(extractInputName())) {
            errorMessageJLabel.setText(getString("ErrorRequiredField", new Object[] {getLabelText(nameJLabel)}));
        } else if (isEmpty(extractInputTitle())) {
            errorMessageJLabel.setText(getString("ErrorRequiredField", new Object[] {getLabelText(titleJLabel)}));
        } else if (isEmpty(extractInputOrganization())) {
            errorMessageJLabel.setText(getString("ErrorRequiredField", new Object[] {getLabelText(organizationJLabel)}));
        } else if (isEmpty(extractInputCountry())) {
            errorMessageJLabel.setText(getString("ErrorRequiredField", new Object[] {getLabelText(countryJLabel)}));
        } else if (isEmpty(extractInputEmail())) {
            errorMessageJLabel.setText(getString("ErrorRequiredField", new Object[] {getLabelText(emailJLabel)}));
        } else if (!isInputEmailValid()) {
            errorMessageJLabel.setText(getString("ErrorEmailInvalid"));
        } else if (!isInputEmailAvailableQuick()) {
            errorMessageJLabel.setText(getString("ErrorEmailNotAvailable"));
        } else if (isChanged(extractInputEmail(), getEMailString())) {
            errorMessageJLabel.setText(getString("ErrorEmailChanged"));
        } else {
            // NOTE The space ensures the dialog leaves room for this control.
            errorMessageJLabel.setText(" ");
        }
    }

    /**
     * Reload the postal code (ie. postal code or zip code) label.
     */
    private void reloadPostalCodeLabel() {
        if (isUnitedStates()) {
            postalCodeJLabel.setText(getString("ZipCode"));
        } else {
            postalCodeJLabel.setText(getString("PostalCode"));    
        }
    }

    /**
     * Reload the province (ie. province or state) label.
     */
    private void reloadProvinceLabel() {
        if (isUnitedStates()) {
            provinceJLabel.setText(getString("State"));
        } else {
            provinceJLabel.setText(getString("Province"));    
        }
    }

    /**
     * Reload the e-mail verification controls.
     *
     */
    private void reloadVerify() {
        final ProfileEMail email = getEMail();
        if (null != email && !email.isVerified()
                && email.getEmail().toString().equals(extractInputEmail())) {
            verificationKeyJLabel.setVisible(true);
            verificationKeyJTextField.setVisible(true);
            verifyJButton.setVisible(true);
            verifyJButton.setEnabled(isVerifyInputValid());
        } else {
            verificationKeyJLabel.setVisible(false);
            verificationKeyJTextField.setVisible(false);
            verifyJButton.setVisible(false);            
        }
    }

    /**
     * Update the email address.
     */
    private void updateEmail() {
        if (isChanged(extractInputEmail(), getEMailString())) {
            final EMail newEmail = EMailBuilder.parse(extractInputEmail());
            getController().runAddProfileEmail(newEmail);
            getController().runRemoveProfileEmail(emails.get(0).getEmailId());
        }
    }

    /**
     * Update the profile.
     */
    private void updateProfile() {
        final String name = extractInputName();
        final String title = extractInputTitle();
        final String organization = extractInputOrganization();
        final String phone = extractInputPhone();
        final String mobilePhone = extractInputMobilePhone();
        final String address = extractInputAddress();
        final String city = extractInputCity();
        final String province = extractInputProvince();
        final String country = extractInputCountry();
        final String postalCode = extractInputPostalCode();
        getController().runUpdateProfile(name, address, city, country, mobilePhone,
                organization, phone, postalCode, province, title);
    }

    class ChangeHandler implements DocumentListener, ItemListener {
        public void changedUpdate(final DocumentEvent e) {
            checkInput();
        }
        public void insertUpdate(final DocumentEvent e) {
            checkInput();
        }
        public void removeUpdate(final DocumentEvent e) {
            checkInput();
        }
        public void itemStateChanged(final ItemEvent e) {
            checkInput();
            reloadProvinceLabel();
            reloadPostalCodeLabel();
        }
        private void checkInput() {
            if (isInputValid() && isInputChanged()) {
                saveJButton.setEnabled(Boolean.TRUE);
            }
            else {
                saveJButton.setEnabled(Boolean.FALSE);
            }
            reloadErrorMessage();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JTextField addressJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JTextField cityJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JComboBox countryJComboBox = new javax.swing.JComboBox();
    private final javax.swing.JLabel countryJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel emailJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField emailJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField mobilePhoneJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JLabel nameJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField nameJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JLabel organizationJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField organizationJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JTextField phoneJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JLabel postalCodeJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField postalCodeJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JLabel provinceJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField provinceJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JButton saveJButton = ButtonFactory.create();
    private final javax.swing.JLabel titleJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField titleJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JLabel verificationKeyJLabel = new javax.swing.JLabel();
    private javax.swing.JTextField verificationKeyJTextField;
    private javax.swing.JButton verifyJButton;
    // End of variables declaration//GEN-END:variables
}
