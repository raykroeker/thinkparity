/*
 * Created On: Fri Jun 02 2006 15:50 PDT
 * $Id$
 */
package com.thinkparity.ophelia.browser.profile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.browser.Constants.DirectoryNames;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;

/**
 * <b>Title:</b>thinkParity Ophelia UI Profile Manager<br>
 * <b>Description:</b>Used to select a profile.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ProfileManager {

    /**
     * Initialize the profile file system.
     * 
     * @return A <code>FileSystem</code>.
     */
    public static FileSystem initProfileFileSystem(final Environment environment) {
        final File fileSystemRoot;
        if (null != System.getProperty("thinkparity.profile.root")) {
            fileSystemRoot = new File(System.getProperty("thinkparity.profile.root"));
        } else {
            final String suffix;
            switch (environment) {
            case DEMO:
                suffix = "Demo";
                break;
            case DEMO_LOCALHOST:
                suffix = "Demo-Localhost";
                break;
            case DEVELOPMENT:
                suffix = "Development";
                break;
            case DEVELOPMENT_LOCALHOST:
                suffix = "Development-Localhost";
                break;
            case DEVELOPMENT_TESTING:
                suffix = "Testing";
                break;
            case DEVELOPMENT_TESTING_LOCALHOST:
                suffix = "Testing-Localhost";
                break;
            case PRODUCTION:
                suffix = null;
                break;
            default:
                throw Assert.createUnreachable("Unknown environment {0}.", environment);
            }
            switch(OSUtil.getOS()) {
                case WINDOWS_XP:
                case WINDOWS_VISTA:
                    final StringBuffer win32Path = new StringBuffer()
                            .append(System.getenv("APPDATA"))
                            .append(File.separatorChar).append("thinkParity");
                    if (null != suffix) {
                        win32Path.append(' ')
                            .append(suffix);
                    }

                    fileSystemRoot = new File(win32Path.toString());
                    break;
                case LINUX:
                    final StringBuffer linuxPath = new StringBuffer()
                            .append(System.getenv("HOME"))
                            .append(File.separatorChar).append(".thinkParity");
                    if (null != suffix) {
                        linuxPath.append(' ')
                            .append(suffix);
                    }

                    fileSystemRoot = new File(linuxPath.toString());
                    break;
                default:
                    throw Assert.createUnreachable("Unsupported operating system.");
            }
        }
        if(!fileSystemRoot.exists()) {
            Assert.assertTrue(fileSystemRoot.mkdirs(),
                    "Cannot create data directory {0}.", fileSystemRoot);
        }
        return new FileSystem(fileSystemRoot);
    }

    /** An auto selection flag. */
    private final Boolean autoselect;

    /** The profile manager dialog. */
    private ProfileManagerDialog dialog;

    /** The <code>Environment</code>. */
    private final Environment environment;

    /** The profile manager avatar. */
    private ProfileManagerAvatar managerAvatar;

    /** The profile manager window. */
    private ProfileManagerWindow window;

    /**
     * Create ProfileManager.
     * 
     * @param autoselect
     *            A <code>Boolean</code>.
     * @param environment
     *            An <code>Environment</code>.
     */
    public ProfileManager(final Boolean autoselect, final Environment environment) {
        super();
        this.environment = environment;
        this.autoselect = autoselect;
    }

    /**
     * Prompt the user to select a profile.
     *
     * @return A profile.
     */
    public Profile select() {
        final Profile profile;
        if (autoselect) {
            profile = readDefaultProfile();
        } else {
            managerAvatar = new ProfileManagerAvatar(this);
            managerAvatar.setInput(readProfiles());
            openWindow(managerAvatar.getTitle(), managerAvatar);
            profile = managerAvatar.getSelectedProfile();
        }
        /* no selection was made */
        if (null != profile) {
            profile.setLastModified();
        }
        return profile;
    }

    /**
     * Create a profile.
     *
     */
    void create() {
        final CreateProfileAvatar avatar = new CreateProfileAvatar(this);
        avatar.setInput(readProfiles());
        avatar.reload();
        openDialog(avatar.getTitle(), avatar);

        // create the profile
        final String profileName = avatar.getProfileName();
        if(null != profileName) {
            final File profileRoot = new File(initProfileFileSystem(environment).getRoot(), avatar.getProfileName());
            Assert.assertTrue(
                    "[LBROWSER] [PROFILE MANAGER] [CREATE] [CANNOT CREATE PROFILE ROOT]",
                    profileRoot.mkdir());
        }

        reloadManager(profileName);
    }

    /**
     * Delete the profile selected in the manager.
     *
     */
    void delete() {
        final Profile selectedProfile = managerAvatar.getSelectedProfile();
        final DeleteProfileAvatar avatar = new DeleteProfileAvatar(this);
        avatar.setInput(selectedProfile);
        openDialog(avatar.getTitle(), avatar);

        final Boolean doDelete = avatar.doDelete();
        if(doDelete) { selectedProfile.delete(); }

        reloadManager();
    }

    /**
     * Rename the profile selected in the manager.
     *
     */
    void rename() {
        final Profile selectedProfile = managerAvatar.getSelectedProfile();
        final RenameProfileAvatar avatar = new RenameProfileAvatar(this);
        final Data data = new Data(2);
        data.set(RenameProfileAvatar.DataKey.ALL_PROFILES, readProfiles());
        data.set(RenameProfileAvatar.DataKey.SELECTED_PROFILE, selectedProfile);
        avatar.setInput(data);
        openDialog(avatar.getTitle(), avatar);

        final String name = avatar.getProfileName();
        if(null != name) { selectedProfile.rename(name); }

        reloadManager();
    }

    /**
     * Create a new manager dialog and open the avatar.
     * 
     * @param avatar
     *            An avatar to display.
     */
    private void openDialog(final String title, final Avatar avatar) {
        dialog = new ProfileManagerDialog(window);
        dialog.open(title, avatar);
    }

    /**
     * Create a new manager window and open the avatar.
     * 
     * @param avatar
     *            An avatar to open.
     */
    private void openWindow(final String title, final Avatar avatar) {
        window = new ProfileManagerWindow();
        window.initComponents(title, avatar);
        window.setVisibleAndWait();
    }

    /**
     * Read the default profile from the file system.
     * 
     * @return The default profile.
     */
    private Profile readDefaultProfile() {
        final File defaultRoot = new File(initProfileFileSystem(environment).getRoot(), DirectoryNames.DEFAULT_PROFILE);
        if(!defaultRoot.exists()) {
            Assert.assertTrue("[LBROWSER] [PROFILE MANAGER] [CANNOT CREATE DEFAULT PROFILE]",
                    defaultRoot.mkdir());
        }
        final Profile profile = new Profile();
        profile.setFileSystem(new FileSystem(defaultRoot));
        return profile;
    }

    /**
     * Read the profiles.
     * 
     * @return A list of profiles.
     */
    private List<Profile> readProfiles() {
        final FileSystem fileSystem = initProfileFileSystem(environment);
        final File[] profileDirectories = fileSystem.listDirectories("/");
        final List<Profile> profiles = new ArrayList<Profile>();
        Profile profile;
        for(final File profileDirectory : profileDirectories) {
            profile = new Profile();
            profile.setFileSystem(new FileSystem(profileDirectory));
            profiles.add(profile);
        }
        if(0 == profiles.size()) { profiles.add(readDefaultProfile()); }
        return profiles;
    }

    /** Re-read the profiles and update the manager. */
    private void reloadManager() {
        managerAvatar.setInput(readProfiles());
        managerAvatar.reload();
    }

    /**
     * Re-read the profiles and update the manager; also select the profile.
     * 
     * @param profileName
     *            A profile name.
     */
    private void reloadManager(final String profileName) {
        final List<Profile> profiles = readProfiles();
        managerAvatar.setInput(profiles);
        for(final Profile profile : profiles) {
            if(profile.getName().equals(profileName)) {
                managerAvatar.reload(profile);
                break;
            }
        }
    }
}
