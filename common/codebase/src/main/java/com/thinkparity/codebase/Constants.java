/*
 * Created On: Jun 22, 2006 12:52:04 PM
 */
package com.thinkparity.codebase;

import java.util.TimeZone;

/**
 * <b>Title:</b>thinkParity Constants <b>Description:</b>thinkParity Global
 * Constants.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Constants {

    public static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getDefault();

    public static final TimeZone UNIVERSAL_TIME_ZONE = TimeZone.getTimeZone("Universal");

    /**
     * <b>Title:</b>Checksum Algorithms<br>
     * <b>Description:</b>Enumerated checksum algorithms.<br>
     */
    public enum ChecksumAlgorithm { MD5 }

    /**
     * <b>Title:</b>thinkParity File Constants<br>
     * <b>Description:</b>thinkParity file constants.<br>
     */
    public static final class Directory {
        public static final String TEMP_DIR_PREFIX = "thinkParity.";
    }

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
        public static final String MAX_BACKUP_INDEX = "1";
        public static final String MAX_FILE_SIZE = "3MB";
        public static final String METRICS_LAYOUT_CONVERSION_PATTERN = "%d{ISO8601} %m%n";
        public static final String SDF_DATE_PATTERN = "yyyy-MM-dd Z";
        public static final String SDF_TIME_PATTERN = "HH:mm:ss.SSS Z";
        public static final String SDF_TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS Z";
        public static final String SERVICE_CONVERSION_PATTERN = "%d{ISO8601} %x %t %m%n";
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
