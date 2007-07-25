/*
 * Created On:  Mar 17, 2006
 */
package com.thinkparity.ophelia.browser.application.system;

import com.thinkparity.codebase.swing.AbstractJFrame;

import com.thinkparity.codebase.model.contact.ContactInvitation;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.util.http.Link;
import com.thinkparity.codebase.model.util.http.LinkFactory;

import com.thinkparity.ophelia.model.events.ContactEvent;
import com.thinkparity.ophelia.model.events.ContainerEvent;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.AbstractApplication;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.action.ActionFactory;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.ActionInvocation;
import com.thinkparity.ophelia.browser.platform.action.ActionRegistry;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.contact.ClearIncomingEMailInvitationNotifications;
import com.thinkparity.ophelia.browser.platform.action.contact.ClearIncomingUserInvitationNotifications;
import com.thinkparity.ophelia.browser.platform.action.contact.Show;
import com.thinkparity.ophelia.browser.platform.action.platform.browser.Iconify;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;
import com.thinkparity.ophelia.browser.platform.application.ApplicationStatus;
import com.thinkparity.ophelia.browser.platform.application.L18nContext;

/**
 * <b>Title:</b>thinkParity OpheliaUI System Application<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.22
 */
public final class SystemApplication extends AbstractApplication {

	/** The action registry. */
    private final ActionRegistry actionRegistry;

    /** The application registry. */
    private final ApplicationRegistry applicationRegistry;

    /** The event dispatcher. */
	private EventDispatcher ed;

	/** The application impl. */
	private SystemApplicationImpl impl;

	/**
     * Create SystemApplication.
     * 
     * @param platform
     *            The <code>Platform</code>.
     */
	public SystemApplication(final Platform platform) {
		super(platform, L18nContext.SYS_APP);
        this.actionRegistry = new ActionRegistry();
        this.applicationRegistry = new ApplicationRegistry();
	}

	/**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#applyBusyIndicator()
     *
     */
    public void applyBusyIndicator() {}

    /**
     * Clear the container notifications.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     */
    public void clearContainerNotifications(final Long containerId,
            final Long versionId) {
        impl.fireClearNotifications(newNotificationId(Container.class,
                containerId, versionId));
    }

    /**
     * Clear the incoming invitation notifications.
     * 
     * @param invitationId
     *            An invitation id <code>Long</code>.
     */
    public void clearInvitationNotifications(final Long invitationId) {
        impl.fireClearNotifications(newNotificationId(ContactInvitation.class,
                invitationId));
    }

    /**
     * Display the info notification.
     * 
     */
    public void displayInfo() {
        impl.displayInfo();
    }

    /**
	 * @see com.thinkparity.ophelia.browser.platform.application.Application#end(com.thinkparity.ophelia.browser.platform.Platform)
	 * 
	 */
	public void end(final Platform platform) {
		logApiId();

		impl.end();
		impl = null;

		ed.end();
		ed = null;

		notifyEnd();
	}

	/**
     * @see com.thinkparity.ophelia.browser.application.AbstractApplication#getBuildId()
     *
     */
    @Override
    public String getBuildId() {
        return super.getBuildId();
    }

	/**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#getConnection()
     *
	 */
    public Connection getConnection() {
        return getSessionModel().isOnline() ?
                Connection.ONLINE : Connection.OFFLINE;
    }

	/**
	 * @see com.thinkparity.ophelia.browser.platform.application.Application#getId()
	 * 
	 */
	public ApplicationId getId() {
        return ApplicationId.SYSTEM;
    }

