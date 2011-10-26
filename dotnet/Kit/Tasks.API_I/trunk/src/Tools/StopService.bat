@echo off

sc query PPWCode.Kit.Tasks.API_I >nul
if errorlevel 0 sc stop PPWCode.Kit.Tasks.API_I
