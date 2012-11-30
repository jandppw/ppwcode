@echo off

REM
REM []-------------------------[]
REM | check INPUT/ENVIRONMENT   |
REM []-------------------------[]
REM
set _ScriptFiles=runme.txt
set _DBServer=%1
set _UserName=%2
set _Password=%3
set _LogDir=.\Log

echo _ScriptFiles=%_ScriptFiles%
echo _DBServer=%_DBServer%
echo _UserName=%_userName%
echo _Password=%_Password%
echo _LogDir=%_LogDir%

REM
REM []---------------------------[]
REM | Actual Execution of scripts |
REM []---------------------------[]
REM
echo.
if exist out.txt erase out.txt
if exist .\Log\*.log erase %_LogDir%\*.log >nul

time /T
for /F "tokens=*" %%A in (%_ScriptFiles%) do (
  call ExecScript.bat "%%A" %_DBServer% %_UserName% %_Password%
  if errorlevel 1 goto show_logfile
)

if exist .\Log\*.log erase %_LogDir%\*.log >nul
goto end

:show_logfile
pause

:end
set _ScriptFiles=
set _DBServer=
