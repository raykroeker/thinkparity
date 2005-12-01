
create sequence parityArtifact_artifactId_seq;
create table parityArtifact (
  artifactId integer DEFAULT nextval('parityArtifact_artifactId_seq') NOT NULL,
  artifactUUID varchar(50) NOT NULL,
  createdOn timestamp NOT NULL default current_timestamp,
  updatedOn timestamp NOT NULL default current_timestamp,
  PRIMARY KEY (artifactId),
  UNIQUE (artifactUUID)
);

create sequence parityArtifactSubscription_artifactSubscriptionId_seq;
create table parityArtifactSubscription (
  artifactSubscriptionId integer DEFAULT nextval('parityArtifactSubscription_artifactSubscriptionId_seq') NOT NULL,
  artifactId integer NOT NULL REFERENCES parityArtifact(artifactId),
  username varchar(32) NOT NULL REFERENCES jiveUser(username),
  createdOn timestamp NOT NULL default current_timestamp,
  updatedOn timestamp NOT NULL default current_timestamp,
  PRIMARY KEY (artifactSubscriptionId),
  UNIQUE (artifactId, username)
);
