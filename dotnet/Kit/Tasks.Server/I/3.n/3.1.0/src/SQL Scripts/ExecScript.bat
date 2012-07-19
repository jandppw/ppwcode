@echo off
REM
REM ExecScript.bat
REM
REM 1. SQL-file
REM 2. Host
REM 3. Database
REM 4/5 Username/Password (optional) 
REM

if not exist %1.sql goto script_not_found
set _LogFileName=%~n1
set _LogFileName=%_LogDir%\%_LogFileName%.LOG

echo Executing script %1 on %2...
echo Executing script %1 on %2... >>%_LogFileName%

if [%4]==[] (
sqlcmd -d %3 -b -e -E -S %2 -i %1.sql >>%_LogFileName%
if errorlevel 1 goto log_error
) ELSE (
sqlcmd -d %3 -b -e -U %4 -P %5 -S %2 -i %1.sql >>%_LogFileName%
if errorlevel 1 goto log_error
)
goto end

:script_not_found
echo script file %1.sql NOT FOUND !
goto show_syntax

:log_error
echo. script %1.sql failed...
goto end

:show_syntax
echo.
echo. ExecScript.bat 1
echo.
echo. 1 : script file
echo. 2 : Server
echo.

:end
set _LogFileName=
