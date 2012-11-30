use [UserManagement]
go


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_User_D_Override]') is not null
  DROP PROCEDURE [dbo].[P_User_D_Override];
GO  
CREATE PROCEDURE [dbo].[P_User_D_Override](
    @Context xml,
    @UserID int,
    @errorinfo nvarchar(4000) OUTPUT)
as
begin
    set nocount on

    -- local variables
    declare 
      @err int,
      @trancountonentry int,
      @ContextUserID int,
      @AdminID int,
      @MetaDataUserID int,
      @lcid int,
      @default_lcid int,
      @language_name sysname;
    declare
      @xact_state int,
      @error_number int,
      @error_severity int,
      @error_state int,
      @error_message nvarchar(4000),
      @error_line int,
      @error_procedure sysname;

    -- If no explicit transaction exists yet, create one.
    select
      @errorinfo = null,
      @err = 0,
      @trancountOnEntry = @@TRANCOUNT;
    if @trancountOnEntry = 0
        begin transaction;

    begin try
      -- Get the UserID using the context.
      -- Set correct language for possible errors.
      select
        @ContextUserID = nullif(cast(dbo.fnXMLGetContextValue(@Context, 'UserID') as int), 0),
        @AdminID = nullif(cast(dbo.fnXMLGetContextValue(@Context, 'AdminID') as int), -1),
        @lcid = dbo.fnXMLGetContextValue(@Context, 'LCID'),
        @default_lcid = dbo.fnXMLGetContextValue(@Context, 'default_LCID');
      set @MetaDataUserID = coalesce(@AdminID, @ContextUserID);
      set @language_name = dbo.fnLCID2DBLanguageName(@lcid, @default_lcid);
      set language @language_name;

      delete from dbo.UserRole where UserRole.UserID = @UserID;
      
      -- Execute the CRUD functionality.   
      exec @err = dbo.P_User_D
        @Context,
        @UserID,
        @errorinfo OUTPUT;
        
    end try
    begin catch
      set @xact_state = xact_state();
      set @error_number = error_number();
      set @error_severity = error_severity();
      set @error_state = error_state();
      set @error_message = error_message();
      set @error_line = error_line();
      set @error_procedure = error_procedure();
      exec @err = dbo.P_ErrorHandler
        @xact_state,
        @lcid,
        @default_lcid,
        @error_number,
        @error_severity,
        @error_state,
        @error_message,
        @error_line,
        @error_procedure,
        @errorinfo OUTPUT,
        null;
    end catch

    -- If this procedues is the owner of the explicit transaction, 
    -- then it will commit or rollback.
    if @trancountOnEntry = 0
    begin
      if @err = 0
      begin
        if XACT_STATE() = 1
          commit transaction;
        else if XACT_STATE() = -1
          rollback transaction;
      end
      else begin
        if XACT_STATE() <> 0
          rollback transaction;
      end
    end

    return @err;
END
GO
