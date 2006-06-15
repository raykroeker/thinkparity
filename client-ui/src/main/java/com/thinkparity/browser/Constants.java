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

        public static final MessageFormat OPTION_PARITY_IMAGE = new MessageFormat("-Dparity.image.name={0}");
        public static final MessageFormat OPTION_PARITY_INSTALL = new MessageFormat("-Dparity.install={0}");
        public static final MessageFormat OPTION_PARITY_SERVER_HOST = new MessageFormat("-Dparity.serverhost={0}");
        public static final MessageFormat OPTION_PARITY_SERVER_PORT = new MessageFormat("-Dparity.serverport={0}");
        public static final MessageFormat OPTION_PARITY_WORKSPACE = new MessageFormat("-Dparity.workspace={0}");

        public static final String MAIN_CLASS = "com.thinkparity.ThinkParity";

    }

    /** Parity directories. */
    public static final class Directories {

        /** System property parity.install. */
        public static final File PARITY_INSTALL =
            new File(System.getProperty("parity.install"));

    }

    public static final class DirectoryNames {
        public static final String DEFAULT_PROFILE = "Default";
    }

    public static final class Release {
        public static final String ARTIFACT_ID = "lBrowser";
        public static final String GROUP_ID = "com.thinkparity.parity";
        public static final String VERSION = Version.getVersion();
    }

    public static final class Network {
        public static final Integer TIMEOUT = 750;
        public static final Integer TTL = 0;
    }

    public static final class Session {
        public static final Long RECONNECT_DELAY = 1 * 60 * 1000L;
        public static final Long RECONNECT_PERIOD = 1 * 60 * 1000L;
    }
}
