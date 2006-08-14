  Cordelia
    The youngest of the king's three daughters in the play King Lear, by William
Shakespeare. King Lear at first thinks her ungrateful to him because she refuses
to flatter him as her sisters do; he soon finds out that she is the only one of
the three who genuinely cares for him.  

    The cordelia product is a local (client side) internal product that
manages the auto-upgrade content on the server.

Build Tools:
    1.  The Apache Ant Project:   1.6.5   http://ant.apache.org
    2.  JUnit:                    3.8.1   http://www.junit.org

Build Tools Installation:
    Windows XP
        1.  Extract "apache-ant-1.6.5-bin.zip"
            to "%ProgramFiles%\Apache Software Foundation"
        2.  Create environment variable
            Name:  "ANT_HOME"
            Value: "%ProgramFiles%\Apache Software Foundation\apache-ant-1.6.5"
        3.  Update PATH to include "%ANT_HOME%\bin"
        4.  Test install:
            C:\Documents and Settings\raymond>ant -version
            Apache Ant version 1.6.5 compiled on June 2 2005
        5.  Copy junit.jar to "%ANT_HOME%\lib"
        6.  Install launch4j-2.1.3-1-win32.exe

Checkout Source
    1.  ant checkout

Source Tree
    +>cordelia
    	+>.externalToolBuilders		// Eclipse IDE
        +>common                    // All location generic code (non local;
            +>codebase              // non-remote) code lives in common.
            +>junitx
            +>migrator
        +>local
            +>model
        +>vendor                    // All vendor libraries live in vendor
            +>commons-codec         // organized by the library then version.
            +>junit
            +>log4j
            +>smack
            +>smackx
            +>swing-layout
            +>xpp3
            +>xstream
        +.classpath                                         // Eclipse IDE
        +.cvsignore
        +.project                                           // Eclipse IDE
        +build.filters                                      // Ant Build Filters
        +build.xml                                          // Ant Build Definition
        +LICENSE.txt
        +README.txt
        +*.launch                                           // Eclipse IDE

How To:  Build a Release
    1.  Update build.xml
        1.1  Set the release.version property.
    2.  ant clean
    3.  ant test
    4.  ant release

How To:  Add a vendor library
    1.  Add a directory for the library beneath vendor.
    2.  Add a directory for the library version.
    3.  Add the library jar file.
    4.  Add the library source jar file.
    5.  Add the library javadoc jar file.   (Optional)
    6.  Open the cvs modules file.
        6.1  Add an alias for lib/${library} to vendor/${library}/${version}.
        6.2  Add an embedded module to opheliaLibs.
    7.  Open .classpath
        7.1  Add a classpath entry including the sourcepath.
