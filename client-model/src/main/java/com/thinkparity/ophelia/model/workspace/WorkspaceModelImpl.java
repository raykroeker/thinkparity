/*
 * Created On: Jul 23, 2005
 */
package com.thinkparity.ophelia.model.workspace;


import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.event.EventListener;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.Constants.ShutdownHookNames;
import com.thinkparity.ophelia.model.Constants.ShutdownHookPriorities;
import com.thinkparity.ophelia.model.Constants.ThreadNames;
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

	/**
	 * Create a WorkspaceModelImpl.
	 * 
	 */
	WorkspaceModelImpl() {
        super();
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
     * Determine if this is the first run of the workspace.
     * 
     * @return True if this is the first run of the workspace; false otherwise.
     */
    Boolean isFirstRun(final Workspace workspace) {
        return findImpl(workspace).isFirstRun();
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
                return ShutdownHookNames.WORKSPACE;
            }
            @Override
            public String getName() {
                return ShutdownHookNames.WORKSPACE;
            }
            @Override
            public Integer getPriority() {
                return ShutdownHookPriorities.WORKSPACE;
            }
            @Override
            public void run() {
                impl.close();
            }
        });
        return impl;
    }
}

