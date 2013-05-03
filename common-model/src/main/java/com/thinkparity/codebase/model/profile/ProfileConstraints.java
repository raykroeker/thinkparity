/*
 * Created On:  3-Apr-07 10:06:15 AM
 */
package com.thinkparity.codebase.model.profile;

import java.util.List;

import com.thinkparity.codebase.constraint.*;
import com.thinkparity.codebase.constraint.IllegalValueException.Reason;

import com.thinkparity.codebase.model.migrator.Feature;

/**
 * <b>Title:</b>thinkParity CommonModel Profile Constraints<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ProfileConstraints {

    /** The singleton instance of <code>ProfileConstraints</code>. */
    private static ProfileConstraints INSTANCE;

    /**
     * Obtain an instance of profile constraints.
     * 
     * @return An instance of <code>ProfileConstraints</code>.
     */
    public static ProfileConstraints getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new ProfileConstraints();
        }
        return INSTANCE;
    }

    /**
     * Determine whether or not the feature list contains a feature with a given
     * name.
     * 
     * @param featureList
     *            A <code>List<Feature></code>.
     * @param name
     *            A <code>Feature.Name</code>.
     * @return A <code>Boolean</code>.
     */
    private static Boolean contains(final List<Feature> featureList, final Feature.Name name) {
        for (final Feature feature : featureList) {
            if (feature.getName() == name) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /** The profile address <code>StringConstraint</code>. */
    private final StringConstraint address;

    /** The profile city <code>StringConstraint</code>. */
    private final StringConstraint city;

    /** The profile country <code>CountryConstraint</code>. */
    private final CountryConstraint country;

    /** The profile e-mail address <code>EMailConstraint</code>. */
    private final EMailConstraint email;

    /** The profile industry <code>StringConstraint</code>. */
    private final StringConstraint industry;

    /** The profile language <code>LanguageConstraint</code>. */
    private final LanguageConstraint language;

    /** The profile mobile phone <code>PhoneNumberConstraint</code>. */
    private final PhoneNumberConstraint mobilePhone;

    /** The profile name <code>StringConstraint</code>. */
    private final StringConstraint name;

    /** The profile organization <code>StringConstraint</code>. */
    private final StringConstraint organization;

    /** The profile organization addresss string constraint. */
    private final StringConstraint organizationAddress;

    /** The profile organization city sring constraint. */
    private final StringConstraint organizationCity;

    /** The profile organization country <code>CountryConstraint</code>. */
    private final CountryConstraint organizationCountry;

    /** The profile organization phone number constraint. */
    private final PhoneNumberConstraint organizationPhone;

    /** The profile organization postal code string constraint. */
    private final StringConstraint organizationPostalCode;

    /** The profile organization province string constraint. */
    private final StringConstraint organizationProvince;

    /** The profile password <code>PasswordConstraint</code>. */
    private final PasswordConstraint password;

    /** The profile phone <code>PhoneNumberConstraint</code>. */
    private final PhoneNumberConstraint phone;

    /** The profile postal code <code>StringConstraint</code>. */
    private final StringConstraint postalCode;

    /** The profile province <code>StringConstraint</code>. */
    private final StringConstraint province;

    /** The profile security answer <code>StringConstraint</code>. */
    private final StringConstraint securityAnswer;

    /** The profile security question <code>StringConstraint</code>. */
    private final StringConstraint securityQuestion;

    /** The profile time zone <code>TimeZoneConstraint</code>. */
    private final TimeZoneConstraint timeZone;

    /** The profile tile <code>StringConstraint</code>. */
    private final StringConstraint title;

    /** The profile username <code>StringConstraint</code>. */
    private final StringConstraint username;

    /** The verify email key <code>StringConstraint</code>. */
    private final StringConstraint verifyEmailKey;

    /**
     * Create ProfileConstraints.
     * 
     */
    public ProfileConstraints() {
        super();
        this.address = new StringConstraint();
        this.address.setMaxLength(64);
        this.address.setMinLength(1);
        this.address.setName("Address");
        this.address.setNullable(Boolean.FALSE);

        this.city = new StringConstraint();
        this.city.setMaxLength(64);
        this.city.setMinLength(1);
        this.city.setName("City");
        this.city.setNullable(Boolean.FALSE);

        this.country = new CountryConstraint();
        this.country.setName("Country");
        this.country.setNullable(Boolean.FALSE);

        this.email = new EMailConstraint();
        this.email.setMaxLength(512);
        this.email.setMinLength(1);
        this.email.setName("EMail");
        this.email.setNullable(Boolean.TRUE);

        this.industry = new StringConstraint();
        this.industry.setMaxLength(64);
        this.industry.setMinLength(1);
        this.industry.setName("Industry");
        this.industry.setNullable(Boolean.FALSE);

        this.mobilePhone = new PhoneNumberConstraint();
        this.mobilePhone.setMaxLength(64);
        this.mobilePhone.setMinLength(1);
        this.mobilePhone.setName("Mobile phone");
        this.mobilePhone.setNullable(Boolean.TRUE);

        this.language = new LanguageConstraint();
        this.language.setName("Language");
        this.language.setNullable(Boolean.FALSE);

        /*
         * HACK - ProfileConstraints#<init> - the requirement to filter out the
         * characters for ease of parsing is incorrect
         */
        final char[] invalidChars = new char[] { '@', '(', ')' };
        this.name = new StringConstraint() {
            @Override
            public void validate(final String value) {
                super.validate(value);
                final char[] valueChars = value.toCharArray();
                for (final char valueChar : valueChars) {
                    for (final char invalidChar : invalidChars) {
                        if (valueChar == invalidChar) {
                            invalidate(Reason.ILLEGAL, value);
                        }
                    }
                }
            }
        };
        this.name.setMaxLength(64);
        this.name.setMinLength(1);
        this.name.setName("Name");
        this.name.setNullable(Boolean.FALSE);

        this.organization = new StringConstraint();
        this.organization.setMaxLength(64);
        this.organization.setMinLength(1);
        this.organization.setName("Organization");
        this.organization.setNullable(Boolean.FALSE);

        this.organizationAddress = new StringConstraint();
        this.organizationAddress.setMaxLength(64);
        this.organizationAddress.setMinLength(1);
        this.organizationAddress.setName("Organization address");
        this.organizationAddress.setNullable(Boolean.TRUE);

        this.organizationCity = new StringConstraint();
        this.organizationCity.setMaxLength(64);
        this.organizationCity.setMinLength(1);
        this.organizationCity.setName("Organization city");
        this.organizationCity.setNullable(Boolean.TRUE);

        this.organizationCountry = new CountryConstraint();
        this.organizationCountry.setName("Organization country");
        this.organizationCountry.setNullable(Boolean.FALSE);

        this.organizationPhone = new PhoneNumberConstraint();
        this.organizationPhone.setMaxLength(64);
        this.organizationPhone.setMinLength(1);
        this.organizationPhone.setName("Organization phone");
        this.organizationPhone.setNullable(Boolean.TRUE);

        this.organizationPostalCode = new StringConstraint();
        this.organizationPostalCode.setMaxLength(64);
        this.organizationPostalCode.setMinLength(1);
        this.organizationPostalCode.setName("Organization postal code");
        this.organizationPostalCode.setNullable(Boolean.TRUE);

        this.organizationProvince = new StringConstraint();
        this.organizationProvince.setMaxLength(64);
        this.organizationProvince.setMinLength(1);
        this.organizationProvince.setName("Organization province");
        this.organizationProvince.setNullable(Boolean.TRUE);

        this.password = new PasswordConstraint();
        this.password.setMaxLength(32);
        this.password.setMinLength(6);
        this.password.setName("Password");
        this.password.setNullable(Boolean.FALSE);

        this.phone = new PhoneNumberConstraint();
        this.phone.setMaxLength(64);
        this.phone.setMinLength(1);
        this.phone.setName("Phone");
        this.phone.setNullable(Boolean.TRUE);

        this.postalCode = new StringConstraint();
        this.postalCode.setMaxLength(64);
        this.postalCode.setMinLength(1);
        this.postalCode.setName("Postal code");
        this.postalCode.setNullable(Boolean.FALSE);

        this.province = new StringConstraint();
        this.province.setMaxLength(64);
        this.province.setMinLength(1);
        this.province.setName("Province");
        this.province.setNullable(Boolean.FALSE);

        this.securityAnswer = new StringConstraint();
        this.securityAnswer.setMaxLength(64);
        this.securityAnswer.setMinLength(1);
        this.securityAnswer.setName("Security Answer");
        this.securityAnswer.setNullable(Boolean.FALSE);

        this.securityQuestion = new StringConstraint();
        this.securityQuestion.setMaxLength(64);
        this.securityQuestion.setMinLength(1);
        this.securityQuestion.setName("Security Question");
        this.securityQuestion.setNullable(Boolean.FALSE);

        this.timeZone = new TimeZoneConstraint();
        this.timeZone.setName("Time zone");
        this.timeZone.setNullable(Boolean.FALSE);

        this.title = new StringConstraint();
        this.title.setMaxLength(64);
        this.title.setMinLength(1);
        this.title.setName("Title");
        this.title.setNullable(Boolean.FALSE);

        final String validUsernameChars = "abcdefghijklmnopqrstuvwxyz0123456789-._+";
        this.username = new StringConstraint() {
            @Override
            public void validate(final String value) {
                super.validate(value);
                final String text = value.toLowerCase();
                for (int index = 0; index < text.length(); index++) {
                    if (-1 == validUsernameChars.indexOf(text.charAt(index))) {
                        invalidate(Reason.ILLEGAL, value);
                    }
                }
            }
        };
        this.username.setMaxLength(name.getMaxLength() + organization.getMaxLength() + 5);
        this.username.setMinLength(6);
        this.username.setName("Username");
        this.username.setNullable(Boolean.FALSE);

        this.verifyEmailKey = new StringConstraint();
        this.verifyEmailKey.setMaxLength(512);
        this.verifyEmailKey.setMinLength(1);
        this.verifyEmailKey.setName("Verify Email Key");
        this.verifyEmailKey.setNullable(Boolean.FALSE);
    }

    /**
     * Obtain address.
     * 
     * @return A StringConstraint.
     */
    public StringConstraint getAddress(final ProfileConstraintsInfo info) {
        final StringConstraint clone = (StringConstraint) address.clone();
        clone.setNullable(Boolean.FALSE == contains(info.getFeatures(), Feature.Name.CORE));
        return clone;
    }

    /**
     * Obtain city.
     * 
     * @param info
     *            A <code>ProfileConstraintsInfo</code>.
     * @return A <code>StringConstraint</code>.
     */
    public StringConstraint getCity(final ProfileConstraintsInfo info) {
        final StringConstraint clone = (StringConstraint) city.clone();
        clone.setNullable(Boolean.FALSE == contains(info.getFeatures(), Feature.Name.CORE));
        return clone;
    }

    /**
     * Obtain country.
     * 
     * @return A CountryConstraint.
     */
    public CountryConstraint getCountry() {
        return country;
    }

    /**
     * Obtain email.
     * 
     * @return A EMailConstraint.
     */
    public EMailConstraint getEMail() {
        return email;
    }

    /**
     * Obtain industry.
     * 
     * @param info
     *            A <code>ProfileConstraintsInfo</code>.
     * @return A StringConstraint.
     */
    public StringConstraint getIndustry(final ProfileConstraintsInfo info) {
        final StringConstraint clone = (StringConstraint) industry.clone();
        clone.setNullable(Boolean.FALSE == contains(info.getFeatures(), Feature.Name.CORE));
        return clone;
    }

    /**
     * Obtain language.
     * 
     * @return A LanguageConstraint.
     */
    public LanguageConstraint getLanguage() {
        return language;
    }

    /**
     * Obtain mobilePhone.
     * 
     * @return A PhoneNumberConstraint.
     */
    public PhoneNumberConstraint getMobilePhone() {
        return mobilePhone;
    }

    /**
     * Obtain name.
     * 
     * @return A StringConstraint.
     */
    public StringConstraint getName() {
        return name;
    }

    /**
     * Obtain organization.
     * 
     * @return A StringConstraint.
     */
    public StringConstraint getOrganization() {
        return organization;
    }

    /**
     * Obtain the organizationAddress.
     * 
     * @return A <code>StringConstraint</code>.
     */
    public StringConstraint getOrganizationAddress() {
        return organizationAddress;
    }

    /**
     * Obtain the organizationCity.
     *
     * @return A <code>StringConstraint</code>.
     */
    public StringConstraint getOrganizationCity() {
        return organizationCity;
    }

    /**
     * Obtain organizationCountry.
     * 
     * @return A CountryConstraint.
     */
    public CountryConstraint getOrganizationCountry() {
        return organizationCountry;
    }

    /**
     * Obtain the organizationPhone.
     *
     * @return A <code>PhoneNumberConstraint</code>.
     */
    public PhoneNumberConstraint getOrganizationPhone() {
        return organizationPhone;
    }

    /**
     * Obtain the organizationPostalCode.
     *
     * @return A <code>StringConstraint</code>.
     */
    public StringConstraint getOrganizationPostalCode() {
        return organizationPostalCode;
    }

    /**
     * Obtain the organizationProvince.
     *
     * @return A <code>StringConstraint</code>.
     */
    public StringConstraint getOrganizationProvince() {
        return organizationProvince;
    }

    /**
     * Obtain the password constraint.
     * 
     * @return A <code>PasswordConstraint</code>.
     */
    public PasswordConstraint getPassword() {
        return password;
    }

    /**
     * Obtain phone.
     * 
     * @return A PhoneNumberConstraint.
     */
    public PhoneNumberConstraint getPhone() {
        return phone;
    }

    /**
     * Obtain postal code constraints.
     * 
     * @param info
     *            A <code>ProfileConstraintsInfo</code>.
     * @return A <code>StringConstraint</code>.
     */
    public StringConstraint getPostalCode(final ProfileConstraintsInfo info) {
        final StringConstraint clone = (StringConstraint) postalCode.clone();
        clone.setNullable(Boolean.FALSE == contains(info.getFeatures(), Feature.Name.CORE));
        return clone;
    }

    /**
     * Obtain province constraints.
     * 
     * @param info
     *            A <code>ProfileConstraintsInfo</code>.
     * @return A <code>StringConstraint</code>.
     */
    public StringConstraint getProvince(final ProfileConstraintsInfo info) {
        final StringConstraint clone = (StringConstraint) province.clone();
        clone.setNullable(Boolean.FALSE == contains(info.getFeatures(), Feature.Name.CORE));
        return clone;
    }

    /**
     * Obtain security answer.
     * 
     * @return A StringConstraint.
     */
    public StringConstraint getSecurityAnswer() {
        return securityAnswer;
    }

    /**
     * Obtain security question.
     * 
     * @return A StringConstraint.
     */
    public StringConstraint getSecurityQuestion() {
        return securityQuestion;
    }

    /**
     * Obtain timeZone.
     * 
     * @return A TimeZoneConstraint.
     */
    public TimeZoneConstraint getTimeZone() {
        return timeZone;
    }

    /**
     * Obtain title.
     * 
     * @return A StringConstraint.
     */
    public StringConstraint getTitle() {
        return title;
    }

    /**
     * Obtain the username constraint.
     * 
     * @return A <code>StringConstraint</code>.
     */
    public StringConstraint getUsername() {
        return username;
    }

    /**
     * Obtain the verify email key constraint.
     * 
     * @return A <code>StringConstraint</code>.
     */
    public StringConstraint getVerifyEmailKey() {
        return verifyEmailKey;
    }
}
