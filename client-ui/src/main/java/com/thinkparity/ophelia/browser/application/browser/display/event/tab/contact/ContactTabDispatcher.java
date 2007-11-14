/*
 * Created On:  9-Apr-07 7:36:40 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.event.tab.contact;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;

import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.events.*;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.session.SessionModel;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact.ContactTabAvatar;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher;

/**
 * <b>Title:</b>thinkParity OpheliaUI Contact Tab Dispatcher<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContactTabDispatcher implements
        EventDispatcher<ContactTabAvatar> {

    /** A <code>ContainerListener</code>. */
    private ContainerListener containerListener;

    /** An instance of <code>ContainerModel</code>. */
    private final ContainerModel containerModel;

    /** A profile listener. */
    private ProfileListener profileListener;

    /** A profile model. */
    private final ProfileModel profileModel;

    /** A <code>SessionListener</code>. */
    private SessionListener sessionListener;

    /** An instance of <code>SessionModel</code>. */
    private final SessionModel sessionModel;

    /**
     * Create ContactTabDispatcher.
     *
     * @param containerModel
     * @param profileModel
     * @param sessionModel
     */
    public ContactTabDispatcher(final ContainerModel containerModel,
            final ProfileModel profileModel, final SessionModel sessionModel) {
        super();
        this.containerModel = containerModel;
        this.profileModel = profileModel;
        this.sessionModel = sessionModel;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher#addListeners(com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar)
     *
     */
    public void addListeners(final ContactTabAvatar avatar) {
        Assert.assertIsNull(containerListener,
                "Listener for avatar {0} already added.", avatar.getId());
        containerListener = new ContainerAdapter() {
            @Override
            public void containerDeleted(final ContainerEvent e) {
                if (!e.getOutgoingEMailInvitations().isEmpty()) {
                    for (final OutgoingEMailInvitation invitation : e.getOutgoingEMailInvitations()) {
                        avatar.syncOutgoingEMailInvitation(invitation.getId(),
                                e.isRemote());
                    }
                }
            }
            @Override
            public void containerPublished(final ContainerEvent e) {
                avatar.firePublished(e);
            }
        };
        containerModel.addListener(containerListener);
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
                avatar.fireProfileEvent(e);
            }
            @Override
            public void profileActivated(final ProfileEvent e) {
                avatar.fireProfileEvent(e);
            }
            @Override
            public void profilePassivated(final ProfileEvent e) {
                avatar.fireProfileEvent(e);
            }
        };
        profileModel.addListener(profileListener);
        sessionListener = new SessionAdapter() {
            @Override
            public void sessionEstablished() {
                avatar.fireSessionEvent();
            }
            @Override
            public void sessionTerminated() {
                avatar.fireSessionEvent();
            }
        };
        sessionModel.addListener(sessionListener);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher#removeListeners(com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar)
     *
     */
    public void removeListeners(final ContactTabAvatar avatar) {
        Assert.assertNotNull(containerListener,
                "Listener for avatar {0} not yet added.", avatar.getId());
        containerModel.removeListener(containerListener);
        containerListener = null;
        profileModel.removeListener(profileListener);
        profileListener = null;
        sessionModel.removeListener(sessionListener);
        sessionListener = null;
    }
}
