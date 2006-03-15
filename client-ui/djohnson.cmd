@echo off
setlocal

@rem HACK
set PATH=C:\Program Files\Java\jre1.5.0_06\bin;.\%PATH%

set B2_OPTS=-Dparity.workspace="%APPDATA%\Parity Software\Parity.dJohnson"

set B2_CP=.\browser2-startup-1.0-SNAPSHOT.jar
set B2_CP=%B2_CP%;.\browser2-1.0-SNAPSHOT.jar
set B2_CP=%B2_CP%;.\codebase-1.0-SNAPSHOT.jar
set B2_CP=%B2_CP%;.\model-1.0-SNAPSHOT.jar
set B2_CP=%B2_CP%;.\xpp3-1.1.3.3.jar
set B2_CP=%B2_CP%;.\xstream-1.1.2.jar
set B2_CP=%B2_CP%;.\smack-2.0.0.jar
set B2_CP=%B2_CP%;.\smackx-2.0.0.jar
set B2_CP=%B2_CP%;.\commons-codec-1.3.jar
set B2_CP=%B2_CP%;.\log4j-1.2.13.jar
set B2_CP=%B2_CP%;.\nativeskin-6.2.jar
set B2_CP=%B2_CP%;.\swing-layout-1.0.jar
set B2_CP=%B2_CP%;.\hsqldb-1.8.0.2.jar
set B2_CP=%B2_CP%;.\lucene-1.9.1.jar

set B2_MAIN=com.thinkparity.browser2.startup.Browser2

set B2_ARGS=

java %B2_OPTS% -cp %B2_CP% %B2_MAIN% %B2_ARGS%

endlocal
