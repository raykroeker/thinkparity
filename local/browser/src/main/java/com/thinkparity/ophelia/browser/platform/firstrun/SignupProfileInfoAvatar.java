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
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileVCard;
import com.thinkparity.codebase.model.profile.Reservation;
import com.thinkparity.codebase.model.session.Credentials;

import com.thinkparity.ophelia.model.Constants.Product;
import com.thinkparity.ophelia.model.profile.ReservationExpiredException;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.dialog.profile.LocaleRenderer;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.firstrun.SignupData.DataKey;

/**
 *
 * @author  user
 */
public class SignupProfileInfoAvatar extends DefaultSignupPage {

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel countryModel;

    /** Creates new form SignupProfileInfoAvatar */
    public SignupProfileInfoAvatar() {
        super("SignupAvatar.ProfileInfo", BrowserConstants.DIALOGUE_BACKGROUND);
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
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getPreviousPageName()
     */
    public String getPreviousPageName() {
        final FeatureSet featureSet = (FeatureSet) ((Data) input).get(DataKey.FEATURE_SET);
        if (featureSet == FeatureSet.FREE) {
            return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_ACCOUNT);
        } else {
            // TODO When ready, hook in payment tab.
            return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_ACCOUNT);
            //return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_PAYMENT);
        }
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
        ((Data) input).set(SignupData.DataKey.PROFILE, extractProfile());
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
                isEmpty(profile.getCountry())) {
            addInputError(Separator.Space.toString());
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
        final Profile profile = extractProfile();
        final EMail email = (EMail)((Data)input).get(SignupData.DataKey.EMAIL);
        try {
            profile.setFeatures(extractFeatures());
            ((SignupProvider) contentProvider).createProfile(reservation,
					credentials, profile, email);
        } catch (final ReservationExpiredException rex) {
        	addInputError(getString("ErrorReservationExpired"));
        } catch (final Throwable t) {
            addInputError(getString("ErrorCreateAccount"));
        }
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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.JLabel titleJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel nameJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel userTitleJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel organizationJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel phoneJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel mobilePhoneJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel addressJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel cityJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel countryJLabel = new javax.swing.JLabel();

        setOpaque(false);
        titleJLabel.setFont(Fonts.DialogFont);
        titleJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.Title"));

        nameJLabel.setFont(Fonts.DialogFont);
        nameJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.Name"));

        nameJTextField.setFont(Fonts.DialogTextEntryFont);

        userTitleJLabel.setFont(Fonts.DialogFont);
        userTitleJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.UserTitle"));

        userTitleJTextField.setFont(Fonts.DialogTextEntryFont);

        organizationJLabel.setFont(Fonts.DialogFont);
        organizationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.Organization"));

        organizationJTextField.setFont(Fonts.DialogTextEntryFont);

        phoneJLabel.setFont(Fonts.DialogFont);
        phoneJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.Phone"));

        phoneJTextField.setFont(Fonts.DialogTextEntryFont);

        mobilePhoneJLabel.setFont(Fonts.DialogFont);
        mobilePhoneJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.MobilePhone"));

        mobilePhoneJTextField.setFont(Fonts.DialogTextEntryFont);

        addressJLabel.setFont(Fonts.DialogFont);
        addressJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.Address"));

        addressJTextField.setFont(Fonts.DialogTextEntryFont);

        cityJLabel.setFont(Fonts.DialogFont);
        cityJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.City"));

        cityJTextField.setFont(Fonts.DialogTextEntryFont);

        provinceJLabel.setFont(Fonts.DialogFont);
        provinceJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.Province"));

        provinceJTextField.setFont(Fonts.DialogTextEntryFont);

        countryJLabel.setFont(Fonts.DialogFont);
        countryJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.Country"));

        countryJComboBox.setFont(Fonts.DialogTextEntryFont);
        countryJComboBox.setModel(countryModel);
        countryJComboBox.setRenderer(new LocaleRenderer(platform.getLocale()));

        postalCodeJLabel.setFont(Fonts.DialogFont);
        postalCodeJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.PostalCode"));

        postalCodeJTextField.setFont(Fonts.DialogTextEntryFont);

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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameJLabel)
                            .addComponent(userTitleJLabel)
                            .addComponent(organizationJLabel)
                            .addComponent(phoneJLabel)
                            .addComponent(mobilePhoneJLabel)
                            .addComponent(addressJLabel)
                            .addComponent(cityJLabel)
                            .addComponent(provinceJLabel)
                            .addComponent(postalCodeJLabel)
                            .addComponent(countryJLabel))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nameJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                            .addComponent(countryJComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(postalCodeJTextField)
                            .addComponent(userTitleJTextField)
                            .addComponent(organizationJTextField)
                            .addComponent(phoneJTextField)
                            .addComponent(mobilePhoneJTextField)
                            .addComponent(addressJTextField)
                            .addComponent(cityJTextField)
                            .addComponent(provinceJTextField))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(errorMessageJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(titleJLabel)
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userTitleJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userTitleJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(organizationJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(organizationJLabel))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(phoneJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                            .addComponent(cityJLabel)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(phoneJLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(provinceJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(provinceJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(countryJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(countryJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(postalCodeJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(postalCodeJLabel))
                .addGap(22, 22, 22)
                .addComponent(errorMessageJLabel)
                .addContainerGap(23, Short.MAX_VALUE))
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
