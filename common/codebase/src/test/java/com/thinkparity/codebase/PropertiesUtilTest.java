/*
 * Dec 10, 2005
 */
package com.thinkparity.codebase;

import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import com.thinkparity.codebase.StringUtil.Separator;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class PropertiesUtilTest extends CodebaseTestCase {

	private class Fixture {
		private final StringBuffer buffer;
		private final String comments;
		private final Properties properties;
		private Fixture(final StringBuffer buffer, final String comments,
				final Properties properties) {
			this.buffer = buffer;
			this.comments = comments;
			this.properties = properties;
		}
	}

	private Vector<Fixture> data;

	/**
	 * Create a PropertiesUtilTest.
	 */
	public PropertiesUtilTest() { super("PropertiesUtilTest"); }

	public void testPrint() {
		for(Fixture datum : data) {
			PropertiesUtil.print(datum.buffer, datum.comments, datum.properties);

			final String bufferString = datum.buffer.toString();
			PropertiesUtilTest.assertNotNull(bufferString);
			PropertiesUtilTest.assertTrue(bufferString.length() > 0);
			PropertiesUtilTest.assertTrue(bufferString.startsWith("# " + datum.comments));
			final Set<Object> keys = datum.properties.keySet();
			String keyColonValue;
			for(Object key : keys) {
				keyColonValue = new StringBuffer(key.toString())
					.append(Separator.FullColon)
					.append(datum.properties.getProperty(key.toString()))
					.toString();

				PropertiesUtilTest.assertTrue(bufferString.contains(keyColonValue));
			}
		}
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		data = new Vector<Fixture>(1);

		data.add(new Fixture(
				new StringBuffer(),
				"--- Test Property Printer ---",
				System.getProperties()));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}
}
