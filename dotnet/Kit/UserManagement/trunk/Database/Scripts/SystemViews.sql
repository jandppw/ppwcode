use UserManagement
go

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


if object_id(N'[v_sys_relation_info]') is not null
  drop view dbo.[v_sys_relation_info]
go
create view dbo.[v_sys_relation_info]
as
select t.TABLE_NAME,
       t.TABLE_TYPE, 
       c.COLUMN_NAME, 
       c.ORDINAL_POSITION, 
       c.DATA_TYPE,
       case c.IS_NULLABLE
         when 'YES' then 1
         else 0
       end as IS_NULLABLE,
       c.CHARACTER_OCTET_LENGTH, 
       c.CHARACTER_MAXIMUM_LENGTH, 
       c.NUMERIC_PRECISION, 
       c.NUMERIC_SCALE,
       sysc.is_computed,
       sysc.is_identity,
       cast(
         case
           when kcu.ORDINAL_POSITION is null then 0
           else 1
         end as bit) as is_pk,
       kcu.ORDINAL_POSITION as PK_ORDINAL_POSITION,
       cast(
         case
           when exists (
                  select null
                    from INFORMATION_SCHEMA.COLUMNS c 
                   where c.TABLE_CATALOG = t.TABLE_CATALOG
                     and c.TABLE_SCHEMA = t.TABLE_SCHEMA
                     and c.TABLE_NAME = t.TABLE_NAME 
                     and c.COLUMN_NAME in ('UserCreate','DateCreated','UserModified','DateModified')) then 1
           else 0          
         end as bit) as has_audit
  from INFORMATION_SCHEMA.TABLES t
       inner join INFORMATION_SCHEMA.COLUMNS c on
             c.TABLE_CATALOG = t.TABLE_CATALOG
         and c.TABLE_SCHEMA = t.TABLE_SCHEMA
         and c.TABLE_NAME = t.TABLE_NAME
       inner join sys.columns sysc on
             sysc.[object_id] = object_id(c.TABLE_CATALOG+'.'+c.TABLE_SCHEMA+'.'+c.TABLE_NAME)
         and sysc.[name] = c.COLUMN_NAME
       left outer join INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc on
             tc.TABLE_CATALOG = t.TABLE_CATALOG
         and tc.TABLE_SCHEMA = t.TABLE_SCHEMA
         and tc.TABLE_NAME = t.TABLE_NAME
         and tc.CONSTRAINT_TYPE = 'PRIMARY KEY'
       left outer join INFORMATION_SCHEMA.KEY_COLUMN_USAGE kcu on
             kcu.CONSTRAINT_CATALOG = tc.CONSTRAINT_CATALOG
         and kcu.CONSTRAINT_SCHEMA = tc.CONSTRAINT_SCHEMA
         and kcu.CONSTRAINT_NAME = tc.CONSTRAINT_NAME
         and kcu.TABLE_CATALOG = t.TABLE_CATALOG
         and kcu.TABLE_SCHEMA = t.TABLE_SCHEMA
         and kcu.TABLE_NAME = t.TABLE_NAME
         and kcu.COLUMN_NAME = c.COLUMN_NAME
 where t.TABLE_CATALOG = 'UserManagement'
   and t.TABLE_SCHEMA = 'dbo';
go


if object_id(N'[v_sys_rowcount]') is not null
  drop view dbo.[v_sys_rowcount]
go
create view [dbo].[v_sys_rowcount]
as
    select o.id,
        o.name as f_table_name,
        i.rowcnt as f_rowcount
    from    sysobjects o
            inner join sysindexes i on
          i.id = o.id
    where   o.xtype = 'U'
    and i.indid in (0, 1);
go


if object_id(N'[v_sys_constraint_primary_key]') is not null
  drop view dbo.[v_sys_constraint_primary_key]
