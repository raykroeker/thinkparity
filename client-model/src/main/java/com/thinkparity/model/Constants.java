/*
 * Sat Apr 29 10:22:32 PDT 2006
 */
package com.thinkparity.model;

import java.io.File;

/**
 * The parity local model constants.
 *
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public final class Constants extends com.thinkparity.codebase.Constants {

    public static final class Connection {
        public static final String SERVER_HOST = System.getProperty("parity.serverhost");
        public static final Integer SERVER_PORT = Integer.getInteger("parity.serverport");
    }

    public static final class Directories extends
            com.thinkparity.codebase.Constants.Local.Directories {
        public static final File ARCHIVE = new File(System.getProperty("parity.archive.directory"));
        public static final File DOWNLOAD = new File(com.thinkparity.codebase.Constants.Local.Directories.INSTALL, DirectoryNames.DOWNLOAD);
    }

    public static final class DirectoryNames {
        public static final String BIN = "bin";
        public static final String CORE = "core";
        public static final String DOWNLOAD = ".download";
        public static final String LIB = "lib";
        public static final String LIB_NATIVE_WIN32 = "win32";
    }

    public static final class Image {
        public static final String MAIN = "com.thinkparity.browser.Browser";
        public static final String MAIN_ARGS = "";
        public static final String PROPERTIES_FILENAME = "thinkParityImage.properties";
    }

    public static final class LibraryFileExtensions {
        public static final String JAVA = ".jar";
        public static final String NATIVE = ".dll";
    }

    public static final class MetaData {
        public static final String RELEASE_ID_KEY = "com.thinkparity.model.parity.releaseId";
        public static final Long RELEASE_ID_PK = 1000L;
    }

    public static final class Preferences {
        public static final class Properties {
            public static final String LAST_RUN = "com.thinkparity.parity.model.lastRun";
            public static final String PASSWORD = "parity.password";
        }
    }

    public static final class Release {
        public static final String ARTIFACT_ID = "lBrowser";
        public static final String GROUP_ID = "com.thinkparity.parity";
        public static final String VERSION = "1.0.0-RC4";
    }

    public static final class Sundry {
        public static final String THINKPARITY_GROUP_ID = "com.thinkparity.parity";
    }
}
