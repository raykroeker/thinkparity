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
public final class S3FilterConstraints {

    /** The singleton instance of <code>S3FilterConstraints</code>. */
    private static S3FilterConstraints INSTANCE;

    /**
     * Obtain an instance of filter constraints.
     * 
     * @return An instance of <code>S3FilterConstraints</code>.
     */
    public static S3FilterConstraints getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new S3FilterConstraints();
        }
        return INSTANCE;
    }

    /** The prefix <code>StringConstraint</code>. */
    private final StringConstraint prefix;

    /** The delimiter <code>StringConstraint</code>. */
    private final StringConstraint delimiter;

    /**
     * Create S3BucketConstraints.
     *
     */
    public S3FilterConstraints() {
        super();
        this.prefix = new StringConstraint() {
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
                        case '/':
                            break;
                        default:
                            invalidate(Reason.FORMAT, value);
                        }
                    }
                }
            }
        };
        this.prefix.setMaxLength(255);
        this.prefix.setMinLength(3);
        this.prefix.setName("Prefix");
        this.prefix.setNullable(Boolean.FALSE);

        this.delimiter = new StringConstraint() {
            @Override
            public void validate(final String value) {
                super.validate(value);

                if (null == value) {
                    return;
                } else {
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
                            case '/':
                                break;
                            default:
                                invalidate(Reason.FORMAT, value);
                            }
                        }
                    }
                }
            }
        };
        this.delimiter.setMaxLength(255);
        this.delimiter.setMinLength(3);
        this.delimiter.setName("Delimiter");
        this.delimiter.setNullable(Boolean.TRUE);
    }

    /**
     * Obtain prefix.
     *
     * @return A StringConstraint.
     */
    public StringConstraint getPrefix() {
        return prefix;
    }

    
    /**
     * Obtain delimiter.
     *
     * @return A StringConstraint.
     */
    public StringConstraint getDelimiter() {
        return delimiter;
    }
}
