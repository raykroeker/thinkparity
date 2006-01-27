/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.util.persistence;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserPersistenceFactory {

	/**
	 * Singleton implementation.
	 * 
	 */
	private static final BrowserPersistenceFactory singleton;

	/**
	 * Singleton synchronzation lock.
	 * 
	 */
	private static final Object singletonLock;

	static {
		singleton = new BrowserPersistenceFactory(WorkspaceModel.getModel().getWorkspace());
		singletonLock = new Object();
	}

	/**
	 * Obtain a handle to a persistence helper for a class.
	 * 
	 * @param clasz
	 *            The class.
	 * @return The persistence helper.
	 */
	public static Persistence getPersistence(final Class clasz) {
		synchronized(singletonLock) { return singleton.getHelper(clasz); }
	}

	/**
	 * A cache of persistence helpers.
	 * 
	 */
	private final Map<Class, Persistence> cache;

	/**
	 * The parity workspace.
	 * 
	 */
	private final Workspace workspace;

	/**
	 * Create a BrowserPersistenceFactory. This singleton factory doles out
	 * custom persitence helpers for classes in the browser.
	 * 
	 * @param workspace
	 *            The parity model workspace.
	 */
	private BrowserPersistenceFactory(final Workspace workspace) {
		super();
		this.cache = new Hashtable<Class, Persistence>(7, 0.75F);
		this.workspace = workspace;
	}

	/**
	 * Create a persistence helper backed by java preferences for a given class.
	 * 
	 * @param clasz
	 *            The class.
	 * @return The persistence helper.
	 */
	private Persistence createHelper(final Class clasz) {
		final File javaFile = getFile(clasz);
		final Properties javaProperties = load(javaFile);
		// save the preferences on shutdown
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() { store(javaProperties, javaFile); }
		});
		return new Persistence() {
			public Dimension get(String key, Dimension defaultValue) {
				return new Dimension(
						get(key + ".width", defaultValue.width),
						get(key + ".height", defaultValue.height));
			}
			public int get(final String key, final int defaultValue) {
				try { return Integer.parseInt(javaProperties.getProperty(key, String.valueOf(defaultValue))); }
				catch(NumberFormatException nfx) {
					return defaultValue;
				}
			}
			public Point get(String key, Point defaultValue) {
				return new Point(
						get(key + ".x", defaultValue.x),
						get(key + ".y", defaultValue.y));
			}
			public String get(String key, String defaultValue) {
				return javaProperties.getProperty(key, defaultValue);
			}
			public void set(String key, Dimension value) {
				set(key + ".height", value.height);
				set(key + ".width", value.width);
			}
			public void set(String key, int value) {
				javaProperties.setProperty(key, String.valueOf(value));
			}
			public void set(String key, Point value) {
				set(key + ".x", value.x);
				set(key + ".y", value.y);
			}
			public void set(String key, String value) {
				javaProperties.setProperty(key, value);
			}
		};
	}

	/**
	 * Obtain the java file use by the class to persist.
	 * 
	 * @param clasz
	 *            The class.
	 * @return The java file.
	 */
	private File getFile(final Class clasz) {
		return new File(
				workspace.getWorkspaceURL().getFile(), clasz.getName() + ".xml");
	}

	private Persistence getHelper(final Class clasz) {
		Persistence helper = cache.get(clasz);
		if(null != helper) { return helper; }
		else { return createHelper(clasz); }
	}

	private void init(final File persistenceFile) throws IOException {
		if(!persistenceFile.exists()) {
			Assert.assertTrue("init", persistenceFile.createNewFile());
			final Properties javaProperties = new Properties();
			store(javaProperties, persistenceFile);
		}
	}

	/**
	 * Load the java properties from the preferences file.
	 * 
	 * @return The java properties.
	 */
	private Properties load(final File persistenceFile) {
		try {
			init(persistenceFile);
			final Properties javaProperties = new Properties();
			javaProperties.loadFromXML(new FileInputStream(persistenceFile));
			return javaProperties;
		}
		catch(FileNotFoundException fnfx) {
			fnfx.printStackTrace(System.err);
			return null;
		}
		catch(IOException iox) {
			iox.printStackTrace(System.err);
			return null;
		}
	}

	/**
	 * Store the java properties to the preferences file.
	 * 
	 * @param javaProperties
	 *            The java properties to store.
	 */
	private void store(final Properties javaProperties,
			final File persistenceFile) {
		try {
			javaProperties.storeToXML(new FileOutputStream(persistenceFile), "");
		}
		catch(FileNotFoundException fnfx) {
			fnfx.printStackTrace(System.err);
		}
		catch(IOException iox) {
			iox.printStackTrace(System.err);
		}
	}
}
