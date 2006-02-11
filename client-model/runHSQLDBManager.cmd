@echo off
setlocal

set DBM="%JAVA_HOME%\bin\java.exe"

set MVN_REPO=%HOME%\.m2\repository
set DBM_CP=%MVN_REPO%\hsqldb\hsqldb\1.8.0.2\hsqldb-1.8.0.2.jar

set DBM_MAIN=org.hsqldb.util.DatabaseManagerSwing

set DBM_ARGS

%DBM% %DBM_OPTS% -cp "%DBM_CP%" %DBM_MAIN% %DBM_ARGS%

endlocal
