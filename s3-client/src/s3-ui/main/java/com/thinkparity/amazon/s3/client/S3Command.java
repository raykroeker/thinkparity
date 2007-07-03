/*
 * Created On:  20-Jun-07 9:52:19 AM
 */
package com.thinkparity.amazon.s3.client;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface S3Command {

    /**
     * Obtain the command name.
     * 
     * @return The command name <code>String</code>.
     */
    public String getName();

    /**
     * Initialize the command.
     * 
     * @param context
     *            A <code>S3CommandContext</code>.
     */
    public void initialize(final S3CommandContext context);

    /**
     * Invoke the command.
     *
     */
    public void invoke();
}
