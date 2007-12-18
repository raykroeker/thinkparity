/*
 * Created On: Aug 27, 2006 9:49:05 AM
 */
package com.thinkparity.codebase.model.profile;

import com.thinkparity.codebase.codec.MD5Util;
import com.thinkparity.codebase.email.EMail;

/**
 * <b>Title:</b>thinkParity CommonModel Verification Key<br>
 * <b>Description:</b>A profile e-mail address verification key.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class VerificationKey {

    /** A verification key seed <code>Long</code>. */
    private static final Long SEED;

    static {
        SEED = Long.valueOf(System.currentTimeMillis());
    }

    /**
     * Create a verfication key.
     * 
     * @param key
     *            A key <code>String</code>.
     * @return A <code>VerificationKey</code>.
     */
    public static VerificationKey create(final String key) {
        final VerificationKey verificationKey = new VerificationKey();
        verificationKey.setKey(key);
        return verificationKey;
    }

    /**
     * Generate a verification code for an <code>EMail</code>.
     * 
     * @param email
     *            An <code>EMail</code>.
     * @return A <code>VerificationCode</code>.
     */
    public static VerificationKey generate(final EMail email) {
        final StringBuilder code = new StringBuilder(75);
        code.append(email.getDomain()).append("-")
            .append(SEED).append("-")
            .append(email.getUsername()).append("-")
            .append(System.currentTimeMillis());
        final VerificationKey key = new VerificationKey();
        key.setKey(MD5Util.md5Base64(code.toString()));
        return key;
    }

    /** The verification key <code>String</code>. */
    private String key;

    /**
     * Create VerificationKey.
     *
     */
    private VerificationKey() {
        super();
    }

    /**
     * Obtain the verificationCode
     *
     * @return The String.
     */
    public String getKey() {
        return key;
    }

    /**
     * Set verificationCode.
     *
     * @param verificationCode The String.
     */
    private void setKey(final String key) {
        this.key = key;
    }
}
