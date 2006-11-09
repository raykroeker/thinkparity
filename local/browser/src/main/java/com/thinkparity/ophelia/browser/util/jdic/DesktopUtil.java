/*
 * Created On: Sep 5, 2006 4:00:58 PM
 */
package com.thinkparity.ophelia.browser.util.jdic;

import java.io.File;
import java.io.IOException;

import com.thinkparity.codebase.OSUtil;

import org.jdesktop.jdic.desktop.Desktop;
import org.jdesktop.jdic.desktop.DesktopException;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DesktopUtil {

    /** Windows wrapper dll. */
    private static final String OPEN_PARAM_WIN32_DLL = "rundll32.exe";

    /**
     * The dll to call; and the api to execute. This will automatically provide
     * the open-with dialogue if no application is associated with the file.
     */
    private static final String OPEN_PARAM_WIN32_PROTOCOL = "shell32.dll,ShellExec_RunDLL";

    /**
     * Open a file.
     * 
     * @param file
     *            A java <code>File</code>.
     * @throws DesktopException
     */
    public static void open(final File file) throws DesktopException {
        try {
            Desktop.open(file);
        } catch (final DesktopException dx) {
            if ("No application associated with the specified file".equals(dx.getMessage()) && OSUtil.isWindows()) {
                try {
                    openWindows(file);
                } catch (final IOException iox) {
                    throw new DesktopException(iox.getMessage());
                }
            }
        }
    }

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

    /**
     * Open the file on windows. This will attempt to open the file using the
     * shell32 dll.
     * 
     * @param file
     *            A <code>File</code>.
     */
    private static void openWindows(final File file) throws IOException {
        Runtime.getRuntime().exec(new String[] { OPEN_PARAM_WIN32_DLL,
                OPEN_PARAM_WIN32_PROTOCOL, file.getAbsolutePath() });        
    }
}
