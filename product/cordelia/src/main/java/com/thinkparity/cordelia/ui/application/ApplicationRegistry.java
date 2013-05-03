/**
 * 
 */
package com.thinkparity.cordelia.ui.application;

import java.util.HashMap;
import java.util.Map;

import com.thinkparity.codebase.ui.application.Application;
import com.thinkparity.codebase.ui.application.ApplicationId;
import com.thinkparity.codebase.ui.application.ApplicationStatus;

import com.thinkparity.cordelia.ui.CordeliaPlatform;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ApplicationRegistry extends
        com.thinkparity.codebase.ui.application.ApplicationRegistry<CordeliaPlatform> {

    private static final Map<Id, Application<CordeliaPlatform>> REGISTRY;

    static {
        REGISTRY = new HashMap<Id, Application<CordeliaPlatform>>();
    }

    /**
     * Create ApplicationRegistry.
     * 
     */
    public ApplicationRegistry(final CordeliaPlatform platform) {
        super();
    }

    /**
     * @see com.thinkparity.codebase.ui.platform.ApplicationRegistry#contains(com.thinkparity.codebase.ui.application.ApplicationId)
     * 
     */
    @Override
    public boolean contains(final ApplicationId id) {
        return REGISTRY.containsKey(id);
    }

    /**
     * @see com.thinkparity.codebase.ui.platform.ApplicationRegistry#get(com.thinkparity.codebase.ui.application.ApplicationId)
     * 
     */
    @Override
    public Application<CordeliaPlatform> get(final ApplicationId id) {
        return REGISTRY.get(id);
    }

    /**
     * @see com.thinkparity.codebase.ui.platform.ApplicationRegistry#getStatus(com.thinkparity.codebase.ui.application.ApplicationId)
     * 
     */
    @Override
    public ApplicationStatus getStatus(final ApplicationId id) {
        return REGISTRY.get(id).getStatus();
    }

    /**
     * @see com.thinkparity.codebase.ui.platform.ApplicationRegistry#remove(com.thinkparity.codebase.ui.application.ApplicationId)
     */
    @Override
    public Application<CordeliaPlatform> remove(final ApplicationId id) {
        return REGISTRY.remove(id);
    }

    /**
     * @see com.thinkparity.codebase.ui.platform.ApplicationRegistry#put(com.thinkparity.codebase.ui.application.Application)
     */
    @Override
    protected Application<CordeliaPlatform> put(final Application<CordeliaPlatform> application) {
        return REGISTRY.put((Id) application.getId(), application);
    }

    /** Cordelia application ids. */
    public enum Id implements
            com.thinkparity.codebase.ui.application.ApplicationId {
        ADMIN
    }
}
