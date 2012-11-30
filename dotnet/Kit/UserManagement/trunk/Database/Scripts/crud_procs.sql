use [UserManagement]
go


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_ConstraintMessage_U]') is not null
  DROP PROCEDURE [dbo].[P_ConstraintMessage_U];
GO  
CREATE PROCEDURE [dbo].[P_ConstraintMessage_U](
    @Context xml,
    @ConstraintName nvarchar(128),
    @ConstraintType varchar(11),
    @Description xml,
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

      -- Execute the CRUD functionality.   
      update [dbo].[ConstraintMessage]
         set [ConstraintType] = @ConstraintType,
             [Description] = @Description,
             [UserModified] = @MetaDataUserID,
             [DateModified] = getdate()
       where [ConstraintName] = @ConstraintName;
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_ConstraintMessage_D]') is not null
  DROP PROCEDURE [dbo].[P_ConstraintMessage_D];
GO  
CREATE PROCEDURE [dbo].[P_ConstraintMessage_D](
    @Context xml,
    @ConstraintName nvarchar(128),
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

      -- Execute the CRUD functionality.   
      delete
        from [dbo].[ConstraintMessage]
       where [ConstraintName] = @ConstraintName;
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_ConstraintMessage_I]') is not null
  DROP PROCEDURE [dbo].[P_ConstraintMessage_I];
GO  
CREATE PROCEDURE [dbo].[P_ConstraintMessage_I](
    @Context xml,
    @ConstraintName nvarchar(128),
    @ConstraintType varchar(11),
    @Description xml,
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

      -- Execute the CRUD functionality.   
      insert into [dbo].[ConstraintMessage] (
        [ConstraintName],
        [ConstraintType],
        [Description],
        [UserCreate],
        [UserModified])
      values (
        @ConstraintName,
        @ConstraintType,
        @Description,
        @ContextUserID,
        @MetaDataUserID);
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_Language_U]') is not null
  DROP PROCEDURE [dbo].[P_Language_U];
GO  
CREATE PROCEDURE [dbo].[P_Language_U](
    @Context xml,
    @LanguageLCID int,
    @CultureName nvarchar(20),
    @Description nvarchar(128),
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

      -- Execute the CRUD functionality.   
      update [dbo].[Language]
         set [CultureName] = @CultureName,
             [Description] = @Description
       where [LanguageLCID] = @LanguageLCID;
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_Language_D]') is not null
  DROP PROCEDURE [dbo].[P_Language_D];
GO  
CREATE PROCEDURE [dbo].[P_Language_D](
    @Context xml,
    @LanguageLCID int,
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

      -- Execute the CRUD functionality.   
      delete
        from [dbo].[Language]
       where [LanguageLCID] = @LanguageLCID;
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_Language_I]') is not null
  DROP PROCEDURE [dbo].[P_Language_I];
GO  
CREATE PROCEDURE [dbo].[P_Language_I](
    @Context xml,
    @LanguageLCID int,
    @CultureName nvarchar(20),
    @Description nvarchar(128),
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

      -- Execute the CRUD functionality.   
      insert into [dbo].[Language] (
        [LanguageLCID],
        [CultureName],
        [Description])
      values (
        @LanguageLCID,
        @CultureName,
        @Description);
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_Menu_U]') is not null
  DROP PROCEDURE [dbo].[P_Menu_U];
GO  
CREATE PROCEDURE [dbo].[P_Menu_U](
    @Context xml,
    @MenuID int,
    @ParentMenuID int,
    @Sequence int,
    @URL nvarchar(512),
    @Description xml,
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

      -- Execute the CRUD functionality.   
      update [dbo].[Menu]
         set [ParentMenuID] = @ParentMenuID,
             [Sequence] = @Sequence,
             [URL] = @URL,
             [Description] = @Description
       where [MenuID] = @MenuID;
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_Menu_D]') is not null
  DROP PROCEDURE [dbo].[P_Menu_D];
