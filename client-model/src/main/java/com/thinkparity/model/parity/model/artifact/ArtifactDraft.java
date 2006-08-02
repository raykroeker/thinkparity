/*
 * Created On: Aug 1, 2006 8:19:47 AM
 */
package com.thinkparity.model.parity.model.artifact;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class ArtifactDraft {

    /** The artifact id. */
    private Long id;

    /** The artifact version id. */
    private Long versionId;

    /** Create an ArtifactDraft. */
    public ArtifactDraft() { super(); }

    /**
     * Obtain the id
     *
     * @return The Long.
     */
    public Long getId() { return id; }

    /**
     * Obtain the versionId
     *
     * @return The Long.
     */
    public Long getVersionId() { return versionId; }

    /**
     * Set id.
     *
     * @param id The Long.
     */
    public void setId(final Long id) { this.id = id; }

    /**
     * Set versionId.
     *
     * @param versionId The Long.
     */
    public void setVersionId(final Long versionId) {
        this.versionId = versionId;
    }
}
