use UserManagement
GO
 
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


if object_id(N'dbo.[fnGetUsers]') is not null
  drop function dbo.[fnGetUsers]
go
create function dbo.[fnGetUsers](
  @UserID int,
  @ViewUserID int
)
returns table  
  as  
  return ( 
    select us.*  
      from dbo.[User] us  
     where us.UserID = coalesce(@ViewUserID, us.UserID)   
  )  
go

if object_id(N'dbo.[fnCanSeeUser]') is not null
  drop function dbo.[fnCanSeeUser]
go
create function dbo.fnCanSeeUser(
    @UserID int,
    @ViewUserID int
)
returns bit
as
begin
  declare
    @Result bit;

  if @UserID = 1
    return cast(1 as bit);
    
  select @Result = cast(count(*) as bit)
    from (select null as dummy) as dual
   where exists(
            select null
              from dbo.fnGetUsers(@UserID, @ViewUserID));
 
  return @Result;  
end
go

if object_id(N'dbo.[fnGetRoles]') is not null
  drop function dbo.[fnGetRoles]
go
create function dbo.[fnGetRoles](
  @UserID int,
  @RoleID int
)
returns table  
  as  
  return ( 
    select r.*  
      from dbo.[Role] r  
     where r.RoleID = coalesce(@RoleID, r.RoleID) 
  )  
go

if object_id(N'dbo.[fnCanSeeRole]') is not null
  drop function dbo.[fnCanSeeRole]
go
create function dbo.fnCanSeeRole(
    @UserID int,
    @RoleID int
)
returns bit
as
begin
  declare
    @Result bit;

  if @UserID = 1
    return cast(1 as bit);
    
  select @Result = cast(count(*) as bit)
    from (select null as dummy) as dual
   where exists(
            select null
              from dbo.fnGetRoles(@UserID, @RoleID));
 
  return @Result;  
end
go

if object_id(N'dbo.[fnGetPermissions]') is not null
  drop function dbo.[fnGetPermissions]
go
create function dbo.[fnGetPermissions](
  @UserID int
)
returns table  
  as  
  return ( 
    select p.*  
      from dbo.[Permission] p
  )  
go