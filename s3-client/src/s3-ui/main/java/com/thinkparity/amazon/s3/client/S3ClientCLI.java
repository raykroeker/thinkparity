/*
 * Created On:  21-Jun-07 9:17:12 AM
 */
package com.thinkparity.amazon.s3.client;

import org.apache.commons.cli.*;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class S3ClientCLI {

    /** The amazon web-services access key id. */
    private Option awsAccessKeyId;

    /** The command option. */
    private Option awsSecretAccessKey;

    /** The command option. */
    private Option command;

    /** The parsed command line. */
    private CommandLine commandLine;

    /** The command line parser. */
    private final CommandLineParser commandLineParser;

    /** The command line options. */
    private final Options options;

    /**
     * Create S3ClientCLI.
     *
     */
    S3ClientCLI() {
        super();
        this.commandLineParser = new GnuParser();
        this.options = new Options();
    }

    /**
     * Obtain the amazon access key id.
     * 
     * @return The access key id file <code>String</code>.
     */
    public String getAmazonAccessKeyId() {
        return commandLine.getOptionValue(awsAccessKeyId.getOpt());
    }

    /**
     * Obtain the amazon secret access key.
     * 
     * @return The amazon secret access key file <code>String</code>.
     */
    public String getAmazonSecretAccessKey() {
        return commandLine.getOptionValue(awsSecretAccessKey.getOpt());
    }

    /**
     * Obtain the command.
     * 
     * @return The command <code>String</code>.
     */
    public String getCommand() {
        return commandLine.getOptionValue(command.getOpt());
    }

    /**
     * Define the client command line options.
     *
     */
    void define() {
        command = new Option("c", "command", true, "The client command to issue.");
        command.setRequired(true);
        command.setOptionalArg(false);

        awsAccessKeyId = new Option("aaki", "amazonaccesskeyid", true,
                "The location of the amazon web-services access key id file.");
        awsAccessKeyId.setRequired(true);
        awsAccessKeyId.setArgName("keyidfile");
        awsAccessKeyId.setOptionalArg(false);

        awsSecretAccessKey = new Option("asak",
                "amazonsecretaccesskey", true,
                "The location of the amazon web-services secret key file.");
        awsSecretAccessKey.setRequired(true);
        awsSecretAccessKey.setArgName("secretkeyfile");
        awsSecretAccessKey.setOptionalArg(false);
        
        options.addOption(command);
        options.addOption(awsAccessKeyId);
        options.addOption(awsSecretAccessKey);
    }

    /**
     * Parse the command line arguments.
     * 
     * @param arguments
     *            An arguments <code>String[]</code>.
     */
    void parse(final String[] arguments) throws ParseException {
        commandLine = commandLineParser.parse(options, arguments, true);
    }

    /**
     * Print the command line usage.
     *
     */
    void printUsage() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("S3Client", options);
    }
}
