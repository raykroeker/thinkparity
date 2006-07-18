/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.model.parity.model.profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private final List<String> emails;

	/** Create Profile. */
	public Profile() {
        super();
        this.emails = new ArrayList<String>();
    }

    public boolean addAllEmails(final List<String> emails) {
        final int originalSize = this.emails.size();
        for(final String email : emails) { addEmail(email); }
        return originalSize < this.emails.size();
    }

    public boolean addEmail(final String email) {
        if(emails.contains(email)) { return false; }
        else { return emails.add(email); }
    }

    public void clearEmails() { emails.clear(); }

    /**
     * Obtain the emails
     *
     * @return The List<String>.
     */
    public List<String> getEmails() {
        return Collections.unmodifiableList(emails);
    }

    public boolean removeAllEmails(final List<String> emails) {
        return this.emails.removeAll(emails);
    }

    public boolean removeEmail(final String email) {
        return emails.remove(email);
    }
}
