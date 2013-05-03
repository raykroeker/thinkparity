/*
 * Created On:  29-Sep-07 3:32:05 PM
 */
package com.thinkparity.adriana.backup.ui;

import org.apache.commons.cli.*;

/**
 * <b>Title:</b>thinkParity Adriana Backup Command Line Interface<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class BackupCLI {

    /** The secret access key id option. */
    private Option awsAccessKeyId;

    /** The secret access key option. */
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
     * Create BackupCLI.
     *
     */
    BackupCLI() {
        super();
        this.commandLineParser = new GnuParser();
        this.options = new Options();
    }

    /**
     * Obtain the amazon access key id.
     * 
     * @return A <code>String</code>.
     */
    public String getAmazonAccessKeyId() {
        return commandLine.getOptionValue(awsAccessKeyId.getOpt());
    }

    /**
     * Obtain the amazon access key.
     * 
     * @return A <code>String</code>.
     */
    public String getAmazonSecretAccessKey() {
        return commandLine.getOptionValue(awsSecretAccessKey.getOpt());
    }

    /**
     * Obtain the command.
     * 
     * @return A <code>String</code>.
     */
    public String getCommand() {
        return commandLine.getOptionValue(command.getOpt());
    }

    /**
     * Define the command line.
     * 
     */
    void define() {
        command = new Option("c", "command", true, "The backup command.");
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
     * @param args
     *            A <code>String[]</code>.
     * @throws ParseException
     *             if the arguments cannot be parsed
     */
    void parse(final String[] args) throws ParseException {
        commandLine = commandLineParser.parse(options, args, true);
    }

    /**
     * Print the command line usage.
     * 
     */
    void printUsage() {
        final HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("BackupClient", options);
    }
}
