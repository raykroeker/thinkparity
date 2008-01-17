/*
 * Created On:  28-Mar-07 1:19:02 PM
 */
package com.thinkparity.codebase;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.common.StringUtil.Separator;

import com.thinkparity.codebase.assertion.Assert;

/**
 * <b>Title:</b>thinkParity CommonModel JVM Util<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class JVMUtil {

    /** The classpath argument. */
    private static final String ARG_CP = "-cp";

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
     * Create and start a process to run a class in a new jvm.
     * 
     * 
     * @param directory
     *            The directory <code>File</code> from which to start the
     *            process.
     * @param jvmArgs
     *            The jvm arguments to be passed to the process in the format of
     *            key=value.
     * @param classpath
     *            A list of classpath <code>File</code>s.
     * @param mainClass
     *            The main <code>Class</code>.
     * @param args
     *            The main class arguments.
     * @return The <code>Process</code>.
     * @throws IOException
     *             if the process cannot be started
     */
    public Process execute(final File directory, final String[] jvmArgs,
            final String[] classpath, final String clasz, final String[] args)
            throws IOException {
        final ProcessBuilder pb = new ProcessBuilder();
        pb.redirectErrorStream(true);
        pb.command(command(jvmArgs, classpath, clasz, args));
        pb.directory(directory);
        return pb.start();
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

    /**
     * Obtain the executable file.
     * 
     * @return A <code>File</code>.
     */
    public File getExecutable() {
        final File executable = new File(javaCommand);
        if (executable.exists()) {
            return executable;
        } else {
            return null;
        }
    }

    /**
     * Print The series of commands that would otherwise be executed to the
     * standard output stream.
     * 
     * @param directory
     *            The directory <code>File</code> from which to start the
     *            process.
     * @param jvmArgs
     *            The jvm arguments to be passed to the process in the format of
     *            key=value.
     * @param mainClass
     *            The main <code>Class</code>.
     * @param args
     *            The main class arguments.
     * @return The <code>Process</code>.
     * @throws IOException
     *             if the process cannot be started
     */
    public void print(final File directory, final String[] jvmArgs,
            final String[] classpath, final String clasz, final String[] args) {
        print(System.out, directory, jvmArgs, classpath, clasz, args);
    }

    /**
     * Print The series of commands that would otherwise be executed.
     * 
     * 
     * @param stream
     *            A <code>PrintStream</code>.
     * @param directory
     *            The directory <code>File</code> from which to start the
     *            process.
     * @param jvmArgs
     *            The jvm arguments to be passed to the process in the format of
     *            key=value.
     * @param clasz
     *            The <code>String</code> name of the class to execute.
     * @param args
     *            The class arguments.
     * @return The <code>Process</code>.
     * @throws IOException
     *             if the process cannot be started
     */
    public void print(final PrintStream stream, final File directory,
            final String[] jvmArgs, final String[] classpath, final String clasz,
            final String[] args) {
        final List<String> command = command(jvmArgs, classpath, clasz, args);
        synchronized (stream) {
            stream.print(getClass().getSimpleName());
            stream.print(" - ");
            stream.println(directory.getAbsolutePath());
            stream.print(getClass().getSimpleName());
            stream.print(" - ");
            for (int i = 0; i < command.size(); i++) {
                if (0 < i)
                    stream.print(Separator.Space);
                stream.print(command.get(i));
            }
            stream.println();
        }
    }

    /**
     * Build the command to execute.
     * 
     * @param jvmArgs
     *            A list of jvm arguments in the -Dkey=value format.
     * @param classpath
     *            A list of classpath files.
     * @param clasz
     *            The <code>String</code> name of the class to execute.
     * @param args
     *            A list of main class arguments.
     * @return A command list of <code>String</code>s.
     */
    private List<String> command(final String[] jvmArgs,
            final String[] classpath, final String clasz, final String[] args) {
        final List<String> commands = new ArrayList<String>();
        commands.add(javaCommand);
        for (final String jvmArg : jvmArgs)
            commands.add(jvmArg);
        if (0 < classpath.length) {
            commands.add(ARG_CP);
            final StringBuilder cp = new StringBuilder();
            for (int i = 0; i < classpath.length; i++) {
                if (0 < i)
                    cp.append(File.pathSeparatorChar);
                cp.append(classpath[i]);
            }
            // if any element contains a string, wrap with quotes
            if (cp.toString().contains(" ")) {
                cp.insert(0, "\"");
                cp.append("\"");
            }
            commands.add(cp.toString());
        }
        commands.add(clasz);
        for (final String arg : args)
            commands.add(arg);
        return commands;
    }
}
