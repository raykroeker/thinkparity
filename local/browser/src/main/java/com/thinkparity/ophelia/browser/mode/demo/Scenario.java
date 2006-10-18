/*
 * Created On: 13-Oct-06 5:15:31 PM
 */
package com.thinkparity.ophelia.browser.mode.demo;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.ophelia.browser.profile.ProfileManager;
import com.thinkparity.ophelia.model.script.Script;
import com.thinkparity.ophelia.model.script.ScriptModel;
import com.thinkparity.ophelia.model.session.DefaultLoginMonitor;
import com.thinkparity.ophelia.model.session.SessionModel;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Scenario {

    /** A list of script <code>Credential</code>s. */
    private final Map<Script, Credentials> credentials;

    /** The scenario display name <code>String</code>. */
    private String displayName;

    /** The demo <code>Environment</code>. */
    private final Environment environment;

    /** The scenario name <code>String<code>. */
    private String name;

    /** The profile <code>FileSystem</code>. */
    private final FileSystem profileFileSystem;

    /** A list of script profile directory <code>File</code>s. */
    private final Map<Script, File> profiles;

    /** A list of <code>Script</code>s. */
    private final List<Script> scripts;

    /** A list of script <code>Workspace</code>s. */
    private final Map<Script, Workspace> workspaces;

    /**
     * Create Scenario.
     *
     */
    Scenario(final Environment environment) {
        super();
        this.credentials = new HashMap<Script, Credentials>(7, 0.75F);
        this.environment = environment;
        this.profileFileSystem = ProfileManager.initProfileFileSystem();
        this.profiles = new HashMap<Script, File>();
        this.scripts = new ArrayList<Script>();
        this.workspaces = new HashMap<Script, Workspace>();
    }

    /**
     * Add a script to the scenario.
     * 
     * @param script
     *            A demo scenario <code>Script</code>.
     * @param credentials
     *            The script's <code>Credentials</code>.
     * @return Whether or not the list of scripts was modified.
     */
    boolean addScript(final Script script, final Credentials credentials) {
        final Boolean modified = scripts.add(script);
        this.credentials.put(script, credentials);
        return modified; 
    }

    /**
     * Execute the scenario.
     *
     */
    void execute(final ExecutionMonitor monitor) {
        // initialize the workspaces
        for (final Script script : scripts) {
            initializeProfile(script);
            initializeWorkspace(script);
            logout(script);
        }

        // execute the scripts
        for (final Script script : scripts) {
            login(script);
            try {
                execute(script);
            } catch (final Throwable t) {
                logout(script);
                monitor.notifyScriptError(script, t);
                break;
            }
            logout(script);
        }
    }

    /**
     * Obtain the credentials for a script.
     * 
     * @param script
     *            A thinkParity <code>Script</code>.
     * @return A thinkParity <code>Credentials</code>.
     */
    Credentials getCredentials(final Script script) {
        return credentials.get(script);
    }

    /**
     * Obtian the demo scenario display name.
     * 
     * @return A display name <code>String</code>.
     */
    String getDisplayName() {
        return displayName;
    }

    /**
     * Obtain the environment
     *
     * @return The Environment.
     */
    Environment getEnvironment() {
        return environment;
    }

    /**
     * Obtain the demo scenario name.
     * 
     * @return A name <code>String</code>.
     */
    String getName() {
        return name;
    }

    /**
     * Obtain the demo scenario scripts.
     * 
     * @return A <code>List&lt;Script&gt;</code>.
     */
    List<Script> getScripts() {
        return Collections.unmodifiableList(scripts);
    }

    /**
     * Remove a script from the scenario.
     * 
     * @param script
     *            A demo scenario <code>Script</code>.
     * @return Whether or not the list of scripts was modified.
     */
    boolean removeScript(final Script script) {
        final Boolean modified = scripts.remove(script);
        this.credentials.remove(script);
        return modified; 
    }

    /**
     * Set the scenario display name.
     * 
     * @param displayName
     *            A display name <code>String</code>.
     */
    void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Set the scenario name.
     * 
     * @param name
     *            A name <code>String</code>.
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * Execute a script.
     * 
     * @param script
     *            A <code>Script</code>.
     */
    private void execute(final Script script) {
        final List<Script> scripts = new ArrayList<Script>(1);
        scripts.add(script);
        ScriptModel.getModel(environment, workspaces.get(script)).execute(scripts);
    }

    /**
     * Obtain the profile path for the script.
     * 
     * @param script
     *            A <code>Script</code>.
     * @return A profile path <code>String</code>.
     */
    private String getProfilePath(final Script script) {
        return MessageFormat.format("/{0}_{1}",
                displayName, credentials.get(script).getUsername());
    }

    /**
     * Initialize the profile for a script.
     * 
     * @param script
     *            A <code>Script</code>.
     */
    private void initializeProfile(final Script script) {
        final File profileDirectory =
            profileFileSystem.findDirectory(getProfilePath(script));
        if (null == profileDirectory) {
            profiles.put(script,
                    profileFileSystem.createDirectory(getProfilePath(script)));
        } else {
            if (profileDirectory.exists()) {
                FileUtil.deleteTree(profileDirectory);
            }
            profiles.put(script,
                    profileFileSystem.createDirectory(getProfilePath(script))); 
        }
    }

    /**
     * Initialize a script's workspace.
     * 
     * @param script
     *            A <code>Script</code>.
     */
    private void initializeWorkspace(final Script script) {
        final WorkspaceModel workspaceModel = WorkspaceModel.getModel(environment);
        final Workspace workspace = workspaceModel.getWorkspace(profiles.get(script));
        workspaceModel.initialize(workspace, new DefaultLoginMonitor() {
            @Override
            public Boolean confirmSynchronize() {
                return Boolean.TRUE;
            }
        }, credentials.get(script));
        workspaces.put(script, workspace);
    }

    /**
     * Login a script.
     * 
     * @param script
     *            A <code>Script</code>.
     */
    private void login(final Script script) {
        SessionModel.getModel(environment, workspaces.get(script)).login(new DefaultLoginMonitor());
    }

    /**
     * Logout a script.
     * 
     * @param script
     *            A <code>Script</code>.
     */
    private void logout(final Script script) {
        SessionModel.getModel(environment, workspaces.get(script)).logout();
    }
}
