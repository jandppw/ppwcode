use [UserManagement]
go

declare
  @rc int,
  @context xml,
  @errorinfo nvarchar(4000),
  @PageID int;
  
select
  @context = '<contextparams><contextparam key="LCID">1033</contextparam><contextparam key="default_LCID">1033</contextparam><contextparam key="UserID">1</contextparam></contextparams>';

if exists(select null from dbo.[PagePermission])
begin
  delete
    from dbo.[PagePermission];
end

if exists(select null from dbo.[Page])
begin
  delete
    from dbo.[Page];
  dbcc checkident('dbo.[Page]', reseed, 0);  
end

--
-- Home
--
exec @rc = dbo.P_Page_I @Context, @PageID OUTPUT, N'~/Home.aspx', @errorinfo OUTPUT;
if (@rc <> 0) goto error;
  exec @rc = dbo.P_PagePermission_I @Context, @PageID, 133, @errorinfo OUTPUT;
  if (@rc <> 0) goto error;
--
-- User
--
exec @rc = dbo.P_Page_I @Context, @PageID OUTPUT, N'~/Users.aspx', @errorinfo OUTPUT;
if (@rc <> 0) goto error;
  exec @rc = dbo.P_PagePermission_I @Context, @PageID, 133, @errorinfo OUTPUT;
  if (@rc <> 0) goto error;
  exec @rc = dbo.P_PagePermission_I @Context, @PageID, 134, @errorinfo OUTPUT;
  if (@rc <> 0) goto error;

exec @rc = dbo.P_Page_I @Context, @PageID OUTPUT, N'~/UserDetail.aspx', @errorinfo OUTPUT;
if (@rc <> 0) goto error;
  exec @rc = dbo.P_PagePermission_I @Context, @PageID, 133, @errorinfo OUTPUT;
  if (@rc <> 0) goto error;
  exec @rc = dbo.P_PagePermission_I @Context, @PageID, 134, @errorinfo OUTPUT;
  if (@rc <> 0) goto error;
  exec @rc = dbo.P_PagePermission_I @Context, @PageID, 135, @errorinfo OUTPUT;
  if (@rc <> 0) goto error;
  exec @rc = dbo.P_PagePermission_I @Context, @PageID, 136, @errorinfo OUTPUT;
  if (@rc <> 0) goto error;
--
-- Policies
--
exec @rc = dbo.P_Page_I @Context, @PageID OUTPUT, N'~/Roles.aspx', @errorinfo OUTPUT;
if (@rc <> 0) goto error;
  exec @rc = dbo.P_PagePermission_I @Context, @PageID, 125, @errorinfo OUTPUT;
  if (@rc <> 0) goto error;
  exec @rc = dbo.P_PagePermission_I @Context, @PageID, 126, @errorinfo OUTPUT;
  if (@rc <> 0) goto error;

exec @rc = dbo.P_Page_I @Context, @PageID OUTPUT, N'~/RoleDetail.aspx', @errorinfo OUTPUT;
if (@rc <> 0) goto error;
  exec @rc = dbo.P_PagePermission_I @Context, @PageID, 125, @errorinfo OUTPUT;
  if (@rc <> 0) goto error;
  exec @rc = dbo.P_PagePermission_I @Context, @PageID, 126, @errorinfo OUTPUT;
  if (@rc <> 0) goto error;
  exec @rc = dbo.P_PagePermission_I @Context, @PageID, 127, @errorinfo OUTPUT;
  if (@rc <> 0) goto error;
  exec @rc = dbo.P_PagePermission_I @Context, @PageID, 128, @errorinfo OUTPUT;
  if (@rc <> 0) goto error;

goto no_errors
error:
if (@rc <> 0)
begin
  select @rc as "Error code", @errorinfo as "Error Information"
  raiserror(60002, 16, 1);
end  
no_errors:
  