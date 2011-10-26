@echo off

sc query PPWCode.Kit.Tasks.API_I >nul
if errorlevel 0 sc start PPWCode.Kit.Tasks.API_I
