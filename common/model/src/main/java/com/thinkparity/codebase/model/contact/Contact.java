/*
 * Mar 1, 2006
 */
package com.thinkparity.codebase.model.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.user.User;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Contact extends User {

    /** The contact's e-mails. */
    private final List<EMail> emails;

    /** The contact vcard info. */
    private ContactVCard vcard;

    /**
     * Create Contact.
     *
     */
	public Contact() {
        super();
        this.emails = new ArrayList<EMail>();
	}

    /**
     * Add all e-mails.
     * 
     * @param emails
     *            A list of e-mails
     * @return True if the list is modified.
     */
    public boolean addAllEmails(final List<EMail> emails) {
        final int originalSize = this.emails.size();
        for(int i = 0; i < emails.size(); i++) {
            if(!this.emails.contains(emails.get(i))) {
                this.emails.add(emails.get(i));
            }
        }
        return originalSize < this.emails.size();
    }

    /**
     * Add an e-mail.
     * 
     * @param email
     *            An e-mail.
     * @return True if the list is modified.
     */
    public boolean addEmail(final EMail email) {
        if(emails.contains(email)) { return false; }
        return emails.add(email);
    }

    /**
     * Clear the list of e-mails.
     * 
     */
    public void clearEmails() { emails.clear(); }

    /**
     * Obtain the contact's address.
     * 
     * @return An address <code>String</code>.
     */
    public String getAddress() {
        return vcard.getAddress();
    }

    /**
     * Obtain the contact's city.
     * 
     * @return A city <code>String</code>.
     */
    public String getCity() {
        return vcard.getCity();
    }

    /**
     * Obtain the contact's country.
     * 
     * @return A country <code>String</code>.
     */
    public String getCountry() {
        return vcard.getCountry();
    }

    /**
     * Obtain the list of user e-mails.
     * 
     * @return An immutable list of user e-mails.
     */
    public List<EMail> getEmails() {
        return Collections.unmodifiableList(emails);
    }

    /**
     * Obtain the user e-mails size.
     * 
     * @return The number of user <code>EMail</code> addresses.
     */
    public int getEmailsSize() {
        return emails.size();
    }

    /**
     * Obtain the mobile phone number.
     * 
     * @return A mobile phone <code>String</code>.
     */
    public String getMobilePhone() {
        return vcard.getMobilePhone();
    }

    /**
     * Obtain the phone number.
     * 
     * @return A phone number <code>String</code>.
     */
    public String getPhone() {
        return vcard.getPhone();
    }

    /**
     * Obtain the contact's postal code.
     * 
     * @return A postal code <code>String</code>.
     */
    public String getPostalCode() {
        return vcard.getPostalCode();
    }

    /**
     * Obtain the contact's province.
     * 
     * @return A province <code>String</code>.
     */
    public String getProvince() {
        return vcard.getProvince();
    }

    /**
     * Obtain vcard.
     *
     * @return A ContactVCard.
     */
    public ContactVCard getVCard() {
        return vcard;
    }

    /**
     * Determine if the moblie phone number is set.
     * 
     * @return True if the phone number is set.
     */
    public Boolean isSetMobilePhone() {
        return vcard.isSetMobilePhone();
    }

    /**
     * Determine if the phone number is set.
     * 
     * @return True if the phone number is set.
     */
    public Boolean isSetPhone() {
        return vcard.isSetPhone();
    }

    /**
     * Remove all e-mails.
     * 
     * @param emails
     *            A list of e-mails
     * @return True if the list is modified.
     */
    public boolean removeAllEmails(final List<EMail> emails) {
        return this.emails.removeAll(emails);
    }

    /**
     * Remove an e-mail.
     * 
     * @param email
     *            An e-mail.
     * @return True if the list is modified.
     */
    public boolean removeEmail(final EMail email) {
        return emails.remove(email);
    }

    /**
     * Set the mobile phone.
     * 
     * @param mobilePhone
     *            A mobile phone <code>String</code>.
     */
    public void setMobilePhone(final String mobilePhone) {
        vcard.setMobilePhone(mobilePhone);
    }

    /**
     * Set the phone.
     * 
     * @param phone
     *            A phone <code>String</code>.
     */
    public void setPhone(final String phone) {
        vcard.setPhone(phone);
    }

    /**
     * Set vcard.
     *
     * @param vcard
     *		A ContactVCard.
     */
    public void setVCard(final ContactVCard vcard) {
        this.vcard = vcard;
    }
}
