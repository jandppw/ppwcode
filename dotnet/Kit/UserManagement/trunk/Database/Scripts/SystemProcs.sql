use UserManagement
go
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


if object_id(N'dbo.[P_ErrorHandler]') is not null
  drop procedure dbo.[P_ErrorHandler];
go
create procedure dbo.[P_ErrorHandler] (
  @xact_state int,
  @lcid int,
  @default_lcid int,
  @error_number int,
  @error_severity int,
  @error_state int,
  @error_message nvarchar(4000),
  @error_line int,
  @error_procedure sysname,
  @errorinfo nvarchar(4000) OUTPUT,
  @constraint_name sysname OUTPUT)
as
begin
  set nocount on;
  
  declare
    @err int;
    
  set @err = @error_number;
  if (@xact_state <> -1)
  begin
    set @errorinfo = dbo.fnGetErrorMessage(ERROR_MESSAGE(), @lcid, @default_lcid);
    set @constraint_name = dbo.fnGetConstraint(ERROR_MESSAGE());
  end
  else begin
    set @errorinfo = N'PROCEDURE='+@error_procedure+N'('+convert(nvarchar,@error_line)+N') '+
                     N'NUMBER='+convert(nvarchar,@error_number)+N' '+
                     N'SEVERITY='+convert(nvarchar,@error_severity)+N' '+
                     N'STATE='+convert(nvarchar,@error_state)+N' '+
                     N'MESSAGE='+@error_message;
    set @constraint_name = null;                 
  end
    
  return @err  
end
go
