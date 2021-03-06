/*
 * Jul 23, 2005
 */
package com.thinkparity.ophelia.model.workspace;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.thinkparity.codebase.ErrorHelper;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.Assertion;

import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.session.Credentials;

import com.thinkparity.ophelia.model.Constants.ShutdownHookNames;
import com.thinkparity.ophelia.model.Constants.ShutdownHookPriorities;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.ShutdownHook;
import com.thinkparity.ophelia.model.util.Step;
import com.thinkparity.ophelia.model.workspace.impl.WorkspaceImpl;
import com.thinkparity.ophelia.model.workspace.monitor.InitializeStep;

/**
 * WorkspaceModel
 * The workspace structure is as follows:
 * Win32:
 * The root of the workspace in a win32 environment is the %APPDATA% environment
 * variable followed by the corporation property followed by the product
 * property.  An example is as follows:
 * <pre>
 * C:\Documents and Settings\Joe Blow\Application Data\
 * +-->thinkParity
 *    +-->data
 *    +-->index
 *    +-->logs
 * </pre>
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class WorkspaceModel {

    /** A list of workspaces. */
    private static final Map<File, WorkspaceImpl> WORKSPACES;

    static {
        WORKSPACES = new HashMap<File, WorkspaceImpl>(1, 0.75F);
    }

    /**
     * Obtain an instance of a workspace model.
     * 
     * @return A <code>WorkspaceModel</code>.
     */
    public static WorkspaceModel getInstance() {
        return new WorkspaceModel();
    }

    /**
     * Notify a process monitor that a given number of steps is upcoming.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param steps
     *            An <code>Integer</code> number of steps.
     */
    protected static final void notifyDetermine(final ProcessMonitor monitor,
            final Integer steps) {
        monitor.determineSteps(steps);
    }

    /**
     * Notify a process monitor that a given process will begin.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param steps
     *            An <code>Integer</code> number of steps.
     */
    protected static final void notifyProcessBegin(final ProcessMonitor monitor) {
        monitor.beginProcess();
    }

    /**
     * Notify a process monitor that a given process will end.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param steps
     *            An <code>Integer</code> number of steps.
     */
    protected static final void notifyProcessEnd(final ProcessMonitor monitor) {
        monitor.endProcess();
    }

    /**
     * Notify a process monitor that a given step will begin.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param step
     *            A <code>Step</code>.
     */
    protected static final void notifyStepBegin(final ProcessMonitor monitor,
            final Step step) {
        notifyStepBegin(monitor, step, null);
    }

    /**
     * Notify a process monitor that a given step will begin.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param step
     *            A <code>Step</code>.
     * @param data
     *            Any extra step data.
     */
    protected static final void notifyStepBegin(final ProcessMonitor monitor,
            final Step step, final Object data) {
        monitor.beginStep(step, data);
    }

    /**
     * Notify a process monitor that a given step will end.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param step
     *            A <code>Step</code>.
     */
    protected static final void notifyStepEnd(final ProcessMonitor monitor,
            final Step step) {
        monitor.endStep(step);
    }

    /**
     * Create a WorkspaceModelImpl.
     * 
     */
    protected WorkspaceModel() {
        super();
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
     * be deleted. The monitor will allow the client to capture the progress of
     * the initialization process.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code> provides progress feedback on
     *            the initialization process.
     * @param workspace
     *            A <code>Workspace</code>.
     * @param credentials
     *            The user's login <code>Credentials</code>.
     */
    public void initialize(final ProcessMonitor monitor,
            final InitializeMediator mediator, final Workspace workspace,
            final Credentials credentials) {
        final WorkspaceImpl workspaceImpl = findImpl(workspace);
        notifyProcessBegin(monitor);
        notifyDetermine(monitor, 3);
        try {
            // begin initialization
            notifyStepBegin(monitor, InitializeStep.PERSISTENCE_INITIALIZE);
            workspaceImpl.beginInitialize();
            notifyStepEnd(monitor, InitializeStep.PERSISTENCE_INITIALIZE);
            // finish initialization
            workspaceImpl.finishInitialize();
            notifyProcessEnd(monitor);
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
            throw panic(t);
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
     * Panic. Nothing can be done about the error that has been generated. An
     * appropriate error is constructed suitable for throwing beyond the model
     * interface.
     * 
     * @param t
     *            A <code>Throwable</code>.
     * @return A <code>RuntimeException</code>.
     */
    protected RuntimeException panic(final Throwable t) {
        if (ThinkParityException.class.isAssignableFrom(t.getClass())) {
            return (ThinkParityException) t;
        } else if (Assertion.class.isAssignableFrom(t.getClass())) {
            return (Assertion) t;
        }
        else {
            final String errorId = new ErrorHelper().getErrorId(t);
            return new ThinkParityException(errorId.toString(), t);
        }
    }
}
