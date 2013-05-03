/*
 * Created On: Sep 5, 2006 4:00:58 PM
 */
package com.thinkparity.ophelia.browser.util.swing;

import java.awt.Desktop;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DesktopUtil {

    /** An awt desktop reference. */
    private static Desktop desktop;

    /** Windows wrapper dll. */
    private static final String OPEN_PARAM_WIN32_DLL = "rundll32.exe";

    /**
     * The dll to call; and the api to execute. This will automatically provide
     * the open-with dialogue if no application is associated with the file.
     */
    private static final String OPEN_PARAM_WIN32_PROTOCOL = "shell32.dll,ShellExec_RunDLL";

    /**
     * Launches the system default browser to show the given URL.
     * 
     * @param url
     *            A URL <code>String</code>.
     */
    public static void browse(final String url) throws URISyntaxException, IOException {
        getDesktop().browse(new URL(url).toURI());
    }

    /**
     * Determine if the file is printable.
     * 
     * @param file
     *            A <code>File</code>.
     * @return true if the file is printable; false otherwise.
     */
    public static boolean isPrintable(final File file) {
        return getDesktop().isSupported(Desktop.Action.PRINT);
    }

    /**
     * Determine if there is a printer.
     * 
     * @return true if there is a printer attached; false otherwise.
     */
    public static boolean isPrintServiceAvailable() {
        return 0 < PrinterJob.lookupPrintServices().length;
    }

    /**
     * Open a file.
     * 
     * @param file
     *            A java <code>File</code>.
     * @throws DesktopException
     */
    public static void open(final File file) throws IOException {
        try {
            getDesktop().open(file);
        } catch (final IOException iox) {
            switch (OSUtil.getOS()) {
            case LINUX:         /* deliberate fall-through */
            case MAC_OSX:
                throw iox;
            case WINDOWS_VISTA: /* deliberate fall-through */
            case WINDOWS_XP:
                openWindows(file);
                break;
            default:
                Assert.assertUnreachable("Unknown os {0}.", OSUtil.getOS());
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
    public static void print(final File file) throws IOException {
        getDesktop().print(file);
    }

    /**
     * Obtain an awt desktop reference. If the reference cannot be obtained an
     * illegal operation exception is thrown.
     * 
     * @return A <code>Desktop</code>.
     */
    private static Desktop getDesktop() {
        if (null == desktop) {
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            } else {
                throw new UnsupportedOperationException(
                        "Desktop api is not available.");
            }
        }
        return desktop;
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
