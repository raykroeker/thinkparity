/*
 * Created On:  27-Jan-07 7:59:49 PM
 */
package com.thinkparity.antx;

import java.io.File;

import org.apache.tools.ant.taskdefs.AbstractCvsTask;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class CvsLocator extends AbstractCvsTask implements Locator {

    /** The cvs locator's cvs command. */
    private static final String COMMAND;

    static {
        COMMAND = "export";
    }

    /**
     * Create CvsLocator.
     * 
     * @param cvsRoot
     *            A cvs root <code>String</code>.
     * @param compressionLevel
     *            A cvs compression level <code>int</code>.
     * @param tag
     *            A cvs tag <code>String</code>.
     * @param dest
     *            A cvs destination <code>File</code>.
     */
    CvsLocator(final String cvsRoot, final int compressionLevel,
            final String tag, final File dest) {
        super();
        setCvsRoot(cvsRoot);
        setCommand(COMMAND);
        setCompressionLevel(compressionLevel);
        setDest(dest);
        setTag(tag);
    }

    /**
     * Locate a dependency
     * 
     * @param dependencyPath
     *            A dependency path <code>String</code>.
     */
    public void locate(final String dependencyPath) {
        setPackage(new StringBuffer("vendor/")
            .append(dependencyPath).toString());
        execute();
    }
}
