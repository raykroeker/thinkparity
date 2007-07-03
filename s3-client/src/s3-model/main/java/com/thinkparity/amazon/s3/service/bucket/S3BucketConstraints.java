/*
 * Created On:  3-Apr-07 10:06:15 AM
 */
package com.thinkparity.amazon.s3.service.bucket;

import com.thinkparity.codebase.constraint.StringConstraint;
import com.thinkparity.codebase.constraint.IllegalValueException.Reason;

/**
 * <b>Title:</b>thinkParity Amazon S3 Bucket Constraints<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3BucketConstraints {

    /** The singleton instance of <code>S3BucketConstraints</code>. */
    private static S3BucketConstraints INSTANCE;

    /**
     * Obtain an instance of profile constraints.
     * 
     * @return An instance of <code>ProfileConstraints</code>.
     */
    public static S3BucketConstraints getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new S3BucketConstraints();
        }
        return INSTANCE;
    }

    /** The bucket name <code>StringConstraint</code>. */
    private final StringConstraint name;

    /**
     * Create S3BucketConstraints.
     *
     */
    public S3BucketConstraints() {
        super();
        this.name = new StringConstraint() {
            @Override
            public void validate(final String value) {
                super.validate(value);
                // ensure the value is alpha-numeric or an "_", "." or "-"
                final char[] chars = value.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    if (Character.isLetterOrDigit(chars[i])) {
                        continue;
                    } else {
                        switch (chars[i]) {
                        case '_':
                        case '.':
                        case '-':
                            break;
                        default:
                            invalidate(Reason.FORMAT, value);
                        }
                    }
                }
            }
        };
        this.name.setMaxLength(255);
        this.name.setMinLength(3);
        this.name.setName("Name");
        this.name.setNullable(Boolean.FALSE);
    }

    /**
     * Obtain name.
     *
     * @return A StringConstraint.
     */
    public StringConstraint getName() {
        return name;
    }
}
