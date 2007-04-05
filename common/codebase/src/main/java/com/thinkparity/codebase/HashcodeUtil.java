/*
 * Created On:  4-Apr-07 1:33:26 PM
 */
package com.thinkparity.codebase;

/**
 * <b>Title:</b>thinkParity Common Hashcode Util<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HashcodeUtil {

    /**
     * Generate a hashcode for a series of variables.
     * 
     * @param variables
     *            An <code>Object[]</code> array of variables.
     * @return A hashcode <code>int</code>.
     */
    public static int hashcode(final Object... variables) {
        final int PRIME = 31;
        int result = 1;
        for (final Object variable : variables)
            if (null != variable)
                result = PRIME * result * variable.hashCode();
        return result;
    }

    /**
     * Create HashcodeUtil.
     *
     */
    private HashcodeUtil() {
        super();
    }
}
