/*
 * Created On: Jun 10, 2006 3:52:03 PM
 */
package com.thinkparity.codebase.model.user;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.4
 */
public class UserNameBuilder {

    /** A user's first name. */
    private final String first;

    /** A user's middle name. */
    private final String last;

    /** A user's last name. */
    private final String middle;

    /**
     * Create UserNameBuilder.
     * 
     * @param first
     *            The first name.
     * @param last
     *            The last name.
     */
    public UserNameBuilder(final String first, final String last) {
        this(first, null, last);
    }

    /** Create UserNameBuilder. */
    public UserNameBuilder(final String first, final String middle,
            final String last) {
        super();
        this.first = first;
        this.middle = middle;
        this.last = last;
    }

    /**
     * Obtain a user's name.
     * 
     * @return A user's name.
     */
    public String getName() {
        if(null == first && null == middle && null == last) { return null; }
        final StringBuffer name = new StringBuffer();
        if(null != first) { name.append(first); }
        if(null != middle) {
            if(0 < name.length()) { name.append(User.NAME_SEP); }
            name.append(middle);
        }
        if(null != last) {
            if(0 < name.length()) { name.append(User.NAME_SEP); }
            name.append(last);
        }
        return name.toString();
    }
}
