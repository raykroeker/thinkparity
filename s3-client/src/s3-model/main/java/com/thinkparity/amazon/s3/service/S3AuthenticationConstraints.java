/*
 * Created On:  3-Apr-07 10:06:15 AM
 */
package com.thinkparity.amazon.s3.service;

import com.thinkparity.codebase.constraint.Constraint;
import com.thinkparity.codebase.constraint.StringConstraint;
import com.thinkparity.codebase.constraint.IllegalValueException.Reason;

/**
 * <b>Title:</b>thinkParity Amazon S3 Bucket Constraints<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3AuthenticationConstraints {

    /** The singleton instance of <code>S3AuthenticationConstraints</code>. */
    private static S3AuthenticationConstraints INSTANCE;

    /**
     * Obtain an instance of amazon s3 authentication constraints.
     * 
     * @return An instance of <code>S3AuthenticationConstraints</code>.
     */
    public static S3AuthenticationConstraints getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new S3AuthenticationConstraints();
        }
        return INSTANCE;
    }

    /** The access key id <code>StringConstraint</code>. */
    private final StringConstraint accessKeyId;

    /** The secret access key <code>StringConstraint</code>. */
    private final Constraint<byte[]> secretAccessKey;

    /**
     * Create S3BucketConstraints.
     *
     */
    public S3AuthenticationConstraints() {
        super();
        this.accessKeyId = new StringConstraint();
        this.accessKeyId.setMaxLength(20);
        this.accessKeyId.setMinLength(20);
        this.accessKeyId.setName("Access key id");
        this.accessKeyId.setNullable(Boolean.FALSE);

        this.secretAccessKey= new Constraint<byte[]>() {
            @Override
            public void validate(final byte[] value) {
                super.validate(value);
                if (40 < value.length)
                    invalidate(Reason.TOO_LONG, value);
                if (40 > value.length)
                    invalidate(Reason.TOO_SHORT, value);
            }
        };
        this.secretAccessKey.setName("Secret access key.");
        this.secretAccessKey.setNullable(Boolean.FALSE);
    }

    /**
     * Obtain the access key id.
     * 
     * @return A <code>StringConstraint</code>.
     */
    public StringConstraint getAccessKeyId() {
        return accessKeyId;
    }

    /**
     * Obtain the secret access key.
     * 
     * @return A <code>Constraint<byte[]></code>.
     */
    public Constraint<byte[]> getSecretAccessKey() {
        return secretAccessKey;
    }
}
