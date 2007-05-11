/*
 * Created On:  10-May-07 2:46:21 PM
 */
package com.thinkparity.codebase.model.container;

import com.thinkparity.codebase.constraint.StringConstraint;

/**
 * <b>Title:</b>thinkParity CommonModel Container Constraints<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContainerConstraints {

    /** A singleton instance of <code>ContainerConstraints</code>. */
    private static ContainerConstraints INSTANCE;

    /**
     * Obtain an instance of container constraints.
     * 
     * @return An instance of <code>ContainerConstraints</code>.
     */
    public static ContainerConstraints getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new ContainerConstraints();
        }
        return INSTANCE;
    }

    /** A draft comment <code>StringConstraint</code>. */
    private final StringConstraint draftComment;

    /** A version name <code>StringConstraint</code>. */
    private final StringConstraint versionName;

    /**
     * Create ContainerConstraints.
     *
     */
    private ContainerConstraints() {
        super();
        this.draftComment = new StringConstraint();
        this.draftComment.setMaxLength(4096);
        this.draftComment.setMinLength(1);
        this.draftComment.setName("Draft comment");
        this.draftComment.setNullable(Boolean.TRUE);

        this.versionName = new StringConstraint();
        this.versionName.setMaxLength(64);
        this.versionName.setMinLength(1);
        this.versionName.setName("Version name");
        this.versionName.setNullable(Boolean.TRUE);
    }

    /**
     * Obtain draft comment.
     * 
     * @return A <code>StringConstraint</code>.
     */
    public StringConstraint getDraftComment() {
        return draftComment;
    }

    /**
     * Obtain version name.
     * 
     * @return A <code>StringConstraint</code>.
     */
    public StringConstraint getVersionName() {
        return versionName;
    }
}
