/*
 * Created On:  10-May-07 3:19:36 PM
 */
package com.thinkparity.ophelia.model.container.delegate;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.model.container.ContainerDelegate;

/**
 * <b>Title:</b>thinkParity OpheliaModel Container Update Draft Comment
 * Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class UpdateDraftComment extends ContainerDelegate {

    /** The comment <code>String</code>. */
    private String comment;

    /** The container id <code>Long</code>. */
    private Long containerId;

    /**
     * Create UpdateDraftComment.
     *
     */
    public UpdateDraftComment() {
        super();
    }

    /**
     * Set comment.
     * 
     * @param comment
     *            A comment <code>String</code>.
     */
    public void setComment(final String comment) {
        this.comment = comment;
    }

    /**
     * Set container id.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void setContainerId(Long containerId) {
        this.containerId = containerId;
    }

    /**
     * Update the draft comment. We need to ensure that there exists a local
     * draft and that the comment meets the minimum constraints.
     * 
     */
    public void updateDraftComment() {
        Assert.assertTrue(doesExistLocalDraft(containerId),
                "Local draft for {0} does not exist.", containerId);
        getContainerConstraints().getDraftComment().validate(comment);

        containerIO.updateDraftComment(containerId, comment);
    }
}
