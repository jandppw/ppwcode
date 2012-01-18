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
print 'Upgrading from v2 to v3 ...';
print '';


print 'Creating table dbo.TaskAttributes';
create table dbo.TaskAttributes (
  TaskID bigint not null,
  AttributeName varchar(64) not null,
  AttributeValue varchar(256) null,

  constraint PK_TaskAttributes primary key (TaskID, AttributeName),
  constraint FK_TaskAttributes_Task foreign key (TaskID) references dbo.Task (TaskId)
 );
go
create index IX_TaskAttributes_Attribute on dbo.TaskAttributes(AttributeValue, AttributeName);
go
