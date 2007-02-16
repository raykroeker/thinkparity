/*
 * Jul 23, 2005
 */
package com.thinkparity.ophelia.model.workspace;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.ErrorHelper;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.Assertion;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.ParityErrorTranslator;
import com.thinkparity.ophelia.model.ParityUncheckedException;
import com.thinkparity.ophelia.model.Constants.ShutdownHookNames;
import com.thinkparity.ophelia.model.Constants.ShutdownHookPriorities;
import com.thinkparity.ophelia.model.Constants.ThreadNames;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.session.LoginMonitor;
import com.thinkparity.ophelia.model.util.ShutdownHook;
import com.thinkparity.ophelia.model.workspace.impl.WorkspaceImpl;

import org.apache.log4j.Logger;

/**
 * WorkspaceModel
 * The workspace structure is as follows:
 * Win32:
 * The root of the workspace in a win32 environment is the %APPDATA% environment
 * variable followed by the corporation property followed by the product
 * property.  An example is as follows:
 * C:\Documents and Settings\Joe Blow\Application Data\
 * 	+>Parity Software
 * 		+>Parity
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class WorkspaceModel {

    /** A list of workspaces. */
    private static final Map<File, WorkspaceImpl> WORKSPACES;

    static {
        WORKSPACES = new HashMap<File, WorkspaceImpl>(1, 0.75F);

        Runtime.getRuntime().addShutdownHook(new Thread(ThreadNames.SHUTDOWN_HOOK) {
            @Override
            public void run() {
                Logger.getLogger(getClass()).trace("Runtime shutting down.");
                synchronized (WORKSPACES) {
                    List<ShutdownHook> shutdownHooks;
                    for (final WorkspaceImpl workspace : WORKSPACES.values()) {
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
     * Obtain an instance of a workspace model.
     * 
     * @param environment
     *            A thinkParity <code>Environment</code>.
     * @return A <code>WorkspaceModel</code>.
     */
    public static WorkspaceModel getInstance(final Environment environment) {
        return new WorkspaceModel(environment);
    }

    /** A thinkParity <code>Context</code>. */
    private final Context context;

    /** A thinkParity <code>Environment</code>. */
    private final Environment environment;

    /**
     * Create a WorkspaceModelImpl.
     * 
     */
    protected WorkspaceModel(final Environment environment) {
        super();
        this.context = new Context();
        this.environment = environment;
    }

    /**
     * Close a workspace.
     * 
     * @param workspace
     *            A <code>Workspace</code>.
     */
    public void close(final Workspace workspace) {
        final WorkspaceImpl impl = findImpl(workspace);
        Assert.assertNotNull(impl, "Workspace {0} has is not open.", workspace);
        impl.close();
        synchronized (WORKSPACES) {
            WORKSPACES.remove(workspace.getWorkspaceDirectory());
        }
    }

    /**
     * Delete a workspace.
     * 
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     */
    public void delete(final Workspace workspace) {
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
    public Workspace getWorkspace(final File workspace) {
        synchronized (WORKSPACES) {
            if (WORKSPACES.containsKey(workspace)) {
                return WORKSPACES.get(workspace);
            } else {
                final WorkspaceImpl impl = new WorkspaceImpl(workspace);
                impl.open();
                WORKSPACES.put(workspace, impl);
                return getWorkspace(workspace);
            }
        }
    }

    /**
     * Initialize the workspace. If the workspace fails to initialize; it will
     * be deleted. The login monitor will allow the client to capture incorrect
     * passwords as well as confirmation of synchronization.
     * 
     * @param workspace
     *            A <code>Workspace</code>.
     * @param loginMonitor
     *            A <code>LoginMonitor</code>.
     * @param credentials
     *            The user's login <code>Credentials</code>.
     */
    public void initialize(final Workspace workspace,
            final LoginMonitor loginMonitor, final Credentials credentials) {
        try {
            final InternalModelFactory modelFactory = InternalModelFactory.getInstance(context, environment, workspace);

            // login
            final InternalSessionModel sessionModel = modelFactory.getSessionModel();
            sessionModel.login(loginMonitor, credentials);
            Assert.assertTrue("User was not logged in.", sessionModel.isLoggedIn());
            // initialize migrator
            modelFactory.getMigratorModel().initialize();
            // download contacts
            modelFactory.getContactModel().download();

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
    public Boolean isInitialized(final Workspace workspace) {
        return findImpl(workspace).isInitialized();
    }

    /**
     * Find the workspace implementation for a workspace.
     * 
     * @param workspace
     *            A <code>Workspace</code>.
     * @return A <code>WorkspaceImpl</code>.
     */
    protected final WorkspaceImpl findImpl(final Workspace workspace) {
        synchronized (WORKSPACES) {
            for (final WorkspaceImpl impl : WORKSPACES.values()) {
                if (impl.getWorkspaceDirectory().equals(workspace.getWorkspaceDirectory())) {
                    return impl;
                }
            }
        }
        return null;
    }

    /**
     * Translate an error into a parity unchecked error.
     * 
     * @param t
     *            An error.
     */
    private RuntimeException translateError(final Workspace workspace,
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
}
