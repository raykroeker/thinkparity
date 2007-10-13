/*
 * Created On:  Mar 17, 2006
 */
package com.thinkparity.ophelia.browser.application.system;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.AbstractJFrame;

import com.thinkparity.codebase.model.contact.ContactInvitation;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.http.Link;
import com.thinkparity.codebase.model.util.http.LinkFactory;

import com.thinkparity.ophelia.model.events.ContactEvent;
import com.thinkparity.ophelia.model.events.ContainerEvent;
import com.thinkparity.ophelia.model.events.MigratorEvent;
import com.thinkparity.ophelia.model.events.ProfileEvent;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.AbstractApplication;
import com.thinkparity.ophelia.browser.application.system.dialog.ProfilePassivatedNotifyPage;
import com.thinkparity.ophelia.browser.application.system.dialog.Notification.NotificationType;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.action.ActionFactory;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.ActionInvocation;
import com.thinkparity.ophelia.browser.platform.action.ActionRegistry;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.contact.AcceptIncomingEMailInvitation;
import com.thinkparity.ophelia.browser.platform.action.contact.AcceptIncomingUserInvitation;
import com.thinkparity.ophelia.browser.platform.action.contact.ClearIncomingEMailInvitationNotifications;
import com.thinkparity.ophelia.browser.platform.action.contact.ClearIncomingUserInvitationNotifications;
import com.thinkparity.ophelia.browser.platform.action.contact.DeclineIncomingEMailInvitation;
import com.thinkparity.ophelia.browser.platform.action.contact.DeclineIncomingUserInvitation;
import com.thinkparity.ophelia.browser.platform.action.platform.browser.Iconify;
import com.thinkparity.ophelia.browser.platform.action.profile.VerifyEMail;
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
     */
    public void clearContainerNotifications(final Long containerId) {
        impl.fireClearNotifications(newNotificationId(Container.class,
                containerId));
    }

    /**
     * Clear the container notifications for a version.
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
     * Clear the email updated notification.
     */
    public void clearEMailUpdatedNotification() {
        impl.fireClearNotifications(newNotificationId(NotificationType.EMAIL_UPDATED));
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
     * Clear the specified notification.
     * 
     * @param notificationId
     *            A notification id <code>String</code>.
     */
    public void clearNotification(final String notificationId) {
        impl.fireClearNotifications(notificationId);
    }

    /**
     * Clear the profile passivated notification.
     */
    public void clearProfilePassivatedNotification() {
        impl.fireClearNotifications(newNotificationId(NotificationType.PROFILE_PASSIVATED));
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
     * Show an email updated notification.
     */
    public void showEMailUpdatedNotification() {
        fireEMailUpdated();
    }

    /**
     * Show a profile passivated notification.
     */
    public void showProfilePassivatedNotification() {
        fireProfilePassivated();
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
     * Fire an incoming contact email invitation created event.
     * 
     * @param e
     *            A <code>ContactEvent</code>.
     */
    void fireContactIncomingEMailInvitationCreated(final ContactEvent e) {
        fireContactIncomingInvitationCreated(e, IncomingInvitationType.EMAIL);
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
     * Fire an incoming contact user invitation created event.
     * 
     * @param e
     *            A <code>ContactEvent</code>.
     */
    void fireContactIncomingUserInvitationCreated(final ContactEvent e) {
        fireContactIncomingInvitationCreated(e, IncomingInvitationType.USER);
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
        final Long containerId = e.getContainer().getId();
        final Long versionId = e.getVersion().getVersionId();
        final boolean publishFirstTime = (null == e.getPreviousVersion() && null == e.getNextVersion());
        impl.fireNotification(new DefaultNotification() {
            @Override
            public String getGroupId() {
                return newNotificationId(Container.class, containerId);
            }
            public String getId() {
                return newNotificationId(Container.class, containerId, versionId);
            }
            public String getMessage() {
                if (publishFirstTime) {
                    return getString("Notification.ContainerPublishedFirstTime.Message",
                            new Object[] { e.getUser().getName(),
                                    e.getUser().getTitle(),
                                    e.getUser().getOrganization(),
                                    e.getContainer().getName() });
                } else {
                    return getString("Notification.ContainerPublishedNotFirstTime.Message",
                            new Object[] { e.getUser().getName(),
                                    e.getUser().getTitle(),
                                    e.getUser().getOrganization(),
                                    e.getContainer().getName() });
                }
            }
            public NotificationPriority getPriority() {
                return NotificationPriority.LOWEST;
            }
            public NotificationType getType() {
                return NotificationType.CONTAINER_PUBLISHED;
            }
            public void invokeAction() {
                final Data data = new Data(3);
                data.set(com.thinkparity.ophelia.browser.platform.action.container.Show.DataKey.CONTAINER_ID, containerId);
                data.set(com.thinkparity.ophelia.browser.platform.action.container.Show.DataKey.VERSION_ID, versionId);
                data.set(com.thinkparity.ophelia.browser.platform.action.container.Show.DataKey.CLEAR_SEARCH, Boolean.TRUE);
                invoke(ActionId.CONTAINER_SHOW, data);
            }
        });
    }

    /**
     * Fire a email updated event.
     * The event is associated with the user changing his email.
     * 
     * @param e
     *            A <code>ProfileEvent</code>.
     */
    void fireEMailUpdated(final ProfileEvent e) {
        fireEMailUpdated();
    }

    /**
     * Fire a email verified event.
     * 
     * @param e
     *            A <code>ProfileEvent</code>.
     */
    void fireEMailVerified(final ProfileEvent e) {
        invoke(ActionId.PROFILE_CLEAR_EMAIL_UPDATED_NOTIFICATION, Data.emptyData());
    }

    /**
     * Fire a product release installed event.
     * 
     * @param e
     *            A <code>MigratorEvent</code>.
     */
    void fireProductReleaseInstalled(final MigratorEvent e) {
        impl.fireNotification(new DefaultNotification() {
            public String getId() {
                return newNotificationId(NotificationType.PRODUCT_INSTALLED);
            }
            public NotificationPriority getPriority() {
                return NotificationPriority.HIGHEST;
            }
            public NotificationType getType() {
                return NotificationType.PRODUCT_INSTALLED;
            }
            public void invokeAction() {
                getPlatform().restart();
            }
        });
    }

    /**
     * Fire a profile activated event.
     * 
     * @param e
     *            A <code>ProfileEvent</code>.
     */
    void fireProfileActivated(final ProfileEvent e) {
        invoke(ActionId.PROFILE_CLEAR_PROFILE_PASSIVATED_NOTIFICATION, Data.emptyData());
    }

    /**
     * Fire a profile passivated event.
     * This event is associated with a failed credit card
     * transaction.
     * 
     * @param e
     *            A <code>ProfileEvent</code>.
     */
    void fireProfilePassivated(final ProfileEvent e) {
        fireProfilePassivated();
    }

    /**
     * Fire an incoming contact invitation created event.
     * 
     * @param e
     *            A <code>ContactEvent</code>.
     * @param incomingInvitationType
     *            The <code>IncomingInvitationType</code>.        
     */
    private void fireContactIncomingInvitationCreated(final ContactEvent e,
            final IncomingInvitationType incomingInvitationType) {
        final Long id = e.getIncomingInvitation().getId();
        impl.fireNotification(new DefaultNotification() {
            public String getId() {
                return newNotificationId(ContactInvitation.class, id);
            }
            public String getMessage() {
                final User user = e.getIncomingInvitation().getExtendedBy();
                return getString("Notification.Invitation.Message",
                        new Object[] { user.getName(), user.getTitle(), user.getOrganization() });
            }
            public NotificationPriority getPriority() {
                return NotificationPriority.LOW;
            }
            public NotificationType getType() {
                return NotificationType.INVITATION;
            }
            public void invokeAction() {
                final Data data = new Data(1);
                switch(incomingInvitationType) {
                case EMAIL:
                    data.set(AcceptIncomingEMailInvitation.DataKey.INVITATION_ID, id);
                    invoke(ActionId.CONTACT_ACCEPT_INCOMING_EMAIL_INVITATION, data);
                    break;
                case USER:
                    data.set(AcceptIncomingUserInvitation.DataKey.INVITATION_ID, id);
                    invoke(ActionId.CONTACT_ACCEPT_INCOMING_USER_INVITATION, data);
                    break;
                default:
                    Assert.assertUnreachable("Unknown contact invitation type.");
                }
            }
            public void invokeDeclineAction() {
                final Data data = new Data(2);
                switch(incomingInvitationType) {
                case EMAIL:
                    data.set(DeclineIncomingEMailInvitation.DataKey.CONFIRM, Boolean.FALSE);
                    data.set(DeclineIncomingEMailInvitation.DataKey.INVITATION_ID, id);
                    invoke(ActionId.CONTACT_DECLINE_INCOMING_EMAIL_INVITATION, data);
                    break;
                case USER:
                    data.set(DeclineIncomingUserInvitation.DataKey.CONFIRM, Boolean.FALSE);
                    data.set(DeclineIncomingUserInvitation.DataKey.INVITATION_ID, id);
                    invoke(ActionId.CONTACT_DECLINE_INCOMING_USER_INVITATION, data);
                    break;
                default:
                    Assert.assertUnreachable("Unknown contact invitation type.");
                }
            }
        });
    }

    /**
     * Fire a email updated event.
     * The event is associated with the user changing his email.
     */
    private void fireEMailUpdated() {
        impl.fireNotification(new DefaultNotification() {
            public String getId() {
                return newNotificationId(NotificationType.EMAIL_UPDATED);
            }
            public NotificationPriority getPriority() {
                return NotificationPriority.HIGH;
            }
            public NotificationType getType() {
                return NotificationType.EMAIL_UPDATED;
            }
            public void invokeAction() {
                if (isBrowserRunning()) {
                    runIconify(Boolean.FALSE);
                    runMoveBrowserToFront();
                } else {
                    runRestoreBrowser();
                }
                final Data data = new Data(1);
                data.set(VerifyEMail.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
                invoke(ActionId.PROFILE_VERIFY_EMAIL, data);
            }
        });
    }

    /**
     * Fire a profile passivated event.
     * This event is associated with a failed credit card
     * transaction.
     */
    private void fireProfilePassivated() {
        impl.fireNotification(new DefaultNotification() {
            Boolean accessiblePaymentInfo = Boolean.TRUE;
            Boolean accessiblePaymentInfoSet = Boolean.FALSE;
            public Data getData() {
                final Data data = new Data(1);
                data.set(ProfilePassivatedNotifyPage.DataKey.PAYMENT_INFO_ACCESSIBLE,
                        isAccessiblePaymentInfo());
                return data;
            }
            public String getId() {
                return newNotificationId(NotificationType.PROFILE_PASSIVATED);
            }
            public String getMessage() {
                if (isAccessiblePaymentInfo())  {
                    return getString("Notification.ProfilePassivated.AccessiblePaymentInfo");
                } else {
                    return getString("Notification.ProfilePassivated.NotAccessiblePaymentInfo");
                }
            }
            public NotificationPriority getPriority() {
                return NotificationPriority.MEDIUM;
            }
            public NotificationType getType() {
                return NotificationType.PROFILE_PASSIVATED;
            }
            public void invokeAction() {
                if (isBrowserRunning()) {
                    runIconify(Boolean.FALSE);
                    runMoveBrowserToFront();
                } else {
                    runRestoreBrowser();
                }
                invoke(ActionId.PROFILE_UPDATE_PAYMENT_INFO, Data.emptyData());
            }
            private Boolean isAccessiblePaymentInfo() {
                if (!accessiblePaymentInfoSet && Connection.ONLINE == getConnection()) {
                    try {
                        accessiblePaymentInfo = getProfileModel().isAccessiblePaymentInfo();
                        accessiblePaymentInfoSet = Boolean.TRUE;
                    } catch (final Throwable t) {
                        accessiblePaymentInfoSet = Boolean.FALSE;
                    }
                }
                return accessiblePaymentInfo;
            }
        });
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

    /**
     * Create a notificatin id for a notification type.
     * This is suitable when there can only be one notification
     * of the type.
     * 
     * @param type
     *            A <code>Class<?></code>.
     */
    private String newNotificationId(final NotificationType type) {
        return type.toString();
    }

    private enum IncomingInvitationType { EMAIL, USER }
}
