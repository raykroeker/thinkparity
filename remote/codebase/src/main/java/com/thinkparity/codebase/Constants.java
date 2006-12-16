/*
 * Created On: Jun 22, 2006 12:52:04 PM
 * $Id$
 */
package com.thinkparity.codebase;

/**
 * <b>Title:</b>thinkParity Remote Constants
 * <b>Description:</b>Contains constants to be used by the thinkParity plugins
 * for the jive server.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class Constants {

    /** Jabber constants. */
    public static final class Jabber {
        public static final String RESOURCE = "parity";
    }

    public static final class Log4J {
        public static final String LAYOUT_CONVERSION_PATTERN = "%d{ISO8601} %x %t %p %m%n";
        public static final String MAX_FILE_SIZE = "3MB";
    }

    /** Xml constants. */
    public static final class Xml {
        public static final String NAME = "query";
        public static final String NAMESPACE = "jabber:iq:parity";
        public static final String RESPONSE_NAMESPACE = "jabber:iq:parity:response";
    }
}
