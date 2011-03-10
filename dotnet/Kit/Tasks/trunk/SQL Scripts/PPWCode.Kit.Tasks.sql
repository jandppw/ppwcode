SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

print '';
print '';
print 'Creating tables ...';
print '';


print 'Creating table dbo.AuditLog';
create table dbo.AuditLog (
  AuditLogID bigint identity(1,1) not null,

  EntryType nchar(1) not null,
  EntityName nvarchar(128) not null,
  EntityId bigint not null,
  PropertyName nvarchar(128) null,
  OldValue nvarchar(max) null,
  NewValue nvarchar(max) null,
  
  CreatedAt datetime null,
  CreatedBy nvarchar(64) null,
  
  constraint PK_AuditLog primary key (AuditLogID),
  constraint CK_EntryType check (EntryType in ('I','U','D'))
);
go


print 'Creating table dbo.Task';
create table dbo.Task (
  TaskID bigint identity(1,1) not null,
  PersistenceVersion int not null,

  [State] tinyint not null,
  TaskType varchar(128) not null,
  Reference varchar(512) not null,
  
  InProgressSince datetime null,
  InProgressBy nvarchar(64) null,
  CompletedSince datetime null,
  CompletedBy nvarchar(64) null,
  
  CreatedAt datetime null,
  CreatedBy nvarchar(64) null,
  LastModifiedAt datetime null,
  LastModifiedBy nvarchar(64) null,

  constraint PK_Task primary key (TaskID)
);
go
create index IX_Task_State on dbo.Task([State]);
create index IX_Task_Reference on dbo.Task(Reference);
create index IX_Task_TaskType on dbo.Task(TaskType);
go