go
CREATE view [dbo].[v_sys_constraint_primary_key]
as
    select  distinct top 100 percent
            obj.name as F_TABLENAME,
            ind.name as F_PK_NAME,
            index_col( obj.name, ind.indid, 1) as F_FIRSTFIELD,
            index_col( obj.name, ind.indid, 2) as F_SECONDFIELD,
            index_col( obj.name, ind.indid, 3) as F_THIRDFIELD,
            index_col( obj.name, ind.indid, 4) as F_FOURTHFIELD,
            index_col( obj.name, ind.indid, 5) as F_FIFTHFIELD,
            index_col( obj.name, ind.indid, 6) as F_SIXTHFIELD,
            index_col( obj.name, ind.indid, 7) as F_SEVENTHFIELD,
            'alter table [' + obj.name + '] drop constraint [' + ind.name + ']' as F_DROP_STATEMENT,
            'alter table [' + obj.name + '] add constraint [PK_' + obj.name + '] primary key clustered ( [' + index_col( obj.name, ind.indid, 1) + ']' + isnull( ', [' + index_col( obj.name, ind.indid, 2) + ']', '') + isnull( ', [' + index_col( obj.name, ind.indid, 3) + ']', '') + isnull( ', [' + index_col( obj.name, ind.indid, 4) + ']', '') + isnull( ', [' + index_col( obj.name, ind.indid, 5) + ']', '') + isnull( ', [' + index_col( obj.name, ind.indid, 6) + ']', '') + isnull( ', [' + index_col( obj.name, ind.indid, 7) + ']', '') + ') with fillfactor = ' +
            case
                when rcnt.F_ROWCOUNT < 500 then '100'
                when rcnt.F_ROWCOUNT between 500 and 10000 then '80'
                when rcnt.F_ROWCOUNT > 10000 then '60'
                else '80'
            end as F_CREATE_STATEMENT
    from    sysindexes ind
            inner join sysindexkeys ikey on
              ind.id = ikey.id
          and ind.indid = ikey.indid
            inner join syscolumns ic on
              ic.id = ind.id
          and ic.colid = ikey.colid
            inner join sysobjects obj on
          obj.id = ind.id
            inner join dbo.v_sys_rowcount rcnt on
          rcnt.id = obj.id
    where   (ind.status & 2048) <> 0
    order by obj.name
go


if object_id(N'[v_sys_constraint_unique]') is not null
  drop view dbo.[v_sys_constraint_unique]
go
CREATE view [dbo].[v_sys_constraint_unique]
as
    select distinct top 100 percent
           obj.name as F_TABLENAME, 
           ind.name as F_UC_NAME, 
           index_col( obj.name, ind.indid, 1) as F_FIRSTFIELD, 
           index_col( obj.name, ind.indid, 2) as F_SECONDFIELD,
           index_col( obj.name, ind.indid, 3) as F_THIRDFIELD, 
           index_col( obj.name, ind.indid, 4) as F_FOURTHFIELD,
           index_col( obj.name, ind.indid, 5) as F_FIFTHFIELD, 
           index_col( obj.name, ind.indid, 6) as F_SIXTHFIELD,
           index_col( obj.name, ind.indid, 7) as F_SEVENTHFIELD,
           case 
             when ind.status & 4096 <> 0 then 'alter table [' + obj.name + '] drop constraint [' + ind.name + ']' 
             else null
           end as F_DROP_STATEMENT,
           'alter table [' + obj.name + '] with nocheck add constraint [UC_' + obj.name + 
             '_' + index_col( obj.name, ind.indid, 1) + isnull( '_' + index_col( obj.name, ind.indid, 2), '') + isnull( '_' + index_col( obj.name, ind.indid, 3), '') + isnull( '_' + index_col( obj.name, ind.indid, 4), '') + isnull( '_' + index_col( obj.name, ind.indid, 5), '') + isnull( '_' + index_col( obj.name, ind.indid, 6), '') + isnull( '_' + index_col( obj.name, ind.indid, 7), '') + 
             '] unique ( [' + index_col( obj.name, ind.indid, 1) + ']' + isnull( ', [' + index_col( obj.name, ind.indid, 2) + ']', '') + isnull( ', [' + index_col( obj.name, ind.indid, 3) + ']', '') + isnull( ', [' + index_col( obj.name, ind.indid, 4) + ']', '') + isnull( ', [' + index_col( obj.name, ind.indid, 5) + ']', '') + isnull( ', [' + index_col( obj.name, ind.indid, 6) + ']', '') + isnull( ', [' + index_col( obj.name, ind.indid, 7) + ']', '') + ')' as F_CREATE_STATEMENT
      from sysindexes ind
           inner join sysindexkeys ikey on 
                 ind.id = ikey.id 
             and ind.indid = ikey.indid
           inner join syscolumns ic on 
                 ic.id = ind.id 
             and ic.colid = ikey.colid
           inner join sysobjects obj on 
             obj.id = ind.id
           inner join V_SYS_ROWCOUNT rcnt on 
             rcnt.id = obj.id
     where ind.status & ( 2| 4096) <> 0 
       and ind.status & 2048 = 0 
    order by obj.name
