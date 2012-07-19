@echo off

REM
REM []-------------------------[]
REM | check INPUT/ENVIRONMENT   |
REM []-------------------------[]
REM
set _DBServer=%1
set _DBName=%2
set _UserName=%3
set _Password=%4
set _LogDir=.\Log

if [%_DBName%]==[] set _DBName="PensioB.Tasks"


echo _DBServer=%_DBServer%
echo _DBName=%_DBName%
echo _UserName=%_userName%
echo _Password=%_Password%
echo _LogDir=%_LogDir%

REM
REM []---------------------------[]
REM | Actual Execution of scripts |
REM []---------------------------[]
REM
echo.
if exist .\Log\*.log erase %_LogDir%\*.log >nul

time /T
echo Creating %_DBName% DB...
set _ScriptFiles=runme_tasks.txt
for /F "tokens=*" %%A in (%_ScriptFiles%) do (
  call ExecScript.bat "%%A" %_DBServer% %_DBName% %_UserName% %_Password%
  if errorlevel 1 goto show_logfile
)
if exist .\Log\*.log erase %_LogDir%\*.log >nul
goto end

:show_logfile
pause

:end
set _ScriptFiles=
set _DBServer=
set _DBName=
subst B: /D