	/**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#getMainWindow()
     *
     */
    public AbstractJFrame getMainWindow() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.AbstractApplication#getReleaseName()
     *
     */
    @Override
    public String getReleaseName() {
        return super.getReleaseName();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.AbstractApplication#getString(java.lang.String)
     * 
     */
	public String getString(final String localKey) {
		return super.getString(localKey);
	}

    /**
     * @see com.thinkparity.ophelia.browser.application.AbstractApplication#getString(java.lang.String,
     *      java.lang.Object[])
     * 
     */
    public String getString(final String localKey, final Object[] arguments) {
		return super.getString(localKey, arguments);
	}

    /**
     * Get the web page.
     * 
     * @return The web page <code>Link</code>.
     */
    public Link getWebPage() {
        return LinkFactory.getInstance().create();
    }

    /**
	 * @see com.thinkparity.ophelia.browser.platform.application.Application#hibernate(com.thinkparity.ophelia.browser.platform.Platform)
	 * 
	 */
	public void hibernate(final Platform platform) {
        logApiId();

		impl.end();
		impl = null;

		ed.end();
		ed = null;

		notifyHibernate();
	}

    /**
     * Determine whether or not the browser is running.
     * 
     * @return True if the browser is running false otherwise.
     */
    public Boolean isBrowserRunning() {
        return ApplicationStatus.RUNNING ==
            applicationRegistry.getStatus(ApplicationId.BROWSER);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#isDevelopmentMode()
     * 
     */
    public Boolean isDevelopmentMode() {
        return getPlatform().isDevelopmentMode();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#removeBusyIndicator()
     *
     */
    public void removeBusyIndicator() {}

	/**
	 * @see com.thinkparity.ophelia.browser.platform.application.Application#restore(com.thinkparity.ophelia.browser.platform.Platform)
	 * 
	 */
	public void restore(final Platform platform) {
        logApiId();

		impl = new SystemApplicationImpl(this);
		impl.start();

		ed = new EventDispatcher(this);
		ed.start();

		notifyRestore();
	}

    /**
     * Run the iconify action.
     * 
     * @param iconify
     *            Whether or not to iconify.
     */
    public void runIconify(final Boolean iconify) {
        final Data data = new Data(1);
        data.set(Iconify.DataKey.ICONIFY, iconify);
        invoke(ActionId.PLATFORM_BROWSER_ICONIFY, data);
    }

    /**
     * Run the platform's login action.
     *
     */
    public void runLogin() {
        invoke(ActionId.PLATFORM_LOGIN, Data.emptyData());
    }

    /**
     * Run the platform's move to front action for the browser.
     *
     */
    public void runMoveBrowserToFront() {
        invoke(ActionId.PLATFORM_BROWSER_MOVE_TO_FRONT, Data.emptyData());
    }

    /**
     * Run the platform's open website action.
     *
     */
    public void runOpenWebsite() {
        invoke(ActionId.PLATFORM_OPEN_WEBSITE, Data.emptyData());
    }

    /**
     * Run the platform's quit action.
     *
     */
    public void runQuitPlatform() {
        invoke(ActionId.PLATFORM_QUIT, Data.emptyData());
    }

    /**
     * Run the platform's restart action.
     *
     */
    public void runRestartPlatform() {
        invoke(ActionId.PLATFORM_RESTART, Data.emptyData());
    }

    /**
     * Run the platform's restore action for the browser.
     *
     */
	public void runRestoreBrowser() {
        invoke(ActionId.PLATFORM_BROWSER_RESTORE, Data.emptyData());
    }

    /**
	 * @see com.thinkparity.ophelia.browser.platform.application.Application#start(com.thinkparity.ophelia.browser.platform.Platform)
	 * 
	 */
	public void start(final Platform platform) {
        logApiId();

		impl = new SystemApplicationImpl(this);
		impl.start();

		ed = new EventDispatcher(this);
		ed.start();

		notifyStart();
	}

	/**
     * @see com.thinkparity.ophelia.browser.application.AbstractApplication#getProfile()
     * 
     */
    @Override
    protected Profile getProfile() {
        return super.getProfile();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.AbstractApplication#translateError(java.lang.Throwable)
     * 
     */
    @Override
    protected BrowserException translateError(final Throwable t) {
        return super.translateError(t);
    }

    /**
     * Fire a connection offline notification.
     *
     */
    void fireConnectionOffline() {
        impl.fireConnectionOffline();
    }

    /**
     * Fire a connection online notification.
     *
     */
    void fireConnectionOnline() {
        impl.fireConnectionOnline();
    }

    /**
     * Fire a contact incoming e-mail invitation deleted event.
     * 
     * @param e
     *            A <code>ContactEvent</code>.
     */
    void fireContactIncomingEMailInvitationDeleted(final ContactEvent e) {
        final Data data = new Data(1);
        data.set(ClearIncomingEMailInvitationNotifications.DataKey.INVITATION_ID, e.getIncomingInvitation().getId());
        invoke(ActionId.CONTACT_CLEAR_INCOMING_EMAIL_INVITATION_NOTIFICATIONS, data);
    }

    /**
     * Fire an incoming contact invitation created event.
     * 
     * @param e
     *            A <code>ContactEvent</code>.
     */
    void fireContactIncomingInvitationCreated(final ContactEvent e) {
        final Data data = new Data(1);
        final Long id = e.getIncomingInvitation().getId();
        data.set(Show.DataKey.INVITATION_ID, id);
        impl.fireNotification(new DefaultNotification() {
            @Override
            public String getContentLine1() {
                return e.getIncomingInvitation().getExtendedBy().getName();
            }
            @Override
            public String getHeadingLine1() {
                return getString("Notification.ContactIncomingInvitationCreated.HeadingLine1");
            }
            @Override
            public String getId() {
                return newNotificationId(ContactInvitation.class, id);
            }
            @Override
            public String getLinkTitle() {
                return getString("Notification.ContactIncomingInvitationCreated.Title");
            }
            @Override
            public int getNumberLines() {
                return 1;
            }
            @Override
            public void invokeAction() {
                invoke(ActionId.CONTACT_SHOW, data);
            }
        });
    }

    /**
     * Fire a contact incoming user invitation deleted event.
     * 
     * @param e
     *            A <code>ContactEvent</code>.
     */
    void fireContactIncomingUserInvitationDeleted(final ContactEvent e) {
        final Data data = new Data(1);
        data.set(ClearIncomingUserInvitationNotifications.DataKey.INVITATION_ID, e.getIncomingInvitation().getId());
        invoke(ActionId.CONTACT_CLEAR_INCOMING_USER_INVITATION_NOTIFICATIONS, data);
    }

    /**
     * Fire a container published event.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    void fireContainerPublished(final ContainerEvent e) {
        final Data data = new Data(2);
        final Long containerId = e.getContainer().getId();
        final Long versionId = e.getVersion().getVersionId();
        data.set(com.thinkparity.ophelia.browser.platform.action.container.Show.DataKey.CONTAINER_ID, containerId);
        data.set(com.thinkparity.ophelia.browser.platform.action.container.Show.DataKey.VERSION_ID, versionId);
        if (null == e.getPreviousVersion() && null == e.getNextVersion()) {
            // this is the first publish event
            impl.fireNotification(new DefaultNotification() {
                @Override
                public String getContentLine1() {
                    return e.getUser().getName();
                }
                @Override
                public String getContentLine2() {
                    return e.getContainer().getName();
                }
                @Override
                public String getHeadingLine1() {
                    return getString("Notification.ContainerPublishedFirstTime.HeadingLine1");
                }
                @Override
                public String getHeadingLine2() {
                    return getString("Notification.ContainerPublishedFirstTime.HeadingLine2");
                }
                @Override
                public String getId() {
                    return newNotificationId(Container.class, containerId, versionId);
                }
                @Override
                public String getLinkTitle() {
                    return getString("Notification.ContainerPublishedFirstTime.Title");
                }
                @Override
                public int getNumberLines() {
                    return 2;
                }
                @Override
                public void invokeAction() {
                    invoke(ActionId.CONTAINER_SHOW, data);
                }
            });
        } else {
            // this is a subsequent publish event
            impl.fireNotification(new DefaultNotification() {
                @Override
                public String getContentLine1() {
                    return e.getUser().getName();
                }
                @Override
                public String getContentLine2() {
                    return e.getContainer().getName();
                }
                @Override
                public String getHeadingLine1() {
                    return getString("Notification.ContainerPublishedNotFirstTime.HeadingLine1");
                }
                @Override
                public String getHeadingLine2() {
                    return getString("Notification.ContainerPublishedNotFirstTime.HeadingLine2");
                }
                @Override
                public String getId() {
                    return newNotificationId(Container.class, containerId, versionId);
                }
                @Override
                public String getLinkTitle() {
                    return getString("Notification.ContainerPublishedNotFirstTime.Title");
                }
                @Override
                public int getNumberLines() {
                    return 2;
                }
                @Override
                public void invokeAction() {
                    invoke(ActionId.CONTAINER_SHOW, data);
                }
            });
        }
    }

    /**
     * Obtain the action from the controller's cache. If the action does not
     * exist in the cache it is created and stored.
     * 
     * @param id
     *            The action id.
     * @return The action.
     * 
     * @see ActionId
     */
    private ActionInvocation getAction(final ActionId id) {
        if (actionRegistry.contains(id)) {
            return actionRegistry.get(id);
        } else {
            return ActionFactory.create(id);
        }
    }

    /**
     * Invoke an action.
     * 
     * @param actionId
     *            An <code>ActionId</code>.
     * @param data
     *            The action <code>Data</code>.
     */
    private void invoke(final ActionId actionId, final Data data) {
        getAction(actionId).invokeAction(this, data);
    }

    /**
     * Create a notification id for a type/type id pair.
     * 
     * @param type
     *            A <code>Class<?></code>.
     * @param typeId
     *            A type id <code>Long</code>.
     * @return A notification id <code>String</code>.
     */
    private String newNotificationId(final Class<?> type, final Long typeId) {
        return new StringBuilder(type.getName()).append("//").append(typeId).toString();
    }

    /**
     * Create a notification id for a type/type+type id pair.
     * 
     * @param type
     *            A <code>Class<?></code>.
     * @param typeId1
     *            A type id <code>Long</code>.
     * @param typeId2
     *            A type id <code>Long</code>.
     * @return A notification id <code>String</code>.
     */
    private String newNotificationId(final Class<?> type, final Long typeId1, final Long typeId2) {
        return new StringBuilder(type.getName()).append("//").append(typeId1).append("//").append(typeId2).toString();
    }
}
