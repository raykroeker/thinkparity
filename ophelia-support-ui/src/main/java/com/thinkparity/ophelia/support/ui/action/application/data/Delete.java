/*
 * Created On:  Nov 19, 2007 9:20:48 AM
 */
package com.thinkparity.ophelia.support.ui.action.application.data;

import com.thinkparity.codebase.FileSystem;

import com.thinkparity.ophelia.browser.profile.ProfileManager;

import com.thinkparity.ophelia.support.ui.Input;
import com.thinkparity.ophelia.support.ui.action.AbstractAction;

/**
 * <b>Title:</b>thinkParity Ophelia Support UI Profile Delete Action<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Delete extends AbstractAction {

    /**
     * Create Delete.
     *
     */
    public Delete() {
        super("/application/data/delete");
    }

    /**
     * @see com.thinkparity.ophelia.support.ui.action.Action#invoke(com.thinkparity.ophelia.support.ui.action.Input)
     *
     */
    @Override
    public void invoke(final Input input) {
        final FileSystem profileFileSystem = getFileSystem();
        try {
            profileFileSystem.deleteTree();
        } catch (final Throwable t) {}
    }

    /**
     * Obtain the file system for the environment.
     * 
     * @param profile
     *            A <code>String</code>.
     * @return A <code>FileSystem</code>.
     */
    private FileSystem getFileSystem() {
        return ProfileManager.initProfileFileSystem(getContext().getEnvironment());
    }
}
