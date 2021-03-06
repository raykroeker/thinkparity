/*
 * Created On: Jun 10, 2006 2:54:01 PM
 */
package com.thinkparity.codebase.model.user;

import java.text.MessageFormat;
import java.util.StringTokenizer;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.3
 */
public class UserNameTokenizer {

    /** The user's name. */
    private final String name;

    /**
     * Create UserNameTokenizer.
     * 
     * @param name
     *            A user's name.
     */
    public UserNameTokenizer(final String name) {
        super();
        this.name = name;
    }

    /**
     * Create UserNameTokenizer.
     * 
     * @param user
     *            A <code>User</code>.
     */
    public UserNameTokenizer(final User user) {
        this(user.getName());
    }

    /**
     * Create UserNameTokenizer.
     * 
     * @param vcard
     *            A <code>UserVCard</code>.
     */
    public UserNameTokenizer(final UserVCard vcard) {
        this(vcard.getName());
    }

    /**
     * Obtain the first name.
     * 
     * @return The first name.
     */
    public String getFamily() {
        if (isSetFamily()) {
            if (isSetMiddle()) {
                return getToken(2);
            } else {
                return getToken(1);
            }
        }
        else { return null; }
    }

    /**
     * Obtain the first name.
     * 
     * @return The first name.
     */
    public String getGiven() {
        if (isSetGiven()) {
            return getToken(0);
        } else {
            return null;
        }
    }

    /**
     * Obtain the given and family name within the given message format.
     * 
     * @param pattern
     *            A pattern <code>String</code>.
     * @return A formatted <code>String</code>.
     */
    public String getGivenAndFamily(final String pattern) {
        return MessageFormat.format(pattern, getGiven(), getFamily());
    }

    /**
     * Obtain the middle name.
     * 
     * @return The first name.
     */
    public String getMiddle() {
        if (isSetMiddle()) {
            return getToken(1);
        } else {
            return null;
        }
    }

    /**
     * Obtain the whole name.
     * 
     * @return The name <code>String</code>.
     */
    public String getName() {
        return name;
    }

    /**
     * Determine if the last name is set.
     * 
     * @return True if the last name is set.
     */
    public Boolean isSetFamily() {
        return hasToken(1) || hasToken(2);
    }

    /**
     * Determine if the middle name is set.
     * 
     * @return True if the middle name is set.
     */
    public Boolean isSetMiddle() {
        return hasToken(2);
    }

    /**
     * Create the string tokenizer for the name.
     * 
     * @return The string tokenizer.
     */
    private StringTokenizer createTokenizer() {
        return new StringTokenizer(name, User.NAME_SEP);
    }

    /**
     * Obtain an indexed token.
     * 
     * @param index
     *            The index to obtain.
     * @return The name token at position index.
     */
    private String getToken(final Integer index) {
        final StringTokenizer tokenizer = createTokenizer();
        for (int i = 0; i < index; i++) {
            tokenizer.nextToken();
        }
        return tokenizer.nextToken();
    }

    /**
     * Determine if a token exists at the given index.
     * 
     * @param index
     *            The index to query.
     * @return True if a token exists.
     */
    private Boolean hasToken(final Integer index) {
        return createTokenizer().countTokens() > index;
    }

    /**
     * Determine if the first name is set.
     * 
     * @return True if the first name is set.
     */
    private Boolean isSetGiven() { return hasToken(0); }
}
