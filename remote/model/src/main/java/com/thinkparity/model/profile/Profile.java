/*
 * Generated On: Jul 19 06 03:25:41 PM
 */
package com.thinkparity.model.profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkparity.server.model.user.User;

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

    /** The user's name. */
    private String name;

    /** The user's organization. */
    private String organization;

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

    /**
     * Obtain the name
     *
     * @return The String.
     */
    public String getName() { return name; }

    /**
     * Obtain the organization
     *
     * @return The String.
     */
    public String getOrganization() { return organization; }

    public boolean removeAllEmails(final List<String> emails) {
        return this.emails.removeAll(emails);
    }

    public boolean removeEmail(final String email) {
        return emails.remove(email);
    }

    /**
     * Set name.
     *
     * @param name The String.
     */
    public void setName(final String name) { this.name = name; }

    /**
     * Set organization.
     *
     * @param organization The String.
     */
    public void setOrganization(final String organization) {
        this.organization = organization;
    }
}
