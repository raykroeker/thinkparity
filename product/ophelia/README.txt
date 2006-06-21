  Ophelia (character)
    Ophelia is a female character from Hamlet by William Shakespeare. She is a
young noblewoman of Denmark, daughter to Polonius, sister to Laertes, and love
interest of Hamlet. She is a loyal and duitiful daughter, who obeys her father's
instruction to "lock herself from [Hamlet's] resort."


Build Tools:
    1.  The Apache Ant Project:   1.6.5   http://ant.apache.org
    2.  JUnit:                    3.8.1   http://www.junit.org
    3.  Launch4J                  2.1.3   http://launch4j.sourceforge.net

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
    +>ophelia
        +>common                    // All location generic code (non local;
            +>codebase              // non-remote) code lives in common.
            +>junitx
            +>migrator
        +>local                     // All local generic code lives in local.
            +>browser
            +>model
            +>thinkparity
        +>vendor                    // All vendor libraries live in vendor
            +>commons-codec         // organized by the library then version.
            +>hsqldb
            +>i686
                +>win32
                    +>jdic
            +>jdic
            +>junit
            +>log4j
            +>lucene
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
        +ophelia - com.thinkparity.browser.Browser.launch   // Eclipse Launch
        +README.txt

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
