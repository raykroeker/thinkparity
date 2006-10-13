/*
 * Created On: Sep 16, 2006 12:01:46 PM
 */
package com.thinkparity.desdemona.model.archive;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.profile.InternalProfileModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.session.SessionModel;
import com.thinkparity.ophelia.model.workspace.InternalWorkspaceModel;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

/**
 * Create instances of client model interfaces for a given workspace.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ClientModelFactory {

    /** A thinkParity context. */
    private final Context context;

    /** A thinkParity client <code>Workspace</code>. */
    private final Workspace workspace;

    /** A thinkParity <code>Environment</code>. */
    private final Environment environment;

    /** Create ClientModelFactory. */
    ClientModelFactory(final Context context, final Environment environment,
            final Workspace workspace) {
        super();
        this.context = context;
        this.environment = environment;
        this.workspace = workspace;
    }

    public InternalArtifactModel getArtifactModel(final Class clasz) {
        return ArtifactModel.getInternalModel(context, environment, workspace);
    }

    public InternalContainerModel getContainerModel(final Class clasz) {
        return ContainerModel.getInternalModel(context, environment, workspace);
    }

    public InternalDocumentModel getDocumentModel(final Class clasz) {
        return DocumentModel.getInternalModel(context, environment, workspace);
    }

    public InternalProfileModel getProfileModel(final Class clasz) {
        return ProfileModel.getInternalModel(context, environment, workspace);
    }

    public InternalSessionModel getSessionModel(final Class clasz) {
        return SessionModel.getInternalModel(context, environment, workspace);
    }

    public InternalWorkspaceModel getWorkspace(final Class clasz) {
        return WorkspaceModel.getInternalModel(context, environment);
    }
}
