@echo off

@rem ***************************************************************************
@rem Setup browser2 java library path
@rem ***************************************************************************
set PATH=C:\Program Files\Java\jre1.5.0_06\bin;..\lib\win32;%PATH%

@rem ***************************************************************************
@rem Setup browser2 classpath
@rem ***************************************************************************
set B2_CP=..\core\browser2-startup-1.0-SNAPSHOT.jar
set B2_CP=%B2_CP%;..\core\browser2-1.0-SNAPSHOT.jar
set B2_CP=%B2_CP%;..\core\codebase-1.0-SNAPSHOT.jar
set B2_CP=%B2_CP%;..\core\model-1.0-SNAPSHOT.jar
set B2_CP=%B2_CP%;..\lib\xpp3-1.1.3.3.jar
set B2_CP=%B2_CP%;..\lib\xstream-1.1.2.jar
set B2_CP=%B2_CP%;..\lib\smack-2.0.0.jar
set B2_CP=%B2_CP%;..\lib\smackx-2.0.0.jar
set B2_CP=%B2_CP%;..\lib\commons-codec-1.3.jar
set B2_CP=%B2_CP%;..\lib\log4j-1.2.13.jar
set B2_CP=%B2_CP%;..\lib\nativeskin-6.2.jar
set B2_CP=%B2_CP%;..\lib\swing-layout-1.0.jar
set B2_CP=%B2_CP%;..\lib\hsqldb-1.8.0.2.jar
set B2_CP=%B2_CP%;..\lib\lucene-1.9.1.jar
set B2_CP=%B2_CP%;..\lib\jdic-0.9.1.jar

@rem ***************************************************************************
@rem Setup browser2 main class
@rem ***************************************************************************
set B2_MAIN=com.thinkparity.browser2.startup.Browser2
