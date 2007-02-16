/*
 * Nov 25, 2005
 */
package com.thinkparity.desdemona.model;


/**
 * @author raykroeker@gmail.com
 * @version 1.1.2.9
 */
public interface Constants {

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
        public static final String SYSTEM_QUALIFIED_JABBER_ID = "thinkparity@thinkparity.net/parity";
    }

    public static final class Jdbc {
        public static final String DRIVER = "org.hsqldb.jdbcDriver";
    }

	/** Jive Messenger Property Names */
    public static final class JivePropertyNames {
        public static final String THINKPARITY_ARCHIVE_ROOT = "thinkparity.archive.root";
        public static final String THINKPARITY_STREAM_ROOT = "thinkparity.stream.root";
        public static final String THINKPARITY_TEMP_ROOT = "thinkparity.temp.root";
        public static final String THINKPARITY_ENVIRONMENT = "thinkparity.environment";
        public static final String XMPP_DOMAIN = "xmpp.domain";
    }

    public static final class VCardFields {
        public static final String TITLE = "TITLE";
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
