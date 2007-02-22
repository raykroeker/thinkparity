/*
 * Created On: Sep 5, 2006 4:00:58 PM
 */
package com.thinkparity.ophelia.browser.util.jdic;

import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.print.PrintService;

import org.jdesktop.jdic.desktop.Desktop;
import org.jdesktop.jdic.desktop.DesktopException;

import com.thinkparity.codebase.OSUtil;

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
     * Launches the system default browser to show the given URL.
     * 
     * @param urlString
     *            A URL <code>String</code>.
     */
    public static void browse(final String urlString) throws DesktopException {
        final URL url = getUrl(urlString);
        Desktop.browse(url);
    }

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
     * Determine if the file is printable.
     * 
     * @param file
     *            A <code>File</code>.
     * @return true if the file is printable; false otherwise.
     */
    public static boolean isPrintable(final File file) {
        return Desktop.isPrintable(file);
    }

    /**
     * Determine if there is a printer.
     * 
     * @return true if there is a printer attached; false otherwise.
     */
    public static boolean isPrintServiceAvailable() {
        final PrintService[] printServices = PrinterJob.lookupPrintServices();
        return printServices.length > 0;
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
     * Convert a string to a URL.
     * 
     * @param urlString
     *            A URL <code>String</code>.
     * @return A java <code>URL</code>.
     * @throws DesktopException
     */
    private static URL getUrl(final String urlString) throws DesktopException {
        final URL url;
        try {
            url = new URL(urlString);
        } catch (final MalformedURLException mux) {
            throw new DesktopException("The given URL is invalid.");
        }
        return url;
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
