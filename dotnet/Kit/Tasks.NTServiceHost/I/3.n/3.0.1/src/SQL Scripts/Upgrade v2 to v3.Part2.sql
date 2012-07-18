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

use [PensioB.Tasks]
go

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

print '';
print '';
print 'Upgrading from v2 to v3 Part2 ...';
print '';

print 'altering dbo.task, drop unneeded column reference'
drop index ix_task_reference on dbo.task;
alter table dbo.task
  drop column reference;
go
