/**
 * Created On: 20-Jun-07 12:04:26 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.event;

import com.thinkparity.ophelia.model.profile.ProfileModel;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.UpdateProfileAvatar;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class UpdateProfileDispatcher implements
        EventDispatcher<UpdateProfileAvatar> {

    /**
     * Create UpdateProfileDispatcher.
     * 
     * @param profileModel
     *            An instance of <code>ProfileModel</code>.
     */
    public UpdateProfileDispatcher(final ProfileModel profileModel) {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher#addListeners(com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar)
     *
     */
    public void addListeners(final UpdateProfileAvatar avatar) {
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher#removeListeners(com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar)
     * 
     */
    public void removeListeners(final UpdateProfileAvatar avatar) {
    }
}
