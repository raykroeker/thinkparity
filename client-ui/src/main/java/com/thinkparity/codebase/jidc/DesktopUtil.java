/*
 * Created On: Sep 5, 2006 4:00:58 PM
 */
package com.thinkparity.codebase.jidc;

import java.io.File;

import org.jdesktop.jdic.desktop.Desktop;
import org.jdesktop.jdic.desktop.DesktopException;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DesktopUtil {

    /**
     * Print a file.
     * 
     * @param file
     *            A java <code>File</code>.
     * @throws DesktopException
     */
    public static void print(final File file) throws DesktopException {
        Desktop.print(file);
    }
}
