/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.codebase.model.profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.service.adapter.LocaleXmlAdapter;

/**
 * <b>Title:</b>thinkParity Profile<br>
 * <b>Description:</b>A thinkParity profile is the same as a thinkParity
 * {@link User} save it represents the local user. Additional functionality is
 * that the profile contains e-mail addresses as well as the user's v-card
 * information.
 * 
 * @author CreateModel.groovy
 * @version
 */
public class Profile extends User {

    /** An active flag. */
    private Boolean active;

    /** A list of <code>Feature</code>s the user has enabled. */
    private final List<Feature> features;

    /** The profile's vcard info. */
    private ProfileVCard vcard;

    /**
     * Create Profile.
     *
     */
	public Profile() {
        super();
        this.features = new ArrayList<Feature>(2);
    }

    /**
     * Add a feature.
     * 
     * @param feature
     *            A <code>Feature</code>.
     * @return True if the list of features is modified.
     */
    public boolean add(final Feature feature) {
        return this.features.add(feature);
    }

    /**
     * Obtain the address.
     * 
     * @return An address <code>String</code>.
     */
    public String getAddress() {
        return vcard.getAddress();
    }

    /**
     * Obtain the city.
     * 
     * @return A city <code>String</code>.
     */
    public String getCity() {
        return vcard.getCity();
    }

    /**
     * Obtain the country.
     * 
     * @return A country <code>String</code>.
     */
    public String getCountry() {
        return vcard.getCountry();
    }

    /**
     * Obtain the features for the profile.
     * 
     * @return A <code>List</code> of <code>Feature</code>s.
     */
    public List<Feature> getFeatures() {
        return Collections.unmodifiableList(features);
    }

    /**
     * Obtain the industry.
     * 
     * @return An industry <code>String</code>.
     */
    public String getIndustry() {
        return vcard.getIndustry();
    }

    /**
     * Obtain the user's language preference.
     * 
     * @return A language <code>String</code>.
     */
    public String getLanguage() {
        return vcard.getLanguage();
    }

    /**
     * Obtain the locale.
     *
     * @return A <code>Locale</code>.
     */
    @XmlJavaTypeAdapter(LocaleXmlAdapter.class)
    public Locale getLocale() {
        return new Locale(getCountry(), getLanguage());
    }

    /**
     * Obtain the user's mobile phone number.
     * 
     * @return A mobile phone number <code>String</code>.
     */
    public String getMobilePhone() {
        return vcard.getMobilePhone();
    }

    /**
     * Obtain the organization country.
     * 
     * @return The organization country <code>String</code>.
     */
    public String getOrganizationCountry() {
        return vcard.getOrganizationCountry();
    }

    /**
     * Obtain phone.
     *
     * @return A phone number <code>String</code>.
     */
    public String getPhone() {
        return vcard.getPhone();
    }

    /**
     * Obtain the postal code.
     * 
     * @return A postal code <code>String</code>.
     */
    public String getPostalCode() {
        return vcard.getPostalCode();
    }

    /**
     * Obtain the province.
     * 
     * @return A province <code>String</code>.
     */
    public String getProvince() {
        return vcard.getProvince();
    }

    /**
     * Obtain timeZone.
     *
     * @return A TimeZone.
     */
    public TimeZone getTimeZone() {
        final TimeZone timeZone = TimeZone.getTimeZone(vcard.getTimeZone());
        return timeZone;
    }

    /**
     * Obtain vcard.
     *
     * @return A <code>ProfileVCard</code>.
     */
    public ProfileVCard getVCard() {
        return vcard;
    }

    /**
     * Determine if the profile is active.
     * 
     * @return A <code>Boolean</code>.
     */
    public Boolean isActive() {
        return active;
    }

    /**
     * Determine whether or not the user's industry is set.
     * 
     * @return True if industry is set; false otherwise.
     */
    public Boolean isSetIndustry() {
        return vcard.isSetIndustry();
    }

    /**
     * Determine whether or not the user's mobile phone number is set.
     * 
     * @return True if it is set; false otherwise.
     */
    public Boolean isSetMobilePhone() {
        return vcard.isSetMobilePhone();
    }

    /**
     * Remove a feature.
     * 
     * @param feature
     *            A <code>Feature</code>.
     * @return True if the list of features is modified.
     */
    public boolean remove(final Feature feature) {
        return this.features.remove(feature);
    }

