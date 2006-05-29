/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.model.parity.model.install;

import java.io.File;
import java.text.MessageFormat;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.Constants.Directories;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.workspace.Workspace;

import com.thinkparity.migrator.Release;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class InstallModelImpl extends AbstractModelImpl {

    /** Create InstallModelImpl. */
    InstallModelImpl(final Workspace workspace) { super(workspace); }

    /**
     * Install a release.
     * 
     * @param release
     *            A release.
     */
    void install(final Release release) throws ParityException {
        logger.info("[LMODEL] [INSTALL MODEL] [INSTALL]");
        logger.debug(release);

        final File installRoot = new File(Directories.INSTALL, release.getVersion());
        final File downloadRoot = new File(Directories.DOWNLOAD, release.getVersion());

        // rename the download
        Assert.assertTrue(
                MessageFormat.format(
                        "[LMODEL] [INSTALL MODEL] [INSTALL] [CANNOT RENAME DOWNLOAD TO INSTALL] [{0}] [{1}]",
                        new Object[] {downloadRoot.getAbsolutePath(), installRoot.getAbsolutePath()}),
                downloadRoot.renameTo(installRoot));
    }
}
