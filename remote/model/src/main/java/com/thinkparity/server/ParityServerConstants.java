/*
 * Nov 25, 2005
 */
package com.thinkparity.server;

/**
 * @author raykroeker@gmail.com
 * @version 1.1.2.9
 */
public interface ParityServerConstants {

	public static final String CLIENT_RESOURCE = "parity";

	public static final String DISPATCHER_NAME =
		"Parity XMPP Dispatcher";

	public static final String IQ_PARITY_INFO_NAME = "query";

	public static final String IQ_PARITY_INFO_NAMESPACE = "jabber:iq:parity";

	public static final String LOG4J_DIRECTORY_NAME = "log4j";

	public static final String SERVER_NAME = "Parity XMPP Server";

    /** Internet Constants */
    public static final class Internet {
        /** Internet Mail Constants. */
        public static final class Mail {
            public static final String BCC_ADDRESS = "thinkparity.bcc@thinkparity.com";
            public static final String BCC_PERSONAL = "thinkParity BCC";
            public static final String FROM_ADDRESS = "thinkparity.noreply@thinkparity.com";
            public static final String FROM_PERSONAL = "thinkParity Team";
        }
        /** Internet WWW Constants. */
        public static final class WWW {
            /** WWW Base. */
            public static final String BASE = "thinkparity.dyndns.org";
            /** WWW Suffix. */
            public static final String SUFFIX = "thinkparity";
        }
    }

    /** Xml constants. */
	public static final class Xml {
	    public static final class Artifact {
            public static final String BYTES = "bytes";
            public static final String NAME = "name";
            public static final String UNIQUE_ID = "uniqueId";
            public static final String VERSION_ID = "versionId";
        }
        /** Contact Xml Constants */
        public static final class Contact {
            public static final String EMAIL = "email";
        }
        /** Profile Xml Constants */
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
