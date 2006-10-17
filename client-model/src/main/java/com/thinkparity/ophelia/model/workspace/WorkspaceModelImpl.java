/*
 * Created On: Jul 23, 2005
 */
package com.thinkparity.ophelia.model.workspace;


import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.ErrorHelper;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.Assertion;
import com.thinkparity.codebase.event.EventListener;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.ParityErrorTranslator;
import com.thinkparity.ophelia.model.ParityUncheckedException;
import com.thinkparity.ophelia.model.Constants.ShutdownHookNames;
import com.thinkparity.ophelia.model.Constants.ShutdownHookPriorities;
import com.thinkparity.ophelia.model.Constants.ThreadNames;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.session.LoginMonitor;
import com.thinkparity.ophelia.model.session.SessionModel;
import com.thinkparity.ophelia.model.util.ShutdownHook;
import com.thinkparity.ophelia.model.workspace.impl.WorkspaceImpl;

/**
 * WorkspaceModelImpl
 * @author raykroeker@gmail.com
 * @version 1.1
 * @see com.thinkparity.ophelia.model.workspace.WorkspaceModel
 */
class WorkspaceModelImpl {

    /** A list of workspaces. */
    private static final List<WorkspaceImpl> WORKSPACES;

	static {
        WORKSPACES = new ArrayList<WorkspaceImpl>(1);

        Runtime.getRuntime().addShutdownHook(new Thread(ThreadNames.SHUTDOWN_HOOK) {
            @Override
            public void run() {
                Logger.getLogger(getClass()).trace("Runtime shutting down.");
                synchronized (WORKSPACES) {
                    List<ShutdownHook> shutdownHooks;
                    for (final WorkspaceImpl workspace : WORKSPACES) {
                        Logger.getLogger(getClass()).trace(
                                MessageFormat.format("Workspace {0} shutting down.", workspace.getName()));
                        shutdownHooks = workspace.getShutdownHooks();
                        for(final ShutdownHook shutdownHook : shutdownHooks) {
                            Logger.getLogger(getClass()).trace(
                                    MessageFormat.format("Workspace {0} priority {1} hook {2} shutting down.",
                                            workspace.getName(), shutdownHook.getPriority(),
                                            shutdownHook.getName()));
                            shutdownHook.run();
                        }
                    }
                }
            }
        });
	}

    /** A thinkParity <code>Environment</code>. */
    private final Environment environment;

	/**
	 * Create a WorkspaceModelImpl.
	 * 
	 */
	WorkspaceModelImpl(final Environment environment) {
        super();
        this.environment = environment;
	}

    /**
     * Add a model's event listener.
     * 
     * @param <T>
     *            A thinkParity event listener type.
     * @param impl
     *            A thinkParity model impl.
     * @param listener
     *            A thinkParity event listener.
     */
    public <T extends EventListener> boolean addListener(final Workspace workspace,
            final AbstractModelImpl impl, final T listener) {
        return findImpl(workspace).addListener(impl, listener);
    }

    /**
     * Obtain the model's event listeners.
     * 
     * @param <T>
     *            A thinkParity event listener type.
     * @return A list of typed thinkParity event listeners.
     */
    public <T extends EventListener> List<T> getListeners(final Workspace workspace,
            final AbstractModelImpl<T> impl) {
        return findImpl(workspace).getListeners(workspace, impl);
    }

    /**
     * Remove a model's event listener.
     * 
     * @param <T>
     *            A thinkParity event listener type.
     * @param listener
     *            A thinkParity event listener.
     */
    public <T extends EventListener> boolean removeListener(final Workspace workspace,
            final AbstractModelImpl impl, final T listener) {
        return findImpl(workspace).removeListener(impl, listener);
    }

    /**
     * Translate an error into a parity unchecked error.
     * 
     * @param t
     *            An error.
     */
    protected RuntimeException translateError(final Workspace workspace,
            final Throwable t) {
        if (ParityUncheckedException.class.isAssignableFrom(t.getClass())) {
            return (ParityUncheckedException) t;
        } else if (Assertion.class.isAssignableFrom(t.getClass())) {
            return (Assertion) t;
        }
        else {
            final String errorId = new ErrorHelper().getErrorId(t);
            return ParityErrorTranslator.translateUnchecked(workspace, errorId, t);
        }
    }

