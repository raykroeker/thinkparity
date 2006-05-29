/*
 * Created On: May 28, 2006 5:55:10 PM
 * $Id$
 */
package com.thinkparity.migrator.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Checksum utility class.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ChecksumUtil {

    /**
     * Calculates the md5 digest and returns the value as a 16 element byte[].
     * 
     * @param data
     *            The byte data.
     * @return The byte checksum.
     */
    public static byte[] md5(final byte[] data) {
        return DigestUtils.md5(data);
    }

    /**
     * Calculates a md5 digest and return the value as a 32 character hex
     * string.
     * 
     * @param data
     *            The byte data.
     * @return The checksum.
     */
    public static String md5Hex(final byte[] data) {
        return DigestUtils.md5Hex(data);
    }

    /** Create ChecksumUtil */
    private ChecksumUtil() { super(); }
}
