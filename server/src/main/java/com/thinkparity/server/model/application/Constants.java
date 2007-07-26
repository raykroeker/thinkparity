/*
 * Nov 25, 2005
 */
package com.thinkparity.desdemona.model;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Constants<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.8
 */
public interface Constants {

    /** <b>Title:</b>Internet Constants:  Mail<br> */
    public static final class Internet {

        /** <b>Title:</b>Mail Constants:  From address<br> */
        public static final class Mail {
            public static final String FROM_ADDRESS = "thinkparity.noreply@thinkparity.com";
            public static final String FROM_PERSONAL = "thinkParity Team";
        }
    }

    /** Jabber Constants */
    public static final class Jabber {
        public static final String SYSTEM_QUALIFIED_JABBER_ID = "thinkparity@thinkparity.net/parity";
    }

    public static final class Jdbc {
        public static final String DRIVER = "org.hsqldb.jdbcDriver";
    }

    /** Jive Messenger Property Names */
    public static final class JivePropertyNames {
        public static final String THINKPARITY_BACKUP_ROOT = "thinkparity.backup.root";
        public static final String THINKPARITY_ENVIRONMENT = "thinkparity.environment";
        public static final String THINKPARITY_STREAM_ROOT = "thinkparity.stream.root";
        public static final String THINKPARITY_TEMP_ROOT = "thinkparity.temp.root";
        public static final String XMPP_DOMAIN = "xmpp.domain";
    }

    /** Product Constants */
    public static final class Product {
        /** Ophelia Product Constants */
        public static final class Ophelia {
            /** Ophelia Product Id Constant */
            public static final Long PRODUCT_ID = 1000L;
            /** Ophelia Product Feature Constants */
            public static final class Feature {
                /** Ophelia Product Feature Backup */
                public static final String BACKUP = "BACKUP";
                /** Ophelia Product Feature Core */
                public static final String CORE = "CORE";
            }
        }
    }

    /**
     * <b>Title:</b>Security Constants: Encryption Algorithm, Keystore Paths<br>
     * 
     */
    public static final class Security {
        public static final class Encryption {
            public static final String ALGORITHM = "AES";
            public static final Integer KEY_SIZE = 256;
        }
        public static final class KeyStore {
            public static final char[] CLIENT_PASS = "password".toCharArray();
            public static final String CLIENT_PATH = "security/client_keystore";
            public static final char[] SERVER_PASS = "password".toCharArray();
            public static final String SERVER_PATH = "security/server_keystore";
        }
    }

    /**
     * <b>Title:</b>Stream Constants: UPSTREAM_RETRY_ATTEMPTS,
     * DOWNSTREAM_RETRY_ATTEMPTS<br>
     */
    public static final class Stream {
        public static final Integer DOWNSTREAM_RETRY_ATTEMPTS = 3;
        public static final Integer UPSTREAM_RETRY_ATTEMPTS = 3;
    }

	public static final class VCardFields {
        public static final String TITLE = "TITLE";
    }

    public static final class Versioning {
        public static final Long INCREMENT = 1L;
        public static final Long START = 1L;
    }

