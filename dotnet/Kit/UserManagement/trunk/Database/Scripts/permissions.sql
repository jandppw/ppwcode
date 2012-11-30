use UserManagement
go

if object_id(N'fnCreateMsg4Relation') is not null
  drop function dbo.[fnCreateMsg4Relation]
go
create function [dbo].[fnCreateMsg4Relation] (
  @Relation sysname,
  @Action char(1)
)
returns xml
as
begin
  declare 
    @Result nvarchar(4000);
  
  set @Result = null;
  with Actions as (
    select 2 as Sequence, 'C' as Action
    union all
    select 1 as Sequence, 'R' as Action
    union all
    select 3 as Sequence, 'U' as Action
    union all
    select 4 as Sequence, 'D' as Action),
    
  ActionLanguageLCIDs as (
    -- Culture: en-US
    select 'C' as Action,
           1033 as LanguageLCID,
           'Insert into' as Description
    union all       
    select 'R' as Action,
           1033 as LanguageLCID,
           'Select from' as Description
    union all       
    select 'U' as Action,
           1033 as LanguageLCID,
           'Update ' as Description
    union all       
    select 'D' as Action,
           1033 as LanguageLCID,
           'Delete from' as Description
           
    -- Culture: nl-BE
    union all
    select 'C' as Action,
           1043 as LanguageLCID,
           'Aanmaken' as Description
    union all       
    select 'R' as Action,
           1043 as LanguageLCID,
           'Selecteren van' as Description
    union all       
    select 'U' as Action,
           1043 as LanguageLCID,
           'Wijzigen in' as Description
    union all       
    select 'D' as Action,
           1043 as LanguageLCID,
           'Verwijderen van' as Description
           
    -- Culture: fr-FR
    union all
    select 'C' as Action,
           1036 as LanguageLCID,
           'Créer' as Description
    union all       
    select 'R' as Action,
           1036 as LanguageLCID,
           'Sélectionner' as Description
    union all       
    select 'U' as Action,
           1036 as LanguageLCID,
           'Modifier' as Description
    union all       
    select 'D' as Action,
           1036 as LanguageLCID,
           'Supprimer' as Description

    -- Culture: de-DE
    union all
    select 'C' as Action,
           1031 as LanguageLCID,
           'Verursachen Sie' as Description
    union all       
    select 'R' as Action,
           1031 as LanguageLCID,
           'Auserwählt' as Description
    union all       
    select 'U' as Action,
           1031 as LanguageLCID,
           'Ändern Sie' as Description
    union all       
    select 'D' as Action,
           1031 as LanguageLCID,
           'Löschung' as Description),
           
  all_tables as (       
    select t.[TABLE_NAME]
      from INFORMATION_SCHEMA.TABLES t
     where t.[TABLE_CATALOG] = 'UserManagement'
       and t.[TABLE_SCHEMA] = 'dbo'
       and t.[TABLE_NAME] = @Relation)
       
  select @Result = dbo.fnListEmpty('<message lcid="'+ltrim(str(ld.LanguageLCID))+'">'/*+ld.Description+' '*/+t.TABLE_NAME+'</message>')
    from all_tables t
         cross join (
           select a.Action, l.LanguageLCID, l.Description 
             from Actions a
                  inner join ActionLanguageLCIDs l on
                    l.Action = a.Action
            where a.Action = @Action) ld
  
  if @Result is not null
    set @Result = '<messages>'+@Result+'</messages>' 
    
  return cast(@Result as xml(dbo.[Messages]));  
end 
go


if exists(select null from dbo.[RolePermission])
begin
  delete
    from dbo.[RolePermission];
  dbcc checkident('dbo.[RolePermission]', reseed, 0);  
end


delete
  from dbo.MenuPermission;  
delete
  from dbo.Menu;
delete
  from dbo.[Permission]  

declare
  @Description xml,
  @context xml,
  @errorinfo nvarchar(4000),
  @name sysname;
  
select
  @context = '<contextparams><contextparam key="LCID">1033</contextparam><contextparam key="default_LCID">1033</contextparam><contextparam key="UserID">1</contextparam></contextparams>';
 
select 
  @Description = dbo.fnCreateMsg4Relation('Language', 'R'),
  @Name = 'Language';
exec dbo.P_Permission_I @context, 101, null, 'R', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('Language', 'C'),
  @Name = 'Language';
exec dbo.P_Permission_I @context, 102, 101, 'C', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('Language', 'U'),
  @Name = 'Language';
exec dbo.P_Permission_I @context, 103, 101, 'U', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('Language', 'D'),
  @Name = 'Language';
exec dbo.P_Permission_I @context, 104, 101, 'D', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('Menu', 'R'),
  @Name = 'Menu';
