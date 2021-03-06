/*
 * Oct 11, 2003
 */
package com.thinkparity.bootstrap;

import java.io.IOException;
import java.io.OutputStream;

/**
 * System utility functions.
 * 
 * @author raykroeker@gmail.com
 * @version 1.3.2.1
 */
public final class SystemUtil {

	/**
	 * Used to synchronize access to the system properties.
	 */
	private static final Object SYSTEM_PROPERTY_LOCK = new Object();

	/**
	 * Obtain a system property
	 * @param systemProperty <code>SystemUtil$SystemProperty</code>
	 * @return <code>java.lang.String</code>
	 */
	public static String getSystemProperty(SystemProperty systemProperty) {
		synchronized(SYSTEM_PROPERTY_LOCK) {
			return System.getProperty(systemProperty.toString());
		}
	}

	/**
	 * Obtain a system proeprty
	 * @param systemProperty <code>SystemUtil$SystemProperty</code>
	 * @param defaultValue <code>java.lang.String</code>
	 * @return <code>java.lang.String</code>
	 */
	public static String getSystemProperty(SystemProperty systemProperty, String defaultValue) {
		synchronized(SYSTEM_PROPERTY_LOCK) {
			return System.getProperty(systemProperty.toString(), defaultValue);
		}
	}

	/**
	 * Save the system properties to an output stream.
	 * 
	 * @param out
	 *            Outputstream to save the properties to.
	 * @param comments
	 *            Comments to apply to the output.
	 * @throws IOException
	 */
	public static void storeSystemProperties(final OutputStream out,
			final String comments) throws IOException {
		synchronized(SYSTEM_PROPERTY_LOCK) {
			System.getProperties().store(out, comments);
		}
	}

	/**
	 * Set a system property.
	 * @param systemProperty <code>SystemUtil$SystemProperty</code>
	 * @param value <code>java.lang.String</code>
	 */	
	public static String setSystemProperty(SystemProperty systemProperty, String value) {
		synchronized(SYSTEM_PROPERTY_LOCK) {
			return System.setProperty(systemProperty.toString(), value);
		}
	}
	
	/**
	 * <b>Title:</b>  SystemProperty
	 * <br><b>Description:</b>  Provides an enum of commonly retrieved system
	 * properties
	 * @author raykroeker@gmail.com
	 * @version 1.0.0
	 */
	public static final class SystemProperty extends Enum {

		private static final long serialVersionUID = 1;

		/**
		 * Application Home
		 */
		public static final SystemProperty AppHome =
			new SystemProperty("app.home");
		
		/**
		 * Application Var
		 */			
		public static final SystemProperty AppVar =
			new SystemProperty("app.var");
		
		/**
		 * Config
		 */
		public static final SystemProperty Config =
			new SystemProperty("config");
		
		/**
		 * Display
		 */
		public static final SystemProperty Display =
			new SystemProperty("display");
		
		/**
		 * Home directory.
		 */
		public static final SystemProperty HomeDirectory =
			new SystemProperty("user.home");

		/**
		 * Line Separator
		 */
		public static final SystemProperty LineSeparator =
			new SystemProperty("line.separator");

		/**
		 * SysLog Ouptut File
		 */
		public static final SystemProperty SysLogFile =
			new SystemProperty("syslog.file");

		/**
		 * Temporary directory.
		 */
		public static final SystemProperty TempDirectory =
			new SystemProperty("java.io.tmpdir");

		/**
		 * Working/Startup Directory
		 */
		public static final SystemProperty WorkingDirectory =
			new SystemProperty("user.dir");

		/**
		 * Create a new SystemProperty
		 * @param systemProperty <code>java.lang.String</code>
		 */
		private SystemProperty(String systemProperty) {super(systemProperty);}
	}

	/**
	 * Create a new SystemUtil [Singleton]
	 */
	public SystemUtil() {super();}

}