    /**
     * Delete a workspace.
     * 
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     */
    void delete(final Workspace workspace) {
        final WorkspaceImpl impl = findImpl(workspace);
        final File workspaceDirectory = impl.getWorkspaceDirectory();
        impl.addShutdownHook(new ShutdownHook() {
            @Override
            public String getDescription() {
                return ShutdownHookNames.WORKSPACE_DELETE;
            }
            @Override
            public String getName() {
                return ShutdownHookNames.WORKSPACE_DELETE;
            }
            @Override
            public Integer getPriority() {
                return ShutdownHookPriorities.WORKSPACE_DELETE;
            }
            @Override
            public void run() {
                FileUtil.deleteTree(workspaceDirectory);
            }
        });
    }

    /**
	 * Obtain the workspace for the parity model software.
	 * 
	 * @return The workspace.
	 */
	Workspace getWorkspace(final File workspace) {
        synchronized (WORKSPACES) {
            final WorkspaceImpl impl = new WorkspaceImpl(workspace);
            if (WORKSPACES.contains(impl)) {
                return WORKSPACES.get(WORKSPACES.indexOf(impl));
            } else {
                WORKSPACES.add(open(impl));
                return getWorkspace(workspace);
            }
        }
	}

    /**
     * Initialize the workspace. If the workspace fails to initialize; it will
     * be deleted.
     * 
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     * @param credentials
     *            A user's <code>Credentials</code>.
     */
    void initialize(final Workspace workspace, final LoginMonitor loginMonitor,
            final Credentials credentials) {
        try {
            final SessionModel sessionModel =
                SessionModel.getModel(environment, workspace);
            sessionModel.login(loginMonitor, credentials);
            Assert.assertTrue("User was not logged in.", sessionModel.isLoggedIn());            
            ContactModel.getModel(environment, workspace).download();
            findImpl(workspace).initialize();
        } catch (final Throwable t) {
            final WorkspaceImpl impl = findImpl(workspace);
            final File workspaceDirectory = impl.getWorkspaceDirectory();
            impl.addShutdownHook(new ShutdownHook() {
                @Override
                public String getDescription() {
                    return ShutdownHookNames.WORKSPACE_DELETE;
                }
                @Override
                public String getName() {
                    return ShutdownHookNames.WORKSPACE_DELETE;
                }
                @Override
                public Integer getPriority() {
                    return ShutdownHookPriorities.WORKSPACE_DELETE;
                }
                @Override
                public void run() {
                    FileUtil.deleteTree(workspaceDirectory);
                }
            });
            throw translateError(workspace, t);
        }
    }

    /**
     * Determine if this is the first run of the workspace.
     * 
     * @return True if this is the first run of the workspace; false otherwise.
     */
    Boolean isInitialized(final Workspace workspace) {
        return findImpl(workspace).isInitialized();
    }

    private WorkspaceImpl findImpl(final Workspace workspace) {
        synchronized (WORKSPACES) {
            for (final WorkspaceImpl impl : WORKSPACES) {
                if (impl.getWorkspaceDirectory().equals(workspace.getWorkspaceDirectory())) {
                    return impl;
                }
            }
        }
        return null;
    }

    /**
     * Open a workspace.
     * 
     * @param impl
     *            A <code>WorkspaceImpl</code>.
     * @return A <code>WorkspaceImpl</code>.
     */
    private WorkspaceImpl open(final WorkspaceImpl impl) {
        impl.open();
        impl.addShutdownHook(new ShutdownHook() {
            @Override
            public String getDescription() {
                return ShutdownHookNames.WORKSPACE_CLOSE;
            }
            @Override
            public String getName() {
                return ShutdownHookNames.WORKSPACE_CLOSE;
            }
            @Override
            public Integer getPriority() {
                return ShutdownHookPriorities.WORKSPACE_CLOSE;
            }
            @Override
            public void run() {
                impl.close();
            }
        });
        return impl;
    }
}

