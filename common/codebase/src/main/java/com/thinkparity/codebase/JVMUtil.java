/*
 * Created On:  28-Mar-07 1:19:02 PM
 */
package com.thinkparity.codebase;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class JVMUtil {

    /** The jar argument. */
    private static final String ARG_JAR = "-jar";

    /** The *nix java binary name. */
    private static final String BINARY_NIX = "java";

    /** The win32 java binary name. */
    private static final String BINARY_WIN32 = "javaw.exe";

    /** An instance of <code>JavaUtil</code>. */
    private static JVMUtil INSTANCE;

    /** A message format pattern for the java command. */
    private static final String JAVA_COMMAND_PATTERN = "{0}{1}bin{1}{2}";

    /**
     * Obtain an instance of java util.
     * 
     * @return An instance of <code>JavaUtil</code>.
     */
    public static JVMUtil getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new JVMUtil();
        }
        return INSTANCE;
    }

    /** The platform specific java command <code>String</code>. */
    private final String javaCommand;

    /**
     * Create JavaUtil.
     *
     */
    private JVMUtil() {
        super();
        final String javaHome = System.getProperty("java.home");
        final String javaBinary;
        switch (OSUtil.getOs().getPlatform()) {
        case WIN32:
            javaBinary = BINARY_WIN32;
            break;
        case LINUX:
        case UNIX:
            javaBinary = BINARY_NIX;
            break;
        default:
            throw Assert.createUnreachable("Unsupported operating system.");
        }
        javaCommand = MessageFormat.format(JAVA_COMMAND_PATTERN, javaHome,
                File.separator, javaBinary);
    }

    /**
     * Create and start a process to run a jar file.
     * 
     * 
     * @param executionDirectory
     *            The directory <code>File</code> from which to start the
     *            process.
     * @param jarFile
     *            The jar <code>File</code> to execute.
     * @param jvmArgs
     *            Optional jvm arguments to be passed to the process in the
     *            format of key=value.
     * @return The <code>Process</code>.
     * @throws IOException
     *             if the process cannot be started
     */
    public Process executeJar(final File executionDirectory,
            final File jarFile, final String... jvmArgs) throws IOException {
        final List<String> executeJarCommand = new ArrayList<String>();
        executeJarCommand.add(javaCommand);
        for (final String jvmArg : jvmArgs)
            executeJarCommand.add(jvmArg);
        executeJarCommand.add(ARG_JAR);
        executeJarCommand.add(jarFile.getAbsolutePath());
        final ProcessBuilder pb = new ProcessBuilder();
        pb.command(executeJarCommand);
        pb.directory(executionDirectory);
        return pb.start();
    }
}
