/*
 * Created On:  13-Feb-07 9:17:52 AM
 */
package com.thinkparity.ophelia.model.document;


/**
 * <b>Title:</b>thinkParity Document Lock<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DocumentVersionLock extends DocumentLock {

    /** A document version id <code>Long</code>. */
    private Long versionId;

    /**
     * Create DocumentLock.
     *
     */
    public DocumentVersionLock() {
        super();
    }

    /**
     * Obtain versionId.
     *
     * @return A Long.
     */
    public Long getVersionId() {
        return versionId;
    }

    /**
     * Set versionId.
     *
     * @param versionId
     *		A Long.
     */
    public void setVersionId(final Long versionId) {
        this.versionId = versionId;
    }
}
