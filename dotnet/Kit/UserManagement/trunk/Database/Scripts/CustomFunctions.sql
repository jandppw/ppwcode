use UserManagement
go

if object_id(N'dbo.[fnLCID2DBLanguageName]') is not null
  drop function dbo.[fnLCID2DBLanguageName]
go
create function dbo.[fnLCID2DBLanguageName](
  @lcid int,
  @defaullcid int
)
returns sysname
as
begin
  declare
    @Result sysname;
    
  select @Result = l.name
    from sys.syslanguages l
   where l.lcid = @lcid;
   
  if @Result is null
    select @Result = l.name
      from sys.syslanguages l
     where l.lcid = @defaullcid;
     
  if @Result is null
    set @Result = 'us_english';   
    
  return @Result;
end    
go

if object_id(N'dbo.[fnGetMenuItems]') is not null
  drop function dbo.[fnGetMenuItems]
go
create function dbo.[fnGetMenuItems](
  @UserID int,
  @LanguageLCID int,
  @DefaultLanguageLCID int
)
returns table
as 
return (
with AvailableMenuItems as (  
  -- List of all available MenuItems for a User.
  select distinct m.MenuID, m.ParentMenuID
    from dbo.[User] u
         inner join dbo.[UserRole] ur on
           ur.UserID = u.UserID
         inner join dbo.RolePermission rp on
           rp.RoleID = ur.RoleID  
         inner join dbo.[MenuPermission] mp on
           mp.PermissionID = rp.PermissionID
         inner join dbo.[Menu] m on
           m.MenuID = mp.MenuID
   where u.UserID = @UserID),
   
  -- Building a bottom-up list of MenuItems
  BottomUpMenuItems as (   
    select mi.MenuID, mi.ParentMenuID
      from AvailableMenuItems mi
    union all
    select m.MenuID, m.ParentMenuID
      from BottomUpMenuItems child
           inner join dbo.[Menu] m on
             m.MenuID = child.ParentMenuID),
             
  -- Determine all top-level MenuItems
  TopLevelMenuItems as (
    select distinct m.MenuID
      from BottomUpMenuItems m
     where m.ParentMenuID is null),

  -- Building the parent-children list for the Menu.
  ParentChildrenMenuItems as (
    select tmi.MenuID,
           cast(null as int) as ParentMenuID,
           cast(1 as int) as [level]
      from TopLevelMenuItems tmi
    union all
    select childs.MenuID,
           childs.ParentMenuID,
           parent.[level]+1 as [level]
      from ParentChildrenMenuItems parent
           inner join AvailableMenuItems childs on
             childs.ParentMenuID = parent.MenuID),

  -- Determine all top-level MenuItems without children
  SingleMenuItems as (
    select distinct m.MenuID
      from AvailableMenuItems m
     where m.ParentMenuID is null
       and not exists (select null
                        from AvailableMenuItems m2
                       where m2.ParentMenuID = m.MenuID)),

  -- Generating the final menu
  FinalMenuItems as (
    select distinct s.MenuID
      from SingleMenuItems s
    union
    select distinct p.MenuID
      from ParentChildrenMenuItems p
    )

  -- Fetching the description for the menu-items
    select fmi.[level],
           m.Sequence,
           m.MenuID,
           m.ParentMenuID,
           cast(substring(dbo.fnXMLGetMessageValue(m.Description, @LanguageLCID, @DefaultLanguageLCID), 1, 255) as varchar(255)) as Description,
           m.URL
      from ParentChildrenMenuItems as fmi
           inner join dbo.[Menu] m on
             m.MenuID = fmi.MenuID
)
go

if object_id(N'dbo.[fnCheckSecurity]') is not null
  drop function dbo.[fnCheckSecurity]
go
create function dbo.fnCheckSecurity(
    @context xml,
    @UserID int,
    @PermissionID int
)
returns bit
as
begin
  declare
    @Result bit,
    @PermissionList nvarchar(4000);

  if @UserID = 1
    return cast(1 as bit);
    
  set @PermissionList = dbo.fnXMLGetContextValue(@Context, 'PermissionIDs');
  if @PermissionList is not null
  begin
    select @Result = cast(count(*) as bit)
      from (select null as dummy) as dual
     where exists(
             select null
               from dbo.fnSplitKeys(@PermissionList) k
              where k.F_KEY = @PermissionID);
  end 
  else begin
    select @Result = cast(count(*) as bit)
      from (select null as dummy) as dual
     where exists(
             select null
               from dbo.UserRole ur
                    inner join dbo.RolePermission rp on
                      rp.RoleID = ur.RoleID 
              where ur.UserID = @UserID
                and rp.PermissionID = @PermissionID);
  end
  return @Result;  
end
go


if object_id(N'dbo.[fnGetPagePermissions]') is not null
  drop function dbo.[fnGetPagePermissions]
go
create function [dbo].fnGetPagePermissions(
    @UserID int
)
returns table
as
return (
  select u.UserID,
         p.[Action],
         pa.[Name] 
    from dbo.[User] u
         inner join dbo.UserRole ur on
           ur.UserID = u.UserID
         inner join dbo.RolePermission rp on
           rp.RoleID = ur.RoleID
         inner join dbo.Permission p on
           p.PermissionID = rp.PermissionID
         inner join dbo.PagePermission pp on
           pp.PermissionID = p.PermissionID
         inner join dbo.Page pa on
           pa.PageID = pp.PageID
   where u.UserID = @UserID
     and u.Lockout = 0
)
go
    

if object_id(N'dbo.[fnGetPermissionsList]') is not null
  drop function dbo.[fnGetPermissionsList]
go
create function dbo.fnGetPermissionsList(
    @UserID int
)
returns nvarchar(4000)
as
begin
  declare
    @Result nvarchar(4000);
    
  select @Result = dbo.fnList(distinct rp.PermissionID)
    from dbo.[User] u
         inner join dbo.UserRole ur on
           ur.UserID = u.UserID
         inner join dbo.RolePermission rp on
           rp.RoleID = ur.RoleID
   where u.UserID = @UserID
     and u.Lockout = 0

  return @Result;
end  
go