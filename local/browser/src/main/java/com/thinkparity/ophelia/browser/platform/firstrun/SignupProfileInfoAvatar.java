/*
 * Created on April 2, 2007, 4:02 PM
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.DefaultComboBoxModel;
import javax.swing.text.AbstractDocument;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.text.JTextFieldLengthFilter;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileConstraints;
import com.thinkparity.codebase.model.profile.ProfileVCard;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.component.TextFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.dialog.profile.LocaleRenderer;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.platform.LearnMore;

/**
 * <b>Title:</b>thinkParity OpheliaUI Sign-Up Profile Info Avatar<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.9
 */
public final class SignupProfileInfoAvatar extends DefaultSignupPage {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JTextField addressJTextField = TextFactory.create();
    private final javax.swing.JTextField cityJTextField = TextFactory.create();
    private final javax.swing.JComboBox countryJComboBox = new javax.swing.JComboBox();
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField mobilePhoneJTextField = TextFactory.create();
    private final javax.swing.JTextField nameJTextField = TextFactory.create();
    private final javax.swing.JTextField organizationJTextField = TextFactory.create();
    private final javax.swing.JTextField phoneJTextField = TextFactory.create();
    private final javax.swing.JLabel postalCodeJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField postalCodeJTextField = TextFactory.create();
    private final javax.swing.JLabel privacyLearnMoreJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JLabel provinceJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField provinceJTextField = TextFactory.create();
    private final javax.swing.JTextField userTitleJTextField = TextFactory.create();
    // End of variables declaration//GEN-END:variables

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel countryModel;

    /** An instance of <code>ProfileConstraints</code>. */
    private final ProfileConstraints profileConstraints;

