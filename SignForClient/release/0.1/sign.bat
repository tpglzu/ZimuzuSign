set current=%~dp0
set confdir=%current%conf
set libdir=%current%lib
echo %current%
java -Drootdir=%current% -Dconfdir=%confdir% -jar %libdir%/sign.jar
@echo off