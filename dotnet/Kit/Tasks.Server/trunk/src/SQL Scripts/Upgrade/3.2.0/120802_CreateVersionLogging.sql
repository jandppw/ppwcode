use [PensioB.Tasks]
go

create table dbo.[Version] (
  VersionID int not null identity(1,1),
  Major int not null,
  Minor int null,
  Bugfix int not null,
  DateCreated datetime not null,
  
  constraint PK_Version primary key (VersionID),
  constraint UQ_Version unique (Major, Minor, Bugfix)
)
go

create table dbo.[ScriptLogging] (
  ScriptLoggingID int not null identity(1,1),
  VersionID int not null,
  ScriptName nvarchar(100) not null,
  ExecutionTime datetime not null,
  
  constraint PK_ScriptLogging primary key (ScriptLoggingID),
  constraint FK_ScriptLogging_Version foreign key (VersionID) references dbo.[Version] (VersionID)
)
go

insert into dbo.[Version] (
  Major,
  Minor,
  Bugfix,
  DateCreated)
values (
  3,
  2,
  0,
  getdate())
go
