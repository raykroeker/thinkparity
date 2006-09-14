/*
 * Created On: Sep 11, 2006 4:06:57 PM
 */
package com.thinkparity.ophelia.model;

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
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class InternalModelFactory {

    /** A thinkParity <code>Context</code>. */
    private final Context context;

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
    InternalModelFactory(final Context context, final Workspace workspace) {
        super();
        this.context = context;
        this.workspace = workspace;
    }

    public InternalArtifactModel getArtifactModel() {
        return ArtifactModel.getInternalModel(context, workspace);
    }

    public InternalAuditModel getAuditModel() {
        return AuditModel.getInternalModel(context, workspace);
    }

    public InternalContactModel getContactModel() {
        return ContactModel.getInternalModel(context, workspace);
    }

    public InternalContainerModel getContainerModel() {
        return ContainerModel.getInternalModel(context, workspace);
    }

    public InternalSessionModel getSessionModel() {
        return SessionModel.getInternalModel(context, workspace);
    }

    public InternalDocumentModel getDocumentModel() {
        return DocumentModel.getInternalModel(context, workspace);
    }

    public InternalLibraryModel getLibraryModel() {
        return LibraryModel.getInternalModel(context, workspace);
    }
}
