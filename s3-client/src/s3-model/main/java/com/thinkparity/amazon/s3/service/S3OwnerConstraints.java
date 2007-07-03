/*
 * Created On:  3-Apr-07 10:06:15 AM
 */
package com.thinkparity.amazon.s3.service;

import com.thinkparity.codebase.constraint.StringConstraint;

/**
 * <b>Title:</b>thinkParity Amazon S3 Owner Constraints<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3OwnerConstraints {

    /** The singleton instance of <code>S3OwnerConstraints</code>. */
    private static S3OwnerConstraints INSTANCE;

    /**
     * Obtain an instance of amazon s3 owner constraints.
     * 
     * @return An instance of <code>S3OwnerConstraints</code>.
     */
    public static S3OwnerConstraints getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new S3OwnerConstraints();
        }
        return INSTANCE;
    }

    /** A display name string constraint. */
    private final StringConstraint displayName;

    /** A id string constraint. */
    private final StringConstraint id;

    /**
     * Create S3OwnerConstraints.
     *
     */
    public S3OwnerConstraints() {
        super();
        this.displayName = new StringConstraint();
        this.displayName.setMaxLength(Integer.MAX_VALUE);
        this.displayName.setMinLength(1);
        this.displayName.setName("Display name");
        this.displayName.setNullable(Boolean.FALSE);

        this.id = new StringConstraint();
        this.id.setMaxLength(Integer.MAX_VALUE);
        this.id.setMinLength(1);
        this.id.setName("Id");
        this.id.setNullable(Boolean.FALSE);
    }

    /**
     * Obtain displayName.
     *
     * @return A StringConstraint.
     */
    public StringConstraint getDisplayName() {
        return displayName;
    }

    /**
     * Obtain id.
     *
     * @return A StringConstraint.
     */
    public StringConstraint getId() {
        return id;
    }
}