go


if object_id(N'[v_sys_constraint_foreign_key]') is not null
  drop view dbo.[v_sys_constraint_foreign_key]
go
create view [dbo].[v_sys_constraint_foreign_key]
as
select  distinct top 100 percent
        object_name( rkeyid) as f_primarykey_table,
        pc.name as f_primarykey_column,
        object_name( fkeyid) as f_foreignkey_table,
        fc.name as f_foreignkey_column,
        object_name( constid) as f_foreign_key_name,
        'alter table [' + object_name( fkeyid) + '] drop constraint [' + object_name( constid) + ']' as F_DROP_FK_STATEMENT,
        'alter table [' + object_name( fkeyid) + '] with check add constraint [FK_' + object_name( fkeyid) + '_' + pk.F_TABLENAME + '_' + fc.name + '] foreign key ( [' + fc.name + ']) references [' + pk.F_TABLENAME + '] ( [' + pc.name + '])' as F_CREATE_FK_STATEMENT,
        'if ( select count( distinct convert( varchar( 40), ' + fc.name + ')) * 1.0 / ( case when count( convert( varchar( 40), ' + index_col( fkobj.name, ind.indid, 1) + ')) > 10000 then count( convert( varchar( 40), ' + index_col( fkobj.name, ind.indid, 1) + ')) / 8 else count( convert( varchar( 40), ' + index_col( fkobj.name, ind.indid, 1) + ')) end + 1) from ' + object_name( fkeyid) + ') > 0.5 create index [IX_' + object_name( fkeyid) + '_' + fc.name + '] on [' + object_name( fkeyid) + '] ( [' + fc.name + ']) with pad_index, fillfactor = ' +
        case
            when rcnt.F_ROWCOUNT < 500 then '100'
            when rcnt.F_ROWCOUNT between 500 and 10000 then '80'
            when rcnt.F_ROWCOUNT > 10000 then '60'
            else '80'
        end as f_create_index_statement,
        '[FK_' + object_name( fkeyid) + '_' + pk.F_TABLENAME + '_' + fc.name + ']' as F_INDEX_NAME,
        '[FK_' + object_name( fkeyid) + '_' + fc.name + ']' as F_INDEX_NAME2
from    sysforeignkeys fk
        inner join syscolumns pc on
          pc.id = fk.rkeyid
      and pc.colid = fk.rkey
        inner join syscolumns fc on
          fc.id = fk.fkeyid
      and fc.colid = fk.fkey
        inner join sysobjects obj on
      obj.id = rkeyid
        inner join sysobjects fkobj on
      fkobj.id = fc.id
        left outer join sysindexes ind on
          ind.id = fc.id
      and (ind.status & 2048) <> 0
        inner join dbo.v_sys_rowcount rcnt on
      rcnt.id = obj.id
        left outer join dbo.v_sys_constraint_primary_key pk on
          pk.F_FIRSTFIELD = pc.name
      and object_name( rkeyid) = pk.F_TABLENAME
order by
object_name( rkeyid)
go


if object_id(N'[v_sys_index]') is not null
  drop view dbo.[v_sys_index]
