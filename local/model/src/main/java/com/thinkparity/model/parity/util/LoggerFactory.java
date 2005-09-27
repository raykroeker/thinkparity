/*
 * Feb 2, 2005
 */
package com.thinkparity.model.parity.util;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Appender;
import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;

import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;


/**
 * LoggerFactory
 * A singleton factory for creating rootLogger instances for this client.
 * @author raykroeker@gmail.com
 * @version 1.3
 */
public class LoggerFactory {

	/**
	 * Handle to the implementation of the factory.
	 */
	private static final LoggerFactory loggerFactory;

	/**
	 * Initialize the logger factory.
	 */
	static {
		try { loggerFactory = new LoggerFactory(); }
		catch(Exception x) {
			x.printStackTrace();
			throw new RuntimeException("Cannot initialize logger factory.", x);
		}
	}

	/**
	 * Obtain a handle to a rootLogger instance initialized for clasz.
	 * @param clasz <code>Class</code>
	 * @return <code>org.apache.log4j.Logger</code>
	 */
	public static Logger createInstance(Class clasz) {
		return loggerFactory.getLogger(clasz);
	}

	/**
	 * LoggerCategory
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private enum LoggerCategory {
		CODEBASE("com.raykroeker.codebase"),
		PARITY("com.thinkparity.model.parity"),
		RCP("com.thinkparity.model.parity.rcp"),
		ROOT("$"),
		SMACK("com.thinkparity.model.smack"),
		SMACKX("com.thinkparity.model.smackx"),
		XMPP("com.thinkparity.model.xmpp");

		/**
		 * Name to be used in log generation.
		 */
		private final String name;

		/**
		 * Create a LoggerCategory.
		 * @param name
		 */
		private LoggerCategory(final String name) { this.name = name; }

		/**
		 * Obtain name.
		 * @return String
		 */
		private String getName() { return name; }
	}

	/**
	 * LoggerFile
	 * Is a simple wrapper around the java File object which represents the
	 * output log file.
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private class LoggerFile {

		/**
		 * Represents the java File used for output.
		 */
		private final File file;

		/**
		 * Parity workspace.
		 */
		private final Workspace workspace;

		/**
		 * Create a LoggerFile.
		 */
		private LoggerFile() {
			super();
			this.workspace = WorkspaceModel.getModel().getWorkspace();
			this.file =
				new File(workspace.getLoggerURL().getFile(), "ParityLog.html");
		}

		/**
		 * Obtain the absolue path for the logger file.
		 * @return String
		 */
		private String getAbsolutePath() { return file.getAbsolutePath(); }
	}

	/**
	 * Handle to a logger file.
	 */
	private final LoggerFile loggerFile;

	/**
	 * Handle to the root logger.
	 */
	private final Logger rootLogger;

	/**
	 * LoggerFactory [Singleton]
	 */
	private LoggerFactory() throws IOException {
		super();
		this.loggerFile = createLoggerFile();
		this.rootLogger = createRootLogger();
	}

	/**
	 * Create an appender for use with all loggers.
	 * @return Appender
	 * @throws IOException
	 */
	private Appender createLoggerAppender() throws IOException {
		final RollingFileAppender rollingFileApender =
			new RollingFileAppender(
					createLoggerLayout(),
					loggerFile.getAbsolutePath(),
					true);
		rollingFileApender.setMaxBackupIndex(3);
		rollingFileApender.setMaxFileSize("3MB");
		return rollingFileApender;
	}

	/**
	 * Create an instance of LoggerFile.
	 * @return LoggerFile
	 */
	private LoggerFile createLoggerFile() { return new LoggerFile(); }

	/**
	 * Create a layout for use with all loggers.
	 * @return Layout
	 */
	private Layout createLoggerLayout() { return new HTMLLayout(); }

	private Logger createRootLogger() throws IOException {
		final Logger rootLogger = Logger.getRootLogger();
		rootLogger.removeAllAppenders();
		rootLogger.addAppender(createLoggerAppender());
		return rootLogger;
	}

	/**
	 * Obtain a handle to an instance of a logger.
	 * @param clasz
	 * @return Logger
	 */
	private Logger getLogger(final Class clasz) {
		return getLogger(getLoggerCategory(clasz));
	}

	/**
	 * Obtain an instance of a logger for a given category.
	 * @param loggerCategory
	 * @param appender
	 * @return Logger
	 */
	private Logger getLogger(final LoggerCategory loggerCategory) {
		if(loggerCategory == LoggerCategory.ROOT) { return rootLogger; }
		else { return Logger.getLogger(loggerCategory.toString()); }
	}

	/**
	 * Find the logger category for a given class.
	 * @param clasz
	 * @return LoggerFactory$LoggerCategory
	 */
	private LoggerCategory getLoggerCategory(final Class clasz) {
		final String claszName = clasz.getName();
		final LoggerCategory[] loggerCategories = LoggerCategory.values();
		for(int i = 0; i < loggerCategories.length; i++) {
			if(claszName.startsWith(loggerCategories[i].getName()))
				return loggerCategories[i];
		}
		return LoggerCategory.ROOT;
	}
}
