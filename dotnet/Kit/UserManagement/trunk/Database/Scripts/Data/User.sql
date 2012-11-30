use [UserManagement]
go

declare
  @rc int,
  @context xml,
  @errorinfo nvarchar(4000);
  
select
  @context = N'<contextparams>'+
             N'  <contextparam key="LCID">1033</contextparam>'+
             N'  <contextparam key="default_LCID">1033</contextparam>'+
             N'  <contextparam key="UserID">1</contextparam>'+
             N'</contextparams>';

--
-- User
--
declare
  @UserID int,
  @UserName nvarchar(320),
  @Password nvarchar(32),
  @Email nvarchar(320),
  @LanguageLCID int,
  @FirstName nvarchar(64),
  @Name nvarchar(128),
  @Lockout bit;

select
  @UserName = 'Admin',
  @Password = 'F274CD458D26A47C366F0801E117D724',
  @Email = 'tom_crommen@peopleware.be',
  @LanguageLCID = 1043,
  @FirstName = 'Tom',
  @Name = 'Crommen',
  @Lockout = 0;
exec @rc = dbo.P_User_I
	@Context,
	@UserID OUTPUT,
	@UserName,
	@Password,
	@Email,
	@LanguageLCID,
	@Name,
	@FirstName,
	@Lockout,
	@errorinfo OUTPUT;
if (@rc <> 0) goto error; 

goto no_errors
error:
if (@rc <> 0)
begin
  select @rc as "Error code", @errorinfo as "Error Information"
  raiserror(60002, 16, 1);
end  
no_errors: