/*
 * Created On: Wed May 10 2006 11:26 PDT
 * $Id$
 */
package com.thinkparity.migrator;

/**
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public final class Constants {

    /** Create Constants. */
    private Constants() { super(); }

    /** Database constants. */
    public static class Database {

        public static final String DIRECTORY = "db";

        public static final String FILE = "rMigrator";
    }

    /** Miscellaneous constants. */
    public static class Sundry {
        public static final String CLIENT_RESOURCE = "parity";

        public static final String LOG4J_FILE = "rMigrator.html";
    }

    public static final class Xml {

        public static final String NAME = "query";
       
        public static final String NAMESPACE = "jabber:iq:parity";

        public static final String RESPONSE_NAMESPACE = "jabber:iq:parity:response";

        public static final class Library {

            public static final String ARTIFACT_ID = "artifactId";

            public static final String BYTES = "bytes";

            public static final String CREATED_ON = "createdOn";

            public static final String GROUP_ID = "groupId";

            public static final String ID = "id";

            public static final String LIBRARIES = "libraries";

            public static final String LIBRARY = "library";

            public static final String TYPE = "type";

            public static final String VERSION = "version";
        }

        public static final class Release {

            public static final String ARTIFACT_ID = "artifactId";

            public static final String CREATED_ON = "createdOn";

            public static final String GROUP_ID = "groupId";

            public static final String ID = "id";

            public static final String LIBRARIES = "libraries";

            public static final String LIBRARY = "library";

            public static final String LIBRARY_ID = "libraryId";

            public static final String LIBRARY_IDS = "libraryIds";

            public static final String NAME = "name";

            public static final String VERSION = "version";
        }
    }
}
