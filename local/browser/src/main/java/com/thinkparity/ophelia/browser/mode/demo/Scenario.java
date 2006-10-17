/*
 * Created On: 13-Oct-06 5:15:31 PM
 */
package com.thinkparity.ophelia.browser.mode.demo;

import java.io.File;
import java.io.FileFilter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

    private final Map<Script, Credentials> credentials;

    private String displayName;

    private Environment environment;

    private String name;

    private final Map<Credentials, File> profiles;

    private String resourcePath;

    private final List<Script> scripts;

    private final Map<Credentials, Workspace> workspaces;

    /**
     * Create Scenario.
     *
     */
    Scenario() {
        super();
        this.credentials = new HashMap<Script, Credentials>(7, 0.75F);
        this.profiles = new HashMap<Credentials, File>();
        this.scripts = new ArrayList<Script>();
        this.workspaces = new HashMap<Credentials, Workspace>();
    }

    public boolean addScript(final Script script, final Credentials credentials) {
        final Boolean modified = scripts.add(script);
        this.credentials.put(script, credentials);
        return modified; 
    }

    public void clearScripts() {
        scripts.clear();
    }

    /**
     * Execute the scenario.
     *
     */
    public void execute() {
        // re-create the profiles
        final FileSystem profileFileSystem = ProfileManager.initProfileFileSystem();
        deleteProfiles(profileFileSystem);
        initializeProfiles(profileFileSystem);

        // initialize the workspaces
        for (final Entry<Credentials, File> entry : profiles.entrySet()) {
            initializeWorkspace(entry.getKey(), entry.getValue());
            logout(entry.getKey());
        }

        // execute the scripts
        for (final Script script : scripts) {
            login(credentials.get(script));
            execute(script);
            logout(credentials.get(script));
        }
    }

    /**
     * Obtain the credentials for a script.
     * 
     * @param script
     *            A thinkParity <code>Script</code>.
     * @return A thinkParity <code>Credentials</code>.
     */
    public Credentials getCredentials(final Script script) {
        return credentials.get(script);
    }

    /**
     * Obtian the display name.
     * 
     * @return A display name <code>String</code>.
     */
    public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public List<Script> getScripts() {
        return Collections.unmodifiableList(scripts);
    }

    public boolean removeScript(final Script script) {
        return scripts.remove(script);
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResourcePath(final String resourcePath) {
        this.resourcePath = resourcePath;
    }

    /**
     * Delete all profiles belonging to this scenario.
     * 
     * @param profileFileSystem
     *            The profile <code>FileSystem</code>.
     */
    private void deleteProfiles(final FileSystem profileFileSystem) {
        final File[] profileDirectories = profileFileSystem.list("/", new FileFilter() {
            public boolean accept(final File pathname) {
                return pathname.getName().startsWith(displayName);
            }
        }, Boolean.FALSE);
        for (final File profileDirectorie : profileDirectories) {
            FileUtil.deleteTree(profileDirectorie);
        }
    }

    /**
     * Execute a script.
     * 
     * @param script
     *            A <code>Script</code>.
     */
    private void execute(final Script script) {
        ScriptModel.getModel(environment,
                workspaces.get(credentials.get(script))).execute(script);
    }

    private String getProfilePath(final Credentials credentials) {
        return MessageFormat.format("/{0}_{1}",
                displayName, credentials.getUsername());
    }

    private void initializeProfile(final FileSystem profileFileSystem,
            final Credentials credentials) {
        final File profileDirectory =
            profileFileSystem.findDirectory(getProfilePath(credentials));
        // the profile can already exist
        if (null == profileDirectory) {
            profiles.put(credentials,
                    profileFileSystem.createDirectory(getProfilePath(credentials)));
        }
    }

    private void initializeProfiles(final FileSystem profileFileSystem) {
        for (final Entry<Script, Credentials> entry : credentials.entrySet()) {
            initializeProfile(profileFileSystem, entry.getValue());
        }
    }

    private void initializeWorkspace(final Credentials credentials, final File profileDirectory) {
        final WorkspaceModel workspaceModel = WorkspaceModel.getModel(environment);
        final Workspace workspace = workspaceModel.getWorkspace(profileDirectory);
        workspaceModel.initialize(workspace, new DefaultLoginMonitor(), credentials);
        workspaces.put(credentials, workspace);
    }

    private void login(final Credentials credentials) {
        SessionModel.getModel(environment, workspaces.get(credentials)).login(new DefaultLoginMonitor());
    }

    private void logout(final Credentials credentials) {
        SessionModel.getModel(environment, workspaces.get(credentials)).logout();
    }
}
