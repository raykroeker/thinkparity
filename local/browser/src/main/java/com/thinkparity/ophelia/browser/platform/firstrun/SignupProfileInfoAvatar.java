/*
 * SignupProfileInfoAvatar.java
 *
 * Created on April 2, 2007, 4:02 PM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.DefaultComboBoxModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.email.EMailFormatException;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileVCard;
import com.thinkparity.codebase.model.profile.Reservation;
import com.thinkparity.codebase.model.session.Credentials;

import com.thinkparity.ophelia.model.Constants.Product;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.dialog.profile.LocaleRenderer;
import com.thinkparity.ophelia.browser.platform.BrowserPlatform;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.firstrun.SignupData.DataKey;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 *
 * @author  user
 */
public class SignupProfileInfoAvatar extends Avatar
        implements SignupPage {

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel countryModel;

    /** The <code>Platform</code>. */
    private final Platform platform;

    /** The  <code>SignupDelegate</code>. */
    private SignupDelegate signupDelegate;

    /** Creates new form SignupProfileInfoAvatar */
    public SignupProfileInfoAvatar() {
        super("SignupAvatar.ProfileInfo", BrowserConstants.DIALOGUE_BACKGROUND);
        this.platform = BrowserPlatform.getInstance();
        this.countryModel = new DefaultComboBoxModel();
        initCountryModel();
        initComponents();
        initDocumentHandlers();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     * 
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_PLATFORM_SIGNUP_PROFILE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getNextPageName()
     */
    public String getNextPageName() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getPageName()
     */
    public String getPageName() {
        return getSignupPageId().toString();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getPreviousPageName()
     */
    public String getPreviousPageName() {
        return SignupPageId.ACCOUNT_INFORMATION.toString();
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getState() */
    public State getState() {
        throw Assert.createNotYetImplemented("SignupAvatar.ProfileInfo#getState");
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#isAvatarBackgroundImage()
     */
    @Override
    public Boolean isAvatarBackgroundImage() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#isFirstPage()
     */
    public Boolean isFirstPage() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#isLastPage()
     */
    public Boolean isLastPage() {
        return Boolean.TRUE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#isNextOk()
     */
    public Boolean isNextOk() {
        if (!isInputValid()) {
            return Boolean.FALSE;
        }
        signUp();
        return (!containsInputErrors());
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    public void reload() {
        reloadCountry();
        reloadProvinceLabel();
        reloadPostalCodeLabel();
        validateInput();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#saveData()
     */
    public void saveData() {
        ((Data) input).set(SignupData.DataKey.EMAIL, extractEmail());
        ((Data) input).set(SignupData.DataKey.PROFILE, extractProfile());
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#setSignupDelegate(com.thinkparity.ophelia.browser.platform.firstrun.SignupDelegate)
     */
    public void setSignupDelegate(final SignupDelegate signupDelegate) {
        this.signupDelegate = signupDelegate;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.ophelia.browser.platform.util.State)
     * 
     */
    public void setState(final State state) {
        throw Assert.createNotYetImplemented("SignupAvatar.ProfileInfo#setState");
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#validateInput()
     *
     */
    @Override
    public final void validateInput() {
        super.validateInput();
        final Profile profile = extractProfile(); 
        if (isEmpty(profile.getName()) ||
                isEmpty(profile.getTitle()) ||
                isEmpty(profile.getOrganization()) ||
                isEmpty(profile.getAddress()) ||
                isEmpty(profile.getCity()) ||
                isEmpty(profile.getProvince()) ||
                isEmpty(profile.getCountry()) ||
                isEmpty(extractInputEmail())) {
            addInputError(Separator.Space.toString());
        }
        if (!isEmpty(extractInputEmail()) && !isInputEmailValid()) {
            addInputError(getString("ErrorInvalidEmail"));
        }

        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
        }

        if (isSignupDelegateInitialized()) {
            signupDelegate.enableNextButton(!containsInputErrors());
        }
    }

    /**
     * Create the account.
     */
    private void createAccount() {
        final Reservation reservation = (Reservation)((Data)input).get(SignupData.DataKey.RESERVATION);
        final Credentials credentials = (Credentials)((Data)input).get(SignupData.DataKey.CREDENTIALS);
        final EMail email = extractEmail();
        final Profile profile = extractProfile();
        try {
            profile.setFeatures(extractFeatures());
            platform.runCreateAccount(reservation, credentials, profile, email);
        } catch (final BrowserException bex) {
            addInputError(getString("ErrorCreateAccount"));
        }
    }

    /**
     * Extract the email from the control.
     * 
     * @return The <code>EMail</code>.
     */
    private EMail extractEmail() {
        return EMailBuilder.parse(extractInputEmail());
    }

    /**
     * Extract the features.
     * 
     * @return A list of <code>Feature</code>.
     */
    private List<Feature> extractFeatures() {
        final FeatureSet featureSet = (FeatureSet) ((Data) input).get(DataKey.FEATURE_SET);
        final List<Feature> allFeatures = ((SignupProvider) contentProvider).readFeatures();
        final List<Feature> features = new ArrayList<Feature>();
        switch (featureSet) {
        case FREE:
            break;
        case STANDARD:
            for (final Feature feature : allFeatures)
                if (feature.getName().equals(Product.Features.CORE))
                    features.add(feature);
            break;
        case PREMIUM:
            for (final Feature feature : allFeatures)
                features.add(feature);
            break;
        }
        return features;
    }

    /**
     * Extract the input email string from the control.
     * 
     * @return The email <code>String</code>.
     */
    private String extractInputEmail() {
        return SwingUtil.extract(emailJTextField, Boolean.TRUE);
    }

    /**
     * Extract the locale from the control.
     * 
     * @return The <code>Locale</code>.
     */
    private Locale extractInputLocale() {
        if (countryJComboBox.getSelectedIndex() >= 0) {
            final Locale locale = ((Locale) countryJComboBox.getSelectedItem());
            return new Locale(Locale.getDefault().getLanguage(), locale.getCountry());
        } else {
            return Locale.getDefault();
        }
    }

    /**
     * Extract the profile from the control.
     * 
     * @return The <code>Profile</code>.
     */
    private Profile extractProfile() {
        final Profile profile = new Profile();
        profile.setVCard(new ProfileVCard());
        profile.setAddress(SwingUtil.extract(addressJTextField, Boolean.TRUE));
        profile.setCity(SwingUtil.extract(cityJTextField, Boolean.TRUE));
        // Note that setLocale() also sets country and language
        profile.setLocale(extractInputLocale());
        profile.setMobilePhone(SwingUtil.extract(mobilePhoneJTextField, Boolean.TRUE));
        profile.setName(SwingUtil.extract(nameJTextField, Boolean.TRUE));
        profile.setOrganization(SwingUtil.extract(organizationJTextField, Boolean.TRUE));
        profile.setOrganizationLocale(extractInputLocale());
        profile.setPhone(SwingUtil.extract(phoneJTextField, Boolean.TRUE));
        profile.setPostalCode(SwingUtil.extract(postalCodeJTextField, Boolean.TRUE));
        profile.setProvince(SwingUtil.extract(provinceJTextField, Boolean.TRUE));
        profile.setTimeZone(TimeZone.getDefault());
        profile.setTitle(SwingUtil.extract(userTitleJTextField, Boolean.TRUE));
        return profile;
    }

    /**
     * Get the signup page id.
     * 
     * @return A <code>SignupPageId</code>.
     */
    private SignupPageId getSignupPageId() {
        return SignupPageId.PROFILE_INFORMATION;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        final javax.swing.JLabel titleJLabel = new javax.swing.JLabel();
        final javax.swing.JPanel headingsJPanel = new javax.swing.JPanel();
        final javax.swing.JLabel nameJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel userTitleJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel organizationJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel phoneJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel mobilePhoneJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel addressJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel cityJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel countryJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel emailJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel fillerJLabel = new javax.swing.JLabel();

        setOpaque(false);
        titleJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.ProfileInfo.Title"));

        headingsJPanel.setLayout(new java.awt.GridBagLayout());

        headingsJPanel.setOpaque(false);
        nameJLabel.setFont(Fonts.DialogFont);
        nameJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.ProfileInfo.Name"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        headingsJPanel.add(nameJLabel, gridBagConstraints);

        userTitleJLabel.setFont(Fonts.DialogFont);
        userTitleJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.ProfileInfo.UserTitle"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        headingsJPanel.add(userTitleJLabel, gridBagConstraints);

        organizationJLabel.setFont(Fonts.DialogFont);
        organizationJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.ProfileInfo.Organization"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        headingsJPanel.add(organizationJLabel, gridBagConstraints);

        phoneJLabel.setFont(Fonts.DialogFont);
        phoneJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.ProfileInfo.Phone"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        headingsJPanel.add(phoneJLabel, gridBagConstraints);

        mobilePhoneJLabel.setFont(Fonts.DialogFont);
        mobilePhoneJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.ProfileInfo.MobilePhone"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        headingsJPanel.add(mobilePhoneJLabel, gridBagConstraints);

        addressJLabel.setFont(Fonts.DialogFont);
        addressJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.ProfileInfo.Address"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        headingsJPanel.add(addressJLabel, gridBagConstraints);

        cityJLabel.setFont(Fonts.DialogFont);
        cityJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.ProfileInfo.City"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        headingsJPanel.add(cityJLabel, gridBagConstraints);

        provinceJLabel.setFont(Fonts.DialogFont);
        provinceJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.ProfileInfo.Province"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        headingsJPanel.add(provinceJLabel, gridBagConstraints);

        countryJLabel.setFont(Fonts.DialogFont);
        countryJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.ProfileInfo.Country"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        headingsJPanel.add(countryJLabel, gridBagConstraints);

        postalCodeJLabel.setFont(Fonts.DialogFont);
        postalCodeJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.ProfileInfo.PostalCode"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        headingsJPanel.add(postalCodeJLabel, gridBagConstraints);

        emailJLabel.setFont(Fonts.DialogFont);
        emailJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.ProfileInfo.Email"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        headingsJPanel.add(emailJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 90, 0, 0);
        headingsJPanel.add(fillerJLabel, gridBagConstraints);

        nameJTextField.setFont(Fonts.DialogTextEntryFont);

        userTitleJTextField.setFont(Fonts.DialogTextEntryFont);

        organizationJTextField.setFont(Fonts.DialogTextEntryFont);

        phoneJTextField.setFont(Fonts.DialogTextEntryFont);

        mobilePhoneJTextField.setFont(Fonts.DialogTextEntryFont);

        addressJTextField.setFont(Fonts.DialogTextEntryFont);

        cityJTextField.setFont(Fonts.DialogTextEntryFont);

        provinceJTextField.setFont(Fonts.DialogTextEntryFont);

        countryJComboBox.setFont(Fonts.DialogTextEntryFont);
        countryJComboBox.setModel(countryModel);
        countryJComboBox.setRenderer(new LocaleRenderer(platform.getLocale()));

        postalCodeJTextField.setFont(Fonts.DialogTextEntryFont);

        emailJTextField.setFont(Fonts.DialogTextEntryFont);

        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
        errorMessageJLabel.setText("!Error Message!");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(titleJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(errorMessageJLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(headingsJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(emailJTextField)
                                    .addComponent(nameJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                                    .addComponent(countryJComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(postalCodeJTextField)
                                    .addComponent(userTitleJTextField)
                                    .addComponent(organizationJTextField)
                                    .addComponent(phoneJTextField)
                                    .addComponent(mobilePhoneJTextField)
                                    .addComponent(addressJTextField)
                                    .addComponent(cityJTextField)
                                    .addComponent(provinceJTextField))))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(titleJLabel)
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(headingsJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(userTitleJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(organizationJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(phoneJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mobilePhoneJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addressJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cityJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(provinceJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(countryJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(postalCodeJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(emailJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(22, 22, 22)
                .addComponent(errorMessageJLabel)
                .addContainerGap(37, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Initialize the list of countries for the country picklist.
     */
    private void initCountryModel() {
        for (final Locale locale : platform.getAvailableLocales()) {
            this.countryModel.addElement(locale);
        }
    }

    /**
     * Initialize document handlers.
     */
    private void initDocumentHandlers() {
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
        addressJTextField.getDocument().addDocumentListener(documentListener);
        cityJTextField.getDocument().addDocumentListener(documentListener);
        emailJTextField.getDocument().addDocumentListener(documentListener);
        mobilePhoneJTextField.getDocument().addDocumentListener(documentListener);
        nameJTextField.getDocument().addDocumentListener(documentListener);
        organizationJTextField.getDocument().addDocumentListener(documentListener);
        phoneJTextField.getDocument().addDocumentListener(documentListener);
        postalCodeJTextField.getDocument().addDocumentListener(documentListener);
        provinceJTextField.getDocument().addDocumentListener(documentListener);
        userTitleJTextField.getDocument().addDocumentListener(documentListener);
        countryJComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent e) {
                validateInput();
                reloadProvinceLabel();
                reloadPostalCodeLabel();
            }
        });
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
     * Determine if the input email is valid.
     * 
     * @return True if the input email is valid; false otherwise.
     */
    private Boolean isInputEmailValid() {
        final String email = extractInputEmail();
        if (isEmpty(email)) {
            return Boolean.FALSE;
        } else {
            try {
                EMailBuilder.parse(email);
                return Boolean.TRUE;
            } catch(final EMailFormatException efx) {
                return Boolean.FALSE;
            }
        }
    }

    /**
     * Determine if the signup delegate has been initialized yet.
     * 
     * @return true if the signup delegate has been initialized.
     */
    private Boolean isSignupDelegateInitialized() {
        return (null != signupDelegate);
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
     * Reload the country from the default locale.
     */
    private void reloadCountry() {
        final Locale defaultLocale = Locale.getDefault();
        Locale locale;
        for (int i = 0; i < countryModel.getSize(); i++) {
            locale = (Locale) countryModel.getElementAt(i);
            if (locale.getCountry().equals(defaultLocale.getCountry())) {
                countryModel.setSelectedItem(locale);
            }
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
     * Sign up.
     */
    private void signUp() {
        SwingUtil.setCursor(this, java.awt.Cursor.WAIT_CURSOR);
        errorMessageJLabel.setText(getString("SigningUp"));
        errorMessageJLabel.paintImmediately(0, 0, errorMessageJLabel.getWidth(), errorMessageJLabel.getHeight());
        createAccount();
        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
        }
        SwingUtil.setCursor(this, java.awt.Cursor.DEFAULT_CURSOR);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JTextField addressJTextField = new javax.swing.JTextField();
    private final javax.swing.JTextField cityJTextField = new javax.swing.JTextField();
    private final javax.swing.JComboBox countryJComboBox = new javax.swing.JComboBox();
    private final javax.swing.JTextField emailJTextField = new javax.swing.JTextField();
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField mobilePhoneJTextField = new javax.swing.JTextField();
    private final javax.swing.JTextField nameJTextField = new javax.swing.JTextField();
    private final javax.swing.JTextField organizationJTextField = new javax.swing.JTextField();
    private final javax.swing.JTextField phoneJTextField = new javax.swing.JTextField();
    private final javax.swing.JLabel postalCodeJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField postalCodeJTextField = new javax.swing.JTextField();
    private final javax.swing.JLabel provinceJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField provinceJTextField = new javax.swing.JTextField();
    private final javax.swing.JTextField userTitleJTextField = new javax.swing.JTextField();
    // End of variables declaration//GEN-END:variables
    
}
