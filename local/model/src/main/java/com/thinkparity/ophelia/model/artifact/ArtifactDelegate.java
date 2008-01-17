/*
 * Created On:  27-Nov-07 5:04:06 PM
 */
package com.thinkparity.ophelia.model.artifact;

import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.DefaultDelegate;
import com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler;

/**
 * <b>Title:</b>thinkParity Ophelia Model Artifact Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class ArtifactDelegate extends DefaultDelegate<ArtifactModelImpl> {

    /** An artifact persistence interface. */
    protected ArtifactIOHandler artifactIO;

    /**
     * Create ArtifactDelegate.
     *
     */
    protected ArtifactDelegate() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.model.DefaultDelegate#initialize(com.thinkparity.ophelia.model.Model)
     * 
     */
    @Override
    public void initialize(final ArtifactModelImpl modelImplementation) {
        super.initialize(modelImplementation);

        artifactIO = modelImplementation.getArtifactIO();
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.ArtifactModelImpl#addTeamMember(Long, User)
     * 
     */
    protected final TeamMember addTeamMember(final Long artifactId, final User user) {
        return modelImplementation.addTeamMember(artifactId, user);
    }
}