go
CREATE view [dbo].[v_sys_index]
as
    select  distinct top 100 percent
            obj.name as f_tablename,
            ind.name as f_indexname,
            index_col( obj.name, ind.indid, 1) as f_firstfield,
            index_col( obj.name, ind.indid, 2) as f_secondfield,
            index_col( obj.name, ind.indid, 3) as f_thirdfield,
            index_col( obj.name, ind.indid, 4) as f_fourthfield,
            index_col( obj.name, ind.indid, 5) as f_fifthfield,
            index_col( obj.name, ind.indid, 6) as f_sixthfield,
            index_col( obj.name, ind.indid, 7) as f_seventhfield,
            'drop index [' + obj.name + '].[' + ind.name + ']' as f_drop_statement
    from    sysindexes ind
            inner join sysindexkeys ikey on
              ind.id = ikey.id
          and ind.indid = ikey.indid
            inner join syscolumns ic on
              ic.id = ind.id
          and ic.colid = ikey.colid
            inner join sysobjects obj on
          obj.id = ind.id
            inner join v_sys_rowcount rcnt on
          rcnt.id = obj.id
    where ind.status & ( 2048 | 4096 | 8388608) = 0 and
            indexproperty( obj.id, ind.name, 'IsStatistics') = 0
 order by obj.name
go


if object_id(N'[v_sys_index_create]') is not null
  drop view dbo.[v_sys_index_create]
go
CREATE view [dbo].[v_sys_index_create]
as
  select obj.name as f_tablename,
         ic.name as f_column_name,
         ic.xtype as f_datatype,
         rcnt.f_rowcount,
         ('IX_FK_' + obj.name + '_' + ic.name) as IndexName,
         'create index [IX_FK_' + obj.name + '_' + ic.name + '] on [' + obj.name + '] ( [' + ic.name + ']) with pad_index, fillfactor = ' +
         case
           when rcnt.f_rowcount < 500 then '100'
           when rcnt.f_rowcount between 500 and 10000 then '80'
           when rcnt.f_rowcount > 10000 then '60'
           else '80'
         end as f_create_statement
    from syscolumns ic
         inner join sysobjects obj on
           obj.id = ic.id
         inner join dbo.v_sys_rowcount rcnt on
           rcnt.id = obj.id
   where ic.xtype not in (173, 34, 99, 98, 35, 165) --no binary types
go


if object_id(N'[DBLocks]') is not null
  drop view dbo.[DBLocks]
go
create view [dbo].[DBLocks]
as
select t.request_session_id as spid,
       db_name(t.resource_database_id) as dbname,
       case
         when t.resource_type = 'OBJECT' then 
object_name(t.resource_associated_entity_id)
         when t.resource_associated_entity_id = 0 then 'n/a'
         else object_name(p.object_id)
       end as entity_name,
       t.resource_type,
       p.index_id,
       t.resource_type as resource,
       t.resource_description as description,
       t.request_mode as mode,
       t.request_status as status
  from sys.dm_tran_locks t
       left outer join  sys.partitions p on
         p.hobt_id = t.resource_associated_entity_id
 where t.resource_database_id = db_id()
go


if object_id(N'dbo.v_sys_uid_routines', N'V') is not null
  drop view dbo.v_sys_uid_routines;
