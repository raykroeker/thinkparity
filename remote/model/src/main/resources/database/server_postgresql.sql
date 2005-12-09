
create table parityArtifact (
  artifactId integer not null,
  artifactUUID varchar(50) not null,
  artifactKeyHolder varchar(32) not null references jiveUser(username),
  createdOn timestamp not null default current_timestamp,
  updatedOn timestamp not null default current_timestamp,
  primary key (artifactId),
  unique (artifactUUID)
);
create index parityArtifact_artifactUUID_index on parityArtifact(artifactUUID);
create index parityArtifact_artifactKeyHolder_index on parityArtifact(artifactKeyHolder);
insert into jiveId (idType,id) values (1000, 1);

create table parityArtifactSubscription (
  artifactSubscriptionId integer not null,
  artifactId integer not null references parityArtifact(artifactId),
  username varchar(32) not null references jiveUser(username),
  createdOn timestamp not null default current_timestamp,
  updatedOn timestamp not null default current_timestamp,
  primary key (artifactSubscriptionId),
  unique (artifactId, username)
);
create index parityArtifactSubscription_artifactId_index on parityArtifactSubscription(artifactId);
create index parityArtifactSubscription_username_index on parityArtifactSubscription(username);
insert into jiveId (idType,id) values (1001, 1);

create table parityQueue (
  queueId integer not null,
  username varchar(32) not null references jiveUser(username),
  queueMessageSize integer not null,
  queueMessage text not null,
  createdOn timestamp not null default current_timestamp,
  updatedOn timestamp not null default current_timestamp,
  primary key (queueId)
);
create index parityQueue_username_index on parityQueue(username);
create index parityQueue_createdOn_index on parityQueue(createdOn);
insert into jiveId (idType,id) values (1002, 1);

