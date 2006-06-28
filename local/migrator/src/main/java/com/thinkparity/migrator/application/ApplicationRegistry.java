/*
 * Created On: Sun Jun 25 2006 11:00 PDT
 * $Id$
 */
package com.thinkparity.migrator.application;

import java.util.HashMap;
import java.util.Map;

import com.thinkparity.codebase.assertion.Assert;

public class ApplicationRegistry {

    private static final Map<ApplicationId, Application> REGISTRY;

    static {
        REGISTRY = new HashMap<ApplicationId, Application>(ApplicationId.values().length, 1.0F);
    }

    /** Create ApplicationRegistry. */
    public ApplicationRegistry() { super(); }

    void register(final Application application) {
        Assert.assertNotTrue("", REGISTRY.containsKey(application.getId()));
        REGISTRY.put(application.getId(), application);
    }

    public Boolean contains(final ApplicationId id) {
        return REGISTRY.containsKey(id);
    }

    public Application get(final ApplicationId id) {
        return REGISTRY.get(id);
    }
}
