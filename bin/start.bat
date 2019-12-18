REM set PATH=%PATH%;derby\bin
echo %CLASSPATH%

REM Create database folder if not present already.
if not exist %cd%\database mkdir ..\database

java  -Ddatabase.location=..\database -jar ..\target\FtcGlasses-1.0-SNAPSHOT-jar-with-dependencies.jar