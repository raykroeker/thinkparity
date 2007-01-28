/*
 * Created On:  27-Jan-07 7:59:49 PM
 */
package com.thinkparity.ant;

import org.apache.tools.ant.taskdefs.AbstractCvsTask;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class CvsLocator extends AbstractCvsTask {

    /** The cvs locator's cvs command. */
    private static final String COMMAND;

    static {
        COMMAND = "checkout";
    }

    /**
     * Create CVSLocator.
     *
     */
    CvsLocator() {
        super();
    }

    /**
     * Locate a dependency
     * 
     * @param dependency
     *            A <code>Dependency</code>.
     */
    void locate(final Dependency dependency) {
        setCvsRoot(dependency.getProject().getProperty("cvs.cvsroot"));
        setCompressionLevel(Integer.valueOf(
                dependency.getProject().getProperty("cvs.compressionlevel")));
        setTag(dependency.getProject().getProperty("cvs.branch"));
        setDest(dependency.getProject().getBaseDir());
        setCommand(COMMAND);
        final String path = new StringBuffer("vendor/")
            .append(dependency.getPath())
            .toString();
        setPackage(path);
        execute();
    }
}
