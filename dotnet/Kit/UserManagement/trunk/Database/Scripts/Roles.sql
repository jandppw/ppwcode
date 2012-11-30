use UserManagement
go

declare
  @rc int,
  @context xml,
  @errorinfo nvarchar(4000),
  @RoleID int;
select
  @context = N'<contextparams>'+
             N'  <contextparam key="LCID">1033</contextparam>'+
             N'  <contextparam key="default_LCID">1033</contextparam>'+
             N'  <contextparam key="UserID">1</contextparam>'+
             N'</contextparams>';
             
if exists(select null from dbo.[UserRole])
begin
  delete
    from dbo.[UserRole];
  dbcc checkident('dbo.[UserRole]', reseed, 0);  
end

if exists(select null from dbo.[Role])
begin
  delete
    from dbo.[Role];
  dbcc checkident('dbo.[Role]', reseed, 0);  
end

exec dbo.P_Role_I 
  @context, 
  @RoleID OUTPUT, 
  '<messages><message lcid="1033">Administrator</message></messages>', 
  @errorinfo;
if @rc <> 0 goto eof;

insert into dbo.RolePermission (
  RoleID,
  PermissionID,
  UserCreate,
  DateCreated,
  UserModified,
  DateModified)
select @RoleID,
       p.PermissionID,
       1,
       getdate(),
       1,
       getdate()
  from dbo.Permission p;
set @rc = @@error;
if @rc <> 0 goto eof;

insert into dbo.UserRole (
  UserID,
  RoleID,
  UserCreate,
  DateCreated,
  UserModified,
  DateModified)
values (1, @RoleID, 1, getdate(), 1, getdate());
set @rc = @@error;
if @rc <> 0 goto eof;

eof:
select 
  @rc, @errorinfo;