/*
 * Created On: Jun 10, 2006 3:52:03 PM
 * $Id$
 */
package com.thinkparity.model.xmpp.user;


/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class UserNameBuilder {

    /** A user's first name. */
    private final String first;

    /** A user's middle name. */
    private final String last;

    /** A user's last name. */
    private final String middle;

    /** Create UserNameBuilder. */
    UserNameBuilder(final String first, final String middle,
            final String last) {
        super();
        if(null == first) { throw new NullPointerException(); }
        if(null == last) { throw new NullPointerException(); }

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
        if(null == first || null == last) { return null; }
        final StringBuffer name = new StringBuffer(first);
        if(null != middle) { name.append(User.NAME_SEP).append(middle); }
        name.append(User.NAME_SEP).append(last);
        return name.toString();
    }
}