exec dbo.P_Permission_I @context, 105, null, 'R', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('Menu', 'C'),
  @Name = 'Menu';
exec dbo.P_Permission_I @context, 106, 105, 'C', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('Menu', 'U'),
  @Name = 'Menu';
exec dbo.P_Permission_I @context, 107, 105, 'U', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('Menu', 'D'),
  @Name = 'Menu';
exec dbo.P_Permission_I @context, 108, 105, 'D', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('MenuPermission', 'R'),
  @Name = 'MenuPermission';
exec dbo.P_Permission_I @context, 109, null, 'R', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('MenuPermission', 'C'),
  @Name = 'MenuPermission';
exec dbo.P_Permission_I @context, 110, 109, 'C', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('MenuPermission', 'U'),
  @Name = 'MenuPermission';
exec dbo.P_Permission_I @context, 111, 109, 'U', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('MenuPermission', 'D'),
  @Name = 'MenuPermission';
exec dbo.P_Permission_I @context, 112, 109, 'D', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('Page', 'R'),
  @Name = 'Page';
exec dbo.P_Permission_I @context, 113, null, 'R', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('Page', 'C'),
  @Name = 'Page';
exec dbo.P_Permission_I @context, 114, 113, 'C', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('Page', 'U'),
  @Name = 'Page';
exec dbo.P_Permission_I @context, 115, 113, 'U', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('Page', 'D'),
  @Name = 'Page';
exec dbo.P_Permission_I @context, 116, 113, 'D', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('PagePermission', 'R'),
  @Name = 'PagePermission';
exec dbo.P_Permission_I @context, 117, null, 'R', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('PagePermission', 'C'),
  @Name = 'PagePermission';
exec dbo.P_Permission_I @context, 118, 117, 'C', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('PagePermission', 'U'),
  @Name = 'PagePermission';
exec dbo.P_Permission_I @context, 119, 117, 'U', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('PagePermission', 'D'),
  @Name = 'PagePermission';
exec dbo.P_Permission_I @context, 120, 117, 'D', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('Permission', 'R'),
  @Name = 'Permission';
exec dbo.P_Permission_I @context, 121, null, 'R', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('Permission', 'C'),
  @Name = 'Permission';
exec dbo.P_Permission_I @context, 122, 121, 'C', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('Permission', 'U'),
  @Name = 'Permission';
exec dbo.P_Permission_I @context, 123, 121, 'U', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('Permission', 'D'),
  @Name = 'Permission';
exec dbo.P_Permission_I @context, 124, 121, 'D', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('Role', 'R'),
  @Name = 'Role';
exec dbo.P_Permission_I @context, 125, null, 'R', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('Role', 'C'),
  @Name = 'Role';
exec dbo.P_Permission_I @context, 126, 125, 'C', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('Role', 'U'),
  @Name = 'Role';
exec dbo.P_Permission_I @context, 127, 125, 'U', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('Role', 'D'),
  @Name = 'Role';
exec dbo.P_Permission_I @context, 128, 125, 'D', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('RolePermission', 'R'),
  @Name = 'RolePermission';
exec dbo.P_Permission_I @context, 129, null, 'R', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('RolePermission', 'C'),
  @Name = 'RolePermission';
exec dbo.P_Permission_I @context, 130, 129, 'C', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('RolePermission', 'U'),
  @Name = 'RolePermission';
exec dbo.P_Permission_I @context, 131, 129, 'U', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('RolePermission', 'D'),
  @Name = 'RolePermission';
exec dbo.P_Permission_I @context, 132, 129, 'D', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('User', 'R'),
  @Name = 'User';
exec dbo.P_Permission_I @context, 133, null, 'R', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('User', 'C'),
  @Name = 'User';
exec dbo.P_Permission_I @context, 134, 133, 'C', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('User', 'U'),
  @Name = 'User';
exec dbo.P_Permission_I @context, 135, 133, 'U', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('User', 'D'),
  @Name = 'User';
exec dbo.P_Permission_I @context, 136, 133, 'D', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('UserRole', 'R'),
  @Name = 'UserRole';
exec dbo.P_Permission_I @context, 137, null, 'R', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('UserRole', 'C'),
  @Name = 'UserRole';
exec dbo.P_Permission_I @context, 138, 137, 'C', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('UserRole', 'U'),
  @Name = 'UserRole';
exec dbo.P_Permission_I @context, 139, 137, 'U', @Name, @Description, @errorinfo OUTPUT;

select 
  @Description = dbo.fnCreateMsg4Relation('UserRole', 'D'),
  @Name = 'UserRole';
exec dbo.P_Permission_I @context, 140, 137, 'D', @Name, @Description, @errorinfo OUTPUT;


if object_id(N'fnCreateMsg4Relation') is not null
  drop function dbo.[fnCreateMsg4Relation]
go


