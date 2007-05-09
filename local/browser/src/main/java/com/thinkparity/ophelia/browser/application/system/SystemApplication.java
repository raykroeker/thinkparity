/*
 * Created On:  Mar 17, 2006
 */
package com.thinkparity.ophelia.browser.application.system;

import javax.swing.SwingUtilities;

import com.thinkparity.codebase.Application;
import com.thinkparity.codebase.swing.AbstractJFrame;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.util.http.Link;
import com.thinkparity.codebase.model.util.http.LinkFactory;

import com.thinkparity.ophelia.model.events.ContactEvent;
import com.thinkparity.ophelia.model.events.ContainerEvent;
import com.thinkparity.ophelia.model.events.MigratorEvent;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.AbstractApplication;
import com.thinkparity.ophelia.browser.platform.BrowserPlatform;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.action.ActionFactory;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.ActionRegistry;
import com.thinkparity.ophelia.browser.platform.action.Data;
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
        return getSessionModel().isLoggedIn() ?
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
        return LinkFactory.getInstance(Application.OPHELIA,
                BrowserPlatform.getInstance().getEnvironment()).create();
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
        runLater(ActionId.PLATFORM_BROWSER_ICONIFY, data);
    }

    /**
     * Run the platform's login action.
     *
     */
    public void runLogin() {
        runLater(ActionId.PLATFORM_LOGIN, Data.emptyData());
    }

    /**
     * Run the platform's move to front action for the browser.
     *
     */
    public void runMoveBrowserToFront() {
        runLater(ActionId.PLATFORM_BROWSER_MOVE_TO_FRONT, Data.emptyData());
    }

    /**
     * Run the platform's open website action.
     *
     */
    public void runOpenWebsite() {
        run(ActionId.PLATFORM_OPEN_WEBSITE, Data.emptyData());
    }

    /**
     * Run the platform's quit action.
     *
     */
    public void runQuitPlatform() {
        runLater(ActionId.PLATFORM_QUIT, Data.emptyData());
    }

    /**
     * Run the platform's restart action.
     *
     */
    public void runRestartPlatform() {
        run(ActionId.PLATFORM_RESTART, Data.emptyData());
    }

    /**
     * Run the platform's restore action for the browser.
     *
     */
	public void runRestoreBrowser() {
        runLater(ActionId.PLATFORM_BROWSER_RESTORE, Data.emptyData());
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
        impl.reloadConnectionStatus(Connection.OFFLINE);
    }

    /**
     * Fire a connection online notification.
     *
     */
    void fireConnectionOnline() {
        impl.reloadConnectionStatus(Connection.ONLINE);
    }

    /**
     * Fire an incoming contact invitation created event.
     * 
     * @param e
     *            A <code>ContactEvent</code>.
     */
    void fireContactIncomingInvitationCreated(final ContactEvent e) {
        final Data data = new Data(1);
        data.set(Show.DataKey.INVITATION_ID, e.getIncomingInvitation().getId());
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
            public String getLinkTitle() {
                return getString("Notification.ContactIncomingInvitationCreated.Title");
            }
            @Override
            public int getNumberLines() {
                return 1;
            }
            @Override
            public void invokeAction() {
                run(ActionId.CONTACT_SHOW, data);
            }
        });
    }

    /**
     * Fire a container published event.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    void fireContainerPublished(final ContainerEvent e) {
        final Data data = new Data(1);
        data.set(com.thinkparity.ophelia.browser.platform.action.container.Show.DataKey.CONTAINER_ID, e.getContainer().getId());
        if (null == e.getPreviousVersion() && null == e.getNextVersion()) {
            // this is the first publish event
            impl.fireNotification(new DefaultNotification() {
                @Override
                public String getContentLine1() {
                    return e.getTeamMember().getName();
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
                public String getLinkTitle() {
                    return getString("Notification.ContainerPublishedFirstTime.Title");
                }
                @Override
                public int getNumberLines() {
                    return 2;
                }
                @Override
                public void invokeAction() {
                    run(ActionId.CONTAINER_SHOW, data);
                }
            });
        } else {
            // this is a subsequent publish event
            impl.fireNotification(new DefaultNotification() {
                @Override
                public String getContentLine1() {
                    return e.getTeamMember().getName();
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
                public String getLinkTitle() {
                    return getString("Notification.ContainerPublishedNotFirstTime.Title");
                }
                @Override
                public int getNumberLines() {
                    return 2;
                }
                @Override
                public void invokeAction() {
                    run(ActionId.CONTAINER_SHOW, data);
                }
            });
        }
    }

    /**
     * Fire a product release installed event.
     * 
     * @param e
     *            A <code>MigratorEvent</code>.
     */
    void fireProductReleaseInstalled(final MigratorEvent e) {
        getPlatform().restart();
    }

    /**
     * Run an action.
     * 
     * @param actionId
     *            The action id.
     * @param data
     *            The action data.
     */
    private void run(final ActionId actionId, final Data data) {
        try {
            if (actionRegistry.contains(actionId)) {
                actionRegistry.get(actionId).invokeAction(this, data);
            } else {
                ActionFactory.create(actionId).invokeAction(this, data);
            }
        } catch(final Throwable t) {
            logger.logError(t,
                    "Could not run system application action {0} with data {1}.",
                    actionId, data);
            throw new RuntimeException(t);
        }
    }

    /**
     * Run an action that doesn't need to be run imediately.
     * 
     * @param actionId
     *            The action id.
     * @param data
     *            The action data.
     * @see SwingUtilities#invokeLater(java.lang.Runnable)
     * @see SystemApplication#run(ActionId, Data)
     */
    private void runLater(final ActionId actionId, final Data data) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SystemApplication.this.run(actionId, data);
            }
        });
    }
}
