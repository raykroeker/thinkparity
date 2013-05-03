/*
 * Created On:  3-Apr-07 10:06:15 AM
 */
package com.thinkparity.amazon.s3.service.object;

import com.thinkparity.codebase.constraint.Constraint;
import com.thinkparity.codebase.constraint.LongConstraint;
import com.thinkparity.codebase.constraint.StringConstraint;

/**
 * <b>Title:</b>thinkParity Amazon S3 Object Constraints<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3ObjectConstraints {

    /** The singleton instance of <code>S3ObjectConstraints</code>. */
    private static S3ObjectConstraints INSTANCE;

    /**
     * Obtain an instance of profile constraints.
     * 
     * @return An instance of <code>ProfileConstraints</code>.
     */
    public static S3ObjectConstraints getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new S3ObjectConstraints();
        }
        return INSTANCE;
    }

    /** The checksum constraint. */
    private final StringConstraint checksum;

    /** The key <code>Constraint<S3Key></code>. */
    private final Constraint<S3Key> key;

    /** The size long constraint. */
    private final LongConstraint size;

    /** A type constraint. */
    private final Constraint<S3ObjectContentType> type;

    /**
     * Create S3ObjectConstraints.
     *
     */
    public S3ObjectConstraints() {
        super();
        this.checksum = new StringConstraint();
        this.checksum.setMaxLength(Integer.MAX_VALUE);
        this.checksum.setMinLength(1);
        this.checksum.setNullable(Boolean.FALSE);

        final StringConstraint keyResource = new StringConstraint();
        keyResource.setMaxLength(512);
        keyResource.setMinLength(1);
        keyResource.setName("Key resource.");
        keyResource.setNullable(Boolean.FALSE);

        this.key = new Constraint<S3Key> () {
            @Override
            public void validate(final S3Key value) {
                super.validate(value);

                keyResource.validate(value.getResource());
            }
        };
        this.key.setName("Key");
        this.key.setNullable(Boolean.FALSE);

        this.size = new LongConstraint();
        this.size.setMaxValue(Long.MAX_VALUE);
        this.size.setMinValue(1L);
        this.size.setName("Size");
        this.size.setNullable(Boolean.FALSE);

        this.type = new Constraint<S3ObjectContentType>() {};
        this.type.setName("Type");
        this.type.setNullable(Boolean.FALSE);
    }

    /**
     * Obtain checksum.
     *
     * @return A StringConstraint.
     */
    public StringConstraint getChecksum() {
        return checksum;
    }

    /**
     * Obtain key.
     *
     * @return A Constraint<S3Key>.
     */
    public Constraint<S3Key> getKey() {
        return key;
    }

    /**
     * Obtain size.
     *
     * @return A LongConstraint.
     */
    public LongConstraint getSize() {
        return size;
    }

    /**
     * Obtain type.
     *
     * @return A Constraint<S3ObjectContentType>.
     */
    public Constraint<S3ObjectContentType> getType() {
        return type;
    }
}