    /** Creates new form SignupProfileInfoAvatar */
    public SignupProfileInfoAvatar() {
        super("SignupAvatar.ProfileInfo", BrowserConstants.DIALOGUE_BACKGROUND);
        this.profileConstraints = ProfileConstraints.getInstance();
        this.countryModel = new DefaultComboBoxModel();
        initCountryModel();
        initComponents();
        addValidationListeners();
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
        return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_ACCOUNT);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getPreviousPageName()
     */
    public String getPreviousPageName() {
        return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_AGREEMENT);
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
     * @see com.thinkparity.ophelia.browser.platform.firstrun.DefaultSignupPage#setDefaultFocus()
     */
    @Override
    public void setDefaultFocus() {
        nameJTextField.requestFocusInWindow();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#validateInput()
     *
     */
    @Override
    public final void validateInput() {
        super.validateInput();
        final Profile profile = extractProfile(); 

        // check the name does not have an '@' character
        if (null != profile.getName() && profile.getName().contains("@")) {
            addInputError(getString("ErrorNameHasInvalidChar"));
        }

        if (isEmpty(profile.getName()) ||
                isEmpty(profile.getTitle()) ||
                isEmpty(profile.getOrganization()) ||
                isEmpty(profile.getAddress()) ||
                isEmpty(profile.getCity()) ||
                isEmpty(profile.getProvince()) ||
                isEmpty(profile.getCountry()) ||
                isEmpty(profile.getPostalCode())) {
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
     * Add validation listeners.
     */
    private void addValidationListeners() {
        addValidationListener(addressJTextField);
        addValidationListener(cityJTextField);
        addValidationListener(mobilePhoneJTextField);
        addValidationListener(nameJTextField);
        addValidationListener(organizationJTextField);
        addValidationListener(phoneJTextField);
        addValidationListener(postalCodeJTextField);
        addValidationListener(provinceJTextField);
        addValidationListener(userTitleJTextField);

        countryJComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent e) {
                validateInput();
                reloadProvinceLabel();
                reloadPostalCodeLabel();
            }
        });
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
        final javax.swing.JLabel explanationJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel nameJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel userTitleJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel organizationJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel phoneJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel mobilePhoneJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel addressJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel cityJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel countryJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel starExplanationjLabel = new javax.swing.JLabel();

        setOpaque(false);
        explanationJLabel.setFont(Fonts.DialogFont);
        explanationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.Explanation"));

        nameJLabel.setFont(Fonts.DialogFont);
        nameJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.Name"));

        nameJTextField.setFont(Fonts.DialogTextEntryFont);
        nameJTextField.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        nameJTextField.setMinimumSize(new java.awt.Dimension(300, 20));
        nameJTextField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) nameJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getName()));

        userTitleJLabel.setFont(Fonts.DialogFont);
        userTitleJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.UserTitle"));

        userTitleJTextField.setFont(Fonts.DialogTextEntryFont);
        userTitleJTextField.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        userTitleJTextField.setMinimumSize(new java.awt.Dimension(300, 20));
        userTitleJTextField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) userTitleJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getTitle()));

        organizationJLabel.setFont(Fonts.DialogFont);
        organizationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.Organization"));

        organizationJTextField.setFont(Fonts.DialogTextEntryFont);
        organizationJTextField.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        organizationJTextField.setMinimumSize(new java.awt.Dimension(300, 20));
        organizationJTextField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) organizationJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getOrganization()));

        phoneJLabel.setFont(Fonts.DialogFont);
        phoneJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.Phone"));

        phoneJTextField.setFont(Fonts.DialogTextEntryFont);
        phoneJTextField.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        phoneJTextField.setMinimumSize(new java.awt.Dimension(300, 20));
        phoneJTextField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) phoneJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getPhone()));

        mobilePhoneJLabel.setFont(Fonts.DialogFont);
        mobilePhoneJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.MobilePhone"));

        mobilePhoneJTextField.setFont(Fonts.DialogTextEntryFont);
        mobilePhoneJTextField.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        mobilePhoneJTextField.setMinimumSize(new java.awt.Dimension(300, 20));
        mobilePhoneJTextField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) mobilePhoneJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getMobilePhone()));

        addressJLabel.setFont(Fonts.DialogFont);
        addressJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.Address"));

        addressJTextField.setFont(Fonts.DialogTextEntryFont);
        addressJTextField.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        addressJTextField.setMinimumSize(new java.awt.Dimension(300, 20));
        addressJTextField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) addressJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getAddress()));

        cityJLabel.setFont(Fonts.DialogFont);
        cityJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.City"));

        cityJTextField.setFont(Fonts.DialogTextEntryFont);
        cityJTextField.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        cityJTextField.setMinimumSize(new java.awt.Dimension(300, 20));
        cityJTextField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) cityJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getCity()));

        provinceJLabel.setFont(Fonts.DialogFont);
        provinceJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.Province"));

        provinceJTextField.setFont(Fonts.DialogTextEntryFont);
        provinceJTextField.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        provinceJTextField.setMinimumSize(new java.awt.Dimension(300, 20));
        provinceJTextField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) provinceJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getProvince()));

        countryJLabel.setFont(Fonts.DialogFont);
        countryJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.Country"));

        countryJComboBox.setFont(Fonts.DialogTextEntryFont);
        countryJComboBox.setModel(countryModel);
        countryJComboBox.setMaximumSize(new java.awt.Dimension(300, 32767));
        countryJComboBox.setMinimumSize(new java.awt.Dimension(300, 18));
        countryJComboBox.setPreferredSize(new java.awt.Dimension(300, 20));
        countryJComboBox.setRenderer(new LocaleRenderer(platform.getLocale()));

        postalCodeJLabel.setFont(Fonts.DialogFont);
        postalCodeJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.PostalCode"));

        postalCodeJTextField.setFont(Fonts.DialogTextEntryFont);
        postalCodeJTextField.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        postalCodeJTextField.setMinimumSize(new java.awt.Dimension(300, 20));
        postalCodeJTextField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) postalCodeJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getPostalCode()));

        starExplanationjLabel.setFont(Fonts.DialogFont);
        starExplanationjLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.StarExplanation"));

        privacyLearnMoreJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.ProfileInfo.PrivacyLearnMore"));
        privacyLearnMoreJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                privacyLearnMoreJLabelMousePressed(evt);
            }
        });

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
                    .addComponent(errorMessageJLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(nameJLabel))
                    .addComponent(explanationJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(starExplanationjLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(privacyLearnMoreJLabel))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(16, 16, 16)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(userTitleJLabel)
                                .addComponent(organizationJLabel)
                                .addComponent(phoneJLabel)
                                .addComponent(mobilePhoneJLabel)
                                .addComponent(addressJLabel)
                                .addComponent(cityJLabel)
                                .addComponent(postalCodeJLabel)
                                .addComponent(countryJLabel)
                                .addComponent(provinceJLabel))
                            .addGap(38, 38, 38)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(postalCodeJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(userTitleJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(organizationJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(phoneJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(mobilePhoneJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(addressJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cityJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(provinceJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(countryJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(nameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(explanationJLabel)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameJLabel)
                    .addComponent(nameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userTitleJLabel)
                    .addComponent(userTitleJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(organizationJLabel)
                    .addComponent(organizationJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phoneJLabel)
                    .addComponent(phoneJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mobilePhoneJLabel)
                    .addComponent(mobilePhoneJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addressJLabel)
                    .addComponent(addressJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cityJLabel)
                    .addComponent(cityJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(provinceJLabel)
                    .addComponent(provinceJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(countryJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(countryJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(postalCodeJLabel)
                    .addComponent(postalCodeJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(starExplanationjLabel)
                    .addComponent(privacyLearnMoreJLabel))
                .addGap(15, 15, 15)
                .addComponent(errorMessageJLabel)
                .addContainerGap(35, Short.MAX_VALUE))
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

    private void privacyLearnMoreJLabelMousePressed(final java.awt.event.MouseEvent evt) {//GEN-FIRST:event_privacyLearnMoreJLabelMousePressed
        platform.runLearnMore(LearnMore.Topic.PRIVACY);
    }//GEN-LAST:event_privacyLearnMoreJLabelMousePressed

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
}
