/**
 * Created On: 27-Jul-07 2:02:02 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.event;

import com.thinkparity.ophelia.model.events.ProfileAdapter;
import com.thinkparity.ophelia.model.events.ProfileEvent;
import com.thinkparity.ophelia.model.events.ProfileListener;
import com.thinkparity.ophelia.model.profile.ProfileModel;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.MainTitleAvatar;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher;

/**
 * <b>Title:</b>thinkParity Ophelia UI Main Title Dispatcher<br>
 * <b>Description:</b>Registers event listeners within the model for updating
 * the main title avatar. The events monitored are:
 * <ul>
 * <li>E-Mail updated.
 * <li>E-Mail verified.
 * <li>Profile updated.
 * <li>Profile disabled.
 * <li>Profile enabled.
 * </ul>
 * <br>
 * 
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public final class MainTitleDispatcher implements
    EventDispatcher<MainTitleAvatar> {

    /** A <code>ProfileListener</code>. */
    private ProfileListener profileListener;

    /** An instance of <code>ProfileModel</code>. */
    private final ProfileModel profileModel;

    /**
     * Create MainTitleDispatcher.
     * 
     * @param profileModel
     *            An instance of <code>ProfileModel</code>.
     */
    public MainTitleDispatcher(final ProfileModel profileModel) {
        super();
        this.profileModel = profileModel;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher#addListeners(com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar)
     */
    public void addListeners(final MainTitleAvatar avatar) {
        profileListener = new ProfileAdapter() {
            @Override
            public void emailUpdated(final ProfileEvent e) {
                avatar.fireProfileEMailEvent(e);
            }
            @Override
            public void emailVerified(final ProfileEvent e) {
                avatar.fireProfileEMailEvent(e);
            }
            @Override
            public void profileUpdated(final ProfileEvent e) {
                removeListeners(avatar);
                addListeners(avatar);
            }
            @Override
            public void profileActivated(final ProfileEvent e) {
                avatar.fireProfileEvent(e.getProfile());
            }
            @Override
            public void profilePassivated(final ProfileEvent e) {
                avatar.fireProfileEvent(e.getProfile());
            }
        };
        profileModel.addListener(profileListener);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher#removeListeners(com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar)
     */
    public void removeListeners(final MainTitleAvatar avatar) {
        profileModel.removeListener(profileListener);
        profileListener = null;
    }
}
