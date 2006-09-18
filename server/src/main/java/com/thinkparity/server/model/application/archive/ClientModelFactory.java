/*
 * Created On: Sep 16, 2006 12:01:46 PM
 */
package com.thinkparity.desdemona.model.archive;

import com.thinkparity.codebase.model.Context;

import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
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
class ClientModelFactory {

    /** A thinkParity context. */
    private final Context context;

    /** A thinkParity client <code>Workspace</code>. */
    private final Workspace workspace;

    /** Create ClientModelFactory. */
    ClientModelFactory(final Context context, final Workspace workspace) {
        super();
        this.context = context;
        this.workspace = workspace;
    }

    InternalArtifactModel getArtifactModel(final Class clasz) {
        return ArtifactModel.getInternalModel(context, workspace);
    }

    InternalContainerModel getContainerModel(final Class clasz) {
        return ContainerModel.getInternalModel(context, workspace);
    }

    InternalDocumentModel getDocumentModel(final Class clasz) {
        return DocumentModel.getInternalModel(context, workspace);
    }

    InternalSessionModel getSessionModel(final Class clasz) {
        return SessionModel.getInternalModel(context, workspace);
    }

    InternalWorkspaceModel getWorkspace(final Class clasz) {
        return WorkspaceModel.getInternalModel(context);
    }
}
