/*
 * Generated On: Sep 01 06 10:06:21 AM
 */
package com.thinkparity.ophelia.model.archive;


import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.InternalModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Archive Internal Model<br>
 * <b>Description:</b><br>
 *
 * @author CreateModel.groovy
 * @version 1.1.2.1
 */
public class InternalArchiveModel extends ArchiveModel implements InternalModel {

    /**
     * Create InternalArchiveModel
     *
     * @param workspace
     *		A thinkParity workspace.
     * @param context
     *		A thinkParity model context.
     */
    InternalArchiveModel(final Context context, final Environment environment,
            final Workspace workspace) {
        super(environment, workspace);
    }

    public void archive(final Long artifactId) {
        synchronized (getImplLock()) {
            getImpl().archive(artifactId);
        }
    }

    public InputStream openDocumentVersion(final UUID uniqueId,
            final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().openDocumentVersion(uniqueId, versionId);
        }
    }

    public Container readContainer(final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readContainer(uniqueId);
        }
    }

    public List<JabberId> readTeamIds(final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readTeamIds(uniqueId);
        }
    }

    public void restore(final UUID uniqueId) {
        synchronized (getImplLock()) {
            getImpl().restore(uniqueId);
        }
    }
}
