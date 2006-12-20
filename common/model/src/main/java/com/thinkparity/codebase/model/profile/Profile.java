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

    public String getCity() {
        return vcard.getCity();
    }

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
     * @return A String.
     */
    public String getPhone() {
        return vcard.getPhone();
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
     *		A String.
     */
    public void setPhone(final String phone) {
        vcard.setPhone(phone);
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