    /**
     * Set active.
     *
     * @param active
     *		A <code>Boolean</code>.
     */
    public void setActive(final Boolean active) {
        this.active = active;
    }

    /**
     * Set address.
     * 
     * @param address
     *            The address <code>String</code>.
     */
    public void setAddress(final String address) {
        vcard.setAddress(address);
    }

    /**
     * Set city.
     * 
     * @param city
     *            The city <code>String</code>.
     */
    public void setCity(final String city) {
        vcard.setCity(city);
    }
   
    /**
     * Set the country.
     * 
     * @param country
     *            The country <code>String</code>.
     */
    public void setCountry(final String country) {
        vcard.setCountry(country);
    }

    /**
     * Set the features for the profile.
     * 
     * @param features
     *            A <code>List</code> of <code>Feature</code>s.
     */
    public void setFeatures(final List<Feature> features) {
        this.features.clear();
        this.features.addAll(features);
    }

    /**
     * Set the industry.
     * 
     * @param industry
     *            The industry <code>String</code>.
     */
    public void setIndustry(final String industry) {
        vcard.setIndustry(industry);
    }

    /**
     * Set locale.
     *
     * @param locale
     *		A Locale.
     */
    public void setLanguage(final String language) {
        vcard.setLanguage(language);
    }

    /**
     * Set the locale.
     * 
     * @param locale
     *            A <code>Locale</code>.
     */
    public void setLocale(final Locale locale) {
        setCountry(locale.getISO3Country());
        setLanguage(locale.getISO3Language());
    }

    /**
     * Set mobile phone.
     * 
     * @param mobilePhone
     *            A mobile phone number <code>String</code>.
     */
    public void setMobilePhone(final String mobilePhone) {
        vcard.setMobilePhone(mobilePhone);
    }

    /**
     * @see com.thinkparity.codebase.model.user.User#setName(java.lang.String)
     *
     */
    @Override
    public void setName(final String name) {
        super.setName(name);
        vcard.setName(getName());
    }

    /**
     * @see com.thinkparity.codebase.model.user.User#setName(java.lang.String, java.lang.String)
     *
     */
    @Override
    public void setName(final String first, final String last) {
        super.setName(first, last);
        vcard.setName(getName());
    }

    /**
     * @see com.thinkparity.codebase.model.user.User#setName(java.lang.String, java.lang.String, java.lang.String)
     *
     */
    @Override
    public void setName(final String first, final String middle,
            final String last) {
        super.setName(first, middle, last);
        vcard.setName(getName());
    }

    /**
     * @see com.thinkparity.codebase.model.user.User#setOrganization(java.lang.String)
     *
     */
    @Override
    public void setOrganization(final String organization) {
        super.setOrganization(organization);
        vcard.setOrganization(getOrganization());
    }

    /**
     * Set the organization country.
     * 
     * @param organizationCountry
     *            The organization country.
     */
    public void setOrganizationCountry(final String organizationCountry) {
        vcard.setOrganizationCountry(organizationCountry);
    }

    /**
     * Set the organization locale.
     * 
     * @param organizationLocale
     *            The organization <code>Locale</code>.
     */
    public void setOrganizationLocale(final Locale organizationLocale) {
        setOrganizationCountry(organizationLocale.getISO3Country());
    }

    /**
     * Set phone.
     *
     * @param phone
     *            A phone number <code>String</code>.
     */
    public void setPhone(final String phone) {
        vcard.setPhone(phone);
    }

    /**
     * Set postal code.
     *
     * @param postalCode
     *            A postal code <code>String</code>.
     */
    public void setPostalCode(final String postalCode) {
        vcard.setPostalCode(postalCode);
    }

    /**
     * Set province.
     *
     * @param province
     *            A province <code>String</code>.
     */
    public void setProvince(final String province) {
        vcard.setProvince(province);
    }

    /**
     * Set timeZone.
     *
     * @param timeZone
     *		A TimeZone.
     */
    public void setTimeZone(final TimeZone timeZone) {
        vcard.setTimeZone(timeZone.getID());
    }

    /**
     * @see com.thinkparity.codebase.model.user.User#setTitle(java.lang.String)
     *
     */
    @Override
    public void setTitle(final String title) {
        super.setTitle(title);
        vcard.setTitle(getTitle());
    }

    /**
     * Set vcard.
     *
     * @param vcard
     *		A <code>ProfileVCard</code>.
     */
    public void setVCard(final ProfileVCard vcard) {
        this.vcard = vcard;
    }
}
