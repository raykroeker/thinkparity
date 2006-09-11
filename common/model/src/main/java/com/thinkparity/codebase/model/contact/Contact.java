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

    /** Create Contact. */
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
     * Obtain the list of user e-mails.
     * 
     * @return An immutable list of user e-mails.
     */
    public List<EMail> getEmails() {
        return Collections.unmodifiableList(emails);
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
}
