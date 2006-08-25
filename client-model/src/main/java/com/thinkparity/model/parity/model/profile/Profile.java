/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.model.parity.model.profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.VCard;
import com.thinkparity.codebase.email.EMail;

import com.thinkparity.model.xmpp.user.User;

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

    /** A list of e-mail addresses. */
    private final List<EMail> emails;

    /** A vCard. */
    private VCard vCard;

	/** Create Profile. */
	public Profile() {
        super();
        this.emails = new ArrayList<EMail>();
    }

    /**
     * Add a list of e-mail addresses.
     * 
     * @param emails
     *            A list of e-mail addresses.
     * @return True if the list is modified false otherwise.
     */
    public boolean addAllEmails(final List<EMail> emails) {
        final int originalSize = this.emails.size();
        for (final EMail email : emails) {
            addEmail(email);
        }
        return originalSize < this.emails.size();
    }

    /**
     * Add an e-mail address.
     * 
     * @param email
     *            An e-mail address.
     * @return True if the list is modified false otherwise.
     */
    public boolean addEmail(final EMail email) {
        if (emails.contains(email)) {
            return false;
        } else {
            return emails.add(email);
        }
    }

    /**
     * Clear the list of e-mail addresses.
     *
     */
    public void clearEmails() {
        emails.clear();
    }

    /**
     * Obtain the emails
     *
     * @return The List<String>.
     */
    public List<EMail> getEmails() {
        return Collections.unmodifiableList(emails);
    }

    /**
     * Obtain the vCard
     *
     * @return The VCard.
     */
    public VCard getVCard() {
        return vCard;
    }

    /**
     * Remove a list of e-mail addresses.
     * 
     * @param emails
     *            A list of e-mail addresses.
     * @return True if the list is modified; false otherwise.
     */
    public boolean removeAllEmails(final List<EMail> emails) {
        return this.emails.removeAll(emails);
    }

    /**
     * Remove an e-mail address.
     * 
     * @param email
     *            An e-mail address.
     * @return True if the list is modified; false otherwise.
     */
    public boolean removeEmail(final EMail email) {
        return emails.remove(email);
    }

    /**
     * Set vCard.
     *
     * @param card The VCard.
     */
    public void setVCard(final VCard vCard) {
        this.vCard = vCard;
    }
}
