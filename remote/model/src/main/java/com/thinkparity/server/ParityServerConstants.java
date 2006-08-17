/*
 * Nov 25, 2005
 */
package com.thinkparity.server;

import com.thinkparity.codebase.Application;

/**
 * @author raykroeker@gmail.com
 * @version 1.1.2.9
 */
public interface ParityServerConstants {

    /** Internet Constants */
    public static final class Internet {
        /** Internet Mail Constants. */
        public static final class Mail {
            public static final String BCC_ADDRESS = "raykroeker+bcc@thinkparity.com";
            public static final String BCC_PERSONAL = "thinkParity BCC";
            public static final String FROM_ADDRESS = "thinkparity.noreply@thinkparity.com";
            public static final String FROM_PERSONAL = "thinkParity Team";
        }
    }

	/** Jabber Constants */
    public static final class Jabber {
        public static final String SYSTEM_QUALIFIED_JABBER_ID = "thinkparity@thinkparity.dyndns.org/parity";
    }

    public static final class JivePropertyNames {
        public static final String XMPP_DOMAIN = "xmpp.domain";
    }

    /** Logging constants. */
    public static final class Logging {
        public static final Object CONTROLLER_LOG_ID = Application.DESDEMONA + "] [CONTROLLER";
        public static final Object MODEL_LOG_ID = Application.DESDEMONA + "] [MODEL";
        public static final Object PLUGIN_LOG_ID = Application.DESDEMONA + "] [PLUGIN";
        public static final Object SQL_LOG_ID = Application.DESDEMONA + "] [SQL IO";
    }

    public static final class PackageNames {
        public static final String SERVER_HANDLER = "com.thinkparity.server.handler";
    }

    /** Xml constants. */
	public static final class Xml {
        public static final class All {
            public static final String EXECUTED_ON = "executedOn";
        }
        public static final class Artifact {
            public static final String BYTES = "bytes";
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
            public static final String EMAILS = "emails";
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
        public static final class Event {
            public static final class Artifact {
                public static final String DRAFT_CREATED = "draftcreate";
                public static final String TEAM_MEMBER_ADDED = "teammemberadded";
                public static final String TEAM_MEMBER_REMOVED = "teammemberremoved";
            }
            public static final class Contact {
                public static final String INVITATION_ACCEPTED = "invitationaccepted";
                public static final String INVITATION_DECLINED = "invitationdeclined";
                public static final String INVITATION_EXTENDED = "invitationextended";
            }
            public static final class Container {
                public static final String ARTIFACT_PUBLISHED = "artifactpublished";
                public static final String ARTIFACT_SENT = "artifactsent";
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
