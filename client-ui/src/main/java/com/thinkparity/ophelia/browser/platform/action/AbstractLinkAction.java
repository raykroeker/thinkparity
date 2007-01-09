/**
 * Created On: Jan 7, 2007 2:13:28 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action;


/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public abstract class AbstractLinkAction implements LinkAction {

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (null == obj)
            return false;
        if (this == obj)
            return true;
        if (obj instanceof AbstractLinkAction) {
            return ((AbstractLinkAction) obj).getActionName().equals(getActionName());
        } else {
            return false;
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getActionName().hashCode();
    }
}
