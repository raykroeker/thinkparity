/*
 * Created On: Fri May 26 2006 16:27 PDT
 * $Id$
 */
package com.thinkparity.browser;

import java.io.File;
import java.text.MessageFormat;

/**
 * Browser constants.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public final class Constants {

    /** Java constants. */
    public static final class Java {

        /** Class path option. */
        public static final String OPTION_CLASS_PATH = "-cp";

        public static final String OPTION_CLASS_PATH_VALUE =
                new StringBuffer(".").append(File.separator)
                .append("thinkParity.jar")
                .toString();

        /** Executable file. */
        public static final String EXECUTABLE =
                new StringBuffer(System.getProperty("java.home"))
                .append(File.separator).append("bin")
                .append(File.separator).append("javaw")
                .toString();

        public static final String OPTION_PARITY_INSTALL =
                new StringBuffer("-Dparity.install=")
                .append(System.getProperty("parity.install"))
                .toString();

        public static final MessageFormat OPTION_PARITY_SERVER_HOST =
                new MessageFormat("-Dparity.serverhost={0}");

        public static final MessageFormat OPTION_PARITY_SERVER_PORT =
                new MessageFormat("-Dparity.serverport={0}");

        public static final String OPTION_PARITY_WORKSPACE =
                new StringBuffer("-Dparity.workspace=")
                .append(System.getProperty("parity.workspace"))
                .toString();

        public static final String MAIN_CLASS = "com.thinkparity.ThinkParity";

    }

    /** Parity directories. */
    public static final class Directories {

        /** System property parity.install. */
        public static final File PARITY_INSTALL =
            new File(System.getProperty("parity.install"));
    }
}
