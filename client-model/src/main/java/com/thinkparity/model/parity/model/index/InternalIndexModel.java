/*
 * Mar 6, 2006
 */
package com.thinkparity.model.parity.model.index;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class InternalIndexModel extends IndexModel {

	/**
	 * Create a InternalIndexModel.
	 * 
	 * @param workspace
	 *            The parity workspace.
	 * @param context
	 *            The parity context.
	 */
	InternalIndexModel(final Workspace workspace, final Context context) {
		super(workspace);
		context.assertContextIsValid();
	}

    public void createContainer(final Long containerId,
            final String containerName) {
        synchronized(getImplLock()) {
            getImpl().createContainer(containerId, containerName);
        }
    }

	public void createDocument(final Long documentId, final String documentName)
			throws ParityException {
		synchronized(getImplLock()) {
			getImpl().createDocument(documentId, documentName);
		}
	}

	public void deleteArtifact(final Long artifactId) {
		synchronized(getImplLock()) { getImpl().deleteArtifactIndex(artifactId); }
	}
}
