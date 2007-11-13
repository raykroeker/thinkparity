/*
 * Created On:  9-Nov-07 3:28:56 PM
 */
package com.thinkparity.ophelia.model.util.daemon;

/**
 * <b>Title:</b>thinkParity Ophelia Model Util Daemon<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface Daemon extends Runnable {

    /**
     * Start the daemon.
     * 
     */
    void start();

    /**
     * Stop the daemon.
     * 
     */
    void stop();
}
