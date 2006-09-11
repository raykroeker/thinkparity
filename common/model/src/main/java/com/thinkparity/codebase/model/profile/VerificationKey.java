/*
 * Created On: Aug 27, 2006 9:49:05 AM
 */
package com.thinkparity.codebase.model.profile;

import com.thinkparity.codebase.email.EMail;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class VerificationKey {

    /**
     * Generate a verification code for an <code>EMail</code>.
     * 
     * @param email
     *            An <code>EMail</code>.
     * @return A <code>VerificationCode</code>.
     */
    public static VerificationKey generate(final EMail email) {
        final VerificationKey verificationCode = new VerificationKey();
        verificationCode.setKey(email.toString());
        return verificationCode;
    }

    /** The verification key <code>String</code>. */
    private String key;

    /** Create VerificationCode. */
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
