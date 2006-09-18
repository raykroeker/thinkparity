create table parityArtifactState (
  artifactStateId tinyint not null,
  artifactState varchar(12) not null,
  primary key (artifactStateId),
  unique(artifactState)
);
create index parityArtifactState_artifactState on parityArtifactState(artifactState);
insert into parityArtifactState (artifactStateId, artifactState) values (0, 'ACTIVE');
insert into parityArtifactState (artifactStateId, artifactState) values (1, 'ARCHIVED');
insert into parityArtifactState (artifactStateId, artifactState) values (2, 'CLOSED');
insert into parityArtifactState (artifactStateId, artifactState) values (3, 'DELETED');

create table parityArtifact (
  artifactId bigint not null,
  artifactUUID varchar(255) not null,
  artifactKeyHolder varchar(32) not null,
  artifactStateId tinyint not null,
  createdBy varchar(32) not null,
  createdOn timestamp default current_timestamp not null,
  updatedBy varchar(32) not null,
  updatedOn timestamp not null,
  primary key (artifactId),
  foreign key (artifactKeyHolder) references jiveUser(username),
  foreign key (artifactStateId) references parityArtifactState(artifactStateId),
  foreign key (createdBy) references jiveUser(username),
  foreign key (updatedBy) references jiveUser(username),
  unique (artifactUUID)
);
create index parityArtifact_artifactUUID_index on parityArtifact(artifactUUID);
create index parityArtifact_artifactKeyHolder_index on parityArtifact(artifactKeyHolder);
insert into jiveID (idType,id) values (1000, 1);

create table parityArtifactSubscription (
  artifactSubscriptionId integer not null,
  artifactId bigint not null,
  username varchar(32) not null,
  createdBy varchar(32) not null,
  createdOn timestamp default current_timestamp not null,
  updatedBy varchar(32) not null,
  updatedOn timestamp not null,
  primary key (artifactSubscriptionId),
  foreign key (artifactId) references parityArtifact(artifactId),
  foreign key (username) references jiveUser(username),
  foreign key (createdBy) references jiveUser(username),
  foreign key (updatedBy) references jiveUser(username),
  unique (artifactId, username)
);
create index parityArtifactSubscription_artifactId_index on parityArtifactSubscription(artifactId);
create index parityArtifactSubscription_username_index on parityArtifactSubscription(username);
insert into jiveID (idType,id) values (1001, 1);

create table parityQueue (
  queueId integer not null,
  username varchar(32) not null,
  queueMessageSize integer not null,
  queueMessage varchar not null,
  createdBy varchar(32) not null,
  createdOn timestamp default current_timestamp not null,
  updatedBy varchar(32) not null,
  updatedOn timestamp not null,
  primary key (queueId),
  foreign key (username) references jiveUser(username),
  foreign key (createdBy) references jiveUser(username),
  foreign key (updatedBy) references jiveUser(username)
);
create index parityQueue_username_index on parityQueue(username);
create index parityQueue_createdOn_index on parityQueue(createdOn);
insert into jiveID (idType,id) values (1002, 1);

create table PARITY_EVENT_QUEUE (
  USERNAME varchar(32) not null,
  EVENT_XML varchar not null,
  EVENT_DATE timestamp default current_timestamp not null,
  foreign key (USERNAME) references jiveUser(USERNAME)
);

create table parityContactInvitation (
  invitationFrom varchar(32) not null,
  invitationTo varchar(32) not null,
  createdBy varchar(32) not null,
  createdOn timestamp default current_timestamp not null,
  updatedBy varchar(32) not null,
  updatedOn varchar(32) not null,
  primary key (invitationFrom,invitationTo),
  foreign key (invitationFrom) references jiveUser(username),
  foreign key (invitationTo) references jiveUser(username),
  foreign key (createdBy) references jiveUser(username),
  foreign key (updatedBy) references jiveUser(username)
);
create table parityContactEmailInvitation (
  invitationFrom varchar(32) not null,
  invitationTo varchar(32) not null,
  createdBy varchar(32) not null,
  createdOn timestamp default current_timestamp not null,
  updatedBy varchar(32) not null,
  updatedOn varchar(32) not null,
  primary key (invitationFrom,invitationTo),
  foreign key (invitationFrom) references jiveUser(username),
  foreign key (createdBy) references jiveUser(username),
  foreign key (updatedBy) references jiveUser(username)
);

create table parityContact (
  username varchar(32) not null,
  contactUsername varchar(32) not null,
  createdBy varchar(32) not null,
  createdOn timestamp default current_timestamp not null,
  updatedBy varchar(32) not null,
  updatedOn varchar(32) not null,
  primary key (username,contactUsername),
  foreign key (username) references jiveUser(username),
  foreign key (createdBy) references jiveUser(username),
  foreign key (updatedBy) references jiveUser(username)
);

create table parityUserEmail (
  username varchar(32) not null,
  email varchar not null,
  verified boolean not null,
  verificationKey varchar null,
  primary key (email),
  foreign key (username) references jiveUser(username)
);

create table parityUserProfile (
    username varchar(32) not null,
    securityQuestion varchar not null,
    securityAnswer varchar not null,
    primary key (username),
    foreign key (username) references jiveUser(username)
);

create table PARITY_USER_ARCHIVE_REL (
	USERNAME varchar(32) not null,
	ARCHIVENAME varchar(32) not null,
	primary key (USERNAME,ARCHIVENAME),
	foreign key (USERNAME) references JIVEUSER(USERNAME),
	foreign key (ARCHIVENAME) references JIVEUSER(USERNAME)
);

create table PARITY_FEATURE (
	FEATURE_ID bigint generated by default as identity (start with 1000),
	FEATURE varchar not null,
	primary key (FEATURE_ID),
	unique (FEATURE)
);

create table PARITY_USER_FEATURE_REL (
	USERNAME varchar(32) not null,
	FEATURE_ID bigint not null,
	primary key (USERNAME,FEATURE_ID),
	foreign key (USERNAME) references jiveUser(USERNAME),
	foreign key (FEATURE_ID) references PARITY_FEATURE(FEATURE_ID)
);

