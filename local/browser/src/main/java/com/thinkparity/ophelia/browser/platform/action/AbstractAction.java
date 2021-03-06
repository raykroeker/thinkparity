/*
 * Created On: Jan 10, 2006
 */
package com.thinkparity.ophelia.browser.platform.action;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.Icon;
import javax.swing.SwingUtilities;

import com.thinkparity.codebase.assertion.Assertion;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.backup.BackupModel;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.session.OfflineException;
import com.thinkparity.ophelia.model.session.SessionModel;
import com.thinkparity.ophelia.model.user.UserModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.BrowserPlatform;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.application.Application;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;
import com.thinkparity.ophelia.browser.platform.plugin.extension.ActionExtension;
import com.thinkparity.ophelia.browser.util.ModelFactory;
import com.thinkparity.ophelia.browser.util.localization.ActionLocalization;

/**
 * <b>Title:</b>thinkParity OpheliaUI Abstract Platform Action<br>
 * <b>Description:</b>An abstraction of an action to be invoked within the ui
 * platform.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class AbstractAction implements ActionInvocation {

    /** A synchronization lock for the invocation thread. */
    private static final Object RUN_LOCK;

    /**
     * A <code>ThreadFactory</code> used to serialize execution of actions on
     * a separate thread.
     */
    private static final ThreadFactory THREAD_FACTORY;

	static {
        RUN_LOCK = new Object();
        THREAD_FACTORY = Executors.defaultThreadFactory();
    }

    /**
     * Run a runnable within a thread.  To ensure that only a single action is
     * run at a time we synchronize on run lock.
     * 
     * @param runnable
     *            A <code>Runnable</code>.
     */
    protected static void run(final Runnable runnable) {
        final Thread thread = THREAD_FACTORY.newThread(new Runnable() {
            public void run() {
                synchronized (RUN_LOCK) {
                    runnable.run();
                }
            }
        });
        thread.setName("TPS-OpheliaUI-ActionRunner");
        thread.start();
    }

	/** Action localization. */
	protected final ActionLocalization localization;

	/** An apache logger. */
	protected final Log4JWrapper logger;

	/** The thinkParity model factory. */
	protected final ModelFactory modelFactory = ModelFactory.getInstance();

	/** The action accelerator. */
    private String accelerator;

    /** The action registry. */
    private final ActionRegistry actionRegistry;

    /** The thinkParity application registry. */
    private final ApplicationRegistry applicationRegistry;
    
    /** The action icon. */
	private Icon icon;
    
    /** The action id. */
	private ActionId id;

    /** The actin menu name (suited for main menus). */
    private String menuName;

	/** The action mnemonic. */
    private String mnemonic;

    /** The action name (suited for context menus). */
	private String name;

	/**
     * Create AbstractAction. This constructor is used by the plugin action
     * extensions.
     * 
     * @param extension
     *            An action extension.
     */
    protected AbstractAction(final ActionExtension extension) {
        super();
        this.actionRegistry = new ActionRegistry();
        this.applicationRegistry = new ApplicationRegistry();
        this.id = null;
        this.icon = null;
        this.localization = new ActionLocalization(extension);
        this.logger = new Log4JWrapper(getClass());
        this.name = localization.getString("NAME");
        this.menuName = localization.getString("MENUNAME");
        this.mnemonic = localization.getString("MNEMONIC").substring(0,1);
        this.accelerator = localization.getString("ACCELERATOR");
    }

    /**
     * Create AbstractAction.
     * 
     * @param id
     *            An action id.
     */
    protected AbstractAction(final ActionId id) {
        super();
        this.actionRegistry = new ActionRegistry();
        this.applicationRegistry = new ApplicationRegistry();
        this.id = id;
        this.icon = null;
        this.localization = new ActionLocalization(id.toString());
        this.logger = new Log4JWrapper(getClass());
        this.name = localization.getString("NAME");
        this.menuName = localization.getString("MENUNAME");
        this.mnemonic = localization.getString("MNEMONIC").substring(0,1);
        this.accelerator = localization.getString("ACCELERATOR");
    }

    /**
     * Display an error dialog for an application.
     * 
     * @param applicationId
     *            An <code>ApplicationId</code>.
     * @param errorMessageKey
     *            An error message localization key <code>String</code>.
     */
    public void displayErrorDialog(final ApplicationId applicationId,
            final String errorMessageKey) {
        BrowserPlatform.getInstance().displayErrorDialog(applicationId, null,
                errorMessageKey, (Object[]) null);
    }
    
    /**
     * Obtain the action ACCELERATOR.
     * 
     * @return The action ACCELERATOR.
     */
    public String getAccelerator() { return accelerator; }
    
    /**
	 * Obtain the action ICON.
	 * 
	 * @return The action ICON.
	 */
	public Icon getIcon() { return icon; }

	/**
	 * Obtain the action id.
	 * 
	 * @return The action id.
	 */
	public ActionId getId() { return id; }

    /**
     * Obtain the action MENUNAME.
     * (This name is used for main menus.)
     * 
     * @return The action MENUNAME.
     */
    public String getMenuName() { return menuName; }

	/**
     * Obtain the action MNEMONIC.
     * 
     * @return The action MNEMONIC.
     */
    public String getMnemonic() { return mnemonic; }

    /**
	 * Obtain the action NAME.
     * (This name is used for context menus.)
	 * 
	 * @return The action NAME.
	 */
	public String getName() { return name; }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.ActionInvocation#invokeAction(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invokeAction(final Application application, final Data data) {
        run(new Runnable() {
            public void run() {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            application.applyBusyIndicator();
                        }
                    });
                    invoke(data);
                } catch (final OfflineException ox) {
                    displayErrorDialog(application.getId(), "ErrorOffline");
                } catch (final Throwable t) {
                    displayErrorDialog(application.getId(), t);
                } finally {
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            public void run() {
                                application.removeBusyIndicator();
                            }
                        });
                    } catch (final InvocationTargetException itx) {
                        logger.logWarning(itx, "Could not remove busy indicator.");
                    } catch (final InterruptedException ix) {
                        logger.logWarning(ix, "Could not remove busy indicator.");
                    }
                }
            }
        });
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.ActionInvocation#invokeAction(com.thinkparity.ophelia.browser.platform.Platform, com.thinkparity.ophelia.browser.platform.action.Data)
     *
     */
    public void invokeAction(final Platform platform, final Data data) {
        run(new Runnable() {
            public void run() {
                try {
                    invoke(data);
                } catch (final Throwable t) {
                    displayErrorDialog(t);
               }
            }
        });
    }

    /**
     * Determine if the accelerator is set.
     * 
     * @return True if the accelerator is set; false otherwise.
     */
    public Boolean isSetAccelerator() {
        return ((null != accelerator) && (accelerator.charAt(0) != '!'));
    }

    /**
     * Determine if the menu name is set.
     * 
     * @return True if the name is set; false otherwise.
     */
    public Boolean isSetMenuName() {
        return null != menuName;
    }

	/**
     * Determine if the mnemonic is set.
     * 
     * @return True if the mnemonic is set; false otherwise.
     */
    public Boolean isSetMnemonic() {
        return ((null != mnemonic) && (mnemonic.charAt(0) != '!'));
    }

    /**
     * Determine if the name is set.
     * 
     * @return True if the name is set; false otherwise.
     */
	public Boolean isSetName() {
        return null != name;
	}

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.ActionInvocation#retryInvokeAction()
     * 
     */
    public void retryInvokeAction() {}
    
    /**
     * Set the accelerator.
     * 
     * @param accelerator
     *             The action accelerator.
     */
    public void setAccelerator(final String accelerator) {
        this.accelerator = accelerator;
    }
    
    /**
     * Set the icon.
     * 
     * @param icon
     *            The <code>Icon</code>.
     */
	public void setIcon(final Icon icon) {
        this.icon = icon;
    }

	/**
     * Set the menu name.
     * 
     * @param name
     *            A name <code>String</code>.
     */
    public void setMenuName(final String menuName) {
        this.menuName = menuName;
    }

	/**
     * Set the action mnemonic.
     * 
     * @param mnemonic
     *            The action mnemonic.
     */
    public void setMnemonic(final String mnemonic) {
        this.mnemonic = mnemonic;
    }
    
    /**
	 * Set the name.
	 * 
	 * @param name
	 *            A name <code>String</code>.
	 */
	public void setName(final String name) {
        this.name = name;
	}
    
    /**
	 * Obtain a thinkParity artifact interface.
	 * 
	 * @return A <code>ArtifactModel</code>.
	 */
	protected ArtifactModel getArtifactModel() {
		return modelFactory.getArtifactModel(getClass());
	}
    
    /**
     * Obtain a backup model.
     * 
     * @return An instance of <code>BackupModel</code>.
     */
    protected BackupModel getBackupModel() {
        return modelFactory.getBackupModel(getClass());
    }

    /**
     * Obtain the thinkParity browser application from the registry.
     * 
     * @return The thinkParity browser application.
     */
    protected Browser getBrowserApplication() {
        return (Browser) applicationRegistry.get(ApplicationId.BROWSER);
    }

	/**
     * Obtain the contact model api.
     * 
     * @return The contact model api.
     */
    protected ContactModel getContactModel() {
        return modelFactory.getContactModel(getClass());
    }

    /**
     * Obtain the container model api.
     * 
     * @return The container model api.
     */
    protected ContainerModel getContainerModel() {
        return modelFactory.getContainerModel(getClass());
    }

	/**
     * Convert the data element foudn at the given key to a list of files.
     * 
     * @param data
     *            The action data.
     * @param key
     *            The data element key.
     * @return A list of files.
     */
    protected List<File> getDataFiles(final Data data, final Enum<?> key) {
        final List<?> list = (List<?>) data.get(key);
        if(null == list) { return null; }
        final List<File> files = new ArrayList<File>();
        for(final Object o : list) { files.add((File) o); }
        return files;
    }

    /**
     * Convert the data element found at the given key to a list of jabber ids.
     * 
     * @param data
     *            The action data.
     * @param key
     *            The data element key.
     * @return A list of jabber ids.
     */
    protected List<JabberId> getDataJabberIds(final Data data, final Enum<?> key) {
        final List<?> list = (List<?>) data.get(key);
        if(null == list) { return null; }
        final List<JabberId> jabberIds = new ArrayList<JabberId>();
        for(final Object o : list) { jabberIds.add((JabberId) o); }
        return jabberIds;
    }

    /**
     * Convert the data element found at the given key to a list of users.
     * 
     * @param data
     *            The action data.
     * @param key
     *            The data element key.
     * @return A list of users.
     */
	protected List<User> getDataUsers(final Data data, final Enum<?> key) {
        final List<?> list = (List<?>) data.get(key);
        if(null == list) { return null; }
        final List<User> users = new ArrayList<User>();
        for(final Object o : list) { users.add((User) o); }
        return users;
    }

    /**
	 * Obtain the document model api.
	 * 
	 * @return The document model api.
	 */
	protected DocumentModel getDocumentModel() {
		return modelFactory.getDocumentModel(getClass());
	}

    /**
     * Obtain a thinkParity profile interface.
     * 
     * @return A <code>ProfileModel</code>.
     */
    protected ProfileModel getProfileModel() {
        return modelFactory.getProfileModel(getClass());
    }
    
    /**
     * Obtain the parity session interface.
     * 
     * @return The parity session interface.
     */
	protected SessionModel getSessionModel() {
		return modelFactory.getSessionModel(getClass());
	}  

	/**
	 * Obtain localized text.
	 * 
	 * @param localKey
	 *            The local key.
	 * @return The localized text.
	 */
	protected String getString(final String localKey) {
		return localization.getString(localKey);
	}   
    
	/**
	 * Obtain localized text.
	 * 
	 * @param localKey
	 *            The local key.
	 * @param arguments
	 *            The text arguments.
	 * @return The localized text.
	 */
	protected String getString(final String localKey, final Object[] arguments) {
		return localization.getString(localKey, arguments);
	}

	/**
     * Obtain the thinkParity user interface.
     * 
     * @return A thinkParity user interface.
     */
    protected UserModel getUserModel() {
        return modelFactory.getUserModel(getClass());
    }
    
	/**
     * Obtain the thinkParity workspace.
     * 
     * @return The thinkParity workspace.
     */
    protected Workspace getWorkspace() {
        return modelFactory.getWorkspace(getClass());
    }

    /**
     * Invoke an action.
     * This method can be used for calling one action from another.
     * 
     * @param actionId
     *            The action id.
     * @param data
     *            The action data.
     */
    protected void invoke(final ActionId actionId, final Data data) {
        getAction(actionId).invoke(data);
    }

    /**
	 * Invoke the action.
	 * 
	 * @param data
	 *            The action data.
	 */
	protected abstract void invoke(final Data data);

	/**
     * Translate an error into a browser runtime exception.
     * 
     * @param t
     *            An error.
     * @return A browser error.
     */
    protected RuntimeException translateError(final Throwable t) {
        if (BrowserException.class.isAssignableFrom(t.getClass())) {
            return (BrowserException) t;
        } else if (Assertion.class.isAssignableFrom(t.getClass())) {
            return (Assertion) t;
        }
        else {
            final String internalErrorId = new StringBuffer()
                    .append(getId()).append(" - ")
                    .append(t.getMessage())
                    .toString();
            logger.logError(t, internalErrorId);
            return new BrowserException(internalErrorId, t);
        }
    }

    /**
     * Display an error dialog for an application.
     * 
     * @param applicationId
     *            An <code>ApplicationId</code>.
     * @param error
     *            An error <code>Throwable</code>.
     */
    private void displayErrorDialog(final ApplicationId applicationId,
            final Throwable error) {
        BrowserPlatform.getInstance().displayErrorDialog(applicationId, error);
    }

    /**
     * Display an error dialog.
     * 
     * @param error
     *            An error <code>Throwable</code>.
     */
    private void displayErrorDialog(final Throwable error) {
        BrowserPlatform.getInstance().displayErrorDialog(error);
    }

    /**
     * Obtain the action.
     * 
     * @param id
     *            An <code>ActionId</code>.
     * @return The <code>AbstractAction</code>.
     */
    private AbstractAction getAction(final ActionId id) {
        if (actionRegistry.contains(id)) {
            return actionRegistry.get(id);
        } else {
            return ActionFactory.create(id);
        }
    }
}