GO  
CREATE PROCEDURE [dbo].[P_Menu_D](
    @Context xml,
    @MenuID int,
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

      -- Execute the CRUD functionality.   
      delete
        from [dbo].[Menu]
       where [MenuID] = @MenuID;
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_Menu_I]') is not null
  DROP PROCEDURE [dbo].[P_Menu_I];
GO  
CREATE PROCEDURE [dbo].[P_Menu_I](
    @Context xml,
    @MenuID int,
    @ParentMenuID int,
    @Sequence int,
    @URL nvarchar(512),
    @Description xml,
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

      -- Execute the CRUD functionality.   
      insert into [dbo].[Menu] (
        [MenuID],
        [ParentMenuID],
        [Sequence],
        [URL],
        [Description])
      values (
        @MenuID,
        @ParentMenuID,
        @Sequence,
        @URL,
        @Description);
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_MenuPermission_D]') is not null
  DROP PROCEDURE [dbo].[P_MenuPermission_D];
GO  
CREATE PROCEDURE [dbo].[P_MenuPermission_D](
    @Context xml,
    @MenuID int,
    @PermissionID int,
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

      -- Execute the CRUD functionality.   
      delete
        from [dbo].[MenuPermission]
       where [MenuID] = @MenuID
         and [PermissionID] = @PermissionID;
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_MenuPermission_I]') is not null
  DROP PROCEDURE [dbo].[P_MenuPermission_I];
GO  
CREATE PROCEDURE [dbo].[P_MenuPermission_I](
    @Context xml,
    @MenuID int,
    @PermissionID int,
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

      -- Execute the CRUD functionality.   
      insert into [dbo].[MenuPermission] (
        [MenuID],
        [PermissionID])
      values (
        @MenuID,
        @PermissionID);
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_Page_U]') is not null
  DROP PROCEDURE [dbo].[P_Page_U];
GO  
CREATE PROCEDURE [dbo].[P_Page_U](
    @Context xml,
    @PageID int,
    @Name nvarchar(255),
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

      -- Execute the CRUD functionality.   
      update [dbo].[Page]
         set [Name] = @Name
       where [PageID] = @PageID;
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_Page_D]') is not null
  DROP PROCEDURE [dbo].[P_Page_D];
GO  
CREATE PROCEDURE [dbo].[P_Page_D](
    @Context xml,
    @PageID int,
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

      -- Execute the CRUD functionality.   
      delete
        from [dbo].[Page]
       where [PageID] = @PageID;
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_Page_I]') is not null
  DROP PROCEDURE [dbo].[P_Page_I];
GO  
CREATE PROCEDURE [dbo].[P_Page_I](
    @Context xml,
    @PageID int OUTPUT,
    @Name nvarchar(255),
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

      -- Execute the CRUD functionality.   
      insert into [dbo].[Page] (
        [Name])
      values (
        @Name);
      set @PageID = scope_identity();
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_PagePermission_D]') is not null
  DROP PROCEDURE [dbo].[P_PagePermission_D];
GO  
CREATE PROCEDURE [dbo].[P_PagePermission_D](
    @Context xml,
    @PageID int,
    @PermissionID int,
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

      -- Execute the CRUD functionality.   
      delete
        from [dbo].[PagePermission]
       where [PageID] = @PageID
         and [PermissionID] = @PermissionID;
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_PagePermission_I]') is not null
  DROP PROCEDURE [dbo].[P_PagePermission_I];
GO  
CREATE PROCEDURE [dbo].[P_PagePermission_I](
    @Context xml,
    @PageID int,
    @PermissionID int,
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

      -- Execute the CRUD functionality.   
      insert into [dbo].[PagePermission] (
        [PageID],
        [PermissionID])
      values (
        @PageID,
        @PermissionID);
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_Permission_U]') is not null
  DROP PROCEDURE [dbo].[P_Permission_U];
