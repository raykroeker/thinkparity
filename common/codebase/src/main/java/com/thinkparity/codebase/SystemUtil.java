/*
 * Oct 11, 2003
 */
package com.thinkparity.codebase;


/**
 * <b>Title:</b>  SystemUtil
 * <br><b>Description:</b>  
 * @author raykroeker@gmail.com
 * @version 1.0.0
 */
public final class SystemUtil {

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
		 * Line Separator
		 */
		public static final SystemProperty LineSeparator =
			new SystemProperty("line.separator");
		
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
		 * Create a new SystemProperty
		 * @param systemProperty <code>java.lang.String</code>
		 */
		private SystemProperty(String systemProperty) {super(systemProperty);}
	}

	/**
	 * Create a new SystemUtil [Singleton]
	 */
	public SystemUtil() {super();}

	/**
	 * Obtain a system property
	 * @param systemProperty <code>SystemUtil$SystemProperty</code>
	 * @return <code>java.lang.String</code>
	 */
	public static synchronized String getSystemProperty(SystemProperty systemProperty) {
		return System.getProperty(systemProperty.toString());
	}
	
	/**
	 * Obtain a system proeprty
	 * @param systemProperty <code>SystemUtil$SystemProperty</code>
	 * @param defaultValue <code>java.lang.String</code>
	 * @return <code>java.lang.String</code>
	 */
	public static synchronized String getSystemProperty(SystemProperty systemProperty, String defaultValue) {
		return System.getProperty(systemProperty.toString(), defaultValue);
	}

	/**
	 * Set a system property.
	 * @param systemProperty <code>SystemUtil$SystemProperty</code>
	 * @param value <code>java.lang.String</code>
	 */	
	public static synchronized void setSystemProperty(SystemProperty systemProperty, String value) {
		System.setProperty(systemProperty.toString(), value);
	}

}
