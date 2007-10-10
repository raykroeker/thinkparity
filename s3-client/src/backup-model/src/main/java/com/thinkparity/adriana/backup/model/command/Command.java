/*
 * Created On:  29-Sep-07 6:13:22 PM
 */
package com.thinkparity.adriana.backup.model.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Command {

    /** A list of arguments. */
    private final List<String> arguments;

    /** A command. */
    private String command;

    /**
     * Create Command.
     *
     */
    public Command() {
        super();
        this.arguments = new ArrayList<String>();
    }

    /**
     * Add an argument.
     * 
     * @param argument
     *            A <code>String</code>.
     * @return True if the argument is list modified.
     */
    public boolean addArgument(final String argument) {
        return arguments.add(argument);
    }

    /**
     * Clear the argument list.
     * 
     */
    public void clearArguments() {
        arguments.clear();
    }

    /**
     * Obtain the arguments.
     * 
     * @return A <code>List<String></code>.
     */
    public List<String> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    /**
     * Obtain the number of arguments.
     * 
     * @return An <code>int</code>.
     */
    public Integer getArgumentsSize() {
        return Integer.valueOf(arguments.size());
    }

    /**
     * Obtain the command.
     * 
     * @return A <code>String</code>.
     */
    public String getCommand() {
        return command;
    }

    /**
     * Remove an argument.
     * 
     * @param argument
     *            A <code>String</code>.
     * @return True if the argument list is modified.
     */
    public boolean removeArgument(final String argument) {
        return arguments.add(argument);
    }

    /**
     * Set the command.
     *
     * @param command
     *		A <code>String</code>.
     */
    public void setCommand(final String command) {
        this.command = command;
    }
}
