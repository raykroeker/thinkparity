/*
 * Created On: Sun Jun 25 2006 10:42 PDT
 * $Id$
 */
package com.thinkparity.migrator;

/**
 * <b>Title:</b>thinkParity Local Application Entry Point<br>
 * <b>Description:</b>The migrator client's main entry point.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class MigratorMain {

    /**
     * Application entry point.
     *
     * @param args
     *      The command line arguments.
     */
    public static void main(final String[] args) {
        new com.thinkparity.migrator.platform.migrator.Migrator().start();
    }

    /** Create a Application. */
    private MigratorMain() { super(); }
}
