@echo off

setlocal
if [%tasks_trunk%]==[] set tasks_trunk=%development%\PPWCode.Kit.Tasks

sc query PPWCode.Kit.Tasks.API_I >nul
if errorlevel 1060 ( 
  installutil %tasks_trunk%\Services\Tasks\PPWCode.Kit.Tasks.Server.NTServiceHost.exe
)

endlocal