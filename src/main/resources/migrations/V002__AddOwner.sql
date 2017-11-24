create table HUMAN (
  ID int primary key,
  NAME nvarchar(1000) not null,
  DOG_ID varchar(36),
  foreign key (DOG_ID) references DOG(ID)
);

