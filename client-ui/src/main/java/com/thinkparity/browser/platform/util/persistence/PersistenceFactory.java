/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.platform.util.persistence;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.model.ModelFactory;

import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class PersistenceFactory {

	/** Singleton implementation. */
	private static final PersistenceFactory SINGLETON;

	static { SINGLETON = new PersistenceFactory(); }

	/**
     * Obtain a parity persistence interface.
     * 
     * @param clasz
     *            A class.
     * @return The parity persistence interface.
     */
	public static Persistence getPersistence(final Class clasz) {
		synchronized(SINGLETON) { return SINGLETON.doGetPersistence(clasz); }
	}

	/** A cache of persistence helpers. */
	private final Map<Class, Persistence> cache;

	/** The properties. */
    private final Properties javaProperties;

	/** Create a PersistenceFactory. */
	private PersistenceFactory() {
		super();
        final ModelFactory modelFactory = ModelFactory.getInstance();
        final Workspace workspace = modelFactory.getWorkspace(PersistenceFactory.class);
        final File file = getFile(workspace);

		this.cache = new Hashtable<Class, Persistence>(7, 0.75F);
		this.javaProperties = load(file);

		// save the preferences on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() { store(javaProperties, file); }
        });
    }

	/**
     * Create the persistence backed by java preferences for a given class.
     * 
     * @param clasz
     *            A class.
     * @return The persistence.
     */
	private Persistence createPersistence(final Class clasz) {
		return new Persistence() {

            /** A context for keys. */
            private final String context = clasz.getName();

            public Boolean get(final String key, final Boolean defaultValue) {
				return Boolean.parseBoolean(get(key, defaultValue.toString()));
			}

            public Dimension get(String key, Dimension defaultValue) {
				return new Dimension(
						get(key + ".width", defaultValue.width),
						get(key + ".height", defaultValue.height));
			}

            public File get(final String key, final File defaultValue) {
                final String sValue = javaProperties.getProperty(key, null);
                if(null == sValue) { return defaultValue; }
                return new File(sValue);
            }

			public int get(final String key, final int defaultValue) {
				try {
                    return Integer.parseInt(
                            javaProperties.getProperty(
                                    contextualize(key),
                                    String.valueOf(defaultValue)));
                }
				catch(final NumberFormatException nfx) { return defaultValue; }
			}

			public Point get(String key, Point defaultValue) {
				return new Point(
						get(key + ".x", defaultValue.x),
						get(key + ".y", defaultValue.y));
			}

			public String get(String key, String defaultValue) {
				return javaProperties.getProperty(contextualize(key), defaultValue);
			}

			public void set(String key, Boolean value) {
				set(key, value.toString());
			}

			public void set(String key, Dimension value) {
				set(key + ".height", value.height);
				set(key + ".width", value.width);
			}

			public void set(final String key, final File value) {
                javaProperties.setProperty(key, value.getAbsolutePath());
            }

            public void set(String key, int value) {
				javaProperties.setProperty(contextualize(key), String.valueOf(value));
			}

			public void set(String key, Point value) {
				set(key + ".x", value.x);
				set(key + ".y", value.y);
			}

			public void set(String key, String value) {
				javaProperties.setProperty(contextualize(key), value);
			}

			/**
             * Contextualize the java property key.
             * @param key The java property key.
             * @return A context sensitive key.
             */
            private String contextualize(final String key) {
                return context + "." + key;
            }
		};
	}

	/**
     * Obtain the persistence for a given class.
     * 
     * @param clasz
     *            The class.
     * @return The persistence.
     */
	private Persistence doGetPersistence(final Class clasz) {
	    if(cache.containsKey(clasz)) { return cache.get(clasz); }
        else { return createPersistence(clasz); }
	}

    /**
	 * Obtain the java file use by the class to persist.
	 * 
	 * @return The java file.
	 */
	private File getFile(final Workspace workspace) {
		return new File(workspace.getWorkspaceURL().getFile(), "thinkParity.properties");
	}

    /**
     * Initialize a file.
     * 
     * @param file
     *            The file.
     * @throws IOException
     */
	private void init(final File file) throws IOException {
		if(!file.exists()) {
			Assert.assertTrue(
                    "[LBROWSER] [PLATFORM] [UTIL] [PERSISTENCE FACTORY INIT] [CANNOT CREATE PERSISTENCE FILE]",
                    file.createNewFile());
		}
	}

	/**
     * Load properties.
     * 
     * @param file
     *            The file.
     * @return The properties.
     */
	private Properties load(final File file) {
		try { init(file); }
		catch(final IOException iox) { throw new RuntimeException(iox); }

        final Properties properties = new Properties();
		try { properties.load(new FileInputStream(file)); }
		catch(final IOException iox) { throw new RuntimeException(iox); }

        return properties;
	}

    /**
     * Store properties.
     * 
     * @param properties
     *            The properties.
     * @param file
     *            The file.
     */
	private void store(final Properties properties, final File file) {
		try { properties.store(new FileOutputStream(file), ""); }
		catch(final IOException iox) { throw new RuntimeException(iox); }
	}
}
