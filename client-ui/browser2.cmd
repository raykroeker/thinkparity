@echo off
setlocal

set B2_CP=.\browser2-1.0-SNAPSHOT.jar
set B2_CP=%B2_CP%;codebase-1.0-SNAPSHOT.jar
set B2_CP=%B2_CP%;model-1.0-SNAPSHOT.jar
set B2_CP=%B2_CP%;xpp3-1.1.3.3.jar
set B2_CP=%B2_CP%;xstream-1.1.2.jar
set B2_CP=%B2_CP%;smack-2.0.0.jar
set B2_CP=%B2_CP%;smackx-2.0.0.jar
set B2_CP=%B2_CP%;commons-codec-1.3.jar
set B2_CP=%B2_CP%;substance-2.2-SNAPSHOT.jar
set B2_CP=%B2_CP%;log4j-1.2.13.jar
set B2_CP=%B2_CP%;nativeskin-1.2.12.jar
set B2_MAIN=com.thinkparity.browser.Browser2
set B2_OPTS=-Djava.library.path=.\

java %B2_OPTS% -cp %B2_CP% %B2_MAIN%

endlocal
