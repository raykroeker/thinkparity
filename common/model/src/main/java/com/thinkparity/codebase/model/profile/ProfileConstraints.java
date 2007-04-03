/*
 * Created On:  3-Apr-07 10:06:15 AM
 */
package com.thinkparity.codebase.model.profile;

import com.thinkparity.codebase.constraint.*;

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

    /** The profile address <code>StringConstraint</code>. */
    private final StringConstraint address;

    /** The profile city <code>StringConstraint</code>. */
    private final StringConstraint city;

    /** The profile country <code>CountryConstraint</code>. */
    private final CountryConstraint country;

    /** The profile e-mail address <code>EMailConstraint</code>. */
    private final EMailConstraint email;

    /** The profile language <code>LanguageConstraint</code>. */
    private final LanguageConstraint language;

    /** The profile mobile phone <code>PhoneNumberConstraint</code>. */
    private final PhoneNumberConstraint mobilePhone;

    /** The profile name <code>StringConstraint</code>. */
    private final StringConstraint name;

    /** The profile organization <code>StringConstraint</code>. */
    private final StringConstraint organization;

    /** The profile organization country <code>CountryConstraint</code>. */
    private final CountryConstraint organizationCountry;

    /** The profile password <code>PasswordConstraint</code>. */
    private final PasswordConstraint password;

    /** The profile phone <code>PhoneNumberConstraint</code>. */
    private final PhoneNumberConstraint phone;

    /** The profile postal code <code>StringConstraint</code>. */
    private final StringConstraint postalCode;

    /** The profile province <code>StringConstraint</code>. */
    private final StringConstraint province;

    /** The profile time zone <code>TimeZoneConstraint</code>. */
    private final TimeZoneConstraint timeZone;

    /** The profile tile <code>StringConstraint</code>. */
    private final StringConstraint title;

    /** The profile username <code>StringConstraint</code>. */
    private final StringConstraint username;

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
        this.address.setNullable(Boolean.TRUE);

        this.city = new StringConstraint();
        this.city.setMaxLength(64);
        this.city.setMinLength(1);
        this.city.setName("City");
        this.city.setNullable(Boolean.TRUE);

        this.country = new CountryConstraint();
        this.country.setName("Country");
        this.country.setNullable(Boolean.FALSE);

        this.email = new EMailConstraint();
        this.email.setMaxLength(512);
        this.email.setMinLength(1);
        this.email.setName("EMail");
        this.email.setNullable(Boolean.TRUE);

        this.mobilePhone = new PhoneNumberConstraint();
        this.mobilePhone.setMaxLength(64);
        this.mobilePhone.setMinLength(1);
        this.mobilePhone.setName("Mobile phone");
        this.mobilePhone.setNullable(Boolean.TRUE);

        this.language = new LanguageConstraint();
        this.language.setName("Language");
        this.language.setNullable(Boolean.FALSE);

        this.name = new StringConstraint();
        this.name.setMaxLength(64);
        this.name.setMinLength(1);
        this.name.setName("Name");
        this.name.setNullable(Boolean.FALSE);

        this.organization = new StringConstraint();
        this.organization.setMaxLength(64);
        this.organization.setMinLength(1);
        this.organization.setName("Organization");
        this.organization.setNullable(Boolean.FALSE);

        this.organizationCountry = new CountryConstraint();
        this.organizationCountry.setName("Organization country");
        this.organizationCountry.setNullable(Boolean.FALSE);

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
        this.postalCode.setNullable(Boolean.TRUE);

        this.province = new StringConstraint();
        this.province.setMaxLength(64);
        this.province.setMinLength(1);
        this.province.setName("Province");
        this.province.setNullable(Boolean.TRUE);

        this.timeZone = new TimeZoneConstraint();
        this.timeZone.setName("Time zone");
        this.timeZone.setNullable(Boolean.FALSE);

        this.title = new StringConstraint();
        this.title.setMaxLength(64);
        this.title.setMinLength(1);
        this.title.setName("Title");
        this.title.setNullable(Boolean.FALSE);

        this.username = new StringConstraint();
        this.username.setMaxLength(32);
        this.username.setMinLength(6);
        this.username.setName("Username");
        this.username.setNullable(Boolean.FALSE);
    }

    /**
     * Obtain address.
     *
     * @return A StringConstraint.
     */
    public StringConstraint getAddress() {
        return address;
    }

    /**
     * Obtain city.
     *
     * @return A StringConstraint.
     */
    public StringConstraint getCity() {
        return city;
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
    public EMailConstraint getEmail() {
        return email;
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
     * Obtain organizationCountry.
     *
     * @return A CountryConstraint.
     */
    public CountryConstraint getOrganizationCountry() {
        return organizationCountry;
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
     * Obtain postalCode.
     *
     * @return A StringConstraint.
     */
    public StringConstraint getPostalCode() {
        return postalCode;
    }

    /**
     * Obtain province.
     *
     * @return A StringConstraint.
     */
    public StringConstraint getProvince() {
        return province;
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

    
}
