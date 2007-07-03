/*
 * Created On:  21-Jun-07 7:30:40 PM
 */
package com.thinkparity.amazon.s3.service;

import com.thinkparity.codebase.email.EMail;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3EMailGrantee extends S3Grantee {

    /** An e-mail address. */
    private EMail email;

    /**
     * Create S3EMailGrantee.
     *
     */
    public S3EMailGrantee() {
        super();
    }

    /**
     * Obtain email.
     *
     * @return A EMail.
     */
    public EMail getEMail() {
        return email;
    }

    /**
     * @see com.thinkparity.amazon.s3.service.S3Grantee#getType()
     *
     */
    @Override
    public Type getType() {
        return Type.EMAIL;
    }

    /**
     * Set email.
     *
     * @param email
     *		A EMail.
     */
    public void setEMail(final EMail email) {
        this.email = email;
    }
}