    /** Xml constants. */
	public static final class Xml {
        public static final String DEFAULT_ENCODING = "ISO-8859-1";
        public static final class All {
            public static final String EXECUTED_ON = "executedOn";
        }
        public static final class Artifact {
            public static final String UNIQUE_ID = "uniqueId";
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
            public static final class Method {
                public static final class Publish {
                    public static final String EVENT_NAME = "jabber:iq:parity:container:published";
                    public static final String NAME = "";
                    public static final class Parameter {
                        public static final String CONTAINER_UNIQUE_ID = "containerUniqueId";
                        public static final String CONTAINER_VERSION_ID = "containerVersionId";
                        public static final String PUBLISHED_BY = "publishedBy";
                        public static final String PUBLISHED_ON = "publishedOn";
                    }
                }
                public static final class PublishArtifact {
                    public static final String EVENT_NAME = "jabber:iq:parity:artifactpublished";
                    public static final String NAME = "jabber:iq:parity:container:publishartifact";
                    public static final class Parameter {
                        public static final String ARTIFACT_BYTES = "artifactBytes";
                        public static final String ARTIFACT_CHECKSUM = "artifactChecksum";
                        public static final String ARTIFACT_NAME = "artifactName";
                        public static final String ARTIFACT_TYPE = "artifactType";
                        public static final String ARTIFACT_UNIQUE_ID = "artifactUniqueId";
                        public static final String ARTIFACT_VERSION_ID = "artifactVersionId";
                        public static final String CONTAINER_ARTIFACT_COUNT = "containerArtifactCount";
                        public static final String CONTAINER_ARTIFACT_INDEX = "containerArtifactIndex";
                        public static final String CONTAINER_NAME = "containerName";
                        public static final String CONTAINER_TEAM_MEMBER = "containerTeamMember";
                        public static final String CONTAINER_TEAM_MEMBERS = "containerTeamMembers";
                        public static final String CONTAINER_UNIQUE_ID = "containerUniqueId";
                        public static final String CONTAINER_VERSION_ID = "containerVersionId";
                        public static final String PUBLISHED_BY = "publishedBy";
                        public static final String PUBLISHED_ON = "publishedOn";
                    }
                }
                public static final class Send {
                    public static final String EVENT_NAME = "jabber:iq:parity:container:sent";
                    public static final String NAME = "jabber:iq:parity:container:send";
                    public static final class Parameter {
                        public static final String CONTAINER_UNIQUE_ID = "containerUniqueId";
                        public static final String CONTAINER_VERSION_ID = "containerVersionId";
                        public static final String SENT_BY = "sentBy";
                        public static final String SENT_ON = "sentOn";
                    }
                }
                public static final class SendArtifact {
                    public static final String EVENT_NAME = "jabber:iq:parity:artifactsent";
                    public static final String NAME = "jabber:iq:parity:container:sendartifact";
                    public static final class Parameter {
                        public static final String ARTIFACT_BYTES = "artifactBytes";
                        public static final String ARTIFACT_CHECKSUM = "artifactChecksum";
                        public static final String ARTIFACT_NAME = "artifactName";
                        public static final String ARTIFACT_TEAM_MEMBER = "artifactTeamMember";
                        public static final String ARTIFACT_TEAM_MEMBERS = "artifactTeamMembers";
                        public static final String ARTIFACT_TYPE = "artifactType";
                        public static final String ARTIFACT_UNIQUE_ID = "artifactUniqueId";
                        public static final String ARTIFACT_VERSION_ID = "artifactVersionId";
                        public static final String CONTAINER_ARTIFACT_COUNT = "containerArtifactCount";
                        public static final String CONTAINER_ARTIFACT_INDEX = "containerArtifactIndex";
                        public static final String CONTAINER_NAME = "containerName";
                        public static final String CONTAINER_UNIQUE_ID = "containerUniqueId";
                        public static final String CONTAINER_VERSION_ID = "containerVersionId";
                        public static final String SENT_BY = "sentBy";
                        public static final String SENT_ON = "sentOn";
                        public static final String SENT_TO = "sentTo";
                    }
                }
            }
        }
        public static final class Event {
            public static final class Artifact {
                public static final String DRAFT_CREATED = "draftcreated";
                public static final String DRAFT_DELETED = "draftdeleted";
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
            public static final String TITLE = "title";
        }
        public static final class Service {
            public static final class Artifact {
                public static final String CONFIRM_RECEIPT = com.thinkparity.codebase.Constants.Xml.NAMESPACE + "artifactconfirmreceipt";
                public static final String CREATE_ARTIFACT = com.thinkparity.codebase.Constants.Xml.NAMESPACE + "createartifact";
                public static final String CREATE_DRAFT = com.thinkparity.codebase.Constants.Xml.NAMESPACE + "artifact:createdraft";
                public static final String DELETE_DRAFT = com.thinkparity.codebase.Constants.Xml.NAMESPACE + "artifact:deletedraft";
                public static final String FLAG_ARTIFACT = com.thinkparity.codebase.Constants.Xml.NAMESPACE + "flagartifact";
                public static final String GET_SUBSCRIPTION = com.thinkparity.codebase.Constants.Xml.NAMESPACE + "getsubscription";
                public static final String READ_CONTACTS = com.thinkparity.codebase.Constants.Xml.NAMESPACE + "artifactreadcontacts";
                public static final String READ_KEY_HOLDER = com.thinkparity.codebase.Constants.Xml.NAMESPACE + "artifact:readkeyholder";
                public static final String REMOVE_TEAM_MEMBER = com.thinkparity.codebase.Constants.Xml.NAMESPACE + "artifact:removeteammember";
            }
            public static final class Contact {
                public static final String ACCEPT_INVITATION = com.thinkparity.codebase.Constants.Xml.NAMESPACE + "contact:acceptinvitation";
                public static final String DECLINE_INVITATION = com.thinkparity.codebase.Constants.Xml.NAMESPACE + "contact:declineinvitation";
                public static final String EXTEND_INVITATION = com.thinkparity.codebase.Constants.Xml.NAMESPACE + "contact:invite";
                public static final String READ = com.thinkparity.codebase.Constants.Xml.NAMESPACE + "contact:read";
                public static final String READ_CONTACTS = com.thinkparity.codebase.Constants.Xml.NAMESPACE + "readcontacts";
            }
            public static final class Container {
                public static final String PUBLISH = "container:publish";
                public static final String PUBLISH_ARTIFACT = "container:publishartifact";
            }
            public static final class Document {
                public static final String SEND = com.thinkparity.codebase.Constants.Xml.NAMESPACE + "documentsend";
            }
            public static final class Profile {
                public static final String READ = com.thinkparity.codebase.Constants.Xml.NAMESPACE + "profile:read";
            }
            public static final class Queue {
                public static final String PROCESS_OFFLINE_QUEUE = com.thinkparity.codebase.Constants.Xml.NAMESPACE + "processofflinequeue";
            }
            public static final class User {
                public static final String READ = com.thinkparity.codebase.Constants.Xml.NAMESPACE + "readusers";
            }
        }
        public static final class User {
            public static final String JABBER_ID = "jabberId";
            public static final String JABBER_IDS = "jabberIds";
        }
    }
}
