/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.model.parity.model.profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkparity.model.xmpp.JabberId;

/**
 * <b>Title:</b>thinkParity Profile<br>
 * <b>Description:</b>
 * 
 * @author CreateModel.groovy
 * @version 
 */
public class Profile {

    /** A list of e-mails for the user. */
    private final List<String> emails;

    /** A jabber id. */
    private JabberId jabberId;

	/** Create Profile. */
	public Profile() {
        super();
        this.emails = new ArrayList<String>();
    }

    /**
     * Add all e-mails.
     * 
     * @param emails
     *            A list of e-mail addresses.
     * @return True if the list of e-mail addresses is modified; false
     *         otherwise.
     */
    public boolean addAllEmails(final List<String> emails) {
        final int originalSize = this.emails.size();
        for(int i = 0; i < emails.size(); i++) {
            if(!this.emails.contains(emails.get(i))) {
                this.emails.add(emails.get(i));
            }
        }
        return originalSize < this.emails.size();
    }

    /**
     * Add an e-mail address.
     * 
     * @param email
     *            An e-mail address.
     * @return True if the list of e-mail addresses is modified; false
     *         otherwise.
     */
    public boolean addEmail(final String email) {
        if(emails.contains(email)) { return false; }
        return emails.add(email);
    }

    /**
     * Obtain the emails
     *
     * @return The List<String>.
     */
    public List<String> getEmails() {
        return Collections.unmodifiableList(emails);
    }

    /**
     * Obtain the jabberId
     *
     * @return The JabberId.
     */
    public JabberId getJabberId() { return jabberId; }

    /**
     * Remove all e-mails.
     * 
     * @param emails
     *            A list of e-mail addresses.
     * @return True if the list is modified; false otherwise.
     */
    public boolean removeAllEmails(final List<String> emails) {
        return this.emails.removeAll(emails);
    }

    /**
     * Remove an e-mail.
     * 
     * @param email
     *            An e-mail address.
     * @return True if the list of e-mail addresses is modified; false
     *         otherwise.
     */
    public boolean removeEmail(final String email) {
        return emails.remove(email);
    }

    /**
     * Set jabberId.
     *
     * @param jabberId The JabberId.
     */
    public void setJabberId(final JabberId jabberId) {
        this.jabberId = jabberId;
    }
}