GO  
CREATE PROCEDURE [dbo].[P_Permission_U](
    @Context xml,
    @PermissionID int,
    @RequiredPermissionID int,
    @Action char(1),
    @Name nvarchar(128),
    @Description xml,
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

      -- Execute the CRUD functionality.   
      update [dbo].[Permission]
         set [RequiredPermissionID] = @RequiredPermissionID,
             [Action] = @Action,
             [Name] = @Name,
             [Description] = @Description,
             [UserModified] = @MetaDataUserID,
             [DateModified] = getdate()
       where [PermissionID] = @PermissionID;
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_Permission_D]') is not null
  DROP PROCEDURE [dbo].[P_Permission_D];
GO  
CREATE PROCEDURE [dbo].[P_Permission_D](
    @Context xml,
    @PermissionID int,
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

      -- Execute the CRUD functionality.   
      delete
        from [dbo].[Permission]
       where [PermissionID] = @PermissionID;
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_Permission_I]') is not null
  DROP PROCEDURE [dbo].[P_Permission_I];
GO  
CREATE PROCEDURE [dbo].[P_Permission_I](
    @Context xml,
    @PermissionID int,
    @RequiredPermissionID int,
    @Action char(1),
    @Name nvarchar(128),
    @Description xml,
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

      -- Execute the CRUD functionality.   
      insert into [dbo].[Permission] (
        [PermissionID],
        [RequiredPermissionID],
        [Action],
        [Name],
        [Description],
        [UserCreate],
        [UserModified])
      values (
        @PermissionID,
        @RequiredPermissionID,
        @Action,
        @Name,
        @Description,
        @ContextUserID,
        @MetaDataUserID);
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_Role_U]') is not null
  DROP PROCEDURE [dbo].[P_Role_U];
GO  
CREATE PROCEDURE [dbo].[P_Role_U](
    @Context xml,
    @RoleID int,
    @Description xml,
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

      -- Execute the CRUD functionality.   
      update [dbo].[Role]
         set [Description] = @Description,
             [UserModified] = @MetaDataUserID,
             [DateModified] = getdate()
       where [RoleID] = @RoleID;
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_Role_D]') is not null
  DROP PROCEDURE [dbo].[P_Role_D];
GO  
CREATE PROCEDURE [dbo].[P_Role_D](
    @Context xml,
    @RoleID int,
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

      -- Execute the CRUD functionality.   
      delete
        from [dbo].[Role]
       where [RoleID] = @RoleID;
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_Role_I]') is not null
  DROP PROCEDURE [dbo].[P_Role_I];
GO  
CREATE PROCEDURE [dbo].[P_Role_I](
    @Context xml,
    @RoleID int OUTPUT,
    @Description xml,
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

      -- Execute the CRUD functionality.   
      insert into [dbo].[Role] (
        [Description],
        [UserCreate],
        [UserModified])
      values (
        @Description,
        @ContextUserID,
        @MetaDataUserID);
      set @RoleID = scope_identity();
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_RolePermission_U]') is not null
  DROP PROCEDURE [dbo].[P_RolePermission_U];
GO  
CREATE PROCEDURE [dbo].[P_RolePermission_U](
    @Context xml,
    @RolePermissionID int,
    @RoleID int,
    @PermissionID int,
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

      -- Execute the CRUD functionality.   
      update [dbo].[RolePermission]
         set [RoleID] = @RoleID,
             [PermissionID] = @PermissionID,
             [UserModified] = @MetaDataUserID,
             [DateModified] = getdate()
       where [RolePermissionID] = @RolePermissionID;
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_RolePermission_D]') is not null
  DROP PROCEDURE [dbo].[P_RolePermission_D];
GO  
CREATE PROCEDURE [dbo].[P_RolePermission_D](
    @Context xml,
    @RolePermissionID int,
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

      -- Execute the CRUD functionality.   
      delete
        from [dbo].[RolePermission]
       where [RolePermissionID] = @RolePermissionID;
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_RolePermission_I]') is not null
  DROP PROCEDURE [dbo].[P_RolePermission_I];
