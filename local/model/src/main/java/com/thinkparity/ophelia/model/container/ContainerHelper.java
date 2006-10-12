/*
 * Created On: 11-Oct-06 12:01:10 PM
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.ophelia.model.AbstractModelImplHelper;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class ContainerHelper extends AbstractModelImplHelper {

    /** The container model implementation. */
    protected final ContainerModelImpl impl;

    /**
     * Create ContainerHelper.
     *
     */
    ContainerHelper(final ContainerModelImpl impl) {
        super();
        this.impl = impl;
    }
}
