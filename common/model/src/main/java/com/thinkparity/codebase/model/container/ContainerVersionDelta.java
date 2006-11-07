/*
 * Created On:  6-Nov-06 9:58:27 AM
 */
package com.thinkparity.codebase.model.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.model.artifact.ArtifactVersion;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContainerVersionDelta {

    private Long compareToVersionId;

    private Long compareVersionId;

    private Long containerId;

    private final List<ContainerVersionArtifactVersionDelta> deltas;

    private Long id;

    /**
     * Create ContainerVersionDelta.
     *
     */
    public ContainerVersionDelta() {
        super();
        this.deltas = new ArrayList<ContainerVersionArtifactVersionDelta>();
    }

    public boolean addDelta(final ContainerVersionArtifactVersionDelta delta) {
        return deltas.add(delta);
    }

    public void clearDeltas() {
        deltas.clear();
    }

    public boolean containsDelta(final ArtifactVersion artifactVersion) {
        for (final ContainerVersionArtifactVersionDelta delta : deltas) {
            if (delta.isFor(artifactVersion)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtain compareTo.
     *
     * @return A ContainerVersion.
     */
    public Long getCompareToVersionId() {
        return compareToVersionId;
    }

    /**
     * Obtain compare.
     *
     * @return A ContainerVersion.
     */
    public Long getCompareVersionId() {
        return compareVersionId;
    }

    /**
     * Obtain container.
     *
     * @return A Container.
     */
    public Long getContainerId() {
        return containerId;
    }

    public List<ContainerVersionArtifactVersionDelta> getDeltas() {
        return Collections.unmodifiableList(deltas);
    }

    /**
     * Obtain id.
     *
     * @return A Long.
     */
    public Long getId() {
        return id;
    }

    public boolean removeDelta(final ContainerVersionArtifactVersionDelta delta) {
        return deltas.remove(delta);
    }

    /**
     * Set compareTo.
     *
     * @param compareTo
     *		A ContainerVersion.
     */
    public void setCompareToVersionId(final Long compareToId) {
        this.compareToVersionId = compareToId;
    }

    /**
     * Set compare.
     *
     * @param compare
     *		A ContainerVersion.
     */
    public void setCompareVersionId(final Long compareId) {
        this.compareVersionId = compareId;
    }

    /**
     * Set container.
     *
     * @param container
     *		A Container.
     */
    public void setContainerId(final Long containerId) {
        this.containerId = containerId;
    }

    /**
     * Set id.
     *
     * @param id
     *		A Long.
     */
    public void setId(final Long id) {
        this.id = id;
    }
}
