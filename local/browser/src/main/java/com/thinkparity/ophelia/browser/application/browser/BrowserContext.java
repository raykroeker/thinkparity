/*
 * Created On:  6-Dec-06 8:37:27 AM
 */
package com.thinkparity.ophelia.browser.application.browser;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class BrowserContext {

    /** A context <code>Object</code>. */
    private final Object context;

    /**
     * Create BrowserContext.
     *
     */
    BrowserContext(final Object context) {
        super();
        this.context = context;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final BrowserContext other = (BrowserContext) obj;
        if (context == null) {
            if (other.context != null)
                return false;
        } else if (!context.equals(other.context))
            return false;
        return true;
    }

    /**
     * @see java.lang.Object#hashCode()
     *
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + (context == null ? 0 : context.hashCode());
        return result;
    }

    
}
