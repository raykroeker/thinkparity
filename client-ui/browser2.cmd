@echo off
setlocal

set B2_CP=.\browser2-1.0-SNAPSHOT.jar;.\codebase-1.0-SNAPSHOT.jar;.\log4j-1.2.12.jar;.\substance-2.2-SNAPSHOT.jar
set B2_MAIN=com.thinkparity.browser.Browser

java -cp %B2_CP% %B2_MAIN%

endlocal
