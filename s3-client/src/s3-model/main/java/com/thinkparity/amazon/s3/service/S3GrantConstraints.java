/*
 * Created On:  3-Apr-07 10:06:15 AM
 */
package com.thinkparity.amazon.s3.service;

import com.thinkparity.codebase.constraint.Constraint;
import com.thinkparity.codebase.constraint.EMailConstraint;
import com.thinkparity.codebase.constraint.ListConstraint;
import com.thinkparity.codebase.constraint.StringConstraint;
import com.thinkparity.codebase.constraint.IllegalValueException.Reason;

/**
 * <b>Title:</b>thinkParity Amazon S3 Grant Constraints<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3GrantConstraints {

    /** The singleton instance of <code>S3GrantConstraints</code>. */
    private static S3GrantConstraints INSTANCE;

    /**
     * Obtain an instance of amazon s3 authentication constraints.
     * 
     * @return An instance of <code>S3AuthenticationConstraints</code>.
     */
    public static S3GrantConstraints getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new S3GrantConstraints();
        }
        return INSTANCE;
    }

    /** The grant constraint. */
    private final Constraint<S3Grant> grant;

    /** The grantee constraint. */
    private final Constraint<S3Grantee> grantee;

    /** A grantee display name string constraint. */
    private final StringConstraint granteeDisplayName;

    /** The grantee e-mail constraint. */
    private final EMailConstraint granteeEMail;

    /** A grantee id string constraint. */
    private final StringConstraint granteeId;

    /** The grants constraint. */
    private final ListConstraint grants;

    /** The permission constraint. */
    private final Constraint<S3Grant.Permission> permission;

    /**
     * Create S3BucketConstraints.
     *
     */
    public S3GrantConstraints() {
        super();
        this.grants = new ListConstraint();
        this.grants.setMaxSize(100);
        this.grants.setMinSize(1);
        this.grants.setName("Grants");
        this.grants.setNullable(Boolean.FALSE);

        this.grant = new Constraint<S3Grant>() {
            @Override
            public void validate(final S3Grant value) {
                super.validate(value);
                if (null == value.getGrantee())
                    invalidate(Reason.NULL, value);
                if (null == value.getPermission())
                    invalidate(Reason.NULL, value);
            }
        };
        this.grant.setName("Grant.");
        this.grant.setNullable(Boolean.FALSE);

        this.grantee = new Constraint<S3Grantee>() {};
        this.grantee.setName("Grant grantee.");
        this.grantee.setNullable(Boolean.FALSE);

        this.granteeDisplayName = new StringConstraint();
        this.granteeDisplayName.setMaxLength(Integer.MAX_VALUE);
        this.granteeDisplayName.setMinLength(1);
        this.granteeDisplayName.setName("Grant grantee display name.");
        this.granteeDisplayName.setNullable(Boolean.FALSE);

        this.granteeEMail = new EMailConstraint();
        this.granteeEMail.setMaxLength(512);
        this.granteeEMail.setMinLength(1);
        this.granteeEMail.setName("Grantee e-mail.");
        this.granteeEMail.setNullable(Boolean.FALSE);

        this.granteeId = new StringConstraint();
        this.granteeId.setMaxLength(Integer.MAX_VALUE);
        this.granteeId.setMinLength(1);
        this.granteeId.setName("Grant grantee id.");
        this.granteeId.setNullable(Boolean.FALSE);

        this.permission = new Constraint<S3Grant.Permission>() {};
        this.permission.setName("Grant permission.");
        this.permission.setNullable(Boolean.FALSE);
    }

    /**
     * Obtain grant.
     *
     * @return A Constraint<S3Grant>.
     */
    public Constraint<S3Grant> getGrant() {
        return grant;
    }

    /**
     * Obtain grantee.
     *
     * @return A Constraint<S3Grantee>.
     */
    public Constraint<S3Grantee> getGrantee() {
        return grantee;
    }

    /**
     * Obtain granteeDisplayName.
     *
     * @return A StringConstraint.
     */
    public StringConstraint getGranteeDisplayName() {
        return granteeDisplayName;
    }

    /**
     * Obtain the grantee email constraint.
     * 
     * @return A <code>EMailConstraint</code>.
     */
    public EMailConstraint getGranteeEMail() {
        return granteeEMail;
    }

    /**
     * Obtain granteeId.
     *
     * @return A StringConstraint.
     */
    public StringConstraint getGranteeId() {
        return granteeId;
    }

    /**
     * Obtain grants.
     *
     * @return A ListConstraint.
     */
    public ListConstraint getGrants() {
        return grants;
    }

    /**
     * Obtain permission.
     *
     * @return A Constraint<S3Grant.Permission>.
     */
    public Constraint<S3Grant.Permission> getPermission() {
        return permission;
    }

}
