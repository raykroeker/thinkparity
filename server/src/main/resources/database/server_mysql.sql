create table parityArtifactState (
  artifactStateId tinyint unsigned not null,
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
  artifactId integer not null,
  artifactUUID varchar(255) not null,
  artifactKeyHolder varchar(32) not null references jiveUser(username),
  artifactStateId tinyint not null references parityArtifactState(artifactStateId),
  createdOn timestamp not null default current_timestamp,
  updatedOn timestamp not null,
  primary key (artifactId),
  unique (artifactUUID)
);
create index parityArtifact_artifactUUID_index on parityArtifact(artifactUUID);
create index parityArtifact_artifactKeyHolder_index on parityArtifact(artifactKeyHolder);
insert into jiveID (idType,id) values (1000, 1);

create table parityArtifactSubscription (
  artifactSubscriptionId integer not null,
  artifactId integer not null references parityArtifact(artifactId),
  username varchar(32) not null references jiveUser(username),
  createdOn timestamp not null default current_timestamp,
  updatedOn timestamp not null,
  primary key (artifactSubscriptionId),
  unique (artifactId, username)
);
create index parityArtifactSubscription_artifactId_index on parityArtifactSubscription(artifactId);
create index parityArtifactSubscription_username_index on parityArtifactSubscription(username);
insert into jiveID (idType,id) values (1001, 1);

create table parityQueue (
  queueId integer not null,
  username varchar(32) not null references jiveUser(username),
  queueMessageSize integer not null,
  queueMessage text not null,
  createdOn timestamp not null default current_timestamp,
  updatedOn timestamp not null,
  primary key (queueId)
);
create index parityQueue_username_index on parityQueue(username);
create index parityQueue_createdOn_index on parityQueue(createdOn);
insert into jiveID (idType,id) values (1002, 1);