go  
create view dbo.v_sys_uid_routines
as
with base as (
  select substring(r.routine_name, 3, len(r.routine_name) - 4) as relation,
         r.routine_name as base_routine,
         coalesce(ro.routine_name, r.routine_name) as routine_name, 
         convert(bit, case when ro.routine_name is not null then 1 else 0 end) overruled
    from INFORMATION_SCHEMA.ROUTINES r
         left outer join INFORMATION_SCHEMA.ROUTINES ro on
               ro.ROUTINE_CATALOG = r.ROUTINE_CATALOG
           and ro.ROUTINE_SCHEMA = r.ROUTINE_SCHEMA
           and ro.ROUTINE_TYPE = r.ROUTINE_TYPE
           and ro.ROUTINE_NAME = r.ROUTINE_NAME+'_Override'
   where r.ROUTINE_CATALOG = 'UserManagement' 
     and r.ROUTINE_SCHEMA = 'dbo'
     and r.ROUTINE_TYPE = 'PROCEDURE'
     and r.ROUTINE_NAME like 'P\_%\_[IUD]' escape '\')
  select b.relation,
         b.base_routine,
         b.routine_name,
         case right(b.base_routine, 1)
           when 'I' then 'Insert'
           when 'D' then 'Delete'
           when 'U' then 'Update'
           else 'Unknown'
         end + b.relation as PartialMethodName,
         b.overruled
    from base b
go


if object_id(N'dbo.v_sys_uid_routine_params', N'V') is not null
  drop view dbo.v_sys_uid_routine_params;
go  
create view dbo.v_sys_uid_routine_params
as
with base as (
  select r.relation,
         r.routine_name,
         r.PartialMethodName,
         p.ORDINAL_POSITION,
         substring(p.PARAMETER_NAME, 2, len(p.PARAMETER_NAME)-1) as PARAMETER_NAME,
         p.PARAMETER_MODE,
         p.DATA_TYPE
    from dbo.v_sys_uid_routines r
         inner join INFORMATION_SCHEMA.PARAMETERS p on
               p.SPECIFIC_CATALOG = 'UserManagement'
           and p.SPECIFIC_SCHEMA = 'dbo'     
           and p.SPECIFIC_NAME = r.base_routine),
base2 as (
  select b.*,
         ri.DATA_TYPE as COLUMN_DATA_TYPE,
         ri.IS_NULLABLE,
         case 
           when ri.DATA_TYPE = 'xml' then 'System.Xml.Linq.XDocument'
           when ri.DATA_TYPE = 'int' then 'int'
           when ri.DATA_TYPE in ('nvarchar', 'varchar', 'nchar', 'char') then 'string'
           when ri.DATA_TYPE = 'bit' then 'bool'
           when ri.DATA_TYPE = 'datetime' then 'DateTime'
           when ri.DATA_TYPE = 'decimal' then 'decimal'
           when ri.DATA_TYPE = 'uniqueidentifier' then 'System.Guid'
           when ri.DATA_TYPE = 'varbinary' then 'System.Data.Linq.Binary'
         end as CS_DATA_TYPE,
         case 
           when ri.DATA_TYPE = 'xml' then 0
           when ri.DATA_TYPE = 'int' then 1
           when ri.DATA_TYPE in ('nvarchar', 'varchar', 'nchar', 'char') then 0
           when ri.DATA_TYPE = 'bit' then 1
           when ri.DATA_TYPE = 'datetime' then 1
           when ri.DATA_TYPE = 'decimal' then 1
           when ri.DATA_TYPE = 'uniqueidentifier' then 1
           when ri.DATA_TYPE = 'varbinary' then 0
         end as can_cast
    from base b           
         left outer join dbo.v_sys_relation_info ri on
               ri.TABLE_NAME = b.relation
           and ri.COLUMN_NAME = b.PARAMETER_NAME),
base3 as (           
  select b.*,
         case b.PARAMETER_NAME
           when 'Context' then 'm_UserContext'
           when 'errorinfo' then 'errorInfo'
           else null
         end as CS_PARAMETER_NAME,
         case 
           when (b.IS_NULLABLE = 0) and (b.can_cast = 1) then 
             cs_data_type+'?'
         end as CS_CAST_NEEDED  
    from base2 b)
select b.relation,
       b.routine_name,
       b.PartialMethodName,
       b.ORDINAL_POSITION,
       case b.PARAMETER_MODE
         when 'INOUT' then coalesce(b.CS_CAST_NEEDED, b.CS_DATA_TYPE)+' '+b.PARAMETER_NAME+' = '+'instance.'+b.PARAMETER_NAME+';'
         else null
       end as CS_DECLARATION,  
       case b.PARAMETER_MODE
         when 'INOUT' then 'ref '+b.PARAMETER_NAME
         else case
                when b.CS_CAST_NEEDED is null then coalesce(b.CS_PARAMETER_NAME, 'instance.'+b.PARAMETER_NAME)
                else '('+b.CS_CAST_NEEDED+')'+coalesce(b.CS_PARAMETER_NAME, 'instance.'+b.PARAMETER_NAME)
              end
       end as CS_PARAMETER_DESCR,
       case 
         when b.PARAMETER_MODE = 'INOUT' and CS_PARAMETER_NAME is null then 'instance.'+b.PARAMETER_NAME+' = '+b.PARAMETER_NAME+'.GetValueOrDefault();'
         else null
       end as CS_AFTER_CALL
  from base3 b    
go
