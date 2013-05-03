/*
 * Created on April 2, 2007, 4:02 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.DefaultComboBoxModel;
import javax.swing.text.AbstractDocument;

import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.text.JTextFieldLengthFilter;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileConstraints;
import com.thinkparity.codebase.model.profile.ProfileConstraintsInfo;
import com.thinkparity.codebase.model.user.UserVCard;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.component.TextFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.dialog.profile.IndustryRenderer;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.dialog.profile.LocaleRenderer;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.platform.LearnMore;
import com.thinkparity.ophelia.browser.util.localization.BrowserLocalization;
import com.thinkparity.ophelia.browser.util.localization.Localization;

import com.thinkparity.common.StringUtil.Separator;

/**
 * <b>Title:</b>thinkParity Ophelia UI Browser Profile Signup<br>
 * <b>Description:</b><br>
 * 
 * @author robert@thinkparity.com
 */
public final class UpgradeAccountProfileAvatar extends DefaultUpgradeAccountPage implements
        ProfileConstraintsInfo {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JTextField addressJTextField = TextFactory.create();
    private final javax.swing.JTextField cityJTextField = TextFactory.create();
    private final javax.swing.JComboBox countryJComboBox = new javax.swing.JComboBox();
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JComboBox industryJComboBox = new javax.swing.JComboBox();
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

    /** The industry <code>Localization</code>. */
    private final Localization industryLocalization;

    /** The industry <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel industryModel;

    /** An instance of <code>ProfileConstraints</code>. */
    private final ProfileConstraints profileConstraints;

    /** A reloaded <code>boolean</code> flag. */
    private boolean reloaded;

    /**
     * Create UpgradeAccountProfileAvatar.
     *
     */
    public UpgradeAccountProfileAvatar() {
        super("UpgradeAccountAvatar.Profile", BrowserConstants.DIALOGUE_BACKGROUND);
        this.industryLocalization = new BrowserLocalization("Industry");
        this.profileConstraints = ProfileConstraints.getInstance();
        this.countryModel = new DefaultComboBoxModel();
        this.industryModel = new DefaultComboBoxModel();
        this.reloaded = false;
        initCountryModel();
        initIndustryModel();
        initComponents();
        addValidationListeners();
    }

    /**
     * @see com.thinkparity.codebase.model.profile.ProfileAddressConstraintsProvider#getFeatures()
     * 
     */
    public List<Feature> getFeatures() {
        if (null == input) {
            return Collections.emptyList();
        } else {
            return ((Data) input).getList(UpgradeAccountData.DataKey.FEATURES);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     * 
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_PROFILE_UPGRADE_ACCOUNT_PROFILE;
    }

    /**)
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.UpgradeAccountPage#getNextPageName()
     */
    public String getNextPageName() {
        return getPageName(AvatarId.DIALOG_PROFILE_UPGRADE_ACCOUNT_PAYMENT);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.UpgradeAccountPage#getPreviousPageName()
     */
    public String getPreviousPageName() {
        return getPageName(AvatarId.DIALOG_PROFILE_UPGRADE_ACCOUNT_AGREEMENT);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.DefaultUpgradeAccountPage#isNextOk()
     */
    @Override
    public Boolean isNextOk() {
        if (super.isNextOk()) {
            updateProfile();

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
    public void reload() {
        if (null == input) {
            return;
        } else {
            final Profile profile = getInputProfile();
            reload(nameJTextField, profile.getName());
            reload(userTitleJTextField, profile.getTitle());
            reload(organizationJTextField, profile.getOrganization());
            reload(phoneJTextField, profile.getPhone());
            reload(mobilePhoneJTextField, profile.getMobilePhone());
            reload(addressJTextField, profile.getAddress());
            reload(cityJTextField, profile.getCity());
            reload(provinceJTextField, profile.getProvince());
            reloadCountry(profile);
            reload(postalCodeJTextField, profile.getPostalCode());
            reloadProvinceLabel();
            reloadPostalCodeLabel();
            reloadIndustry(profile);
            reloaded = true;
            validateInput();
        }
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
        if (!reloaded) {
            return;
        }
        final Profile profile = extractProfile(); 

        // check the name does not have an invalid character
        final String name = profile.getName();
        if (null != name &&
                (name.contains("@") || name.contains("(") || name.contains(")"))) {
            addInputError(getString("ErrorNameHasInvalidChar"));
        }

        if ((isEmpty(profile.getName()) && !profileConstraints.getName().isNullable()) ||
                (isEmpty(profile.getTitle()) && !profileConstraints.getTitle().isNullable()) ||
                (isEmpty(profile.getOrganization()) && !profileConstraints.getOrganization().isNullable()) ||
                (isEmpty(profile.getPhone()) && !profileConstraints.getPhone().isNullable()) ||
                (isEmpty(profile.getMobilePhone()) && !profileConstraints.getMobilePhone().isNullable()) ||
                (isEmpty(profile.getAddress()) && !profileConstraints.getAddress(this).isNullable()) ||
                (isEmpty(profile.getCity()) && !profileConstraints.getCity(this).isNullable()) ||
                (isEmpty(profile.getProvince()) && !profileConstraints.getProvince(this).isNullable()) ||
                (isEmpty(profile.getCountry()) && !profileConstraints.getCountry().isNullable()) ||
                (isEmpty(profile.getPostalCode()) && !profileConstraints.getPostalCode(this).isNullable()) ||
                (isEmpty(profile.getIndustry()) && !profileConstraints.getIndustry(this).isNullable())) {
            addInputError(Separator.Space.toString());
        }

        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
        }

        if (isUpgradeAccountDelegateInitialized()) {
            upgradeAccountDelegate.enableNextButton(!containsInputErrors());
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.DefaultUpgradeAccountPage#isLogoPainted()
     */
    @Override
    protected Boolean isLogoPainted() {
        return Boolean.FALSE;
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

        industryJComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent e) {
                validateInput();
            }
        });
    }

    /**
     * Extract the industry from the control.
     * This is the enum name, not the localized value.
     * 
     * @return The industry <code>String</code>.
     */
    private String extractInputIndustry() {
        if (industryJComboBox.getSelectedIndex() >= 0) {
            return ((UserVCard.Industry) industryJComboBox.getSelectedItem()).toString();
        } else {
            return null;
        }
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
        final Profile profile = getInputProfile();
        profile.setName(SwingUtil.extract(nameJTextField, Boolean.TRUE));
        profile.setTitle(SwingUtil.extract(userTitleJTextField, Boolean.TRUE));
        profile.setOrganization(SwingUtil.extract(organizationJTextField, Boolean.TRUE));
        profile.setPhone(SwingUtil.extract(phoneJTextField, Boolean.TRUE));
        profile.setMobilePhone(SwingUtil.extract(mobilePhoneJTextField, Boolean.TRUE));
        profile.setAddress(SwingUtil.extract(addressJTextField, Boolean.TRUE));
        profile.setCity(SwingUtil.extract(cityJTextField, Boolean.TRUE));
        profile.setProvince(SwingUtil.extract(provinceJTextField, Boolean.TRUE));
        profile.setPostalCode(SwingUtil.extract(postalCodeJTextField, Boolean.TRUE));
        profile.setIndustry(extractInputIndustry());

        // Note that setLocale() also sets country and language
        profile.setLocale(extractInputLocale());
        profile.setOrganizationLocale(extractInputLocale());
        profile.setTimeZone(TimeZone.getDefault());

        return profile;
    }

    /**
     * Read the profile from the input.
     * 
     * @return A <code>Profile</code>.
     */
    private Profile getInputProfile() {
        return (Profile) ((Data) input).get(UpgradeAccountData.DataKey.PROFILE);
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
        final javax.swing.JLabel industryJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel starExplanationjLabel = new javax.swing.JLabel();

        setOpaque(false);
        explanationJLabel.setFont(Fonts.DialogFont);
        explanationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Profile.Explanation"));

        nameJLabel.setFont(Fonts.DialogFont);
        nameJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Profile.Name"));

        nameJTextField.setFont(Fonts.DialogTextEntryFont);
        nameJTextField.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        nameJTextField.setMinimumSize(new java.awt.Dimension(300, 20));
        nameJTextField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) nameJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getName()));

        userTitleJLabel.setFont(Fonts.DialogFont);
        userTitleJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Profile.UserTitle"));

        userTitleJTextField.setFont(Fonts.DialogTextEntryFont);
        userTitleJTextField.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        userTitleJTextField.setMinimumSize(new java.awt.Dimension(300, 20));
        userTitleJTextField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) userTitleJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getTitle()));

        organizationJLabel.setFont(Fonts.DialogFont);
        organizationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Profile.Organization"));

        organizationJTextField.setFont(Fonts.DialogTextEntryFont);
        organizationJTextField.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        organizationJTextField.setMinimumSize(new java.awt.Dimension(300, 20));
        organizationJTextField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) organizationJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getOrganization()));

        phoneJLabel.setFont(Fonts.DialogFont);
        phoneJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Profile.Phone"));

        phoneJTextField.setFont(Fonts.DialogTextEntryFont);
        phoneJTextField.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        phoneJTextField.setMinimumSize(new java.awt.Dimension(300, 20));
        phoneJTextField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) phoneJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getPhone()));

        mobilePhoneJLabel.setFont(Fonts.DialogFont);
        mobilePhoneJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Profile.MobilePhone"));

        mobilePhoneJTextField.setFont(Fonts.DialogTextEntryFont);
        mobilePhoneJTextField.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        mobilePhoneJTextField.setMinimumSize(new java.awt.Dimension(300, 20));
        mobilePhoneJTextField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) mobilePhoneJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getMobilePhone()));

        addressJLabel.setFont(Fonts.DialogFont);
        addressJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Profile.Address"));

        addressJTextField.setFont(Fonts.DialogTextEntryFont);
        addressJTextField.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        addressJTextField.setMinimumSize(new java.awt.Dimension(300, 20));
        addressJTextField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) addressJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getAddress(this)));

        cityJLabel.setFont(Fonts.DialogFont);
        cityJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Profile.City"));

        cityJTextField.setFont(Fonts.DialogTextEntryFont);
        cityJTextField.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        cityJTextField.setMinimumSize(new java.awt.Dimension(300, 20));
        cityJTextField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) cityJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getCity(this)));

        provinceJLabel.setFont(Fonts.DialogFont);
        provinceJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Profile.Province"));

        provinceJTextField.setFont(Fonts.DialogTextEntryFont);
        provinceJTextField.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        provinceJTextField.setMinimumSize(new java.awt.Dimension(300, 20));
        provinceJTextField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) provinceJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getProvince(this)));

        countryJLabel.setFont(Fonts.DialogFont);
        countryJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Profile.Country"));

        countryJComboBox.setFont(Fonts.DialogTextEntryFont);
        countryJComboBox.setModel(countryModel);
        countryJComboBox.setMaximumSize(new java.awt.Dimension(300, 32767));
        countryJComboBox.setMinimumSize(new java.awt.Dimension(300, 18));
        countryJComboBox.setPreferredSize(new java.awt.Dimension(300, 20));
        countryJComboBox.setRenderer(new LocaleRenderer(getController().getLocale()));

        postalCodeJLabel.setFont(Fonts.DialogFont);
        postalCodeJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Profile.PostalCode"));

        postalCodeJTextField.setFont(Fonts.DialogTextEntryFont);
        postalCodeJTextField.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        postalCodeJTextField.setMinimumSize(new java.awt.Dimension(300, 20));
        postalCodeJTextField.setPreferredSize(new java.awt.Dimension(300, 20));
        ((AbstractDocument) postalCodeJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getPostalCode(this)));

        industryJLabel.setFont(Fonts.DialogFont);
        industryJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Profile.Industry"));

        industryJComboBox.setFont(Fonts.DialogTextEntryFont);
        industryJComboBox.setModel(industryModel);
        industryJComboBox.setRenderer(new IndustryRenderer(industryLocalization));

        starExplanationjLabel.setFont(Fonts.DialogFont);
        starExplanationjLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Profile.StarExplanation"));

        privacyLearnMoreJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Profile.PrivacyLearnMore"));
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
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(nameJLabel))
                    .addComponent(explanationJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                    .addComponent(errorMessageJLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
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
                                .addComponent(provinceJLabel)
                                .addComponent(industryJLabel))
                            .addGap(38, 38, 38)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(postalCodeJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(userTitleJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(organizationJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(phoneJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(mobilePhoneJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(addressJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cityJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(provinceJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(countryJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(nameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(industryJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(industryJLabel)
                    .addComponent(industryJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(starExplanationjLabel)
                    .addComponent(privacyLearnMoreJLabel))
                .addGap(15, 15, 15)
                .addComponent(errorMessageJLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
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
     * Initialize the list of industries for the industry picklist.
     */
    private void initIndustryModel() {
        for (final UserVCard.Industry industry : UserVCard.Industry.values()) {
            this.industryModel.addElement(industry);
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
        getController().runLearnMore(LearnMore.Topic.PRIVACY);
    }//GEN-LAST:event_privacyLearnMoreJLabelMousePressed

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
     * Reload the country.
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
     * Reload the industry.
     * 
     * @param profile
     *            A <code>Profile</code>.
     */
    private void reloadIndustry(final Profile profile) {
        if (!profile.isSetIndustry()) {
            industryJComboBox.setSelectedIndex(-1);
        } else {
            try {
                final UserVCard.Industry industry = UserVCard.Industry.valueOf(profile.getIndustry());
                industryJComboBox.setSelectedItem(industry);
            } catch (final IllegalArgumentException iae) {
                industryJComboBox.setSelectedIndex(-1);
            } catch (final NullPointerException npe) {
                industryJComboBox.setSelectedIndex(-1);
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

    /**
     * Update the profile.
     */
    private void updateProfile() {
        final Profile profile = extractProfile();
        getController().runUpdateProfile(profile);
    }
}
