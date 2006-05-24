/*
 * Created On: May 22, 2006 12:41:45 PM
 * $Id$
 */
package com.thinkparity.codebase;

import java.io.File;

/**
 * Contains all parity global constants.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class Constants {

    /** Local execution constants. */
    public static final class Local {

        /** Directory constants. */
        public static class Directories {

            /** The parity install directory. */
            public static final File INSTALL =
                new File(System.getProperty("parity.install"));

            /** The parity workspace directory. */
            public static final File WORKSPACE =
                new File(System.getProperty("parity.workspace"));
        }
    }
}
