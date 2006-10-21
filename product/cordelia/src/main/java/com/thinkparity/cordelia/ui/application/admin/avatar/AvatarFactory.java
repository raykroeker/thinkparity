/**
 * 
 */
package com.thinkparity.cordelia.ui.application.admin.avatar;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.ui.avatar.Avatar;
import com.thinkparity.codebase.ui.avatar.AvatarId;
import com.thinkparity.codebase.ui.provider.Provider;

import com.thinkparity.cordelia.ui.CordeliaPlatform;
import com.thinkparity.cordelia.ui.application.admin.AdminApplication;
import com.thinkparity.cordelia.ui.application.admin.avatar.user.UserTabAvatar;
import com.thinkparity.cordelia.ui.application.admin.provider.user.UserTabProvider;

/**
 * @author raymond
 *
 */
public final class AvatarFactory
        extends
        com.thinkparity.codebase.ui.avatar.AvatarFactory<CordeliaPlatform, AdminApplication> {

    private static final AvatarFactory INSTANCE;

    static {
        INSTANCE = new AvatarFactory(new AvatarRegistry());
    }

    public static AvatarFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Create AvatarFactory.
     * 
     */
    private AvatarFactory(final AvatarRegistry registry) {
        super(registry);
    }

    /**
     * @see com.thinkparity.codebase.ui.avatar.AvatarFactory#doCreate(com.thinkparity.codebase.ui.avatar.AvatarId)
     * 
     */
    @Override
    protected Avatar<CordeliaPlatform, AdminApplication, ? extends Provider> doCreate(
            final AvatarId id) {
        switch (toEnum(id)) {
        case USER_TAB:
            final UserTabAvatar userTab = new UserTabAvatar();
            userTab.setInput(null);
            userTab.setProvider(new UserTabProvider());
            return userTab;
        default:
            throw Assert.createUnreachable("UNKOWN AVATAR {0}", id);
        }
    }

    private com.thinkparity.cordelia.ui.application.admin.avatar.AvatarId toEnum(final AvatarId id) {
        return (com.thinkparity.cordelia.ui.application.admin.avatar.AvatarId) id;
    }
}
