use [UserManagement]
go

declare
  @catalog sysname;
  
set @catalog = 'UserManagement';
delete
  from dbo.ConstraintMessage; 

insert into dbo.ConstraintMessage (
  ConstraintName,
  ConstraintType,
  UserCreate,
  DateCreated,
  UserModified,
  DateModified)
select tc.CONSTRAINT_NAME,
       tc.CONSTRAINT_TYPE,
       1,
       getdate(),
       1,
       getdate()
  from INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc
 where tc.CONSTRAINT_CATALOG = @catalog  
   and tc.CONSTRAINT_SCHEMA = 'dbo';
