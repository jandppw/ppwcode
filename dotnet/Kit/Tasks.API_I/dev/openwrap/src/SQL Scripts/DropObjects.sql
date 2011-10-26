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

declare
  @err int,
  @cr cursor,
  @object_name sysname,
  @stmt nvarchar(1024),
  @i int;

print 'Dropping tables ...';
print '';
set @cr = cursor fast_forward for
  with fk as (
    select fk.parent_object_id,
           object_name(fk.parent_object_id) as table_name,
           fk.referenced_object_id,
           object_name(fk.referenced_object_id) as referenced_table_name
      from sys.foreign_keys fk
     where fk.parent_object_id <> fk.referenced_object_id)

    select s.[name]+'.['+object_name(o.[object_id])+']' as [object_name]
      from sys.objects o
           inner join sys.schemas s on
             s.[schema_id] = o.[schema_id]
     where o.[type] = 'u'
       and s.[name] in ('dbo', 'Actua', 'Meta', 'Config')
       and not exists (
             select null
               from fk
              where fk.referenced_object_id = o.[object_id])

set @i = 0;
while (@i < 20)
begin
  open @cr;
  set @err = @@error;
  while(@err = 0)
  begin
    fetch next from @cr into
      @object_name;
    set @err = @@error;
    if (@err <> 0) or (@@FETCH_STATUS <> 0)
      break;
    set @stmt = 'drop table '+@object_name;
    print @stmt
    execute @err = sp_executesql @stmt;
  end
  close @cr
  set @i = @i + 1;
end
deallocate @cr
go


print '';
print '';
print 'Dropping functions ...';
print '';

declare
  @err int, 
  @cr cursor,
  @object_name sysname,
  @stmt nvarchar(1024);
  
set @cr = cursor fast_forward for
  select schema_name(o.schema_id)+'.['+o.name+']'
    from sys.objects o
         inner join sys.schemas s on
           s.[schema_id] = o.[schema_id]
   where o.[type] in ('FN', 'FS', 'FT', 'IF')
     and s.[name] in ('dbo', 'Actua', 'Meta');
     
open @cr;
set @err = @@error;
while(@err = 0)
begin
  fetch next from @cr into
    @object_name;
  set @err = @@error;
  if (@err <> 0) or (@@FETCH_STATUS <> 0)
    break;
  set @stmt = 'drop function '+@object_name;
  print @stmt
  execute @err = sp_executesql @stmt;
end
close @cr
deallocate @cr   
go


print '';
print '';
print 'Dropping procedures ...';
print '';

declare
  @err int, 
  @cr cursor,
  @object_name sysname,
  @stmt nvarchar(1024);
  
set @cr = cursor fast_forward for
  select schema_name(o.schema_id)+'.['+o.name+']'
    from sys.objects o
         inner join sys.schemas s on
           s.[schema_id] = o.[schema_id]
   where o.[type] in ('P', 'PC')
     and o.name like 'sp%'
     and s.[name] in ('dbo', 'Actua', 'Meta');
     
open @cr;
set @err = @@error;
while(@err = 0)
begin
  fetch next from @cr into
    @object_name;
  set @err = @@error;
  if (@err <> 0) or (@@FETCH_STATUS <> 0)
    break;
  set @stmt = 'drop procedure '+@object_name;
  print @stmt
  execute @err = sp_executesql @stmt;
end
close @cr
deallocate @cr   
go


print '';
print '';
print 'Dropping views ...';
print '';

declare
  @err int,
  @cr cursor,
  @object_name sysname,
  @stmt nvarchar(1024);

set @cr = cursor fast_forward for
  select schema_name(o.schema_id)+'.['+o.name+']'
    from sys.objects o
         inner join sys.schemas s on
           s.[schema_id] = o.[schema_id]
   where o.[type] = 'V'
     and s.[name] in ('dbo', 'Actua', 'Meta');
     
open @cr;
set @err = @@error;
while(@err = 0)
begin
  fetch next from @cr into
    @object_name;
  set @err = @@error;
  if (@err <> 0) or (@@FETCH_STATUS <> 0)
    break;
  set @stmt = 'drop view '+@object_name;
  print @stmt
  execute @err = sp_executesql @stmt;
end
close @cr
deallocate @cr   
go
