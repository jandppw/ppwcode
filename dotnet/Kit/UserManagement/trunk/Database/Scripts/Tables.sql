use UserManagement
go

set nocount on
declare
  @err int, 
  @cr cursor,
  @constraint_name sysname,
  @object_name sysname,
  @stmt nvarchar(1024),
  @i int;  

-- Drop tables
set @cr = cursor fast_forward for
  with fk as (
    select fk.parent_object_id,
           object_name(fk.parent_object_id) as table_name,
           fk.referenced_object_id,
           object_name(fk.referenced_object_id) as referenced_table_name
      from sys.foreign_keys fk
     where fk.parent_object_id <> fk.referenced_object_id)
      
    select object_name(o.[object_id]) as [object_name]
      from sys.objects o 
           inner join sys.schemas s on
             s.[schema_id] = o.[schema_id]
     where o.[type] = 'u'
       and s.[name] = 'dbo'
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
    set @stmt = 'drop table ['+@object_name+']';
    execute @err = sp_executesql @stmt;
  end
  close @cr
  set @i = @i + 1;
end  
deallocate @cr   
go



create table dbo.[Language] (
  LanguageLCID int not null,
  CultureName nvarchar(20) not null,
  [Description] nvarchar(128) not null,
  
  constraint PK_Language primary key (LanguageLCID),
  constraint UQ_Language_CultureName unique (CultureName)
)
go


create table dbo.[User] (
    UserID int not null identity(1,1),
    UserName nvarchar(320) not null,
    [Password] nvarchar(32) null,
    Email nvarchar(320) not null,
    LanguageLCID int not null,
    Name nvarchar(128) not null,
    FirstName nvarchar(64) null,
    Lockout bit not null constraint DF_User_Lockout default (0),

    UserCreate int null,
    DateCreated datetime null constraint DF_User_DateCreated default (getdate()),
    UserModified int null,
    DateModified datetime null constraint DF_User_DateModified default (getdate()),
    
    constraint PK_User primary key (UserID),
    constraint UQ_User_UserName unique (UserName),
    constraint FK_User_LanguageLCID foreign key (LanguageLCID) references dbo.[Language] (LanguageLCID),
    constraint FK_User_UserCreated foreign key (UserCreate) references dbo.[User] (UserID),
    constraint FK_User_UserModified foreign key (UserModified) references dbo.[User] (UserID)
)
go


create table dbo.[Role] (
    RoleID int not null identity(1,1),
    [Description] xml(Messages) not null,

    UserCreate int null,
    DateCreated datetime null constraint DF_Role_DateCreated default (getdate()),
    UserModified int null,
    DateModified datetime null constraint DF_Role_DateModified default (getdate()),

    constraint PK_Role primary key (RoleID),
    constraint FK_Role_UserCreated foreign key (UserCreate) references dbo.[User] (UserID),
    constraint FK_Role_UserModified foreign key (UserModified) references dbo.[User] (UserID)
)
go


create table dbo.[UserRole] (
    UserRoleID int not null identity(1,1),
    UserID int not null,
    RoleID int not null,

    UserCreate int null,
    DateCreated datetime null constraint DF_UserRoley_DateCreated default (getdate()),
    UserModified int null,
    DateModified datetime null constraint DF_UserRole_DateModified default (getdate()),

    constraint PK_UserRole primary key (UserRoleID),
    constraint UQ_UserRole unique (UserID, RoleID),
    constraint FK_UserRole_User foreign key (UserID) references dbo.[User] (UserID),
    constraint FK_UserRole_Role foreign key (RoleID) references dbo.[Role] (RoleID),
    constraint FK_UserRole_UserCreated foreign key (UserCreate) references dbo.[User] (UserID),
    constraint FK_UserRole_UserModified foreign key (UserModified) references dbo.[User] (UserID)
)
go


create table dbo.[Permission] (
    PermissionID int not null,
    RequiredPermissionID int null,
    [Action] char(1) not null,
    Name nvarchar(128) not null,
    [Description] xml(Messages) not null,
    
    UserCreate int null,
    DateCreated datetime null constraint DF_Permission_DateCreated default (getdate()),
    UserModified int null,
    DateModified datetime null constraint DF_Permission_DateModified default (getdate()),

    constraint PK_Permission primary key (PermissionID),
    constraint UQ_Permission unique (Action, Name),
    constraint FK_Permission_RequiredPermission foreign key (RequiredPermissionID) references dbo.[Permission] (PermissionID),
    constraint FK_Permission_UserCreated foreign key (UserCreate) references dbo.[User] (UserID),
    constraint FK_Permission_UserModified foreign key (UserModified) references dbo.[User] (UserID)
)
go


create table dbo.RolePermission (
    RolePermissionID int not null identity(1,1),
    RoleID int not null,
    PermissionID int not null,
    
    UserCreate int null,
    DateCreated datetime null constraint DF_RolePermission_DateCreated default (getdate()),
    UserModified int null,
    DateModified datetime null constraint DF_RolePermission_DateModified default (getdate()),

    constraint PK_RolePermission primary key (RolePermissionID),
    constraint UQ_RolePermission unique (RoleID, PermissionID),
    constraint FK_RolePermission_Permission foreign key (RoleID) references dbo.[Role] (RoleID),
    constraint FK_RolePermission_Entity foreign key (PermissionID) references dbo.[Permission] (PermissionID),
    constraint FK_RolePermission_UserCreated foreign key (UserCreate) references dbo.[User] (UserID),
    constraint FK_RolePermission_UserModified foreign key (UserModified) references dbo.[User] (UserID)
)
go


create table dbo.[ConstraintMessage] (
    ConstraintName sysname not null,
    ConstraintType varchar(11) not null,
    Description xml(dbo.Messages) null,
    
    UserCreate int null,
    DateCreated datetime null constraint DF_ConstraintMessage_DateCreated default (getdate()),
    UserModified int null,
    DateModified datetime null constraint DF_ConstraintMessage_DateModified default (getdate()),

    constraint PK_ConstraintMessage primary key (ConstraintName),
    constraint FK_ConstraintMessage_UserCreated foreign key (UserCreate) references dbo.[User] (UserID),
    constraint FK_ConstraintMessage_UserModified foreign key (UserModified) references dbo.[User] (UserID)
)
go


create table dbo.[Menu] (
    MenuID int not null,
    ParentMenuID int null,
    Sequence int null,
    URL nvarchar(512) null,
    [Description] xml(Messages) null,
    
    constraint PK_Menu primary key (MenuID)
)
go


create table dbo.[MenuPermission] (
    MenuID int not null,
    PermissionID int not null,
    
    constraint PK_MenuPermission primary key (MenuID, PermissionID),
    constraint FK_MenuPermission_MenuID foreign key (MenuID) references dbo.[Menu] (MenuID),
    constraint FK_MenuPermission_PermissionID foreign key (PermissionID) references dbo.[Permission](PermissionID)
)
go


create table dbo.[Page] (
    PageID int not null identity(1,1),
    Name nvarchar(255) not null

    constraint PK_Page primary key (PageID),
    constraint UQ_Page unique (Name)
)
go


create table dbo.[PagePermission] (
    PageID int not null,
    PermissionID int not null,

    constraint PK_PagePermission primary key (PageID, PermissionID),
    constraint FK_PagePermission_Page foreign key (PageID) references dbo.[Page] (PageID),
    constraint FK_PagePermission_Permission foreign key (PermissionID) references dbo.[Permission] (PermissionID)
)
go
