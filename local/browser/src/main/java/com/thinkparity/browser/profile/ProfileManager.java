/*
 * Created On: Fri Jun 02 2006 15:50 PDT
 * $Id$
 */
package com.thinkparity.browser.profile;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.Mode;
import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.BrowserException;
import com.thinkparity.browser.Version;
import com.thinkparity.browser.Constants.DirectoryNames;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;

public class ProfileManager {

    /** The profile manager dialog. */
    private ProfileManagerDialog dialog;

    /** The profile manager avatar. */
    private ProfileManagerAvatar managerAvatar;

    /** The profile manager window. */
    private ProfileManagerWindow window;

    /** Create ProfileManager. */
    public ProfileManager() { super(); }

    /**
     * Prompt the user to select a profile.
     *
     * @return A profile.
     */
    public Profile select() {
        if(Mode.TESTING == Version.getMode() || Mode.DEVELOPMENT == Version.getMode()) {
            managerAvatar = new ProfileManagerAvatar(this);
            managerAvatar.setInput(readProfiles());
            openWindow(managerAvatar.getTitle(), managerAvatar);
    
            return managerAvatar.getSelectedProfile().setLastModified();
        }
        else { return readDefaultProfile().setLastModified(); }
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
            final File profileRoot = new File(initFileSystem().getRoot(), avatar.getProfileName());
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
     * Initialize the profile file system.
     * 
     * @return A file system.
     */
    private FileSystem initFileSystem() {
        final File fileSystemRoot;
        switch(OSUtil.getOS()) {
            case WINDOWS_2000:
            case WINDOWS_XP:
                final StringBuffer win32Path = new StringBuffer()
                        .append(System.getenv("APPDATA"))
                        .append(File.separatorChar).append("thinkParity");
                fileSystemRoot = new File(win32Path.toString());
                break;
            case LINUX:
                final StringBuffer linuxPath = new StringBuffer()
                        .append(System.getenv("HOME"))
                        .append(File.separatorChar).append("thinkParity");
                fileSystemRoot = new File(linuxPath.toString());
                break;
            default:
                throw Assert.createUnreachable("[LBROWSER] [PROFILE MANAGER] [UNUSPPORTED OS]");
        }
        if(!fileSystemRoot.exists()) {
            Assert.assertTrue("[LBROWSER] [PROFILE MANAGER] [CANNOT CREATE PROFILE FILESYSTEM ROOT]",
                    fileSystemRoot.mkdirs());
        }
        return new FileSystem(fileSystemRoot);
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
        window.addWindowListener(new WindowAdapter() {
            public void windowClosed(final WindowEvent e) {
                synchronized(window) { window.notifyAll(); }
            }
        });
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() { window.open(title, avatar); }
            });
        }
        catch(final InterruptedException ix) { throw new BrowserException("", ix); }
        catch(final InvocationTargetException itx) { throw new BrowserException("", itx); }
        synchronized(window) {
            try { window.wait(); }
            catch(final InterruptedException ix) { throw new BrowserException("", ix); }
        }
    }

    /**
     * Read the default profile from the file system.
     * 
     * @return The default profile.
     */
    private Profile readDefaultProfile() {
        final File defaultRoot = new File(initFileSystem().getRoot(), DirectoryNames.DEFAULT_PROFILE);
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
        final FileSystem fileSystem = initFileSystem();
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
