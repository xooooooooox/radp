@echo off

set APP_DIR=..\app
set APP_PATTERN=%APP_DIR%\*.jar
for /f "delims=\" %%a in ('dir /b /a-d /o-d %APP_PATTERN%') do (
     set APP_NAME=%APP_DIR%\%%a
)

if "%1" == "start" (
     if exist "%JAVA_HOME%" (
          echo JAVA_HOME: %JAVA_HOME%
          echo JAVA_VERSION:
          java -version
     )
     echo start %APP_NAME%
     start "%APP_NAME%" java -server -Xms1g -Xmx1g -Dfile.encoding=UTF-8 -Dspring.config.additional-location=../conf/ -Dlogging.file.path=../logs/ -jar %APP_NAME%
) else if "%1" == "stop" (
     echo stop %APP_NAME%
     taskkill /fi "WINDOWTITLE eq %APP_NAME%"
)
