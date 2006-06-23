/*
 * Nov 25, 2005
 */
package com.thinkparity.server;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface ParityServerConstants {

	public static final String CLIENT_RESOURCE = "parity";

	public static final String DISPATCHER_NAME =
		"Parity XMPP Dispatcher";

	public static final String IQ_PARITY_INFO_NAME = "query";

	public static final String IQ_PARITY_INFO_NAMESPACE = "jabber:iq:parity";

	public static final String LOG4J_DIRECTORY_NAME = "log4j";

	public static final String SERVER_NAME = "Parity XMPP Server";

    /** Xml constants. &*/
	public static final class Xml {
	    public static final class Artifact {
            public static final String BYTES = "bytes";
            public static final String NAME = "name";
            public static final String UNIQUE_ID = "uniqueId";
            public static final String VERSION_ID = "versionId";
        }
        public static final class User {
            public static final String JABBER_ID = "jabberId";
            public static final String JABBER_IDS = "jabberIds";
        }
    }
}
