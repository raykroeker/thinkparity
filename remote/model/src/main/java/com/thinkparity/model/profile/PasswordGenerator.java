/*
 * Created On: Aug 29, 2006 9:28:33 AM
 */
package com.thinkparity.model.profile;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class PasswordGenerator {

    /** A singleton instance. */
    private static final PasswordGenerator SINGLETON;

    static {
        SINGLETON = new PasswordGenerator();
    }

    /**
     * Generate a password with the given complexity.
     * 
     * @return A password <code>String</code>.
     */
    public static String generate() throws NoSuchAlgorithmException {
        return SINGLETON.doGenerate();
    }

    /** The algorithm to use when creating the random number generator. */
    private final String algorithm;

    /** The length of the password to generate. */
    private final int length;

    /** A list of characters from which to pull. */
    private final char[] selectFrom;

    /** Create PasswordGenerator. */
    private PasswordGenerator() {
        super();
        this.algorithm = "SHA1PRNG";
        this.length = 7;
        this.selectFrom = new StringBuffer()
            .append("abcdefghijklmnopqrstuvwxyz")
            .append("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
            .append("0123456789")
            .toString().toCharArray();
    }

    /**
     * Generate a password with the given complexity.
     * 
     * @param complexity
     *            The password <code>Complexity</code>.
     * @return A password <code>String</code>.
     */
    private String doGenerate() throws NoSuchAlgorithmException {
        final Random random = SecureRandom.getInstance(algorithm);
        final StringBuffer password = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            password.append(selectFrom[random.nextInt(selectFrom.length)]);
        }
        return password.toString();
    }
}
