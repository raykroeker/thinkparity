/*
 * Created On:  27-Jan-07 7:59:49 PM
 */
package com.thinkparity.antx;

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
    void locate(final DependencyTask dependencyTask) {
        setCvsRoot(dependencyTask.getProject().getProperty("cvs.cvsroot"));
        setCompressionLevel(Integer.valueOf(
                dependencyTask.getProject().getProperty("cvs.compressionlevel")));
        setTag(dependencyTask.getProject().getProperty("cvs.branch"));
        setDest(dependencyTask.getProject().getBaseDir());
        setCommand(COMMAND);
        final String path = new StringBuffer("vendor/")
            .append(dependencyTask.getPath())
            .toString();
        setPackage(path);
        execute();
    }
}
