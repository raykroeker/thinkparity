/*
 * Created On:  24-Oct-07 2:28:04 PM
 */
package com.thinkparity.desdemona.model.admin.report;

import java.util.Calendar;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b>thinkParity Desdemona Model Admin Report User<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReportUser extends User {

    /** An address. */
    private String address;

    /** A billable flag. */
    private Boolean billable;

    /** A city. */
    private String city;

    /** A country. */
    private String country;

    /** A created on date. */
    private Calendar createdOn;

    /** An e-mail. */
    private EMail email;

    /** A payment invoice date. */
    private Calendar invoiceDate;

    /** A language. */
    private String language;
    
    /** A mobile phone. */
    private String mobilePhone;

    /** A payment amount. */
    private Long paymentAmount;

    /** A payment date. */
    private Calendar paymentDate;

    /** A payment plan name. */
    private String paymentPlanName;

    /** A phone. */
    private String phone;

    /** A postal code. */
    private String postalCode;

    /** A province. */
    private String province;

    /** A time zone. */
    private String timeZone;

    /**
     * Create ReportUser.
     *
     */
    public ReportUser() {
        super();
    }

    /**
     * Obtain the address.
     *
     * @return A <code>String</code>.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Obtain the city.
     *
     * @return A <code>String</code>.
     */
    public String getCity() {
        return city;
    }

    /**
     * Obtain the country.
     *
     * @return A <code>String</code>.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Obtain the createdOn.
     *
     * @return A <code>Calendar</code>.
     */
    public Calendar getCreatedOn() {
        return createdOn;
    }

    /**
     * Obtain the email.
     *
     * @return A <code>EMail</code>.
     */
    public EMail getEmail() {
        return email;
    }

    /**
     * Obtain the paymentInvoiceDate.
     *
     * @return A <code>Calendar</code>.
     */
    public Calendar getInvoiceDate() {
        return invoiceDate;
    }

    /**
     * Obtain the language.
     *
     * @return A <code>String</code>.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Obtain the mobilePhone.
     *
     * @return A <code>String</code>.
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * Obtain the paymentAmount.
     *
     * @return A <code>String</code>.
     */
    public Long getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * Obtain the paymentDate.
     *
     * @return A <code>Calendar</code>.
     */
    public Calendar getPaymentDate() {
        return paymentDate;
    }

    /**
     * Obtain the paymentPlanName.
     *
     * @return A <code>String</code>.
     */
    public String getPaymentPlanName() {
        return paymentPlanName;
    }

    /**
     * Obtain the phone.
     *
     * @return A <code>String</code>.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Obtain the postalCode.
     *
     * @return A <code>String</code>.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Obtain the province.
     *
     * @return A <code>String</code>.
     */
    public String getProvince() {
        return province;
    }

    /**
     * Obtain the timeZone.
     *
     * @return A <code>String</code>.
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * Determine if the user is billable.
     * 
     * @return A <code>Boolean</code>.
     */
    public Boolean isBillable() {
        return billable;
    }

    /**
     * Set the address.
     *
     * @param address
     *		A <code>String</code>.
     */
    public void setAddress(final String address) {
        this.address = address;
    }

    /**
     * Set the billable.
     *
     * @param billable
     *		A <code>Boolean</code>.
     */
    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    /**
     * Set the city.
     *
     * @param city
     *		A <code>String</code>.
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * Set the country.
     *
     * @param country
     *		A <code>String</code>.
     */
    public void setCountry(final String country) {
        this.country = country;
    }

    /**
     * Set the createdOn.
     *
     * @param createdOn
     *		A <code>Calendar</code>.
     */
    public void setCreatedOn(final Calendar createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * Set the email.
     *
     * @param email
     *		A <code>EMail</code>.
     */
    public void setEmail(final EMail email) {
        this.email = email;
    }

    /**
     * Set the paymentInvoiceDate.
     *
     * @param paymentInvoiceDate
     *		A <code>Calendar</code>.
     */
    public void setInvoiceDate(final Calendar paymentInvoiceDate) {
        this.invoiceDate = paymentInvoiceDate;
    }

    /**
     * Set the language.
     *
     * @param language
     *		A <code>String</code>.
     */
    public void setLanguage(final String language) {
        this.language = language;
    }

    /**
     * Set the mobilePhone.
     *
     * @param mobilePhone
     *		A <code>String</code>.
     */
    public void setMobilePhone(final String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    /**
     * Set the paymentAmount.
     *
     * @param paymentAmount
     *		A <code>String</code>.
     */
    public void setPaymentAmount(final Long paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    /**
     * Set the paymentDate.
     *
     * @param paymentDate
     *		A <code>Calendar</code>.
     */
    public void setPaymentDate(final Calendar paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * Set the paymentPlanName.
     *
     * @param paymentPlanName
     *		A <code>String</code>.
     */
    public void setPaymentPlanName(final String paymentPlanName) {
        this.paymentPlanName = paymentPlanName;
    }

    /**
     * Set the phone.
     *
     * @param phone
     *		A <code>String</code>.
     */
    public void setPhone(final String phone) {
        this.phone = phone;
    }

    /**
     * Set the postalCode.
     *
     * @param postalCode
     *		A <code>String</code>.
     */
    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Set the province.
     *
     * @param province
     *		A <code>String</code>.
     */
    public void setProvince(final String province) {
        this.province = province;
    }

    /**
     * Set the timeZone.
     *
     * @param timeZone
     *		A <code>String</code>.
     */
    public void setTimeZone(final String timeZone) {
        this.timeZone = timeZone;
    }
}
