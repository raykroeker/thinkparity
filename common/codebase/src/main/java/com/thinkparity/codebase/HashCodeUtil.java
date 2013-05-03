/*
 * Created On:  6-Apr-07 2:32:46 PM
 */
package com.thinkparity.codebase;

/**
 * <b>Title:</b>thinkParity Common HashCode Util<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HashCodeUtil {

    /**
     * Generate a hashcode for a series of variables.
     * 
     * @param variables
     *            An <code>Object[]</code> array of variables.
     * @return A hashcode <code>int</code>.
     */
    public static int hashCode(final Object... variables) {
        final int PRIME = 31;
        int result = 1;
        for (final Object variable : variables)
            if (null != variable)
                result = PRIME * result * variable.hashCode();
        return result;
    }

    /**
     * Create HashCodeUtil.
     *
     */
    private HashCodeUtil() {
        super();
    }
}
