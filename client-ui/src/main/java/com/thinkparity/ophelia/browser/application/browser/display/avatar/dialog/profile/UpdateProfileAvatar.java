/*
 * Created on January 31, 2007, 3:14 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;

import com.thinkparity.codebase.BytesFormat;
import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.BytesFormat.Unit;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.email.EMailFormatException;
import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.text.JTextComponentLengthFilter;

import com.thinkparity.codebase.model.backup.Statistics;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileConstraints;
import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.ophelia.model.events.BackupEvent;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.component.TextFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.event.UpdateProfileDispatcher;
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

    /** A size format to use for the backup statistics. */
    private static final BytesFormat BYTES_FORMAT;

    static {
        BYTES_FORMAT = new BytesFormat();
    }

    /** The unit to use when displaying the backup information. */
    private final Unit backupUnit;

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel countryModel;

    /** The emails. */
    private List<ProfileEMail> emails;

    /** The profile. */
    private Profile profile;

    /** An instance of <code>ProfileConstraints</code>. */
    private final ProfileConstraints profileConstraints;

    /** Backup statistics */
    private Statistics statistics;

    /** Unavailable email. */
    private String unavailableEmail = null;

    /** Creates new form UpdateProfileAvatar */
    public UpdateProfileAvatar() {
        super("UpdateProfileAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        this.profileConstraints = ProfileConstraints.getInstance();
        this.countryModel = new DefaultComboBoxModel();
        this.backupUnit = Unit.AUTO;
        initCountryModel();
        initComponents();
        initDocumentHandlers();
        bindEscapeKey("Cancel", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });
        addPropertyChangeListener("eventDispatcher", new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent evt) {
                if (null != evt.getOldValue())
                    ((UpdateProfileDispatcher) evt.getOldValue()).removeListeners(
                            UpdateProfileAvatar.this);
                if (null != evt.getNewValue())
                    ((UpdateProfileDispatcher) evt.getNewValue()).addListeners(
                            UpdateProfileAvatar.this);
            }
        });
    }

    /**
     * Fire a backup event.
     * 
     * This event keeps the backup statistics up to date
     * so it is not necessary to read the information when the
     * dialog is opened by the user (other than the first time).
     * 
     * @param e
     *            A <code>BackupEvent</code>.
     */
    public void fireBackupEvent(final BackupEvent e) {
        this.statistics = e.getStatistics();
        reloadBackupStatistics(statistics);
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
        reloadBackupStatistics();
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
        final javax.swing.JLabel phoneJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel mobilePhoneJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel cityJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel addressJLabel = new javax.swing.JLabel();
        final javax.swing.JSeparator profileJSeparator = new javax.swing.JSeparator();
        final javax.swing.JButton cancelJButton = ButtonFactory.create();

        countryJLabel.setFont(Fonts.DialogFont);
        countryJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.Country"));

        nameJLabel.setFont(Fonts.DialogFont);
        nameJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.Name"));

        nameJTextField.setText("John McClean");
        ((AbstractDocument) nameJTextField.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(profileConstraints.getName()));

        emailJTextField.setText("john@nypd.org");
        ((AbstractDocument) emailJTextField.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(profileConstraints.getEmail()));

        titleJLabel.setFont(Fonts.DialogFont);
        titleJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.UserTitle"));

        emailJLabel.setFont(Fonts.DialogFont);
        emailJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.Email"));

        titleJTextField.setText("Over The Hill Cop");
        ((AbstractDocument) titleJTextField.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(profileConstraints.getTitle()));

        countryJComboBox.setFont(Fonts.DialogTextEntryFont);
        countryJComboBox.setModel(countryModel);
        countryJComboBox.setRenderer(new LocaleRenderer(getController().getLocale()));

        organizationJLabel.setFont(Fonts.DialogFont);
        organizationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.Organization"));

        organizationJTextField.setText("NYPD");
        ((AbstractDocument) organizationJTextField.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(profileConstraints.getOrganization()));

        phoneJLabel.setFont(Fonts.DialogFont);
        phoneJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.Phone"));

        postalCodeJTextField.setText("90210");
        ((AbstractDocument) postalCodeJTextField.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(profileConstraints.getPostalCode()));

        postalCodeJLabel.setFont(Fonts.DialogFont);
        postalCodeJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.PostalCode"));

        phoneJTextField.setText("555-555-1111");
        ((AbstractDocument) phoneJTextField.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(profileConstraints.getPhone()));

        mobilePhoneJLabel.setFont(Fonts.DialogFont);
        mobilePhoneJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.MobilePhone"));

        mobilePhoneJTextField.setText("555-555-1111");
        ((AbstractDocument) mobilePhoneJTextField.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(profileConstraints.getMobilePhone()));

        provinceJLabel.setFont(Fonts.DialogFont);
        provinceJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.Province"));

        provinceJTextField.setText("NY");
        ((AbstractDocument) provinceJTextField.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(profileConstraints.getProvince()));

        cityJLabel.setFont(Fonts.DialogFont);
        cityJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.City"));

        cityJTextField.setText("NYC");
        ((AbstractDocument) cityJTextField.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(profileConstraints.getCity()));

        addressJLabel.setFont(Fonts.DialogFont);
        addressJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.Address"));

        addressJTextField.setText("1234 5th Street");
        ((AbstractDocument) addressJTextField.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(profileConstraints.getAddress()));

        backupJLabel.setFont(Fonts.DialogFont);
        backupJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.Backup"));

        backupStatisticsJLabel.setFont(Fonts.DialogFont);
        backupStatisticsJLabel.setText("1 MB");

        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
        errorMessageJLabel.setText("!Error Message!");

        saveJButton.setFont(Fonts.DialogButtonFont);
        saveJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.OK"));
        saveJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setFont(Fonts.DialogButtonFont);
        cancelJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.Cancel"));
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
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(saveJButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelJButton)
                        .addGap(12, 12, 12))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(provinceJLabel)
                            .addComponent(cityJLabel)
                            .addComponent(addressJLabel)
                            .addComponent(mobilePhoneJLabel)
                            .addComponent(phoneJLabel)
                            .addComponent(organizationJLabel)
                            .addComponent(titleJLabel)
                            .addComponent(nameJLabel)
                            .addComponent(countryJLabel)
                            .addComponent(postalCodeJLabel))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(postalCodeJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                            .addComponent(organizationJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                            .addComponent(phoneJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                            .addComponent(mobilePhoneJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                            .addComponent(addressJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                            .addComponent(cityJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                            .addComponent(provinceJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                            .addComponent(titleJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(nameJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addComponent(countryJComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, 239, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(emailJLabel)
                        .addGap(76, 76, 76)
                        .addComponent(emailJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(backupJLabel)
                        .addGap(13, 13, 13)
                        .addComponent(backupStatisticsJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(profileJSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(errorMessageJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelJButton, saveJButton});

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
                    .addComponent(countryJLabel)
                    .addComponent(countryJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(postalCodeJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(postalCodeJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailJLabel)
                    .addComponent(emailJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(profileJSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backupStatisticsJLabel)
                    .addComponent(backupJLabel))
                .addGap(14, 14, 14)
                .addComponent(errorMessageJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelJButton)
                    .addComponent(saveJButton))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {countryJComboBox, countryJLabel});

    }// </editor-fold>//GEN-END:initComponents

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
        final ChangeHandler changeHandler = new ChangeHandler();
        addressJTextField.getDocument().addDocumentListener(changeHandler);
        cityJTextField.getDocument().addDocumentListener(changeHandler);
        emailJTextField.getDocument().addDocumentListener(changeHandler);
        mobilePhoneJTextField.getDocument().addDocumentListener(changeHandler);
        nameJTextField.getDocument().addDocumentListener(changeHandler);
        organizationJTextField.getDocument().addDocumentListener(changeHandler);
        phoneJTextField.getDocument().addDocumentListener(changeHandler);
        postalCodeJTextField.getDocument().addDocumentListener(changeHandler);
        provinceJTextField.getDocument().addDocumentListener(changeHandler);
        titleJTextField.getDocument().addDocumentListener(changeHandler);
        countryJComboBox.addItemListener(changeHandler);
    }

    /**
     * Determine if the backup feature is enabled.
     * 
     * @return True if the backup feature is enabled.
     */
    private boolean isBackupEnabled() {
        return ((UpdateProfileProvider) contentProvider).isBackupEnabled().booleanValue();
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
     * Determine if the model is online.
     * 
     * @return True if the model is online.
     */
    private boolean isOnline() {
        return ((UpdateProfileProvider) contentProvider).isOnline().booleanValue();
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
     * Read the backup statistics.
     * 
     * @return An instance of <code>Statistics</code>.
     */
    private Statistics readBackupStatistics() {
        return ((UpdateProfileProvider) contentProvider).readBackupStatistics();
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
     * Reload the backup statistics.  If the backup is enabled and the
     * system is online, the backup statistics are displayed.
     */
    private void reloadBackupStatistics() {
        backupJLabel.setText("");
        backupStatisticsJLabel.setText("");
        if (null != statistics) {
            reloadBackupStatistics(statistics);
        } else if (isOnline() && isBackupEnabled()) {
            this.statistics = readBackupStatistics();
            reloadBackupStatistics(statistics);
        }
    }

    /**
     * Reload the backup statistics.  If the backup is enabled and the
     * system is online, the backup statistics are displayed.
     * 
     * @param statistics
     *            A <code>Statistics</code>.
     */
    private void reloadBackupStatistics(final Statistics statistics) {
        backupJLabel.setText(getString("Backup"));
        if (null == backupUnit) {
            backupStatisticsJLabel.setText(BYTES_FORMAT.format(
                    statistics.getDiskUsage()));
        } else {
            backupStatisticsJLabel.setText(BYTES_FORMAT.format(backupUnit,
                    statistics.getDiskUsage()));
        }
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
            // The space ensures the dialog leaves room for this control.
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
    private final javax.swing.JLabel backupJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel backupStatisticsJLabel = new javax.swing.JLabel();
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
    // End of variables declaration//GEN-END:variables
}