GO  
CREATE PROCEDURE [dbo].[P_RolePermission_I](
    @Context xml,
    @RolePermissionID int OUTPUT,
    @RoleID int,
    @PermissionID int,
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

      -- Execute the CRUD functionality.   
      insert into [dbo].[RolePermission] (
        [RoleID],
        [PermissionID],
        [UserCreate],
        [UserModified])
      values (
        @RoleID,
        @PermissionID,
        @ContextUserID,
        @MetaDataUserID);
      set @RolePermissionID = scope_identity();
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_User_U]') is not null
  DROP PROCEDURE [dbo].[P_User_U];
GO  
CREATE PROCEDURE [dbo].[P_User_U](
    @Context xml,
    @UserID int,
    @UserName nvarchar(320),
    @Password nvarchar(32),
    @Email nvarchar(320),
    @LanguageLCID int,
    @Name nvarchar(128),
    @FirstName nvarchar(64),
    @Lockout bit,
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

      -- Execute the CRUD functionality.   
      update [dbo].[User]
         set [UserName] = @UserName,
             [Password] = @Password,
             [Email] = @Email,
             [LanguageLCID] = @LanguageLCID,
             [Name] = @Name,
             [FirstName] = @FirstName,
             [Lockout] = @Lockout,
             [UserModified] = @MetaDataUserID,
             [DateModified] = getdate()
       where [UserID] = @UserID;
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_User_D]') is not null
  DROP PROCEDURE [dbo].[P_User_D];
GO  
CREATE PROCEDURE [dbo].[P_User_D](
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

      -- Execute the CRUD functionality.   
      delete
        from [dbo].[User]
       where [UserID] = @UserID;
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_User_I]') is not null
  DROP PROCEDURE [dbo].[P_User_I];
GO  
CREATE PROCEDURE [dbo].[P_User_I](
    @Context xml,
    @UserID int OUTPUT,
    @UserName nvarchar(320),
    @Password nvarchar(32),
    @Email nvarchar(320),
    @LanguageLCID int,
    @Name nvarchar(128),
    @FirstName nvarchar(64),
    @Lockout bit,
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

      -- Execute the CRUD functionality.   
      insert into [dbo].[User] (
        [UserName],
        [Password],
        [Email],
        [LanguageLCID],
        [Name],
        [FirstName],
        [Lockout],
        [UserCreate],
        [UserModified])
      values (
        @UserName,
        @Password,
        @Email,
        @LanguageLCID,
        @Name,
        @FirstName,
        @Lockout,
        @ContextUserID,
        @MetaDataUserID);
      set @UserID = scope_identity();
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_UserRole_U]') is not null
  DROP PROCEDURE [dbo].[P_UserRole_U];
GO  
CREATE PROCEDURE [dbo].[P_UserRole_U](
    @Context xml,
    @UserRoleID int,
    @UserID int,
    @RoleID int,
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

      -- Execute the CRUD functionality.   
      update [dbo].[UserRole]
         set [UserID] = @UserID,
             [RoleID] = @RoleID,
             [UserModified] = @MetaDataUserID,
             [DateModified] = getdate()
       where [UserRoleID] = @UserRoleID;
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_UserRole_D]') is not null
  DROP PROCEDURE [dbo].[P_UserRole_D];
GO  
CREATE PROCEDURE [dbo].[P_UserRole_D](
    @Context xml,
    @UserRoleID int,
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

      -- Execute the CRUD functionality.   
      delete
        from [dbo].[UserRole]
       where [UserRoleID] = @UserRoleID;
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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
if object_id('[dbo].[P_UserRole_I]') is not null
  DROP PROCEDURE [dbo].[P_UserRole_I];
GO  
CREATE PROCEDURE [dbo].[P_UserRole_I](
    @Context xml,
    @UserRoleID int OUTPUT,
    @UserID int,
    @RoleID int,
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

      -- Execute the CRUD functionality.   
      insert into [dbo].[UserRole] (
        [UserID],
        [RoleID],
        [UserCreate],
        [UserModified])
      values (
        @UserID,
        @RoleID,
        @ContextUserID,
        @MetaDataUserID);
      set @UserRoleID = scope_identity();
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


