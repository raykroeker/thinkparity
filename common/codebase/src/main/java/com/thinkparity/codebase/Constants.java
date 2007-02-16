/*
 * Created On: Jun 22, 2006 12:52:04 PM
 */
package com.thinkparity.codebase;

/**
 * <b>Title:</b>thinkParity Constants <b>Description:</b>thinkParity Global
 * Constants.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Constants {
    /**
     * <b>Title:</b>thinkParity File Constants<br>
     * <b>Description:</b>thinkParity file constants.<br>
     */
    public static final class File {
        public static final String TEMP_FILE_PREFIX = "thinkParity.";
    }

    /**
     * <b>Title:</b>thinkParity Jabber Constants<br>
     * <b>Description:</b>thinkParity specific constants for the jabber
     * communications protocol.<br>
     */
    public static final class Jabber {
        public static final String RESOURCE = "parity";
    }

    /**
     * <b>Title:</b>thinkParity Log4J Constants<br>
     * <b>Description:</b>thinkParity specific constants for the log4J logging
     * framework.<br>
     */
    public static final class Log4J {
        public static final String LAYOUT_CONVERSION_PATTERN = "%d{ISO8601} %x %t %p %m%n";
        public static final String MAX_FILE_SIZE = "3MB";
    }

    /**
     * <b>Title:</b>thinkParity XML Constants<br>
     * <b>Description:</b>thinkParity specific xml constants.<br>
     * 
     */
    public static final class Xml {
        public static final String NAME = "query";
        public static final String NAMESPACE = "jabber:iq:parity";
        public static final String RESPONSE_NAMESPACE = "jabber:iq:parity:response";
    }
    /**
     * <b>Title:</b>thinkParity XML RPC Constants<br>
     * <b>Description:</b>thinkParity specific constants for the xml rpc
     * framework.<br>
     */
    public static final class XmlRpc {
        public static final String LIST_ITEM = "list-item";
    }
}
