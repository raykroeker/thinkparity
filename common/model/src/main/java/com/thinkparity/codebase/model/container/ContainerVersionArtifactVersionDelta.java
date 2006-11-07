/*
 * Created On:  6-Nov-06 9:59:32 AM
 */
package com.thinkparity.codebase.model.container;

import com.thinkparity.codebase.model.artifact.ArtifactVersion;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContainerVersionArtifactVersionDelta {

    private Long artifactId;

    private Long artifactVersionId;

    private Delta delta;

    /**
     * Create ContainerVersionArtifactVersionDelta.
     *
     */
    public ContainerVersionArtifactVersionDelta() {
        super();
    }

    /**
     * Obtain artifactId.
     *
     * @return A Long.
     */
    public Long getArtifactId() {
        return artifactId;
    }

    /**
     * Obtain artifactVersionId.
     *
     * @return A Long.
     */
    public Long getArtifactVersionId() {
        return artifactVersionId;
    }

    /**
     * Obtain delta.
     *
     * @return A Delta.
     */
    public Delta getDelta() {
        return delta;
    }

    public boolean isFor(final ArtifactVersion artifactVersion) {
        return artifactVersionId.equals(artifactVersion.getArtifactId());
    }

    /**
     * Set artifactId.
     *
     * @param artifactId
     *		A Long.
     */
    public void setArtifactId(Long artifactId) {
        this.artifactId = artifactId;
    }

    /**
     * Set artifactVersionId.
     *
     * @param artifactVersionId
     *		A Long.
     */
    public void setArtifactVersionId(Long artifactVersionId) {
        this.artifactVersionId = artifactVersionId;
    }

    /**
     * Set delta.
     *
     * @param delta
     *		A Delta.
     */
    public void setDelta(Delta delta) {
        this.delta = delta;
    }

    public enum Delta { ADDED, MODIFIED, NONE, REMOVED }
}
