/*
 * Created On:  27-Jan-07 7:59:49 PM
 */
package com.thinkparity.antx;

import java.io.File;
import java.text.MessageFormat;

import org.apache.tools.ant.Project;
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
     * @see com.thinkparity.antx.Locator#locate(java.lang.String,
     *      java.lang.Boolean)
     * 
     */
    public void locate(final String dependencyPath, final Boolean includeSource) {
        setPackage(new StringBuffer("vendor/")
            .append(dependencyPath).toString());
        execute();
        if (includeSource) {
            final String name = resolveName(dependencyPath);
            final String extension = resolveExtension(dependencyPath);
            final String sourcePackage = new StringBuffer("vendor/")
                .append(name).append("-sources").append(extension)
                .toString();
            setPackage(sourcePackage);
            try {
                execute();
            } catch (final Exception x) {
                getProject().log(MessageFormat.format(
                        "Cannot locate dependency source {0}.", sourcePackage),
                        Project.MSG_WARN);
            }
        }
    }

    /**
     * Resolve the path extension for the path including the "dot".
     * 
     * @param path
     *            A path <code>String</code>.
     * @return A path extension <code>String</code>.
     */
    private String resolveExtension(final String path) {
        final int indexOfDot = path.lastIndexOf('.');
        if (-1 == indexOfDot) {
            getProject().log(MessageFormat.format(
                    "Cannot resolve source path extension for dependency {0}.", path),
                    Project.MSG_WARN);
            return null;
        } else {
            return path.substring(indexOfDot);
        }
        
    }

    /**
     * Resolve the path name for the path.
     * 
     * @param path
     *            A path <code>String</code>.
     * @return A path name <code>String</code>.
     */
    private String resolveName(final String path) {
        final int indexOfDot = path.lastIndexOf('.');
        if (-1 == indexOfDot) {
            getProject().log(MessageFormat.format(
                    "Cannot resolve source path name for dependency {0}.", path),
                    Project.MSG_WARN);
            return null;
        } else {
            return path.substring(0, indexOfDot);
        }
    }
}
