/*
 * Created On:  Sunday December 17, 2006 14:53
 */
package com.thinkparity.codebase.model.user;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <b>Title:</b>thinkParity User VCard<br>
 * <b>Description:</b>An abstraction of a user object's interface into their
 * virtual card.<br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@XStreamAlias("v-card")
public abstract class UserVCard {
    
    private String city;

    private String country;

    private String mobilePhone;

    private String name;

    private String organization;

    private String phone;

    private String title;

    /**
     * Create UserVCard.
     *
     */
    protected UserVCard() {
        super();
    }

    /**
     * Obtain the city.
     * 
     * @return The city <code>String</code>.
     */
    public String getCity() {
        return city;
    }

    /**
     * Obtain the country.
     * 
     * @return The country <code>String</code>.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Obtain the mobile phone.
     * 
     * @return The mobile phone <code>String</code>.
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * Obtain name.
     *
     * @return A String.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtain the organization.
     * 
     * @return The organization <code>String</code>.
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * Obtain the phone number.
     * 
     * @return The phone number <code>String</code>.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Obtain the title.
     * 
     * @return The title <code>String</code>.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Determine if the city is set.
     * 
     * @return True if the city is set.
     */
    public Boolean isSetCity() {
        return isSet(city);
    }

    /**
     * Determine if the country is set.
     * 
     * @return True if the country is set.
     */
    public Boolean isSetCountry() {
        return isSet(country);
    }

    /**
     * Determine if the mobile phone is set.
     * 
     * @return True if the mobile phone is set.
     */
    public Boolean isSetMobilePhone() {
        return isSet(mobilePhone);
    }

    /**
     * Determine if the organization is set.
     * 
     * @return True if the organization is set.
     */    
    public Boolean isSetOrganization() {
        return isSet(organization);
    }

    /**
     * Determine if the mobile phone is set.
     * 
     * @return True if the mobile phone is set.
     */
    public Boolean isSetPhone() {
        return isSet(phone);
    }

    /**
     * Determine if the title is set.
     * 
     * @return True if the title is set.
     */    
    public Boolean isSetTitle() {
        return isSet(title);
    }

    /**
     * Set city.
     *
     * @param city
     *		A String.
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * Set country.
     *
     * @param country
     *		A String.
     */
    public void setCountry(final String country) {
        this.country = country;
    }

    /**
     * Set mobilePhone.
     *
     * @param mobilePhone
     *		A String.
     */
    public void setMobilePhone(final String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    /**
     * Set the vcard name.
     * 
     * @param given
     *            The given name <code>String</code>.
     * @param middle
     *            The middle name <code>String</code>.
     * @param family
     *            The family name <code>String</code>.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Set organization.
     *
     * @param organization
     *		A String.
     */
    public void setOrganization(final String organization) {
        this.organization = organization;
    }

    /**
     * Set phone.
     *
     * @param phone
     *		A String.
     */
    public void setPhone(final String phone) {
        this.phone = phone;
    }

    /**
     * Set title.
     *
     * @param title
     *		A String.
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Determine if a field value is set.
     * 
     * @param fieldValue
     *            The field value <code>Object</code>.
     * @return True if it is set.
     */
    private Boolean isSet(final Object field) {
        return null != field;
    }
}
