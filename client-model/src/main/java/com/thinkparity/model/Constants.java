/*
 * Created On: Sat Apr 29 10:22:32 PDT 2006
 */
package com.thinkparity.model;

import java.io.File;

/**
 * The parity local model constants.
 *
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public final class Constants {

    public static final class Compression {
        public static final Integer NONE = 0;
    }
    public static final class Connection {
        public static final String SERVER_HOST = System.getProperty("parity.serverhost");
        public static final Integer SERVER_PORT = Integer.getInteger("parity.serverport");
    }


    public static final class Directories {
        public static final File ARCHIVE = new File(System.getProperty("parity.archive.directory"));
        public static final File DOWNLOAD = new File(System.getProperty("parity.install"), DirectoryNames.DOWNLOAD);
        public static final File INSTALL = new File(System.getProperty("parity.install"));
        public static final File WORKSPACE = new File(System.getProperty("parity.workspace"));
    }

    public static final class DirectoryNames {
        public static final String BIN = "bin";
        public static final String CORE = "core";
        public static final String DOWNLOAD = ".download";
        public static final String LIB = "lib";
        public static final String LIB_NATIVE_WIN32 = "win32";
        public static final class Workspace {
            public static final String DATA = "data";
            public static final String INDEX = "index";
            public static final String LOG = "logs";
            public static final String TEMP = ".temp";
            public static final class Data {
                public static final String DB = "db.io";
                public static final String LOCAL = "local";
            }
        }
    }

    public static final class Encoding {
        public static final String BASE_64 = "Base64";
    }

    public static final class FileNames {
        public static final class Workspace {
            public static final class Data {
                public static final String DB = "db";
            }
        }
    }

    public static final class Files {
        public static final String TEMP_FILE_PREFIX = "thinkParity.";
    }

    public static final class Image {
        public static final String MAIN = "com.thinkparity.browser.Browser";
        public static final String MAIN_ARGS = "";
        public static final String PROPERTIES_FILENAME = "thinkParityImage.properties";
    }

    public static final class IO {
        public static final Integer BUFFER_SIZE = 512;
    }

    public static final class Jabber {
        public static final String RESOURCE = "parity";
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

    public static final class ShutdownHookNames {
        public static final String HYPERSONIC = "thinkParity Hypersonic Database";
        public static final String WORKSPACE = "thinkParity Workspace";
    }

    public static final class ShutdownHookPriorities {
        public static final Integer HYPERSONIC = 9;
        public static final Integer WORKSPACE = 0;
    }

    public static final class Sundry {
        public static final String THINKPARITY_GROUP_ID = "com.thinkparity.parity";
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
        public static final class Container {
            public static final String ARTIFACT_COUNT = "artifactCount";
            public static final String ARTIFACT_INDEX = "artifactIndex";
            public static final String CONTAINER_NAME = "containerName";
            public static final String CONTAINER_UNIQUE_ID = "containerUniqueId";
            public static final String CONTAINER_VERSION_ID = "containerVersionId";
            public static final String PUBLISHED_BY = "publishedBy";
            public static final String PUBLISHED_ON = "publishedOn";
            public static final String SENT_BY = "sentBy";
            public static final String SENT_ON = "sentOn";
        }
        public static final class EventHandler {
            public static final class Artifact {
                public static final String TEAM_MEMBER_ADDED = "jabber:iq:parity:teammemberadded";
                public static final String TEAM_MEMBER_REMOVED = "jabber:iq:parity:teammemberremoved";
            }
            public static final class Container {
                public static final String ARTIFACT_PUBLISHED = "jabber:iq:parity:artifactpublished";
                public static final String ARTIFACT_SENT = "jabber:iq:parity:artifactsent";
            }
        }
        public static final class Method {
            public static final class Artifact {
                public static final String ADD_TEAM_MEMBER = "artifact:addteammember";
                public static final String DELETE = "artifact:delete";
            }
            public static final class Container {
                public static final String PUBLISH = "container:publish";
                public static final String PUBLISH_ARTIFACT = "container:publishartifact";
                public static final String SEND = "container:send";
            }
        }
        public static final class Profile {
            public static final String EMAIL = "email";
            public static final String EMAILS = "emails";
            public static final String JABBER_ID = "jabberId";
            public static final String NAME = "name";
            public static final String ORGANIZATION = "organization";
        }
        public static final class User {
            public static final String JABBER_ID = "jabberId";
            public static final String JABBER_IDS = "jabberIds";
        }
    }
}
