/*
 * Created On: Jun 25, 2006 11:43:27 AM
 * $Id$
 */
package com.thinkparity.migrator.application.migrator.avatar;

import java.util.HashMap;
import java.util.Map;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class AvatarRegistry {

    /** The backing registry. */
    private static final Map<AvatarId, Avatar> REGISTRY;

    static {
        REGISTRY = new HashMap<AvatarId, Avatar>(AvatarId.values().length, 1.0F);
    }

    /** Create AvatarRegistry. */
    public AvatarRegistry() { super(); }

    public Boolean contains(final AvatarId id) {
        return REGISTRY.containsKey(id);
    }

    public Avatar get(final AvatarId id) {
        return REGISTRY.get(id);
    }

    void register(final Avatar avatar) {
        Assert.assertNotTrue("", REGISTRY.containsKey(avatar.getId()));
        REGISTRY.put(avatar.getId(), avatar);
    }
}
