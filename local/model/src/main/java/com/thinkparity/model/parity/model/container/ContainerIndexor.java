/*
 * Created On: Jun 28, 2006 9:09:49 PM
 */
package com.thinkparity.model.parity.model.container;

import com.thinkparity.model.parity.model.AbstractIndexor;
import com.thinkparity.model.parity.model.Context;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.4.3
 */
class ContainerIndexor extends AbstractIndexor {

    /**
     * Create ContainerIndexor.
     * 
     * @param context
     *            A thinkParity context.
     */
    ContainerIndexor(final Context context) { super(context); }

    void create(final Long containerId, final String containerName) {
        getInternalIndexModel().createContainer(containerId, containerName);
    }

    void delete(final Long containerId) {
        getInternalIndexModel().deleteArtifact(containerId);
    }
}
