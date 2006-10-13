/*
 * Created On: Aug 14, 2006 12:20:48 PM
 */
package com.thinkparity.ophelia.model.util.filter.contact;

import org.apache.log4j.Logger;

import com.thinkparity.ophelia.model.contact.ContactInvitation;
import com.thinkparity.ophelia.model.util.filter.AbstractFilter;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class DefaultInvitationFilter extends AbstractFilter<ContactInvitation> {

    /** Create DefaultInvitationFilter. */
    public DefaultInvitationFilter() { super(); }

    /**
     * @see com.thinkparity.ophelia.model.util.filter.Filter#logVariable(String, org.apache.log4j.Logger)
     */
    public void debug(final Logger logger) {}

    /**
     * @see com.thinkparity.ophelia.model.util.filter.Filter#doFilter(java.lang.Object)
     */
    public Boolean doFilter(final ContactInvitation o) {
        return Boolean.FALSE;
    }
}
