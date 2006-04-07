/*
 * Apr 6, 2006
 */
package com.thinkparity.model.parity.model;

import java.io.File;

import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.IParityModelConstants;
import com.thinkparity.model.parity.util.SystemUtil;


/**
 * The parity model initializer. The initialize api is called prior to invoking
 * any api in the lModel. This means that ***NO API*** should be called from
 * here.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 * 
 * @see AbstractModel
 */
class ModelInitialiser {

    /**
     * Create a ModelInitialiser.
     * 
     */
    ModelInitialiser() { super(); }

    /**
     * Intialize the user's environment for use with the thinkParity lModel. We
     * create the following directories:
     * <ul>
     * <li>${root}/thinkParity/Archive
     * <li>${root}/thinkParity/Export
     * </ul>
     * 
     * Where ${root} is either "My Documents" on windows XP\2K and the home
     * direcotry on linux.
     */
    void initialize() {
        final File root;
        switch(OSUtil.getOS()) {
        case WINDOWS_XP:
        case WINDOWS_2000:
            final File user = new File(SystemUtil.getenv("USERPROFILE"));
            root = new File(user, "My Documents");
            break;
        case LINUX:
            root = new File(SystemUtil.getenv("HOME"));
            break;
        default:
            throw Assert.createUnreachable("[LMODEL] [INITIALISE]");
        }

        final File thinkParity = new File(root, IParityModelConstants.ROOT_DIRECTORY);
        if(!thinkParity.exists()) { Assert.assertTrue("", thinkParity.mkdir()); }

        final File archive = new File(thinkParity, IParityModelConstants.ARCHIVE_DIRECTORY);
        if(!thinkParity.exists()) { Assert.assertTrue("", archive.mkdir()); }

        final File export = new File(thinkParity, IParityModelConstants.EXPORT_DIRECTORY);
        if(!thinkParity.exists()) { Assert.assertTrue("", export.mkdir()); }

        System.setProperty(
                "parity.archive.directory",
                archive.getAbsolutePath());
    }
}
