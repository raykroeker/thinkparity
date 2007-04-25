/*
 * Created On: Sat Apr 29 10:22:32 PDT 2006
 */
package com.thinkparity.ophelia.model;

import java.io.File;

/**
 * <b>Title:</b>thinkParity OpheliaModel Constants<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.20
 */
public final class Constants {
    public static final class Directories {
        public static final class ThinkParity {
            public static final File DIRECTORY = new File(System.getProperty(PropertyNames.ThinkParity.DIRECTORY));
        }
    }
    public static final class DirectoryNames {
        public static final String BIN = "bin";
        public static final String CORE = "core";
        public static final String LIB = "lib";
        public static final String LIB_NATIVE_WIN32 = "win32";
        public static final class Workspace {
            public static final String DATA = "data";
            public static final String INDEX = "index";
            public static final String LOG = "logs";
            public static final String TEMP = ".temp";
            public static final String DOWNLOAD = "download";
            public static final class Data {
                public static final String DB = "db.io";
                public static final String LOCAL = "local";
            }
        }
    }
    public static final class FileNames {
        public static final class Install {
            public static final String PROPERTIES = "thinkParity.properties";
        }
        public static final class Workspace {
            public static final class Data {
                public static final String DB = "db";
            }
            public static final class Logging {
                public static final String LOGFILE = "thinkParity.log";
            }
        }
    }
    public static final class MetaData {
        public static final String RELEASE_ID_KEY = "thinkparity.release-name";
        public static final Long RELEASE_ID_PK = 1000L;
    }
    public static final class Product {
        public static final String NAME = System.getProperty("thinkparity.product-name");
        public static final class Features {
            public static final String BACKUP = "BACKUP";
            public static final String CORE = "CORE";
        }
    }
    public static final class PropertyNames {
        public static final class ThinkParity {
            public static final String DIRECTORY = "thinkparity-directory";
        }
    }
    public static final class Release {
        public static final String NAME = System.getProperty("thinkparity.release-name");
    }
    public static final class ShutdownHookNames {
        public static final String HYPERSONIC = "thinkParity Hypersonic Database";
        public static final String WORKSPACE_CLOSE = "thinkParity Workspace - Close";
        public static final String WORKSPACE_DELETE = "thinkParity Workspace - Delete";
    }
    public static final class ShutdownHookPriorities {
        public static final Integer DEFAULT = Integer.valueOf(4);
        public static final Integer WORKSPACE_DELETE = Integer.valueOf(9);
    }
    public static final class ThreadNames {
        public static final String SHUTDOWN_HOOK = "thinkParity Shutdown Hook";
    }
    public static final class Versioning {
        public static final Long INCREMENT = 1L;
        public static final Long START = 1L;
    }
    public static final class Xml {
        public static final class All {
            public static final String EXECUTED_ON = "executedOn";
        }
        public static final class Artifact {
            public static final String BYTES = "bytes";
            public static final String CHECKSUM = "checksum";
            public static final String NAME = "name";
            public static final String TYPE = "type";
            public static final String UNIQUE_ID = "uniqueId";
            public static final String VERSION_ID = "versionId";
        }
        public static final class Contact {
            public static final String ACCEPTED_BY = "acceptedBy";
            public static final String ACCEPTED_ON = "acceptedOn";
            public static final String DECLINED_BY = "declinedBy";
            public static final String DECLINED_ON = "declinedOn";
            public static final String EMAIL = "email";
            public static final String INVITED_AS = "invitedAs";
            public static final String INVITED_BY = "invitedBy";
            public static final String INVITED_ON = "invitedOn";
            public static final String JABBER_ID = "jabberId";
            public static final String NAME = "name";
            public static final String ORGANIZATION = "organization";
            public static final String VCARD = "vCard";
        }
        public static final class Service {
            public static final String NAME = "query";
            public static final String NAMESPACE = "jabber:iq:parity:";
            public static final String NAMESPACE_RESPONSE = NAMESPACE + "response";
        }
    }
}
