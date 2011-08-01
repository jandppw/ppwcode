/*
 * Copyright 2004 - $Date: 2008-11-15 23:58:07 +0100 (za, 15 nov 2008) $ by PeopleWare n.v..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
