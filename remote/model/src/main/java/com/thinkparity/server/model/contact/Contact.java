/*
 * Feb 28, 2006
 */
package com.thinkparity.server.model.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkparity.server.model.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Contact extends User {

    /** A list of e-mail addresses. */
    private final List<String> emails;

    /** The user's name. */
    private String name;

    /** The user's organization. */
    private String organization;

    /** Create Contact. */
    public Contact() {
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
