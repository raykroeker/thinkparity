/*
 * Created On: Sep 6, 2006 1:55:00 PM
 */
package com.thinkparity.model.artifact;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class RemoteArtifact extends Artifact {

    /** The artifact type. */
    private ArtifactType type;

    /** Create RemoteArtifact. */
    public RemoteArtifact() {
        super();
    }

    /**
     * @see com.thinkparity.model.artifact.Artifact#getType()
     */
    @Override
    public ArtifactType getType() {
        return type;
    }

    /**
     * Set type.
     *
     * @param type The ArtifactType.
     */
    public void setType(final ArtifactType type) {
        this.type = type;
    }
}
