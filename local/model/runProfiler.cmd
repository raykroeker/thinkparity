@echo off
setlocal

set PATH=%EJP_HOME%\lib;%PATH%

set PR_JAVA="%JAVA_HOME%\bin\java.exe"

set PR_OPTS=-Xruntracer:config=runProfiler.config

set MVN_REPO=%HOME%\.m2\repository
set PR_CP=.\target\classes;.\target\test-classes
set PR_CP=%PR_CP%;%MVN_REPO%\com\thinkparity\parity\codebase\1.0-SNAPSHOT\codebase-1.0-SNAPSHOT.jar
set PR_CP=%PR_CP%;%MVN_REPO%\commons-codec\commons-codec\1.3\commons-codec-1.3.jar
set PR_CP=%PR_CP%;%MVN_REPO%\xpp3\xpp3\1.1.3.3\xpp3-1.1.3.3.jar
set PR_CP=%PR_CP%;%MVN_REPO%\jivesoftware\smack\2.0.0\smack-2.0.0.jar
set PR_CP=%PR_CP%;%MVN_REPO%\jivesoftware\smackx\2.0.0\smackx-2.0.0.jar
set PR_CP=%PR_CP%;%MVN_REPO%\xstream\xstream\1.1.2\xstream-1.1.2.jar
set PR_CP=%PR_CP%;%MVN_REPO%\log4j\log4j\1.2.13\log4j-1.2.13.jar
set PR_CP=%PR_CP%;%MVN_REPO%\com\raykroeker\jUnitX\1.0-SNAPSHOT\jUnitX-1.0-SNAPSHOT.jar
set PR_CP=%PR_CP%;%MVN_REPO%\junit\junit\3.8.1\junit-3.8.1.jar
set PR_CP=%PR_CP%;%MVN_REPO%\hsqldb\hsqldb\1.8.0.2\hsqldb-1.8.0.2.jar

set PR_MAIN=com.thinkparity.model.ProfileRunner

%PR_JAVA% %PR_OPTS% -cp "%PR_CP%" %PR_MAIN%

endlocal
