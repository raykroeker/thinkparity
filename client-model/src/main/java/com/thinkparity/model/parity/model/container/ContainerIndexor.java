/*
 * Created On: Jun 28, 2006 9:09:49 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractIndexor;
import com.thinkparity.model.parity.model.Context;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class ContainerIndexor extends AbstractIndexor {

    /**
     * Create ContainerIndexor.
     * 
     * @param context
     *            A thinkParity context.
     */
    ContainerIndexor(final Context context) { super(context); }

    void create(final Long containerId, final String containerName)
            throws ParityException {
        getInternalIndexModel().createContainer(containerId, containerName);
    }

    void delete(final Long containerId) throws ParityException {
        getInternalIndexModel().deleteArtifact(containerId);
    }
}
