/*
 * Created On: Fri May 26 2006 16:27 PDT
 */
package com.thinkparity.browser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.MessageFormat;

import com.thinkparity.browser.platform.util.ImageIOUtil;

/**
 * Browser constants.
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.7
 */
public final class Constants {

    /** Colour constants. */
    public static final class Colors {
        public static final class BrowserStatus {
            public static final Color BG_GRAD_FINISH = new Color(183, 190, 196, 255);
            public static final Color BG_GRAD_START = Color.WHITE;
            public static final Color TOP_BORDER = new Color(92, 102, 127, 255);
        }
        public static final class BrowserTitle {
            public static final Color BG_GRAD_FINISH = new Color(192, 197, 205, 255);
            public static final Color BG_GRAD_START = new Color(251, 252, 252, 255);
            public static final Color SEARCH_BACKGROUND = new Color(237, 241, 244, 255);
            public static final Color SEARCH_OUTLINE = new Color(204, 215, 226, 255);
            public static final Color SIGN_UP_BACKGROUND = new Color(255, 199, 60, 70);
        }
    }

    /** Dimension constatns. */
    public static final class Dimensions {
        public static final class BrowserWindow {
            public static final Dimension MIN_SIZE = new Dimension(450, 587);
        }
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

    public static final class Images {
        public static final class BrowserTitle {
            public static final BufferedImage LOGO =
                    ImageIOUtil.read("BrowserTitle_Logo.png");
        }
    }

    /** Java constants. */
    public static final class Java {

        /** Executable file. */
        public static final String EXECUTABLE =
                new StringBuffer(System.getProperty("java.home"))
                .append(File.separator).append("bin")
                .append(File.separator).append("javaw")
                .toString();

        public static final String MAIN_CLASS = "com.thinkparity.ThinkParity";

        /** Class path option. */
        public static final String OPTION_CLASS_PATH = "-cp";

        public static final String OPTION_CLASS_PATH_VALUE =
                new StringBuffer(".").append(File.separator)
                .append("thinkParity.jar")
                .toString();
        public static final MessageFormat OPTION_PARITY_IMAGE = new MessageFormat("-Dparity.image.name={0}");
        public static final MessageFormat OPTION_PARITY_INSTALL = new MessageFormat("-Dparity.install={0}");
        public static final MessageFormat OPTION_PARITY_SERVER_HOST = new MessageFormat("-Dparity.serverhost={0}");
        public static final MessageFormat OPTION_PARITY_SERVER_PORT = new MessageFormat("-Dparity.serverport={0}");

        public static final MessageFormat OPTION_PARITY_WORKSPACE = new MessageFormat("-Dparity.workspace={0}");

    }

    public static final class Network {
        public static final Integer TIMEOUT = 750;
        public static final Integer TTL = 0;
    }

    public static final class Release {
        public static final String ARTIFACT_ID = "lBrowser";
        public static final String GROUP_ID = "com.thinkparity.parity";
        public static final String VERSION = Version.getVersion();
    }

    public static final class Session {
        public static final Long RECONNECT_DELAY = 1 * 60 * 1000L;
        public static final Long RECONNECT_PERIOD = 1 * 60 * 1000L;
    }
}
