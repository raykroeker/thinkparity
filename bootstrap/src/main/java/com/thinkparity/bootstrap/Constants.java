/*
 * Created On: May 25, 2006 10:46:50 PM
 * $Id$
 */
package com.thinkparity.bootstrap;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * <b>Title:</b>thinkParity OpheliaUI Constants<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.6
 */
public final class Constants {

    /** Create Constants. */
    private Constants() { super(); }

    public static final class DateFormats {
        public static final SimpleDateFormat ImageLastRun = new SimpleDateFormat("yyyy MM dd HH:mm:ss.SSS");
        static {
            ImageLastRun.setTimeZone(TimeZones.GMT);
        }
    }

    public static final class Directories {
        public static final class ThinkParity {
            public static final File Directory = new File(System.getProperty(PropertyNames.ThinkParity.Directory));
        }
    }

    public static final class FileNames {
        public static final String ThinkParityImageProperties = "thinkParityImage.properties";
        public static final String ThinkParityProperties = "thinkParity.properties";
        public static final String ThinkParityJar = "thinkParity.jar";
    }

    public static final class Files {
        public static final class ThinkParity {
            public static final File Executable = new File(System.getProperty(PropertyNames.ThinkParity.Executable));
            public static final File Jar = new File(Directories.ThinkParity.Directory, FileNames.ThinkParityJar);
        }
    }

    public static final class PropertyNames {
        public static final class System {
            public static final String JavaLibraryPath = "java.library.path";
        }

        public static final class ThinkParity {
            public static final String Directory = "thinkparity-directory";
            public static final String Environment = "thinkparity.environment";
            public static final String Executable = "thinkparity-executable";
            public static final String Image = "thinkparity.image";
            public static final String ImageClassPath = "thinkparity.image-classpath";
            public static final String ImageExecutable = "thinkparity.image-executable";
            public static final String ImageJVMArgs = "thinkparity.image-jvmargs";
            public static final String ImageLastRun = "thinkparity.image-lastrun";
            public static final String ImageLibraryPath = "thinkparity.image-librarypath";
            public static final String ImageMain = "thinkparity.image-main";
            public static final String ImageMainArgs = "thinkparity.image-mainargs";
            public static final String Mode = "thinkparity.mode";
            public static final String ProductName = "thinkparity.product-name";
            public static final String ReleaseName = "thinkparity.release-name";
            public static final String ReleaseOs = "thinkparity.release-os";
        }
    }

    public static final class Sundry {
        public static final String ThinkParityHeader = "thinkParity:  Configuration";
        public static final String ThinkParityImageHeader = "thinkParity:  Image Configuration";
    }

    public static final class TimeZones {
        public static final TimeZone GMT = new SimpleTimeZone(0, "GMT");
    }
}
