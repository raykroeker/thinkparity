/*
 * Created On: Sep 11, 2006 4:06:57 PM
 */
package com.thinkparity.ophelia.model;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.audit.AuditModel;
import com.thinkparity.ophelia.model.audit.InternalAuditModel;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.contact.InternalContactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.migrator.InternalLibraryModel;
import com.thinkparity.ophelia.model.migrator.LibraryModel;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.session.SessionModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Internal Model Factory<br>
 * <b>Description:</b>An internal model factory is used by the model to
 * generate references to interal models outside the scope of the model impl
 * classes; usually to pass off to helper command pattern objects.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class InternalModelFactory {

    /** A thinkParity <code>Context</code>. */
    private final Context context;

    /** A thinkParity <code>Environment</code>. */
    private final Environment environment;

    /** A thinkParity <code>Workspace</code>. */
    private final Workspace workspace;

    /**
     * Create InternalModelFactory.
     * 
     * @param context
     *            A thinkParity <code>Context</code>.
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     */
    InternalModelFactory(final Context context, final Environment environment,
            final Workspace workspace) {
        super();
        this.context = context;
        this.environment = environment;
        this.workspace = workspace;
    }

    public InternalArtifactModel getArtifactModel() {
        return ArtifactModel.getInternalModel(context, environment, workspace);
    }

    public InternalAuditModel getAuditModel() {
        return AuditModel.getInternalModel(context, environment, workspace);
    }

    public InternalContactModel getContactModel() {
        return ContactModel.getInternalModel(context, environment, workspace);
    }

    public InternalContainerModel getContainerModel() {
        return ContainerModel.getInternalModel(context, environment, workspace);
    }

    public InternalDocumentModel getDocumentModel() {
        return DocumentModel.getInternalModel(context, environment, workspace);
    }

    public InternalLibraryModel getLibraryModel() {
        return LibraryModel.getInternalModel(context, environment, workspace);
    }

    public InternalSessionModel getSessionModel() {
        return SessionModel.getInternalModel(context, environment, workspace);
    }

    /**
     * Obtain the thinkParity workspace.
     * 
     * @return A <code>Workspace</code>.
     */
    public Workspace getWorkspace() {
        return workspace;
    }
}
