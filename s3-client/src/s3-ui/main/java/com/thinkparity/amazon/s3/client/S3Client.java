/*
 * Created On:  20-Jun-07 9:54:07 AM
 */
package com.thinkparity.amazon.s3.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.config.ConfigFactory;
import com.thinkparity.codebase.constraint.IllegalValueException;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import org.apache.commons.cli.ParseException;

import com.thinkparity.amazon.s3.service.S3Authentication;
import com.thinkparity.amazon.s3.service.client.ServiceException;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3Client {

    private static final int MAX_FILE_SIZE;

    static {
        MAX_FILE_SIZE = 64;

        System.setProperty("networkaddress.cache.ttl", String.valueOf(90));
        System.setProperty("networkaddress.cache.negative.ttl", String.valueOf(90));

        final Properties log4JProperties = ConfigFactory.newInstance("log4j.properties");
        final File defaultFile = new File(log4JProperties.getProperty("log4j.appender.DEFAULT.File"));
        final File loggingDirectory = defaultFile.getParentFile();
        if (!defaultFile.getParentFile().exists()) {
            Assert.assertTrue(loggingDirectory.mkdirs(),
                    "Could not create logging directory {0}.", loggingDirectory);
        }
    }

    /**
     * Run an s3 client.
     * 
     * @param args
     *            A command line argument <code>String[]</code>.
     */
    public static void main(final String[] args) {
        try {
            final S3ClientCLI cli = new S3ClientCLI();
            cli.define();
            try {
                cli.parse(args);
            } catch (final ParseException px) {
                cli.printUsage();
                System.exit(1);
            }
            new S3Client(cli).invoke();
        } catch (final Exception x) {
            new Log4JWrapper().logFatal(x, "Could not run client.");
            System.exit(1);
        }
    }

    /** A command line interpreter. */
    private final S3ClientCLI cli;

    /** A log4j wrapper. */
    private final Log4JWrapper logger;

    /**
     * Create S3Client.
     *
     */
    private S3Client(final S3ClientCLI cli) {
        super();
        this.cli = cli;
        this.logger = new Log4JWrapper(getClass());
    }

    /**
     * Run the specified command.
     * 
     * @param command
     *            A command <code>String</code>.
     */
    private void invoke() {
        // create the authentication
        final S3Authentication authentication = new S3Authentication();
        try {
            authentication.setAccessKeyId(readFile(cli.getAmazonAccessKeyId()));
        } catch (final IOException iox) {
            logger.logFatal("Could not read amazon access key id {0}.", cli.getAmazonAccessKeyId());
            throw new IllegalArgumentException("Could not read amazon secret access key.");
        }
        try {
            authentication.setSecretAccessKey(readFileBytes(cli.getAmazonSecretAccessKey()));
        } catch (final IOException iox) {
            logger.logFatal("Could not read amazon secret access key {0}.", cli.getAmazonSecretAccessKey());
            throw new IllegalArgumentException("Could not read amazon secret access key.");
        }
        // create the context
        final S3CommandContext context = new S3CommandContext();
        context.setAuthentication(authentication);
        // create the command
        final S3Command s3Command = newCommand(cli.getCommand());
        // initialize
        s3Command.initialize(context);
        // invoke
        try {
            invoke(s3Command);
        } catch (final ServiceException sx) {
            final Throwable cause = sx.getCause();
            if (null == cause) {
                logger.logFatal(sx, "An unexpected error has occured.");
                context.getConsole().println("An unexpected error has occured.  {0}", sx.getMessage());
            } else {
                if (IllegalValueException.class.isAssignableFrom(cause.getClass())) {
                    final IllegalValueException ivx = (IllegalValueException) sx.getCause();
                    final String fatalMessagePattern;
                    switch (ivx.getReason()) {
                    case FORMAT:
                        fatalMessagePattern = "{0} ({1}) does not match expected format.";
                        break;
                    case NULL:
                        fatalMessagePattern = "{0} is null.";
                        break;
                    case TOO_LARGE:
                        fatalMessagePattern = "{0} ({1}) is too large.";
                        break;
                    case TOO_LONG:
                        fatalMessagePattern = "{0} ({1}) is too long.";
                        break;
                    case TOO_SHORT:
                        fatalMessagePattern = "{0} ({1}) is too short.";
                        break;
                    case TOO_SMALL:
                        fatalMessagePattern = "{0} ({1}) is too small.";
                        break;
                    default:
                        fatalMessagePattern = "{0} ({1}) is illegal.";
                        break;
                    }
                    logger.logFatal(fatalMessagePattern, ivx.getName(), ivx.getValue());
                    context.getConsole().println(fatalMessagePattern, ivx.getName(), ivx.getValue());
                } else {
                    logger.logFatal(sx, "An unexpected error has occured.");
                    context.getConsole().println("An unexpected error has occured.  {0}", sx.getMessage());
                    throw sx;
                }
            }
        }
    }

    /**
     * Run the s3 client command.
     *
     */
    private void invoke(final S3Command command) {
        logger.logTraceId();
        logger.logVariable("command", command);
        logger.logInfo("Invoking command {0}.", command.getName());
        command.invoke();
        logger.logTraceId();
    }

    /**
     * Locate the file from the pathname.
     * 
     * @param pathname
     *            A pathname <code>String</code>.
     * @return A <code>File</code>.
     */
    private File locateFile(final String pathname) {
        final File file = new File(pathname);
        if (!file.exists())
            throw new IllegalArgumentException(pathname + " does not exist.");
        if (!file.isFile())
            throw new IllegalArgumentException(pathname + " is not a file.");
        if (!file.canRead())
            throw new IllegalArgumentException(pathname + " cannot be read.");
        if (file.canWrite())
            throw new IllegalArgumentException(pathname + " can be written to.");
        return file;
    }

    /**
     * Create an instance of a command.
     * 
     * @param command
     *            A command <code>String</code>.
     * @return A <code>Command</code>.
     */
    private S3Command newCommand(final String command) {
        try {
            final String commandClassName = MessageFormat.format(
                    "com.thinkparity.amazon.s3.client.command.{0}", command);
            final Class<?> commandClass = Class.forName(commandClassName);
            return (S3Command) commandClass.newInstance();
        } catch (final ClassNotFoundException cnfx) {
            throw new RuntimeException("Command " + command + " does not exist.");
        } catch (final IllegalAccessException iax) {
            throw new RuntimeException("Command " + command + " does not exist.");
        } catch (final InstantiationException ix) {
            throw new RuntimeException("Command " + command + " does not exist.");
        }
    }

    private String readFile(final String pathname) throws IOException {
        return new String(readFileBytes(pathname));
    }

    private byte[] readFileBytes(final String pathname) throws IOException {
        final File file = locateFile(pathname);
        final int size = (int) file.length();
        if (size > MAX_FILE_SIZE)
            throw new IllegalArgumentException("Invalid key file.");
        final InputStream inputStream = new FileInputStream(file);
        try {
            final byte[] fileBytes = new byte[size];
            inputStream.read(fileBytes);
            return fileBytes;
        } finally {
            inputStream.close();
        }
    }
}
