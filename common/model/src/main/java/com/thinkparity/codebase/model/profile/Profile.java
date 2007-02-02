/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.codebase.model.profile;

import com.thinkparity.codebase.model.user.User;

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

    /** The profile's vcard info. */
    private ProfileVCard vcard;

    /**
     * Create Profile.
     *
     */
	public Profile() {
        super();
    }

    /**
     * Obtain the address.
     * 
     * @return An address <code>String</code>.
     */
    public String getAddress() {
        // TODO Get address
        return "";
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
     * Obtain the user's mobile phone number.
     * 
     * @return A mobile phone number <code>String</code>.
     */
    public String getMobilePhone() {
        return vcard.getMobilePhone();
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
        // TODO Get postal code
        return "";
    }

    /**
     * Obtain the province.
     * 
     * @return A province <code>String</code>.
     */
    public String getProvince() {
        // TODO Get province
        return "";
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
     * Determine whether or not the user's mobile phone number is set.
     * 
     * @return True if it is set; false otherwise.
     */
    public Boolean isSetMobilePhone() {
        return vcard.isSetMobilePhone();
    }

    /**
     * Set address.
     * 
     * @param address
     *            The address <code>String</code>.
     */
    public void setAddress(final String address) {
        // TODO Set address
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
        // TODO Set postal code
    }

    /**
     * Set province.
     *
     * @param province
     *            A province <code>String</code>.
     */
    public void setProvince(final String province) {
        // TODO Set province
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
