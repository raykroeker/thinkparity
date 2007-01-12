/*
 * Created On: Sep 16, 2006 12:01:46 PM
 */
package com.thinkparity.desdemona.model.archive;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.profile.InternalProfileModel;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * Create instances of client model interfaces for a given workspace.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ClientModelFactory {

    /** A <code>InternalModelFactory</code>. */
    private final InternalModelFactory modelFactory;

    /** Create ClientModelFactory. */
    ClientModelFactory(final Context context, final Environment environment,
            final Workspace workspace) {
        super();
        this.modelFactory = InternalModelFactory.getInstance(context,
                environment, workspace);
    }

    public InternalArtifactModel getArtifactModel(final Class clasz) {
        return modelFactory.getArtifactModel();
    }

    public InternalContainerModel getContainerModel(final Class clasz) {
        return modelFactory.getContainerModel();
    }

    public InternalDocumentModel getDocumentModel(final Class clasz) {
        return modelFactory.getDocumentModel();
    }

    public InternalProfileModel getProfileModel(final Class clasz) {
        return modelFactory.getProfileModel();
    }

    public InternalSessionModel getSessionModel(final Class clasz) {
        return modelFactory.getSessionModel();
    }
}
